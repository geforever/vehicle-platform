package org.platform.vehicle.param;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/24 16:27
 */
@Data
public class TireCheckDataParam {

    /**
     * 接收器(GPS_ID,设备client_id)
     *
     * @required
     */
    private String receiverId;

    /**
     * 流水号
     */
    private Integer serialNo;

    /**
     * 挂车状态:1-上挂,2-下挂
     */
    private Integer trailerStatus;

    /**
     * 挂车中继器ID,无挂车中继器默认:000000
     */
    private String guaRepeaterId;

    /**
     * 轮胎数据明细
     *
     * @required
     */
    private List<TireCheckDataDetailParam> tireCheckDataDetailParamList;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;
}
