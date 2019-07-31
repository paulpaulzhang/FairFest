package cn.paulpaulzhang.fair.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.R;
import cn.paulpaulzhang.fair.R2;
import cn.paulpaulzhang.fair.base.activities.FairActivity;
import cn.paulpaulzhang.fair.base.app.AccountManager;
import cn.paulpaulzhang.fair.base.app.IUserChecker;
import cn.paulpaulzhang.fair.ui.sign.SignUpActivity;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import cn.paulpaulzhang.fair.util.timer.BaseTimerTask;
import cn.paulpaulzhang.fair.util.timer.ITimerListener;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.sc.launcher
 * 文件名：   LauncherActivity
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/29 9:18
 * 描述：     启动页
 */
public class LauncherActivity extends FairActivity implements ITimerListener {
    @BindView(R2.id.tv_launcher_timer)
    AppCompatTextView mTvTimer = null;

    private Timer mTimer = null;
    private int mCount = 5;

    @OnClick(R2.id.tv_launcher_timer)
    void onClickTimerView() {
        assert mTimer != null;
        mTimer.cancel();
        mTimer = null;
        checkIsShowScroll();
    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    private void checkIsShowScroll() {
        if (!FairPreference.getAppFlag(ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name())) {
            startActivity(new Intent(LauncherActivity.this, LauncherScrollActivity.class));
            finish();
        } else {
            //判断用户是否登陆
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    Toast.makeText(LauncherActivity.this, "already sign", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNotSignIn() {
                    startActivity(new Intent(LauncherActivity.this, SignUpActivity.class));
                    finish();
                }
            });
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_launcher;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initTimer();
    }

    @Override
    public void onTimer() {
        runOnUiThread(() -> {
            assert mTvTimer != null;
            mTvTimer.setText(MessageFormat.format("跳过\n{0}s", mCount));
            mCount--;
            if (mCount < 0) {
                assert mTimer != null;
                mTimer.cancel();
                mTimer = null;
                checkIsShowScroll();
            }
        });
    }
}
