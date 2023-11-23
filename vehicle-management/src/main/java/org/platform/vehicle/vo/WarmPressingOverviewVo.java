package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/13 10:36
 */
@Data
public class WarmPressingOverviewVo {

    /**
     * 温压异常数
     */
    private Long totalCount;

    /**
     * 高温报警数
     */
    private Long highTemperatureCount;

    /**
     * 高压报警数
     */
    private Long highPressureCount;

    /**
     * 低压报警数
     */
    private Long lowPressureCount;

    /**
     * 低电报警数
     */
    private Long lowElectricityCount;

    /**
     * 离线车辆数
     */
    private Long vehicleOfflineCount;
}
