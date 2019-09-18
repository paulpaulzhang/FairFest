package cn.paulpaulzhang.fair.sc.main.market.adapter;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.DrawableContainer;
import android.net.Uri;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.market.model.Product;
import cn.paulpaulzhang.fair.util.dimen.DimenUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.market.adapter
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class ProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    public ProductAdapter(int layoutResId, @Nullable List<Product> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_overview, item.getProductCache().getSname())
                .setText(R.id.tv_price, String.valueOf(item.getProductCache().getPrice()));
        SimpleDraweeView mHeadImg = helper.getView(R.id.iv_image);

        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(mHeadImg.getController())
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {

                    }

                    @Override
                    public void onFinalImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo, @javax.annotation.Nullable Animatable animatable) {
                        if (imageInfo == null) {
                            return;
                        }
                        int height = imageInfo.getHeight();
                        int width = imageInfo.getWidth();
                        int imgWidth = (DimenUtil.getScreenWidth() - DimenUtil.dip2px(20 /*商品卡片四周间距为5dp，两张卡片20dp*/)) / 2;
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHeadImg.getLayoutParams();
                        params.width = imgWidth;
                        params.height = (int) ((float)imgWidth * height / width);
                        mHeadImg.setLayoutParams(params);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo) {

                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {

                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {

                    }

                    @Override
                    public void onRelease(String id) {

                    }
                })
                .setUri(Uri.parse(item.getProductCache().getHeadImg()))
                .build();
        mHeadImg.setController(controller);
    }
}
