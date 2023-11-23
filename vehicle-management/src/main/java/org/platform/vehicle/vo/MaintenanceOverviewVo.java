package org.platform.vehicle.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/13 10:51
 */
@Data
public class MaintenanceOverviewVo {

    /**
     * 累计维保订单数
     */
    private Long totalMaintenanceOrderCount;

    /**
     * 累计维保订金额
     */
    private BigDecimal totalMaintenanceOrderAmount;

    /**
     * 今日订单数
     */
    private Long todayMaintenanceOrderCount;

    /**
     * 本周订单数
     */
    private Long weekMaintenanceOrderCount;

    /**
     * 本月订单数
     */
    private Long monthMaintenanceOrderCount;
}
