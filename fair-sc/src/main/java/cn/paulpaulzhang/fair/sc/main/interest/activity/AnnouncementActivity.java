package cn.paulpaulzhang.fair.sc.main.interest.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.activity
 * 创建时间：9/3/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class AnnouncementActivity extends FairActivity {

    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    @Override
    public int setLayout() {
        return R.layout.activity_announcement;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        initToolbar(mToolbar, "公告");
    }
}
