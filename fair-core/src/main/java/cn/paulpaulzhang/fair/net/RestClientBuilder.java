package cn.paulpaulzhang.fair.net;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import cn.paulpaulzhang.fair.net.callback.IError;
import cn.paulpaulzhang.fair.net.callback.IFailure;
import cn.paulpaulzhang.fair.net.callback.IRequest;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.net
 * 文件名：   RestClientBuilder
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/19 12:44
 * 描述：     TODO
 */
public class RestClientBuilder {

    private String mUrl;
    private static final Map<String, Object> PARAMS = RestCreator.getParams();
    private IRequest mIRequest;
    private ISuccess mISuccess;
    private IError mIError;
    private IFailure mIFailure;
    private ResponseBody mBody;
    private File mFile;

    RestClientBuilder() {

    }

    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
        return this;
    }

    public final RestClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String file) {
        this.mFile = new File(file);
        return this;
    }

    public final RestClientBuilder raw(String raw) {
        this.mBody = ResponseBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder request(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    public final RestClient build() {
        return new RestClient(mUrl, PARAMS, mIRequest, mISuccess, mIError, mIFailure, mBody, mFile);
    }
}
