package cn.paulpaulzhang.fair.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.activities
 * 文件名：   FairActivity
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/28 23:20
 * 描述：     应用activity
 */
public abstract class FairActivity extends PermissionCheckerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initToolbar(Toolbar mToolbar, String title) {
        if (title != null) {
            mToolbar.setTitle(title);
        }
        initToolbar(mToolbar);
    }
}
