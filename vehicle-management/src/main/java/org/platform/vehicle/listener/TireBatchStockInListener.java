package org.platform.vehicle.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.platform.vehicle.constant.AssetTireConstant;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.entity.AssetTireStockRecord;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.TireBrandEntity;
import org.platform.vehicle.entity.TireSpecEntity;
import org.platform.vehicle.entity.WarehouseSetting;
import org.platform.vehicle.helper.jdbc.AssetTireJdbc;
import org.platform.vehicle.mapper.AssetTireMapper;
import org.platform.vehicle.util.AssetTireStockRecordAsync;
import org.platform.vehicle.vo.AssetTireBatchStockInExcelVo;
import org.platform.vehicle.vo.ImportedDataErrVo;
import org.platform.vehicle.vo.ImportedResultVo;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.vo.UserVo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author gejiawei
 * @Date 2023/9/22 11:59
 */
@Slf4j
public class TireBatchStockInListener implements ReadListener<AssetTireBatchStockInExcelVo> {

    /**
     * 每隔1000条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;
    public static final int LIMIT_ROW = 10000;
    private List<AssetTireBatchStockInExcelVo> cachedDataList = ListUtils.newArrayListWithExpectedSize(
            BATCH_COUNT);

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


    public ImportedResultVo getImportedResultVo() {
        ImportedResultVo importedResultVo = new ImportedResultVo();
        importedResultVo.setCompleteCount(completeCount);
        importedResultVo.setErrorCount(errorCount);
        importedResultVo.setErrorList(errorList);
        return importedResultVo;
    }


    private UserVo user;
    private AssetTireMapper assetTireMapper;
    private AssetTireJdbc assetTireJdbc;
    private AssetTireStockRecordAsync assetTireStockRecordAsync;
    private List<SysCustomer> fleetList;
    private List<WarehouseSetting> warehouseSettingList;
    private List<TireBrandEntity> tireBrandList;
    private List<TireSpecEntity> tireSpecList;

    public TireBatchStockInListener() {
    }

    public TireBatchStockInListener(
            UserVo user,
            AssetTireMapper assetTireMapper,
            AssetTireJdbc assetTireJdbc,
            AssetTireStockRecordAsync assetTireStockRecordAsync,
            List<SysCustomer> fleetList,
            List<WarehouseSetting> warehouseSettingList,
            List<TireBrandEntity> tireBrandList,
            List<TireSpecEntity> tireSpecList
    ) {
        this.user = user;
        this.assetTireMapper = assetTireMapper;
        this.assetTireJdbc = assetTireJdbc;
        this.assetTireStockRecordAsync = assetTireStockRecordAsync;
        this.fleetList = fleetList;
        this.warehouseSettingList = warehouseSettingList;
        this.tireBrandList = tireBrandList;
        this.tireSpecList = tireSpecList;
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
    public void invoke(AssetTireBatchStockInExcelVo data, AnalysisContext analysisContext) {
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

    private SysCustomer getFleet(AssetTireBatchStockInExcelVo data) {
        for (SysCustomer sysCustomer : fleetList) {
            if (data.getFleetName().equals(sysCustomer.getName())) {
                return sysCustomer;
            }
        }
        return null;
    }

    /**
     * 校验轮胎号是否存在
     *
     * @param data
     * @param assetTireList
     * @return
     */
    private boolean isTireCodeExist(AssetTireBatchStockInExcelVo data,
            List<AssetTire> assetTireList) {
        boolean isExist = false;
        for (AssetTire assetTire : assetTireList) {
            if (data.getTireCode().equals(assetTire.getCode())) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    private void saveData(List<AssetTireBatchStockInExcelVo> cachedDataList) {
        if (cachedDataList.isEmpty()) {
            return;
        }
        // 轮胎入库数据
        List<AssetTire> insertTireParamList = new ArrayList<>();
        // 轮胎入库记录数据
        List<AssetTireStockRecord> insertStockRecordParamList = new ArrayList<>();
        // 根据轮胎号查询轮胎信息
        List<AssetTire> assetTireList = this.getAssetTireList();
        for (AssetTireBatchStockInExcelVo data : cachedDataList) {
            // 校验数据
            Result result = this.getResult(data, assetTireList);
            if (result == null) {
                continue;
            }
            completeCount++;
            AssetTire insertParam = this.getAssetTireInsertParam(data, result);
            insertTireParamList.add(insertParam);
            /**
             * 填写入库仓库,保存入库记录
             */
            if (StringUtils.isNotBlank(data.getWarehouseName())) {
                AssetTireStockRecord assetTireStockRecord = this.getAssetTireStockRecordInsertParam(
                        data, result);
                insertStockRecordParamList.add(assetTireStockRecord);
            }
        }
        this.execute(insertTireParamList, insertStockRecordParamList);
    }

    private void execute(List<AssetTire> insertTireParamList,
            List<AssetTireStockRecord> insertStockRecordParamList) {
        // 轮胎批量导入-插入数据库耗时
        long startTime = System.currentTimeMillis();
        if (!insertTireParamList.isEmpty()) {
            try {
                assetTireJdbc.saveBatchTire(insertTireParamList);
            } catch (SQLException e) {
                throw new BaseException("99999", "轮胎批量导入-插入数据库异常");
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("轮胎批量导入-插入数据库耗时:{}", endTime - startTime);
        // 轮胎入库数据批量保存
        if (!insertStockRecordParamList.isEmpty()) {
            assetTireStockRecordAsync.batchSave(insertStockRecordParamList);
        }
    }

    private AssetTireStockRecord getAssetTireStockRecordInsertParam(
            AssetTireBatchStockInExcelVo data,
            Result result) {
        AssetTireStockRecord assetTireStockRecord = new AssetTireStockRecord();
        assetTireStockRecord.setTireCode(data.getTireCode());
        assetTireStockRecord.setFleetId(result.fleet.getId());
        assetTireStockRecord.setClientName(user.getCompanyName());
        assetTireStockRecord.setFleetName(result.fleet.getName());
        assetTireStockRecord.setWarehouseId(result.warehouseSetting.getId());
        assetTireStockRecord.setWarehouseName(result.warehouseSetting.getName());
        assetTireStockRecord.setType(AssetTireConstant.STOCK_RECORD_TYPE_IN);
        assetTireStockRecord.setStockType(result.stockInType);
        assetTireStockRecord.setTarget(data.getTarget());
        assetTireStockRecord.setTireBrand(result.tireBrand.getBrandName());
        assetTireStockRecord.setTireSpec(result.tireSpec.getSpecName());
        assetTireStockRecord.setDegree(data.getDegree());
        assetTireStockRecord.setMileage(data.getMileage());
        assetTireStockRecord.setCreatePerson(user.getName());
        return assetTireStockRecord;
    }

    private List<AssetTire> getAssetTireList() {
        List<String> tireCodeList = new ArrayList<>();
        for (AssetTireBatchStockInExcelVo data : cachedDataList) {
            tireCodeList.add(data.getTireCode());
        }
        List<AssetTire> assetTireList = new ArrayList<>();
        if (!tireCodeList.isEmpty()) {
            assetTireList = assetTireMapper.selectList(
                    new LambdaQueryWrapper<AssetTire>()
                            .in(AssetTire::getCode, tireCodeList)
                            .eq(AssetTire::getClientId, user.getCompanyId())
                            .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        }
        return assetTireList;
    }

    private AssetTire getAssetTireInsertParam(AssetTireBatchStockInExcelVo data, Result result) {
        AssetTire insertParam = new AssetTire();
        insertParam.setCode(data.getTireCode());
        insertParam.setClientId(user.getCompanyId());
        insertParam.setFleetId(result.fleet.getId());
        if (StringUtils.isNotBlank(data.getWarehouseName())) {
            insertParam.setWarehouseId(result.warehouseSetting.getId());
        }
        insertParam.setTireBrandId(result.tireBrand.getId());
        insertParam.setTireSpecId(result.tireSpec.getId());
        insertParam.setDegree(data.getDegree());
        insertParam.setMileage(data.getMileage());
        insertParam.setTireStatus(AssetTireConstant.TIRE_STATUS_WAREHOUSE);
        insertParam.setHasSensor(AssetTireConstant.NOT_BIND_SENSOR);
        insertParam.setIsDelete(AssetTireConstant.NOT_DELETE);
        insertParam.setCreatePerson(user.getName());
        return insertParam;
    }

    private Result getResult(AssetTireBatchStockInExcelVo data, List<AssetTire> assetTireList) {
        TireSpecEntity tireSpec;
        SysCustomer fleet;
        TireBrandEntity tireBrand;
        WarehouseSetting warehouseSetting = null;
        // 校验轮胎号是否填写
        if (StringUtils.isBlank(data.getTireCode())) {
            errorCount++;
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                    "轮胎号未填写");
            errorList.add(importedDataErrVo);
            return null;
        }
        // 校验轮胎号是否存在
        if (this.isTireCodeExist(data, assetTireList)) {
            errorCount++;
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                    "与现有数据重复");
            errorList.add(importedDataErrVo);
            return null;
        }
        // 校验所属车队是否填写
        if (StringUtils.isBlank(data.getFleetName())) {
            errorCount++;
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                    "所属车队未填写");
            errorList.add(importedDataErrVo);
            return null;
        }
        // 校验车队名称是否存在
        fleet = this.getFleet(data);
        if (fleet == null) {
            errorCount++;
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                    "车队名称不存在");
            errorList.add(importedDataErrVo);
            return null;
        }
        // 校验品牌名是否填写
        if (StringUtils.isBlank(data.getTireBrandName())) {
            errorCount++;
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                    "品牌名未填写");
            errorList.add(importedDataErrVo);
            return null;
        }
        // 校验轮胎品牌是否存在
        tireBrand = this.getTireBrand(data);
        if (tireBrand == null) {
            errorCount++;
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                    "轮胎品牌在该客户下不存在");
            errorList.add(importedDataErrVo);
            return null;
        }
        // 校验轮胎规格是否填写
        if (StringUtils.isBlank(data.getTireSpecName())) {
            errorCount++;
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                    "轮胎规格未填写");
            errorList.add(importedDataErrVo);
            return null;
        }
        // 校验轮胎规格
        tireSpec = this.getTireSpec(data);
        if (tireSpec == null) {
            errorCount++;
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                    "轮胎规格在该客户下不存在");
            errorList.add(importedDataErrVo);
            return null;
        }
        // 获取入库类型
        Integer stockInType = this.getStockInType(data.getStockInType());
        if (stockInType == null) {
            errorCount++;
            ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                    "入库类型不存在");
            errorList.add(importedDataErrVo);
            return null;
        }
        // 入库仓库存在,则校验入库仓库是否存在
        if (StringUtils.isNotBlank(data.getWarehouseName())) {
            warehouseSetting = this.getWarehouse(data);
            if (warehouseSetting == null) {
                errorCount++;
                ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                        "入库仓库不存在");
                errorList.add(importedDataErrVo);
                return null;
            }
        }
        // 轮胎新旧程度校验
        if (StringUtils.isNotBlank(data.getDegree())) {
            if (!data.getDegree().equals("30") && !data.getDegree().equals("50")
                    && !data.getDegree().equals("70") && !data.getDegree().equals("100")) {
                errorCount++;
                ImportedDataErrVo importedDataErrVo = new ImportedDataErrVo(data.getTireCode(),
                        "轮胎新旧程度未填写");
                errorList.add(importedDataErrVo);
                return null;
            }
        }
        Result result = new Result(fleet, tireBrand, tireSpec, warehouseSetting, stockInType);
        return result;
    }

    private static class Result {

        public final SysCustomer fleet;
        public final TireBrandEntity tireBrand;
        public final TireSpecEntity tireSpec;
        public final WarehouseSetting warehouseSetting;
        public final Integer stockInType;

        public Result(SysCustomer fleet, TireBrandEntity tireBrand, TireSpecEntity tireSpec,
                WarehouseSetting warehouseSetting, Integer stockInType) {
            this.fleet = fleet;
            this.tireBrand = tireBrand;
            this.tireSpec = tireSpec;
            this.warehouseSetting = warehouseSetting;
            this.stockInType = stockInType;
        }
    }

    /**
     * 获取入库类型
     *
     * @param stockInType
     * @return
     */
    private Integer getStockInType(String stockInType) {
        Integer type = null;
        switch (stockInType) {
            case "集采":
                type = AssetTireConstant.STOCK_IN_TYPE_COLLECT;
                break;
            case "赔付":
                type = AssetTireConstant.STOCK_IN_TYPE_COMPENSATE;
                break;
            case "拆卸入库":
                type = AssetTireConstant.STOCK_IN_TYPE_UNINSTALL;
                break;
            case "调拨入库":
                type = AssetTireConstant.STOCK_IN_TYPE_TRANSFER;
                break;
            default:
                break;
        }
        return type;
    }

    private WarehouseSetting getWarehouse(AssetTireBatchStockInExcelVo data) {
        for (WarehouseSetting warehouseSettingEntity : warehouseSettingList) {
            if (data.getWarehouseName().equals(warehouseSettingEntity.getName())) {
                return warehouseSettingEntity;
            }
        }
        return null;
    }

    private TireSpecEntity getTireSpec(AssetTireBatchStockInExcelVo data) {
        for (TireSpecEntity tireSpecEntity : tireSpecList) {
            if (data.getTireSpecName().equals(tireSpecEntity.getSpecName())) {
                return tireSpecEntity;
            }
        }
        return null;
    }

    private TireBrandEntity getTireBrand(AssetTireBatchStockInExcelVo data) {
        for (TireBrandEntity tireBrandEntity : tireBrandList) {
            if (data.getTireBrandName().equals(tireBrandEntity.getBrandName())) {
                return tireBrandEntity;
            }
        }
        return null;
    }
}
