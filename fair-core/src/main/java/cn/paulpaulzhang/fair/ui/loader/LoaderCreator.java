package cn.paulpaulzhang.fair.ui.loader;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.Objects;
import java.util.WeakHashMap;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.ui
 * 文件名：   LoaderCreator
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/21 20:56
 * 描述：     加载动画创建
 */
public class LoaderCreator {
    private static final WeakHashMap<String, Indicator> LOADING_MAP = new WeakHashMap<>();

    static AVLoadingIndicatorView create(String type, Context context) {
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if (LOADING_MAP.get(type) == null) {
            final Indicator indicator = getIndicator(type);
            LOADING_MAP.put(type, indicator);
        }
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }

    private static Indicator getIndicator(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        final StringBuilder drawableClassname = new StringBuilder();
        if (!name.contains(".")) {
            final String defaultPackageName = Objects.requireNonNull(AVLoadingIndicatorView.class.getPackage()).getName();
            drawableClassname.append(defaultPackageName)
                    .append(".indicators")
                    .append(".");
        }

        drawableClassname.append(name);
        try {
            final Class<?> drawableClass = Class.forName(drawableClassname.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
