package cn.paulpaulzhang.fair.net;

import android.content.Context;

import java.util.Map;
import java.util.WeakHashMap;

import cn.paulpaulzhang.fair.net.callback.IError;
import cn.paulpaulzhang.fair.net.callback.IFailure;
import cn.paulpaulzhang.fair.net.callback.IRequest;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.net.callback.RequestCallbacks;
import cn.paulpaulzhang.fair.ui.LoaderStyle;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.net
 * 文件名：   RestClient
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/19 11:54
 * 描述：     Rest请求
 */
public class RestClient {
    private final String URL;
    private final static WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IError ERROR;
    private final IFailure FAILURE;
    private final ResponseBody BODY;

    public RestClient(String url,
                      Map<String, Object> params,
                      IRequest iRequest,
                      ISuccess iSuccess,
                      IError iError,
                      IFailure iFailure,
                      ResponseBody body) {
        this.URL = url;
        PARAMS.putAll(params);
        this.REQUEST = iRequest;
        this.SUCCESS = iSuccess;
        this.ERROR = iError;
        this.FAILURE = iFailure;
        this.BODY = body;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(HttpMethod method) {
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            default:
                break;
        }

        if (call != null) {
            call.enqueue(getRequestCallbacks());
        }
    }

    private Callback<String> getRequestCallbacks() {
        return new RequestCallbacks(
                REQUEST,
                SUCCESS,
                ERROR,
                FAILURE
        );
    }

    public final void get() {
        request(HttpMethod.GET);
    }

    public final void post() {
        request(HttpMethod.POST);
    }

    public final void put() {
        request(HttpMethod.PUT);
    }

    public final void delete() {
        request(HttpMethod.DELETE);
    }
}
