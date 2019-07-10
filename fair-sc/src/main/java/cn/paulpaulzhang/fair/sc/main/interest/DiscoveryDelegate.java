package cn.paulpaulzhang.fair.sc.main.interest;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间：7/10/19
 * 创建人： paulpaulzhang
 * 描述：发现页
 */
public class DiscoveryDelegate extends FairDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_discovery;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {

    }
}
