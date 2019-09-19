package cn.paulpaulzhang.fair.sc.main.chat;

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
import androidx.appcompat.app.AlertDialog;

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
 * 创建时间: 8/7/2019
 * 创建人: zlm31
 * 描述:
 */
public class ChatPhotoActivity extends FairActivity {

    @BindView(R2.id.photo_view)
    PhotoView mPhotoView;

    @BindView(R2.id.button)
    MaterialButton mButton;

    @Override
    public int setLayout() {
        return R.layout.activity_photo_chat;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Fade());

        ImmersionBar.with(this).init();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String appkey = intent.getStringExtra("appkey");
        int mid = intent.getIntExtra("mid", -1);

        Conversation conversation = JMessageClient.getSingleConversation(username, appkey);
        if (mid == -1) {
            Toasty.error(this, "获取图片失败", Toasty.LENGTH_SHORT).show();
            finish();
        }
        Message message = conversation.getMessage(mid);
        ImageContent content = (ImageContent) message.getContent();
        if (content.getLocalPath() == null) {
            Glide.with(this).load(content.getLocalThumbnailPath()).into(mPhotoView);
        } else {
            mButton.setVisibility(View.GONE);
            Glide.with(this).load(content.getLocalPath()).into(mPhotoView);
        }


        mButton.setOnClickListener(v -> {
            mButton.setVisibility(View.GONE);
            FairLoader.showLoading(ChatPhotoActivity.this);
            content.downloadOriginImage(message, new DownloadCompletionCallback() {
                @Override
                public void onComplete(int i, String s, File file) {
                    FairLoader.stopLoading();
                    Glide.with(ChatPhotoActivity.this).load(file.getPath()).into(mPhotoView);
                }
            });

        });

        mPhotoView.setOnClickListener(v -> finishAfterTransition());

        mPhotoView.setOnLongClickListener(view -> {
            AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                    .setTitle("保存图片")
                    .setMessage("点击确认保存图片到本地")
                    .setPositiveButton("确认", (dialogInterface, i14) -> {
                        final Bitmap bitmap = Bitmap.createBitmap(mPhotoView.getWidth(), mPhotoView.getHeight(), Bitmap.Config.ARGB_8888, true);
                        ImageUtil.convertLayoutToBitmap(getWindow(), mPhotoView, bitmap, i15 -> {
                            if (i15 == PixelCopy.SUCCESS) {
                                File file = FileUtil.saveBitmap(bitmap, "FairSchool", 100);
                                if (file != null) {
                                    dialogInterface.dismiss();
                                    Toasty.success(ChatPhotoActivity.this, file.getName() + "已保存", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toasty.error(ChatPhotoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    })
                    .setNegativeButton("取消", (dialogInterface, i13) -> dialogInterface.cancel())
                    .show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorAccent));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.font_default));
            return true;
        });
    }
}
