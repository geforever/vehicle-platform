package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/10 11:21
 */
@Data
public class TirePressureAndTemperatureStatisticVo {

    /**
     * 高温报警
     */
    private Long highTemperatureWarning;

    /**
     * 高压报警
     */
    private Long highPressureWarning;

    /**
     * 低压报警
     */
    private Long lowPressureWarning;

    /**
     * 离线报警
     */
    private Long offlineWarning;
}
