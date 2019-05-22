package cn.paulpaulzhang.fair.net.download;

import android.os.AsyncTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.WeakHashMap;

import cn.paulpaulzhang.fair.net.RestCreator;
import cn.paulpaulzhang.fair.net.callback.IError;
import cn.paulpaulzhang.fair.net.callback.IFailure;
import cn.paulpaulzhang.fair.net.callback.IRequest;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.net.download
 * 文件名：   DownloadHandler
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/22 8:53
 * 描述：     TODO
 */
public class DownloadHandler {

    private final String URL;
    private final static WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IError ERROR;
    private final IFailure FAILURE;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;

    public DownloadHandler(String url,
                           IRequest request,
                           ISuccess success,
                           IError error,
                           IFailure failure,
                           String downloadDir,
                           String extension,
                           String name) {
        this.URL = url;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.ERROR = error;
        this.FAILURE = failure;
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
    }

    public final void handleDownload() {
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        RestCreator.getRestService().download(URL, PARAMS).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    final ResponseBody responseBody = response.body();

                    final SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS);
                    task.executeOnExecutor
                            (AsyncTask.THREAD_POOL_EXECUTOR, DOWNLOAD_DIR, EXTENSION, responseBody, NAME);

                    if (task.isCancelled()) {
                        if (REQUEST != null) {
                            REQUEST.onRequestEnd();
                        }
                    }
                } else {
                    if (ERROR != null) {
                        ERROR.onError(response.code(), response.message());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                if (FAILURE != null) {
                    FAILURE.onFailure(t);
                }
            }
        });
    }
}
