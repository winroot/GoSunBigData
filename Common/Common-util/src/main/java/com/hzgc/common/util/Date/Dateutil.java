package com.hzgc.common.util.Date;

import java.util.Calendar;

public class Dateutil {

    /**
     * 获取当前月份共有多少天
     * @param date 时间 格式：2018-09
     * @return 当月天数
     */
    public static int getActualMaximum(String date) {
        int year = Integer.parseInt(String.valueOf(date.charAt(0)) + date.charAt(1) + date.charAt(2) + date.charAt(3));
        int month = Integer.parseInt(String.valueOf(date.charAt(5)) + date.charAt(6));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);             // 当前月份减1
        return cal.getActualMaximum(Calendar.DATE);
    }

}
