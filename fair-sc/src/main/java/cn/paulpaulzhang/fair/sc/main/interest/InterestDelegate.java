package cn.paulpaulzhang.fair.sc.main.interest;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.constant.Constant;

import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.main.post.activity.CreateArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.CreateDynamicActivity;
import cn.paulpaulzhang.fair.sc.main.search.SearchActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import io.objectbox.Box;

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

    @BindView(R2.id.civ_user)
    CircleImageView mUser;

    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    @BindView(R2.id.ll_search)
    LinearLayout mSearch;

    private TabViewPagerAdapter mAdapter;
    private int lastPosition = 1;

    @Override
    public Object setLayout() {
        return R.layout.delegate_interest;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        setHasOptionsMenu(true);
        mToolbar.inflateMenu(R.menu.menu_interest);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add) {
                initBottomDialog();
            }
            return true;
        });

        mSearch.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle()));
        initTab();
        loadUser();
    }

    private void initTab() {
        String[] titles = new String[]{
                getString(R.string.follow),
                getString(R.string.discovery),
                getString(R.string.topic)};

        mAdapter = new TabViewPagerAdapter
                (getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mAdapter);

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setViewPager(mViewPager, titles);
        mTabLayout.setCurrentTab(1);
        mViewPager.setCurrentItem(1);
        mTabLayout.setOnTabSelectListener(this);
        mTabLayout.getTitleView(1).setTextSize(18);
        mTabLayout.getTitleView(1).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private void loadUser() {
        Box<User> userBox = ObjectBox.get().boxFor(User.class);
        String avatar = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())).getAvatar();
        Glide.with(this).load(avatar == null ? Constant.DEFAULT_AVATAR : avatar).into(mUser);
    }

    @OnClick(R2.id.civ_user)
    void user() {
        Intent intent = new Intent(getContext(), UserCenterActivity.class);
        intent.putExtra("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        startActivity(intent);
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

    @OnClick(R2.id.ll_search)
    void openSearchBar() {

    }

    @OnClick(R2.id.civ_user)
    void openUserCenter() {

    }

    private void initBottomDialog() {
        MaterialDialog dialog = new MaterialDialog(Objects.requireNonNull(getContext()), new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_create_bottom,
                null, false, true, false, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);

        customerView.findViewById(R.id.iv_dynamic).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreateDynamicActivity.class));
            dialog.dismiss();
        });
        customerView.findViewById(R.id.iv_article).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreateArticleActivity.class));
            dialog.dismiss();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
    }
}
