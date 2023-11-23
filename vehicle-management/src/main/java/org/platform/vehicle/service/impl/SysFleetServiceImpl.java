package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.conf.CustomerContext;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.constant.SysCustomerConstant;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.SysCustomerUser;
import org.platform.vehicle.entity.SysFleetNotice;
import org.platform.vehicle.entity.SysNoticeConfig;
import org.platform.vehicle.entity.SysUser;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.entity.WxUserInfo;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.SysCustomerUserMapper;
import org.platform.vehicle.mapper.SysFleetNoticeMapper;
import org.platform.vehicle.mapper.SysNoticeConfigMapper;
import org.platform.vehicle.mapper.SysRoleMenuMapper;
import org.platform.vehicle.mapper.SysUserMapper;
import org.platform.vehicle.mapper.SysUserRoleMapper;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.VehicleSpecMapper;
import org.platform.vehicle.mapper.WxUserInfoMapper;
import org.platform.vehicle.param.FleetConditionQuery;
import org.platform.vehicle.param.FleetRemindSettingParam;
import org.platform.vehicle.param.FleetWxUserInfo;
import org.platform.vehicle.param.SysFleetAddParam;
import org.platform.vehicle.param.SysFleetEditParam;
import org.platform.vehicle.service.SysFleetService;
import org.platform.vehicle.service.SysRoleService;
import org.platform.vehicle.service.SysUserService;
import org.platform.vehicle.util.OauthUtil;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.vo.FleetRemindSettingVo;
import org.platform.vehicle.vo.SysFleetDetailVo;
import org.platform.vehicle.vo.SysFleetVo;
import org.platform.vehicle.vo.context.CustomerContextVo;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author gejiawei
 * @Date 2023/8/30 10:05
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysFleetServiceImpl implements SysFleetService {

    private final SysCustomerMapper sysCustomerMapper;
    private final SysFleetNoticeMapper sysFleetNoticeMapper;
    private final SysNoticeConfigMapper sysNoticeConfigMapper;
    private final SysUserMapper sysUserMapper;
    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final OperateLogAsync operateLogAsync;
    private final VehicleMapper vehicleMapper;
    private final VehicleSpecMapper vehicleSpecMapper;
    private final SysCustomerUserMapper sysCustomerUserMapper;
    private final WxUserInfoMapper wxUserInfoMapper;
    private final OauthUtil oauthUtil;
    private final CustomerContext customerContext;


    /**
     * 车队档案-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse conditionQuery(FleetConditionQuery param) {
        UserVo user = UserContext.getUser();
        Page<SysCustomer> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<SysCustomer> wrapper = this.getFleetWrapper(param);
        Page<SysCustomer> sysFleetPage = sysCustomerMapper.selectPage(page, wrapper);
        List<SysCustomer> sysFleetList = sysFleetPage.getRecords();
        if (sysFleetList.isEmpty()) {
            return BaseResponse.ok(new ArrayList<>(), page);
        }
        // 查询客户信息
        List<SysCustomer> sysClientList = this.getClientList(sysFleetList);
        // 查询上级车队信息
        List<SysCustomer> sysParentFleetList = this.getParentFleetList(
                sysFleetList);
        List<SysFleetVo> sysFleetVoList = new ArrayList<>();
        for (SysCustomer fleet : sysFleetList) {
            SysFleetVo sysFleetVo = this.convertToFleetVo(fleet, sysClientList, sysParentFleetList);
            sysFleetVoList.add(sysFleetVo);
        }
        return BaseResponse.ok(sysFleetVoList, page);
    }

    /**
     * 车队档案-新增
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse addFleet(SysFleetAddParam param, HttpServletRequest request) {
        // 当前登录人信息
        UserVo user = UserContext.getUser();
        // 保存车队信息
        SysCustomer sysCustomer = this.saveFleet(param, user);
        // 保存车队提醒设置
        this.saveFleetNotice(param.getRemindSettingList(), sysCustomer);
        operateLogAsync.pushAddLog(
                sysCustomer,
                OperateModuleEnum.ARCHIVE_FLEET_ADD,
                String.valueOf(sysCustomer.getId()),
                user,
                request);
        oauthUtil.refresh();
        this.updateCustomerContext(sysCustomer);
        return BaseResponse.ok();
    }

    /**
     * 车队档案-修改
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse editFleet(SysFleetEditParam param, HttpServletRequest request) {
        // 当前登录人信息
        UserVo user = UserContext.getUser();
        // 校验车队名称
        SysCustomer sysFleet = this.checkFleetInfo(param);
        // 修改车队信息
        SysCustomer newSysFleet = this.editFleet(param, sysFleet, user);
        this.editFleetNotice(param, sysFleet);
        // 保存日志
        operateLogAsync.pushEditLog(
                SysCustomer.class,
                sysFleet,
                newSysFleet,
                OperateModuleEnum.ARCHIVE_FLEET_EDIT,
                String.valueOf(sysFleet.getId()),
                user,
                request);
        this.updateCustomerContext(sysFleet);
        return BaseResponse.ok();
    }

    private void updateCustomerContext(SysCustomer customer) {
        CustomerContextVo customerContextVo = new CustomerContextVo();
        customerContextVo.setId(customer.getId());
        customerContextVo.setName(customer.getName());
        customerContextVo.setCompanyId(customer.getCompanyId());
        customerContextVo.setType(customer.getType());
        customerContext.add(customerContextVo);
    }

    /**
     * 车队档案-删除
     *
     * @param id
     * @param phone
     * @param request
     * @return
     */
    @Override
    public BaseResponse deleteFleet(Integer id, String phone, HttpServletRequest request) {
        // 当前登录人信息
        UserVo user = UserContext.getUser();
        // 校验手机号
        if (!user.getPhone().equals(phone)) {
            return BaseResponse.failure("手机号错误，删除操作未执行");
        }
        // 查询车队信息
        List<Integer> customerTypeList = new ArrayList<>();
        customerTypeList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
        customerTypeList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
        SysCustomer sysFleet = sysCustomerMapper.selectOne(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, id)
                        .in(SysCustomer::getType, customerTypeList)
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (sysFleet == null) {
            throw new BaseException("99999", "车队信息不存在");
        }
        // 查询是否存在下次车队
        Long childrenFleetCount = sysCustomerMapper.selectCount(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getParentId, sysFleet.getId())
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (childrenFleetCount > 0) {
            return BaseResponse.failure("当前车队存在下级车队,不允许删除");
        }
        Long vehicleCount = vehicleMapper.selectCount(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getCustomerId, sysFleet.getId())
                .eq(VehicleEntity::getIsDeleted, SysCustomerConstant.NOT_DELETED));
        if (vehicleCount > 0) {
            return BaseResponse.failure("当前车队存在车辆档案数据,不允许删除");
        }
        Long vehicleSpecCount = vehicleSpecMapper.selectCount(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .eq(VehicleSpecEntity::getCustomerId, sysFleet.getId())
                        .eq(VehicleSpecEntity::getIsDeleted, SysCustomerConstant.NOT_DELETED));
        if (vehicleSpecCount > 0) {
            return BaseResponse.failure("当前车队存在车辆轮胎数据,不允许删除");
        }
        // 删除车队信息
        SysCustomer sysCustomer = new SysCustomer();
        sysCustomer.setId(id);
        sysCustomer.setIsDelete(SysCustomerConstant.DELETED);
        sysCustomer.setUpdatePerson(user.getName());
        // 保存日志
        operateLogAsync.pushDeleteLog(
                OperateModuleEnum.ARCHIVE_FLEET_DELETE,
                String.valueOf(sysCustomer.getId()),
                user,
                request,
                String.valueOf(sysCustomer.getName()));
        this.deleteCustomerContext(sysCustomer);
        return BaseResponse.ok();
    }

    private void deleteCustomerContext(SysCustomer updateParam) {
        customerContext.remove(updateParam.getId());
    }

    /**
     * 车队档案-查看车队详情
     *
     * @param fleetId
     * @return
     */
    @Override
    public BaseResponse getFleetDetail(Integer fleetId) {
        // 查询车队信息
        List<Integer> customerTypeList = new ArrayList<>();
        customerTypeList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
        customerTypeList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
        SysCustomer sysFleet = sysCustomerMapper.selectOne(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, fleetId)
                        .in(SysCustomer::getType, customerTypeList)
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (sysFleet == null) {
            throw new BaseException("99999", "车队信息不存在");
        }
        // 查询客户信息
        SysCustomer sysClient = sysCustomerMapper.selectOne(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, sysFleet.getCompanyId())
                        .eq(SysCustomer::getType, SysCustomerConstant.CLIENT_TYPE));
        // 查询上级车队信息
        SysCustomer parentFleet = sysCustomerMapper.selectOne(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, sysFleet.getParentId())
                        .eq(SysCustomer::getType, SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE));
        // 查询车队提醒设置
        List<SysFleetNotice> sysFleetNoticeList = sysFleetNoticeMapper.selectList(
                new LambdaQueryWrapper<SysFleetNotice>()
                        .eq(SysFleetNotice::getCustomerId, sysFleet.getId()));
        SysFleetDetailVo sysFleetDetailVo = new SysFleetDetailVo();
        sysFleetDetailVo.setId(sysFleet.getId());
        sysFleetDetailVo.setName(sysFleet.getName());
        sysFleetDetailVo.setClientId(sysClient.getId());
        sysFleetDetailVo.setClientName(sysClient.getName());
        if (parentFleet != null) {
            sysFleetDetailVo.setParentFleetId(parentFleet.getId());
            sysFleetDetailVo.setParentFleetName(parentFleet.getName());
        }
        sysFleetDetailVo.setContactName(sysFleet.getContactName());
        sysFleetDetailVo.setContactPhone(sysFleet.getContactPhone());
        sysFleetDetailVo.setProvince(sysFleet.getProvince());
        sysFleetDetailVo.setProvinceId(sysFleet.getProvinceId());
        sysFleetDetailVo.setCity(sysFleet.getCity());
        sysFleetDetailVo.setCityId(sysFleet.getCityId());
        sysFleetDetailVo.setCounty(sysFleet.getCounty());
        sysFleetDetailVo.setCountyId(sysFleet.getCountyId());
        sysFleetDetailVo.setAddress(sysFleet.getAddressDetail());
        sysFleetDetailVo.setStatus(sysFleet.getStatus());
        List<FleetRemindSettingVo> fleetRemindSettingVoList = new ArrayList<>();
        for (SysFleetNotice sysFleetNotice : sysFleetNoticeList) {
            // 查询提醒配置
            SysNoticeConfig sysNoticeConfig = sysNoticeConfigMapper.selectById(
                    sysFleetNotice.getNoticeConfigId());
            FleetRemindSettingVo fleetRemindSettingVo = new FleetRemindSettingVo();
            fleetRemindSettingVo.setNoticeConfigId(sysNoticeConfig.getId());
            fleetRemindSettingVo.setNoticeConfigName(sysNoticeConfig.getName());
            fleetRemindSettingVo.setRemindType(sysFleetNotice.getRemindType());
            fleetRemindSettingVo.setReceivedAccount(sysFleetNotice.getReceivedAccount());
            fleetRemindSettingVoList.add(fleetRemindSettingVo);
        }
        sysFleetDetailVo.setFleetRemindSettingVoList(fleetRemindSettingVoList);
        return BaseResponse.ok(sysFleetDetailVo);
    }

    /**
     * 获取当前登录人所属客户列表
     *
     * @return
     */
    @Override
    public BaseResponse<List<SysFleetVo>> getClientList() {
        // 当前登录人
        UserVo user = UserContext.getUser();
        List<SysCustomer> clientList = new ArrayList<>();
        // 面心用户查询所有客户
        if (user.getCustomerType().equals(SysCustomerConstant.PARENT_TYPE)) {
            List<SysCustomer> allClientList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .eq(SysCustomer::getType, SysCustomerConstant.CLIENT_TYPE)
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            return BaseResponse.ok(allClientList);
        }
        // 非面心用户根据用户customerId查询
        SysCustomer sysCustomer = sysCustomerMapper.selectOne(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, user.getCustomerId())
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (sysCustomer == null) {
            return BaseResponse.ok(new ArrayList<>());
        }
        // 如果是客户直接返回
        if (sysCustomer.getType().equals(SysCustomerConstant.CLIENT_TYPE)) {
            clientList.add(sysCustomer);
            return BaseResponse.ok(clientList);
        }
        SysCustomer client = sysCustomerMapper.selectOne(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, sysCustomer.getCompanyId())
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (client != null) {
            clientList.add(client);
        }
        return BaseResponse.ok(clientList);
    }

    /**
     * 获取当前登录人上级车队列表
     *
     * @return
     */
    @Override
    public BaseResponse<List<SysFleetVo>> getParentFleetList(Integer clientId) {
        // 当前登录人
        UserVo user = UserContext.getUser();
        // 面心用户查询所有一级车队
        if (user.getCustomerType().equals(SysCustomerConstant.PARENT_TYPE)) {
            List<SysCustomer> allParentFleetList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .eq(SysCustomer::getCompanyId, clientId)
                            .eq(SysCustomer::getType, SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE)
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            return BaseResponse.ok(allParentFleetList);
        }
        // 非面心用户根据用户customerId查询
        SysCustomer sysCustomer = sysCustomerMapper.selectOne(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, user.getCustomerId())
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (sysCustomer == null) {
            return BaseResponse.ok(new ArrayList<>());
        }
        // 当前用户所属客户,则查询客户下所有一级车队
        if (sysCustomer.getType().equals(SysCustomerConstant.CLIENT_TYPE)) {
            List<SysCustomer> parentFleetList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .eq(SysCustomer::getCompanyId, sysCustomer.getId())
                            .eq(SysCustomer::getType, SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE)
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            return BaseResponse.ok(parentFleetList);
        }
        // 当前用户所属一级车队,则返回该一级车队
        if (sysCustomer.getType().equals(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE)) {
            List<SysCustomer> parentFleetList = new ArrayList<>();
            parentFleetList.add(sysCustomer);
            return BaseResponse.ok(parentFleetList);
        }
        // 当前用户属于二级车队,返回所属上级车队
        if (sysCustomer.getType().equals(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE)) {
            SysCustomer parentFleet = sysCustomerMapper.selectOne(
                    new LambdaQueryWrapper<SysCustomer>()
                            .eq(SysCustomer::getId, sysCustomer.getParentId())
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            if (parentFleet != null) {
                List<SysCustomer> parentFleetList = new ArrayList<>();
                parentFleetList.add(parentFleet);
                return BaseResponse.ok(parentFleetList);
            }
        }
        return BaseResponse.ok(new ArrayList<>());

    }

    /**
     * 获取当前用户及下属已绑定微信的用户(通知接收人)
     *
     * @return
     */
    @Override
    public BaseResponse<List<FleetWxUserInfo>> getwxUser() {
        UserVo user = UserContext.getUser();
        List<Integer> customerIds = user.getCustomerIds();
        // 查询车队用户
        List<SysCustomerUser> sysCustomerUserList = sysCustomerUserMapper.selectList(
                new LambdaQueryWrapper<SysCustomerUser>()
                        .in(SysCustomerUser::getCustomerId, customerIds));
        List<Integer> userIdList = new ArrayList<>();
        for (SysCustomerUser sysCustomerUser : sysCustomerUserList) {
            userIdList.add(sysCustomerUser.getUserId());
        }
        // 查询用户信息
        List<SysUser> sysUserList = sysUserMapper.selectList(
                new LambdaQueryWrapper<SysUser>()
                        .in(SysUser::getId, userIdList)
                        .eq(SysUser::getIsDelete, SysCustomerConstant.NOT_DELETED));
        // 查询微信用户信息
        List<WxUserInfo> wxUserInfoList = wxUserInfoMapper.selectList(
                new LambdaQueryWrapper<WxUserInfo>()
                        .in(WxUserInfo::getUserId, userIdList));
        List<FleetWxUserInfo> fleetUserInfoList = new ArrayList<>();
        for (SysUser sysUser : sysUserList) {
            for (WxUserInfo wxUserInfo : wxUserInfoList) {
                if (sysUser.getId().equals(wxUserInfo.getUserId())) {
                    FleetWxUserInfo fleetUserInfo = new FleetWxUserInfo();
                    fleetUserInfo.setAccount(sysUser.getAccount());
                    fleetUserInfo.setName(sysUser.getName());
                    fleetUserInfo.setPhone(sysUser.getPhone());
                    fleetUserInfo.setOpenId(wxUserInfo.getOpenId());
                    fleetUserInfoList.add(fleetUserInfo);
                }
            }
        }
        return BaseResponse.ok(fleetUserInfoList);
    }

    private SysCustomer checkFleetInfo(SysFleetEditParam param) {
        Long count = sysCustomerMapper.selectCount(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getName, param.getName())
                        .eq(SysCustomer::getCompanyId, param.getClientId())
                        .ne(SysCustomer::getId, param.getId())
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (count > 0) {
            throw new BaseException("99999", "车队名称已存在");
        }
        // 查询车队信息
        List<Integer> customerTypeList = new ArrayList<>();
        customerTypeList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
        customerTypeList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
        SysCustomer sysFleet = sysCustomerMapper.selectOne(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, param.getId())
                        .in(SysCustomer::getType, customerTypeList)
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (sysFleet == null) {
            throw new BaseException("99999", "车队信息不存在");
        }
        return sysFleet;
    }

    private void editFleetNotice(SysFleetEditParam param, SysCustomer sysFleet) {
        // 删除车队提醒设置
        sysFleetNoticeMapper.delete(
                new LambdaQueryWrapper<SysFleetNotice>()
                        .eq(SysFleetNotice::getCustomerId, param.getId()));
        // 保存车队提醒设置
        this.saveFleetNotice(param.getRemindSettingList(), sysFleet);
    }

    private SysCustomer editFleet(SysFleetEditParam param, SysCustomer sysFleet, UserVo user) {
        Integer parentFleetId = param.getParentFleetId();
        int type = 0;
        int parentId = 0;
        // 上级车队ID为空则创建一级车队
        if (param.getType() == SysCustomerConstant.CREATE_FIRST_LEVEL_FLEET) {
            type = SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE;
            parentId = param.getClientId();
        } else if (param.getType() == SysCustomerConstant.CREATE_SECOND_LEVEL_FLEET) {
            type = SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE;
            parentId = parentFleetId;
        } else {
            throw new BaseException("99999", "创建车队类型错误");
        }
        SysCustomer fleetEditParam = new SysCustomer();
        fleetEditParam.setId(sysFleet.getId());
        fleetEditParam.setName(param.getName());
        fleetEditParam.setCompanyId(param.getClientId());
        fleetEditParam.setContactName(param.getContactName());
        fleetEditParam.setContactPhone(param.getContactPhone());
        fleetEditParam.setProvince(param.getProvince());
        fleetEditParam.setProvinceId(param.getProvinceId());
        fleetEditParam.setCity(param.getCity());
        fleetEditParam.setCityId(param.getCityId());
        fleetEditParam.setCounty(param.getCounty());
        fleetEditParam.setCountyId(param.getCountyId());
        fleetEditParam.setAddressDetail(param.getAddress());
        fleetEditParam.setType(type);
        fleetEditParam.setStatus(param.getStatus());
        fleetEditParam.setUpdatePerson(user.getName());
        fleetEditParam.setParentId(parentId);
        sysCustomerMapper.updateById(fleetEditParam);
        return fleetEditParam;
    }

    private void saveFleetNotice(List<FleetRemindSettingParam> remindSettingList,
            SysCustomer sysCustomer) {
        if (remindSettingList != null && !remindSettingList.isEmpty()) {
            for (FleetRemindSettingParam fleetRemindSettingParam : remindSettingList) {
                //查询提醒配置是否存在
                Long count = sysNoticeConfigMapper.selectCount(
                        new LambdaQueryWrapper<SysNoticeConfig>()
                                .eq(SysNoticeConfig::getId,
                                        fleetRemindSettingParam.getNoticeConfigId()));
                if (count == 0) {
                    throw new BaseException("99999", "提醒配置不存在");
                }
                SysFleetNotice sysFleetNotice = new SysFleetNotice();
                sysFleetNotice.setNoticeConfigId(fleetRemindSettingParam.getNoticeConfigId());
                sysFleetNotice.setCustomerId(sysCustomer.getId());
                sysFleetNotice.setRemindType(fleetRemindSettingParam.getRemindType());
                sysFleetNotice.setReceivedAccount(fleetRemindSettingParam.getReceivedAccount());
                sysFleetNoticeMapper.insert(sysFleetNotice);
            }
        }
    }

    private SysCustomer saveFleet(SysFleetAddParam param, UserVo user) {
        Integer parentFleetId = param.getParentFleetId();
        // 车队名称校验
        Long count = sysCustomerMapper.selectCount(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getName, param.getName())
                        .eq(SysCustomer::getCompanyId, param.getClientId())
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (count > 0) {
            throw new BaseException("99999", "车队名称已存在");
        }
        int type = 0;
        int parentId = 0;
        // 上级车队ID为空则创建一级车队
        if (param.getType() == SysCustomerConstant.CREATE_FIRST_LEVEL_FLEET) {
            type = SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE;
            parentId = param.getClientId();
        } else if (param.getType() == SysCustomerConstant.CREATE_SECOND_LEVEL_FLEET) {
            type = SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE;
            parentId = parentFleetId;
        } else {
            throw new BaseException("99999", "创建车队类型错误");
        }
        SysCustomer sysCustomer = new SysCustomer();
        sysCustomer.setName(param.getName());
        sysCustomer.setCompanyId(param.getClientId());
        sysCustomer.setContactName(param.getContactName());
        sysCustomer.setContactPhone(param.getContactPhone());
        sysCustomer.setProvince(param.getProvince());
        sysCustomer.setProvinceId(param.getProvinceId());
        sysCustomer.setCity(param.getCity());
        sysCustomer.setCityId(param.getCityId());
        sysCustomer.setCounty(param.getCounty());
        sysCustomer.setCountyId(param.getCountyId());
        sysCustomer.setAddressDetail(param.getAddress());
        sysCustomer.setType(type);
        sysCustomer.setCreatePerson(user.getName());
        sysCustomer.setUpdatePerson(user.getName());
        sysCustomer.setParentId(parentId);
        sysCustomerMapper.insert(sysCustomer);
        return sysCustomer;
    }

    private SysFleetVo convertToFleetVo(SysCustomer fleet, List<SysCustomer> sysClientList,
            List<SysCustomer> sysParentFleetList) {
        SysCustomer sysClient = this.getClient(fleet, sysClientList);
        SysCustomer parentFleet = this.getParentFleet(fleet, sysParentFleetList);
        SysFleetVo sysFleetVo = new SysFleetVo();
        sysFleetVo.setId(fleet.getId());
        sysFleetVo.setName(fleet.getName());
        sysFleetVo.setStatus(fleet.getStatus());
        if (sysClient != null) {
            sysFleetVo.setParentClientId(sysClient.getId());
            sysFleetVo.setParentClientName(sysClient.getName());
        }
        if (parentFleet != null) {
            sysFleetVo.setParentFleetId(parentFleet.getId());
            sysFleetVo.setParentFleetName(parentFleet.getName());
        }
        sysFleetVo.setContactName(fleet.getContactName());
        sysFleetVo.setContactPhone(fleet.getContactPhone());
        sysFleetVo.setType(fleet.getType());
        sysFleetVo.setUpdatePerson(fleet.getUpdatePerson());
        sysFleetVo.setUpdateTime(fleet.getUpdateTime());
        return sysFleetVo;
    }

    private SysCustomer getParentFleet(SysCustomer fleet,
            List<SysCustomer> sysParentFleetList) {
        SysCustomer parentFleet = null;
        for (SysCustomer temp : sysParentFleetList) {
            if (fleet.getParentId().equals(temp.getId())) {
                parentFleet = temp;
                break;
            }
        }
        return parentFleet;
    }

    private List<SysCustomer> getParentFleetList(List<SysCustomer> sysFleetList) {
        Set<Integer> parentFleetIdSet = new HashSet<>();
        for (SysCustomer sysFleet : sysFleetList) {
            parentFleetIdSet.add(sysFleet.getParentId());
        }
        List<SysCustomer> sysParentFleetList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .in(SysCustomer::getId, parentFleetIdSet)
                        .eq(SysCustomer::getType, SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE)
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        return sysParentFleetList;
    }

    private SysCustomer getClient(SysCustomer fleet, List<SysCustomer> sysClientList) {
        SysCustomer sysClient = null;
        for (SysCustomer temp : sysClientList) {
            if (fleet.getCompanyId().equals(temp.getId())) {
                sysClient = temp;
                break;
            }
        }
        return sysClient;
    }

    private List<SysCustomer> getClientList(List<SysCustomer> sysFleetList) {
        List<Integer> companyIdList = new ArrayList<>();
        for (SysCustomer sysFleet : sysFleetList) {
            companyIdList.add(sysFleet.getCompanyId());
        }
        List<SysCustomer> sysClientList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .in(SysCustomer::getId, companyIdList)
                        .eq(SysCustomer::getType, SysCustomerConstant.CLIENT_TYPE)
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        return sysClientList;
    }

    private LambdaQueryWrapper<SysCustomer> getFleetWrapper(FleetConditionQuery param) {
        UserVo user = UserContext.getUser();
        LambdaQueryWrapper<SysCustomer> wrapper = new LambdaQueryWrapper<>();
//        // 当前用户属于客户,则查询该客户下所有车队
//        if (user.getCustomerType() == SysCustomerConstant.CLIENT_TYPE) {
//            wrapper.eq(SysCustomer::getCompanyId, user.getCustomerId());
//        } else if (user.getCustomerType() == SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE) {
//            // 当前用户属于一级车队,则查询该一级车队下所有二级车队
//            wrapper.eq(SysCustomer::getParentId, user.getCustomerId());
//        } else if (user.getCustomerType() == SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE) {
//            // 当前用户属于二级车队,则查询该二级车队
//            wrapper.eq(SysCustomer::getId, user.getCustomerId());
//        }
        wrapper.in(SysCustomer::getId, user.getCustomerIds());
        if (StringUtils.isNotBlank(param.getName())) {
            wrapper.like(SysCustomer::getName, param.getName());
        }
        if (StringUtils.isNotBlank(param.getParentName())) {
            // 模糊查询上级车队
            List<SysCustomer> parentFleetList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .like(SysCustomer::getName, param.getParentName())
                            .eq(SysCustomer::getType, SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE)
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED)
                            .select(SysCustomer::getId));
            List<Integer> parentFleetId = new ArrayList<>();
            for (SysCustomer fleet : parentFleetList) {
                parentFleetId.add(fleet.getId());
            }
            if (parentFleetId.isEmpty()) {
                parentFleetId.add(-1);
            }
            wrapper.in(SysCustomer::getParentId, parentFleetId);
        }
        List<Integer> typeList = new ArrayList<>();
        typeList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
        typeList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
        wrapper.in(SysCustomer::getType, typeList);
        wrapper.eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED);
        wrapper.orderByDesc(SysCustomer::getUpdateTime);
        return wrapper;
    }

}
