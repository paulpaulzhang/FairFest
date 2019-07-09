package cn.paulpaulzhang.fair.sc.main;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/3/19
 * 创建人： paulpaulzhang
 * 描述：首页activity
 */
public class HomeActivity extends FairActivity {

    @BindView(R2.id.iv_menu)
    AppCompatImageView mMenu;

    @BindView(R2.id.ll_search)
    LinearLayout mSearch;

    @BindView(R2.id.iv_add)
    AppCompatImageView mAdd;

    @BindView(R2.id.bottom_navigation)
    AHBottomNavigation mBottomNavigation;

    @BindView(R2.id.view_pager)
    AHBottomNavigationViewPager mViewPager;

    @BindView(R2.id.drawer_layout)
    DrawerLayout mDrawerLayout;

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

    @OnClick(R2.id.iv_menu)
    void isOpenMenu() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}
