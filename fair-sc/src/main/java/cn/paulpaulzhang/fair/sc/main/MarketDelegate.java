package cn.paulpaulzhang.fair.sc.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/8/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class MarketDelegate extends FairDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_market;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {

    }
}
