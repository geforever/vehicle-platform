package org.platform.vehicle.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/23 10:50
 */
@Data
public class AssetTireBatchInstallExcelVo {

    /**
     * 轮胎编号
     */
    @ExcelProperty("轮胎号 *")
    private String tireCode;

    /**
     * 车牌号
     */
    @ExcelProperty("车牌号 *")
    private String licensePlate;

    /**
     * 轮位
     */
    @ExcelProperty("轮位 *")
    private Integer tireSite;

    /**
     * 传感器ID
     */
    @ExcelProperty("传感器ID")
    private String sensorId;

    /**
     * 领用司机
     */
    @ExcelProperty("领用司机")
    private String driverName;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;
}
