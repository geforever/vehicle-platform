package org.platform.vehicle.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/3 11:17
 */
@Data
public class WarningPressingExportVo {

    /**
     * 车牌号
     */
    @ExcelProperty("车牌号")
    private String licensePlate;

    /**
     * 轮胎号
     */
    @ExcelProperty("轮胎号")
    private String tireCode;

    /**
     * 轮位
     */
    @ExcelProperty("轮位")
    private String tireSiteName;

    /**
     * 时间
     */
    @ExcelProperty("时间")
    private String time;

    /**
     * 压力
     */
    @ExcelProperty("压力")
    private String pressure;

    /**
     * 温度
     */
    @ExcelProperty("温度")
    private String temperature;

    /**
     * 电池电压
     */
    @ExcelProperty("电池电压")
    private String batteryVoltage;
}
