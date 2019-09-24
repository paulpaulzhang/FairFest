package cn.paulpaulzhang.fair.util.image;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.PixelCopy;
import android.view.View;
import android.view.Window;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.BlurPostProcessor;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.material.appbar.AppBarLayout;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;

import cn.paulpaulzhang.fair.R;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.net.callback.IFailure;
import cn.paulpaulzhang.fair.util.log.FairLogger;

/**
 * 包名: cn.paulpaulzhang.fair.util.image
 * 创建时间: 7/14/2019
 * 创建人: zlm31
 * 描述:
 */
public class ImageUtil {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 利用fresco实现毛玻璃效果
     *
     * @param context
     * @param simpleDraweeView
     * @param res
     */
    public static void setBlurImage(Context context, SimpleDraweeView simpleDraweeView, Object res, int radius) {

        ImageRequest request;
        if (res instanceof String) {
            request = ImageRequestBuilder.newBuilderWithSource(Uri.parse((String) res))
                    .setPostprocessor(new BlurPostProcessor(radius, context, 1))
                    .build();
        } else {
            request = ImageRequestBuilder.newBuilderWithResourceId((Integer) res)
                    .setPostprocessor(new BlurPostProcessor(radius, context, 1))
                    .build();
        }

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .build();
        simpleDraweeView.setController(controller);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void convertLayoutToBitmap(Window window, View view, Bitmap dest,
                                             PixelCopy.OnPixelCopyFinishedListener listener) {
        //获取layout的位置
        final int[] location = new int[2];
        view.getLocationInWindow(location);
        //请求转换

        PixelCopy.request(window,
                new Rect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight()),
                dest, listener, new Handler(Looper.getMainLooper()));
    }

}
