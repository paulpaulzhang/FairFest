package cn.paulpaulzhang.fair.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.activities
 * 文件名：   ProxyActivity
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/18 19:38
 * 描述：     activity基类，继承自AppCompatActivity，
 * 已绑定ButterKnife，其子类可直接使用，不需要再次绑定或解绑， 使用时请继承FairActivity
 */
public abstract class ProxyActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    /**
     * 绑定布局id，子类实现该方法传入布局id即可，不需要再实现onCreate绑定布局
     *
     * @return id
     */
    public abstract int setLayout();

    /**
     * 重写该方法实现自己的业务逻辑，该方法在onCreate调用
     *
     * @param savedInstanceState savedInstanceState
     */
    public abstract void init(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(setLayout());
        mUnbinder = ButterKnife.bind(this);
        init(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        System.gc();
        System.runFinalization();
    }
}
