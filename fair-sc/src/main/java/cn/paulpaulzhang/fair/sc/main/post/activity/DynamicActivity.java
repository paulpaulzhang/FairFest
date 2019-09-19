package cn.paulpaulzhang.fair.sc.main.post.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityOptionsCompat;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.gyf.immersionbar.ImmersionBar;
import com.sunhapper.x.spedit.SpUtil;
import com.sunhapper.x.spedit.view.SpXEditText;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
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
import cn.paulpaulzhang.fair.sc.file.IUploadFileListener;
import cn.paulpaulzhang.fair.sc.file.UploadUtil;
import cn.paulpaulzhang.fair.sc.listener.IMentionTopicListener;
import cn.paulpaulzhang.fair.sc.main.common.PhotoActivity;
import cn.paulpaulzhang.fair.sc.main.data.MentionTopic;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TopicAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Topic;
import cn.paulpaulzhang.fair.sc.main.nineimage.NineAdapter;
import cn.paulpaulzhang.fair.sc.main.post.adapter.ViewPagerAdapter;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.ui.loader.FairLoader;
import cn.paulpaulzhang.fair.ui.view.MyGridView;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.image.Glide4Engine;
import cn.paulpaulzhang.fair.util.keyboard.KeyBoardUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import cn.paulpaulzhang.fair.util.text.TextUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/15/2019
 * 创建人: zlm31
 * 描述:
 */
public class DynamicActivity extends PostActivity implements IMentionTopicListener {

    @BindView(R2.id.tb_dynamic)
    MaterialToolbar mToolbar;

    @BindView(R2.id.civ_user_dynamic)
    CircleImageView mAvatar;

    @BindView(R2.id.tv_username_dynamic)
    AppCompatTextView mUsername;

    @BindView(R2.id.tv_time_dynamic)
    AppCompatTextView mTime;

    @BindView(R2.id.tv_device_dynamic)
    AppCompatTextView mDevice;

    @BindView(R2.id.btn_follow)
    MaterialButton mFollowButton;

    @BindView(R2.id.tv_content_dynamic)
    ExpandableTextView mContent;

    @BindView(R2.id.gv_images_dynamic)
    MyGridView mGridView;

    @BindView(R2.id.tab_dynamic)
    SlidingTabLayout mTabLayout;

    @BindView(R2.id.vp_dynamic)
    ViewPager mViewPager;

    @BindView(R2.id.ll_edit)
    LinearLayout mEdit;

    @BindView(R2.id.iv_like)
    AppCompatImageView mLike;

    @BindView(R2.id.iv_collect)
    AppCompatImageView mCollect;

    @BindView(R2.id.iv_share)
    AppCompatImageView mShare;


    @Override
    public int setLayout() {
        return R.layout.activity_dynamic;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        pid = intent.getLongExtra("pid", -1);
        uid = intent.getLongExtra("uid", -1);
        isLike = intent.getBooleanExtra("isLike", false);

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
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
                        String content = object.getJSONObject("post").getString("content");
                        String images = object.getJSONObject("post").getJSONObject("imagesUrl").toJSONString();
                        String device = object.getJSONObject("post").getString("device");
                        long time = object.getJSONObject("post").getLongValue("time");
                        int likeCount = object.getJSONObject("post").getIntValue("likeCount");
                        int commentCount = object.getJSONObject("post").getIntValue("commentCount");
                        int shareCount = object.getJSONObject("post").getIntValue("shareCount");

                        List<String> imgUrls = JsonParseUtil.parseImgs(images);
                        mContent.setContent(TextUtil.textHightLightTopic(content));
                        mGridView.setAdapter(new NineAdapter(imgUrls, this));
                        mTime.setText(DateUtil.getTime(time));
                        mDevice.setText(device);

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

    private AppCompatImageView mImgShow;
    private AppCompatImageView mDelete;
    private File imgFile;

    private void initBottomDialog() {
        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_comment_bottom,
                null, false, true, false, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        SpXEditText mComment = customerView.findViewById(R.id.et_edit);

        mImgShow = customerView.findViewById(R.id.image_view);
        mDelete = customerView.findViewById(R.id.iv_delete);

        mImgShow.setOnClickListener(v -> {
            if (imgFile != null) {
                Intent intent = new Intent(DynamicActivity.this, PhotoActivity.class);
                intent.putExtra("path", imgFile.getPath());
                //noinspection unchecked
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
            }
        });

        mDelete.setOnClickListener(v -> {
            mImgShow.setVisibility(View.GONE);
            imgFile = null;
            mDelete.setVisibility(View.GONE);
        });

        customerView.findViewById(R.id.iv_mention).setOnClickListener(v -> {

        });

        customerView.findViewById(R.id.iv_img).setOnClickListener(v -> openAlbum());

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
            if (Objects.requireNonNull(mComment.getText()).toString().isEmpty() && imgFile == null) {
                return;
            }
            UploadUtil util = UploadUtil.INSTANCE();
            List<File> files = new ArrayList<>();
            if (imgFile != null) {
                files.add(imgFile);
            }
            FairLoader.showLoading(DynamicActivity.this);
            util.uploadFile(DynamicActivity.this, files, new IUploadFileListener() {
                @Override
                public void onSuccess(List<String> paths) {
                    RestClient.builder()
                            .url(Api.ADD_COMMENT)
                            .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                            .params("pid", pid)
                            .params("content", mComment.getText().toString())
                            .params("imagesUrl", paths.size() == 0 ? "" : paths.get(0))
                            .success(response -> {
                                FairLoader.stopLoading();
                                FairLogger.d("IMG" + (paths.size() == 0 ? "" : paths.get(0)));
                                String result = JSON.parseObject(response).getString("result");
                                if (!TextUtils.equals(result, "ok")) {
                                    Toasty.error(DynamicActivity.this, "发表失败", Toasty.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                }
                            })
                            .error((code, msg) -> {
                                Toasty.error(DynamicActivity.this, "发表失败 " + code, Toasty.LENGTH_SHORT).show();
                                FairLoader.stopLoading();
                            })
                            .build()
                            .post();
                }

                @Override
                public void onError() {
                    FairLoader.stopLoading();
                    Toasty.error(DynamicActivity.this, "发表失败 ", Toasty.LENGTH_SHORT).show();
                }
            });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            compressPhoto(Matisse.obtainResult(data).get(0));
        }
    }

    private void openAlbum() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .maxSelectable(1)
                    .countable(true)
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true, "cn.paulpaulzhang.fairfest.fileprovider"))
                    .gridExpectedSize(getResources()
                            .getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .theme(R.style.Matisse_Zhihu_Custom)
                    .imageEngine(new Glide4Engine())
                    .forResult(Constant.REQUEST_CODE_CHOOSE);
        } else {
            EasyPermissions.requestPermissions(this, "打开图库需要存储读取权限", 1001,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        }
    }

    private void compressPhoto(Uri uri) {
        Luban.with(this)
                .load(uri)
                .ignoreBy(0)
                .setTargetDir(Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath())
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        imgFile = file;
                        mImgShow.setVisibility(View.VISIBLE);
                        Glide.with(DynamicActivity.this).load(file).into(mImgShow);
                        mDelete.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(DynamicActivity.this, "加载失败", Toasty.LENGTH_SHORT).show();
                    }
                }).launch();
    }


}
