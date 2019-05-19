package cn.paulpaulzhang.fair.net;

import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import cn.paulpaulzhang.fair.app.ConfigType;
import cn.paulpaulzhang.fair.app.Fair;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.net
 * 文件名：   RestCreator
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/19 12:25
 * 描述：     网络请求
 */
public class RestCreator {

    private static class ParamsHolder {
        public static final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    }

    public static WeakHashMap<String, Object> getParams() {
        return ParamsHolder.PARAMS;
    }

    public static RestService getRestService() {
        return RestServiceHolder.REST_SERVICE;
    }

    private static class RetrofitHolder {
        private static final String BASE_URL =
                (String) Fair.getConfigurations().get(ConfigType.API_HOST.name());
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(Objects.requireNonNull(BASE_URL))
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    private static class OkHttpHolder {
        private static final int TIME_OUT = 60;
        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private static class RestServiceHolder {
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }
}
