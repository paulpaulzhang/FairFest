package cn.paulpaulzhang.fair.sc.main.post.activity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;

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

        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Fade());

        ImmersionBar.with(this).init();
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        Glide.with(this).load(path).into(mPhotoView);

        mPhotoView.setOnClickListener(v -> finishAfterTransition());
    }
}
