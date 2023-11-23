package org.platform.vehicle.listener;


import static org.platform.vehicle.constant.AssetTireConstant.DEVICE_TYPE_SENSOR;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.platform.vehicle.constant.AssetTireConstant;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.entity.AssetTireDeviceBindRecord;
import org.platform.vehicle.entity.AssetTireFitRecord;
import org.platform.vehicle.entity.AssetTireStockRecord;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.TireBrandEntity;
import org.platform.vehicle.entity.TireSpecEntity;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.entity.WarehouseSetting;
import org.platform.vehicle.helper.jdbc.AssetTireJdbc;
import org.platform.vehicle.mapper.AssetTireMapper;
import org.platform.vehicle.service.AssetTireBaseService;
import org.platform.vehicle.util.AssetTireDeviceRecordAsync;
import org.platform.vehicle.util.AssetTireFitRecordAsync;
import org.platform.vehicle.util.AssetTireStockRecordAsync;
import org.platform.vehicle.util.TireSiteUtil;
import org.platform.vehicle.vo.AssetTireBatchInstallExcelVo;
import org.platform.vehicle.vo.ImportedDataErrVo;
import org.platform.vehicle.vo.ImportedResultVo;
import org.platform.vehicle.vo.TireSiteResult;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author gejiawei
 * @Date 2023/9/23 10:49
 */
@Slf4j
public class TireBatchInstallListener implements ReadListener<AssetTireBatchInstallExcelVo> {

    /**
     * 100条数据存数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;
    /**
     * 10辆车入库限制
     */
    public static final int VEHICLE_LIMIT = 10;
    public static final int LIMIT_ROW = 10000;
    private List<AssetTireBatchInstallExcelVo> cachedDataList = new ArrayList<>();


    /**
     * 总行数
     */
    private Integer totalCount;

    /**
     * 成功条数
     */
    private Integer completeCount = 0;

    /**
     * 失败条数
     */
    private Integer errorCount = 0;

    /**
     * 错误列表
     */
    private List<ImportedDataErrVo> errorList = new ArrayList<>();

    private UserVo user;
    private AssetTireMapper assetTireMapper;
    private AssetTireBaseService assetTireBaseService;
    private AssetTireDeviceRecordAsync assetTireDeviceRecordAsync;
    private AssetTireStockRecordAsync assetTireStockRecordAsync;
    private AssetTireFitRecordAsync assetTireFitRecordAsync;
    private AssetTireJdbc assetTireJdbc;
    private List<SysCustomer> fleetList;
    private List<WarehouseSetting> warehouseSettingList;
    private List<TireBrandEntity> tireBrandList;
    private List<TireSpecEntity> tireSpecList;
    private List<VehicleEntity> vehicleList;
    private List<VehicleSpecEntity> vehicleSpecList;

    public TireBatchInstallListener(
            UserVo user,
            AssetTireMapper assetTireMapper,
            AssetTireBaseService assetTireBaseService,
            AssetTireDeviceRecordAsync assetTireDeviceRecordAsync,
            AssetTireStockRecordAsync assetTireStockRecordAsync,
            AssetTireFitRecordAsync assetTireFitRecordAsync,
            AssetTireJdbc assetTireJdbc,
            List<SysCustomer> fleetList,
            List<WarehouseSetting> warehouseSettingList,
            List<TireBrandEntity> tireBrandList,
            List<TireSpecEntity> tireSpecList,
            List<VehicleEntity> vehicleList,
            List<VehicleSpecEntity> vehicleSpecList
    ) {
        this.user = user;
        this.assetTireMapper = assetTireMapper;
        this.assetTireBaseService = assetTireBaseService;
        this.assetTireDeviceRecordAsync = assetTireDeviceRecordAsync;
        this.assetTireStockRecordAsync = assetTireStockRecordAsync;
        this.assetTireFitRecordAsync = assetTireFitRecordAsync;
        this.assetTireJdbc = assetTireJdbc;
        this.fleetList = fleetList;
        this.warehouseSettingList = warehouseSettingList;
        this.tireBrandList = tireBrandList;
        this.tireSpecList = tireSpecList;
        this.vehicleList = vehicleList;
        this.vehicleSpecList = vehicleSpecList;
    }

    public ImportedResultVo getImportedResultVo() {
        ImportedResultVo importedResultVo = new ImportedResultVo();
        importedResultVo.setCompleteCount(completeCount);
        importedResultVo.setErrorCount(errorCount);
        importedResultVo.setErrorList(errorList);
        return importedResultVo;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("解析失败，停止解析:{}", exception.getMessage());
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(),
                    excelDataConvertException.getCellData());
            throw new BaseException("99999",
                    "第" + excelDataConvertException.getRowIndex() + "行，第" +
                            excelDataConvertException.getColumnIndex() + "列解析异常，数据为:" +
                            excelDataConvertException.getCellData());
        }
        if (exception instanceof BaseException) {
            throw new BaseException("99999", exception.getMessage());
        }
    }

    @Override
    public void invoke(AssetTireBatchInstallExcelVo data, AnalysisContext analysisContext) {
        Integer totalRow = analysisContext.readSheetHolder()
                .getApproximateTotalRowNumber();
        if (totalRow > LIMIT_ROW) {
            throw new BaseException("99999", "导入数据不能超过" + LIMIT_ROW + "条");
        }
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            this.saveData(cachedDataList);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        this.saveData(cachedDataList);
    }

    private void saveData(List<AssetTireBatchInstallExcelVo> cachedDataList) {
        if (cachedDataList.isEmpty()) {
            return;
        }
        // 轮胎更新参数
        List<AssetTire> updateTireParamList = new ArrayList<>();
        // 设备绑定插入参数
        List<AssetTireDeviceBindRecord> insertTireDeviceBindRecordList = new ArrayList<>();
        // 仓库待用出库记录插入参数
        List<AssetTireStockRecord> insertTireStockOutRecordList = new ArrayList<>();
        // 装卸记录插入参数
        List<AssetTireFitRecord> insertAssetTireFitRecordList = new ArrayList<>();
        List<String> tireCodeList = new ArrayList<>();
        List<String> sensorIdList = new ArrayList<>();
        for (AssetTireBatchInstallExcelVo data : cachedDataList) {
            if (StringUtils.isNotBlank(data.getTireCode())) {
                tireCodeList.add(data.getTireCode());
                sensorIdList.add(data.getSensorId());
            }
        }
        List<AssetTire> tireList = this.getAssetTireList(tireCodeList);
        List<AssetTire> tireSensorList = this.getAssetTireSensorList(sensorIdList);
        for (AssetTireBatchInstallExcelVo data : cachedDataList) {
            Result result = this.getResult(data, tireList, tireSensorList);
            if (result == null) {
                continue;
            }
            // 根据轮位获取轮位名称和轴位
            Integer tireSiteType = result.tireSiteResult.getTireSiteType();
            String tireSiteName = result.tireSiteResult.getTireSiteName();
            String tireSiteTypeName = result.tireSiteResult.getTireSiteTypeName();
            // 更新轮胎参数
            AssetTire updateParamTire = this.getAssetTireUpdateParam(data, result.assetTire,
                    tireSiteName, tireSiteType, tireSiteTypeName);
            updateTireParamList.add(updateParamTire);
            // 保存设备绑定记录
            if (StringUtils.isNotBlank(data.getSensorId())) {
                AssetTireDeviceBindRecord insertParam = this.getAssetTireDeviceBindRecordInsertParam(
                        data, tireSiteName, result.assetTire);
                insertTireDeviceBindRecordList.add(insertParam);
            }
            SysCustomer fleet = this.getSysCustomer(result.assetTire);
            TireBrandEntity tireBrand = this.getTireBrandEntity(result.assetTire);
            // 仅仓库待用保存出库记录
            if (result.assetTire.getTireStatus() == AssetTireConstant.TIRE_STATUS_WAREHOUSE) {
                AssetTireStockRecord stockOutInsertParam = this.getAssetTireStockRecord(data,
                        result.assetTire, fleet, tireBrand);
                insertTireStockOutRecordList.add(stockOutInsertParam);
            }
            // 安装出库保存装卸记录
            AssetTireFitRecord assetTireFitRecord = this.getAssetTireFitRecord(data, fleet,
                    result.assetTire, tireSiteName, tireBrand);
            insertAssetTireFitRecordList.add(assetTireFitRecord);
        }
        // 执行入库操作
        this.execute(
                updateTireParamList,
                insertTireDeviceBindRecordList,
                insertTireStockOutRecordList,
                insertAssetTireFitRecordList
        );
    }

    private List<AssetTire> getAssetTireSensorList(List<String> sensorIdList) {
        List<AssetTire> tireSensorList = new ArrayList<>();
        if (!sensorIdList.isEmpty()) {
            tireSensorList = assetTireMapper.selectList(new LambdaQueryWrapper<AssetTire>()
                    .in(AssetTire::getSensorId, sensorIdList)
                    .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        }
        return tireSensorList;
    }

    private void execute(List<AssetTire> updateTireParamList,
            List<AssetTireDeviceBindRecord> insertTireDeviceBindRecordList,
            List<AssetTireStockRecord> insertTireStockOutRecordList,
            List<AssetTireFitRecord> insertAssetTireFitRecordList) {
        long startTime = System.currentTimeMillis();
        if (!updateTireParamList.isEmpty()) {
            assetTireBaseService.updateBatchById(updateTireParamList);
        }
        long endTime = System.currentTimeMillis();
        log.info("轮胎批量导入-插入数据库耗时:{}", endTime - startTime);
        // 传感器存在生成传感器绑定记录
        if (!insertTireDeviceBindRecordList.isEmpty()) {
            assetTireDeviceRecordAsync.batchSave(insertTireDeviceBindRecordList);
        }
        // 保存出库记录
        if (!insertTireStockOutRecordList.isEmpty()) {
            assetTireStockRecordAsync.batchSave(insertTireStockOutRecordList);
        }
        // 保存轮胎拆卸记录
        if (!insertAssetTireFitRecordList.isEmpty()) {
            assetTireFitRecordAsync.batchSave(insertAssetTireFitRecordList);
        }
    }

    private Result getResult(AssetTireBatchInstallExcelVo data, List<AssetTire> tireList,
            List<AssetTire> tireSensorList) {
        // 校验轮胎号是否填写
        if (StringUtils.isBlank(data.getTireCode())) {
            errorCount++;
            errorList.add(new ImportedDataErrVo(data.getTireCode(), "轮胎号不能为空"));
            return null;
        }
        // 校验轮胎号是否存在
        AssetTire assetTire = this.getAssetTire(data, tireList);
        if (assetTire == null) {
            errorCount++;
            errorList.add(new ImportedDataErrVo(data.getTireCode(),
                    "轮胎号不存在或者不存在于当前组织架构"));
            return null;
        }
        // 校验车牌是否填写
        if (StringUtils.isBlank(data.getLicensePlate())) {
            errorCount++;
            errorList.add(new ImportedDataErrVo(data.getTireCode(), "车牌号未填写"));
            return null;
        }
        // 校验车牌是否存在
        VehicleEntity vehicle = this.getVehicleEntity(data);
        if (vehicle == null) {
            errorCount++;
            errorList.add(new ImportedDataErrVo(data.getTireCode(),
                    "车牌号不存在于当前组织架构"));
            return null;
        }
        // 查询车辆规格是否存在
        VehicleSpecEntity vehicleSpec = this.getVehicleSpec(vehicle);
        if (vehicleSpec == null) {
            errorCount++;
            errorList.add(new ImportedDataErrVo(data.getTireCode(), "车辆规格不存在"));
            return null;
        }
        // 轮位号是否填写
        if (data.getTireSite() == null) {
            errorCount++;
            errorList.add(new ImportedDataErrVo(data.getTireCode(), "轮位号未填写"));
            return null;
        }
        // 目标轮位是否已安装其他轮胎
        if (assetTire.getTireSite() != null) {
            errorCount++;
            errorList.add(new ImportedDataErrVo(data.getTireCode(), "轮位上已安装其他轮胎"));
            return null;
        }
        // 校验轮位是否填写正确
        TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(data.getTireSite(),
                vehicleSpec.getSpecType(), vehicleSpec.getWheelCount(),
                vehicleSpec.getWheelbaseType());
        if (tireSiteResult == null) {
            errorCount++;
            errorList.add(new ImportedDataErrVo(data.getTireCode(), "轮位号填写错误"));
            return null;
        }
        if (StringUtils.isNotBlank(data.getSensorId())) {
            // 传感器存在则校验传感器是否被使用
            if (this.isSensorUsed(data, tireSensorList)) {
                errorCount++;
                errorList.add(new ImportedDataErrVo(data.getTireCode(), "传感器已被使用"));
                return null;
            }
        }
        return new Result(assetTire, vehicleSpec, tireSiteResult);
    }

    private static class Result {

        public final AssetTire assetTire;
        public final VehicleSpecEntity vehicleSpec;

        public final TireSiteResult tireSiteResult;

        public Result(AssetTire assetTire, VehicleSpecEntity vehicleSpec,
                TireSiteResult tireSiteResult) {
            this.assetTire = assetTire;
            this.vehicleSpec = vehicleSpec;
            this.tireSiteResult = tireSiteResult;
        }
    }

    private AssetTireFitRecord getAssetTireFitRecord(AssetTireBatchInstallExcelVo data,
            SysCustomer fleet, AssetTire assetTire, String tireSiteName,
            TireBrandEntity tireBrand) {
        AssetTireFitRecord assetTireFitRecord = new AssetTireFitRecord();
        assetTireFitRecord.setClientName(user.getCompanyName());
        assetTireFitRecord.setClientId(user.getCompanyId());
        assetTireFitRecord.setFleetName(fleet.getName());
        assetTireFitRecord.setFleetId(fleet.getId());
        assetTireFitRecord.setLicensePlate(data.getLicensePlate());
        assetTireFitRecord.setTireCode(assetTire.getCode());
        assetTireFitRecord.setTireSiteName(tireSiteName);
        assetTireFitRecord.setBrandName(tireBrand.getBrandName());
        assetTireFitRecord.setType(AssetTireConstant.TIRE_INSTALL);
        assetTireFitRecord.setCreatePerson(user.getName());
        return assetTireFitRecord;
    }

    private AssetTireStockRecord getAssetTireStockRecord(AssetTireBatchInstallExcelVo data,
            AssetTire assetTire, SysCustomer fleet, TireBrandEntity tireBrand) {
        String target = StringUtils.isNotBlank(data.getDriverName()) ? data.getDriverName()
                : data.getLicensePlate();
        WarehouseSetting warehouseSetting = this.getWarehouseSetting(assetTire);
        TireSpecEntity tireSpec = this.getTireSpecEntity(assetTire);
        AssetTireStockRecord stockOutInsertParam = new AssetTireStockRecord();
        stockOutInsertParam.setTireCode(assetTire.getCode());
        stockOutInsertParam.setFleetId(assetTire.getFleetId());
        stockOutInsertParam.setClientName(user.getCompanyName());
        if (fleet != null) {
            stockOutInsertParam.setFleetName(fleet.getName());
        }
        stockOutInsertParam.setWarehouseId(assetTire.getWarehouseId());
        if (warehouseSetting != null) {
            stockOutInsertParam.setWarehouseName(warehouseSetting.getName());
        }
        stockOutInsertParam.setType(AssetTireConstant.STOCK_RECORD_TYPE_OUT);
        stockOutInsertParam.setStockType(AssetTireConstant.STOCK_OUT_TYPE_INSTALL);
        stockOutInsertParam.setTarget(target);
        if (tireBrand != null) {
            stockOutInsertParam.setTireBrand(tireBrand.getBrandName());
        }
        if (tireSpec != null) {
            stockOutInsertParam.setTireSpec(tireSpec.getSpecName());
        }
        stockOutInsertParam.setDegree(assetTire.getDegree());
        stockOutInsertParam.setMileage(assetTire.getMileage());
        stockOutInsertParam.setRemark(data.getRemark());
        stockOutInsertParam.setCreatePerson(user.getName());
        return stockOutInsertParam;
    }

    private TireSpecEntity getTireSpecEntity(AssetTire assetTire) {
        TireSpecEntity tireSpec = null;
        for (TireSpecEntity spec : tireSpecList) {
            if (spec.getId().equals(assetTire.getTireSpecId())) {
                tireSpec = spec;
                break;
            }
        }
        return tireSpec;
    }

    private WarehouseSetting getWarehouseSetting(AssetTire assetTire) {
        WarehouseSetting warehouseSetting = null;
        for (WarehouseSetting setting : warehouseSettingList) {
            if (setting.getId().equals(assetTire.getWarehouseId())) {
                warehouseSetting = setting;
                break;
            }
        }
        return warehouseSetting;
    }

    private TireBrandEntity getTireBrandEntity(AssetTire assetTire) {
        TireBrandEntity tireBrand = null;
        for (TireBrandEntity brand : tireBrandList) {
            if (brand.getId().equals(assetTire.getTireBrandId())) {
                tireBrand = brand;
                break;
            }
        }
        return tireBrand;
    }

    private SysCustomer getSysCustomer(AssetTire assetTire) {
        SysCustomer fleet = null;
        for (SysCustomer sysCustomer : fleetList) {
            if (sysCustomer.getId().equals(assetTire.getFleetId())) {
                fleet = sysCustomer;
                break;
            }
        }
        return fleet;
    }

    private AssetTireDeviceBindRecord getAssetTireDeviceBindRecordInsertParam(
            AssetTireBatchInstallExcelVo data, String tireSiteName, AssetTire assetTire) {
        AssetTireDeviceBindRecord insertParam = new AssetTireDeviceBindRecord();
        insertParam.setCode(data.getSensorId());
        insertParam.setFleetId(assetTire.getFleetId());
        insertParam.setClientId(assetTire.getClientId());
        insertParam.setDeviceType(DEVICE_TYPE_SENSOR);
        insertParam.setLicensePlate(data.getLicensePlate());
        insertParam.setTireSiteName(tireSiteName);
        insertParam.setTireCode(assetTire.getCode());
        insertParam.setCreatePerson(user.getName());
        return insertParam;
    }

    private AssetTire getAssetTireUpdateParam(AssetTireBatchInstallExcelVo data,
            AssetTire assetTire, String tireSiteName, Integer tireSiteType,
            String tireSiteTypeName) {
        AssetTire updateParamTire = new AssetTire();
        updateParamTire.setId(assetTire.getId());
        updateParamTire.setLicensePlate(data.getLicensePlate());
        updateParamTire.setTireSite(data.getTireSite());
        updateParamTire.setTireSiteName(tireSiteName);
        updateParamTire.setTireSiteType(tireSiteType);
        updateParamTire.setTireSiteTypeName(tireSiteTypeName);
        updateParamTire.setSensorId(data.getSensorId());
        updateParamTire.setHasSensor(AssetTireConstant.IS_BIND_SENSOR);
        updateParamTire.setTireStatus(AssetTireConstant.TIRE_STATUS_USING);
        updateParamTire.setIsDelete(AssetTireConstant.NOT_DELETE);
        updateParamTire.setUpdatePerson(user.getName());
        return updateParamTire;
    }

    private VehicleSpecEntity getVehicleSpec(VehicleEntity vehicle) {
        VehicleSpecEntity vehicleSpec = null;
        for (VehicleSpecEntity entity : vehicleSpecList) {
            if (entity.getId().equals(vehicle.getSpecId())) {
                vehicleSpec = entity;
                break;
            }
        }
        return vehicleSpec;
    }

    private boolean isSensorUsed(AssetTireBatchInstallExcelVo data,
            List<AssetTire> tireSensorList) {
        boolean isUsed = false;
        for (AssetTire tire : tireSensorList) {
            if (tire.getSensorId().equals(data.getSensorId())) {
                isUsed = true;
                break;
            }
        }
        return isUsed;
    }

    private List<AssetTire> getAssetTireList(List<String> tireCodeList) {
        List<AssetTire> tireList = new ArrayList<>();
        // 根据轮胎号查询历史记录
        if (!tireCodeList.isEmpty()) {
            tireList = assetTireMapper.selectList(new LambdaQueryWrapper<AssetTire>()
                    .in(AssetTire::getCode, tireCodeList)
                    .eq(AssetTire::getClientId, user.getCompanyId())
                    .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        }
        return tireList;
    }

    private VehicleEntity getVehicleEntity(AssetTireBatchInstallExcelVo data) {
        VehicleEntity vehicle = null;
        for (VehicleEntity vehicleEntity : vehicleList) {
            if (data.getLicensePlate().equals(vehicleEntity.getLicensePlate())) {
                vehicle = vehicleEntity;
                break;
            }
        }
        return vehicle;
    }

    private AssetTire getAssetTire(AssetTireBatchInstallExcelVo data,
            List<AssetTire> tireList) {
        AssetTire assetTire = null;
        for (AssetTire tire : tireList) {
            if (data.getTireCode().equals(tire.getCode())) {
                assetTire = tire;
                break;
            }
        }
        return assetTire;
    }
}
