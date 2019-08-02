package cn.paulpaulzhang.fairfest;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.paulpaulzhang.fair.app.Fair;
import cn.paulpaulzhang.fair.net.interceptors.DebugInterceptor;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import es.dmoral.toasty.Toasty;
import okhttp3.logging.HttpLoggingInterceptor;

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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(s -> FairLogger.d("RetrofitLog", s));
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BASIC);
        Fair.init(this)
                .withApiHost("http://www.matchstickmen.club:8080/")
//                .withInterceptor(new DebugInterceptor("index", R.raw.test))
//                .withInterceptor(new DebugInterceptor("post", R.raw.post))
//                .withInterceptor(new DebugInterceptor("ic_like", R.raw.like))
//                .withInterceptor(new DebugInterceptor("ic_topic", R.raw.topic))
//                .withInterceptor(new DebugInterceptor("recommend", R.raw.recommend_user))
                .withInterceptor(loggingInterceptor)
                .configure();

        Logger.addLogAdapter(new AndroidLogAdapter());
        ObjectBox.init(this);
        Fresco.initialize(this);
        Toasty.Config.getInstance().apply();
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
