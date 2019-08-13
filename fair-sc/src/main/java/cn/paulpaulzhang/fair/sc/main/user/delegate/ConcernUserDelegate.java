package cn.paulpaulzhang.fair.sc.main.user.delegate;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user.delegate
 * 创建时间: 8/13/2019
 * 创建人: zlm31
 * 描述:
 */
public class ConcernUserDelegate extends FairDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_concern_user;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {

    }
}
