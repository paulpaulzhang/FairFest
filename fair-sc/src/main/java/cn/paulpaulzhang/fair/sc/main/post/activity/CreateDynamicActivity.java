package cn.paulpaulzhang.fair.sc.main.post.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Constant;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.file.IUploadFileListener;
import cn.paulpaulzhang.fair.sc.file.UploadUtil;
import cn.paulpaulzhang.fair.sc.main.post.model.Image;
import cn.paulpaulzhang.fair.sc.main.post.adapter.ImagePickerAdapter;
import cn.paulpaulzhang.fair.ui.loader.FairLoader;
import cn.paulpaulzhang.fair.util.common.CommonUtil;

import cn.paulpaulzhang.fair.util.image.Glide4Engine;
import cn.paulpaulzhang.fair.util.log.FairLogger;

import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/28/2019
 * 创建人: zlm31
 * 描述:
 */
public class CreateDynamicActivity extends FairActivity {

    @BindView(R2.id.et_edit)
    AppCompatEditText mEdit;

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


    @Override
    public int setLayout() {
        return R.layout.activity_create_dynamic;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initToolbar(mToolbar);
        initImagePicker();
        CommonUtil.showKeyboard(mEdit);
    }

    private void initImagePicker() {
        mAdapter = new ImagePickerAdapter(R.layout.view_image_picker_item, new ArrayList<>());
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
    public boolean onOptionsItemSelected(MenuItem item) {
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

    }

    @OnClick(R2.id.iv_mention)
    void openFriendDialog() {

    }

    @OnClick(R2.id.iv_send)
    void doSend() {
        FairLoader.showLoading(this);
        List<File> files = new ArrayList<>();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            files.add(mAdapter.getData().get(i).getFile());
        }
        UploadUtil util = UploadUtil.INSTANCE();
        util.uploadFile(this, files, new IUploadFileListener() {
            @Override
            public void onSuccess(List<String> paths) {
                paths.forEach(FairLogger::d);
                FairLoader.stopLoading();
                FairLogger.d("success");
            }

            @Override
            public void onError() {
                FairLogger.d("error");
                FairLoader.stopLoading();
            }
        });
    }

}
