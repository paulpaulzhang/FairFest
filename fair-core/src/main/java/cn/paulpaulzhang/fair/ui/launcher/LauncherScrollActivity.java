package cn.paulpaulzhang.fair.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import cn.paulpaulzhang.fair.R;
import cn.paulpaulzhang.fair.R2;
import cn.paulpaulzhang.fair.base.activities.FairActivity;
import cn.paulpaulzhang.fair.base.app.AccountManager;
import cn.paulpaulzhang.fair.base.app.IUserChecker;
import cn.paulpaulzhang.fair.ui.sign.SignUpActivity;
import cn.paulpaulzhang.fair.util.storage.FairPreference;

public class LauncherScrollActivity extends FairActivity implements OnItemClickListener {
    @BindView(R2.id.CB_start)
    ConvenientBanner<Integer> mConvenientBanner = null;
    private final ArrayList<Integer> images = new ArrayList<>();

    private void initBanner() {
        images.add(R.mipmap.launcher_01);
        images.add(R.mipmap.launcher_02);
        images.add(R.mipmap.launcher_03);
        images.add(R.mipmap.launcher_04);
        images.add(R.mipmap.launcher_05);

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                    Toast.makeText(LauncherScrollActivity.this, "already sign", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNotSignIn() {
                    startActivity(new Intent(LauncherScrollActivity.this, SignUpActivity.class));
                }
            });
        }
    }
}
