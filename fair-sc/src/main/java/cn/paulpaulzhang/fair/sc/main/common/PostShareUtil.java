package cn.paulpaulzhang.fair.sc.main.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.util.file.FileUtil;
import cn.paulpaulzhang.fair.util.image.ImageUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.common
 * 创建时间：9/22/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class PostShareUtil {
    private static final PostShareUtil util = new PostShareUtil();

    public static PostShareUtil INSTANCE() {
        return util;
    }

    public void share(AppCompatActivity activity, Context context, View view, int time) {

        if (view instanceof AppBarLayout) {
            ((AppBarLayout) view).setExpanded(true, false);
        }

        new Handler().postDelayed(() -> {
            int width = view.getWidth();
            int height = view.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            view.draw(canvas);

            File file = FileUtil.saveBitmap(bitmap, "Fair School", 100);
            if (file == null) {
                return;
            }
            activity.runOnUiThread(() -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getPath()));
                context.startActivity(Intent.createChooser(intent, "分享"));
            });

        }, time);

    }
}
