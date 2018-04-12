package com.hzgc.common.util.date;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>创建时间：2017-5-20 11:37</p>
 *
 * @author 娄存银
 * @version 1.0
 */
public class DateUtils {
    /**
     * TIME_PATTERN
     */
    private static final String TIME_PATTERN = "HH:mm:ss";
    /**
     * DATE_PATTERN
     */
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * MONTH_PATTERN
     */
    private static final String MONTH_PATTERN = "yyyy-MM";
    /**
     * DATE_TIME_PATTERN
     */
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String COLON = ":";

    /**
     * 获取当天的 00:00:00
     *
     * @param date 日期
     * @return 开始时间
     */
    public static Date getDayStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dayStart(calendar);
        return calendar.getTime();
    }

    /**
     * 获取昨天的 00:00:00
     *
     * @param date 日期
     * @return 开始时间
     */
    public static Date getBeforeDayStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        dayStart(calendar);
        return calendar.getTime();
    }

    /**
     * 获取明天的 00:00:00
     *
     * @param date 日期
     * @return 开始时间
     */
    public static Date getNextDayStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        dayStart(calendar);
        return calendar.getTime();
    }

    /**
     * 获取这周一 00:00:00
     *
     * @param date 日期
     * @return 开始时间
     */
    public static Date getWeekStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        weekStart(calendar);
        return calendar.getTime();
    }

    /**
     * 获取下周一 00:00:00
     *
     * @param date 日期
     * @return 开始时间
     */
    public static Date getNextWeekStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        weekStart(calendar);
        return calendar.getTime();
    }

    /**
     * 获取本月 1 号 00:00:00
     *
     * @param date 日期
     * @return 开始时间
     */
    public static Date getMonthStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        monthStart(calendar);
        return calendar.getTime();
    }

    /**
     * 获取下月 1 号 00:00:00
     *
     * @param date 日期
     * @return 开始时间
     */
    public static Date getNextMonthStart(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        monthStart(calendar);
        return calendar.getTime();
    }

    private static void dayStart(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
    }

    private static void weekStart(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        dayStart(calendar);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
    }

    private static void monthStart(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        dayStart(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
    }

    private static void yearStart(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        monthStart(calendar);
        calendar.set(Calendar.MONTH, 0);
    }


    /**
     * 解析时间
     *
     * @param strDate yyyy-MM-dd 或者 yyyy-MM-dd HH:mm:ss
     * @return date
     */
    public static Date parseDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException ignored) {
        }
        if (date == null) {
            format = new SimpleDateFormat(DATE_PATTERN);
            try {
                date = format.parse(strDate);
            } catch (ParseException ignored) {
            }
        }
        return date;
    }

    /**
     * 格式化时间
     *
     * @param date 日期
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
        return format.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @return yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(DATE_PATTERN);
        return format.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date 日期
     * @return HH:mm:ss
     */
    public static String formatTime(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(TIME_PATTERN);
        return format.format(date);
    }

    /**
     * 格式化月份
     *
     * @param date 日期
     * @return yyyy-MM
     */
    public static String formatMonth(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(MONTH_PATTERN);
        return format.format(date);
    }


    /**
     * 解析统计开始时间
     *
     * @param string 时间字符串
     * @return date
     */
    public static Date startDate(String string) {
        return parseDate(string);
    }

    /**
     * 解析统计结束时间
     *
     * @param string 时间字符串
     * @return date
     */
    public static Date endDate(String string) {
        Date date = parseDate(string);
        return date == null ? new Date() : date;
    }

    /**
     * 获得指定日期的前几天
     *
     * @param specifiedDay 日期
     * @param day          天数
     * @return 转之后的天数
     */
    public static String getSpecifiedDayBefore(String specifiedDay, int day) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_TIME_PATTERN).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int cDay = c.get(Calendar.DATE);
        c.set(Calendar.DATE, cDay - day);

        String dayBefore = new SimpleDateFormat(DATE_TIME_PATTERN).format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后几天
     *
     * @param specifiedDay 日期
     * @param day          天数
     * @return 转之后的天数
     */
    public static String getSpecifiedDayAfter(String specifiedDay, int day) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_TIME_PATTERN).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int cDay = c.get(Calendar.DATE);
        c.set(Calendar.DATE, cDay + day);

        String dayAfter = new SimpleDateFormat(DATE_TIME_PATTERN).format(c.getTime());
        return dayAfter;
    }

    /**
     * 获得指定日期的前几月
     *
     * @param specifiedDay 日期
     * @param month        月数
     * @return 转之后的月数
     */
    public static String getSpecifiedMonthBefore(String specifiedDay, int month) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(MONTH_PATTERN).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int cDay = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, cDay - month);

        String dayBefore = new SimpleDateFormat(MONTH_PATTERN).format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后几月
     *
     * @param specifiedDay 日期
     * @param month        月数
     * @return 转之后的月数
     */
    public static String getSpecifiedMonthAfter(String specifiedDay, int month) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(MONTH_PATTERN).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int cDay = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, cDay + month);

        String dayAfter = new SimpleDateFormat(MONTH_PATTERN).format(c.getTime());
        return dayAfter;
    }

    /**
     * 将时间整理成整点
     *
     * @param time 时间
     * @return
     */
    public static String checkTime(String time) {
        if (time.indexOf(COLON) > 0) {
            return time.substring(0, time.indexOf(COLON)) + ":00:00";
        }
        return time + " 00:00:00";
    }
}
