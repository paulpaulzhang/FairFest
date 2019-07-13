package cn.paulpaulzhang.fair.sc.main.nineimage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.maning.imagebrowserlibrary.MNImageBrowser;
import com.maning.imagebrowserlibrary.listeners.OnClickListener;
import com.maning.imagebrowserlibrary.listeners.OnLongClickListener;
import com.maning.imagebrowserlibrary.listeners.OnPageChangeListener;
import com.maning.imagebrowserlibrary.model.ImageBrowserConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.util.file.FileUtil;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.nineimage
 * 创建时间: 7/12/2019
 * 创建人: zlm31
 * 描述: 九宫格图片Adapter
 */
public class NineAdapter extends BaseAdapter {
    private List<String> imagesUrl;
    private Context mContext;

    public NineAdapter(List<String> imagesUrl, Context context) {
        this.imagesUrl = imagesUrl;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return imagesUrl.size();
    }

    @Override
    public Object getItem(int i) {
        return imagesUrl.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder mViewHolder;
        mViewHolder = new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.view_nine_image_item, null);
            mViewHolder.imageView = view.findViewById(R.id.iv_nine);
            view.setTag("NineView");
        }
        mViewHolder.imageView = view.findViewById(R.id.iv_nine);

        Glide.with(mContext)
                .asBitmap()
                .load(imagesUrl.get(i))
                .apply(new RequestOptions().fitCenter().error(R.mipmap.ic_launcher).placeholder(R.drawable.default_placeholder))
                .into(mViewHolder.imageView);

        mViewHolder.imageView.setOnClickListener(v -> MNImageBrowser
                .with(mContext)
                .setTransformType(ImageBrowserConfig.TransformType.Transform_Default)
                .setIndicatorType(ImageBrowserConfig.IndicatorType.Indicator_Number)
                .setIndicatorHide(false)
                .setCustomProgressViewLayoutID(R.layout.view_custom_progress)
                .setCurrentPosition(i)
                .setImageEngine(new GlideImageEngine())
                .setImageList((ArrayList<String>) imagesUrl)
                .setScreenOrientationType(ImageBrowserConfig.ScreenOrientationType.Screenorientation_Default)
                .setOnClickListener((fragmentActivity, imageView, i1, s) -> {
                    MNImageBrowser.finishImageBrowser();
                })
                .setOnLongClickListener((fragmentActivity, imageView, i12, s) -> {
                    fragmentActivity.setTheme(R.style.DialogTheme);
                    new MaterialAlertDialogBuilder(fragmentActivity)
                            .setTitle("保存图片")
                            .setMessage("点击确认保存图片到本地")
                            .setPositiveButton("确认", (dialogInterface, i14) -> {
                                final Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888, true);
                                convertLayoutToBitmap(fragmentActivity.getWindow(), imageView, bitmap, i15 -> {
                                    if (i15 == PixelCopy.SUCCESS) {
                                        File file = FileUtil.saveBitmap(bitmap, "FairSchool", 100);
                                        if (file != null) {
                                            dialogInterface.dismiss();
                                            Toast.makeText(fragmentActivity, file.getName() + "已保存", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(fragmentActivity, "保存失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            })
                            .setNegativeButton("取消", (dialogInterface, i13) -> {
                                dialogInterface.cancel();
                            })
                            .show();
                })
                .setFullScreenMode(false)
                .setActivityOpenAnime(R.anim.activity_in)
                .setActivityExitAnime(R.anim.activity_out)
                .show(mViewHolder.imageView));
        return view;
    }

    public final class ViewHolder {
        AppCompatImageView imageView;
    }

    private void convertLayoutToBitmap(Window window, View view, Bitmap dest,
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
