package org.platform.vehicle.vo;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/10 14:58
 */
@Data
public class TrendStatisticVo {

    /**
     * 车辆7日绑定新增趋势
     */
    private List<Long> vehicleBindTrend;

    /**
     * 轮胎7日绑定新增趋势
     */
    private List<Long> tireBindTrend;

    /**
     * 高温告警7日新增趋势
     */
    private List<Long> highTemperatureWarningTrend;

    /**
     * 高压告警7日新增趋势
     */
    private List<Long> highPressureWarningTrend;

    /**
     * 低压告警7日新增趋势
     */
    private List<Long> lowPressureWarningTrend;
}
