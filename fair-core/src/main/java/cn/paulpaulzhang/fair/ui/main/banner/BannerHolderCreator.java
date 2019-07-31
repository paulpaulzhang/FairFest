package cn.paulpaulzhang.fair.ui.main.banner;

import android.view.View;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

import cn.paulpaulzhang.fair.R;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.banner
 * 创建时间: 7/13/2019
 * 创建人: zlm31
 * 描述:
 */
public class BannerHolderCreator implements CBViewHolderCreator {
    @Override
    public Holder createHolder(View itemView) {
        return new BannerImageHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.scroll_image;
    }
}
