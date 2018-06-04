package com.hzgc.service.visual.util;

import com.hzgc.service.visual.dao.EsSearchParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final String COLON = ":";
    private static SimpleDateFormat sdf = new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS);

    /**
     * 将时间整理成整点
     *
     * @param time 时间
     * @return 整点时间
     */
    public static String checkTime(String time) {
        if (time.indexOf(COLON) > 0) {
            return time.substring(0, time.indexOf(COLON)) + ":00:00";
        }
        return time + " 00:00:00";
    }


    /**
     * 获得指定日期的后几天
     *
     * @param specifiedDay 日期
     * @param day          天数
     * @return 转之后的天数
     */
    public static String getSpecifiedDayAfter(String specifiedDay, int day){
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        try {
            date = new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int cDay = c.get(Calendar.DATE);
        c.set(Calendar.DATE, cDay + day);

        return new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS).format(c.getTime());
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
        Date date = new Date();
        try {
            date = new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int cDay = c.get(Calendar.DATE);
        c.set(Calendar.DATE, cDay - day);

        return new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS).format(c.getTime());
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
        DateFormat format = new SimpleDateFormat(EsSearchParam.TIMEFORMAT_YMDHMS);
        return format.format(date);
    }

    public static boolean checkForm(String date){
        if(date == null || "".equals(date)){
            return true;
        }
        boolean judgment1 = date.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}");
        boolean judgment2 = date.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}");
        if(!judgment1 && !judgment2)
            return false;
        if(judgment2){
            String[] date1 = date.split(" ")[0].split("-");
            int month = Integer.parseInt(date1[1]);
            int day = Integer.parseInt(date1[2]);
            return month <= 12 && day <= 31;
        } else {
            String[] date1 = date.split(" ")[0].split("-");
            String[] date2 = date.split(" ")[1].split(":");
            int month = Integer.parseInt(date1[1]);
            int day = Integer.parseInt(date1[2]);
            int hour = Integer.parseInt(date2[0]);
            int minits = Integer.parseInt(date2[1]);
            int second = Integer.parseInt(date2[2]);
            return month <= 12 && hour <= 24 && minits <= 60 && second <= 60 && day <= 31;
        }
    }

    public static int comparetor(String time1, String time2){
        try {
            long res = sdf.parse(time1).getTime() - sdf.parse(time2).getTime();
            return res > 0 ? 1 : (res < 0 ? -1 : 0);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
