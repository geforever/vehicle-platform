package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/12 13:58
 */
@Data
public class WarmPressingTireVo {

    /**
     * 轮位
     */
    private Integer tireSiteId;

    /**
     * 轮位名称
     */
    private String tireSitName;

    /**
     * 胎压
     */
    private String tirePressure;

    /**
     * 胎温
     */
    private String tireTemperature;

    /**
     * 颜色:1-红:一级高温、一级高压、一级低压报警、急漏气, 2-黄:二级高温、二级高压、二级低压报警、低电压报警, 3-绿:无告警, 4-灰:无信号报警
     */
    private Integer color = 3;

}
