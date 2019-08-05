package cn.paulpaulzhang.fair.ui.launcher;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.gyf.immersionbar.ImmersionBar;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.app.AccountManager;
import cn.paulpaulzhang.fair.app.IUserChecker;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.HomeActivity;
import cn.paulpaulzhang.fair.sc.sign.SignInActivity;
import cn.paulpaulzhang.fair.sc.sign.SignUpActivity;
import cn.paulpaulzhang.fair.ui.launcher.ScrollLauncherTag;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import cn.paulpaulzhang.fair.util.timer.BaseTimerTask;
import cn.paulpaulzhang.fair.util.timer.ITimerListener;
import es.dmoral.toasty.Toasty;

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
                    JMessageClient.login(String.valueOf(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())), "admin", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                startActivity(new Intent(LauncherActivity.this, HomeActivity.class));
                                finish();
                                FairLogger.d("JMessage", "登陆成功");
                            } else {
                                Toasty.error(LauncherActivity.this, "登陆失败", Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(LauncherActivity.this, SignInActivity.class));
                            }
                        }
                    });
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
        ImmersionBar.with(this).init();
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
