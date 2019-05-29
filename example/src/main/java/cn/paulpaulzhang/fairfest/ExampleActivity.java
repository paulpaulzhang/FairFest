package cn.paulpaulzhang.fairfest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.IRequest;
import cn.paulpaulzhang.fair.ui.FairLoader;

public class ExampleActivity extends FairActivity {

    @BindView(R.id.text_view)
    TextView textView;

    static Handler HANDLER = new Handler();

    @Override
    public int setLayout() {
        return R.layout.activity_example;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        testClient();
    }

    @SuppressLint("SetTextI18n")
    private void testClient() {
        RestClient.builder()
                .url("http://127.0.0.1/index")
                .request(new IRequest() {
                    @Override
                    public void onRequestStart() {
                        FairLoader.showLoading(ExampleActivity.this);
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
