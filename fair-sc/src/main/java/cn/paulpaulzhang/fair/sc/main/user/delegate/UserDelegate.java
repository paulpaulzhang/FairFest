package cn.paulpaulzhang.fair.sc.main.user.delegate;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user
 * 创建时间: 8/11/2019
 * 创建人: zlm31
 * 描述:
 */
public class UserDelegate extends FairDelegate {
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    @Override
    public Object setLayout() {
        return R.layout.delegate_user;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
    }
}
