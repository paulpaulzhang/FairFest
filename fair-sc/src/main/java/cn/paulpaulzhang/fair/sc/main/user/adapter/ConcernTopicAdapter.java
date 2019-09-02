package cn.paulpaulzhang.fair.sc.main.user.adapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.user.model.ConcernTopic;
import cn.paulpaulzhang.fair.util.image.GlideRoundTransform;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.adapter
 * 创建时间：9/2/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class ConcernTopicAdapter extends BaseQuickAdapter<ConcernTopic, BaseViewHolder> {
    public ConcernTopicAdapter(int layoutResId, @Nullable List<ConcernTopic> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConcernTopic item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.follow, item.getFollow() + "")
                .setText(R.id.post, item.getPost() + "");
        AppCompatImageView mImageView = helper.getView(R.id.picture);
        Glide.with(mContext)
                .load(item.getImg())
                .apply(RequestOptions
                        .bitmapTransform(new GlideRoundTransform(mContext, 4))
                        .placeholder(R.drawable.ic_topic))
                .into(mImageView);
    }
}
