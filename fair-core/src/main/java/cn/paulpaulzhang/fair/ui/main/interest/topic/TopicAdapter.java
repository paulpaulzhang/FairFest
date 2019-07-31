package cn.paulpaulzhang.fair.ui.main.interest.topic;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.R;
import cn.paulpaulzhang.fair.util.image.GlideRoundTransform;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.ic_topic
 * 创建时间: 7/20/2019
 * 创建人: zlm31
 * 描述:
 */
public class TopicAdapter extends BaseQuickAdapter<TopicItem, BaseViewHolder> {

    public TopicAdapter(int layoutResId, @Nullable List<TopicItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicItem item) {
        helper.setText(R.id.tv_name, item.getTopicCache().getName())
                .setText(R.id.follow, item.getTopicCache().getFollow() + "")
                .setText(R.id.post, item.getTopicCache().getPost() + "");
        AppCompatImageView mImageView = helper.getView(R.id.picture);
        Glide.with(mContext)
                .load(item.getTopicCache().getImg())
                .apply(RequestOptions
                        .bitmapTransform(new GlideRoundTransform(mContext, 4))
                        .placeholder(R.drawable.ic_topic))
                .into(mImageView);
    }
}
