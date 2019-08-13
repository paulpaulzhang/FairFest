package cn.paulpaulzhang.fair.sc.main.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.listener.AppBarStateChangeListener;
import cn.paulpaulzhang.fair.sc.main.user.delegate.ConcernTopicDelegate;
import cn.paulpaulzhang.fair.sc.main.user.delegate.ConcernUserDelegate;
import cn.paulpaulzhang.fair.sc.main.user.delegate.DynamicDelegate;
import cn.paulpaulzhang.fair.util.common.AnimationUtils;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.header.ClassicHeader;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user
 * 创建时间: 7/12/2019
 * 创建人: zlm31
 * 描述:
 */
public class UserCenterActivity extends FairActivity {
    @BindView(R2.id.abl_user)
    AppBarLayout mAppBarLayout;

    @BindView(R2.id.ctl_user)
    CollapsingToolbarLayout mCollapsingLayout;

    @BindView(R2.id.tb_user)
    MaterialToolbar mToolbar;

    @BindView(R2.id.stl_user)
    SlidingTabLayout mTabLayout;

    @BindView(R2.id.iv_bg)
    AppCompatImageView mBackground;

    @BindView(R2.id.civ_avatar_title)
    CircleImageView mTitleAvatar;

    @BindView(R2.id.tv_name_title)
    AppCompatTextView mTitleName;

    @BindView(R2.id.vp_user)
    ViewPager mViewPager;

    @BindView(R2.id.cl_user)
    ConstraintLayout mConstraintLayout;

    @BindView(R2.id.civ_avatar)
    CircleImageView mAvatar;

    @BindView(R2.id.tv_name)
    AppCompatTextView mName;

    @BindView(R2.id.btn_edit)
    MaterialButton mEdit;

    @BindView(R2.id.btn_follow)
    MaterialButton mFollow;

    @BindView(R2.id.tv_introduction)
    AppCompatTextView mIntroduction;

    @BindView(R2.id.tv_pay_count)
    AppCompatTextView mPayCount;

    @BindView(R2.id.tv_fans_count)
    AppCompatTextView mFansCount;

    @BindView(R2.id.tv_gender_year)
    AppCompatTextView mGenderYear;

    @BindView(R2.id.tv_constellation)
    AppCompatTextView mConstellation;

    @BindView(R2.id.srl_user)
    SwipeRefreshLayout mSwipeRefresh;

    private int lastPosition = 0;
    private long uid;


    @Override
    public int setLayout() {
        return R.layout.activity_user_center;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).titleBar(mToolbar).init();

        Intent intent = getIntent();
        uid = intent.getLongExtra("uid", -1);

        initToolbar(mToolbar);
        initHeader();
        initTab();
        initSwipeRefresh();

    }

    @Override
    public void initToolbar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initHeader() {
        if (uid == FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())) {
            mEdit.setVisibility(View.VISIBLE);
            mFollow.setVisibility(View.GONE);
        } else {
            mEdit.setVisibility(View.GONE);
            mFollow.setVisibility(View.VISIBLE);
        }

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                //展开
                mConstraintLayout.setAlpha(1.0f);
                mTitleAvatar.setVisibility(View.GONE);
                mTitleName.setVisibility(View.GONE);
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                //折叠
                mTitleAvatar.setVisibility(View.VISIBLE);
                mTitleName.setVisibility(View.VISIBLE);
                mConstraintLayout.setAlpha(0.0f);
            } else {
                //中间过程
                float alpha = (float) (appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset)) /
                        appBarLayout.getTotalScrollRange();
                mConstraintLayout.setAlpha(alpha);
                mTitleAvatar.setVisibility(View.GONE);
                mTitleName.setVisibility(View.GONE);
                mBackground.setColorFilter(Color.parseColor("#" + getAlpha(Math.abs(verticalOffset)) + "000000"));
                mAvatar.setColorFilter(Color.parseColor("#" + getAlpha(Math.abs(verticalOffset)) + "000000"));
            }
        });
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> {
            mSwipeRefresh.setRefreshing(false);
        });
    }

    private void initTab() {
        String[] titles = new String[]{getString(R.string.dynamic),
                getString(R.string.concern_topic),
                getString(R.string.concern_user)};
        ArrayList<Fragment> delegates = new ArrayList<>();
        delegates.add(new DynamicDelegate());
        delegates.add(new ConcernTopicDelegate());
        delegates.add(new ConcernUserDelegate());
        mTabLayout.setViewPager(mViewPager, titles, this, delegates);
        mTabLayout.getTitleView(0).setTextSize(18);
        mTabLayout.getTitleView(0).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
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

            }
        });
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });
    }

    private String getAlpha(int i) {
        int max = 85;
        float rate = 0.15f;
        float temp;
        if (i * rate < max) {
            temp = 255 * i * rate * 1.0f / 100f;
        } else {
            temp = 255 * max * 1.0f / 100f;
        }
        int alpha = Math.round(temp);
        String hexStr = Integer.toHexString(alpha);
        if (hexStr.length() < 2)
            hexStr = "0" + hexStr;
        return hexStr.toUpperCase();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
