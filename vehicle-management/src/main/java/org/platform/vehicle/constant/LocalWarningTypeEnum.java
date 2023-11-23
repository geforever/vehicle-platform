package org.platform.vehicle.constant;

import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_DANGER_WARNING;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_FATIGUE_DRIVING;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_GNSS_ANTENNA_DISCONNECTED;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_GNSS_ANTENNA_SHORT_CIRCUIT;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_GNSS_MODULE_FAILURE;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_MAIN_POWER_OFF;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_MAIN_POWER_UNDER_VOLTAGE;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_OVER_SPEED;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_URGENT;

/**
 * @Author gejiawei
 * @Date 2023/10/30 15:34
 */
public enum LocalWarningTypeEnum {

    /**
     * 1-紧急报警
     */
    URGENT(WARNING_TYPE_URGENT, "紧急报警"),

    /**
     * 2-超速报警
     */
    OVER_SPEED(WARNING_TYPE_OVER_SPEED, "超速报警"),

    /**
     * 3-疲劳驾驶
     */
    FATIGUE_DRIVING(WARNING_TYPE_FATIGUE_DRIVING, "疲劳驾驶"),

    /**
     * 4-危险预警
     */
    DANGER_WARNING(WARNING_TYPE_DANGER_WARNING, "危险预警"),

    /**
     * 5-GNSS模块发生鼓掌
     */
    GNSS_MODULE_FAILURE(WARNING_TYPE_GNSS_MODULE_FAILURE, "GNSS模块发生鼓掌"),

    /**
     * c 6-GNSS天线未接或者被剪断
     */
    GNSS_ANTENNA_DISCONNECTED(WARNING_TYPE_GNSS_ANTENNA_DISCONNECTED, "GNSS天线未接或者被剪断"),

    /*c*
     * 7-GNSS天线短路
     */
    GNSS_ANTENNA_SHORT_CIRCUIT(WARNING_TYPE_GNSS_ANTENNA_SHORT_CIRCUIT, "GNSS天线短路"),

    /**
     * 8-终端主电源欠压
     */
    MAIN_POWER_UNDER_VOLTAGE(WARNING_TYPE_MAIN_POWER_UNDER_VOLTAGE, "终端主电源欠压"),

    /**
     * 9-终端主电源掉电
     */
    MAIN_POWER_OFF(WARNING_TYPE_MAIN_POWER_OFF, "终端主电源掉电"),

    UNKNOWN(-1, "");

    /**
     * 类型
     */
    private final int type;
    private final String description;

    LocalWarningTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public static LocalWarningTypeEnum getByType(int type) {
        for (LocalWarningTypeEnum warningTypeEnum : values()) {
            if (warningTypeEnum.type == type) {
                return warningTypeEnum;
            }
        }
        // 如果未找到匹配的 ID，返回 null 或抛出异常，具体根据需求决定
        return UNKNOWN;
    }

}
