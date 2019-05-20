package cn.paulpaulzhang.fair.net.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.net.callback
 * 文件名：   RequestCallbacks
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/19 14:10
 * 描述：     请求回调
 */
public class RequestCallbacks implements Callback<String> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IError ERROR;
    private final IFailure FAILURE;

    public RequestCallbacks(IRequest request, ISuccess success, IError error, IFailure failure) {
        this.REQUEST = request;
        this.SUCCESS = success;
        this.ERROR = error;
        this.FAILURE = failure;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (SUCCESS != null) {
                    SUCCESS.onSuccess(response.body());
                }
            }
        } else {
            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE != null) {
            FAILURE.onFailure(t);
        }

        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }
    }
}
