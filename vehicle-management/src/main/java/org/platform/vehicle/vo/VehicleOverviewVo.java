package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/13 09:38
 */
@Data
public class VehicleOverviewVo {

    /**
     * 车辆总数
     */
    private Long totalVehicleCount;

    /**
     * 今日接车数
     */
    private Long todayReceiveCount;

    /**
     * 今日交车数
     */
    private Long todayDeliverCount;

    /**
     * 今日新增
     */
    private Long todayIncreaseCount;

    /**
     * 本周新增
     */
    private Long weekIncreaseCount;

    /**
     * 本月新增
     */
    private Long monthIncreaseCount;
}
