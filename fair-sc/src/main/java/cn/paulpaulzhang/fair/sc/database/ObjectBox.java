package cn.paulpaulzhang.fair.sc.database;

import android.content.Context;

import cn.paulpaulzhang.fair.sc.BuildConfig;
import cn.paulpaulzhang.fair.sc.database.Entity.MyObjectBox;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class ObjectBox {
    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();

        if (BuildConfig.DEBUG) {
//            boolean started = new AndroidObjectBrowser(boxStore).start(context.getApplicationContext());
//            FairLogger.i("ObjectBrowser", "Started: " + started);
        }
    }

    public static BoxStore get() {
        return boxStore;
    }
}
