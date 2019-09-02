package cn.paulpaulzhang.fair.sc.main.post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.sunhapper.x.spedit.SpUtil;
import com.sunhapper.x.spedit.view.SpXEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicCache;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.listener.AppBarStateChangeListener;
import cn.paulpaulzhang.fair.sc.listener.IMentionTopicListener;
import cn.paulpaulzhang.fair.sc.main.data.MentionTopic;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TopicAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Topic;
import cn.paulpaulzhang.fair.sc.main.nineimage.NineAdapter;
import cn.paulpaulzhang.fair.sc.main.post.adapter.ViewPagerAdapter;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.ui.view.MyGridView;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.keyboard.KeyBoardUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import cn.paulpaulzhang.fair.util.text.TextUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/15/2019
 * 创建人: zlm31
 * 描述:
 */
public class ArticleActivity extends PostActivity implements IMentionTopicListener {

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

    @Override
    public void initHeader() {
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
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setViewPager(mViewPager, titles);
        mTabLayout.setCurrentTab(1);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(3);
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
                        mContent.setContent(TextUtil.textHightLightTopic(content));
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
                        JSONObject user = object.getJSONObject("user");
                        String avatar = user.getString("avatar");
                        String username = user.getString("username");
                        Glide.with(this)
                                .load(avatar == null ? Constant.DEFAULT_AVATAR : avatar)
                                .into(mAvatar);
                        mUsername.setText(username == null ? String.valueOf(uid) : username);
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
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_comment_bottom,
                null, false, true, false, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        SpXEditText mComment = customerView.findViewById(R.id.et_edit);

        customerView.findViewById(R.id.iv_mention).setOnClickListener(v -> {

        });
        customerView.findViewById(R.id.iv_topic).setOnClickListener(v -> {
            Box<TopicCache> topicCacheBox = ObjectBox.get().boxFor(TopicCache.class);
            List<Topic> topics = new ArrayList<>();
            for (TopicCache cache : topicCacheBox.getAll()) {
                topics.add(new Topic(cache));
            }
            MaterialDialog topicDialog = new MaterialDialog(this, new BottomSheet());
            DialogCustomViewExtKt.customView(topicDialog, R.layout.dialog_topic_list,
                    null, false, false, false, true);
            View topicView = DialogCustomViewExtKt.getCustomView(topicDialog);
            topicDialog.cornerRadius(8f, null);
            RecyclerView recyclerView = topicView.findViewById(R.id.rv_topic_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            TopicAdapter mAdapter = new TopicAdapter(R.layout.item_topic, topics);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                Topic topic = (Topic) adapter.getItem(position);
                if (topic != null) {
                    String tname = topic.getTopicCache().getName();
                    SpUtil.insertSpannableString(mComment.getEditableText(),
                            new MentionTopic(tname, topic.getTopicCache().getId(), this).getSpannableString());
                    topicIdList.add(topic.getTopicCache().getId());
                }
                topicDialog.dismiss();
            });
            topicDialog.show();
        });
        customerView.findViewById(R.id.iv_send).setOnClickListener(v -> {
            if (Objects.requireNonNull(mComment.getText()).toString().isEmpty()) {
                return;
            }
            RestClient.builder()
                    .url(Api.ADD_COMMENT)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("pid", pid)
                    .params("content", mComment.getText().toString())
                    .params("imagesUrl", "")
                    .success(response -> {
                        String result = JSON.parseObject(response).getString("result");
                        if (!TextUtils.equals(result, "ok")) {
                            Toasty.error(this, "发表失败", Toasty.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                        }
                    })
                    .error((code, msg) -> Toasty.error(this, "发表失败 " + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .post();

        });
        new Handler().postDelayed(() -> KeyBoardUtil.showKeyboard(mComment), 10);
    }

    @Override
    public void removeTag(long id) {
        for (int i = 0; i < topicIdList.size(); i++) {
            if (topicIdList.get(i) == id) {
                topicIdList.remove(i);
                return;
            }
        }
    }
}
