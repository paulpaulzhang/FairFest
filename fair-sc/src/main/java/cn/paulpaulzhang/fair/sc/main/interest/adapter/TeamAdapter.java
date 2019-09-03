package cn.paulpaulzhang.fair.sc.main.interest.adapter;

import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.chat.MessageActivity;
import cn.paulpaulzhang.fair.sc.main.interest.model.Team;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.image.ImageUtil;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.adapter
 * 创建时间：9/2/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TeamAdapter extends BaseQuickAdapter<Team, BaseViewHolder> {
    public TeamAdapter(int layoutResId, @Nullable List<Team> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Team item) {
        SimpleDraweeView mBg = helper.getView(R.id.user_bg);
        CircleImageView mAvatar = helper.getView(R.id.civ_avatar);

        if (item.getBackground() != null) {
            ImageUtil.setBlurImage(mContext, mBg, item.getBackground(), 10);
        } else {
            ImageUtil.setBlurImage(mContext, mBg, Constant.DEFAULT_BACKGROUND, 10);
        }

        if (item.getAvatar() != null) {
            Glide.with(mContext).load(item.getAvatar()).into(mAvatar);
        } else {
            Glide.with(mContext).load(Constant.DEFAULT_AVATAR).into(mAvatar);
        }

        String genderIcon = "";
        if (item.getGender() == null) {
            genderIcon = "⚥";
        } else if (TextUtils.equals(item.getGender(), "男")) {
            genderIcon = "♂";
        } else if (TextUtils.equals(item.getGender(), "女")) {
            genderIcon = "♀";
        }

        helper.setText(R.id.tv_username, item.getUsername())
                .setText(R.id.tv_gender, genderIcon)
                .setText(R.id.tv_college, item.getCollege())
                .setText(R.id.tv_introduction, item.getIntroduction());

        MaterialButton mMessage = helper.getView(R.id.btn_message);
        mMessage.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MessageActivity.class);
            intent.putExtra("uid", String.valueOf(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())));
            intent.putExtra("username", String.valueOf(item.getUid()));
            mContext.startActivity(intent);
        });
    }
}
