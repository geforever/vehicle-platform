package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/18 17:26
 */
@Data
public class InstallTireParam {

    /**
     * 主键Id
     *
     * @required
     */
    private Integer id;

    /**
     * 轮位ID
     *
     * @required
     */
    private Integer tireSite;

    /**
     * 车牌号
     *
     * @required
     */
    private String licensePlate;

    /**
     * 接收人
     */
    private String target;

    /**
     * 轮胎号
     *
     * @required
     */
    private String tireCode;

    /**
     * 传感器ID
     *
     * @required
     */
    private String sensorId;

}
