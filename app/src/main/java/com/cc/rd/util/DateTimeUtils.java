package com.cc.rd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public final static Long ONE_HOUR_IN_MILLS = 60 * 60 * 1000L;

    public final static Long ONE_DAY_IN_MILLS = 24 * 60 * 60 * 1000L;

    public final static Long ONE_WEEK_IN_MILLS = 7 * ONE_DAY_IN_MILLS;

    private final static Long EIGHT_HOURS_IN_MILLS = 8 * 60 * 60 * 1000L;

    public final static Long DAY_IN_MILLS_1080 = 1080 * ONE_DAY_IN_MILLS;

    public final static Long NANO_PER_MILLS = 1000000L;

    public final static int MIN_DAY_OF_MONTH = 28;


    /**
     * 一分钟多少毫秒
     */
    private static final Long SEC = 60 * 1000L;


    /**
     * 这个跟
     * LocalDateTime
     * .now(ZoneOffset.UTC)
     * .toInstant(ZoneOffset.UTC)
     * .toEpochMilli()
     * 是一个效果
     *
     * @return
     */
    public static long utcNow() {
        return System.currentTimeMillis();
    }


    public static Long getMillisByMins(int duration) {
        return duration * SEC;
    }


    private static String addZero(int param) {
        String paramStr = param < 10 ? "0" + param : "" + param;
        return paramStr;
    }

    /**
     * 将毫秒字符串转成时间格式yyyy-MM-dd HH:mm
     */
    public static String getMil2mmTimeFormat(String timeStr) {
        long time = Long.parseLong(timeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = addZero(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = addZero(day);
        //24小时制
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String hourStr = addZero(hour);
        int minute = calendar.get(Calendar.MINUTE);
        String minuteStr = addZero(minute);
        int second = calendar.get(Calendar.SECOND);
        String secondStr = addZero(second);
        return (year + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr);
    }

    /**
     * 将毫秒字符串转成时间格式yyyy-MM-dd HH:mm:ss
     */
    public static String getMil2ssTimeFormat(String timeStr) {
        long time = Long.parseLong(timeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = addZero(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = addZero(day);
        //24小时制
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String hourStr = addZero(hour);
        int minute = calendar.get(Calendar.MINUTE);
        String minuteStr = addZero(minute);
        int second = calendar.get(Calendar.SECOND);
        String secondStr = addZero(second);
        return (year + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr + ":" + secondStr);
    }

    /**
     * 将毫秒字符串转成时间格式yyyy-MM-dd
     */
    public static String getMil2ddTimeFormat(String timeStr) {
        long time = Long.parseLong(timeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = addZero(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = addZero(day);
        return (year + "-" + monthStr + "-" + dayStr);
    }

    /**
     * 时间戳 获取年
     */
    public static int getYear(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String timeString = sdf.format(time);
        try {
            date = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    /**
     * 时间戳 获取月
     */
    @Deprecated
    public static int getMonth(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String timeString = sdf.format(time);
        try {
            date = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = (calendar.get(Calendar.MONTH) + 1);
        return month;
    }

    /**
     * 时间戳 获取日
     */
    @Deprecated
    public static int getDay(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String timeString = sdf.format(time);
        try {
            date = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = (calendar.get(Calendar.DAY_OF_MONTH));
        return day;
    }


    /**
     * 时间戳转换日期
     */
    @Deprecated
    public static String stampToTime(String stamp) {
        String sd = "";
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 时间戳转换日期
        sd = sdf.format(new Date(Long.parseLong(stamp)));
        return sd;
    }

    /**
     * 日期转换为时间戳 timeToStamp("2015-07-03")
     */
    public static Long timeToStamp(String timers) {
        Date d = new Date();
        long timeStemp = 0;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            // 日期转换为时间戳
            d = sf.parse(timers);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeStemp = d.getTime();
        return timeStemp;
    }

    /**
     * 日期转换为时间戳 timeToStamp("2015-07-03")
     */
    public static Long timemmToStamp(String timers) {
        Date d = new Date();
        long timeStemp = 0;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            // 日期转换为时间戳
            d = sf.parse(timers);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeStemp = d.getTime();
        return timeStemp;
    }

}
