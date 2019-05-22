package cn.paulpaulzhang.fair.net.interceptors;

import androidx.annotation.RawRes;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.paulpaulzhang.fair.util.file.FileUtil;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.net.interceptors
 * 文件名：   DebugInterceptor
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/22 20:28
 * 描述：     调试拦截器
 */
public class DebugInterceptor extends BaseInterceptor {

    private final String DEBUG_URL;
    private final int DEBUG_RAW_ID;

    public DebugInterceptor(String debugUrl, int debugRawId) {
        this.DEBUG_URL = debugUrl;
        this.DEBUG_RAW_ID = debugRawId;
    }

    private Response getResponse(Chain chain, String json) {
        return new Response.Builder()
                .code(200)
                .addHeader("Content-Type", "application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"), json))
                .message("OK")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();
    }

    private Response debugResponse(Chain chain, @RawRes int rawId) {
        final String json = FileUtil.getRawFile(rawId);
        return getResponse(chain, json);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        final String url  = chain.request().url().toString();
        if (url.contains(DEBUG_URL)) {
            return debugResponse(chain, DEBUG_RAW_ID);
        }

        return chain.proceed(chain.request());
    }
}
