package cn.paulpaulzhang.fair.sc.main.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.listener.AppBarStateChangeListener;
import cn.paulpaulzhang.fair.sc.main.nineimage.NineAdapter;
import cn.paulpaulzhang.fair.ui.view.MyGridView;
import cn.paulpaulzhang.fair.util.common.CommonUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/15/2019
 * 创建人: zlm31
 * 描述:
 */
public class ArticleActivity extends FairActivity implements View.OnClickListener {

    @BindView(R2.id.tb_article)
    MaterialToolbar mToolbar;

    @BindView(R2.id.civ_user_article)
    CircleImageView mAvatar;

    @BindView(R2.id.tv_username_article)
    AppCompatTextView mUsername;

    @BindView(R2.id.tv_time_article)
    AppCompatTextView mTime;

    @BindView(R2.id.tv_device_article)
    AppCompatTextView mDevice;

    @BindView(R2.id.btn_follow)
    MaterialButton mFollowButton;

    @BindView(R2.id.tv_content_article)
    AppCompatTextView mContent;

    @BindView(R2.id.gv_images_article)
    MyGridView mGridView;

    @BindView(R2.id.tab_article)
    SlidingTabLayout mTabLayout;

    @BindView(R2.id.vp_article)
    ViewPager mViewPager;

    @BindView(R2.id.ll_edit)
    LinearLayout mEdit;

    @BindView(R2.id.iv_like)
    AppCompatImageView mLike;

    @BindView(R2.id.iv_collect)
    AppCompatImageView mCollect;

    @BindView(R2.id.iv_share)
    AppCompatImageView mShare;

    @BindView(R2.id.ctl_article)
    CollapsingToolbarLayout mCollapsing;

    @BindView(R2.id.app_bar)
    AppBarLayout mAppBar;

    @BindView(R2.id.tv_title)
    AppCompatTextView mTitle;

    @Override
    public int setLayout() {
        return R.layout.activity_article;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);

        ImmersionBar.with(this).titleBar(mToolbar).init();

        Toasty.info(this, id + "", Toasty.LENGTH_SHORT).show();

        initToolbar(mToolbar);
        initTab();
        initCollapsing();
    }

    private void initCollapsing() {
        mUsername.setText("PaulPaulZhang");
        mTime.setText("2019-1-1");
        mDevice.setText("MI CC 9");
        mContent.setText("你猜猜");
        mTitle.setText("文章");

        List<String> list = new ArrayList<>();
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563891473309&di=d4702b7ea26cd7b17ba45920f5d27f46&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170506%2F0054524r5-2.jpg");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-oi9W7QkQ4yu3xHUYOngWam_JckiY4ic0SeESz8oGjbXvLDEH");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563891473309&di=d4702b7ea26cd7b17ba45920f5d27f46&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170506%2F0054524r5-2.jpg");
        mGridView.setAdapter(new NineAdapter(list, this));

        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    mToolbar.setTitle("");
                    mTitle.setVisibility(View.VISIBLE);
                } else if (state == State.COLLAPSED) {
                    mToolbar.setTitle(mTitle.getText());
                    mTitle.setVisibility(View.GONE);
                    mCollapsing.setContentScrimColor(getColor(R.color.colorAccent));
                } else {
                    mCollapsing.setContentScrimColor(getColor(android.R.color.transparent));
                    mCollapsing.setStatusBarScrimColor(getColor(android.R.color.transparent));
                }
            }
        });
    }

    private void initTab() {
        String[] titles = new String[]{getString(R.string.like), getString(R.string.comment), getString(R.string.share)};
        ArrayList<Fragment> delegates = new ArrayList<>();
        delegates.add(new LikeDelegate());
        delegates.add(new CommentDelegate());
        delegates.add(new ShareDelegate());
        mTabLayout.setViewPager(mViewPager, titles, this, delegates);
        mViewPager.setCurrentItem(1);
        mTabLayout.setCurrentTab(1);
        mViewPager.setOffscreenPageLimit(1);
    }

    @OnClick(R2.id.ll_edit)
    void openBottomDialog() {
        initBottomDialog();
    }

    @OnClick(R2.id.iv_like)
    void doLike() {

    }

    @OnClick(R2.id.iv_collect)
    void doCollect() {

    }

    @OnClick(R2.id.iv_share)
    void doShare() {

    }

    private void initBottomDialog() {
        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.view_edit_bottom_dialog,
                null, false, true, false, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);

        customerView.findViewById(R.id.iv_mention).setOnClickListener(this);
        customerView.findViewById(R.id.iv_topic).setOnClickListener(this);
        customerView.findViewById(R.id.iv_send).setOnClickListener(this);
        AppCompatEditText mEditText = customerView.findViewById(R.id.et_edit);
        new Handler().postDelayed(() -> CommonUtil.showKeyboard(mEditText), 10);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_mention) {

        } else if (view.getId() == R.id.iv_topic) {

        } else if (view.getId() == R.id.iv_send) {

        }
    }
}
