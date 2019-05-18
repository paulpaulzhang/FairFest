package cn.paulpaulzhang.fair.app;

import android.content.Context;

import java.util.HashMap;

/**
 * 项目名：   FairFest
 * 包名：     com.paulpaulzhang.fair.app
 * 文件名：   Fair
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/18 18:49
 * 描述：     工具
 */
public final class Fair {
    public static Configurator init(Context context) {
        getConfigurations().put(ConfigType.APPLICATION_CONTEXT.name(), context.getApplicationContext());
        return Configurator.getInstance();
    }

    private static HashMap<String, Object> getConfigurations() {
        return Configurator.getInstance().getLatteConfigs();
    }

    public static Context getApplicationContext() {
        return (Context) getConfigurations().get(ConfigType.APPLICATION_CONTEXT.name());
    }
}
