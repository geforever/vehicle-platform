package org.platform.vehicle.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/22 11:46
 */
@Data
public class AssetTireBatchStockInExcelVo {

    /**
     * 轮胎编号
     */
    @ExcelProperty("轮胎号 *")
    private String tireCode;

    /**
     * 轮胎品牌
     */
    @ExcelProperty("轮胎品牌 *")
    private String tireBrandName;

    /**
     * 轮胎型号
     */
    @ExcelProperty("轮胎型号 *")
    private String tireSpecName;

    /**
     * 所属车队
     */
    @ExcelProperty("所属车队 *")
    private String fleetName;

    /**
     * 入库仓库
     */
    @ExcelProperty("入库仓库")
    private String warehouseName;

    /**
     * 入库类型:1:采集,2赔付
     */
    @ExcelProperty("入库类型")
    private String stockInType;

    /**
     * 供给方
     */
    @ExcelProperty("供给方")
    private String target;

    /**
     * 新旧程度:30%,50%,70%,100%
     */
    @ExcelProperty("新旧程度（%）")
    private String degree;

    /**
     * 当前里程
     */
    @ExcelProperty("当前里程（KM）")
    private Integer mileage;
}
