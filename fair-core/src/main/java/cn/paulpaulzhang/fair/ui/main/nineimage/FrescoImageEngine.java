package cn.paulpaulzhang.fair.ui.main.nineimage;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.maning.imagebrowserlibrary.ImageEngine;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.nineimage
 * 创建时间: 7/14/2019
 * 创建人: zlm31
 * 描述:
 */
public class FrescoImageEngine implements ImageEngine {
    @Override
    public void loadImage(Context context, String url, ImageView imageView, View progressView) {
        Uri uri = Uri.parse(url);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;
        simpleDraweeView.setImageURI(uri);
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                .setProgressBarImage(progressView.getId())
                .build();
        simpleDraweeView.setHierarchy(hierarchy);
    }
}
