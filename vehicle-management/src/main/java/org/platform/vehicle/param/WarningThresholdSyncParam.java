package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/30 11:28
 */
@Data
public class WarningThresholdSyncParam {

    /**
     * 主车车牌
     *
     * @required
     */
    private String mainLicensePlate;

    /**
     * 挂车车牌
     */
    private String minorLicensePlate;

}
