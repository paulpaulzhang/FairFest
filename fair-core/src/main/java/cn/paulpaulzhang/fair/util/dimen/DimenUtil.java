package cn.paulpaulzhang.fair.util.dimen;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import cn.paulpaulzhang.fair.app.Fair;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.util
 * 文件名：   DimenUtil
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/21 21:23
 * 描述：     测量工具类
 */
public class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = Fair.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = Fair.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getScreenWidthByDp() {
        final Resources resources = Fair.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = Fair.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = Fair.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
