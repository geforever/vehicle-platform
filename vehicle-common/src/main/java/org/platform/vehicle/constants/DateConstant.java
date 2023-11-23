package org.platform.vehicle.constants;

/**
 * 日期常量类
 *
 * ClassName: DateConstant <br/>
 * Function: ADD FUNCTION. <br/>
 * Reason: ADD REASON(可选). <br/>
 * date:  <br/>
 *
 * @author zengxinyan
 * @version
 * @since JDK 1.8
 */
public final class DateConstant {


	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN_YMD = "yyyy-MM-dd";
	public static final String DATE_PATTERN_YMDhmsS ="yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATE_PATTERN_YMDh="yyyy-MM-dd HH";
	public static final String DATE_PATTERN_YMDhm="yyyy-MM-dd HH:mm";
	public static final String DATE_PATTERN_YM="yyyy-MM";
	public static final String DATE_PATTERN_Y="yyyy";
	public static final String DATE_PATTERN_MD="MM-dd";

	public static final String DATE_PATTERN_YMD_LINE ="yyyy/MM/dd";
	public static final String DATE_PATTERN_LINE="yyyy/MM/dd HH:mm:ss";

    public static final String DATE_PATTERN_YMDhmsS_NO ="yyyyMMddHHmmssSSS";
    public static final String DATE_PATTERN_NO = "yyyyMMddHHmmss";


    //-----------------------------------------------

	/**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd
     */
	public static final int DEFAULT = 0;

	/**
	 * yyyy/MM
	 */
	public static final int YM = 1;

    /**
     * 变量：日期格式化类型 - 格式:yyyy-MM-dd
     *
     */
    public static final int YMR_SLASH = 11;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMMdd
     *
     */
    public static final int NO_SLASH = 2;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMM
     *
     */
    public static final int YM_NO_SLASH = 3;

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm:ss
     *
     */
    public static final int DATE_TIME = 4;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMMddHHmmss
     *
     */
    public static final int DATE_TIME_NO_SLASH = 5;

    /**
     * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm
     *
     */
    public static final int DATE_HM = 6;

    /**
     * 变量：日期格式化类型 - 格式:HH:mm:ss
     *
     */
    public static final int TIME = 7;

    /**
     * 变量：日期格式化类型 - 格式:HH:mm
     *
     */
    public static final int HM = 8;

    /**
     * 变量：日期格式化类型 - 格式:HHmmss
     *
     */
    public static final int LONG_TIME = 9;

    /**
     * 变量：日期格式化类型 - 格式:HHmm
     *
     */
    public static final int SHORT_TIME = 10;

    /**
     * 变量：日期格式化类型 - 格式:yyyy-MM-dd HH:mm:ss
     */
    public static final int DATE_TIME_LINE = 12;

    /**
     * 变量：日期格式化类型 - 格式:yyyyMMddHH
     *
     */
    public static final int DATE_TIME_NO_SECOND = 13;

}
