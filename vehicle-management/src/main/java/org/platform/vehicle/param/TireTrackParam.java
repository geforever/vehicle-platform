package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/2 16:02
 */
@Data
public class TireTrackParam {

    /**
     * gps_id
     *
     * @required
     */
    private String gpsId;

    /**
     * 开始时间(yyyy-MM-dd HH:mm:ss)
     *
     * @required
     */
    private String startTime;

    /**
     * 结束时间(yyyy-MM-dd HH:mm:ss)
     *
     * @required
     */
    private String endTime;
}
