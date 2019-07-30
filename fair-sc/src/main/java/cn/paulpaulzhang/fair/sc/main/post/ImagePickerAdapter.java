package cn.paulpaulzhang.fair.sc.main.post;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/29/2019
 * 创建人: zlm31
 * 描述:
 */
public class ImagePickerAdapter extends BaseQuickAdapter<ImageItem, BaseViewHolder> {
    public ImagePickerAdapter(int layoutResId, @Nullable List<ImageItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageItem item) {
        helper.addOnClickListener(R.id.iv_delete);
        AppCompatImageView mImageView = helper.getView(R.id.image_view);
        Glide.with(mContext).load(item.getUrl()).centerCrop().into(mImageView);
    }

}
