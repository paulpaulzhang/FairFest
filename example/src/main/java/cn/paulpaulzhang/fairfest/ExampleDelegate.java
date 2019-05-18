package cn.paulpaulzhang.fairfest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.paulpaulzhang.fairfest.R;

import cn.paulpaulzhang.fair.delegates.FairDelegate;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fairfest
 * 文件名：   ExampleDelegate
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/18 19:48
 * 描述：     TODO
 */
public class ExampleDelegate extends FairDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
