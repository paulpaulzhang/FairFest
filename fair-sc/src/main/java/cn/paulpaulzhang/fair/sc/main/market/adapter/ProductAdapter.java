package cn.paulpaulzhang.fair.sc.main.market.adapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.market.model.Product;

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
        helper.setText(R.id.tv_overview, item.getProductCache().getOverview())
                .setText(R.id.tv_price, String.valueOf(item.getProductCache().getPrice()));
        AppCompatImageView mHeadImg = helper.getView(R.id.iv_image);
        Glide.with(mContext).load(item.getProductCache().getHeadImg()).into(mHeadImg);
    }
}
