package cn.paulpaulzhang.fair.sc.main.post.adapter;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.post.model.Share;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/26/2019
 * 创建人: zlm31
 * 描述:
 */
public class ShareAdapter extends BaseQuickAdapter<Share, BaseViewHolder> {
    public ShareAdapter(int layoutResId, @Nullable List<Share> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Share item) {
        CircleImageView mUser = helper.getView(R.id.civ_user);
        Glide.with(mContext).load(item.getAvatarUrl()).into(mUser);

        helper.setText(R.id.tv_user, item.getUsername());
    }
}
