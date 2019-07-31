package cn.paulpaulzhang.fair.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import cn.paulpaulzhang.fair.base.activities.FairActivity;
import cn.paulpaulzhang.fair.R;
import cn.paulpaulzhang.fair.R2;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main
 * 创建时间: 7/30/2019
 * 创建人: zlm31
 * 描述:
 */
public class PhotoPreviewActivity extends FairActivity {

    @BindView(R2.id.photo_view)
    PhotoView mPhotoView;

    @Override
    public int setLayout() {
        return R.layout.activity_photo_preview;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).init();
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        Glide.with(this).load(path).into(mPhotoView);

        mPhotoView.setOnClickListener(v -> finish());
    }
}
