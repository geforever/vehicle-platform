package org.platform.vehicle.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 时间工具类
 */
public class DateUtils {

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static Date strToDate(String str) {
        return strToDate(str, null);
    }

    public static Date strToDate(String str, String format) {
        try {
            return strToDateThrow(str, format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date strToDateThrow(String str, String format) throws ParseException {
        if (null == str || "".equals(str)) return null;
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "".equals(format) || "Datetime".equals(format)) {
            format = DATETIME_FORMAT;
        } else if ("Timestamp".equals(format)) {
            format = TIMESTAMP_FORMAT;
        } else if ("Date".equals(format)) {
            format = DATE_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(str);
    }

    /**
     * 日期转换为字符串
     */
    public static String dateToStr(Date date) {
        return dateToStr(date, null);
    }

    public static String dateToStr(Date date, String format) {
        if (null == date) {
            return null;
        }
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == format || "".equals(format) || "Datetime".equals(format)) {
            format = DATETIME_FORMAT;
        } else if ("Timestamp".equals(format)) {
            format = TIMESTAMP_FORMAT;
        } else if ("Date".equals(format)) {
            format = DATE_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取当前对应格式的日期
     */
    public static String getDateStr() {
        return getDateStr(null);
    }

    public static String getDateStr(String format) {
        if (null == format || "".equals(format)) {
            format = "yyyyMMddHHmmssSSS";
        }
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 计算两个日期之间相差的秒数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差秒数
     */
    public static int getSecsBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 1000;

        return Math.abs(Integer.parseInt(String.valueOf(between_days)));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int getDaysBetween(Date smdate, Date bdate) {
        int second = getSecsBetween(smdate, bdate);
        long between_days = second / 3600 / 24;

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取该天是星期几,星期一时返回1，其他依次类推
     *
     * @param date
     * @return
     */
    public static int getIsoDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);  //是周几
        return getIsoDayOfWeek(weekDay);
    }

    public static String getWeekNameWithIsoDayOfWeek(int isoDayOfWeek) {
        int index = isoDayOfWeek;
        if (isoDayOfWeek == 7) index = 0;
        return getWeekName(index);
    }

    /**
     * 获取该天是星期几,星期一时返回1，其他依次类推
     *
     * @param dayOfWeek
     * @return
     */
    public static int getIsoDayOfWeek(int dayOfWeek) {
        int result;
        if (dayOfWeek == 1) {
            result = 7;
        } else {
            result = dayOfWeek - 1;
        }
        return result;
    }

    public static String getTodayWeek() {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weeks[w];
    }

    /**
     * 获取星期
     */
    public static String getWeekName(Calendar calendar) {
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return getWeekName(index);
    }

    /**
     * 获取星期
     */
    public static String getWeekName(int week) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        if (week < 0) week = 0;
        return weeks[week];
    }

    /**
     * 根据日期获取星期
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];
    }

    /**
     * 根据星期获取下标
     */
    public static int getWeekIndex(String week) {
        String[] weekDays = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
        return Arrays.asList(weekDays).indexOf(week);
    }

    /**
     * 根据下标获取星期
     */
    public static String getWeekIndex(Integer index) {
        String[] weekDays = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
        return weekDays[index];
    }

    /**
     * 获取字符串日期
     */
    public static String getCalendarStr(Calendar cal) {
        int year = cal.get(Calendar.YEAR);//获取年份
        int month = cal.get(Calendar.MONTH) + 1;//获取月份
        String monthStr = month < 10 ? "0" + month : month + "";
        int day = cal.get(Calendar.DATE);//获取日
        String dayStr = day < 10 ? "0" + day : day + "";
        return year + "-" + monthStr + "-" + dayStr;
    }

    public static Date formatter(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date formatterToMongodb(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, -8);
        return c.getTime();
    }

    public static String toDouble(int x) {
        if (x < 10) return "0" + x;
        else return x + "";
    }

    /**
     * 获取一周的date
     */
    public static List<Date> dateToWeek(Date mdate) {
        int b = mdate.getDay();
        Date fdate;
        List<Date> list = new ArrayList<>();
        long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a - 1, fdate);
        }
        return list;
    }

    /**
     * 获取前几天的date
     */
    public static List<Date> getDateListBefore(Date date, Integer num) {
        if (date == null) return null;
        List<Date> result = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -num);
        for (int a = 1; a <= num; a++) {
            c.add(Calendar.DATE, 1);
            Date temp = c.getTime();
            result.add(temp);
        }
        return result;
    }

    /**
     * 获取第一天的date
     */
    public static List<Date> getDateListBefore(Date date, String kind) {
        if (date == null || kind == null) return null;

        Calendar end = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if ("week".equals(kind)) {
            c.add(Calendar.DATE, c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK));
            end.setTime(c.getTime());
            end.add(Calendar.DATE, 7);
        } else if ("month".equals(kind)) {
            c.set(Calendar.DAY_OF_MONTH, 1);
            end.setTime(c.getTime());
            end.add(Calendar.MONTH, 1);
        } else if ("half".equals(kind)) {
            c.add(Calendar.MONTH, -6);
            c.set(Calendar.DAY_OF_MONTH, 1);
            end.setTime(date);
            end.set(Calendar.DAY_OF_MONTH, 1);
            end.add(Calendar.MONTH, 1);
            end.add(Calendar.DAY_OF_MONTH, -1);
        } else if ("year".equals(kind)) {
            c.set(Calendar.DAY_OF_YEAR, 1);
            end.setTime(c.getTime());
            end.add(Calendar.YEAR, 1);
        } else return null;

        List<Date> result = new ArrayList<>();
        while (c.before(end)) {
            result.add(c.getTime());
            c.add(Calendar.DATE, 1);
        }
        return result;
    }

    /**
     * 获取一周、月、年的date
     */
    public static List<Date> getDateListByKind(Date date, String kind) {
        if (date == null || kind == null) return null;

        Calendar end = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if ("week".equals(kind)) {
            c.add(Calendar.DATE, c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK));
            end.setTime(c.getTime());
            end.add(Calendar.DATE, 7);
        } else if ("month".equals(kind)) {
            c.set(Calendar.DAY_OF_MONTH, 1);
            end.setTime(c.getTime());
            end.add(Calendar.MONTH, 1);
        } else if ("half".equals(kind)) {
            c.add(Calendar.MONTH, -6);
            c.set(Calendar.DAY_OF_MONTH, 1);
            end.setTime(date);
            end.set(Calendar.DAY_OF_MONTH, 1);
            end.add(Calendar.MONTH, 1);
            end.add(Calendar.DAY_OF_MONTH, -1);
        } else if ("year".equals(kind)) {
            c.set(Calendar.DAY_OF_YEAR, 1);
            end.setTime(c.getTime());
            end.add(Calendar.YEAR, 1);
        } else return null;

        List<Date> result = new ArrayList<>();
        while (c.before(end)) {
            result.add(c.getTime());
            c.add(Calendar.DATE, 1);
        }
        return result;
    }

    /**
     * 填充时分秒
     */
    public static Date fitTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //将时分秒填充满
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 清除时分秒
     */
    public static Date clearTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 清除秒
     */
    public static Date clearSec(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 根据日期判断是上午下午还是晚上
     */
    public static Integer getTimeByDate(Date date) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour <= 12) return 1;
        else if (hour <= 18) return 2;
        else return 3;
    }

    /**
     * 获取前一秒
     */
    public static Date getLastSec(Date date) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, -1);
        return c.getTime();
    }

    /**
     * 获取后一天
     */
    public static Date getNextDate(Date date) {
        return addDay(date, 1);
    }

    /**
     * 添加天
     */
    public static Date addDay(Date date, Integer day) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

    /**
     * 获取当前时间的哪一天
     *
     * @param date
     * @param val
     * @return
     */
    public static Date getWhichDate(Date date, int val) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, val);
        return c.getTime();
    }

    //获取一个月的开始和结束时间
    public static List<Date> dateToMonth(Date date) {
        List<Date> result = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号
        result.add(c.getTime());

        c.add(Calendar.MONTH, 1);//加一个月
        c.add(Calendar.MILLISECOND, -1);//减一毫秒
        result.add(c.getTime());

        return result;
    }

    /**
     * 获取当前date的所在周的周一
     *
     * @param date
     * @return
     */
    public static Date getDateWeekMondayStart(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(calendar.DAY_OF_WEEK, calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    //获取月初
    public static Date getMonthFirstDay(Date date, int val) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, val);
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    //获取月末
    public static Date getMonthLastDay(Date date, int val) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1 + val);
        c.set(Calendar.DAY_OF_MONTH, 0);
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


    /**
     * 该天是否为月末最后一天
     *
     * @param date
     * @return
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取该月有多少天
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将时分秒设置成当前时间
     */
    public static Date setTimeToNow(Date date) {
        return setTime(date, new Date());
    }

    /**
     * 将时分秒设置成新时间
     */
    public static Date setTime(Date date, Date newDate) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendar now = Calendar.getInstance();
        now.setTime(newDate);
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
        c.set(Calendar.SECOND, now.get(Calendar.SECOND));
        c.set(Calendar.MILLISECOND, now.get(Calendar.MILLISECOND));
        return c.getTime();
    }

    /**
     * 获取今天0点-24点日期
     */
    public static List<Date> getToday() {
        List<Date> result = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        result.add(c.getTime());

        c.add(Calendar.DAY_OF_YEAR, 1);
        c.add(Calendar.SECOND, -1);
        result.add(c.getTime());
        return result;
    }

    public static String getDateDiff(Date startDate, Date endDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        //long ns = 1000;
        //获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        //计算差多少天
        long day = diff / nd;
        //计算差多少小时
        long hour = diff % nd / nh;
        //计算差多少分钟
        long min = diff % nd % nh / nm;
        //计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        String result = "";
        if (day > 0) result += day + "天";
        if (hour > 0) result += hour + "小时";
        if (min > 0) result += min + "分钟";
        if (result.isEmpty()) result = sec + "秒";
        return result;
    }

    public static BigDecimal getDateDiffHour(Date startDate, Date endDate) {
        BigDecimal nh = new BigDecimal(1000 * 60 * 60);
        BigDecimal diff = new BigDecimal(endDate.getTime() - startDate.getTime());
        return diff.divide(nh, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static Date anyStrToDate(String time) {
        SimpleDateFormat formatter;
        int tempPos = time.indexOf("AD");
        time = time.trim();
        formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
        if (tempPos > -1) {
            time = time.substring(0, tempPos) +
                    "公元" + time.substring(tempPos + "AD".length());//china
            formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
        }
        tempPos = time.indexOf("-");
        if (tempPos > -1 && (!time.contains(" "))) {
            formatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
        } else if ((time.contains("/")) && (time.contains(" "))) {
            formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        } else if ((time.contains("-")) && (time.contains(" "))) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if ((time.contains("/")) && (time.contains("am")) || (time.contains("pm"))) {
            formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
        } else if ((time.contains("-")) && (time.contains("am")) || (time.contains("pm"))) {
            formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
        }
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(time, pos);
    }

    public static int getHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @Description: 当前日期的天数加或减xx天
     * @param:
     * @return:
     * @auther: sk
     * @date: 2018/11/22 9:51
     */
    public static List<Date> getOrderManyDate(Integer day) {
        if (day == null) {
            day = 1;
        }
        List<Date> result = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, day);
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        result.add(c.getTime());

        c.add(Calendar.DAY_OF_MONTH, -day);
        c.add(Calendar.DAY_OF_YEAR, 1);
        c.add(Calendar.SECOND, -1);
        result.add(c.getTime());
        return result;
    }

    //前第n天的0:00:00-23:59:59
    public static List<Date> getBeforeToday(Integer day) {
        List<Date> result = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, day);
        //将时分秒,毫秒域清零
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        result.add(c.getTime());

        c.add(Calendar.DAY_OF_YEAR, 1);
        c.add(Calendar.SECOND, -1);
        result.add(c.getTime());
        return result;
    }

    public static Date getPayDay(Date date) {
        String dateStr = dateToStr(date);
        String day = dateStr.substring(8, 10);
        int days = Integer.parseInt(day);
        if (days >= 25) {
            return DateUtils.getMonthFirstDay(new Date(), 0);
        } else {
            //上个月月初
            return DateUtils.getMonthFirstDay(new Date(), -1);
        }
    }

    public static String getDay(Date date) {
        String dateStr = dateToStr(date);
        return dateStr.substring(8, 10);
    }

    public static String getMonth(Date date) {
        String dateStr = dateToStr(date);
        return dateStr.substring(5, 7);
    }

    public static Date getDate() {
        Calendar canlendar = Calendar.getInstance();
        return canlendar.getTime();
    }

    public static void main(String[] args) {

        //System.out.println(DateUtils.dateToStr(getPayDay(new Date())));
        //System.out.println(compareSecsBetween(DateUtils.getDate(),new Date()));
        //System.out.println(compareSecsBetween(DateUtils.strToDate("2021-01-12 14:37:00"),new Date()));

        String str = "/api/user/login/JX!666/admin";

        System.out.println(str.lastIndexOf("/login/JX!666/admin"));

        List<Date> dateList = getDateListBefore(new Date(), 15);
        List<String> dateStr = dateList.stream().map(e -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(e);
            return DateUtils.getCalendarStr(cal);
        }).collect(Collectors.toList());


//        getDateListBefore(new Date(), 15);

        System.out.println(dateStr);
    }

    public static String getYear(Date date) {
        String dateStr = dateToStr(date);
        return dateStr.substring(0, 4);
    }

    /**
     * 计算两个日期之间相差的秒数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差秒数
     */
    public static Long compareSecsBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / 1000;

        return between_days;
    }

}
