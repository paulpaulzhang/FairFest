package cn.paulpaulzhang.fair.util.common;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 包名: cn.paulpaulzhang.fair.util
 * 创建时间: 7/29/2019
 * 创建人: zlm31
 * 描述:
 */
public class CommonUtil {
    public static void showKeyboard(AppCompatEditText editText) {
        //其中editText为dialog中的输入框的 EditText
        if (editText != null) {
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.showSoftInput(editText, 0);
            }
        }
    }

    // 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
    public static String getTime(Date date) {
        boolean sameYear = false;
        String todaySDF = "HH:mm";
        String yesterdaySDF = "昨天";
        String beforeYesterdaySDF = "前天";
        String otherSDF = "MM月dd日";
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
                time = yesterdaySDF;
                return time;
            }
            todayCalendar.add(Calendar.DATE, -2);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是前天
                time = beforeYesterdaySDF;
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

