package cn.paulpaulzhang.fair.util.timer;

import java.util.TimerTask;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.util.timer
 * 文件名：   BaseTimerTask
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/22 22:23
 * 描述：     基础定时器
 */
public class BaseTimerTask extends TimerTask {

    private ITimerListener mITimerListener;

    public BaseTimerTask(ITimerListener timerListener) {
        this.mITimerListener = timerListener;
    }

    @Override
    public void run() {
        if (mITimerListener != null) {
            mITimerListener.onTimer();
        }
    }
}
