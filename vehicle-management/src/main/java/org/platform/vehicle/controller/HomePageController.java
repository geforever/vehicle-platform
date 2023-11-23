package org.platform.vehicle.controller;

import org.platform.vehicle.service.HomePageService;
import org.platform.vehicle.vo.MaintenanceOverviewVo;
import org.platform.vehicle.vo.VehicleOverviewVo;
import org.platform.vehicle.vo.WarmPressingOverviewVo;
import org.platform.vehicle.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @Author gejiawei
 * @Date 2023/10/13 09:33
 */
@Slf4j
@RestController
@RequestMapping("/homePage")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HomePageController {

    private final HomePageService homePageService;

    /**
     * 首页-车辆概况
     */
    @GetMapping("/vehicleOverview")
    public BaseResponse<VehicleOverviewVo> getVehicleOverview() {
        log.info("首页-车辆概况, url:/homePage/vehicleOverview");
        return homePageService.getVehicleOverview();
    }

    /**
     * 温压概况
     */
    @GetMapping("/warmPressingOverview")
    public BaseResponse<WarmPressingOverviewVo> getWarmPressingOverview() {
        log.info("首页-温压概况, url:/homePage/warmPressingOverview");
        return homePageService.getWarmPressingOverview();
    }

    /**
     * 维保概况
     */
    @GetMapping("/maintenanceOverview")
    public BaseResponse<MaintenanceOverviewVo> getMaintenanceOverview() {
        log.info("首页-维保概况, url:/homePage/maintenanceOverview");
        return homePageService.getMaintenanceOverview();
    }

    /**
     * 初始化数据接口
     *
     * @return
     */
    @PutMapping("/initData")
    public BaseResponse<String> initData() {
        log.info("首页-初始化数据, url:/homePage/initData");
        return homePageService.initData();
    }
}
