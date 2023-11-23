package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/30 10:58
 */
@Data
public class RelayBindParam {

    /**
     * 主车车牌
     */
    private String mainLicensePlate;

    /**
     * 挂车车牌
     */
    private String minorLicensePlate;

    /**
     * 主车中继器
     */
    private String mainRelay;

    /**
     * 挂车中继器
     */
    private String minorRelay;

    /**
     * 绑定中继器类型:1-只绑主车,2-绑定主车、挂车
     *
     * @required
     */
    private Integer type;

}
