package org.platform.vehicle.dto;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/18 14:49
 */
@Data
public class AssetTireFitDto {

    /**
     * 主车车牌
     */
    private String licensePlate;

    /**
     * 挂车车牌
     */
    private String guaLicensePlate;

    /**
     * 车辆型号ID
     */
    private Integer vehicleSpecId;

    /**
     * 是否有轮位:0-无, 1-有
     */
    private Integer hasWheelCount;

    /**
     * 轮胎是否全部安装
     */
    private Boolean wheelsComplete;

    /**
     * 传感器是否全部安装
     */
    private Boolean sensorsComplete;

}
