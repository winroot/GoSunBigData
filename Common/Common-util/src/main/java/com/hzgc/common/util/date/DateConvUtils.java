package com.hzgc.common.util.date;

import java.time.*;
import java.util.Date;

/**
 * java8 数据转换工具类
 *
 * @author liuzhikun
 */
public class DateConvUtils {
    /**
     * 01. java.util.Date --> java.time.LocalDateTime
     * @param dateTime
     * @return
     */
    public static LocalDateTime convUDateToLocalDateTime(Date dateTime) {
        Date date = dateTime;
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    /**
     * 02. java.util.Date --> java.time.LocalDate
     * @param date
     * @return
     */
    public static LocalDate convUDateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

    /**
     * 03. java.util.Date --> java.time.LocalTime
     * @param time
     * @return
     */
    public static LocalTime convUDateToLocalTime(Date time) {
        Date date = time;
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalTime localTime = localDateTime.toLocalTime();
        return localTime;
    }

    /**
     * 04. java.time.LocalDateTime --> java.util.Date
     * @param dateTime
     * @return
     */
    public static Date convLocalDateTimeToUdate(LocalDateTime dateTime) {
        LocalDateTime localDateTime = dateTime;
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 05. java.time.LocalDate --> java.util.Date
     * @param date
     * @return
     */
    public static Date convLocalDateToUdate(LocalDate date) {
        LocalDate localDate = date;
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date d = Date.from(instant);
        return d;
    }

    /**
     * 06. java.time.LocalTime --> java.util.Date
     * @param time
     * @return
     */
    public static Date convLocalTimeToUdate(LocalTime time) {
        LocalTime localTime = time;
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }
}
