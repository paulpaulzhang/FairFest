package cn.paulpaulzhang.fair.util.image;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.PixelCopy;
import android.view.View;
import android.view.Window;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.postprocessors.BlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;

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
     * @param url
     */
    public static void setBlurImage(Context context, SimpleDraweeView simpleDraweeView, String url, int radius) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setPostprocessor(new BlurPostProcessor(radius, context, 1))
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .build();
        simpleDraweeView.setController(controller);
    }

    public static void convertLayoutToBitmap(Window window, View view, Bitmap dest,
                                             PixelCopy.OnPixelCopyFinishedListener listener) {
        //获取layout的位置
        final int[] location = new int[2];
        view.getLocationInWindow(location);
        //请求转换

        FairLogger.d("window", window);
        FairLogger.d("view", view);
        FairLogger.d("dest", dest);
        FairLogger.d("listener", listener);

        PixelCopy.request(window,
                new Rect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight()),
                dest, listener, new Handler(Looper.getMainLooper()));
    }

}
