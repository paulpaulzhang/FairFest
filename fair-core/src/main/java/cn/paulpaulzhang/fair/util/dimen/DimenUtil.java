package cn.paulpaulzhang.fair.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import cn.paulpaulzhang.fair.base.app.Fair;

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
}
