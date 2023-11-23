package org.platform.vehicle.vo;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/2 15:07
 */
@Data
public class TireTrendParam {

    /**
     * 设备ID
     *
     * @required
     */
    private String gpsId;

    /**
     * 是否显示速度曲线:0-否,1是
     */
    private Integer isShowSpeedCurve = 0;

    /**
     * 是否显示海拔曲线:0-否,1是
     */
    private Integer isShowAltitudeCurve = 0;

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

    /**
     * 主车轮胎位置ID集合
     */
    private List<Integer> mainVehicleTireSiteIdList;

    /**
     * 挂车轮胎位置ID集合
     */
    private List<Integer> minorVehicleTireSiteIdList;
}
