package org.platform.vehicle.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/16 16:32
 */
@Data
public class AssetTireStockInRecordExportVo {

    /**
     * 轮胎编号
     */
    @ExcelProperty("轮胎编号")
    private String tireCode;

    /**
     * 所属客户
     */
    @ExcelProperty("所属客户")
    private String customerName;

    /**
     * 所属车队
     */
    @ExcelProperty("所属车队")
    private String fleetName;

    /**
     * 所属仓库
     */
    @ExcelProperty("所属仓库")
    private String warehouseName;

    /**
     * 入库类型
     */
    @ExcelProperty("入库类型")
    private String stockTypeStr;

    /**
     * 供给方
     */
    @ExcelProperty("供给方")
    private String target;

    /**
     * 品牌
     */
    @ExcelProperty("品牌")
    private String tireBrand;

    /**
     * 型号
     */
    @ExcelProperty("型号")
    private String tireSpec;

    /**
     * 新旧程度
     */
    @ExcelProperty("新旧程度")
    private String degree;

    /**
     * 登记里程KM
     */
    @ExcelProperty("登记里程KM")
    private String mileage;

    /**
     * 入库人
     */
    @ExcelProperty("入库人")
    private String createPerson;

    /**
     * 入库时间
     */
    @ExcelProperty("入库时间")
    private String createTime;
}
