package org.platform.vehicle.web.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/31 14:26
 */
@Data
public class VehicleSpecContextVo implements Serializable {

    private static final long serialVersionUID = 1915934568708543810L;

    /**
     * 车辆型号
     */
    private Integer specId;

    /**
     * 车型分类（1：主车；2：挂车；3：主挂一体）
     */
    private Integer specType;

    /**
     * 每轴轮数
     */
    private String wheelCount;

    /**
     * 车轴类型
     */
    private String wheelbaseType;

    /**
     * 低压警报等级1
     */
    private BigDecimal lowPressureAlarmLevel1;

    /**
     * 高压警报等级1
     */
    private BigDecimal highPressureAlarmLevel1;

    /**
     * 高温警报等级1
     */
    private BigDecimal highTemperatureAlarmLevel1;

    /**
     * 低压警报等级2
     */
    private BigDecimal lowPressureAlarmLevel2;

    /**
     * 高压警报等级2
     */
    private BigDecimal highPressureAlarmLevel2;

    /**
     * 高温警报等级2
     */
    private BigDecimal highTemperatureAlarmLevel2;

    /**
     * 二级警报启用状态
     */
    private Boolean enableLev2Alarm;
}
