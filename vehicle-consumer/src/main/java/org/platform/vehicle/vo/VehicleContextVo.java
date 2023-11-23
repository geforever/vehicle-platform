package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/8 15:22
 */
@Data
public class VehicleContextVo {

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 车队ID
     */
    private Integer fleetId;

    /**
     * 车辆型号ID
     */
    private Integer specId;
}
