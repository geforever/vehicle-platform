package org.platform.vehicle.constant;

/**
 * @Author gejiawei
 * @Date 2023/10/30 15:27
 */
public class LocalWarningConstant {

    /**
     * 车辆状态: 1-行驶中,2-已停车,3-已断电
     */
    public static final int VEHICLE_STATUS_RUNNING = 1;
    public static final int VEHICLE_STATUS_PARKING = 2;
    public static final int VEHICLE_STATUS_POWER_OFF = 3;

    /**
     * 1-紧急报警,2-超速报警,3-疲劳驾驶,4-危险预警,5-GNSS模块发生鼓掌,6-GNSS天线未接或者被剪断,7-GNSS天线短路,8-终端主电源欠压,9-终端主电源掉电
     */
    public static final int WARNING_TYPE_URGENT = 1;
    public static final int WARNING_TYPE_OVER_SPEED = 2;
    public static final int WARNING_TYPE_FATIGUE_DRIVING = 3;
    public static final int WARNING_TYPE_DANGER_WARNING = 4;
    public static final int WARNING_TYPE_GNSS_MODULE_FAILURE = 5;
    public static final int WARNING_TYPE_GNSS_ANTENNA_DISCONNECTED = 6;
    public static final int WARNING_TYPE_GNSS_ANTENNA_SHORT_CIRCUIT = 7;
    public static final int WARNING_TYPE_MAIN_POWER_UNDER_VOLTAGE = 8;
    public static final int WARNING_TYPE_MAIN_POWER_OFF = 9;

}
