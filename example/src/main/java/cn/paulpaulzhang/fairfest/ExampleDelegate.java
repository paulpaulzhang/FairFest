package cn.paulpaulzhang.fairfest;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.paulpaulzhang.fairfest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.IRequest;
import cn.paulpaulzhang.fair.ui.FairLoader;
import cn.paulpaulzhang.fair.ui.LoaderCreator;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fairfest
 * 文件名：   ExampleDelegate
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/18 19:48
 * 描述：     测试程序
 */
public class ExampleDelegate extends FairDelegate {
    @BindView(R.id.text_view)
    TextView textView;

    static Handler HANDLER = new Handler();

    @Override
    public Object setLayout() {
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        testClient();

    }

    @SuppressLint("SetTextI18n")
    private void testClient() {
        RestClient.builder()
                .url("http://127.0.0.1:5000/")
                .request(new IRequest() {
                    @Override
                    public void onRequestStart() {
                        FairLoader.showLoading(getContext());
                    }

                    @Override
                    public void onRequestEnd() {
                        HANDLER.postDelayed(FairLoader::stopLoading, 1000);
                    }
                })
                .success(response -> textView.setText(response))
                .error((code, msg) -> textView.setText(code + "   " + msg))
                .failure(throwable -> textView.setText(throwable.getMessage()))
                .build()
                .get();
    }
}
