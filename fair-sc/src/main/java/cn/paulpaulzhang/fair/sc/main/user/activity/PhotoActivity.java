package cn.paulpaulzhang.fair.sc.main.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;

import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.ui.loader.FairLoader;
import cn.paulpaulzhang.fair.util.file.FileUtil;
import cn.paulpaulzhang.fair.util.image.ImageUtil;
import es.dmoral.toasty.Toasty;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.chat
 * 创建时间: 8/16/2019
 * 创建人: zlm31
 * 描述:
 */
public class PhotoActivity extends FairActivity {

    @BindView(R2.id.photo_view)
    PhotoView mPhotoView;

    @BindView(R2.id.button)
    MaterialButton mButton;

    @Override
    public int setLayout() {
        return R.layout.activity_photo;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void init(@Nullable Bundle savedInstanceState) {

        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Fade());

        ImmersionBar.with(this).init();
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

        if (path != null) {
            Glide.with(this).load(path).into(mPhotoView);
        }

        mButton.setVisibility(View.GONE);

        mPhotoView.setOnClickListener(v -> finish());

        mPhotoView.setOnLongClickListener(view -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("保存图片")
                    .setMessage("点击确认保存图片到本地")
                    .setPositiveButton("确认", (dialogInterface, i14) -> {
                        final Bitmap bitmap = Bitmap.createBitmap(mPhotoView.getWidth(), mPhotoView.getHeight(), Bitmap.Config.ARGB_8888, true);
                        ImageUtil.convertLayoutToBitmap(getWindow(), mPhotoView, bitmap, i15 -> {
                            if (i15 == PixelCopy.SUCCESS) {
                                File file = FileUtil.saveBitmap(bitmap, "FairSchool", 100);
                                if (file != null) {
                                    dialogInterface.dismiss();
                                    Toasty.success(PhotoActivity.this, file.getName() + "已保存", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toasty.error(PhotoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    })
                    .setNegativeButton("取消", (dialogInterface, i13) -> dialogInterface.cancel())
                    .show();
            return true;
        });
    }
}
