package org.platform.vehicle.controller;

import org.platform.vehicle.service.WarningTraceRecordService;
import org.platform.vehicle.vo.TirePressureAndTemperatureStatisticVo;
import org.platform.vehicle.vo.TrendStatisticVo;
import org.platform.vehicle.vo.VehicleAndTireStatisticVo;
import org.platform.vehicle.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 温压管理-胎温胎压概览
 *
 * @Author gejiawei
 * @Date 2023/10/10 11:16
 */
@Slf4j
@RestController
@RequestMapping("/monitoringTireStatistic")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MonitoringTireStatisticController {

    private final WarningTraceRecordService warningTraceRecordService;

    /**
     * 温压管理-胎温胎压概览-胎温胎压报警
     *
     * @return
     */
    @GetMapping("/getWarningData")
    public BaseResponse<TirePressureAndTemperatureStatisticVo> getWarningData() {
        log.info("温压管理-胎温胎压概览-胎温胎压报警, url:/warningTraceRecord/getWarningData");
        return warningTraceRecordService.getWarningData();
    }

    /**
     * 温压管理-胎温胎压概览-车辆轮胎概况
     *
     * @return
     */
    @GetMapping("/getVehicleAndTireData")
    public BaseResponse<VehicleAndTireStatisticVo> getVehicleAndTireData() {
        log.info(
                "温压管理-胎温胎压概览-车辆轮胎概况, url:/warningTraceRecord/getVehicleAndTireData");
        return warningTraceRecordService.getVehicleAndTireData();
    }

    /**
     * 温压管理-胎温胎压概览-趋势图
     *
     * @return
     */
    @GetMapping("/getTrendData")
    public BaseResponse<TrendStatisticVo> getTrendData() {
        log.info("温压管理-胎温胎压概览-趋势图, url:/warningTraceRecord/getTrendData");
        return warningTraceRecordService.getTrendData();
    }
}
