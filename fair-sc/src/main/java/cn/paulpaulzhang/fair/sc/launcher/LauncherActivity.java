package cn.paulpaulzhang.fair.sc.launcher;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
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

    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_launcher;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
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
            }
        });
    }
}
