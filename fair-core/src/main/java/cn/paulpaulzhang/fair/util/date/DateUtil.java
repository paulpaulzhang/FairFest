package cn.paulpaulzhang.fair.util.date;

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
    // 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
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
}
