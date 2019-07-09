package cn.paulpaulzhang.fair.sc.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import butterknife.BindView;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.interest.TabViewPagerAdapter;

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

    private TabViewPagerAdapter mAdapter;
    private FairDelegate currentDelegate;
    private int lastPosition = 0;

    @Override
    public Object setLayout() {
        return R.layout.delegate_interest;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        initTab();
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

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
        //TODO 刷新逻辑
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
