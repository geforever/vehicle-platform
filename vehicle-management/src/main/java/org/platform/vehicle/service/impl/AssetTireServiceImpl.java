package org.platform.vehicle.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.conf.ExcelCellWidthStyleStrategy;
import org.platform.vehicle.constant.AssetTireConstant;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.constant.SysCustomerConstant;
import org.platform.vehicle.constant.WarehouseSettingConstant;
import org.platform.vehicle.dto.AssetTireFitDto;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.entity.AssetTireFitRecord;
import org.platform.vehicle.entity.AssetTireStockRecord;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.SysCustomerUser;
import org.platform.vehicle.entity.TireBrandEntity;
import org.platform.vehicle.entity.TireSpecEntity;
import org.platform.vehicle.entity.UploadFile;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.entity.WarehouseSetting;
import org.platform.vehicle.helper.jdbc.AssetTireJdbc;
import org.platform.vehicle.listener.TireBatchInstallListener;
import org.platform.vehicle.listener.TireBatchStockInListener;
import org.platform.vehicle.mapper.AssetTireFitRecordMapper;
import org.platform.vehicle.mapper.AssetTireMapper;
import org.platform.vehicle.mapper.AssetTireStockRecordMapper;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.SysCustomerUserMapper;
import org.platform.vehicle.mapper.TireBrandMapper;
import org.platform.vehicle.mapper.TireSpecMapper;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.VehicleSpecMapper;
import org.platform.vehicle.mapper.WarehouseSettingMapper;
import org.platform.vehicle.param.AssetTireConditionQueryParam;
import org.platform.vehicle.param.AssetTireFitConditionQuery;
import org.platform.vehicle.param.AssetTireSensorBindParam;
import org.platform.vehicle.param.AssetTireStockOutParam;
import org.platform.vehicle.param.InstallTireParam;
import org.platform.vehicle.param.TireDeviceBindRecordAddParam;
import org.platform.vehicle.param.UninstallTireParam;
import org.platform.vehicle.service.AssetTireBaseService;
import org.platform.vehicle.service.AssetTireDeviceBindRecordService;
import org.platform.vehicle.service.AssetTireService;
import org.platform.vehicle.service.AssetTireStockRecordBaseService;
import org.platform.vehicle.service.UploadFileService;
import org.platform.vehicle.util.AssetTireDeviceRecordAsync;
import org.platform.vehicle.util.AssetTireFitRecordAsync;
import org.platform.vehicle.util.AssetTireStockRecordAsync;
import org.platform.vehicle.util.EasyExcelUtils;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.util.TireSiteUtil;
import org.platform.vehicle.vo.AssertTireDetailVo;
import org.platform.vehicle.vo.AssetTireBatchInstallExcelVo;
import org.platform.vehicle.vo.AssetTireBatchStockInExcelVo;
import org.platform.vehicle.vo.AssetTireEditParam;
import org.platform.vehicle.vo.AssetTireFitDetailVo;
import org.platform.vehicle.vo.AssetTireFitPageVo;
import org.platform.vehicle.vo.AssetTirePageExportVo;
import org.platform.vehicle.vo.AssetTirePageVo;
import org.platform.vehicle.vo.AssetTireStockInParam;
import org.platform.vehicle.vo.FleetWarehouseVo;
import org.platform.vehicle.vo.ImportedDataErrVo;
import org.platform.vehicle.vo.ImportedResultVo;
import org.platform.vehicle.vo.TireSiteResult;
import org.platform.vehicle.vo.TireSiteSimpleVo;
import org.platform.vehicle.vo.VehicleTireDetailVo;
import org.platform.vehicle.vo.WarehouseSimpleVo;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.DateUtils;
import org.platform.vehicle.utils.IdWorker;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

/**
 * (AssetTire)表服务实现类
 *
 * @author geforever
 * @since 2023-09-14 14:42:31
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireServiceImpl implements AssetTireService {

    private final WarehouseSettingMapper warehouseSettingMapper;
    private final AssetTireMapper assetTireMapper;
    private final SysCustomerMapper sysCustomerMapper;
    private final TireBrandMapper tireBrandMapper;
    private final TireSpecMapper tireSpecMapper;
    private final AssetTireStockRecordMapper assetTireStockRecordMapper;
    private final AssetTireDeviceBindRecordService assetTireDeviceBindRecordService;
    private final VehicleMapper vehicleMapper;
    private final VehicleSpecMapper vehicleSpecMapper;
    private final AssetTireBaseService assetTireBaseService;
    private final AssetTireFitRecordMapper assetTireFitRecordMapper;
    private final IdWorker ID_WORKER = new IdWorker();
    private final OperateLogAsync operateLogAsync;
    private final UploadFileService uploadFileService;
    private final AssetTireStockRecordBaseService assetTireStockRecordBaseService;
    private final AssetTireStockRecordAsync assetTireStockRecordAsync;
    private final AssetTireDeviceRecordAsync assetTireDeviceRecordAsync;
    private final AssetTireFitRecordAsync assetTireFitRecordAsync;
    private final AssetTireJdbc assetTireJdbc;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SysCustomerUserMapper sysCustomerUserMapper;

    /**
     * 资产管理-轮胎列表-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse<List<AssetTirePageVo>> conditionQuery(
            AssetTireConditionQueryParam param) {
        Page<AssetTire> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<AssetTire> wrapper = this.getAssetTireWrapper(param);
        Page<AssetTire> assetTirePage = assetTireMapper.selectPage(page, wrapper);
        List<AssetTire> assetTireList = assetTirePage.getRecords();
        if (assetTireList.isEmpty()) {
            return BasePageResponse.ok(assetTireList, page);
        }
        // 查询客户信息
        List<SysCustomer> sysCustomerList = this.getClientList(assetTireList);
        // 查询车队信息
        List<SysCustomer> fleetList = this.getFleetList(assetTireList);
        // 查询仓库信息
        List<WarehouseSetting> warehouseSettingList = this.getWarehouseSettingList(assetTireList);
        // 查询轮胎品牌
        List<TireBrandEntity> tireBrandList = this.getTireBrandList(assetTireList);
        // 查询轮胎规格
        List<TireSpecEntity> tireSpecList = this.getTireSpecList(assetTireList);
        List<AssetTirePageVo> assetTirePageVoList = new ArrayList<>();
        for (AssetTire assetTire : assetTireList) {
            AssetTirePageVo assetTirePageVo = this.getAssetTirePageVo(assetTire, sysCustomerList,
                    fleetList, warehouseSettingList, tireBrandList, tireSpecList);
            assetTirePageVoList.add(assetTirePageVo);
        }
        return BasePageResponse.ok(assetTirePageVoList, page);
    }

    /**
     * 资产管理-轮胎列表-轮胎入库
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse stockIn(AssetTireStockInParam param, HttpServletRequest request) {
        UserVo user = UserContext.getUser();
        this.checkTireStockInParam(param, user.getCompanyId());
        // 查询车队记录
        SysCustomer fleet = sysCustomerMapper.selectById(param.getFleetId());
        if (fleet == null) {
            throw new BaseException("99999", "车队不存在");
        }
        AssetTire assetTire = this.saveAssetTire(param, fleet, user);
        // 存在仓库,保存入库记录
        if (param.getWarehouseId() != null) {
            this.saveStockInRecord(param, fleet, user);
        }
        // 保存操作日志
        operateLogAsync.pushAddLog(
                assetTire,
                OperateModuleEnum.ASSET_TIRE_STOCK_IN,
                String.valueOf(assetTire.getId()),
                user,
                request);
        return BaseResponse.ok();
    }

    private AssetTire saveAssetTire(AssetTireStockInParam param, SysCustomer fleet, UserVo user) {
        AssetTire assetTire = new AssetTire();
        assetTire.setCode(param.getTireCode());
        assetTire.setClientId(fleet.getCompanyId());
        assetTire.setFleetId(fleet.getId());
        assetTire.setWarehouseId(param.getWarehouseId());
        assetTire.setTireBrandId(param.getTireBrandId());
        assetTire.setTireSpecId(param.getTireSpecId());
        assetTire.setDegree(param.getDegree());
        assetTire.setMileage(param.getMileage());
//        assetTire.setSensorId(param.getSensorId());
//        assetTire.setHasSensor(
//                StringUtils.isNotBlank(param.getSensorId()) ? AssetTireConstant.IS_BIND_SENSOR
//                        : AssetTireConstant.NOT_BIND_SENSOR);
//        assetTire.setSensorType(AssetTireConstant.SENSOR_BIND_TYPE_BUNDLE);
        assetTire.setTireStatus(AssetTireConstant.TIRE_STATUS_WAREHOUSE);
        assetTire.setCreatePerson(user.getName());
        assetTire.setUpdatePerson(user.getName());
        assetTireMapper.insert(assetTire);
        return assetTire;
    }

    /**
     * 保存入库记录
     *
     * @param param
     * @param fleet
     * @param user
     */
    private void saveStockInRecord(AssetTireStockInParam param, SysCustomer fleet,
            UserVo user) {
        // 查询客户记录
        SysCustomer client = sysCustomerMapper.selectById(fleet.getCompanyId());
        // 查询仓库记录
        WarehouseSetting warehouseSetting = warehouseSettingMapper.selectById(
                param.getWarehouseId());
        // 查询轮胎品牌
        TireBrandEntity tireBrand = tireBrandMapper.selectById(param.getTireBrandId());
        // 查询轮胎规格
        TireSpecEntity tireSpec = tireSpecMapper.selectById(param.getTireSpecId());
        this.saveStockIn(param, fleet, user, client, warehouseSetting, tireBrand, tireSpec);
    }

    /**
     * 保存入库记录
     *
     * @param param
     * @param fleet
     * @param client
     * @param tireBrand
     * @param user
     */
    private void saveStockInRecord(AssetTireStockInParam param, SysCustomer fleet,
            SysCustomer client, TireBrandEntity tireBrand, UserVo user) {
        // 查询仓库记录
        WarehouseSetting warehouseSetting = warehouseSettingMapper.selectById(
                param.getWarehouseId());
        // 查询轮胎规格
        TireSpecEntity tireSpec = null;
        if (param.getTireSpecId() != null) {
            tireSpec = tireSpecMapper.selectById(param.getTireSpecId());
        }
        this.saveStockIn(param, fleet, user, client, warehouseSetting, tireBrand, tireSpec);
    }

    private AssetTireStockRecord saveStockIn(AssetTireStockInParam param, SysCustomer fleet,
            UserVo user, SysCustomer client, WarehouseSetting warehouseSetting,
            TireBrandEntity tireBrand, TireSpecEntity tireSpec) {
        AssetTireStockRecord assetTireStockRecord = new AssetTireStockRecord();
        assetTireStockRecord.setTireCode(param.getTireCode());
        assetTireStockRecord.setFleetId(param.getFleetId());
        assetTireStockRecord.setClientName(client.getName());
        assetTireStockRecord.setFleetName(fleet.getName());
        assetTireStockRecord.setWarehouseId(param.getWarehouseId());
        assetTireStockRecord.setWarehouseName(warehouseSetting.getName());
        assetTireStockRecord.setType(AssetTireConstant.STOCK_RECORD_TYPE_IN);
        assetTireStockRecord.setStockType(param.getStockInType());
        assetTireStockRecord.setTarget(param.getTarget());
        assetTireStockRecord.setTireBrand(tireBrand.getBrandName());
        if (tireSpec != null) {
            assetTireStockRecord.setTireSpec(tireSpec.getSpecName());
        }
        assetTireStockRecord.setDegree(param.getDegree());
        assetTireStockRecord.setMileage(param.getMileage());
        assetTireStockRecord.setCreatePerson(user.getName());
        assetTireStockRecordMapper.insert(assetTireStockRecord);
        return assetTireStockRecord;
    }

    /**
     * 轮胎入库参数校验
     *
     * @param param
     * @param customerId
     */
    private void checkTireStockInParam(AssetTireStockInParam param, Integer customerId) {
        // 轮胎号重复校验
        Long usedTireCode = assetTireMapper.selectCount(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getCode, param.getTireCode())
                        .eq(AssetTire::getClientId, customerId)
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        if (usedTireCode > 0) {
            throw new BaseException("99999", "轮胎号已存在");
        }
        // 如果有传感器ID则校验数据重复:一个传感器ID只能绑定一个轮胎
//        if (StringUtils.isNotBlank(param.getSensorId())) {
//            Long usedSensorId = assetTireMapper.selectCount(
//                    new LambdaQueryWrapper<AssetTire>()
//                            .eq(AssetTire::getSensorId, param.getSensorId()));
//            if (usedSensorId > 0) {
//                throw new BaseException("99999", "传感器ID已绑定轮胎");
//            }
//        }
    }

    /**
     * 资产管理-轮胎列表-获取轮胎编码(轮胎入库是获取轮胎编码用)
     *
     * @return
     */
    @Override
    public BaseResponse getTireCode() {
        String newTireCode = "L" + ID_WORKER.nextId();
        return BaseResponse.ok(newTireCode);
    }

    /**
     * 资产管理-轮胎列表-获取明细
     *
     * @param id
     * @return
     */
    @Override
    public BaseResponse<AssertTireDetailVo> getAssetTireDetail(Integer id) {
        AssetTire assetTire = assetTireMapper.selectById(id);
        // 查询车队信息
        SysCustomer fleet = sysCustomerMapper.selectById(assetTire.getFleetId());
        // 查询客户信息
        SysCustomer client = sysCustomerMapper.selectById(assetTire.getClientId());
        // 查询仓库信息
        WarehouseSetting warehouseSetting = null;
        if (assetTire.getWarehouseId() != null) {
            warehouseSetting = warehouseSettingMapper.selectById(assetTire.getWarehouseId());
        }
        // 查询轮胎品牌
        TireBrandEntity tireBrand = tireBrandMapper.selectById(assetTire.getTireBrandId());
        // 查询轮胎规格
        TireSpecEntity tireSpec = null;
        if (assetTire.getTireSpecId() != null) {
            tireSpec = tireSpecMapper.selectById(assetTire.getTireSpecId());
        }
        AssertTireDetailVo assertTireDetailVo = new AssertTireDetailVo();
        assertTireDetailVo.setId(assetTire.getId());
        assertTireDetailVo.setCode(assetTire.getCode());
        assertTireDetailVo.setClientName(client.getName());
        assertTireDetailVo.setFleetName(fleet.getName());
        if (warehouseSetting != null) {
            assertTireDetailVo.setWarehouseName(warehouseSetting.getName());
        }
        assertTireDetailVo.setLicensePlate(assetTire.getLicensePlate());
        assertTireDetailVo.setTireSiteName(assetTire.getTireSiteName());
        assertTireDetailVo.setTireSiteTypeName(assetTire.getTireSiteTypeName());
        assertTireDetailVo.setTireBrandName(tireBrand.getBrandName());
        assertTireDetailVo.setTireSpecName(tireSpec == null ? "" : tireSpec.getSpecName());
        assertTireDetailVo.setDegree(assetTire.getDegree());
        assertTireDetailVo.setMileage(assetTire.getMileage());
        assertTireDetailVo.setSensorId(assetTire.getSensorId());
        assertTireDetailVo.setHasSensorId(assetTire.getHasSensor());
        assertTireDetailVo.setTireStatus(assetTire.getTireStatus());
        return BaseResponse.ok(assertTireDetailVo);
    }

    /**
     * 资产管理-轮胎列表-轮胎编辑
     *
     * @param param
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(AssetTireEditParam param, HttpServletRequest request) {
        UserVo user = UserContext.getUser();
        AssetTire assetTire = assetTireMapper.selectById(param.getId());
        if (assetTire == null) {
            throw new BaseException("99999", "轮胎不存在");
        }
        this.checkEditParam(param);
        AssetTire newAssetTire = this.updateAssetTire(param, assetTire, user);
        this.saveTireDeviceBindRecord(param, assetTire, user);
        // 保存日志
        operateLogAsync.pushEditLog(
                AssetTire.class,
                assetTire,
                newAssetTire,
                OperateModuleEnum.ASSET_TIRE_EDIT,
                String.valueOf(param.getId()),
                user,
                request);
        return BaseResponse.ok();
    }

    private void saveTireDeviceBindRecord(AssetTireEditParam param, AssetTire assetTire,
            UserVo user) {
        if (StringUtils.isNotBlank(param.getSensorId())) {
            if (!param.getSensorId().equals(assetTire.getSensorId())) {
                // 保存绑定记录
                TireDeviceBindRecordAddParam tireDeviceBindRecordAddParam = new TireDeviceBindRecordAddParam();
                tireDeviceBindRecordAddParam.setCode(param.getSensorId());
                tireDeviceBindRecordAddParam.setClientId(assetTire.getClientId());
                tireDeviceBindRecordAddParam.setFleetId(assetTire.getFleetId());
                tireDeviceBindRecordAddParam.setDeviceType(AssetTireConstant.DEVICE_TYPE_SENSOR);
                tireDeviceBindRecordAddParam.setTireSiteName(assetTire.getTireSiteName());
                tireDeviceBindRecordAddParam.setLicensePlate(assetTire.getLicensePlate());
                tireDeviceBindRecordAddParam.setTireCode(param.getTireCode());
                tireDeviceBindRecordAddParam.setCreatePerson(user.getName());
                assetTireDeviceBindRecordService.save(tireDeviceBindRecordAddParam);
            }
        }
    }

    private void checkEditParam(AssetTireEditParam param) {
        // 轮胎号重复校验
        Long usedTireCode = assetTireMapper.selectCount(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getCode, param.getTireCode())
                        .ne(AssetTire::getId, param.getId()));
        if (usedTireCode > 0) {
            throw new BaseException("99999", "轮胎号已存在");
        }
        // 校验传感器是否存在
        if (StringUtils.isNotBlank(param.getSensorId())) {
            Long usedSensorId = assetTireMapper.selectCount(
                    new LambdaQueryWrapper<AssetTire>()
                            .eq(AssetTire::getSensorId, param.getSensorId())
                            .ne(AssetTire::getId, param.getId()));
            if (usedSensorId > 0) {
                throw new BaseException("99999", "传感器ID已绑定轮胎");
            }
        }
        // 传感器是否被使用
        this.checkEditTireSensorIsUsed(param.getSensorId(), param.getId());
    }

    private void checkEditTireSensorIsUsed(String sensorId, Integer id) {
        if (StringUtils.isNotBlank(sensorId)) {
            Long usedSensorId = assetTireMapper.selectCount(
                    new LambdaQueryWrapper<AssetTire>()
                            .eq(AssetTire::getSensorId, sensorId)
                            .ne(AssetTire::getId, id)
                            .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
            if (usedSensorId > 0) {
                throw new BaseException("99999", "传感器ID已绑定轮胎");
            }
        }
    }

    private AssetTire updateAssetTire(AssetTireEditParam param, AssetTire assetTire, UserVo user) {
        AssetTire updateParam = new AssetTire();
        updateParam.setId(assetTire.getId());
        updateParam.setCode(param.getTireCode());
        updateParam.setTireBrandId(param.getTireBrandId());
        updateParam.setTireSpecId(param.getTireSpecId());
        updateParam.setDegree(param.getDegree());
        updateParam.setMileage(param.getId());
        updateParam.setSensorId(param.getSensorId());
        updateParam.setHasSensor(
                StringUtils.isNotBlank(param.getSensorId()) ? AssetTireConstant.IS_BIND_SENSOR
                        : AssetTireConstant.NOT_BIND_SENSOR);
        updateParam.setSensorType(AssetTireConstant.SENSOR_BIND_TYPE_BUNDLE);
        updateParam.setUpdatePerson(user.getName());
        assetTireMapper.updateById(updateParam);
        return updateParam;
    }

    /**
     * 资产管理-轮胎列表-根据轮胎编号获取传感器ID
     *
     * @param tireCode
     * @return
     */
    @Override
    public BaseResponse getSensorId(String tireCode) {
        AssetTire assetTire = assetTireMapper.selectOne(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getCode, tireCode));
        if (assetTire == null) {
            return BaseResponse.ok("");
        }
        return BaseResponse.ok(StringUtils.isNotBlank(assetTire.getSensorId()) ? assetTire
                .getSensorId() : "");
    }

    /**
     * 资产管理-轮胎列表-传感器解绑捆绑
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse editSensorBind(AssetTireSensorBindParam param) {
        UserVo user = UserContext.getUser();
        AssetTire assetTire = assetTireMapper.selectById(param.getId());
        if (assetTire == null) {
            throw new BaseException("99999", "轮胎不存在");
        }
        // 如果有传感器ID则校验数据重复:一个传感器ID只能绑定一个轮胎
        this.checkEditTireSensorIsUsed(param.getSensorId(), param.getId());
        String sensorId = "";
        // 绑定传感器
        if (param.getIsBindSensor().equals(AssetTireConstant.IS_BIND_SENSOR)) {
            sensorId = param.getSensorId();
        }
        this.updateEdit(param, assetTire, sensorId, user);
        // 解绑传感器如果轮胎记录为使用中,则更新车辆是否全部绑定传感器字段
        if (StringUtils.isNotBlank(assetTire.getLicensePlate())) {
            // 查询车辆信息
            VehicleEntity vehicle = vehicleMapper.selectOne(
                    new LambdaQueryWrapper<VehicleEntity>()
                            .eq(VehicleEntity::getLicensePlate, assetTire.getLicensePlate())
                            .eq(VehicleEntity::getIsDeleted, false));
            if (vehicle != null) {
                VehicleSpecEntity vehicleSpec = vehicleSpecMapper.selectOne(
                        new LambdaQueryWrapper<VehicleSpecEntity>()
                                .eq(VehicleSpecEntity::getId, vehicle.getSpecId())
                                .eq(VehicleSpecEntity::getIsDeleted, false));
                this.updateVehicleTireStatus(assetTire.getLicensePlate(), vehicle, vehicleSpec);
            }
            // 查询车辆规格信息
        }
        // 保存设备变更日志
        this.saveSensorBindRecord(assetTire.getSensorId(), param.getSensorId(), assetTire.getCode(),
                assetTire, user);
        return BaseResponse.ok();
    }

    /**
     * 资产管理-轮胎列表-轮胎出库
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse stockOut(AssetTireStockOutParam param, HttpServletRequest request) {
        UserVo user = UserContext.getUser();
        if (param.getStockOutType() == AssetTireConstant.STOCK_OUT_TYPE_INSTALL) {
            // 出库方式-拆卸安装
            InstallTireParam installTireParam = this.convertStockOutToInstallTireParam(param);
            return this.installTire(installTireParam);
        } else if (param.getStockOutType() == AssetTireConstant.STOCK_OUT_TYPE_TRANSFER) {
            // 调拨
            ImportedResultVo importedResultVo = this.processTransferStockOut(param, user);
            return BaseResponse.ok(importedResultVo);
        } else if (param.getStockOutType() == AssetTireConstant.STOCK_OUT_TYPE_SOLD) {
            // 变卖
            ImportedResultVo importedResultVo = this.processSoldStockOut(param, user);
            return BaseResponse.ok(importedResultVo);
        }
        return BaseResponse.ok();
    }

    private ImportedResultVo processSoldStockOut(AssetTireStockOutParam param, UserVo user) {
        ImportedResultVo importedResultVo = new ImportedResultVo();
        List<String> tireCodeList = param.getTireCodeList();
        if (tireCodeList.isEmpty()) {
            throw new BaseException("99999", "轮胎号不能为空");
        }
        if (tireCodeList.size() > 500) {
            throw new BaseException("99999", "轮胎号不能超过500个");
        }
        List<AssetTire> assetTireList = assetTireMapper.selectList(
                new LambdaQueryWrapper<AssetTire>()
                        .in(AssetTire::getCode, tireCodeList)
                        .in(AssetTire::getFleetId, user.getCustomerIds())
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        // 数据校验
        int repeatCount = 0;
        List<AssetTire> checkThroughTireList = new ArrayList<>();
        List<ImportedDataErrVo> importedDataErrVoList = new ArrayList<>();
        // 校验tireCodeList是否有重复数据
        for (String tireCode : tireCodeList) {
            this.checkTransferData(tireCode, tireCodeList, importedDataErrVoList, assetTireList,
                    checkThroughTireList, repeatCount);
        }
        //  状态修改已变卖
        this.batchUpdateSold(assetTireList, user);
        //  新增出库记录(待用状态不生成出库记录)
        this.batchSaveStockRecord(checkThroughTireList, new WarehouseSetting(), user,
                AssetTireConstant.STOCK_OUT_TYPE_SOLD);
        importedResultVo.setCompleteCount(checkThroughTireList.size());
        importedResultVo.setRepeatCount(repeatCount);
        importedResultVo.setErrorCount(importedDataErrVoList.size());
        importedResultVo.setErrorList(importedDataErrVoList);
        return importedResultVo;
    }

    private ImportedResultVo processTransferStockOut(AssetTireStockOutParam param, UserVo user) {
        ImportedResultVo importedResultVo = new ImportedResultVo();
        Integer warehouseId = param.getWarehouseId();
        List<String> tireCodeList = param.getTireCodeList();
        if (tireCodeList.isEmpty()) {
            throw new BaseException("99999", "轮胎号不能为空");
        }
        if (tireCodeList.size() > 100) {
            throw new BaseException("99999", "轮胎号不能超过100个");
        }
        WarehouseSetting targetWarehouse = warehouseSettingMapper.selectById(warehouseId);
        if (targetWarehouse == null) {
            throw new BaseException("99999", "仓库不存在");
        }
        List<AssetTire> assetTireList = assetTireMapper.selectList(
                new LambdaQueryWrapper<AssetTire>()
                        .in(AssetTire::getCode, tireCodeList)
                        .in(AssetTire::getFleetId, user.getCustomerIds())
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        // 校验数据
        int repeatCount = 0;
        List<AssetTire> checkThroughTireList = new ArrayList<>();
        List<ImportedDataErrVo> importedDataErrVoList = new ArrayList<>();
        // 校验tireCodeList是否有重复数据
        for (String tireCode : tireCodeList) {
            this.checkTransferData(tireCode, tireCodeList, importedDataErrVoList, assetTireList,
                    checkThroughTireList, repeatCount);
        }
        // 状态改为仓库待用,修改仓库ID
        this.batchUpdateTransfer(checkThroughTireList, warehouseId, user);
        // 新增出库记录(待用状态不生成出库记录),入库记录
        this.batchSaveStockRecord(checkThroughTireList, targetWarehouse, user,
                AssetTireConstant.STOCK_OUT_TYPE_TRANSFER);
        importedResultVo.setCompleteCount(checkThroughTireList.size());
        importedResultVo.setRepeatCount(repeatCount);
        importedResultVo.setErrorCount(importedDataErrVoList.size());
        importedResultVo.setErrorList(importedDataErrVoList);
        return importedResultVo;
    }

    private void batchUpdateSold(List<AssetTire> assetTireList, UserVo user) {
        List<AssetTire> batchUpdateParamList = new ArrayList<>();
        for (AssetTire tire : assetTireList) {
            AssetTire updateParam = new AssetTire();
            updateParam.setId(tire.getId());
            updateParam.setTireStatus(AssetTireConstant.TIRE_STATUS_SOLD);
            updateParam.setUpdatePerson(user.getName());
            batchUpdateParamList.add(updateParam);
        }
        assetTireBaseService.saveBatch(batchUpdateParamList);
    }

    private void checkTransferData(String tireCode, List<String> tireCodeList,
            List<ImportedDataErrVo> importedDataErrVoList, List<AssetTire> assetTireList,
            List<AssetTire> checkThroughTireList, int repeatCount) {
        long count = tireCodeList.stream().filter(t -> t.equals(tireCode)).count();
        if (count > 1) {
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(tireCode,
                    "轮胎号在文件中重复");
            importedDataErrVoList.add(importedDataErrVo);
            repeatCount++;
            return;
        }
        // assetTireList中查不到tireCode
        AssetTire tire = null;
        for (AssetTire temp : assetTireList) {
            if (temp.getCode().equals(tireCode)) {
                tire = temp;
                break;
            }
        }
        if (tire == null) {
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(tireCode,
                    "轮胎号不存在或不在您下级车队/仓库");
            importedDataErrVoList.add(importedDataErrVo);
            return;
        }
        // 轮胎状态不为“待用”或“仓库待用”
        if (!tire.getTireStatus().equals(AssetTireConstant.TIRE_STATUS_WAREHOUSE)
                && !tire.getTireStatus().equals(AssetTireConstant.TIRE_STATUS_WAITING)) {
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(tireCode,
                    "轮胎状态不为“待用”或“仓库待用”");
            importedDataErrVoList.add(importedDataErrVo);
            return;
        }
        checkThroughTireList.add(tire);
    }

    private void batchSaveStockRecord(List<AssetTire> checkThroughTireList,
            WarehouseSetting targetWarehouse, UserVo user, int stockOutType) {
        if (checkThroughTireList.isEmpty()) {
            return;
        }
        List<Integer> clientIdList = new ArrayList<>();
        List<Integer> fleetIdList = new ArrayList<>();
        List<Integer> wareHouseIdList = new ArrayList<>();
        List<Integer> brandIdList = new ArrayList<>();
        List<Integer> tireSpecId = new ArrayList<>();
        for (AssetTire tire : checkThroughTireList) {
            clientIdList.add(tire.getClientId());
            fleetIdList.add(tire.getFleetId());
            wareHouseIdList.add(tire.getFleetId());
            brandIdList.add(tire.getTireBrandId());
            if (tire.getTireSpecId() != null) {
                tireSpecId.add(tire.getTireSpecId());
            }
        }
        // 查询客户记录
        List<SysCustomer> clientList = sysCustomerMapper.selectBatchIds(clientIdList);
        // 查询车队记录
        List<SysCustomer> fleetList = sysCustomerMapper.selectBatchIds(fleetIdList);
        // 查询仓库记录
        List<WarehouseSetting> warehouseSettingList = warehouseSettingMapper.selectBatchIds(
                wareHouseIdList);
        // 查询轮胎品牌
        List<TireBrandEntity> tireBrandList = tireBrandMapper.selectBatchIds(brandIdList);
        // 查询轮胎规格
        List<TireSpecEntity> tireSpecList = new ArrayList<>();
        if (!tireSpecId.isEmpty()) {
            tireSpecList = tireSpecMapper.selectBatchIds(tireSpecId);
        }
        // 2-调拨出库,3-变卖出库
        if (stockOutType == AssetTireConstant.STOCK_OUT_TYPE_TRANSFER
                || stockOutType == AssetTireConstant.STOCK_OUT_TYPE_SOLD) {
            this.batchInsertStockOutRecord(checkThroughTireList, clientList, fleetList,
                    warehouseSettingList, tireBrandList, tireSpecList, targetWarehouse.getName(),
                    user.getName());
        }
        // 2-调拨出库
        if (stockOutType == AssetTireConstant.STOCK_OUT_TYPE_TRANSFER) {
            this.batchInsertStockInRecord(checkThroughTireList, clientList, fleetList,
                    warehouseSettingList, tireBrandList, tireSpecList, targetWarehouse.getId(),
                    targetWarehouse.getName(), user.getName());
        }
    }

    private void batchInsertStockInRecord(List<AssetTire> checkThroughTireList,
            List<SysCustomer> clientList, List<SysCustomer> fleetList,
            List<WarehouseSetting> warehouseSettingList, List<TireBrandEntity> tireBrandList,
            List<TireSpecEntity> tireSpecList,
            Integer targetWarehouseId, String targetWarehouseName, String userName) {
        List<AssetTireStockRecord> assetTireStockRecordList = new ArrayList<>();
        for (AssetTire assetTire : checkThroughTireList) {
            // 查询客户记录
            SysCustomer client = clientList.stream()
                    .filter(c -> c.getId().equals(assetTire.getClientId()))
                    .findFirst()
                    .orElse(null);
            // 查询车队记录
            SysCustomer fleet = fleetList.stream()
                    .filter(f -> f.getId().equals(assetTire.getFleetId()))
                    .findFirst()
                    .orElse(null);
            // 查询仓库记录
            WarehouseSetting warehouseSetting = warehouseSettingList.stream()
                    .filter(w -> w.getId().equals(assetTire.getWarehouseId()))
                    .findFirst()
                    .orElse(null);
            // 查询轮胎品牌
            TireBrandEntity tireBrand = tireBrandList.stream()
                    .filter(b -> b.getId().equals(assetTire.getTireBrandId()))
                    .findFirst()
                    .orElse(null);
            // 查询轮胎规格
            TireSpecEntity tireSpec = tireSpecList.stream()
                    .filter(s -> s.getId().equals(assetTire.getTireSpecId()))
                    .findFirst()
                    .orElse(null);
            AssetTireStockRecord assetTireStockRecord = new AssetTireStockRecord();
            assetTireStockRecord.setTireCode(assetTire.getCode());
            if (fleet != null) {
                assetTireStockRecord.setFleetId(fleet.getId());
                assetTireStockRecord.setFleetName(fleet.getName());
            }
            if (client != null) {
                assetTireStockRecord.setClientName(client.getName());
            }
            assetTireStockRecord.setWarehouseId(targetWarehouseId);
            assetTireStockRecord.setWarehouseName(targetWarehouseName);
            assetTireStockRecord.setType(AssetTireConstant.STOCK_RECORD_TYPE_IN);
            if (warehouseSetting != null) {
                assetTireStockRecord.setTarget(warehouseSetting.getName());
            }
            assetTireStockRecord.setStockType(AssetTireConstant.STOCK_IN_TYPE_TRANSFER);
            if (tireBrand != null) {
                assetTireStockRecord.setTireBrand(tireBrand.getBrandName());
            }
            if (tireSpec != null) {
                assetTireStockRecord.setTireSpec(tireSpec.getSpecName());
            }
            assetTireStockRecord.setDegree(assetTire.getDegree());
            assetTireStockRecord.setMileage(assetTire.getMileage());
            assetTireStockRecord.setCreatePerson(userName);
            assetTireStockRecordList.add(assetTireStockRecord);
        }
        assetTireStockRecordBaseService.saveBatch(assetTireStockRecordList);

    }

    private void batchInsertStockOutRecord(List<AssetTire> checkThroughTireList,
            List<SysCustomer> clientList, List<SysCustomer> fleetList,
            List<WarehouseSetting> warehouseSettingList, List<TireBrandEntity> tireBrandList,
            List<TireSpecEntity> tireSpecList,
            String target, String userName) {
        List<AssetTireStockRecord> assetTireStockRecordList = new ArrayList<>();
        for (AssetTire assetTire : checkThroughTireList) {
            if (assetTire.getTireStatus() == AssetTireConstant.TIRE_STATUS_WAITING) {
                return;
            }
            // 查询客户记录
            SysCustomer client = clientList.stream()
                    .filter(c -> c.getId().equals(assetTire.getClientId()))
                    .findFirst()
                    .orElse(null);
            // 查询车队记录
            SysCustomer fleet = fleetList.stream()
                    .filter(f -> f.getId().equals(assetTire.getFleetId()))
                    .findFirst()
                    .orElse(null);
            // 查询仓库记录
            WarehouseSetting warehouseSetting = warehouseSettingList.stream()
                    .filter(w -> w.getId().equals(assetTire.getWarehouseId()))
                    .findFirst()
                    .orElse(null);
            // 查询轮胎品牌
            TireBrandEntity tireBrand = tireBrandList.stream()
                    .filter(b -> b.getId().equals(assetTire.getTireBrandId()))
                    .findFirst()
                    .orElse(null);
            // 查询轮胎规格
            TireSpecEntity tireSpec = tireSpecList.stream()
                    .filter(s -> s.getId().equals(assetTire.getTireSpecId()))
                    .findFirst()
                    .orElse(null);
            AssetTireStockRecord assetTireStockRecord = new AssetTireStockRecord();
            assetTireStockRecord.setTireCode(assetTire.getCode());
            if (fleet != null) {
                assetTireStockRecord.setFleetId(fleet.getId());
                assetTireStockRecord.setFleetName(fleet.getName());
            }
            if (client != null) {
                assetTireStockRecord.setClientName(client.getName());
            }
            if (warehouseSetting != null) {
                assetTireStockRecord.setWarehouseId(warehouseSetting.getId());
                assetTireStockRecord.setWarehouseName(warehouseSetting.getName());
            }
            assetTireStockRecord.setType(AssetTireConstant.STOCK_RECORD_TYPE_OUT);
            assetTireStockRecord.setStockType(AssetTireConstant.STOCK_OUT_TYPE_TRANSFER);
            assetTireStockRecord.setTarget(target);
            if (tireBrand != null) {
                assetTireStockRecord.setTireBrand(tireBrand.getBrandName());
            }
            if (tireSpec != null) {
                assetTireStockRecord.setTireSpec(tireSpec.getSpecName());
            }
            assetTireStockRecord.setDegree(assetTire.getDegree());
            assetTireStockRecord.setMileage(assetTire.getMileage());
            assetTireStockRecord.setCreatePerson(userName);
            assetTireStockRecordList.add(assetTireStockRecord);
        }
        assetTireStockRecordBaseService.saveBatch(assetTireStockRecordList);
    }

    private void batchUpdateTransfer(List<AssetTire> assetTireList, Integer warehouseId,
            UserVo user) {
        List<AssetTire> batchUpdateParamList = new ArrayList<>();
        for (AssetTire tire : assetTireList) {
            AssetTire updateParam = new AssetTire();
            updateParam.setId(tire.getId());
            updateParam.setWarehouseId(warehouseId);
            updateParam.setTireStatus(AssetTireConstant.TIRE_STATUS_WAREHOUSE);
            updateParam.setUpdatePerson(user.getName());
            batchUpdateParamList.add(updateParam);
        }
        if (batchUpdateParamList.isEmpty()) {
            assetTireBaseService.saveBatch(batchUpdateParamList);
        }
    }

    private InstallTireParam convertStockOutToInstallTireParam(AssetTireStockOutParam param) {
        InstallTireParam installTireParam = new InstallTireParam();
        installTireParam.setTireSite(param.getTireSite());
//        installTireParam.setTireSiteName(param.getTireSiteName());
//        installTireParam.setTireSiteType(param.getTireSiteType());
//        installTireParam.setTireSiteTypeName(param.getTireSiteTypeName());
        installTireParam.setLicensePlate(param.getLicensePlate());
//        installTireParam.setVehicleSpecId();
        installTireParam.setTireCode(param.getTireCode());
        installTireParam.setSensorId(param.getSensorId());
        if (StringUtils.isBlank(param.getTarget())) {
            installTireParam.setTarget(param.getLicensePlate());
        } else {
            installTireParam.setTarget(param.getTarget());
        }
        return installTireParam;
    }

    /**
     * 资产管理-轮胎列表-删除
     *
     * @param id
     * @param phone
     * @return
     */
    @Override
    public BaseResponse delete(Integer id, String phone, HttpServletRequest request) {
        UserVo user = UserContext.getUser();
        // 校验手机号
        if (!user.getPhone().equals(phone)) {
            return BaseResponse.failure("手机号不正确");
        }
        // 当前轮胎已绑定了传感器ID或者车辆时，不允许删除
        AssetTire assetTire = assetTireMapper.selectById(id);
        if (assetTire == null) {
            return BaseResponse.failure("轮胎不存在");
        }
        if (StringUtils.isNotBlank(assetTire.getSensorId())) {
            return BaseResponse.failure("当前轮胎已绑定传感器或已安装到车上不能删除");
        }
        if (assetTire.getHasSensor() == AssetTireConstant.IS_BIND_SENSOR) {
            return BaseResponse.failure("当前轮胎已绑定传感器或已安装到车上不能删除");
        }
        if (assetTire.getTireSite() != null) {
            return BaseResponse.failure("当前轮胎已绑定传感器或已安装到车上不能删除");
        }
        AssetTire updateParam = new AssetTire();
        updateParam.setId(assetTire.getId());
        updateParam.setIsDelete(AssetTireConstant.IS_DELETE);
        updateParam.setUpdatePerson(user.getName());
        assetTireMapper.updateById(updateParam);
        // 保存日志
        operateLogAsync.pushDeleteLog(
                OperateModuleEnum.ASSET_TIRE_DELETE,
                String.valueOf(assetTire.getId()),
                user,
                request,
                assetTire.getCode());
        return BaseResponse.ok();
    }

    /**
     * 资产管理-轮胎拆装-车辆分页查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse<List<AssetTireFitPageVo>> fitConditionQuery(
            AssetTireFitConditionQuery param) {
        UserVo user = UserContext.getUser();
        param.setCustomerIds(user.getCustomerIds());
        // 查询车辆档案信息
        Page<AssetTireFitDto> page = new Page<>(param.getPageNum(), param.getPageSize());
        Page<AssetTireFitDto> assetTireFitDtoPage = assetTireMapper.fitConditionQuery(page, param);
        List<AssetTireFitDto> records = assetTireFitDtoPage.getRecords();
        List<AssetTireFitPageVo> assetTireFitPageVoList = this.getAssetTireFitPageVos(
                records);
        return BasePageResponse.ok(assetTireFitPageVoList, page);
    }

    private List<AssetTireFitPageVo> getAssetTireFitPageVos(List<AssetTireFitDto> records) {
        List<AssetTireFitPageVo> assetTireFitPageVoList = new ArrayList<>();
        for (AssetTireFitDto record : records) {
            AssetTireFitPageVo assetTireFitPageVo = new AssetTireFitPageVo();
            assetTireFitPageVo.setLicensePlate(record.getLicensePlate());
            assetTireFitPageVo.setGuaLicensePlate(record.getGuaLicensePlate());
            assetTireFitPageVo.setVehicleSpecId(record.getVehicleSpecId());
            assetTireFitPageVo.setColor(this.getBindStatusErrColor(record.getHasWheelCount(),
                    record.getWheelsComplete(), record.getSensorsComplete()));
            assetTireFitPageVoList.add(assetTireFitPageVo);
        }
        return assetTireFitPageVoList;
    }

    /**
     * 资产管理-轮胎拆装-安装轮胎
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse installTire(InstallTireParam param) {
        // 接受人为空,则默认为车牌号
        if (StringUtils.isBlank(param.getTarget())) {
            param.setTarget(param.getLicensePlate());
        }
        UserVo user = UserContext.getUser();
        // 查询轮胎信息,轮胎号+客户全局唯一
        AssetTire assetTire = assetTireMapper.selectOne(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getCode, param.getTireCode())
                        .in(AssetTire::getFleetId, user.getCustomerIds())
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        if (assetTire == null) {
            return BasePageResponse.failure("轮胎不存在");
        }
        // 查询车辆信息
        VehicleEntity vehicle = vehicleMapper.selectOne(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getLicensePlate, param.getLicensePlate())
                        .eq(VehicleEntity::getIsDeleted, false));
        if (vehicle == null) {
            return BasePageResponse.failure("车辆不存在");
        }
        // 查询车辆规格信息
        VehicleSpecEntity vehicleSpec = vehicleSpecMapper.selectOne(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .eq(VehicleSpecEntity::getId, vehicle.getSpecId())
                        .eq(VehicleSpecEntity::getIsDeleted, false));
        // 校验轮胎
        this.checkTire(param, assetTire);
        // 获取轮位信息
        TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(param.getTireSite(),
                vehicleSpec.getSpecType(), vehicleSpec.getWheelCount(),
                vehicleSpec.getWheelbaseType());
        // 轮胎列表更新为使用中
        AssetTire newAssetTire = this.saveInstalledTire(assetTire, param, tireSiteResult, user);
        // 传感器绑定记录:校验轮胎是否已经绑定传感器
        if (StringUtils.isNotBlank(param.getSensorId())) {
            // 保存传感器绑定记录
            this.saveSensorBindRecord(assetTire.getSensorId(), param.getSensorId(),
                    param.getTireCode(),
                    newAssetTire, user);
        }
        // 出库记录,若当前轮胎为“仓库待用”状态，出库记录，接收人为车牌号，出库人为“当前登录人”
        this.saveStockOutRecord(
                AssetTireConstant.TIRE_STATUS_WAREHOUSE,
                AssetTireConstant.STOCK_OUT_TYPE_INSTALL,
                param.getLicensePlate(),
                param.getTarget(),
                tireSiteResult == null ? "" : tireSiteResult.getTireSiteName(),
                assetTire,
                user,
                "");
        // 更新车辆轮胎、传感器数据
        this.updateVehicleTireStatus(param.getLicensePlate(), vehicle, vehicleSpec);
        return BaseResponse.ok();
    }

    private void checkTire(InstallTireParam param, AssetTire assetTire) {
        // 根据车牌查询轮位是否重复安装
        Long tireCount = assetTireMapper.selectCount(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getLicensePlate, param.getLicensePlate())
                        .eq(AssetTire::getTireSite, param.getTireSite())
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        if (tireCount > 0) {
            throw new BaseException("99999", "轮位已安装轮胎");
        }
        // 查询该传感器是否已经绑定了其他轮胎
        Long sensorCount = assetTireMapper.selectCount(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getSensorId, param.getSensorId())
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        if (sensorCount > 0) {
            throw new BaseException("99999", "传感器已绑定其他轮胎");
        }
        // 校验轮胎入库状态是否正确:1-仓库待用 or null
        if (assetTire.getTireStatus() != null) {
            if (assetTire.getTireStatus() != AssetTireConstant.TIRE_STATUS_WAREHOUSE &&
                    assetTire.getTireStatus() != AssetTireConstant.TIRE_STATUS_WAITING) {
                throw new BaseException("99999", "轮胎状态不正确");
            }
        }
        if (assetTire.getTireSite() != null) {
            throw new BaseException("99999", "轮胎已被使用");
        }
    }

    /**
     * 更新车辆轮胎、传感器数据
     *
     * @param licensePlate
     * @param vehicle
     * @param vehicleSpec
     */
    private void updateVehicleTireStatus(String licensePlate, VehicleEntity vehicle,
            VehicleSpecEntity vehicleSpec) {
        List<AssetTire> assetTireList = assetTireMapper.selectList(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getLicensePlate, licensePlate)
                        .eq(AssetTire::getTireSite, AssetTireConstant.TIRE_STATUS_USING)
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        // 获取车辆轮胎总数
        int totalWheelCount = this.getTotalWheelCount(vehicle, vehicleSpec);
        VehicleEntity updateParam = new VehicleEntity();
        updateParam.setId(vehicle.getId());
        // 判断轮胎是否全部安装
        if (this.checkTireComplete(assetTireList, totalWheelCount)) {
            // 更新车辆状态为已安装
            updateParam.setWheelsComplete(true);
        } else {
            return;
        }
        // 判断轮胎是否全部安装传感器
        if (this.checkSensorComplete(licensePlate, assetTireList, totalWheelCount)) {
            updateParam.setSensorsComplete(true);
        }
        // 必填字段更新
        updateParam.setRunRoute(vehicle.getRunRoute());
        updateParam.setRepeaterIdNumber(vehicle.getRepeaterIdNumber());
        updateParam.setTrailerRepeaterIdNumber(vehicle.getTrailerRepeaterIdNumber());
        updateParam.setReceiverIdNumber(vehicle.getReceiverIdNumber());
        // 更新车辆状态为已安装
        vehicleMapper.updateById(updateParam);
    }

    private boolean checkTireComplete(List<AssetTire> assetTireList, int totalWheelCount) {
        int installedTireCount = assetTireList.size();
        // 如果轮胎全部安装则更新字段,否则直接返回
        return installedTireCount == totalWheelCount;
    }

    private int getTotalWheelCount(VehicleEntity vehicle, VehicleSpecEntity vehicleSpec) {
        String wheelCount = vehicleSpec.getWheelCount();
        // 根据,号分割并相加
        String[] split = wheelCount.split(",");
        int totalWheelCount = 0;
        for (String s : split) {
            totalWheelCount += Integer.parseInt(s);
        }
        return totalWheelCount;
    }

    private boolean checkSensorComplete(String licensePlate, List<AssetTire> assetTireList,
            int totalWheelCount) {
        List<Integer> tireIdList = new ArrayList<>();
        for (AssetTire assetTire : assetTireList) {
            tireIdList.add(assetTire.getId());
        }
        Long installedSensorCount = assetTireMapper.selectCount(
                new LambdaQueryWrapper<AssetTire>()
                        .in(AssetTire::getId, tireIdList)
                        .eq(AssetTire::getLicensePlate, licensePlate)
                        .eq(AssetTire::getHasSensor, AssetTireConstant.IS_BIND_SENSOR)
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        return installedSensorCount == totalWheelCount;
    }

    private AssetTire saveInstalledTire(AssetTire assetTire, InstallTireParam param,
            TireSiteResult tireSiteResult, UserVo user) {
        AssetTire updateParam = new AssetTire();
        updateParam.setId(assetTire.getId());
        updateParam.setLicensePlate(param.getLicensePlate());
        updateParam.setTireSite(param.getTireSite());
        if (tireSiteResult != null) {
            updateParam.setTireSiteName(tireSiteResult.getTireSiteName());
            updateParam.setTireSiteType(tireSiteResult.getTireSiteType());
            updateParam.setTireSiteTypeName(tireSiteResult.getTireSiteTypeName());
        }
        updateParam.setSensorId(param.getSensorId());
        updateParam.setHasSensor(AssetTireConstant.IS_BIND_SENSOR);
        updateParam.setTireStatus(AssetTireConstant.TIRE_STATUS_USING);
        updateParam.setUpdatePerson(user.getName());
        assetTireMapper.updateById(updateParam);
        return updateParam;
    }

    /**
     * 资产管理-轮胎列表-轮胎号模糊查询
     *
     * @param tireCode
     * @return
     */
    @Override
    public BaseResponse<List<String>> getTireCodeLikeTireCode(String tireCode) {
        UserVo user = UserContext.getUser();
        List<AssetTire> assetTireList = assetTireMapper.selectList(
                new LambdaQueryWrapper<AssetTire>()
                        .in(AssetTire::getFleetId, user.getCustomerIds())
                        .like(AssetTire::getCode, tireCode)
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        List<String> tireCodeList = new ArrayList<>();
        for (AssetTire assetTire : assetTireList) {
            tireCodeList.add(assetTire.getCode());
        }
        return BaseResponse.ok(tireCodeList);
    }

    /**
     * 资产管理-轮胎拆装-明细
     *
     * @param licensePlate
     * @return
     */
    @Override
    public BaseResponse<List<AssetTireFitDetailVo>> getFitDetail(String licensePlate) {
        List<AssetTireFitDetailVo> assetTireFitDetailVoList = new ArrayList<>();
        // 根据主车牌查询轮胎列表
        List<AssetTire> mainTireList = assetTireMapper.selectList(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getLicensePlate, licensePlate)
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
//        if (mainTireList.isEmpty()) {
//            return BaseResponse.ok(assetTireFitDetailVoList);
//        }
        // 根据车牌查询车辆档案信息
        VehicleEntity mainVehicle = vehicleMapper.selectOne(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getLicensePlate, licensePlate));
        String guaLicensePlate = mainVehicle.getGuaLicensePlate();
        // 如果挂车存在则查询挂车轮胎列表
        List<AssetTire> minorTireList = new ArrayList<>();
        if (StringUtils.isNotBlank(guaLicensePlate)) {
            minorTireList = assetTireMapper.selectList(
                    new LambdaQueryWrapper<AssetTire>()
                            .eq(AssetTire::getLicensePlate, guaLicensePlate)
                            .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        }
        // 查询轮胎品牌数据
        List<TireBrandEntity> tireBrandList = this.getTireBrand(mainTireList, minorTireList);
        // 获取主车轮胎明细
        AssetTireFitDetailVo mainTireFitDetailVo = this.getAssetTireFitDetailVo(
                mainVehicle.getLicensePlate(), mainVehicle.getReceiverIdNumber(),
                mainVehicle.getRepeaterIdNumber(), mainVehicle.getId(), mainVehicle.getSpecId(),
                mainTireList, tireBrandList);
        assetTireFitDetailVoList.add(mainTireFitDetailVo);
        // 如果存在挂车则获取挂车轮胎明细
        if (StringUtils.isNotBlank(guaLicensePlate)) {
            VehicleEntity minorVehicle = vehicleMapper.selectOne(
                    new LambdaQueryWrapper<VehicleEntity>()
                            .eq(VehicleEntity::getLicensePlate, guaLicensePlate));
            if (minorVehicle != null) {
                AssetTireFitDetailVo minorTireFitDetailVo = this.getAssetTireFitDetailVo(
                        guaLicensePlate, "", mainVehicle.getTrailerRepeaterIdNumber(),
                        minorVehicle.getId(), minorVehicle.getSpecId(), minorTireList,
                        tireBrandList);
                assetTireFitDetailVoList.add(minorTireFitDetailVo);
            }
        }
        return BaseResponse.ok(assetTireFitDetailVoList);
    }

    /**
     * 资产管理-轮胎拆装-拆卸轮胎
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse uninstallTire(UninstallTireParam param) {
        UserVo user = UserContext.getUser();
        // 查询使用中的轮胎信息
        AssetTire assetTire = assetTireMapper.selectOne(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getId, param.getId())
                        .eq(AssetTire::getTireStatus, AssetTireConstant.TIRE_STATUS_USING)
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        if (assetTire == null) {
            return BaseResponse.failure("轮胎不存在");
        }
        SysCustomer fleet = sysCustomerMapper.selectById(assetTire.getFleetId());
        // 查询客户记录
        SysCustomer client = sysCustomerMapper.selectById(fleet.getCompanyId());
        // 查询轮胎品牌
        TireBrandEntity tireBrand = tireBrandMapper.selectById(assetTire.getTireBrandId());
        // 更新轮胎记录
        this.updateAssetTireUninstall(param, user);
        // 存在仓库ID则插入入库记录
        this.insertStockInRecordByWarehouseId(param, assetTire, fleet, client, tireBrand, user);
        // 轮胎装卸记录
        this.saveUninstallRecord(client, fleet, assetTire, tireBrand, user);
        // 查询车辆信息
        this.updateVehicleSensorUnbind(param, assetTire);
        return BaseResponse.ok();
    }

    /**
     * 资产管理-轮胎列表-导入模版下载
     *
     * @param type     :1-轮胎导入,2-轮胎安装
     * @param response
     * @retirm
     */
    @Override
    public void downloadTemplate(Integer type, HttpServletResponse response) {
        String path = "./importTemplate/";
        String fileName = "";
        if (type == 1) {
            fileName = "tire_import_template.xlsx";
        } else if (type == 2) {
            fileName = "tire_install_template.xlsx";
        }
        // 构建文件路径
        Path filePath = Paths.get(path, fileName);
        // 检查文件是否存在
        if (!Files.exists(filePath)) {
            throw new BaseException("99999", "文件不存在");
        }
        // 读取文件并创建输入流
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath.toFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            response.reset();
            //设置输出文件格式
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.ISO_8859_1));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buff = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, length);
            }
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模版文件下载出错" + e.getMessage());
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("关闭资源出错" + e.getMessage());
            }
        }
    }

    /**
     * 资产管理-轮胎列表-批量入库
     *
     * @param file
     * @return
     */
    @Override
    public BaseResponse batchStockIn(MultipartFile file) {
        UserVo user = UserContext.getUser();
        String key = AssetTireConstant.BATCH_STOCK_IN_LOCK_KEY + user.getUserId();
        // 查询redis,判断该用户是否在导入
        Object lock = redisTemplate.opsForValue().get(key);
        if (lock != null) {
            return BaseResponse.failure("数据正在导入,请稍后再试");
        } else {
            // redis锁
            redisTemplate.opsForValue().set(key, "", 1, TimeUnit.HOURS);
        }
        try {
            ImportedResultVo importedResultVo = this.processTireBatchStockIn(file, user);
            return BaseResponse.ok(importedResultVo);
        } catch (Exception e) {
            log.error("轮胎批量导入失败", e);
            return BaseResponse.failure(e.getMessage());
        } finally {
            // 删除redis锁
            redisTemplate.delete(key);
        }
    }

    /**
     * 资产管理-轮胎列表-批量安装
     *
     * @param file
     * @return
     */
    @Override
    public BaseResponse batchInstall(MultipartFile file) {
        UserVo user = UserContext.getUser();
        String key = AssetTireConstant.BATCH_INSTALL_LOCK_KEY + user.getUserId();
        // 查询redis,判断该用户是否在导入
        Object lock = redisTemplate.opsForValue().get(key);
        if (lock != null) {
            return BaseResponse.failure("数据正在导入,请稍后再试");
        } else {
            // redis锁
            redisTemplate.opsForValue().set(key, "", 1, TimeUnit.HOURS);
        }
        try {
            ImportedResultVo importedResultVo = this.processTireBatchInstall(file, user);
            return BaseResponse.ok(importedResultVo);
        } catch (Exception e) {
            log.error("轮胎批量导入失败", e);
            return BaseResponse.failure(e.getMessage());
        } finally {
            // 删除redis锁
            redisTemplate.delete(key);
        }
    }

    /**
     * 资产管理-轮胎列表-导出
     *
     * @param param
     * @return
     */
    @Override
    public void export(AssetTireConditionQueryParam param, HttpServletResponse response) {
        LambdaQueryWrapper<AssetTire> wrapper = this.getAssetTireWrapper(param);
        List<AssetTire> assetTireList = assetTireMapper.selectList(wrapper);
        if (assetTireList.isEmpty()) {
            return;
        }
        // 查询客户信息
        List<SysCustomer> sysCustomerList = this.getClientList(assetTireList);
        // 查询车队信息
        List<SysCustomer> fleetList = this.getFleetList(assetTireList);
        // 查询仓库信息
        List<WarehouseSetting> warehouseSettingList = this.getWarehouseSettingList(assetTireList);
        // 查询轮胎品牌
        List<TireBrandEntity> tireBrandList = this.getTireBrandList(assetTireList);
        // 查询轮胎规格
        List<TireSpecEntity> tireSpecList = this.getTireSpecList(assetTireList);
        List<AssetTirePageExportVo> exportList = new ArrayList<>();
        for (AssetTire assetTire : assetTireList) {
            AssetTirePageExportVo assetTirePageExportVo = this.getAssetTirePageExportVo(assetTire,
                    sysCustomerList, fleetList, warehouseSettingList, tireBrandList, tireSpecList);
            exportList.add(assetTirePageExportVo);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode("轮胎列表", "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-disposition",
                    "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(response.getOutputStream(), AssetTirePageExportVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new ExcelCellWidthStyleStrategy())
                    .registerWriteHandler(EasyExcelUtils.getTitleStyle())
                    .sheet("sheet_1")
                    .doWrite(exportList);
        } catch (Exception e) {
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>(2);
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 资产管理-轮胎列表-选择轮位
     *
     * @param licensePlate
     * @return
     */
    @Override
    public BaseResponse<List<TireSiteSimpleVo>> getTireSite(String licensePlate) {
        // 查询车辆信息
        VehicleEntity vehicle = vehicleMapper.selectOne(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getLicensePlate, licensePlate)
                        .eq(VehicleEntity::getIsDeleted, false));
        if (vehicle == null) {
            return BaseResponse.failure("车辆不存在");
        }
        // 查询车辆规格信息
        VehicleSpecEntity vehicleSpec = vehicleSpecMapper.selectOne(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .eq(VehicleSpecEntity::getId, vehicle.getSpecId())
                        .eq(VehicleSpecEntity::getIsDeleted, false));
        // 查询轮胎列表
        List<AssetTire> assetTireList = assetTireMapper.selectList(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getLicensePlate, licensePlate)
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        List<Integer> canUseTireSiteId = new ArrayList<>();
        // 轮位图
        String wheelArrange = vehicleSpec.getWheelArrange();
        for (String axle : wheelArrange.split(";")) {
            for (String tireSite : axle.split(",")) {
                if (!this.isTireUsed(Integer.valueOf(tireSite), assetTireList)) {
                    canUseTireSiteId.add(Integer.valueOf(tireSite));
                }
            }
        }
        List<TireSiteSimpleVo> tireSiteStrResult = new ArrayList<>();
        for (Integer tireSiteId : canUseTireSiteId) {
            TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(tireSiteId,
                    vehicleSpec.getSpecType(), vehicleSpec.getWheelCount(),
                    vehicleSpec.getWheelbaseType());
            if (tireSiteResult != null) {
                TireSiteSimpleVo tireSiteSimpleVo = new TireSiteSimpleVo();
                tireSiteSimpleVo.setTireSiteId(tireSiteId);
                tireSiteSimpleVo.setTireSiteName(tireSiteResult.getTireSiteName());
                tireSiteStrResult.add(tireSiteSimpleVo);
            }
        }
        return BaseResponse.ok(tireSiteStrResult);
    }

    /**
     * 资产管理-轮胎列表-选择调入仓库
     *
     * @return
     */
    @Override
    public BaseResponse<FleetWarehouseVo> getFleetWarehouse() {
        // 获取当前用户
        UserVo user = UserContext.getUser();
        // 客户用户关联表,一对一
        SysCustomerUser sysCustomerUser = sysCustomerUserMapper.selectOne(
                new LambdaQueryWrapper<SysCustomerUser>()
                        .eq(SysCustomerUser::getUserId, user.getUserId()));
        if (sysCustomerUser == null) {
            return BaseResponse.ok(new ArrayList<>());
        }
        List<FleetWarehouseVo> parentWarehouseVoList = new ArrayList<>();
        // 查询客户信息
        List<SysCustomer> parentCustomerList = new ArrayList<>();
        parentCustomerList = this.getParentCustomers(user, sysCustomerUser);
        if (user.getCustomerType() != SysCustomerConstant.PARENT_TYPE && parentCustomerList.isEmpty()) {
            return BaseResponse.ok(new ArrayList<>());
        }
        for (SysCustomer parentCustomer : parentCustomerList) {
            FleetWarehouseVo parentWarehouseVo = new FleetWarehouseVo();
            parentWarehouseVo.setId(parentCustomer.getId());
            parentWarehouseVo.setName(parentCustomer.getName());
            parentWarehouseVo.setParentId(parentCustomer.getParentId());
            parentWarehouseVo.setType(parentCustomer.getType());
            parentWarehouseVoList.add(parentWarehouseVo);
        }
        // 查询所有客户
        List<SysCustomer> allCustomerList = this.getAllCustomer(parentCustomerList);
        // 查询所有仓库
        List<Integer> clientIdList = new ArrayList<>();
        for (SysCustomer sysCustomer : allCustomerList) {
            clientIdList.add(sysCustomer.getId());
        }
        if (clientIdList.isEmpty()) {
            return BaseResponse.ok(new ArrayList<>());
        }
        List<WarehouseSetting> warehouseSettingList = warehouseSettingMapper.selectList(
                new LambdaQueryWrapper<WarehouseSetting>()
                        .in(WarehouseSetting::getFleetId, clientIdList)
                        .eq(WarehouseSetting::getIsDelete, SysCustomerConstant.NOT_DELETED));
        List<FleetWarehouseVo> allCustomerTreeVoList = new ArrayList<>();
        for (SysCustomer sysCustomer : allCustomerList) {
            List<WarehouseSimpleVo> warehouseSimpleList = new ArrayList<>();
            for (WarehouseSetting warehouseSetting : warehouseSettingList) {
                if (sysCustomer.getId().equals(warehouseSetting.getFleetId())) {
                    WarehouseSimpleVo warehouseSimpleVo = new WarehouseSimpleVo();
                    warehouseSimpleVo.setId(warehouseSetting.getId());
                    warehouseSimpleVo.setName(warehouseSetting.getName());
                    warehouseSimpleList.add(warehouseSimpleVo);
                }
            }
            FleetWarehouseVo children = new FleetWarehouseVo();
            children.setId(sysCustomer.getId());
            children.setName(sysCustomer.getName());
            children.setParentId(sysCustomer.getParentId());
            children.setType(sysCustomer.getType());
            children.setWarehouseSimpleList(warehouseSimpleList);
            allCustomerTreeVoList.add(children);
        }
        // 递归检索所有子客户
        this.getChildrenCustomer(parentWarehouseVoList, allCustomerTreeVoList);
        return BaseResponse.ok(parentWarehouseVoList);
    }

    /**
     * 资产管理-轮胎列表-通过车队ID选择调入仓库
     *
     * @param fleetId
     * @return
     */
    @Override
    public BaseResponse<List<FleetWarehouseVo>> getWarehouseByFleetId(Integer fleetId) {
        List<Integer> typeList = new ArrayList<>();
        typeList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
        typeList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
        SysCustomer sysCustomer = sysCustomerMapper.selectOne(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getId, fleetId)
                        .in(SysCustomer::getType, typeList)
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        if (sysCustomer == null) {
            return BaseResponse.ok(new ArrayList<>());
        }
        // 查询车队仓库
        List<WarehouseSetting> warehouseSettingList = warehouseSettingMapper.selectList(
                new LambdaQueryWrapper<WarehouseSetting>()
                        .eq(WarehouseSetting::getFleetId, sysCustomer.getId())
                        .eq(WarehouseSetting::getIsDelete, SysCustomerConstant.NOT_DELETED));
        return BaseResponse.ok(warehouseSettingList);
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
                .selectList(new LambdaQueryWrapper<SysCustomer>()
                        .in(SysCustomer::getCompanyId, companyIdSet)
                        .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
        return allCustomerList;
    }

    private void getChildrenCustomer(List<FleetWarehouseVo> parentList,
            List<FleetWarehouseVo> allCustomerList) {
        for (FleetWarehouseVo parent : parentList) {
            Integer parentId = parent.getId();
            List<FleetWarehouseVo> children = new ArrayList<>();
            for (FleetWarehouseVo allCustomer : allCustomerList) {
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

    private boolean isTireUsed(Integer tireSiteId, List<AssetTire> assetTireList) {
        for (AssetTire assetTire : assetTireList) {
            if (tireSiteId.equals(assetTire.getTireSite())) {
                return true;
            }
        }
        return false;
    }

    private ImportedResultVo processTireBatchInstall(MultipartFile file, UserVo user) {
        // 查询客户所有车队
        List<Integer> customerTypeIdList = new ArrayList<>();
        customerTypeIdList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
        customerTypeIdList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
        List<SysCustomer> fleetList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getCompanyId, user.getCompanyId())
                        .in(SysCustomer::getType, customerTypeIdList)
                        .eq(SysCustomer::getIsDelete, false));
        // 查询所有仓库
        List<WarehouseSetting> warehouseSettingList = warehouseSettingMapper.selectList(
                new LambdaQueryWrapper<WarehouseSetting>()
                        .eq(WarehouseSetting::getClientId, user.getCompanyId())
                        .eq(WarehouseSetting::getIsDelete, WarehouseSettingConstant.NOT_DELETE));
        // 查询所有轮胎品牌
        List<TireBrandEntity> tireBrandList = tireBrandMapper.selectList(
                new LambdaQueryWrapper<TireBrandEntity>()
                        .eq(TireBrandEntity::getCustomerId, user.getCompanyId())
                        .eq(TireBrandEntity::getIsDeleted, false));
        // 查询所有轮胎型号
        List<TireSpecEntity> tireSpecList = tireSpecMapper.selectList(
                new LambdaQueryWrapper<TireSpecEntity>()
                        .eq(TireSpecEntity::getCustomerId, user.getCompanyId())
                        .eq(TireSpecEntity::getIsDeleted, false));
        // 查询所有车队车辆
        List<VehicleEntity> vehicleList = vehicleMapper.selectList(
                new LambdaQueryWrapper<VehicleEntity>()
                        .in(VehicleEntity::getCustomerId, user.getCustomerIds())
                        .eq(VehicleEntity::getIsDeleted, false));
        // 查询所有车辆型号
        List<VehicleSpecEntity> vehicleSpecList = vehicleSpecMapper.selectList(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .in(VehicleSpecEntity::getCustomerId, user.getCustomerIds())
                        .eq(VehicleSpecEntity::getIsDeleted, false));
        // 开始时间
        long startTime = System.currentTimeMillis();
        // 解析excel
        TireBatchInstallListener listener = new TireBatchInstallListener(
                user,
                assetTireMapper,
                assetTireBaseService,
                assetTireDeviceRecordAsync,
                assetTireStockRecordAsync,
                assetTireFitRecordAsync,
                assetTireJdbc,
                fleetList,
                warehouseSettingList,
                tireBrandList,
                tireSpecList,
                vehicleList,
                vehicleSpecList
        );
        try {
            EasyExcel.read(file.getInputStream(), AssetTireBatchInstallExcelVo.class, listener)
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        log.info("轮胎批量安装,解析excel耗时:" + (endTime - startTime) + "ms");
        ImportedResultVo importedResultVo = listener.getImportedResultVo();
        return importedResultVo;
    }

    /**
     * 处理轮胎批量入库
     *
     * @param file
     * @param user
     * @return
     * @throws IOException
     */
    private ImportedResultVo processTireBatchStockIn(MultipartFile file, UserVo user) {
        // 查询客户所有车队
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("轮胎批量入库-数据准备");
        List<Integer> customerTypeIdList = new ArrayList<>();
        customerTypeIdList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
        customerTypeIdList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
        List<SysCustomer> fleetList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getCompanyId, user.getCompanyId())
                        .in(SysCustomer::getType, customerTypeIdList)
                        .eq(SysCustomer::getIsDelete, false));
        // 查询所有仓库
        List<WarehouseSetting> warehouseSettingList = warehouseSettingMapper.selectList(
                new LambdaQueryWrapper<WarehouseSetting>()
                        .eq(WarehouseSetting::getClientId, user.getCompanyId())
                        .eq(WarehouseSetting::getIsDelete, WarehouseSettingConstant.NOT_DELETE));
        // 查询所有轮胎品牌
        List<TireBrandEntity> tireBrandList = tireBrandMapper.selectList(
                new LambdaQueryWrapper<TireBrandEntity>()
                        .eq(TireBrandEntity::getCustomerId, user.getCompanyId())
                        .eq(TireBrandEntity::getIsDeleted, false));
        // 查询所有轮胎型号
        List<TireSpecEntity> tireSpecList = tireSpecMapper.selectList(
                new LambdaQueryWrapper<TireSpecEntity>()
                        .eq(TireSpecEntity::getCustomerId, user.getCompanyId())
                        .eq(TireSpecEntity::getIsDeleted, false));
        stopWatch.stop();
        log.info("轮胎批量入库-数据准备耗时:" + stopWatch.getLastTaskTimeMillis() + "ms");
        stopWatch.start("轮胎批量入库-解析excel并入库");
        // 解析excel
        TireBatchStockInListener listener = new TireBatchStockInListener(
                user,
                assetTireMapper,
                assetTireJdbc,
                assetTireStockRecordAsync,
                fleetList,
                warehouseSettingList,
                tireBrandList,
                tireSpecList
        );
        try {
            EasyExcel.read(file.getInputStream(), AssetTireBatchStockInExcelVo.class, listener)
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 结束时间
        stopWatch.stop();
        log.info("轮胎批量导入,解析excel并导入总耗时:" + stopWatch.getLastTaskTimeMillis() + "ms");
        return listener.getImportedResultVo();
    }

    /**
     * 将文件保存至本地
     *
     * @param file
     * @return
     */
    private UploadFile saveFile(MultipartFile file) {
        try {
            UploadFile uploadFile = uploadFileService.uploadFastDfs(file);
            return uploadFile;
        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new BaseException("99999", "上传文件失败");
        }
    }

    private void saveUninstallRecord(SysCustomer client, SysCustomer fleet, AssetTire assetTire,
            TireBrandEntity tireBrand, UserVo user) {
        AssetTireFitRecord assetTireFitRecord = new AssetTireFitRecord();
        assetTireFitRecord.setClientName(client.getName());
        assetTireFitRecord.setClientId(client.getId());
        assetTireFitRecord.setFleetName(fleet.getName());
        assetTireFitRecord.setFleetId(fleet.getId());
        assetTireFitRecord.setLicensePlate(assetTire.getLicensePlate());
        assetTireFitRecord.setTireCode(assetTire.getCode());
        assetTireFitRecord.setTireSiteName(assetTire.getTireSiteName());
        assetTireFitRecord.setBrandName(tireBrand.getBrandName());
        assetTireFitRecord.setType(AssetTireConstant.TIRE_UNINSTALL);
        assetTireFitRecord.setCreatePerson(user.getName());
        assetTireFitRecordMapper.insert(assetTireFitRecord);
    }

    /**
     * 根据入库仓库ID保存入库记录,入库仓库ID为空不保存
     *
     * @param param
     * @param assetTire
     * @param fleet
     * @param client
     * @param tireBrand
     * @param user
     */
    private void insertStockInRecordByWarehouseId(UninstallTireParam param, AssetTire assetTire,
            SysCustomer fleet, SysCustomer client, TireBrandEntity tireBrand, UserVo user) {
        if (param.getWarehouseId() != null) {
            // 查询车队记录
            AssetTireStockInParam assetTireStockInParam = new AssetTireStockInParam();
            assetTireStockInParam.setStockInType(AssetTireConstant.STOCK_IN_TYPE_UNINSTALL);
            assetTireStockInParam.setTireCode(param.getTireCode());
            // 司机姓名未填写时，使用车牌号作为供给方, 接收人为车牌号
            String target = "";
            if (StringUtils.isNotBlank(param.getDriver())) {
                target = param.getDriver();
            } else {
                target = param.getLicensePlate();
            }
            assetTireStockInParam.setTarget(target);
            assetTireStockInParam.setFleetId(fleet.getId());
            assetTireStockInParam.setWarehouseId(param.getWarehouseId());
            assetTireStockInParam.setDegree(assetTire.getDegree());
            assetTireStockInParam.setMileage(assetTire.getMileage());
            assetTireStockInParam.setTireBrandId(assetTire.getTireBrandId());
            assetTireStockInParam.setTireSpecId(assetTire.getTireSpecId());
            assetTireStockInParam.setSensorId(assetTire.getSensorId());
            this.saveStockInRecord(assetTireStockInParam, fleet, client, tireBrand, user);
        }
    }

    private void updateAssetTireUninstall(UninstallTireParam param, UserVo user) {
        UpdateWrapper<AssetTire> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", param.getId());
        // 卸载轮胎如果仓库ID为空则设置为null
        if (param.getWarehouseId() == null) {
            updateWrapper.set("warehouse_id", null);
            updateWrapper.set("tire_status", AssetTireConstant.TIRE_STATUS_WAITING);
        } else {
            updateWrapper.set("warehouse_id", param.getWarehouseId());
            updateWrapper.set("tire_status", AssetTireConstant.TIRE_STATUS_WAREHOUSE);
        }
        updateWrapper.set("tire_site", null);
        updateWrapper.set("tire_site_name", null);
        // 拆卸入库时轮位分类不变更
//        updateWrapper.set("tire_site_type", null);
//        updateWrapper.set("tire_site_type_name", null);
        updateWrapper.set("license_plate", null);
        if (param.getIsUnbindSensor() == AssetTireConstant.UNBIND_SENSOR_YES) {
            updateWrapper.set("sensor_id", null);
            updateWrapper.set("has_sensor", AssetTireConstant.NOT_BIND_SENSOR);
        }
        updateWrapper.set("update_person", user.getName());
        assetTireBaseService.update(updateWrapper);
    }

    private void updateVehicleSensorUnbind(UninstallTireParam param, AssetTire assetTire) {
        VehicleEntity vehicle = vehicleMapper.selectOne(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getLicensePlate, assetTire.getLicensePlate())
                        .eq(VehicleEntity::getIsDeleted, false));
        VehicleEntity updateParam = new VehicleEntity();
        updateParam.setId(vehicle.getId());
        // 拆卸轮胎更新车辆是否全部安装轮胎字段
        updateParam.setWheelsComplete(false);
        // 如果传感器解绑更新车辆是否全部绑定传感器字段
        if (param.getIsUnbindSensor() == AssetTireConstant.UNBIND_SENSOR_YES) {
            updateParam.setSensorsComplete(false);
        }
        // 必填字段更新
        updateParam.setRunRoute(vehicle.getRunRoute());
        updateParam.setRepeaterIdNumber(vehicle.getRepeaterIdNumber());
        updateParam.setTrailerRepeaterIdNumber(vehicle.getTrailerRepeaterIdNumber());
        updateParam.setReceiverIdNumber(vehicle.getReceiverIdNumber());
        vehicleMapper.updateById(updateParam);
    }

    private List<TireBrandEntity> getTireBrand(List<AssetTire> mainTireList,
            List<AssetTire> minorTireList) {
        Set<Integer> tireBrandIdSet = new HashSet<>();
        for (AssetTire assetTire : mainTireList) {
            tireBrandIdSet.add(assetTire.getTireBrandId());
        }
        if (!minorTireList.isEmpty()) {
            for (AssetTire assetTire : minorTireList) {
                tireBrandIdSet.add(assetTire.getTireBrandId());
            }
        }
        if (tireBrandIdSet.isEmpty()) {
            return new ArrayList<>();
        }
        List<TireBrandEntity> tireBrandList = tireBrandMapper.selectBatchIds(tireBrandIdSet);
        return tireBrandList;
    }

    private AssetTireFitDetailVo getAssetTireFitDetailVo(String licensePlate,
            String receiverIdNumber, String repeaterIdNumber, Integer vehicleId, Integer specId,
            List<AssetTire> tireList, List<TireBrandEntity> tireBrandList) {
        VehicleSpecEntity vehicleSpec = vehicleSpecMapper.selectById(specId);
        AssetTireFitDetailVo mainTireFitDetailVo = new AssetTireFitDetailVo();
        mainTireFitDetailVo.setLicensePlate(licensePlate);
        mainTireFitDetailVo.setReceiverIdNumber(receiverIdNumber);
        mainTireFitDetailVo.setRepeaterIdNumber(repeaterIdNumber);
        mainTireFitDetailVo.setVehicleId(vehicleId);
        List<VehicleTireDetailVo> vehicleTireDetailVoList = new ArrayList<>();
        if (StringUtils.isBlank(vehicleSpec.getWheelArrange())) {
            return mainTireFitDetailVo;
        }
        for (String wheel : vehicleSpec.getWheelArrange().split(";")) {
            for (String siteSiteId : wheel.split(",")) {
                AssetTire tire = null;
                for (AssetTire assetTire : tireList) {
                    if (Integer.valueOf(siteSiteId).equals(assetTire.getTireSite())) {
                        tire = assetTire;
                        break;
                    }
                }
                VehicleTireDetailVo vehicleTireDetailVo = new VehicleTireDetailVo();
                if (tire != null) {
                    TireBrandEntity tireBrandEntity = this.getTireBrand(tire, tireBrandList);
                    vehicleTireDetailVo.setId(tire.getId());
                    vehicleTireDetailVo.setTireCode(tire.getCode());
                    vehicleTireDetailVo.setTireSite(tire.getTireSite());
                    vehicleTireDetailVo.setTireSiteName(tire.getTireSiteName());
                    vehicleTireDetailVo.setTireSiteType(tire.getTireSiteType());
                    vehicleTireDetailVo.setTireBrandName(tireBrandEntity.getBrandName());
                    vehicleTireDetailVo.setSensorId(tire.getSensorId());
                    vehicleTireDetailVo.setCreateTime(tire.getCreateTime());
                    // 已绑定传感器则为绿色,未绑定传感器则为蓝色
                    if (tire.getHasSensor() == AssetTireConstant.IS_BIND_SENSOR) {
                        vehicleTireDetailVo.setColor(AssetTireConstant.BIND_TYPE_TIRE_BIND_SENSOR);
                    } else {
                        vehicleTireDetailVo.setColor(
                                AssetTireConstant.BIND_TYPE_TIRE_NOT_BIND_SENSOR);
                    }
                } else {
                    TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(
                            Integer.valueOf(siteSiteId),
                            vehicleSpec.getSpecType(), vehicleSpec.getWheelCount(),
                            vehicleSpec.getWheelbaseType());
                    vehicleTireDetailVo.setTireSite(Integer.valueOf(siteSiteId));
                    if (tireSiteResult != null) {
                        vehicleTireDetailVo.setTireSiteName(tireSiteResult.getTireSiteName());
                    }
                    vehicleTireDetailVo.setColor(0);
                }
                vehicleTireDetailVoList.add(vehicleTireDetailVo);
            }
        }
        mainTireFitDetailVo.setVehicleTireDetailVoList(vehicleTireDetailVoList);
        return mainTireFitDetailVo;
    }

    private VehicleTireDetailVo getVehicleTireDetailVo(AssetTire tire,
            TireBrandEntity tireBrandEntity, VehicleSpecEntity vehicleSpec) {
        VehicleTireDetailVo vehicleTireDetailVo = new VehicleTireDetailVo();
        vehicleTireDetailVo.setId(tire.getId());
        vehicleTireDetailVo.setTireCode(tire.getCode());
        vehicleTireDetailVo.setTireSite(tire.getTireSite());
        vehicleTireDetailVo.setTireSiteName(tire.getTireSiteName());
        vehicleTireDetailVo.setTireSiteType(tire.getTireSiteType());
        // 已绑定传感器则为绿色,未绑定传感器则为蓝色
        if (tire.getHasSensor() == AssetTireConstant.IS_BIND_SENSOR) {
            vehicleTireDetailVo.setColor(AssetTireConstant.BIND_TYPE_TIRE_BIND_SENSOR);
        } else {
            vehicleTireDetailVo.setColor(AssetTireConstant.BIND_TYPE_TIRE_NOT_BIND_SENSOR);
        }
        vehicleTireDetailVo.setTireBrandName(tireBrandEntity.getBrandName());
        vehicleTireDetailVo.setSensorId(tire.getSensorId());
        vehicleTireDetailVo.setCreateTime(tire.getCreateTime());
        return vehicleTireDetailVo;
    }

    private TireBrandEntity getTireBrand(AssetTire assetTire, List<TireBrandEntity> tireBrandList) {
        TireBrandEntity tireBrandEntity = new TireBrandEntity();
        for (TireBrandEntity tireBrand : tireBrandList) {
            if (assetTire.getTireBrandId().equals(tireBrand.getId())) {
                tireBrandEntity = tireBrand;
                break;
            }
        }
        return tireBrandEntity;
    }

    /**
     * 绑定状态颜色设置:1-绿色, 2-灰色, 3-蓝色, 4-红色
     *
     * @param hasWheelCount
     * @param wheelsComplete
     * @param sensorsComplete
     * @return
     */
    private Integer getBindStatusErrColor(Integer hasWheelCount, boolean wheelsComplete,
            boolean sensorsComplete) {
        // 当前车辆未设置轮位
        if (hasWheelCount == 0) {
            return AssetTireConstant.BIND_TYPE_VEHICLE_NOT_SET_TIRE;
        }
        if (!wheelsComplete) {
            return AssetTireConstant.BIND_TYPE_TIRE_NOT_MOUNT;
        }
        if (!sensorsComplete) {
            return AssetTireConstant.BIND_TYPE_TIRE_NOT_BIND_SENSOR;
        }
        return AssetTireConstant.BIND_TYPE_TIRE_BIND_SENSOR;
    }

    private void saveStockOutRecord(Integer tireStatus, Integer stockOutType,
            String licensePlate, String target, String tireSiteName, AssetTire assetTire,
            UserVo user, String remark) {
        // 查询客户记录
        SysCustomer client = sysCustomerMapper.selectById(assetTire.getClientId());
        // 查询车队记录
        SysCustomer fleet = sysCustomerMapper.selectById(assetTire.getFleetId());
        // 查询仓库记录
        WarehouseSetting warehouseSetting = warehouseSettingMapper
                .selectById(assetTire.getWarehouseId());
        // 查询轮胎品牌
        TireBrandEntity tireBrand = tireBrandMapper.selectById(assetTire.getTireBrandId());
        // 查询轮胎规格
        TireSpecEntity tireSpec = null;
        if (assetTire.getTireSpecId() != null) {
            tireSpec = tireSpecMapper.selectById(assetTire.getTireSpecId());
        }
        // 仅仓库待用保存出库记录
        if (tireStatus == AssetTireConstant.TIRE_STATUS_WAREHOUSE) {
            this.saveStockOut(stockOutType, target, assetTire, user, fleet, client,
                    warehouseSetting, tireBrand, tireSpec, remark);
        }
        // 安装出库保存装卸记录
        if (stockOutType == AssetTireConstant.STOCK_OUT_TYPE_INSTALL) {
            this.saveInstallRecord(licensePlate, tireSiteName, assetTire, user,
                    client.getName(), client.getId(), fleet.getName(), fleet.getId(),
                    tireBrand.getBrandName());
        }
    }

    private void saveInstallRecord(String licensePlate, String tireSiteName, AssetTire assetTire,
            UserVo user, String clientName, Integer clientId, String fleetName, Integer FleetId,
            String brandName) {
        AssetTireFitRecord assetTireFitRecord = new AssetTireFitRecord();
        assetTireFitRecord.setClientName(clientName);
        assetTireFitRecord.setClientId(clientId);
        assetTireFitRecord.setFleetName(fleetName);
        assetTireFitRecord.setFleetId(FleetId);
        assetTireFitRecord.setLicensePlate(licensePlate);
        assetTireFitRecord.setTireCode(assetTire.getCode());
        assetTireFitRecord.setTireSiteName(tireSiteName);
        assetTireFitRecord.setBrandName(brandName);
        assetTireFitRecord.setType(AssetTireConstant.TIRE_INSTALL);
        assetTireFitRecord.setCreatePerson(user.getName());
        assetTireFitRecordMapper.insert(assetTireFitRecord);
    }

    private AssetTireStockRecord saveStockOut(Integer stockType, String target, AssetTire assetTire,
            UserVo user, SysCustomer fleet, SysCustomer client, WarehouseSetting warehouseSetting,
            TireBrandEntity tireBrand, TireSpecEntity tireSpec, String remark) {
        AssetTireStockRecord assetTireStockRecord = new AssetTireStockRecord();
        assetTireStockRecord.setTireCode(assetTire.getCode());
        assetTireStockRecord.setFleetId(fleet.getId());
        assetTireStockRecord.setClientName(client.getName());
        assetTireStockRecord.setFleetName(fleet.getName());
        if (warehouseSetting != null) {
            assetTireStockRecord.setWarehouseId(warehouseSetting.getId());
            assetTireStockRecord.setWarehouseName(warehouseSetting.getName());
        }
        assetTireStockRecord.setType(AssetTireConstant.STOCK_RECORD_TYPE_OUT);
        assetTireStockRecord.setStockType(stockType);
        assetTireStockRecord.setTarget(target);
        assetTireStockRecord.setTireBrand(tireBrand.getBrandName());
        if (tireSpec != null) {
            assetTireStockRecord.setTireSpec(tireSpec.getSpecName());
        }
        assetTireStockRecord.setDegree(assetTire.getDegree());
        assetTireStockRecord.setMileage(assetTire.getMileage());
        assetTireStockRecord.setRemark(remark);
        assetTireStockRecord.setCreatePerson(user.getName());
        assetTireStockRecordMapper.insert(assetTireStockRecord);
        return assetTireStockRecord;
    }

    private void updateAssetTire(AssetTireStockOutParam param, AssetTire assetTire,
            VehicleEntity vehicle, UserVo user) {
        AssetTire updateParam = new AssetTire();
        updateParam.setId(assetTire.getId());
        updateParam.setLicensePlate(param.getLicensePlate());
        updateParam.setTireSite(param.getTireSite());
//        updateParam.setTireSiteName(param.getTireSiteName());
//        updateParam.setTireSiteType(param.getTireSiteType());
//        updateParam.setTireSiteTypeName(param.getTireSiteTypeName());
//        updateParam.setVehicleSpecId(vehicle.getId());
        updateParam.setUpdatePerson(user.getName());
        assetTireMapper.updateById(updateParam);
    }

    /**
     * 保存设备绑定记录
     *
     * @param oldSensorId
     * @param newSensorId
     * @param assetTire
     * @param user
     */
    private void saveSensorBindRecord(String oldSensorId, String newSensorId, String tireCode,
            AssetTire assetTire,
            UserVo user) {
        if (StringUtils.isNotBlank(newSensorId) && !newSensorId.equals(oldSensorId)) {
            // 保存变更记录
            TireDeviceBindRecordAddParam tireDeviceBindRecordAddParam = new TireDeviceBindRecordAddParam();
            tireDeviceBindRecordAddParam.setCode(newSensorId);
            tireDeviceBindRecordAddParam.setClientId(assetTire.getClientId());
            tireDeviceBindRecordAddParam.setFleetId(assetTire.getFleetId());
            tireDeviceBindRecordAddParam.setDeviceType(AssetTireConstant.DEVICE_TYPE_SENSOR);
            tireDeviceBindRecordAddParam.setLicensePlate(assetTire.getLicensePlate());
            tireDeviceBindRecordAddParam.setTireSiteName(assetTire.getTireSiteName());
            tireDeviceBindRecordAddParam.setTireCode(tireCode);
            tireDeviceBindRecordAddParam.setCreatePerson(user.getName());
            assetTireDeviceBindRecordService.save(tireDeviceBindRecordAddParam);
        }
    }

    private void updateEdit(AssetTireSensorBindParam param, AssetTire assetTire,
            String sensorId, UserVo user) {
        AssetTire updateParam = new AssetTire();
        updateParam.setId(assetTire.getId());
        updateParam.setSensorId(sensorId);
        updateParam.setHasSensor(StringUtils.isNotBlank(sensorId) ? AssetTireConstant.IS_BIND_SENSOR
                : AssetTireConstant.NOT_BIND_SENSOR);
        updateParam.setSensorType(param.getSensorType());
        updateParam.setUpdatePerson(user.getName());
        assetTireMapper.updateById(updateParam);
    }

    /**
     * 转换为vo
     *
     * @param assetTire
     * @param sysCustomerList
     * @param fleetList
     * @param warehouseSettingList
     * @param tireBrandList
     * @param tireSpecList
     * @return
     */
    private AssetTirePageVo getAssetTirePageVo(AssetTire assetTire,
            List<SysCustomer> sysCustomerList, List<SysCustomer> fleetList,
            List<WarehouseSetting> warehouseSettingList, List<TireBrandEntity> tireBrandList,
            List<TireSpecEntity> tireSpecList) {
        SysCustomer client = this.getSysCustomer(assetTire, sysCustomerList);
        SysCustomer fleet = this.getFleet(assetTire, fleetList);
        WarehouseSetting warehouseSetting = this.getWarehouseSetting(assetTire,
                warehouseSettingList);
        TireBrandEntity vehicleBrand = this.getTireBrand(assetTire, tireBrandList);
        TireSpecEntity tireSpecEntity = this.getTireSpec(assetTire, tireSpecList);
        AssetTirePageVo assetTirePageVo = new AssetTirePageVo();
        assetTirePageVo.setId(assetTire.getId());
        assetTirePageVo.setCode(assetTire.getCode());
        if (client != null) {
            assetTirePageVo.setClientName(client.getName());
        }
        if (fleet != null) {
            assetTirePageVo.setFleetName(fleet.getName());
        }
        if (warehouseSetting != null) {
            assetTirePageVo.setWarehouseName(warehouseSetting.getName());
        }
        assetTirePageVo.setLicensePlate(assetTire.getLicensePlate());
        assetTirePageVo.setTireSiteName(assetTire.getTireSiteName());
        assetTirePageVo.setTireSiteTypeName(assetTire.getTireSiteTypeName());
        if (vehicleBrand != null) {
            assetTirePageVo.setTireBrandName(vehicleBrand.getBrandName());
        }
        if (tireSpecEntity != null) {
            assetTirePageVo.setTireSpecName(tireSpecEntity.getSpecName());
        }
        assetTirePageVo.setDegree(assetTire.getDegree());
        assetTirePageVo.setMileage(assetTire.getMileage());
        assetTirePageVo.setSensorId(assetTire.getSensorId());
        assetTirePageVo.setTireStatus(assetTire.getTireStatus());
        assetTirePageVo.setUpdateTime(assetTire.getUpdateTime());
        assetTirePageVo.setUpdatePerson(assetTire.getUpdatePerson());
        return assetTirePageVo;
    }

    /**
     * 转换为exportVo
     *
     * @param assetTire
     * @param sysCustomerList
     * @param fleetList
     * @param warehouseSettingList
     * @param tireBrandList
     * @param tireSpecList
     * @return
     */
    private AssetTirePageExportVo getAssetTirePageExportVo(AssetTire assetTire,
            List<SysCustomer> sysCustomerList, List<SysCustomer> fleetList,
            List<WarehouseSetting> warehouseSettingList, List<TireBrandEntity> tireBrandList,
            List<TireSpecEntity> tireSpecList) {
        SysCustomer client = this.getSysCustomer(assetTire, sysCustomerList);
        SysCustomer fleet = this.getFleet(assetTire, fleetList);
        WarehouseSetting warehouseSetting = this.getWarehouseSetting(assetTire,
                warehouseSettingList);
        TireBrandEntity vehicleBrand = this.getTireBrand(assetTire, tireBrandList);
        TireSpecEntity tireSpecEntity = this.getTireSpec(assetTire, tireSpecList);
        AssetTirePageExportVo assetTirePageExportVo = new AssetTirePageExportVo();
        assetTirePageExportVo.setCode(assetTire.getCode());
        if (client != null) {
            assetTirePageExportVo.setClientName(client.getName());
        }
        if (fleet != null) {
            assetTirePageExportVo.setFleetName(fleet.getName());
        }
        if (warehouseSetting != null) {
            assetTirePageExportVo.setWarehouseName(warehouseSetting.getName());
        }
        assetTirePageExportVo.setLicensePlate(assetTire.getLicensePlate());
        assetTirePageExportVo.setTireSiteName(assetTire.getTireSiteName());
        assetTirePageExportVo.setTireSiteTypeName(assetTire.getTireSiteTypeName());
        if (vehicleBrand != null) {
            assetTirePageExportVo.setTireBrandName(vehicleBrand.getBrandName());
        }
        if (tireSpecEntity != null) {
            assetTirePageExportVo.setTireSpecName(tireSpecEntity.getSpecName());
        }
        assetTirePageExportVo.setDegree(assetTire.getDegree());
        assetTirePageExportVo.setMileage(String.valueOf(assetTire.getMileage()));
        assetTirePageExportVo.setSensorId(assetTire.getSensorId());
        assetTirePageExportVo.setTireStatusStr(this.getTireStatusStr(assetTire.getTireStatus()));
        assetTirePageExportVo.setUpdateTime(DateUtils.dateToStr(assetTire.getUpdateTime()));
        assetTirePageExportVo.setUpdatePerson(assetTire.getUpdatePerson());
        return assetTirePageExportVo;
    }

    /**
     * 轮胎状态:1-仓库待用,2-使用中,3-已变卖,4-已调拨,5-待用
     */
    private String getTireStatusStr(Integer tireStatus) {
        String tireStatusStr = "";
        switch (tireStatus) {
            case AssetTireConstant.TIRE_STATUS_WAREHOUSE:
                tireStatusStr = "仓库待用";
                break;
            case AssetTireConstant.TIRE_STATUS_USING:
                tireStatusStr = "使用中";
                break;
            case AssetTireConstant.TIRE_STATUS_SOLD:
                tireStatusStr = "已变卖";
                break;
            case AssetTireConstant.TIRE_STATUS_TRANSFER:
                tireStatusStr = "已调拨";
                break;
            case AssetTireConstant.TIRE_STATUS_WAITING:
                tireStatusStr = "待用";
                break;
            default:
                break;
        }
        return tireStatusStr;
    }

    /**
     * 查询轮胎规格
     *
     * @param assetTire
     * @param tireSpecList
     * @return
     */
    private TireSpecEntity getTireSpec(AssetTire assetTire,
            List<TireSpecEntity> tireSpecList) {
        TireSpecEntity tireSpecEntity = null;
        for (TireSpecEntity spec : tireSpecList) {
            if (spec.getId().equals(assetTire.getTireSpecId())) {
                tireSpecEntity = spec;
                break;
            }
        }
        return tireSpecEntity;
    }

    private List<TireSpecEntity> getTireSpecList(List<AssetTire> assetTireList) {
        List<Integer> tireSpecIdList = new ArrayList<>();
        for (AssetTire assetTire : assetTireList) {
            if (assetTire.getTireSpecId() != null) {
                tireSpecIdList.add(assetTire.getTireSpecId());
            }
        }
        List<TireSpecEntity> tireSpecList = new ArrayList<>();
        if (tireSpecIdList.isEmpty()) {
            return tireSpecList;
        }
        tireSpecList = tireSpecMapper.selectList(
                new LambdaQueryWrapper<TireSpecEntity>()
                        .in(TireSpecEntity::getId, tireSpecIdList));
        return tireSpecList;
    }

    private List<TireBrandEntity> getTireBrandList(List<AssetTire> assetTireList) {
        List<Integer> tireBrandIdList = new ArrayList<>();
        for (AssetTire assetTire : assetTireList) {
            tireBrandIdList.add(assetTire.getTireBrandId());
        }
        List<TireBrandEntity> tireBrandList = new ArrayList<>();
        if (tireBrandIdList.isEmpty()) {
            return tireBrandList;
        }
        tireBrandList = tireBrandMapper.selectList(
                new LambdaQueryWrapper<TireBrandEntity>()
                        .in(TireBrandEntity::getId, tireBrandIdList));
        return tireBrandList;
    }


    private WarehouseSetting getWarehouseSetting(AssetTire assetTire,
            List<WarehouseSetting> warehouseSettingList) {
        WarehouseSetting warehouseSetting = new WarehouseSetting();
        for (WarehouseSetting temp : warehouseSettingList) {
            if (assetTire.getWarehouseId() != null && assetTire.getWarehouseId()
                    .equals(temp.getId())) {
                warehouseSetting = temp;
                break;
            }
        }
        return warehouseSetting;
    }

    private List<WarehouseSetting> getWarehouseSettingList(List<AssetTire> assetTireList) {
        List<Integer> warehouseIdList = new ArrayList<>();
        for (AssetTire assetTire : assetTireList) {
            warehouseIdList.add(assetTire.getWarehouseId());
        }
        List<WarehouseSetting> warehouseSettingList = new ArrayList<>();
        if (warehouseIdList.isEmpty()) {
            return warehouseSettingList;
        }
        warehouseSettingList = warehouseSettingMapper.selectList(
                new LambdaQueryWrapper<WarehouseSetting>()
                        .in(WarehouseSetting::getId, warehouseIdList));
        return warehouseSettingList;
    }

    /**
     * 查询车队信息
     *
     * @param assetTireList
     * @return
     */
    private List<SysCustomer> getFleetList(List<AssetTire> assetTireList) {
        List<Integer> fleetIdList = new ArrayList<>();
        for (AssetTire assetTire : assetTireList) {
            fleetIdList.add(assetTire.getFleetId());
        }
        List<SysCustomer> fleetList = new ArrayList<>();
        if (fleetIdList.isEmpty()) {
            return fleetList;
        }
        fleetList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .in(SysCustomer::getId, fleetIdList));
        return fleetList;
    }

    private SysCustomer getFleet(AssetTire assetTire, List<SysCustomer> fleetList) {
        SysCustomer fleet = null;
        for (SysCustomer sysCustomer : fleetList) {
            if (assetTire.getFleetId().equals(sysCustomer.getId())) {
                fleet = sysCustomer;
                break;
            }
        }
        return fleet;
    }

    /**
     * 查询客户信息
     *
     * @param assetTireList
     * @return
     */
    private List<SysCustomer> getClientList(List<AssetTire> assetTireList) {
        List<Integer> clientIdList = new ArrayList<>();
        for (AssetTire assetTire : assetTireList) {
            clientIdList.add(assetTire.getClientId());
        }
        List<SysCustomer> sysCustomerList = new ArrayList<>();
        if (clientIdList.isEmpty()) {
            return sysCustomerList;
        }
        sysCustomerList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .in(SysCustomer::getId, clientIdList));
        return sysCustomerList;
    }

    private SysCustomer getSysCustomer(AssetTire assetTire,
            List<SysCustomer> sysCustomerList) {
        SysCustomer client = null;
        for (SysCustomer sysCustomer : sysCustomerList) {
            if (assetTire.getClientId().equals(sysCustomer.getId())) {
                client = sysCustomer;
                break;
            }
        }
        return client;
    }

    private LambdaQueryWrapper<AssetTire> getAssetTireWrapper(
            AssetTireConditionQueryParam param) {
        UserVo user = UserContext.getUser();
        LambdaQueryWrapper<AssetTire> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getTireCode())) {
            wrapper.like(AssetTire::getCode, param.getTireCode());
        }
        if (StringUtils.isNotBlank(param.getLicensePlate())) {
            wrapper.like(AssetTire::getLicensePlate, param.getLicensePlate());
        }
        if (StringUtils.isNotBlank(param.getFleetName())) {
            List<Integer> customerTypeList = new ArrayList<>();
            customerTypeList.add(SysCustomerConstant.FIRST_LEVEL_FLEET_TYPE);
            customerTypeList.add(SysCustomerConstant.SECOND_LEVEL_FLEET_TYPE);
            List<SysCustomer> sysCustomerList = sysCustomerMapper.selectList(
                    new LambdaQueryWrapper<SysCustomer>()
                            .like(SysCustomer::getName, param.getFleetName())
                            .in(SysCustomer::getType, customerTypeList)
                            .eq(SysCustomer::getIsDelete, SysCustomerConstant.NOT_DELETED));
            List<Integer> fleetIdList = new ArrayList<>();
            sysCustomerList.forEach(sysCustomer -> fleetIdList.add(sysCustomer.getId()));
            if (fleetIdList.isEmpty()) {
                fleetIdList.add(-1);
            }
            wrapper.in(AssetTire::getFleetId, fleetIdList);
        }
        if (StringUtils.isNotBlank(param.getSensorId())) {
            wrapper.like(AssetTire::getSensorId, param.getSensorId());
        }
        if (param.getTireStatus() != null) {
            wrapper.eq(AssetTire::getTireStatus, param.getTireStatus());
        }
        if (param.getHasSensor() != null) {
            wrapper.eq(AssetTire::getHasSensor, param.getHasSensor());
        }
        wrapper.in(AssetTire::getFleetId, user.getCustomerIds());
        wrapper.eq(AssetTire::getIsDelete, SysCustomerConstant.NOT_DELETED);
        wrapper.orderByDesc(AssetTire::getUpdateTime);
        return wrapper;
    }
}
