package cn.paulpaulzhang.fair.sc.main.market.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gyf.immersionbar.ImmersionBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.file.IUploadFileListener;
import cn.paulpaulzhang.fair.sc.file.UploadUtil;
import cn.paulpaulzhang.fair.ui.loader.FairLoader;
import cn.paulpaulzhang.fair.util.image.Glide4Engine;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.market.activity
 * 创建时间：9/4/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class PublishActivity extends FairActivity {

    @BindView(R2.id.fab)
    FloatingActionButton fab;

    @BindView(R2.id.iv_add)
    AppCompatImageView mAdd;

    @BindView(R2.id.iv_delete)
    AppCompatImageView mDelete;

    @BindView(R2.id.et_overview)
    AppCompatEditText mOverview;

    @BindView(R2.id.et_name)
    AppCompatEditText mName;

    @BindView(R2.id.et_price)
    AppCompatEditText mPrice;

    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    private File imgFile = null;

    @Override
    public int setLayout() {
        return R.layout.activity_publish;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initToolbar(mToolbar, getString(R.string.my_free));
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        fab.setOnClickListener(v -> {
            String overview = Objects.requireNonNull(mOverview.getText()).toString().trim();
            String price = Objects.requireNonNull(mPrice.getText()).toString().trim();
            String name = Objects.requireNonNull(mName.getText()).toString().trim();

            if (overview.isEmpty() || price.isEmpty() || name.isEmpty()) {
                Toasty.info(this, "商品信息不能为空", Toasty.LENGTH_SHORT).show();
                return;
            }
            try {
                Float.valueOf(price);
            } catch (Exception e) {
                Toasty.info(this, "价格输入有误", Toasty.LENGTH_SHORT).show();
                return;
            }
            if (imgFile == null) {
                Toasty.info(this, "商品图片不能为空", Toasty.LENGTH_SHORT).show();
                return;
            }
            FairLoader.showLoading(this);
            UploadUtil util = UploadUtil.INSTANCE();
            List<File> files = new ArrayList<>();
            files.add(imgFile);
            util.uploadFile(this, files, new IUploadFileListener() {
                @Override
                public void onSuccess(List<String> paths) {
                    RestClient.builder()
                            .url(Api.ADD_STORE)
                            .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                            .params("headImg", paths.get(0))
                            .params("sname", name)
                            .params("overview", overview)
                            .params("price", Float.valueOf(price))
                            .success(response -> {
                                String result = JSON.parseObject(response).getString("result");
                                if (TextUtils.equals(result, "ok")) {
                                    Toasty.success(PublishActivity.this, "发布成功", Toasty.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toasty.success(PublishActivity.this, "发布失败", Toasty.LENGTH_SHORT).show();
                                }
                            })
                            .error((code, msg) -> Toasty.success(PublishActivity.this, "发布失败", Toasty.LENGTH_SHORT).show())
                            .build()
                            .post();
                }

                @Override
                public void onError() {
                    FairLoader.stopLoading();
                    Toasty.error(PublishActivity.this, "发布失败", Toasty.LENGTH_SHORT).show();
                }
            });

        });

        mDelete.setOnClickListener(v -> {
            if (imgFile != null) {
                imgFile = null;
                mAdd.setImageResource(R.drawable.ic_img_picker_add);
                mDelete.setVisibility(View.GONE);
            }
        });

        mAdd.setOnClickListener(v -> openAlbum());
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
                        mDelete.setVisibility(View.VISIBLE);
                        Glide.with(PublishActivity.this).load(file).into(mAdd);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(PublishActivity.this, "加载失败", Toasty.LENGTH_SHORT).show();
                    }
                }).launch();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
