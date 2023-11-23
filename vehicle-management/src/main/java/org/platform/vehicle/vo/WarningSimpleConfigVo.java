package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/26 13:06
 */
@Data
public class WarningSimpleConfigVo {

    /**
     * 紧急类告警时间间隔(小时)
     */
    private Integer urgentInterval = 1;

    /**
     * 常规类一般告警
     */
    private Integer commonInterval = 3 * 24;

    /**
     * 常规类无信号报警断电告警超时时间间隔(小时)
     */
    private Integer commonPowerOffOverInterval = 3 * 24;

    /**
     * 常规类无信号报警通电告警超时时间间隔(小时)
     */
    private Integer commonPowerOnOverInterval = 6;

}
