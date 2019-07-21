package cn.paulpaulzhang.fairfest;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import cn.paulpaulzhang.fair.app.Fair;
import cn.paulpaulzhang.fair.net.interceptors.DebugInterceptor;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import es.dmoral.toasty.Toasty;

/**
 * 项目名：   FairFest
 * 包名：     com.paulpaulzhang.fairfest
 * 文件名：   FairApp
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/18 19:02
 * 描述：     样例
 */
public class FairApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fair.init(this)
                .withIcon(new FontAwesomeModule())
                .withApiHost("http://127.0.0.1/")
                .withInterceptor(new DebugInterceptor("index", R.raw.test))
                .withInterceptor(new DebugInterceptor("post", R.raw.post))
                .withInterceptor(new DebugInterceptor("user", R.raw.user))
                .withInterceptor(new DebugInterceptor("like", R.raw.like))
                .withInterceptor(new DebugInterceptor("topic", R.raw.topic))
                .configure();
        Logger.addLogAdapter(new AndroidLogAdapter());
        ObjectBox.init(this);
        Fresco.initialize(this);
        Toasty.Config.getInstance().apply();
    }
}
