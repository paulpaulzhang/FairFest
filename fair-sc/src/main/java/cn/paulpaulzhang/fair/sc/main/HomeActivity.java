package cn.paulpaulzhang.fair.sc.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.user.UserCenterActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/3/19
 * 创建人： paulpaulzhang
 * 描述：首页activity
 */
public class HomeActivity extends FairActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R2.id.civ_user)
    CircleImageView mUser;

    @BindView(R2.id.ll_search)
    LinearLayout mSearch;

    @BindView(R2.id.iv_add)
    AppCompatImageView mAdd;

    @BindView(R2.id.bottom_navigation)
    AHBottomNavigation mBottomNavigation;

    @BindView(R2.id.view_pager)
    AHBottomNavigationViewPager mViewPager;


    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

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
        setSupportActionBar(mToolbar);
        initBottomNavigation();
        requestPermissions();
    }

    private void requestPermissions() {
        EasyPermissions.requestPermissions(this, "应用需要存取图片", 1000,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void initBottomNavigation() {
        AHBottomNavigationItem study = new AHBottomNavigationItem
                (getString(R.string.study), R.drawable.outline_school_24);
        AHBottomNavigationItem interest = new AHBottomNavigationItem
                (getString(R.string.interest), R.drawable.outline_bubble_chart_24);
        AHBottomNavigationItem market = new AHBottomNavigationItem
                (getString(R.string.market), R.drawable.outline_local_mall_24);
        AHBottomNavigationItem chat = new AHBottomNavigationItem
                (getString(R.string.chat), R.drawable.outline_chat_24);

        items.add(study);
        items.add(interest);
        items.add(market);
        items.add(chat);

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

            mViewPager.setCurrentItem(position);

            if (currentDelegate == null) {
                return true;
            }

            return true;

        });

        mViewPager.setOffscreenPageLimit(4);
        mViewPagerAdapter = new BottomNavViewPagerAdapter
                (getSupportFragmentManager(),
                        BottomNavViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(1);
        mBottomNavigation.setCurrentItem(1);

        currentDelegate = mViewPagerAdapter.getCurrentDelegate();
    }

    @OnClick(R2.id.civ_user)
    void openUserCenter() {
        //startActivity(new Intent(HomeActivity.this, UserCenterActivity.class));
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "拒绝权限可能会影响使用，请前往设置开启所需权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
