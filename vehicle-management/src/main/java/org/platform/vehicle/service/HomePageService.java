package org.platform.vehicle.service;

import org.platform.vehicle.vo.MaintenanceOverviewVo;
import org.platform.vehicle.vo.VehicleOverviewVo;
import org.platform.vehicle.vo.WarmPressingOverviewVo;
import org.platform.vehicle.response.BaseResponse;

/**
 * @Author gejiawei
 * @Date 2023/10/13 09:38
 */
public interface HomePageService {


    /**
     * 首页-车辆概况
     */
    BaseResponse<VehicleOverviewVo> getVehicleOverview();

    /**
     * 温压概况
     */
    BaseResponse<WarmPressingOverviewVo> getWarmPressingOverview();

    /**
     * 维保概况
     */
    BaseResponse<MaintenanceOverviewVo> getMaintenanceOverview();

    /**
     * 初始化数据接口
     *
     * @return
     */
    BaseResponse<String> initData();
}
