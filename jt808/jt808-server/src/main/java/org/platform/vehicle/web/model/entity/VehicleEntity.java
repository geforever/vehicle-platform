package org.platform.vehicle.web.model.entity;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/2 11:34
 */

@Data
public class VehicleEntity {

    /**
     * 流水ID
     */
    private Integer id;

    /**
     * 车辆型号
     */
    private Integer specId;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 挂车车牌号
     */
    private String guaLicensePlate;

    /**
     * 中继器ID
     */
    private String repeaterIdNumber;

    /**
     * 拖车中继器ID
     */
    private String trailerRepeaterIdNumber;

    /**
     * 接收器ID
     */
    private String receiverIdNumber;

}
