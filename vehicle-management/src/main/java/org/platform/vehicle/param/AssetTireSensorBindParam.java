package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/15 11:47
 */
@Data
public class AssetTireSensorBindParam {

    /**
     * 主键ID
     *
     * @required
     */
    private Integer id;

    /**
     * 传感器ID
     *
     * @required
     */
    private String sensorId;

    /**
     * 传感器绑定类型:1-捆绑式,2-背贴式,3-气门嘴式
     *
     * @required
     */
    private Integer sensorType;

    /**
     * :0-解绑,1-捆绑
     *
     * @required
     */
    private Integer isBindSensor;
}
