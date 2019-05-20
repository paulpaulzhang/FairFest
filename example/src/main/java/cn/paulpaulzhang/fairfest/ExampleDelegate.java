package cn.paulpaulzhang.fairfest;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.paulpaulzhang.fairfest.R;

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
    @Override
    public Object setLayout() {
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        testClient();
    }

    private void testClient() {
        RestClient.builder()
                .url("https://www.baidu.com/")
                .success(response -> Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show())
                .error((code, msg) -> Toast.makeText(getContext(), "请求错误 " + code + "   " + msg, Toast.LENGTH_LONG).show())
                .failure(() -> Toast.makeText(getContext(), "请求失败", Toast.LENGTH_LONG).show())
                .build()
                .get();
    }
}
