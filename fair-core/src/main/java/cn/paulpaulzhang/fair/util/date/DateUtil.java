package cn.paulpaulzhang.fair.util.date;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 包名: cn.paulpaulzhang.fair.util.date
 * 创建时间: 8/15/2019
 * 创建人: zlm31
 * 描述:
 */
public class DateUtil {

    public static String getTime(long time) {
        return getTime(new Date(time));
    }

    /**
     * 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
     *
     * @param date 日期
     * @return 对应的时间表示
     */
    public static String getTime(Date date) {
        boolean sameYear = false;
        String todaySDF = "HH:mm";
        String yesterdaySDF = "昨天 HH:mm";
        String beforeYesterdaySDF = "前天 HH:mm";
        String otherSDF = "MM月dd日 HH:mm";
        String otherYearSDF = "yyyy年";
        SimpleDateFormat sfd;
        String time;
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(now);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);

        if (dateCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
            sameYear = true;
        }

        if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
            sfd = new SimpleDateFormat(todaySDF, Locale.CHINA);
            time = sfd.format(date);
            return time;
        } else {
            todayCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
                sfd = new SimpleDateFormat(yesterdaySDF, Locale.CHINA);
                time = sfd.format(date);
                return time;
            }
            todayCalendar.add(Calendar.DATE, -2);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是前天
                sfd = new SimpleDateFormat(beforeYesterdaySDF, Locale.CHINA);
                time = sfd.format(date);
                return time;
            }
        }

        if (sameYear) {
            sfd = new SimpleDateFormat(otherSDF, Locale.CHINA);
            time = sfd.format(date);
        } else {
            sfd = new SimpleDateFormat(otherYearSDF, Locale.CHINA);
            time = sfd.format(date);
        }

        return time;
    }

    /**
     * 拿到两个时间的间隔时长
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 间隔天数
     */
    public static long getIntervalAsDay(long startTime, long endTime) {
        return (endTime - startTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 拿到月份对应的星座
     *
     * @param time 时间
     * @return 星座
     */
    public static String getConstellation(long time) {
        String format = "MM-dd";
        Date targetTime = long2Date(time, format);
        if (targetTime.getTime() >= string2Date("01-21", format).getTime()
                && targetTime.getTime() <= string2Date("02-19", format).getTime()) {
            return "水瓶座";
        }

        if (targetTime.getTime() >= string2Date("02-20", format).getTime()
                && targetTime.getTime() <= string2Date("03-20", format).getTime()) {
            return "双鱼座";
        }

        if (targetTime.getTime() >= string2Date("03-21", format).getTime()
                && targetTime.getTime() <= string2Date("04-20", format).getTime()) {
            return "白羊座";
        }

        if (targetTime.getTime() >= string2Date("04-21", format).getTime()
                && targetTime.getTime() <= string2Date("05-21", format).getTime()) {
            return "金牛座";
        }

        if (targetTime.getTime() >= string2Date("05-22", format).getTime()
                && targetTime.getTime() <= string2Date("06-21", format).getTime()) {
            return "双子座";
        }


        if (targetTime.getTime() >= string2Date("06-22", format).getTime()
                && targetTime.getTime() <= string2Date("07-23", format).getTime()) {
            return "巨蟹座";
        }

        if (targetTime.getTime() >= string2Date("07-24", format).getTime()
                && targetTime.getTime() <= string2Date("08-23", format).getTime()) {
            return "狮子座";
        }

        if (targetTime.getTime() >= string2Date("08-24", format).getTime()
                && targetTime.getTime() <= string2Date("09-23", format).getTime()) {
            return "处女座";
        }

        if (targetTime.getTime() >= string2Date("09-24", format).getTime()
                && targetTime.getTime() <= string2Date("10-23", format).getTime()) {
            return "天秤座";
        }

        if (targetTime.getTime() >= string2Date("10-24", format).getTime()
                && targetTime.getTime() <= string2Date("11-22", format).getTime()) {
            return "天蝎座";
        }

        if (targetTime.getTime() >= string2Date("11-23", format).getTime()
                && targetTime.getTime() <= string2Date("12-22", format).getTime()) {
            return "射手座";
        }

        if (targetTime.getTime() >= string2Date("12-23", format).getTime()
                && targetTime.getTime() <= string2Date("01-22", format).getTime()) {
            return "摩羯座";
        }

        return "";
    }

    /**
     * 拿到时间对应的年代，如90后，80后
     *
     * @param time 时间
     * @return 年代
     */
    public static String getEra(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);

        if (year >= 2005) {
            return "05后";
        }

        if (year >= 2000) {
            return "00后";
        }
        if (year >= 1995) {
            return "95后";
        }
        if (year >= 1990) {
            return "90后";
        }

        return "";

    }

    public static String date2String(Date date, String format) {
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }

    public static Date long2Date(long time, String format) {
        Date dateOld = new Date(time);
        String strTime = date2String(dateOld, format);
        return string2Date(strTime, format);
    }

    public static Date string2Date(String time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long string2Long(String time, String format) {
        Date date = string2Date(time, format);
        return date.getTime();
    }

}
