package org.platform.vehicle.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.platform.vehicle.constants.DateConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DateUtil {

    /**
     * 定義一年的月份數
     */
    public static final int ONE_YEAR_MONTHS = 0xC;
    /**
     * 定義一星期的天數
     */
    public static final int ONE_WEEK_DAYS = 0x7;
    /**
     * 定義一天的小時數
     */
    public static final int ONE_DAY_HOURS = 0x18;
    /**
     * 定義一小時的分鐘數
     */
    public static final int ONE_HOUR_MONUTES = 0x3C;
    /**
     * 定義一分鐘的秒鐘數
     */
    public static final int ONE_MINUTE_SECONDS = 0x3C;
    /**
     * 定義一秒鐘的毫秒數
     */
    public static final int ONE_SECOND_MILLISECONDS = 0x3E8;
    /**
     * 定義一分鐘的毫秒數
     */
    public static final int ONE_MINUTE_MILLISECONDS =
            DateUtil.ONE_SECOND_MILLISECONDS * DateUtil.ONE_MINUTE_SECONDS;
    /**
     * 定義一小時的毫秒數
     */
    public static final int ONE_HOUR_MILLISECONDS =
            DateUtil.ONE_MINUTE_MILLISECONDS * DateUtil.ONE_HOUR_MONUTES;
    /**
     * 定義一天的毫秒數
     */
    public static final int ONE_DAY_MILLISECONDS =
            DateUtil.ONE_HOUR_MILLISECONDS * DateUtil.ONE_DAY_HOURS;
    /**
     * 標準的日期格式範本
     */
    public static final String STANDARD_DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 簡短的日期格式範本
     */
    public static final String SHORT_DATE_PATTERN = "yyyy.MM.dd";
    /**
     * 完整的日期時間格式範本
     */
    public static final String FULL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 標準的日期時間格式模版
     */
    public static final String STANDARD_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 中文日期格式模版
     */
    public static final String CHINESE_DATE_PATTERN = "yyyy年MM月dd日";
    /**
     * 中文日期時間格式模版
     */
    public static final String CHINESE_DATE_TIME_PATTERN = "yyyy年MM月dd日 HH時mm分ss秒";
    /**
     * 標準時間格式模版
     */
    public static final String STANDARD_TIME_PATTERN = "HH:mm:ss";
    /**
     * 完整時間格式模版
     */
    public static final String FULL_TIME_PATTERN = "HH:mm:ss.SSS";
    /**
     * 中文的標准時間格式模版
     */
    public static final String STANDARD_CHINESE_TIME_PATTERN = "HH時mm分ss秒";
    /**
     * 中文的完整時間格式模版
     */
    public static final String FULL_CHINESE_TIME_PATTERN = "HH時mm分ss秒SSS毫秒";
    /**
     * 下午的英文表示
     */
    public static final String PM = "PM";
    /**
     * 上午的英文表示
     */
    public static final String AM = "AM";
    /**
     * 中文上下午格式範本
     */
    public static final String Chinese_MERIDIAN_PATTERN = "a";
    /**
     * 中文星期格式範本
     */
    public static final String CHINESE_DAY_OF_WEEK_PATTERN = "E";
    /**
     * 時區偏移毫秒數
     */
    public static final long TIME_ZONE_OFFSET = Calendar.getInstance().getTimeZone().getRawOffset();
    /**
     * 日誌記錄工具接口
     */
    private static Logger log = LoggerFactory.getLogger(DateUtil.class);

    private DateUtil() {
    }

    public static Date StringToDate(String dat) {
        SimpleDateFormat press = new SimpleDateFormat(STANDARD_DATE_PATTERN);
        Date date = null;
        try {
            date = press.parse(dat);
        } catch (Exception e) {
            log.error("String 转换 Date 异常", e);
        }
        return date;
    }

    public static String dateToString(Date dat) {
        SimpleDateFormat press = new SimpleDateFormat(STANDARD_DATE_PATTERN);
        String dateString = null;
        try {
            dateString = press.format(dat);
        } catch (Exception e) {
            log.error("Date 转换 String 异常", e);
        }
        return dateString;
    }

    public static Date getDate() {
        Calendar canlendar = Calendar.getInstance();
        return canlendar.getTime();
    }

    public static Date getDate(int iYear, int iMonth, int iDate, int iHour, int iMinute,
            int iSecond) {
        Calendar canlendar = Calendar.getInstance();
        canlendar.clear();
        canlendar.set(iYear, iMonth - 1, iDate, iHour, iMinute, iSecond);
        return canlendar.getTime();
    }

    public static Date getDate(int iYear, int iMonth, int iDate, int iHour, int iMinute) {
        return DateUtil.getDate(iYear, iMonth, iDate, iHour, iMinute, 0);
    }

    public static Date getDate(int iYear, int iMonth, int iDate, int iHour) {
        return DateUtil.getDate(iYear, iMonth, iDate, iHour, 0, 0);
    }

    public static Date getDate(int iYear, int iMonth, int iDate) {
        return DateUtil.getDate(iYear, iMonth, iDate, 0, 0, 0);
    }

    public static Date getDate(int iYear, int iMonth) {
        return DateUtil.getDate(iYear, iMonth, 1, 0, 0, 0);
    }

    public static Date getDate(int iYear) {
        return DateUtil.getDate(iYear, 1, 1, 0, 0, 0);
    }

    public static Date getDate(String sYear) {
        int iYear = DateUtil.getRightNumber(sYear);
        return DateUtil.getDate(iYear);
    }

    public static Date getDate(String sYear, String sMonth) {
        int iYear = DateUtil.getRightNumber(sYear);
        int iMonth = DateUtil.getRightNumber(sMonth);
        return DateUtil.getDate(iYear, iMonth);
    }

    public static Date getDate(String sYear, String sMonth, String sDate) {
        int iYear = DateUtil.getRightNumber(sYear);
        int iMonth = DateUtil.getRightNumber(sMonth);
        int iDate = DateUtil.getRightNumber(sDate);
        return DateUtil.getDate(iYear, iMonth, iDate);
    }

    public static Date getDate(String sYear, String sMonth, String sDate, String sHour) {
        int iYear = DateUtil.getRightNumber(sYear);
        int iMonth = DateUtil.getRightNumber(sMonth);
        int iDate = DateUtil.getRightNumber(sDate);
        int iHour = DateUtil.getRightNumber(sHour);
        return DateUtil.getDate(iYear, iMonth, iDate, iHour);
    }

    public static Date getDate(String sYear, String sMonth, String sDate, String sHour,
            String sMinute) {
        int iYear = DateUtil.getRightNumber(sYear);
        int iMonth = DateUtil.getRightNumber(sMonth);
        int iDate = DateUtil.getRightNumber(sDate);
        int iHour = DateUtil.getRightNumber(sHour);
        int iMinute = DateUtil.getRightNumber(sMinute);
        return DateUtil.getDate(iYear, iMonth, iDate, iHour, iMinute);
    }

    public static Date getDate(String sYear, String sMonth, String sDate, String sHour,
            String sMinute,
            String sSecond) {
        int iYear = DateUtil.getRightNumber(sYear);
        int iMonth = DateUtil.getRightNumber(sMonth);
        int iDate = DateUtil.getRightNumber(sDate);
        int iHour = DateUtil.getRightNumber(sHour);
        int iMinute = DateUtil.getRightNumber(sMinute);
        int iSecond = DateUtil.getRightNumber(sSecond);
        return DateUtil.getDate(iYear, iMonth, iDate, iHour, iMinute, iSecond);
    }

    private static int getRightNumber(String sNumber) {
        try {
            return Integer.parseInt(sNumber);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean isMax(Date date0, Date date1) {
        if (date0 == null || date1 == null) {
            return false;
        }
        return date0.getTime() > date1.getTime();

    }

    public static Date Max(Date date0, Date date1) {
        if (date0 != null && date1 != null) {
            if (date0.getTime() > date1.getTime()) {
                return date0;
            }
            return date1;
        } else if (date0 != null && date1 == null) {
            return date0;
        } else if (date0 == null && date1 != null) {
            return date1;
        } else {
            return null;
        }
    }

    public static Date Min(Date date0, Date date1) {
        if (date0 != null && date1 != null) {
            if (date0.getTime() < date1.getTime()) {
                return date0;
            }
            return date1;
        }
        return null;
    }

    public static long getMMDif(Date date0, Date date1) {
        if (date0 == null || date1 == null) {
            return 0;
        }
        return date0.getTime() - date1.getTime();
    }

    public static long getSeDif(Date date0, Date date1) {
        return DateUtil.getMMDif(date0, date1) / 1000;
    }

    public static long getMiDif(Date date0, Date date1) {
        return DateUtil.getSeDif(date0, date1) / 60;
    }

    public static int getHoDif(Date date0, Date date1) {
        return (int) DateUtil.getMiDif(date0, date1) / 60;
    }

    public static int getDaDif(Date date0, Date date1) {
        return DateUtil.getHoDif(date0, date1) / 24;
    }

    private static Date addDate(Date date, int iArg0, int iDate) {
        Calendar canlendar = Calendar.getInstance();
        canlendar.setTime(date);
        canlendar.add(iArg0, iDate);
        return canlendar.getTime();
    }

    public static Date addSe(Date date, int iSecond) {
        return addDate(date, Calendar.SECOND, iSecond);
    }

    public static Date addMi(Date date, int iMinute) {
        return addDate(date, Calendar.MINUTE, iMinute);
    }

    public static Date addHo(Date date, int iHour) {
        return addDate(date, Calendar.HOUR, iHour);
    }

    public static Date addDa(Date date, int iDate) {
        return addDate(date, Calendar.DAY_OF_MONTH, iDate);
    }

    public static Date addMo(Date date, int iMonth) {
        return addDate(date, Calendar.MONTH, iMonth);
    }

    public static Date addYe(Date date, int iYear) {
        return addDate(date, Calendar.YEAR, iYear);
    }

    public static Date addWe(Date date, int iWeek) {
        return addDate(date, Calendar.WEEK_OF_YEAR, iWeek);
    }

    public static Date addSe(Date date, String sSecond) {
        return addSe(date, getRightNumber(sSecond));
    }

    public static Date addMi(Date date, String sMinute) {
        return addMi(date, getRightNumber(sMinute));
    }

    public static Date addHo(Date date, String sHour) {
        return addHo(date, getRightNumber(sHour));
    }

    public static Date addDa(Date date, String sDate) {
        return addDa(date, getRightNumber(sDate));
    }

    public static Date addMo(Date date, String sMonth) {
        return addMo(date, getRightNumber(sMonth));
    }

    public static Date addYe(Date date, String sYear) {
        return addYe(date, getRightNumber(sYear));
    }

    public static Date addWe(Date date, String sWeek) {
        return addWe(date, getRightNumber(sWeek));
    }

    public static Date parseDate(String sDate, String formate) {
        SimpleDateFormat simpleDateFormate = new SimpleDateFormat(formate);
        try {
            return simpleDateFormate.parse(sDate);
        } catch (ParseException e) {
            return null;
        }
    }

    // ============通过传入date按一定格式转为日期字符串===========开始====//

    public static Date parseDateFullYear(String sDate) {
        SimpleDateFormat simpleDateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormate.parse(sDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToStr(Date date, String pattern) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.format(date);
        } catch (Exception e) {
            // ------
        }
        return null;
    }

    public static String get4yMdHmsS(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN_YMDhmsS);
    }

    public static String get4yMdHms(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN);
    }

    public static String get4yMdH(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN_YMDh);
    }

    public static String get4yMdHm(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN_YMDhm);
    }

    public static String get4yMd(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN_YMD);
    }

    public static String get4yM(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN_YM);
    }

    public static String get4y(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN_Y);
    }

    public static String getMd(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN_MD);
    }

    public static String getLine4Ymd(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN_YMD_LINE);
    }

    public static String get4yMdHmsSNoSplit(Date date) {
        return dateToStr(date, DateConstant.DATE_PATTERN_YMDhmsS_NO);
    }

    // --------------------结束-------------------------//

    public static String dateToStr(Date date, int type) {
        switch (type) {
            case DateConstant.DEFAULT:
                return getLine4Ymd(date);
            case DateConstant.YM:
                return dateToStr(date, "yyyy/MM");
            case DateConstant.NO_SLASH:
                return dateToStr(date, "yyyyMMdd");
            case DateConstant.YMR_SLASH:
                return dateToStr(date, "yyyy-MM-dd");
            case DateConstant.YM_NO_SLASH:
                return dateToStr(date, "yyyyMM");
            case DateConstant.DATE_TIME:
                return dateToStr(date, DateConstant.DATE_PATTERN_LINE);
            case DateConstant.DATE_TIME_NO_SLASH:
                return dateToStr(date, "yyyyMMddHHmmss");
            case DateConstant.DATE_HM:
                return dateToStr(date, "yyyy/MM/dd HH:mm");
            case DateConstant.TIME:
                return dateToStr(date, "HH:mm:ss");
            case DateConstant.HM:
                return dateToStr(date, "HH:mm");
            case DateConstant.LONG_TIME:
                return dateToStr(date, "HHmmss");
            case DateConstant.SHORT_TIME:
                return dateToStr(date, "HHmm");
            case DateConstant.DATE_TIME_LINE:
                return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
            case DateConstant.DATE_TIME_NO_SECOND:
                return dateToStr(date, "yyyyMMddHH");
            default:
                throw new IllegalArgumentException("Type undefined : " + type);
        }
    }

    public static int getPartOfTime(Date date, String part) {
        Calendar canlendar = Calendar.getInstance();
        canlendar.clear();
        canlendar.setTime(date);
        if ("year".equals(part)) {
            return canlendar.get(Calendar.YEAR);
        }
        if ("month".equals(part)) {
            return canlendar.get(Calendar.MONTH) + 1;
        }
        if ("date".equals(part)) {
            return canlendar.get(Calendar.DAY_OF_MONTH);
        }
        if ("hour".equals(part)) {
            return canlendar.get(Calendar.HOUR_OF_DAY);
        }
        if ("minute".equals(part)) {
            return canlendar.get(Calendar.MINUTE);
        }
        if ("second".equals(part)) {
            return canlendar.get(Calendar.SECOND);
        }
        if ("milliSecond".equals(part)) {
            return canlendar.get(Calendar.MILLISECOND);
        }
        return -1;
    }

    /**
     * 计算当前日期是星期几(星期日为0)
     *
     * @param strDate
     * @return
     */
    public static int getWeekDay(Date strDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(strDate);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 日期增加天数
     */
    public static Date addDay(Date date, int iDate) {
        return addDate(date, Calendar.DAY_OF_MONTH, iDate);
    }

    public static Date getEndDate(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static Date getStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 判断两个日期是否在同一个日期内
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setFirstDayOfWeek(Calendar.MONDAY);// 西方周日为一周的第一天，咱得将周一设为一周第一天
        cal2.setFirstDayOfWeek(Calendar.MONDAY);
        cal1.setTime(d1);
        cal2.setTime(d2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (subYear == 0)// subYear==0,说明是同一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        } else if (subYear == 1 && cal2.get(Calendar.MONTH)
                == 11) // subYear==1,说明cal比cal2大一年;java的一月用"0"标识，那么12月用"11"
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        } else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11)// subYear==-1,说明cal比cal2小一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断日期为周几
     *
     * @param pTime
     * @return
     */
    public static Integer dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return Integer.valueOf(dayForWeek);
    }

    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * "yyyy-MM-dd"时间格式转换成Calendar格式
     *
     * @param str
     * @return Calendar
     * @Title: strDateTimeToCalendarTo
     * @author : frank_zeng
     */
    public static Calendar strDateTimeToCalendarTo(final String str, final String style) {
        final Calendar calendar = Calendar.getInstance();
        final Date date = strDateTimeToDateTo(str, style);
        if (date == null) {
            return null;
        }
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 根据传入日期格式和字符串返回格式化对应日期
     *
     * @param str
     * @param style
     * @return Date
     * @Title: strDateTimeToDateTo
     * @author : frank_zeng
     */
    public static Date strDateTimeToDateTo(final String str, final String style) {
        if (str == null || str.trim().length() == 0) {
            return null;
        }

        return new SimpleDateFormat(style, Locale.CHINA).parse(
                str.length() <= 10 ? str + " 00:00:00" : str,
                new ParsePosition(0));
    }

    /**
     * 比较两个时间字符串；
     *
     * @param srcdate 要比较的源字符串；
     * @param tagdate 要比较的对象字符串；
     * @return 大于返回 1，等于返回0，小于返回-1，参数错误返回-2；
     */
    public static int compareTwoDate(final String srcdate, final String tagdate) {
        final Calendar scalendar = strDateTimeToCalendarTo(srcdate,
                DateConstant.DATE_PATTERN_YMDhm);
        final Calendar tcalendar = strDateTimeToCalendarTo(tagdate,
                DateConstant.DATE_PATTERN_YMDhm);
        if (scalendar == null || tcalendar == null) {
            return -2;
        }
        return scalendar.getTime().compareTo(tcalendar.getTime());
    }

    /**
     * 根据传入日期格式进行日期格式化比较
     *
     * @param srcdate
     * @param tagdate
     * @param style
     * @return int
     * @Title: compareTwoDateStyle
     * @author : frank_zeng
     */
    public static int compareTwoDateStyle(final String srcdate, final String tagdate,
            final String style) {
        final Calendar scalendar = strDateTimeToCalendarTo(srcdate, style);
        final Calendar tcalendar = strDateTimeToCalendarTo(tagdate, style);
        if (scalendar == null || tcalendar == null) {
            return -2;
        }
        return scalendar.getTime().compareTo(tcalendar.getTime());
    }

    /**
     * 获取某个时间点到未来第n天第24点的时间差(秒)
     *
     * @param date
     * @param afterDays
     * @return
     */
    public static int dateSubtract(Date date, int afterDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        System.out.println(cal.getTime());
        cal.add(Calendar.DAY_OF_YEAR, afterDays);
        return (int) ((cal.getTime().getTime() - date.getTime()) / 1000);
    }

    /**
     * 获取当月开始的日期
     *
     * @param date 某一天
     * @return 某一天所在月的第一天
     */
    public static Date getBeginMonthDate(Date date) {
        Calendar calendar = new Calendar.Builder().setInstant(date).build();
        Calendar begin = new Calendar.Builder()
                .setFields(Calendar.YEAR, calendar.get(Calendar.YEAR), Calendar.MONTH,
                        calendar.get(Calendar.MONTH))
                .build();
        return begin.getTime();
    }

    /**
     * 获取当月最后一天的日期
     */
    public static Date getLastMonthDate(Date date) {
        Calendar calendar = new Calendar.Builder().setInstant(date).build();
        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        end.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        end.set(Calendar.DAY_OF_MONTH, 1);
        end.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        return end.getTime();
    }

    public static long floorTime(Date date) {
        long currentTime = date.getTime();
        return currentTime - TIME_ZONE_OFFSET - (currentTime % DateUtil.ONE_DAY_MILLISECONDS);
    }

    public static long ceilTime(Date date) {
        long currentTime = date.getTime();
        return currentTime - TIME_ZONE_OFFSET - (currentTime % DateUtil.ONE_DAY_MILLISECONDS)
                + DateUtil.ONE_DAY_MILLISECONDS - 1;
    }

    public static Date floor(Date date) {
        return new Date(floorTime(date));
    }

    public static Date ceil(Date date) {
        return new Date(ceilTime(date));
    }

    public static Date strToDate(String dat) {
        SimpleDateFormat press = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = press.parse(dat);
        } catch (Exception e) {
            log.error("String 转换 Date 异常", e);
        }
        return date;
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return floor(cal.getTime());
    }

    public static List<String> getDateBetween(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = new ArrayList<>(); // 保存日期集合
        // try {
        // Date date_start = sdf.parse(start);
        // Date date_end = sdf.parse(end);
        Date date = start;
        Calendar cd = Calendar.getInstance();// 用Calendar 进行日期比较判断
        while (date.getTime() <= end.getTime()) {
            list.add(sdf.format(date));
            cd.setTime(date);
            cd.add(Calendar.DATE, 1);// 增加一天 放入集合
            date = cd.getTime();
        }
        // }
        // catch (ParseException e) {
        // log.error("getDateBetween error", e);
        // }
        return list;
    }

    public static void main(String[] args) {
        int x = DateUtil.dateSubtract(new Date(), 1);
        System.out.println(x);
    }

}
