package cn.paulpaulzhang.fair.sc.main.banner;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import cn.paulpaulzhang.fair.sc.R;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.banner
 * 创建时间: 7/13/2019
 * 创建人: zlm31
 * 描述:
 */
public class BannerImageHolder extends Holder<String> {
    private AppCompatImageView mImageView;

    public BannerImageHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        mImageView = itemView.findViewById(R.id.scroll_iv);
    }

    @Override
    public void updateUI(String data) {
        RoundedCorners corners = new RoundedCorners(15);
        RequestOptions options = RequestOptions.bitmapTransform(corners);
        Glide.with(itemView).load(data).apply(options).into(mImageView);
    }
}
