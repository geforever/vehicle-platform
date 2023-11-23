package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/27 17:00
 */
@Data
public class VehicleHangParam {

    /**
     * 主车车牌
     */
    private String mainLicensePlate;

    /**
     * 挂车车牌
     */
    private String minorLicensePlate;

    /**
     * 操作类型:1-上挂,2-下挂
     *
     * @required true
     */
    private Integer type;

}
