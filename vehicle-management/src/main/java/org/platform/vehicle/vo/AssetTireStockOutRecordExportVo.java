package org.platform.vehicle.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/16 16:54
 */
@Data
public class AssetTireStockOutRecordExportVo {

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
     * 出库类型
     */
    @ExcelProperty("出库类型")
    private String stockTypeStr;

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
     * 接收方
     */
    @ExcelProperty("接收方")
    private String target;


    /**
     * 出库人
     */
    @ExcelProperty("出库人")
    private String createPerson;

    /**
     * 出库时间
     */
    @ExcelProperty("出库时间")
    private String createTime;
}
