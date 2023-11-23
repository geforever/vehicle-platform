package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/18 16:00
 */
@Data
public class WarmPressingPageVo {

    /**
     * 车牌
     */
    private String licensePlate;

    /**
     * 主车车牌
     */
    private String mainLicensePlate;

    /**
     * 副车车牌
     */
    private String minorLicensePlate;

    /**
     * 主车中继器ID
     */
    private String mainRelayId;

    /**
     * 副车中继器ID
     */
    private String minorRelayId;

    /**
     * 主车挂车绑定类型:1-主车挂车,2-挂车主车
     */
    private Integer type;

    /**
     * 颜色:1-红:一级高温、一级高压、一级低压报警、急漏气, 1-黄:二级高温、二级高压、二级低压报警、低电压报警, 3-绿:无告警, 4-灰:无信号报警
     */
    private Integer color;
}
