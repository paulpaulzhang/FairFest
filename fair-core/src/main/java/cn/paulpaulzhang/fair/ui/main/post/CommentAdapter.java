package cn.paulpaulzhang.fair.ui.main.post;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.R;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/26/2019
 * 创建人: zlm31
 * 描述:
 */
public class CommentAdapter extends BaseQuickAdapter<CommentItem, BaseViewHolder> {
    public CommentAdapter(int layoutResId, @Nullable List<CommentItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentItem item) {
        CircleImageView mUser = helper.getView(R.id.civ_user);
        Glide.with(mContext).load(item.getAvatarUrl()).into(mUser);

        helper.setText(R.id.tv_user, item.getUsername())
                .setText(R.id.tv_time, item.getTime())
                .setText(R.id.tv_content, item.getContent());

        mUser.setOnClickListener(view -> {
            Toasty.info(mContext, "跳转个人主页", Toasty.LENGTH_SHORT).show();
        });
    }
}
