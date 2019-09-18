package cn.paulpaulzhang.fair.sc.main.user.adapter;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.user.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.adapter
 * 创建时间：9/18/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public UserAdapter(int layoutResId, @Nullable List<User> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.tv_name, item.getUsername())
                .setText(R.id.follow, item.getFollowers() + "")
                .setText(R.id.fans, item.getFans() + "");
        CircleImageView mImageView = helper.getView(R.id.avatar);
        Glide.with(mContext)
                .load(item.getAvatar() == null ? Constant.DEFAULT_AVATAR : item.getAvatar())
                .into(mImageView);
    }
}
