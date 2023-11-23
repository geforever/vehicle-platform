package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/10 11:41
 */
@Data
public class VehicleAndTireStatisticVo {

    /**
     * 车辆-总数
     */
    private Long totalVehicleCount;

    /**
     * 车辆-已绑定(中继器)
     */
    private Long vehicleBoundCount;

    /**
     * 车辆-未绑定(中继器)
     */
    private Long vehicleUnboundCount;

    /**
     * 轮胎-总数
     */
    private Long totalTireCount;

    /**
     * 轮胎-已绑定(传感器)
     */
    private Long tireBoundCount;

    /**
     * 轮胎-未绑定(传感器)
     */
    private Long tireUnboundCount;
}
