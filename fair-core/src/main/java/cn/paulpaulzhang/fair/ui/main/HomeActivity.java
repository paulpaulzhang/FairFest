package cn.paulpaulzhang.fair.ui.main;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.paulpaulzhang.fair.R;
import cn.paulpaulzhang.fair.R2;
import cn.paulpaulzhang.fair.base.activities.FairActivity;
import cn.paulpaulzhang.fair.base.delegates.FairDelegate;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/3/19
 * 创建人： paulpaulzhang
 * 描述：首页activity
 */
public class HomeActivity extends FairActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R2.id.bottom_navigation)
    AHBottomNavigation mBottomNavigation;

    @BindView(R2.id.view_pager)
    AHBottomNavigationViewPager mViewPager;


    private AHBottomNavigationAdapter mAdapter;
    private BottomNavViewPagerAdapter mViewPagerAdapter;
    private List<AHBottomNavigationItem> items = new ArrayList<>();
    private FairDelegate currentDelegate;


    @Override
    public int setLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initBottomNavigation();
        requestPermissions();

    }

    private void requestPermissions() {
        EasyPermissions.requestPermissions(this, "应用需要存取图片", 1000,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void initBottomNavigation() {

        AHBottomNavigationItem interest = new AHBottomNavigationItem
                (getString(R.string.interest), R.drawable.ic_interest);
        AHBottomNavigationItem market = new AHBottomNavigationItem
                (getString(R.string.market), R.drawable.ic_mall);
        AHBottomNavigationItem message = new AHBottomNavigationItem
                (getString(R.string.message), R.drawable.ic_message);

        items.add(interest);
        items.add(market);
        items.add(message);

        mBottomNavigation.addItems(items);
        mBottomNavigation.setTranslucentNavigationEnabled(true);
        mBottomNavigation.setBehaviorTranslationEnabled(false);
        mBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        mBottomNavigation.setAccentColor(getColor(R.color.colorAccent));

        mBottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if (currentDelegate == null) {
                currentDelegate = mViewPagerAdapter.getCurrentDelegate();
            }

            if (wasSelected) {
                //TODO 刷新逻辑
                return true;
            }

            mViewPager.setCurrentItem(position, false);

            if (currentDelegate == null) {
                return true;
            }

            return true;

        });

        mViewPager.setOffscreenPageLimit(3);
        mViewPagerAdapter = new BottomNavViewPagerAdapter
                (getSupportFragmentManager(),
                        BottomNavViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);
        mBottomNavigation.setCurrentItem(0);

        currentDelegate = mViewPagerAdapter.getCurrentDelegate();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "拒绝权限可能会影响使用，请前往设置开启所需权限", Toast.LENGTH_SHORT).show();
    }
}
