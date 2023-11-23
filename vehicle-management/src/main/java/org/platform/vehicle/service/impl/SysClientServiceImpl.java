package org.platform.vehicle.service.impl;

import static org.platform.vehicle.constant.SysCustomerConstant.CLIENT_TYPE;
import static org.platform.vehicle.constant.SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE;
import static org.platform.vehicle.constant.SysCustomerConstant.PARENT_TYPE;
import static org.platform.vehicle.constant.SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Arrays;
import org.platform.vehicle.conf.CustomerContext;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.constant.SysCustomerConstant;
import org.platform.vehicle.constant.SysRoleConstant;
import org.platform.vehicle.constant.SysUserConstant;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.SysCustomerUser;
import org.platform.vehicle.entity.SysRole;
import org.platform.vehicle.entity.SysRoleMenu;
import org.platform.vehicle.entity.SysUser;
import org.platform.vehicle.entity.SysUserRole;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.SysCustomerUserMapper;
import org.platform.vehicle.mapper.SysRoleMenuMapper;
import org.platform.vehicle.mapper.SysUserMapper;
import org.platform.vehicle.mapper.SysUserRoleMapper;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.VehicleSpecMapper;
import org.platform.vehicle.param.ClientAddParam;
import org.platform.vehicle.param.ClientConditionQuery;
import org.platform.vehicle.param.ClientEditParam;
import org.platform.vehicle.param.SysRoleAddParam;
import org.platform.vehicle.param.SysRoleEditParam;
import org.platform.vehicle.param.SysUserAddParam;
import org.platform.vehicle.param.SysUserEditParam;
import org.platform.vehicle.service.SysClientService;
import org.platform.vehicle.service.SysRoleService;
import org.platform.vehicle.service.SysUserService;
import org.platform.vehicle.util.OauthUtil;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.vo.ClientDetailVo;
import org.platform.vehicle.vo.ClientNameVo;
import org.platform.vehicle.vo.SimpleCustomerVo;
import org.platform.vehicle.vo.SimpleFleetVo;
import org.platform.vehicle.vo.SysClientVo;
import org.platform.vehicle.vo.SysCustomerTreeVo;
import org.platform.vehicle.vo.context.CustomerContextVo;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.response.ResponseEnum;
import org.platform.vehicle.utils.DateUtil;
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
 * @Date 2023/8/28 15:59
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysClientServiceImpl implements SysClientService {

    private final SysCustomerMapper sysCustomerMapper;
    private final SysUserMapper sysUserMapper;
    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysCustomerUserMapper sysCustomerUserMapper;
    private final OperateLogAsync operateLogAsync;
    private final VehicleMapper vehicleMapper;
    private final VehicleSpecMapper vehicleSpecMapper;
    private final OauthUtil oauthUtil;
    private final CustomerContext customerContext;


    /**
     * 客户档案-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse conditionQuery(ClientConditionQuery param) {
        Page<SysCustomer> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<SysCustomer> wrapper = this.getClientWrapper(param);
        Page<SysCustomer> sysCustomerPage = sysCustomerMapper.selectPage(page, wrapper);
        List<SysCustomer> sysCustomerList = sysCustomerPage.getRecords();
        List<SysUser> sysUserList = this.getSysUserList(sysCustomerList);
        List<SysClientVo> sysClientVoList = new ArrayList<>();
        for (SysCustomer sysCustomer : sysCustomerList) {
            SysUser clientUser = this.getAdminUser(sysCustomer, sysUserList);
            SysClientVo sysClientVo = this.getSysClientVo(sysCustomer, clientUser);
            sysClientVoList.add(sysClientVo);
        }
        return BasePageResponse.ok(sysClientVoList, page);
    }

    /**
     * 客户档案-新增客户
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse addClient(ClientAddParam param, HttpServletRequest request) {
        // 获取当前用户
        UserVo user = UserContext.getUser();
        // 客户名称校验
        String clientName = param.getName();
        Long count = sysCustomerMapper.selectCount(new LambdaQueryWrapper<SysCustomer>()
                .eq(SysCustomer::getName, clientName)
                .eq(SysCustomer::getType, SysCustomerConstant.CLIENT_TYPE)
                .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (count > 0) {
            return BaseResponse.failure("客户名称已存在");
        }
        // 保存客户信息
        SysCustomer sysClient = this.saveClient(param, user);
        // 创建客户管理员角色
        SysRole sysRole = this.saveSysRole(param, sysClient, request);
        // 新增客户管理员账号
        SysUser sysUser = this.saveSysUser(param, sysClient, sysRole, request);
        // 根据客户company_id
        this.updateSysClientCompanyId(sysClient);
        // 更新客户管理员账号
        this.updateClientAdminUser(sysClient, sysUser);
        operateLogAsync.pushAddLog(sysUser, OperateModuleEnum.ARCHIVE_CLIENT_ADD,
                String.valueOf(sysUser.getId()), user,
                request);
        oauthUtil.refresh();
        this.updateCustomerContext(sysClient);
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

    private void updateSysClientCompanyId(SysCustomer sysClient) {
        SysCustomer updateParam = new SysCustomer();
        updateParam.setId(sysClient.getId());
        updateParam.setCompanyId(sysClient.getId());
        sysCustomerMapper.updateById(updateParam);
    }

    /**
     * 客户档案-修改客户
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse editClient(ClientEditParam param, HttpServletRequest request) {
        // 获取当前用户
        UserVo user = UserContext.getUser();
        // 查询客户
        SysCustomer sysCustomer = sysCustomerMapper.selectById(param.getId());
        if (sysCustomer == null) {
            return BaseResponse.failure("客户不存在");
        }
        // 校验客户名称重复
        String clientName = param.getName();
        Long count = sysCustomerMapper.selectCount(new LambdaQueryWrapper<SysCustomer>()
                .eq(SysCustomer::getName, clientName)
                .eq(SysCustomer::getType, SysCustomerConstant.CLIENT_TYPE)
                .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED)
                .ne(SysCustomer::getId, param.getId()));
        if (count > 0) {
            return BaseResponse.failure("客户名称已存在");
        }
        // 判断当前用户是否是客户管理员
        // if (!user.getUserId().equals(SysUserConstant.ADMINISTRATOR_ID)
        // || !sysCustomer.getAdminUserId().equals(param.getAdminUserId())) {
        // return BaseResponse.failure("客户管理员错误");
        // }
        // 查询客户管理员角色
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId,
                        sysCustomer.getAdminUserId()));
        if (sysUserRole == null || !sysUserRole.getRoleId().equals(param.getRoleId())) {
            return BaseResponse.failure("客户管理员角色错误");
        }
        // 编辑客户
        SysCustomer newCustomer = this.updateClient(param, user);
        // 编辑用户
        this.updateSysUser(param, sysCustomer, request);
        // 编辑角色
        this.updateSysRole(param, request);
        // 保存日志
        operateLogAsync.pushEditLog(SysCustomer.class, sysCustomer, newCustomer,
                OperateModuleEnum.ARCHIVE_CLIENT_EDIT,
                String.valueOf(sysCustomer.getId()), user, request);
        this.updateCustomerContext(newCustomer);
        return BaseResponse.ok();
    }

    /**
     * 客户档案-查看客户详情
     *
     * @param clientId
     * @return
     */
    @Override
    public BaseResponse getClientDetail(Integer clientId) {
        // 查询客户
        SysCustomer sysCustomer = sysCustomerMapper.selectOne(new LambdaQueryWrapper<SysCustomer>()
                .eq(SysCustomer::getId, clientId)
                .eq(SysCustomer::getType, SysCustomerConstant.CLIENT_TYPE)
                .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (sysCustomer == null) {
            return BaseResponse.failure("客户不存在");
        }
        // 查询客户管理员
        SysUser sysUser = sysUserMapper.selectById(sysCustomer.getAdminUserId());
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId,
                        sysCustomer.getAdminUserId()));
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper
                .selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId,
                        sysUserRole.getRoleId()));
        List<Integer> menuIdList = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : sysRoleMenuList) {
            menuIdList.add(sysRoleMenu.getMenuId());
        }
        ClientDetailVo clientDetailVo = this.convertToClientDetailVo(sysCustomer, sysUserRole,
                sysUser, menuIdList);
        return BaseResponse.ok(clientDetailVo);
    }

    /**
     * 客户档案-删除客户
     *
     * @param clientId
     * @param phone
     * @param request
     * @return
     */
    @Override
    public BaseResponse deleteClient(Integer clientId, String phone, HttpServletRequest request) {
        // 获取当前用户
        UserVo userVo = UserContext.getUser();
        // 校验手机号
        if (!userVo.getPhone().equals(phone)) {
            return BaseResponse.failure("手机号错误，删除操作未执行");
        }
        // 查询客户
        SysCustomer sysCustomer = sysCustomerMapper.selectById(clientId);
        if (sysCustomer == null) {
            return BaseResponse.failure("客户不存在");
        }
        Long vehicleCount = vehicleMapper
                .selectCount(
                        new LambdaQueryWrapper<VehicleEntity>().eq(VehicleEntity::getCustomerId,
                                        clientId)
                                .eq(VehicleEntity::getIsDeleted, SysCustomerConstant.NOT_DELETED));
        if (vehicleCount > 0) {
            return BaseResponse.failure("当前客户存在车辆档案数据,不允许删除");
        }
        Long vehicleSpecCount = vehicleSpecMapper
                .selectCount(new LambdaQueryWrapper<VehicleSpecEntity>().eq(
                                VehicleSpecEntity::getCustomerId, clientId)
                        .eq(VehicleSpecEntity::getIsDeleted, SysCustomerConstant.NOT_DELETED));
        if (vehicleSpecCount > 0) {
            return BaseResponse.failure("当前客户存在车辆轮胎数据,不允许删除");
        }
        // 删除客户
        SysCustomer updateParam = new SysCustomer();
        updateParam.setId(clientId);
        updateParam.setIsDelete(SysCustomerConstant.DELETED);
        updateParam.setUpdatePerson(userVo.getName());
        sysCustomerMapper.updateById(updateParam);
        // 删除用户
        sysUserService.delete(sysCustomer.getAdminUserId(), phone, request);
        // 保存日志
        operateLogAsync.pushDeleteLog(OperateModuleEnum.ARCHIVE_CLIENT_DELETE,
                String.valueOf(sysCustomer.getId()),
                userVo, request, String.valueOf(sysCustomer.getName()));
        this.deleteCustomerContext(updateParam);
        return BaseResponse.ok();
    }

    private void deleteCustomerContext(SysCustomer updateParam) {
        customerContext.remove(updateParam.getId());
    }

    /**
     * 客户档案-获取所有客户名称
     *
     * @return
     */
    @Override
    public BaseResponse getAllClientName() {
        List<SysCustomer> sysCustomerList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>().eq(SysCustomer::getType,
                                SysCustomerConstant.CLIENT_TYPE)
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        List<ClientNameVo> clientNameVoList = new ArrayList<>();
        for (SysCustomer sysCustomer : sysCustomerList) {
            ClientNameVo clientNameVo = new ClientNameVo();
            clientNameVo.setId(sysCustomer.getId());
            clientNameVo.setName(sysCustomer.getName());
            clientNameVoList.add(clientNameVo);
        }
        return BaseResponse.ok(clientNameVoList);
    }

    /**
     * 客户档案-查询当前用户归属
     */
    @Override
    public BaseResponse<List<SysCustomerTreeVo>> getCustomerTree() {
        // 获取当前用户
        UserVo user = UserContext.getUser();
        // 客户用户关联表,一对一
        SysCustomerUser sysCustomerUser = sysCustomerUserMapper
                .selectOne(new LambdaQueryWrapper<SysCustomerUser>().eq(SysCustomerUser::getUserId,
                        user.getUserId()));
        if (sysCustomerUser == null) {
            return BaseResponse.ok(new ArrayList<>());
        }
        List<SysCustomerTreeVo> parentCustomerTreeList = new ArrayList<>();
        // 查询客户信息
        List<SysCustomer> parentCustomerList = new ArrayList<>();
        parentCustomerList = this.getParentCustomers(user, sysCustomerUser);
        if (user.getCustomerType() != SysCustomerConstant.PARENT_TYPE && parentCustomerList.isEmpty()) {
            return BaseResponse.ok(new ArrayList<>());
        }
        for (SysCustomer parentCustomer : parentCustomerList) {
            SysCustomerTreeVo parentCustomerTree = new SysCustomerTreeVo();
            parentCustomerTree.setId(parentCustomer.getId());
            parentCustomerTree.setName(parentCustomer.getName());
            parentCustomerTree.setParentId(parentCustomer.getParentId());
            parentCustomerTree.setType(parentCustomer.getType());
            parentCustomerTreeList.add(parentCustomerTree);
        }
        List<SysCustomer> allCustomerList = this.getAllCustomer(parentCustomerList);
        List<SysCustomerTreeVo> allCustomerTreeVoList = new ArrayList<>();
        for (SysCustomer sysCustomer : allCustomerList) {
            SysCustomerTreeVo children = new SysCustomerTreeVo();
            children.setId(sysCustomer.getId());
            children.setName(sysCustomer.getName());
            children.setParentId(sysCustomer.getParentId());
            children.setType(sysCustomer.getType());
            allCustomerTreeVoList.add(children);
        }
        // 递归检索所有子客户
        this.getChildrenCustomer(parentCustomerTreeList, allCustomerTreeVoList);
        // 面心用户查询面心数据
        if (user.getCustomerType() == SysCustomerConstant.PARENT_TYPE) {
            SysCustomer customer = sysCustomerMapper.selectOne(
                    new LambdaQueryWrapper<SysCustomer>().eq(SysCustomer::getType,
                                    SysCustomerConstant.PARENT_TYPE)
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            List<SysCustomerTreeVo> customerTreeVoList = new ArrayList<>();
            SysCustomerTreeVo customerTreeVo = new SysCustomerTreeVo();
            customerTreeVo.setId(customer.getId());
            customerTreeVo.setName(customer.getName());
            customerTreeVo.setParentId(customer.getParentId());
            customerTreeVo.setType(customer.getType());
            customerTreeVo.setChildren(parentCustomerTreeList);
            customerTreeVoList.add(customerTreeVo);
            return BaseResponse.ok(customerTreeVoList);
        }
        return BaseResponse.ok(parentCustomerTreeList);
    }

    private List<SysCustomer> getParentCustomers(UserVo user, SysCustomerUser sysCustomerUser) {
        List<SysCustomer> parentCustomerList;
        // 如果是面心用户查询所有客户信息,否则只查询当前登录人客户信息
        if (user.getCustomerType() == SysCustomerConstant.PARENT_TYPE) {
            parentCustomerList = sysCustomerMapper.selectList(new LambdaQueryWrapper<SysCustomer>()
                    .eq(SysCustomer::getType, SysCustomerConstant.CLIENT_TYPE)
                    .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        } else {
            parentCustomerList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>().eq(SysCustomer::getId,
                                    sysCustomerUser.getCustomerId())
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        }
        return parentCustomerList;
    }

    private List<SysCustomer> getAllCustomer(List<SysCustomer> parentCustomerList) {
        if (parentCustomerList.isEmpty()) {
            return new ArrayList<>();
        }
        Set<Integer> companyIdSet = new HashSet<>();
        for (SysCustomer sysCustomer : parentCustomerList) {
            companyIdSet.add(sysCustomer.getCompanyId());
        }
        // 查询客户下的所有子客户
        List<SysCustomer> allCustomerList = sysCustomerMapper
                .selectList(new LambdaQueryWrapper<SysCustomer>().in(SysCustomer::getCompanyId,
                                companyIdSet)
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        return allCustomerList;
    }

    private void getChildrenCustomer(List<SysCustomerTreeVo> parentList,
            List<SysCustomerTreeVo> allCustomerList) {
        for (SysCustomerTreeVo parent : parentList) {
            Integer parentId = parent.getId();
            List<SysCustomerTreeVo> children = new ArrayList<>();
            for (SysCustomerTreeVo allCustomer : allCustomerList) {
                if (parentId.equals(allCustomer.getParentId())) {
                    children.add(allCustomer);
                }
            }
            if (!children.isEmpty()) {
                parent.getChildren().addAll(children);
            }
            if (parent.getChildren().isEmpty()) {
                continue;
            }
            getChildrenCustomer(parent.getChildren(), allCustomerList);
        }
    }

    private ClientDetailVo convertToClientDetailVo(SysCustomer sysCustomer, SysUserRole sysUserRole,
            SysUser sysUser,
            List<Integer> menuIdList) {
        ClientDetailVo clientDetailVo = new ClientDetailVo();
        clientDetailVo.setId(sysCustomer.getId());
        clientDetailVo.setRoleId(sysUserRole.getRoleId());
        clientDetailVo.setName(sysCustomer.getName());
        clientDetailVo.setContactName(sysCustomer.getContactName());
        clientDetailVo.setContactPhone(sysCustomer.getContactPhone());
        clientDetailVo.setProvince(sysCustomer.getProvince());
        clientDetailVo.setProvinceId(sysCustomer.getProvinceId());
        clientDetailVo.setCity(sysCustomer.getCity());
        clientDetailVo.setCityId(sysCustomer.getCityId());
        clientDetailVo.setCounty(sysCustomer.getCounty());
        clientDetailVo.setCountyId(sysCustomer.getCountyId());
        clientDetailVo.setAddress(sysCustomer.getAddressDetail());
        clientDetailVo.setAdminUserId(sysCustomer.getAdminUserId());
        clientDetailVo.setValidDate(
                sysCustomer.getInvalidTime() == null ? ""
                        : DateUtil.dateToString(sysCustomer.getInvalidTime()));
        clientDetailVo.setMaxVehicle(sysCustomer.getMaxVehicle());
        clientDetailVo.setAdminAccount(sysUser.getAccount());
        clientDetailVo.setBillInfo(sysCustomer.getBillInfo());
        clientDetailVo.setMenuIdList(menuIdList);
        clientDetailVo.setStatus(sysCustomer.getStatus());
        return clientDetailVo;
    }

    private void updateSysRole(ClientEditParam param, HttpServletRequest request) {
        SysRoleEditParam sysRoleEditParam = new SysRoleEditParam();
        sysRoleEditParam.setId(param.getRoleId());
        // 角色名称 = 客户名称
        sysRoleEditParam.setName(param.getName());
        sysRoleEditParam.setMenuIdList(param.getMenuIdList());
        sysRoleEditParam.setStatus(SysRoleConstant.ENABLE);
        BaseResponse sysRoleEditResponse = sysRoleService.edit(sysRoleEditParam, request);
        if (!sysRoleEditResponse.getCode().equals(ResponseEnum.SUCCESS.getCode())) {
            log.error("客户档案-修改客户, 修改客户管理员角色失败, param:{}, sysRoleEditResponse:{}",
                    param, sysRoleEditResponse);
            throw new BaseException(sysRoleEditResponse.getCode(),
                    sysRoleEditResponse.getMessage());
        }
    }

    private void updateSysUser(ClientEditParam param, SysCustomer sysCustomer,
            HttpServletRequest request) {
        SysUserEditParam sysUserEditParam = new SysUserEditParam();
        sysUserEditParam.setId(sysCustomer.getAdminUserId());
        sysUserEditParam.setCustomerId(sysCustomer.getId());
        sysUserEditParam.setAccount(param.getAdminAccount());
        sysUserEditParam.setPhone(param.getContactPhone());
        sysUserEditParam.setName(param.getContactName());
        sysUserEditParam.setRoleId(param.getRoleId());
        sysUserEditParam.setStatus(SysUserConstant.ENABLE);
        BaseResponse sysUserEditResponse = sysUserService.edit(sysUserEditParam, request);
        if (!sysUserEditResponse.getCode().equals(ResponseEnum.SUCCESS.getCode())) {
            log.error("客户档案-修改客户, 修改客户管理员账号失败, param:{}, sysUserEditResponse:{}",
                    param, sysUserEditResponse);
            throw new BaseException(sysUserEditResponse.getCode(),
                    sysUserEditResponse.getMessage());
        }

    }

    private SysCustomer updateClient(ClientEditParam param, UserVo user) {
        SysCustomer updateParam = new SysCustomer();
        updateParam.setId(param.getId());
        updateParam.setName(param.getName());
        updateParam.setContactName(param.getContactName());
        updateParam.setContactPhone(param.getContactPhone());
        updateParam.setProvince(param.getProvince());
        updateParam.setProvinceId(param.getProvinceId());
        updateParam.setCity(param.getCity());
        updateParam.setCityId(param.getCityId());
        updateParam.setCounty(param.getCounty());
        updateParam.setCountyId(param.getCountyId());
        updateParam.setAddressDetail(param.getAddress());
        updateParam.setAdminUserId(param.getAdminUserId());
        updateParam.setStatus(param.getStatus());
        updateParam.setMaxVehicle(param.getMaxVehicle());
        updateParam.setBillInfo(param.getBillInfo());
        updateParam.setInvalidTime(
                StringUtils.isBlank(param.getValidDate()) ? null
                        : DateUtil.StringToDate(param.getValidDate()));
        updateParam.setUpdatePerson(user.getName());
        sysCustomerMapper.updateById(updateParam);
        return updateParam;
    }

    private void updateClientAdminUser(SysCustomer sysClient, SysUser sysUser) {
        SysCustomer updateParam = new SysCustomer();
        updateParam.setId(sysClient.getId());
        updateParam.setAdminUserId(sysUser.getId());
        sysCustomerMapper.updateById(updateParam);
    }

    private SysUser saveSysUser(ClientAddParam param, SysCustomer sysClient, SysRole sysRole,
            HttpServletRequest request) {
        SysUserAddParam sysUserAddParam = new SysUserAddParam();
        sysUserAddParam.setCustomerId(sysClient.getId());
        sysUserAddParam.setAccount(param.getAdminAccount());
        String password = "";
        if (param.getPassword() == null || StringUtils.isBlank(param.getPassword())) {
            password = SysUserConstant.DEFAULT_PASSWORD;
        } else {
            password = param.getPassword();
        }
        sysUserAddParam.setPassword(password);
        sysUserAddParam.setPhone(param.getContactPhone());
        sysUserAddParam.setName(param.getContactName());
        sysUserAddParam.setType(SysUserConstant.CLIENT_ADMINISTRATOR_ID);
        sysUserAddParam.setRoleId(sysRole.getId());
        BaseResponse addSysUserResponse = sysUserService.add(sysUserAddParam, request);
        if (!addSysUserResponse.getCode().equals(ResponseEnum.SUCCESS.getCode())) {
            log.error("客户档案-新增客户, 新增客户管理员账号失败, param:{}, addSysUserResponse:{}",
                    param, addSysUserResponse);
            throw new BaseException(addSysUserResponse.getCode(), addSysUserResponse.getMessage());
        }
        SysUser sysUser = (SysUser) addSysUserResponse.getData();
        return sysUser;
    }

    private SysRole saveSysRole(ClientAddParam param, SysCustomer sysClient,
            HttpServletRequest request) {
        SysRoleAddParam sysRoleAddParam = new SysRoleAddParam();
        sysRoleAddParam.setCustomerId(sysClient.getId());
        // 角色名称 = 客户名称
        sysRoleAddParam.setName(sysClient.getName());
        // 客户管理员的角色不能可见
        sysRoleAddParam.setLevel(SysRoleConstant.DEFAULT_LEVEL);
        sysRoleAddParam.setMenuIdList(param.getMenuIdList());
        BaseResponse addRoleResponse = sysRoleService.add(sysRoleAddParam, request);
        if (!addRoleResponse.getCode().equals(ResponseEnum.SUCCESS.getCode())) {
            log.error("客户档案-新增客户, 新增客户管理员角色失败, param:{}, addRoleResponse:{}",
                    param, addRoleResponse);
            throw new BaseException(addRoleResponse.getCode(), addRoleResponse.getMessage());
        }
        return (SysRole) addRoleResponse.getData();
    }

    private SysCustomer saveClient(ClientAddParam param, UserVo user) {
        SysCustomer insertParam = new SysCustomer();
        insertParam.setName(param.getName());
        insertParam.setContactName(param.getContactName());
        insertParam.setContactPhone(param.getContactPhone());
        insertParam.setProvince(param.getProvince());
        insertParam.setProvinceId(param.getProvinceId());
        insertParam.setCity(param.getCity());
        insertParam.setCityId(param.getCityId());
        insertParam.setCounty(param.getCounty());
        insertParam.setCountyId(param.getCountyId());
        insertParam.setAddressDetail(param.getAddress());
        insertParam.setType(SysCustomerConstant.CLIENT_TYPE);
        insertParam.setStatus(SysCustomerConstant.ENABLE);
        insertParam.setMaxVehicle(param.getMaxVehicle());
        insertParam.setBillInfo(param.getBillInfo());
        insertParam.setInvalidTime(
                StringUtils.isBlank(param.getValidDate()) ? null
                        : DateUtil.StringToDate(param.getValidDate()));
        insertParam.setCreatePerson(user.getName());
        insertParam.setUpdatePerson(user.getName());
        insertParam.setParentId(SysCustomerConstant.DEFAULT_PARENT_ID);
        sysCustomerMapper.insert(insertParam);
        return insertParam;
    }

    private SysClientVo getSysClientVo(SysCustomer sysCustomer, SysUser clientUser) {
        SysClientVo sysClientVo = new SysClientVo();
        sysClientVo.setId(sysCustomer.getId());
        sysClientVo.setName(sysCustomer.getName());
        sysClientVo.setStatus(sysCustomer.getStatus());
        // 客户的customerId是自身主键ID
        sysClientVo.setCompanyId(sysCustomer.getId());
        sysClientVo.setAdminUserId(sysCustomer.getAdminUserId());
        if (clientUser != null) {
            sysClientVo.setAdminUserAccount(clientUser.getAccount());
        }
        sysClientVo.setContactName(sysCustomer.getContactName());
        sysClientVo.setContactPhone(sysCustomer.getContactPhone());
        sysClientVo.setType(sysCustomer.getType());
        sysClientVo.setMaxVehicle(sysCustomer.getMaxVehicle());
        sysClientVo.setInvalidTime(
                sysCustomer.getInvalidTime() == null ? ""
                        : DateUtil.dateToString(sysCustomer.getInvalidTime()));
        sysClientVo.setUpdatePerson(sysCustomer.getUpdatePerson());
        sysClientVo.setUpdateTime(sysCustomer.getUpdateTime());
        sysClientVo.setParentId(sysCustomer.getParentId());
        return sysClientVo;
    }

    private SysUser getAdminUser(SysCustomer sysCustomer, List<SysUser> sysUserList) {
        SysUser clientUser = null;
        for (SysUser sysUser : sysUserList) {
            if (sysCustomer.getAdminUserId().equals(sysUser.getId())) {
                clientUser = sysUser;
                break;
            }
        }
        return clientUser;
    }

    private List<SysUser> getSysUserList(List<SysCustomer> sysCustomerList) {
        // 根据主角ID查询sysUser数据
        List<Integer> adminUserIdList = new ArrayList<>();
        for (SysCustomer sysCustomer : sysCustomerList) {
            adminUserIdList.add(sysCustomer.getAdminUserId());
        }
        List<SysUser> sysUserList = new ArrayList<>();
        if (!adminUserIdList.isEmpty()) {
            sysUserList = sysUserMapper.selectBatchIds(adminUserIdList);
        }
        return sysUserList;
    }

    private LambdaQueryWrapper<SysCustomer> getClientWrapper(ClientConditionQuery param) {
        LambdaQueryWrapper<SysCustomer> wrapper = new LambdaQueryWrapper<>();
        String name = param.getName();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(SysCustomer::getName, name);
        }
        wrapper.eq(SysCustomer::getType, SysCustomerConstant.CLIENT_TYPE);
        wrapper.eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED);
        wrapper.orderByDesc(SysCustomer::getUpdateTime);
        return wrapper;
    }

    @Override
    public Set<Integer> getAllRelatedIds() {
        Integer customerId = UserContext.getUser().getCustomerId();
        SysCustomer sysCustomer = this.sysCustomerMapper.selectById(customerId);
        Set<Integer> idSet = new HashSet<Integer>();
        if (sysCustomer.getType().intValue() == PARENT_TYPE) {
            idSet.addAll(this.sysCustomerMapper.getAllCustomerId());
            return idSet;
        }
        idSet.add(customerId);
        Set<Integer> parentIds = new HashSet<Integer>();
        parentIds.add(customerId);
        while (true) {
            List<Integer> childIds = this.sysCustomerMapper.getChildCustomerId(
                    Arrays.toString(parentIds.toArray(new Integer[0])));
            if (childIds == null || childIds.isEmpty()) {
                break;
            }
            parentIds.clear();
            parentIds.addAll(childIds);
            idSet.addAll(childIds);
        }
        return idSet;
    }

    @Override
    public SimpleCustomerVo getCustomerInfo(Integer customerId) {
        SysCustomer currentCustomer = this.sysCustomerMapper.selectById(customerId);
        if (currentCustomer.getId().intValue() != currentCustomer.getCompanyId().intValue()) {
            currentCustomer = this.sysCustomerMapper.selectById(currentCustomer.getCompanyId());
        }
        return new SimpleCustomerVo(customerId, currentCustomer.getName());
    }

    @Override
    public SimpleCustomerVo getCustomerInfo() {
        return this.getCustomerInfo(UserContext.getUser().getCustomerId());
    }

    @Override
    public List<SimpleCustomerVo> getCustomersLite() {
        Integer customerId = UserContext.getUser().getCustomerId();
        List<SimpleCustomerVo> list = new ArrayList<SimpleCustomerVo>();
        SysCustomer currentCustomer = this.sysCustomerMapper.selectById(customerId);
        if (currentCustomer == null || currentCustomer.getType() == null) {
            return list;
        }
        if (currentCustomer.getType().intValue() == PARENT_TYPE) {
            list.add(new SimpleCustomerVo(currentCustomer.getId(), currentCustomer.getName()));
            list.addAll(this.sysCustomerMapper.getAllCustomerByType(CLIENT_TYPE));
            return list;
        } else if (currentCustomer.getType().intValue() == CLIENT_TYPE) {
            list.add(new SimpleCustomerVo(currentCustomer.getId(), currentCustomer.getName()));
            return list;
        } else if (currentCustomer.getType().intValue() == FIRST_LEVEL_FLEET_TYPE
                || currentCustomer.getType().intValue() == SECOND_LEVEL_FLEET_TYPE) {
            currentCustomer = this.sysCustomerMapper.selectById(currentCustomer.getCompanyId());
            list.add(new SimpleCustomerVo(currentCustomer.getId(), currentCustomer.getName()));
            return list;
        }
        return list;
    }

    @Override
    public List<SimpleFleetVo> getFleetsLite() {
        List<SimpleCustomerVo> cusList = this.getCustomersLite();
        List<SimpleFleetVo> list = new ArrayList<SimpleFleetVo>();
        for (SimpleCustomerVo cusVo : cusList) {
            list.add(this.fillSimpleFleetVo(cusVo));
        }
        return list;
    }

    private SimpleFleetVo fillSimpleFleetVo(SimpleCustomerVo cusVo) {
        SimpleFleetVo fleetVo = new SimpleFleetVo(cusVo.getId(), cusVo.getName());
        List<SimpleFleetVo> fleetLev1List = this.sysCustomerMapper.getFleetVoByTypeAndParent(
                cusVo.getId(),
                FIRST_LEVEL_FLEET_TYPE);
        for (SimpleFleetVo lev1Fleet : fleetLev1List) {
            List<SimpleFleetVo> fleetLev2List = this.sysCustomerMapper.getFleetVoByTypeAndParent(
                    lev1Fleet.getId(),
                    SECOND_LEVEL_FLEET_TYPE);
            lev1Fleet.getChildren().addAll(fleetLev2List);
            fleetVo.getChildren().add(lev1Fleet);
        }
        return fleetVo;
    }

    @Override
    public SysCustomer getById(Integer customerId) {
        return this.sysCustomerMapper.selectById(customerId);
    }

}
