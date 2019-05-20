package cn.paulpaulzhang.fairfest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.paulpaulzhang.fairfest.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;

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
                .success(response -> textView.setText(response))
                .error((code, msg) -> textView.setText(code + "   " + msg))
                .failure(throwable -> textView.setText(throwable.getMessage()))
                .build()
                .get();
    }
}
