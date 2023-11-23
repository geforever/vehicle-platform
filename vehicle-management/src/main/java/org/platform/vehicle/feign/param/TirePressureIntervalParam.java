package org.platform.vehicle.feign.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/20 11:30
 */
@Data
public class TirePressureIntervalParam {

    private Integer idx;

    /**
     * 高压
     */
    private Double gaoya;

    /**
     * 低压
     */
    private Double diya;
}
