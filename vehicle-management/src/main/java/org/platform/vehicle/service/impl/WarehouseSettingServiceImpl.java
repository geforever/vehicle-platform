package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.constant.SysCustomerConstant;
import org.platform.vehicle.constant.WarehouseSettingConstant;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.WarehouseSetting;
import org.platform.vehicle.mapper.AssetTireMapper;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.WarehouseSettingMapper;
import org.platform.vehicle.param.WarehouseSettingAddParam;
import org.platform.vehicle.param.WarehouseSettingConditionQueryParam;
import org.platform.vehicle.param.WarehouseSettingEditParam;
import org.platform.vehicle.service.WarehouseSettingService;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.vo.WarehouseSettingDetailVo;
import org.platform.vehicle.vo.WarehouseSettingVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (WarehouseSetting)表服务实现类
 *
 * @author geforever
 * @since 2023-09-13 10:20:17
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WarehouseSettingServiceImpl implements WarehouseSettingService {

    private final WarehouseSettingMapper warehouseSettingMapper;
    private final SysCustomerMapper sysCustomerMapper;
    private final OperateLogAsync operateLogAsync;
    private final AssetTireMapper assetTireMapper;

    /**
     * 资产管理-仓库管理-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse<List<WarehouseSettingVo>> conditionQuery(
            WarehouseSettingConditionQueryParam param) {
        Page<WarehouseSetting> page = new Page<>(param.getPageNum(), param.getPageSize());
        List<SysCustomer> sysCustomerList = this.getCustomerList(param);
        LambdaQueryWrapper<WarehouseSetting> wrapper = this.getWarehouseSettingWrapper(
                param);
        Page<WarehouseSetting> warehouseSettingPage = warehouseSettingMapper.selectPage(page,
                wrapper);
        List<WarehouseSetting> warehouseSettingList = warehouseSettingPage.getRecords();
        List<WarehouseSettingVo> warehouseSettingVoList = new ArrayList<>();
        for (WarehouseSetting warehouseSetting : warehouseSettingList) {
            WarehouseSettingVo warehouseSettingVo = this.getWarehouseSettingVo(warehouseSetting,
                    sysCustomerList);
            warehouseSettingVoList.add(warehouseSettingVo);
        }
        return new BasePageResponse(warehouseSettingVoList, page);
    }

    /**
     * 资产管理-仓库管理-新增
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    public BaseResponse add(WarehouseSettingAddParam param, HttpServletRequest request) {
        // 当前登录用户
        UserVo user = UserContext.getUser();
        // 查询车队信息
        SysCustomer fleet = sysCustomerMapper.selectById(param.getFleetId());
        if (fleet == null) {
            return BaseResponse.failure("车队不存在");
        }
        // 校验仓库名称是否重复, 同一个客户下不重复
        LambdaQueryWrapper<WarehouseSetting> wrapper = new LambdaQueryWrapper<WarehouseSetting>();
        wrapper.eq(WarehouseSetting::getName, param.getName());
        wrapper.eq(WarehouseSetting::getClientId, fleet.getCompanyId());
        wrapper.eq(WarehouseSetting::getIsDelete, SysCustomerConstant.NOT_DELETED);
        Long count = warehouseSettingMapper.selectCount(wrapper);
        if (count > 0) {
            return BaseResponse.failure("仓库名称重复");
        }
        WarehouseSetting warehouseSetting = new WarehouseSetting();
        warehouseSetting.setName(param.getName());
        warehouseSetting.setFleetId(fleet.getId());
        warehouseSetting.setClientId(fleet.getCompanyId());
        warehouseSetting.setStockMin(param.getStockMin());
        warehouseSetting.setStockMax(param.getStockMax());
        warehouseSetting.setCreatePerson(user.getName());
        warehouseSetting.setUpdatePerson(user.getName());
        warehouseSettingMapper.insert(warehouseSetting);
        // 保存日志
        operateLogAsync.pushAddLog(
                warehouseSetting,
                OperateModuleEnum.ASSET_WAREHOUSE_EDIT,
                String.valueOf(warehouseSetting.getId()),
                user,
                request);
        return BaseResponse.ok();
    }

    /**
     * 资产管理-仓库管理-修改
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    public BaseResponse edit(WarehouseSettingEditParam param, HttpServletRequest request) {
        // 当前登录用户
        UserVo user = UserContext.getUser();
        // 查询仓库信息
        WarehouseSetting warehouseSetting = warehouseSettingMapper.selectById(param.getId());
        // 查询车队信息
        SysCustomer fleet = sysCustomerMapper.selectById(param.getFleetId());
        if (fleet == null) {
            return BaseResponse.failure("车队不存在");
        }
        // 校验仓库名称是否重复, 同一个客户下不重复
        LambdaQueryWrapper<WarehouseSetting> wrapper = new LambdaQueryWrapper<WarehouseSetting>();
        wrapper.eq(WarehouseSetting::getName, param.getName());
        wrapper.eq(WarehouseSetting::getClientId, fleet.getCompanyId());
        wrapper.eq(WarehouseSetting::getIsDelete, SysCustomerConstant.NOT_DELETED);
        wrapper.ne(WarehouseSetting::getId, param.getId());
        Long count = warehouseSettingMapper.selectCount(wrapper);
        if (count > 0) {
            return BaseResponse.failure("仓库名称重复");
        }
        WarehouseSetting newWarehouseSetting = this.updateWarehouse(param, fleet, user);
        operateLogAsync.pushEditLog(
                WarehouseSetting.class,
                warehouseSetting,
                newWarehouseSetting,
                OperateModuleEnum.ASSET_WAREHOUSE_EDIT,
                String.valueOf(param.getId()),
                user,
                request);
        return BaseResponse.ok();
    }

    /**
     * 资产管理-仓库管理-删除
     *
     * @param id
     * @param phone
     * @return
     */
    @Override
    public BaseResponse delete(Integer id, String phone, HttpServletRequest request) {
        // 当前登录用户
        UserVo user = UserContext.getUser();
        // 查询仓库信息
        WarehouseSetting warehouseSetting = warehouseSettingMapper.selectById(id);
        if (warehouseSetting == null) {
            return BaseResponse.failure("仓库不存在");
        }
        // 校验仓库是否在使用, 仓库下有轮胎不允许删除
        Long count = assetTireMapper.selectCount(new LambdaQueryWrapper<AssetTire>()
                .eq(AssetTire::getId, id)
                .eq(AssetTire::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (count > 0) {
            return BaseResponse.failure("仓库下有轮胎不允许删除");
        }
        WarehouseSetting updateParam = new WarehouseSetting();
        updateParam.setId(id);
        updateParam.setIsDelete(WarehouseSettingConstant.IS_DELETE);
        warehouseSettingMapper.updateById(updateParam);
        // 保存日志
        operateLogAsync.pushDeleteLog(
                OperateModuleEnum.ASSET_WAREHOUSE_DELETE,
                String.valueOf(warehouseSetting.getId()),
                user,
                request,
                warehouseSetting.getName());
        return BaseResponse.ok();
    }

    /**
     * 资产管理-仓库管理-查看仓库详情
     *
     * @param id
     * @return
     */
    @Override
    public BaseResponse<WarehouseSettingVo> getWarehouseDetail(Integer id) {
        // 查询仓库信息
        WarehouseSetting warehouseSetting = warehouseSettingMapper.selectById(id);
        if (warehouseSetting == null) {
            return BaseResponse.failure("仓库不存在");
        }
        // 查询车队信息
        SysCustomer fleet = sysCustomerMapper.selectById(warehouseSetting.getFleetId());
        // 查询客户信息
        SysCustomer client = sysCustomerMapper.selectById(warehouseSetting.getClientId());
        WarehouseSettingDetailVo warehouseSettingVo = new WarehouseSettingDetailVo();
        warehouseSettingVo.setId(warehouseSetting.getId());
        warehouseSettingVo.setName(warehouseSetting.getName());
        warehouseSettingVo.setCustomerId(warehouseSetting.getFleetId());
        if (fleet != null) {
            warehouseSettingVo.setFleetName(fleet.getName());
        }
        if (client != null) {
            warehouseSettingVo.setClientName(client.getName());
        }
        warehouseSettingVo.setStockMin(warehouseSetting.getStockMin());
        warehouseSettingVo.setStockMax(warehouseSetting.getStockMax());
        warehouseSettingVo.setUpdatePerson(warehouseSetting.getUpdatePerson());
        warehouseSettingVo.setUpdateTime(warehouseSetting.getUpdateTime());
        return BaseResponse.ok(warehouseSettingVo);
    }

    private WarehouseSetting updateWarehouse(WarehouseSettingEditParam param, SysCustomer fleet,
            UserVo user) {
        WarehouseSetting updateParam = new WarehouseSetting();
        updateParam.setId(param.getId());
        updateParam.setName(param.getName());
        updateParam.setFleetId(fleet.getId());
        updateParam.setClientId(fleet.getCompanyId());
        updateParam.setStockMin(param.getStockMin());
        updateParam.setStockMax(param.getStockMax());
        updateParam.setUpdatePerson(user.getName());
        warehouseSettingMapper.updateById(updateParam);
        return updateParam;
    }

    private WarehouseSettingVo getWarehouseSettingVo(WarehouseSetting warehouseSetting,
            List<SysCustomer> sysCustomerList) {
        SysCustomer fleet = null;
        for (SysCustomer sysCustomer : sysCustomerList) {
            if (sysCustomer.getId().equals(warehouseSetting.getFleetId())) {
                fleet = sysCustomer;
                break;
            }
        }
        SysCustomer client = null;
        if (fleet != null) {
            for (SysCustomer sysCustomer : sysCustomerList) {
                if (sysCustomer.getId().equals(fleet.getCompanyId())) {
                    client = sysCustomer;
                    break;
                }
            }
        }
        WarehouseSettingVo warehouseSettingVo = new WarehouseSettingVo();
        warehouseSettingVo.setId(warehouseSetting.getId());
        warehouseSettingVo.setName(warehouseSetting.getName());
        warehouseSettingVo.setCustomerId(warehouseSetting.getFleetId());
        if (fleet != null) {
            warehouseSettingVo.setFleetName(fleet.getName());
        }
        if (client != null) {
            warehouseSettingVo.setClientName(client.getName());
        }
        warehouseSettingVo.setUpdatePerson(warehouseSetting.getUpdatePerson());
        warehouseSettingVo.setUpdateTime(warehouseSetting.getUpdateTime());
        return warehouseSettingVo;
    }

    private LambdaQueryWrapper<WarehouseSetting> getWarehouseSettingWrapper(
            WarehouseSettingConditionQueryParam param) {
        LambdaQueryWrapper<WarehouseSetting> wrapper = new LambdaQueryWrapper<WarehouseSetting>();
        if (StringUtils.isNotBlank(param.getWarehouseName())) {
            wrapper.like(WarehouseSetting::getName, param.getWarehouseName());
        }
        if (StringUtils.isNotBlank(param.getFleetName())) {
            List<Integer> customerTypeList = new ArrayList<>();
            customerTypeList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
            customerTypeList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
            LambdaQueryWrapper<SysCustomer> sysCustomerWrapper = new LambdaQueryWrapper<SysCustomer>();
            sysCustomerWrapper.like(SysCustomer::getName, param.getFleetName());
            sysCustomerWrapper.in(SysCustomer::getType, customerTypeList)
                    .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED);
            List<SysCustomer> sysCustomerList = sysCustomerMapper.selectList(sysCustomerWrapper);
            List<Integer> fleetIdList = new ArrayList<>();
            sysCustomerList.forEach(sysCustomer -> fleetIdList.add(sysCustomer.getId()));
            if (sysCustomerList.isEmpty()) {
                fleetIdList.add(-1);
            }
            wrapper.in(WarehouseSetting::getFleetId, fleetIdList);
        }
        wrapper.eq(WarehouseSetting::getIsDelete, WarehouseSettingConstant.NOT_DELETE);
        wrapper.orderByDesc(WarehouseSetting::getUpdatePerson);
        return wrapper;
    }

    private List<SysCustomer> getCustomerList(WarehouseSettingConditionQueryParam param) {
        List<Integer> customerTypeList = new ArrayList<>();
        customerTypeList.add(SysCustomerConstant.CLIENT_TYPE);
        customerTypeList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
        customerTypeList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
        LambdaQueryWrapper<SysCustomer> wrapper = new LambdaQueryWrapper<SysCustomer>();
        wrapper.in(SysCustomer::getType, customerTypeList)
                .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED);
        List<SysCustomer> sysCustomerList = sysCustomerMapper.selectList(wrapper);
        return sysCustomerList;
    }
}
