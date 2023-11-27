package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/27 17:11
 */
@Data
public class TrailerInstallCallbackParam {
    /**
     * 接收器ID
     */
    private String receiverIdNumber;

    /**
     * 主车中继器ID
     */
    private String mainRelayId;

    /**
     * 副车中继器ID
     */
    private String minorRelayId;

}
