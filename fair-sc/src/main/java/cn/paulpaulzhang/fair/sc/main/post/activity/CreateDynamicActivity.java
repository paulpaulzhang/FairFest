package cn.paulpaulzhang.fair.sc.main.post.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.alibaba.fastjson.JSON;
import com.google.android.material.appbar.MaterialToolbar;
import com.gyf.immersionbar.ImmersionBar;
import com.sunhapper.x.spedit.SpUtil;
import com.sunhapper.x.spedit.view.SpXEditText;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;

import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicCache;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.file.IUploadFileListener;
import cn.paulpaulzhang.fair.sc.file.UploadUtil;
import cn.paulpaulzhang.fair.sc.listener.IMentionTopicListener;
import cn.paulpaulzhang.fair.sc.main.data.MentionTopic;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TopicAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Topic;
import cn.paulpaulzhang.fair.sc.main.post.model.Image;
import cn.paulpaulzhang.fair.sc.main.post.adapter.ImagePickerAdapter;
import cn.paulpaulzhang.fair.ui.loader.FairLoader;

import cn.paulpaulzhang.fair.util.image.Glide4Engine;
import cn.paulpaulzhang.fair.util.keyboard.KeyBoardUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;

import cn.paulpaulzhang.fair.util.storage.FairPreference;
import cn.paulpaulzhang.fair.util.system.SystemUtil;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/28/2019
 * 创建人: zlm31
 * 描述:
 */
public class CreateDynamicActivity extends FairActivity implements IMentionTopicListener {

    @BindView(R2.id.et_edit)
    SpXEditText mEdit;

    @BindView(R2.id.tb_create)
    MaterialToolbar mToolbar;

    @BindView(R2.id.iv_picture)
    AppCompatImageView mPicture;

    @BindView(R2.id.iv_mention)
    AppCompatImageView mMention;

    @BindView(R2.id.iv_topic)
    AppCompatImageView mTopic;

    @BindView(R2.id.rv_image_picker)
    RecyclerView mImagePicker;

    private ImagePickerAdapter mAdapter;
    private View addView; //footer view

    private List<Long> topicIdList = new ArrayList<>();


    @Override
    public int setLayout() {
        return R.layout.activity_create_dynamic;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String initTopic = intent.getStringExtra("topic");
        long initTopicId = intent.getLongExtra("tid", -1);
        if (initTopic != null && !TextUtils.equals(initTopic, "")) {
            SpUtil.insertSpannableString(mEdit.getEditableText(),
                    new MentionTopic(initTopic, initTopicId, this).getSpannableString());
        }
        if (initTopicId != -1) {
            topicIdList.add(initTopicId);
        }
        initToolbar(mToolbar);
        initImagePicker();
        KeyBoardUtil.showKeyboard(mEdit);

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //软键盘自动弹出
                .init();
    }

    private void initImagePicker() {
        mAdapter = new ImagePickerAdapter(R.layout.item_image_picker, new ArrayList<>());
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mImagePicker.setLayoutManager(manager);
        mImagePicker.setAdapter(mAdapter);
        addView = View.inflate(this, R.layout.view_image_picker_add, null);
        addView.findViewById(R.id.iv_add).setOnClickListener(v -> openAlbum());

        mAdapter.setFooterViewAsFlow(true);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(CreateDynamicActivity.this, PhotoPreviewActivity.class);
            Image item = (Image) adapter.getItem(position);
            if (item != null) {
                intent.putExtra("path", item.getFile().getPath());
                startActivity(intent);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            adapter.remove(position);
            int size = adapter.getData().size();
            int count = adapter.getFooterLayoutCount();
            if (size < 9 && count == 0) {
                adapter.addFooterView(addView);
            }
            if (size < 1 && count == 1) {
                adapter.removeFooterView(addView);
            }
        });

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            compressPhoto(Matisse.obtainResult(data));
        }
    }

    @OnClick(R2.id.iv_picture)
    void openAlbum() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .maxSelectable(9)
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
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void compressPhoto(List<Uri> uris) {
        Luban.with(this)
                .load(uris)
                .ignoreBy(0)
                .setTargetDir(Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath())
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        int size = mAdapter.getData().size();
                        int count = mAdapter.getFooterLayoutCount();
                        if (size < 9) {
                            mAdapter.addData(new Image(file));
                            size += 1;
                        }
                        if (size < 9 && count == 0) {
                            mAdapter.addFooterView(addView);
                        }
                        if (size >= 9 && count == 1) {
                            mAdapter.removeFooterView(addView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(CreateDynamicActivity.this, "加载失败", Toasty.LENGTH_SHORT).show();
                    }
                }).launch();
    }

    @OnClick(R2.id.iv_topic)
    void openTopicDialog() {
        Box<TopicCache> topicCacheBox = ObjectBox.get().boxFor(TopicCache.class);
        List<Topic> topics = new ArrayList<>();
        for (TopicCache cache : topicCacheBox.getAll()) {
            topics.add(new Topic(cache));
        }
        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet());
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_topic_list,
                null, false, false, false, true);
        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        dialog.cornerRadius(8f, null);
        RecyclerView recyclerView = customerView.findViewById(R.id.rv_topic_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TopicAdapter mAdapter = new TopicAdapter(R.layout.item_topic, topics);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Topic topic = (Topic) adapter.getItem(position);
            if (topic != null) {
                String tname = topic.getTopicCache().getName();
                SpUtil.insertSpannableString(mEdit.getEditableText(),
                        new MentionTopic(tname, topic.getTopicCache().getId(), this).getSpannableString());
                topicIdList.add(topic.getTopicCache().getId());
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    @OnClick(R2.id.iv_mention)
    void openFriendDialog() {

    }

    @OnClick(R2.id.iv_send)
    void doSend() {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            files.add(mAdapter.getData().get(i).getFile());
        }
        if (Objects.requireNonNull(mEdit.getText()).toString().isEmpty() && files.size() == 0) {
            Toasty.info(this, "内容不能为空哦！", Toasty.LENGTH_SHORT).show();
            return;
        }
        FairLoader.showLoading(this);
        UploadUtil util = UploadUtil.INSTANCE();
        util.uploadFile(this, files, new IUploadFileListener() {
            @Override
            public void onSuccess(List<String> paths) {
                Map<String, Long> topicMap = new HashMap<>();
                for (int i = 0; i < topicIdList.size(); i++) {
                    topicMap.put("t" + i, topicIdList.get(i));
                }
                Map<String, String> imgMap = new TreeMap<>();
                for (int i = 0; i < paths.size(); i++) {
                    imgMap.put("i" + i, paths.get(i));
                }
                RestClient.builder()
                        .url(Api.SEND_POST)
                        .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                        .params("type", 0)
                        .params("title", "")
                        .params("content", Objects.requireNonNull(mEdit.getText()).toString())
                        .params("device", SystemUtil.getSystemModel())
                        .params("topic", JSON.toJSONString(topicMap))
                        .params("imagesUrl", JSON.toJSONString(imgMap))
                        .success(r -> {
                            String result = JSON.parseObject(r).getString("result");
                            FairLoader.stopLoading();
                            if (TextUtils.equals(result, "ok")) {
                                Toasty.success(CreateDynamicActivity.this, "发表成功", Toasty.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toasty.error(CreateDynamicActivity.this, "发表失败，请重试", Toasty.LENGTH_SHORT).show();
                            }
                        })
                        .error((code, msg) -> {
                            FairLoader.stopLoading();
                            Toasty.error(CreateDynamicActivity.this, "发表失败，请重试 " + code, Toasty.LENGTH_SHORT).show();
                            FairLogger.d(code);
                        })
                        .build()
                        .post();
            }

            @Override
            public void onError() {
                FairLogger.d("error");
                FairLoader.stopLoading();
            }
        });
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
