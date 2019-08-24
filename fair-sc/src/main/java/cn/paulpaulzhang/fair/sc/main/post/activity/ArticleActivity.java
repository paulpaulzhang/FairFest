package cn.paulpaulzhang.fair.sc.main.post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.ctetin.expandabletextviewlibrary.app.LinkType;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.gyf.immersionbar.ImmersionBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.listener.AppBarStateChangeListener;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.nineimage.NineAdapter;
import cn.paulpaulzhang.fair.sc.main.post.delegate.CommentDelegate;
import cn.paulpaulzhang.fair.sc.main.post.delegate.LikeDelegate;
import cn.paulpaulzhang.fair.sc.main.post.delegate.ShareDelegate;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.ui.view.MyGridView;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.keyboard.KeyBoardUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import cn.paulpaulzhang.fair.util.text.TextUtil;
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
    ExpandableTextView mContent;

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

    @BindView(R2.id.iv_title_bg)
    AppCompatImageView mTopBg;

    private long pid;
    private long uid;
    private boolean isLike;

    @Override
    public int setLayout() {
        return R.layout.activity_article;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        pid = intent.getLongExtra("pid", -1);
        uid = intent.getLongExtra("uid", -1);
        isLike = intent.getBooleanExtra("isLike", false);

        ImmersionBar.with(this)
                .titleBar(mToolbar)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();

        initToolbar(mToolbar);
        initTab();
        initHeader();
        initBottomMenu();
    }

    private void initHeader() {
        requestData();

        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    mToolbar.setTitle("");
                    mTitle.setVisibility(View.VISIBLE);
                } else if (state == State.COLLAPSED) {
                    mToolbar.setTitle(mTitle.getText());
                    mTitle.setVisibility(View.GONE);
                    mCollapsing.setContentScrimColor(getColor(android.R.color.white));
                } else {
                    mCollapsing.setContentScrimColor(getColor(android.R.color.transparent));
                    mCollapsing.setStatusBarScrimColor(getColor(android.R.color.transparent));
                }
            }
        });

        if (uid == FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())) {
            mFollowButton.setVisibility(View.INVISIBLE);
        } else {
            RestClient.builder()
                    .url(Api.IS_PAY_USER)
                    .params("focuserId", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("focusedId", uid)
                    .success(r -> {
                        String result = JSON.parseObject(r).getString("result");
                        if (TextUtils.equals(result, "已关注")) {
                            mFollowButton.setText("已关注");
                        } else {
                            mFollowButton.setText("关注");
                        }
                    })
                    .error(((code, msg) -> FairLogger.d(code)))
                    .build()
                    .get();
        }

        mContent.setLinkClickListener((t, c, selfContent) -> {
            if (t.equals(LinkType.LINK_TYPE)) {
                Toast.makeText(this, "你点击了链接 内容是：" + c, Toast.LENGTH_SHORT).show();
            } else if (t.equals(LinkType.MENTION_TYPE)) {
                Toast.makeText(this, "你点击了@用户 内容是：" + c, Toast.LENGTH_SHORT).show();
            } else if (t.equals(LinkType.SELF)) {
                Intent intent = new Intent(this, TopicDetailActivity.class);
                intent.putExtra("name", TextUtil.getTopicName(c));
                startActivity(intent);
            }
        });

        mAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserCenterActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
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

    private void initBottomMenu() {
        if (isLike) {
            mLike.setImageResource(R.drawable.ic_liked);
        } else {
            mLike.setImageResource(R.drawable.ic_like);
        }
    }

    private void requestData() {
        RestClient.builder()
                .url(Api.GET_POST_INFO)
                .params("pid", pid)
                .success(response -> {
                    JSONObject object = JSON.parseObject(response);
                    String result = object.getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        String title = object.getJSONObject("post").getString("title");
                        String content = object.getJSONObject("post").getString("content");
                        String images = object.getJSONObject("post").getJSONObject("imagesUrl").toJSONString();
                        String device = object.getJSONObject("post").getString("device");
                        long time = object.getJSONObject("post").getLongValue("time");
                        int likeCount = object.getJSONObject("post").getIntValue("likeCount");
                        int commentCount = object.getJSONObject("post").getIntValue("commentCount");
                        int shareCount = object.getJSONObject("post").getIntValue("shareCount");

                        List<String> imgUrls = JsonParseUtil.parseImgs(images);
                        mTitle.setText(title);
                        mContent.setContent(TextUtil.text2Post(content));
                        mGridView.setAdapter(new NineAdapter(imgUrls, this));
                        mTime.setText(DateUtil.getTime(time));
                        mDevice.setText(device);

                        if (imgUrls != null && imgUrls.size() != 0) {
                            Glide.with(this).load(imgUrls.get(0)).into(mTopBg);
                        } else {
                            mTopBg.setImageResource(R.mipmap.user_background);
                        }

                        mTabLayout.getTitleView(0).setText(String.format(Locale.CHINA, "%s %d", getString(R.string.like), likeCount));
                        mTabLayout.getTitleView(1).setText(String.format(Locale.CHINA, "%s %d", getString(R.string.comment), commentCount));
                        mTabLayout.getTitleView(2).setText(String.format(Locale.CHINA, "%s %d", getString(R.string.share), shareCount));
                    } else {
                        Toasty.error(this, "请求错误，请重试", Toasty.LENGTH_SHORT).show();
                    }
                })
                .error((code, msg) -> Toasty.error(this, "请求错误，请重试 " + code, Toasty.LENGTH_SHORT).show())
                .build()
                .get();

        RestClient.builder()
                .url(Api.USER_INFO)
                .params("uid", uid)
                .success(response -> {
                    JSONObject object = JSON.parseObject(response);
                    String result = object.getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        String avatar = object.getString("avatar");
                        String username = object.getString("username");

                        Glide.with(this)
                                .load(avatar == null ? Constant.DEFAULT_AVATAR : avatar)
                                .into(mAvatar);

                        mUsername.setText(username == null ? String.valueOf(uid).substring(8) : username);
                    } else {
                        Toasty.error(this, "请求错误，请重试", Toasty.LENGTH_SHORT).show();
                    }
                })
                .error((code, msg) -> Toasty.error(this, "请求错误，请重试 " + code, Toasty.LENGTH_SHORT).show())
                .build()
                .get();
    }

    @OnClick(R2.id.ll_edit)
    void openBottomDialog() {
        initBottomDialog();
    }

    @OnClick(R2.id.btn_follow)
    void follow() {
        if (TextUtils.equals(mFollowButton.getText().toString(), "关注")) {
            RestClient.builder()
                    .url(Api.PAY_USER)
                    .params("focuserId", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("focusedId", uid)
                    .success(r -> {
                        String result = JSON.parseObject(r).getString("result");
                        if (TextUtils.equals(result, "ok")) {
                            mFollowButton.setText("已关注");
                        } else {
                            Toasty.error(this, "关注失败 ", Toasty.LENGTH_SHORT).show();
                        }
                    })
                    .error((code, msg) -> Toasty.error(this, "关注失败 " + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .post();
        } else {
            RestClient.builder()
                    .url(Api.CANCEL_PAY_USER)
                    .params("focuserId", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("focusedId", uid)
                    .success(r -> {
                        String result = JSON.parseObject(r).getString("result");
                        if (TextUtils.equals(result, "ok")) {
                            mFollowButton.setText("关注");
                        } else {
                            Toasty.error(this, "取消失败 ", Toasty.LENGTH_SHORT).show();
                        }
                    })
                    .error((code, msg) -> Toasty.error(this, "取消失败 " + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .post();
        }
    }

    @OnClick(R2.id.iv_like)
    void doLike() {
        if (!isLike) {
            RestClient.builder()
                    .url(Api.THUMBSUP_POST)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("pid", pid)
                    .success(r -> {
                        mLike.setImageResource(R.drawable.ic_liked);
                        isLike = true;
                    })
                    .error((code, msg) -> Toasty.error(this, "点赞失败 " + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .post();
        } else {
            RestClient.builder()
                    .url(Api.CANCEL_THUMBSUP_POST)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("pid", pid)
                    .success(r -> {
                        mLike.setImageResource(R.drawable.ic_like);
                        isLike = false;
                    })
                    .error((code, msg) -> Toasty.error(this, "取消失败 " + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .post();
        }
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
        new Handler().postDelayed(() -> KeyBoardUtil.showKeyboard(mEditText), 10);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
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
