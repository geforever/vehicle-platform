package org.platform.vehicle.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/9 14:47
 */
@Data
public class WarningTraceRecordExportVo {

    /**
     * 轮胎号
     */
    @ExcelProperty("轮胎号")
    private String tireCode;

    /**
     * 车队名称
     */
    @ExcelProperty("车队名称")
    private String fleetName;

    /**
     * 客户名称
     */
    @ExcelProperty("客户名称")
    private String clientName;

    /**
     * 车牌
     */
    @ExcelProperty("车牌")
    private String licensePlate;

    /**
     * 轮位名称
     */
    @ExcelProperty("轮位名称")
    private String tireSiteName;

    /**
     * 报警类型:逗号分割
     */
    @ExcelProperty("报警类型")
    private String warningType;

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
     * 电压
     */
    @ExcelProperty("电压")
    private String voltage;

    /**
     * 当前地址
     */
    @ExcelProperty("当前地址")
    private String location;

    /**
     * 报警时间
     */
    @ExcelProperty("报警时间")
    private String createTime;

    /**
     * 跟进时间
     */
    @ExcelProperty("跟进时间")
    private String followTime;

    /**
     * 原因
     */
    @ExcelProperty("原因")
    private String reason;
}
