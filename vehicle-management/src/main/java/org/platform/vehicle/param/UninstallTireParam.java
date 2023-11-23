package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/19 14:26
 */
@Data
public class UninstallTireParam {

    /**
     * 主键ID
     *
     * @required
     */
    private Integer id;

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * 轮胎号
     *
     * @required
     */
    private String tireCode;

    /**
     * 仓库ID(可为空)
     */
    private Integer warehouseId;

    /**
     * 司机姓名
     */
    private String driver;

    /**
     * 备注
     */
    private String remark;

    /**
     * 解除传感器绑定:1-解除,0-不解除
     */
    private Integer isUnbindSensor;

}
