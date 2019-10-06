package cn.paulpaulzhang.fair.sc.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.app.AccountManager;
import cn.paulpaulzhang.fair.app.IUserChecker;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.HomeActivity;
import cn.paulpaulzhang.fair.sc.sign.SignInActivity;
import cn.paulpaulzhang.fair.sc.sign.SignUpActivity;
import cn.paulpaulzhang.fair.ui.launcher.LauncherHolderCreator;
import cn.paulpaulzhang.fair.ui.launcher.ScrollLauncherTag;
import cn.paulpaulzhang.fair.util.storage.FairPreference;

public class LauncherScrollActivity extends FairActivity implements OnItemClickListener {
    @BindView(R2.id.CB_start)
    ConvenientBanner<Integer> mConvenientBanner = null;
    private final ArrayList<Integer> images = new ArrayList<>();

    private void initBanner() {
        images.add(R.mipmap.launcher1);
        images.add(R.mipmap.launcher2);
        images.add(R.mipmap.launcher3);
        images.add(R.mipmap.launcher4);

        mConvenientBanner.setPages(new LauncherHolderCreator(), images)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(this);

    }

    @Override
    public int setLayout() {
        return R.layout.activity_scroll_launcher;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).init();
        initBanner();
    }

    @Override
    public void onItemClick(int position) {
        if (position == images.size() - 1) {
            FairPreference.setAppFlag(ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name(), true);
            //检测用户是否已经登陆
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    startActivity(new Intent(LauncherScrollActivity.this, HomeActivity.class));
                    finish();
                }

                @Override
                public void onNotSignIn() {
                    startActivity(new Intent(LauncherScrollActivity.this, SignUpActivity.class));
                    finish();
                }
            });
        }
    }
}
