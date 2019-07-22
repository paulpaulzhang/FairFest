package cn.paulpaulzhang.fair.sc.main.interest;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import butterknife.BindView;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.constant.Constant;
import cn.paulpaulzhang.fair.sc.main.interest.discovery.DiscoveryDelegate;
import cn.paulpaulzhang.fair.sc.main.interest.TabViewPagerAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.follow.FollowDelegate;
import cn.paulpaulzhang.fair.sc.main.interest.topic.TopicDelegate;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/8/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class InterestDelegate extends FairDelegate implements
        OnTabSelectListener, ViewPager.OnPageChangeListener {

    @BindView(R2.id.vp_interest)
    ViewPager mViewPager;

    @BindView(R2.id.tl_interest)
    SlidingTabLayout mTabLayout;

    private Toolbar mToolbar;
    private TabViewPagerAdapter mAdapter;
    private FairDelegate currentDelegate;
    private int lastPosition = 0;

    @Override
    public Object setLayout() {
        return R.layout.delegate_interest;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        initToolbar();
        initTab();
    }

    private void initToolbar() {
        AppCompatActivity mActivity = (AppCompatActivity) getActivity();
        if (mActivity != null) {
            mToolbar = mActivity.findViewById(R.id.toolbar);
            mActivity.setSupportActionBar(mToolbar);
        }
    }

    private void initTab() {
        String[] titles = new String[]{
                getString(R.string.follow),
                getString(R.string.discovery),
                getString(R.string.topic)};

        mAdapter = new TabViewPagerAdapter
                (getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setViewPager(mViewPager, titles);
        mTabLayout.setCurrentTab(0);
        mTabLayout.setOnTabSelectListener(this);
        mTabLayout.getTitleView(0).setTextSize(18);
        mTabLayout.getTitleView(0).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    @Override
    public void onTabSelect(int position) {
        mTabLayout.getTitleView(position).setTextSize(18);
        mTabLayout.getTitleView(position).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        if (lastPosition != position) {
            mTabLayout.getTitleView(lastPosition).setTextSize(16);
            mTabLayout.getTitleView(lastPosition).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        lastPosition = position;
    }

    //双击刷新
    private long time = 0;

    @Override
    public void onTabReselect(int position) {
        if (position == lastPosition && (System.currentTimeMillis() - time < 2000)) {
            if (position == 0) {
                FollowDelegate fragment = (FollowDelegate) getChildFragmentManager().getFragments().get(position);
                @SuppressLint("InflateParams") View view = fragment.getView();
                SwipeRefreshLayout swipeRefreshLayout;
                if (view != null) {
                    swipeRefreshLayout = view.findViewById(R.id.srl_follow);
                    swipeRefreshLayout.setRefreshing(true);
                    fragment.loadData(Constant.REFRESH_DATA);
                }
            } else if (position == 1) {
                DiscoveryDelegate fragment = (DiscoveryDelegate) getChildFragmentManager().getFragments().get(position);
                @SuppressLint("InflateParams") View view = fragment.getView();
                SwipeRefreshLayout swipeRefreshLayout;
                if (view != null) {
                    swipeRefreshLayout = view.findViewById(R.id.srl_discovery);
                    swipeRefreshLayout.setRefreshing(true);
                    fragment.loadData(Constant.REFRESH_DATA);
                }
            } else if (position == 2) {
                TopicDelegate fragment = (TopicDelegate) getChildFragmentManager().getFragments().get(position);
                @SuppressLint("InflateParams") View view = fragment.getView();
                SwipeRefreshLayout swipeRefreshLayout;
                if (view != null) {
                    swipeRefreshLayout = view.findViewById(R.id.srl_topic);
                    swipeRefreshLayout.setRefreshing(true);
                    fragment.loadData(Constant.REFRESH_DATA);
                }
            }
        } else {
            time = System.currentTimeMillis();
        }
        lastPosition = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTabLayout.getTitleView(position).setTextSize(18);
        mTabLayout.getTitleView(position).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        if (lastPosition != position) {
            mTabLayout.getTitleView(lastPosition).setTextSize(16);
            mTabLayout.getTitleView(lastPosition).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        lastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
