package cn.paulpaulzhang.fair.sc.main.nineimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.maning.imagebrowserlibrary.ImageEngine;

import cn.paulpaulzhang.fair.sc.R;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.nineimage
 * 创建时间: 7/13/2019
 * 创建人: zlm31
 * 描述:
 */
public class GlideImageEngine implements ImageEngine {

    @Override
    public void loadImage(Context context, String url, ImageView imageView, final View progressView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().fitCenter().error(R.mipmap.ic_launcher).placeholder(R.drawable.default_placeholder))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

}
