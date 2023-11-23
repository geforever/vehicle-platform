package org.platform.vehicle.constant;


import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_FAST_LEAK;
import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_FIRST_HIGH_PRESSURE;
import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_FIRST_HIGH_TEMPERATURE;
import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_FIRST_LOW_PRESSURE;
import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_LOW_VOLTAGE;
import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_NO_SIGNAL;
import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_SECOND_HIGH_PRESSURE;
import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_SECOND_HIGH_TEMPERATURE;
import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_SECOND_LOW_PRESSURE;
import static org.platform.vehicle.constant.WarningConstant.WARNING_TYPE_SLOW_LEAK;

/**
 * @Author gejiawei
 * @Date 2023/10/25 13:20
 */
public enum WarningTypeEnum {

    /**
     * 一级高压报警
     */
    FIRST_HIGH_PRESSURE(WARNING_TYPE_FIRST_HIGH_PRESSURE, "一级高压报警"),
    /**
     * 一级高温报警
     */
    FIRST_HIGH_TEMPERATURE(WARNING_TYPE_FIRST_HIGH_TEMPERATURE, "一级高温报警"),
    /**
     * 一级低压报警
     */
    FIRST_LOW_PRESSURE(WARNING_TYPE_FIRST_LOW_PRESSURE, "一级低压报警"),
    /**
     * 二级高压报警
     */
    SECOND_HIGH_PRESSURE(WARNING_TYPE_SECOND_HIGH_PRESSURE, "二级高压报警"),
    /**
     * 二级高温报警
     */
    SECOND_HIGH_TEMPERATURE(WARNING_TYPE_SECOND_HIGH_TEMPERATURE, "二级高温报警"),
    /**
     * 二级低压报警
     */
    SECOND_LOW_PRESSURE(WARNING_TYPE_SECOND_LOW_PRESSURE, "二级低压报警"),
    /**
     * 低电压报警
     */
    LOW_VOLTAGE(WARNING_TYPE_LOW_VOLTAGE, "低电压报警"),
    /**
     * 急漏气报警
     */
    FAST_LEAK(WARNING_TYPE_FAST_LEAK, "急漏气报警"),
    /**
     * 慢漏气报警
     */
    SLOW_LEAK(WARNING_TYPE_SLOW_LEAK, "慢漏气报警"),
    /**
     * 无信号报警
     */
    NO_SIGNAL(WARNING_TYPE_NO_SIGNAL, "无信号报警"),
    UNKNOWN(-1, "");

    /**
     * 类型
     */
    private final int type;
    private final String description;

    WarningTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public static WarningTypeEnum getByType(int type) {
        for (WarningTypeEnum warningTypeEnum : values()) {
            if (warningTypeEnum.type == type) {
                return warningTypeEnum;
            }
        }
        // 如果未找到匹配的 ID，返回 null 或抛出异常，具体根据需求决定
        return UNKNOWN;
    }
}
