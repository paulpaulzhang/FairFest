package cn.paulpaulzhang.fair.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.FrameLayout;

import com.paulpaulzhang.fair_core.R;

import cn.paulpaulzhang.fair.delegates.FairDelegate;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.activities
 * 文件名：   ProxyActivity
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/18 19:38
 * 描述：     activity容器
 */
public abstract class ProxyActivity extends SupportActivity {

    public abstract FairDelegate setRootDelegate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
    }

    private void initContainer(@Nullable Bundle savedInstanceState) {
        final FrameLayout container = new FrameLayout(this);
        container.setId(R.id.delegate_container);
        setContentView(container);

        if (savedInstanceState == null) {
            loadRootFragment(R.id.delegate_container, setRootDelegate());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        System.runFinalization();
    }
}
