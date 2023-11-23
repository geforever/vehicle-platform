package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/9/14 14:57
 */
@Getter
@Setter
public class AssetTireConditionQueryParam extends PageParam {

    /**
     * 轮胎编号
     */
    private String tireCode;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 所属车队
     */
    private String fleetName;

    /**
     * 传感器ID
     */
    private String sensorId;

    /**
     * 轮胎状态:1-仓库待用,2-使用中,3-已变卖,4-已调拨
     */
    private Integer tireStatus;

    /**
     * 是否绑定传感器:0-未绑定,1-已绑定
     */
    private Integer hasSensor;
}
