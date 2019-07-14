package cn.paulpaulzhang.fair.util.image;

import android.content.Context;

/**
 * 包名: cn.paulpaulzhang.fair.util.image
 * 创建时间: 7/14/2019
 * 创建人: zlm31
 * 描述:
 */
public class ImageUtil {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
