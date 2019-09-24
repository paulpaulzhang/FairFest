package cn.paulpaulzhang.fair.sc.main.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.sunhapper.x.spedit.SpUtil;
import com.sunhapper.x.spedit.view.SpXEditText;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicCache;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.file.IUploadFileListener;
import cn.paulpaulzhang.fair.sc.file.UploadUtil;
import cn.paulpaulzhang.fair.sc.listener.IMentionTopicListener;
import cn.paulpaulzhang.fair.sc.main.data.MentionTopic;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TopicAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Topic;
import cn.paulpaulzhang.fair.ui.loader.FairLoader;
import cn.paulpaulzhang.fair.util.image.Glide4Engine;
import cn.paulpaulzhang.fair.util.keyboard.KeyBoardUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.common
 * 创建时间：9/21/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class PostCommentUtil implements IMentionTopicListener {
    private static final PostCommentUtil util = new PostCommentUtil();

    private List<Long> topicIdList = new ArrayList<>();

    public static PostCommentUtil INSTANCE() {
        return util;
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

    private AppCompatImageView mImgShow;
    private AppCompatImageView mDelete;
    private File imgFile;

    public void comment(long pid, AppCompatActivity activity, Context context, Fragment fragment) {
        MaterialDialog dialog = new MaterialDialog(Objects.requireNonNull(context), new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_comment_bottom,
                null, false, true, false, true);
        LifecycleExtKt.lifecycleOwner(dialog, activity);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        SpXEditText mComment = customerView.findViewById(R.id.et_edit);

        mImgShow = customerView.findViewById(R.id.image_view);
        mDelete = customerView.findViewById(R.id.iv_delete);

        mImgShow.setOnClickListener(v -> {
            if (imgFile != null) {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("path", imgFile.getPath());
                //noinspection unchecked
                context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
            }
        });

        mDelete.setOnClickListener(v -> {
            mImgShow.setVisibility(View.GONE);
            imgFile = null;
            mDelete.setVisibility(View.GONE);
        });

        customerView.findViewById(R.id.iv_mention).setOnClickListener(v -> {

        });

        customerView.findViewById(R.id.iv_img).setOnClickListener(v -> openAlbum(context, activity, fragment));

        customerView.findViewById(R.id.iv_topic).setOnClickListener(v -> {
            Box<TopicCache> topicCacheBox = ObjectBox.get().boxFor(TopicCache.class);
            List<Topic> topics = new ArrayList<>();
            for (TopicCache cache : topicCacheBox.getAll()) {
                topics.add(new Topic(cache));
            }
            MaterialDialog topicDialog = new MaterialDialog(context, new BottomSheet());
            DialogCustomViewExtKt.customView(topicDialog, R.layout.dialog_topic_list,
                    null, false, false, false, true);
            View topicView = DialogCustomViewExtKt.getCustomView(topicDialog);
            topicDialog.cornerRadius(8f, null);
            RecyclerView recyclerView = topicView.findViewById(R.id.rv_topic_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
            FairLoader.showLoading(context);
            util.uploadFile(activity, files, new IUploadFileListener() {
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
                                String result = JSON.parseObject(response).getString("result");
                                imgFile = null;
                                topicIdList.clear();
                                if (!TextUtils.equals(result, "ok")) {
                                    Toasty.error(context, "发表失败", Toasty.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                }
                            })
                            .error((code, msg) -> {
                                Toasty.error(context, "发表失败 " + code, Toasty.LENGTH_SHORT).show();
                                FairLoader.stopLoading();
                                imgFile = null;
                                topicIdList.clear();
                            })
                            .build()
                            .post();
                }

                @Override
                public void onError() {
                    FairLoader.stopLoading();
                    Toasty.error(context, "发表失败 ", Toasty.LENGTH_SHORT).show();
                }
            });

        });
        new Handler().postDelayed(() -> KeyBoardUtil.showKeyboard(mComment), 10);
    }

    private void openAlbum(Context context, AppCompatActivity activity, Fragment fragment) {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            Matisse matisse;
            if (fragment == null) {
                matisse = Matisse.from(activity);
            } else {
                matisse = Matisse.from(fragment);
            }
            matisse.choose(MimeType.ofImage())
                    .maxSelectable(1)
                    .countable(true)
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true, "cn.paulpaulzhang.fairfest.fileprovider"))
                    .gridExpectedSize(context.getResources()
                            .getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .theme(R.style.Matisse_Zhihu_Custom)
                    .imageEngine(new Glide4Engine())
                    .forResult(Constant.REQUEST_CODE_CHOOSE);
        } else {
            EasyPermissions.requestPermissions(activity, "打开图库需要存储读取权限", 1001,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        }
    }

    public void compressPhoto(Uri uri, Context context, AppCompatActivity activity) {
        Luban.with(context)
                .load(uri)
                .ignoreBy(0)
                .setTargetDir(Objects.requireNonNull(activity.getExternalCacheDir()).getAbsolutePath())
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        imgFile = file;
                        mImgShow.setVisibility(View.VISIBLE);
                        Glide.with(context).load(file).into(mImgShow);
                        mDelete.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(context, "加载失败", Toasty.LENGTH_SHORT).show();
                    }
                }).launch();
    }
}
