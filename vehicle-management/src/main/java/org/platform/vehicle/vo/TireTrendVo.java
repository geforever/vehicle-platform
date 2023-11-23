package org.platform.vehicle.vo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/2 15:14
 */
@Data
public class TireTrendVo {

    /**
     * 轮胎压力趋势
     */
    private List<TirePressureTrendVo> tirePressureTrend = new ArrayList<>();

    /**
     * 轮胎温度趋势
     */
    private List<TireTemperatureTrendVo> tireTemperatureTrend = new ArrayList<>();

    /**
     * 轮胎数据趋势
     */
    private List<TireTrendDetailVo> tireTrendDetail = new ArrayList<>();

    /**
     * 速度趋势
     */
    private List<SpeedTrendVo> speedTrend = new ArrayList<>();

    /**
     * 海拔趋势
     */
    private List<AltitudeTrendVo> altitudeTrend = new ArrayList<>();


}
