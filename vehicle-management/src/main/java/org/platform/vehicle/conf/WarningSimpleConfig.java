package org.platform.vehicle.conf;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/20 16:16
 */
@Getter
@RefreshScope
@Component
public class WarningSimpleConfig {

    /**
     * 紧急类告警时间间隔(小时)
     */
    @Value("${warning_interval.common_interval}")
    private Integer urgentInterval;

    /**
     * 常规类一般告警
     */
    @Value("${warning_interval.common_power_offOver_interval}")
    private Integer commonInterval;

    /**
     * 常规类无信号报警断电告警超时时间间隔(小时)
     */
    @Value("${warning_interval.common_power_off_over_interval}")
    private Integer commonPowerOffOverInterval;

    /**
     * 常规类无信号报警通电告警超时时间间隔(小时)
     */
    @Value("${warning_interval.common_power_on_over_interval}")
    private Integer commonPowerOnOverInterval;

}
