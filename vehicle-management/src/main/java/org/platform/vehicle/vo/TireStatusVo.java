package org.platform.vehicle.vo;

import java.util.Date;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/30 13:41
 */
@Data
public class TireStatusVo {

    /**
     * 轮位名称
     */
    private String tireSiteName;

    /**
     * 轮胎号
     */
    private String tireCode;

    /**
     * 轮胎状态
     */
    private String currentStatus;

    /**
     * 传感器ID
     */
    private String sensorId;

    /**
     * 当前压力(bar)
     */
    private String currentTirePressure;

    /**
     * 当前温度(℃)
     */
    private String currentTireTemperature;

    /**
     * 电池电压(V)
     */
    private String batteryVoltage;

    /**
     * 安装时间
     */
    private Date installTime;

    /**
     * 品牌
     */
    private String tireBrand;

    /**
     * 一级高压
     */
    private String firstHighPressure;

    /**
     * 二级高压
     */
    private String secondHighPressure;

    /**
     * 一级低压
     */
    private String firstLowPressure;

    /**
     * 二级低压
     */
    private String secondLowPressure;

    /**
     * 一级高温
     */
    private String firstHighTemperature;

    /**
     * 二级高温
     */
    private String secondHighTemperature;

    /**
     * 监控时间
     */
    private Date monitorTime;

}
