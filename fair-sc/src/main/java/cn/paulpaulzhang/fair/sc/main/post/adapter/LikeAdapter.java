package cn.paulpaulzhang.fair.sc.main.post.adapter;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.post.model.Like;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/26/2019
 * 创建人: zlm31
 * 描述:
 */
public class LikeAdapter extends BaseQuickAdapter<Like, BaseViewHolder> {
    public LikeAdapter(int layoutResId, @Nullable List<Like> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Like item) {
        CircleImageView mUser = helper.getView(R.id.civ_user);
        Glide.with(mContext).load(item.getAvatar()).into(mUser);

        helper.setText(R.id.tv_user, item.getUsername())
                .setText(R.id.tv_time, DateUtil.getTime(item.getTime()));

        mUser.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, UserCenterActivity.class);
            intent.putExtra("uid", item.getUid());
            mContext.startActivity(intent);
        });
    }
}
