package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/24 16:33
 */
@Data
public class TireCheckDataDetailParam {

    /**
     * 轮胎类型:0-主车,1-挂车
     */
    private Integer type;

    /**
     * 轮胎位置
     */
    private String tireSiteId;

    /**
     * 胎压传感器ID
     */
    private String tireSensorId;

    /**
     * 电压
     */
    private String voltage;

    /**
     * 胎压
     */
    private String tirePressure;

    /**
     * 胎温
     */
    private String tireTemperature;

    /**
     * 轮胎报警数据
     */
    private TireCheckWarningDataParam tireWarningData;

}
