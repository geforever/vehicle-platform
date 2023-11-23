package org.platform.vehicle.web.constant;

/**
 * @Author gejiawei
 * @Date 2023/10/10 11:29
 */
public class WarningConstant {

    /**
     * 是否运行:0-否,1-是
     */
    public static final Integer IS_RUNNING_NO = 0;
    public static final Integer IS_RUNNING_YES = 1;

    /**
     * 告警类型:1-一级高压报警,2-一级高温报警,3-一级低压报警,4-二级高压报警,5-二级高温报警,6-二级低压报警,7-低电压报警,8-急漏气报警,9-慢漏气报警,10-无信号报警
     */
    public static final Integer WARNING_TYPE_FIRST_HIGH_PRESSURE = 1;
    public static final Integer WARNING_TYPE_FIRST_HIGH_TEMPERATURE = 2;
    public static final Integer WARNING_TYPE_FIRST_LOW_PRESSURE = 3;
    public static final Integer WARNING_TYPE_SECOND_HIGH_PRESSURE = 4;
    public static final Integer WARNING_TYPE_SECOND_HIGH_TEMPERATURE = 5;
    public static final Integer WARNING_TYPE_SECOND_LOW_PRESSURE = 6;
    public static final Integer WARNING_TYPE_LOW_VOLTAGE = 7;
    public static final Integer WARNING_TYPE_FAST_LEAK = 8;
    public static final Integer WARNING_TYPE_SLOW_LEAK = 9;
    public static final Integer WARNING_TYPE_NO_SIGNAL = 10;

    /**
     * 告警颜色:1-红:一级高温、一级高压、一级低压报警、急漏气, 1-黄:二级高温、二级高压、二级低压报警、低电压报警, 3-绿:无告警, 4-灰:无信号报警
     */
    public static final Integer WARNING_COLOR_RED = 1;
    public static final Integer WARNING_COLOR_YELLOW = 2;
    public static final Integer WARNING_COLOR_GREEN = 3;
    public static final Integer WARNING_COLOR_GRAY = 4;

    /**
     * 跟进类型:1-紧急类,2-常规类
     */
    public static final Integer FOLLOW_UP_TYPE_URGENT = 1;
    public static final Integer FOLLOW_UP_TYPE_COMMON = 2;

    /**
     * 是否跟进:0-否,1-是
     */
    public static final Integer IS_FOLLOW_UP_NO = 0;
    public static final Integer IS_FOLLOW_UP_YES = 1;

    /**
     * 告警间隙前缀
     */
    public static final String WARNING_INTERVAL_INDEX = "warning_interval";

    /**
     * 告警间隙默认值:60分钟
     */
    public static final String DEFAULT_INTERVAL_VALUE = "60";
}
