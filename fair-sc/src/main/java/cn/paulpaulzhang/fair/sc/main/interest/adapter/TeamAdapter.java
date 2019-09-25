package cn.paulpaulzhang.fair.sc.main.interest.adapter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.chat.MessageActivity;
import cn.paulpaulzhang.fair.sc.main.interest.model.Team;
import cn.paulpaulzhang.fair.sc.main.interest.model.TeamSection;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.image.ImageUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.adapter
 * 创建时间：9/2/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TeamAdapter extends BaseSectionQuickAdapter<TeamSection, BaseViewHolder> {

    public TeamAdapter(int layoutResId, int sectionHeadResId, List<TeamSection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, TeamSection item) {
        helper.setText(R.id.tv_header, item.header);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TeamSection item) {
        SimpleDraweeView mBg = helper.getView(R.id.user_bg);
        CircleImageView mAvatar = helper.getView(R.id.civ_user);

        if (item.t.getBackground() != null) {
            ImageUtil.setBlurImage(mContext, mBg, item.t.getBackground(), 10);
        } else {
            ImageUtil.setBlurImage(mContext, mBg, Constant.DEFAULT_BACKGROUND, 10);
        }

        if (item.t.getAvatar() != null) {
            Glide.with(mContext).load(item.t.getAvatar()).into(mAvatar);
        } else {
            Glide.with(mContext).load(Constant.DEFAULT_AVATAR).into(mAvatar);
        }

        String genderIcon = "";
        if (item.t.getGender() == null) {
            genderIcon = "⚥";
        } else if (TextUtils.equals(item.t.getGender(), "男")) {
            genderIcon = "♂";
        } else if (TextUtils.equals(item.t.getGender(), "女")) {
            genderIcon = "♀";
        }

        helper.setText(R.id.tv_username, item.t.getUsername())
                .setText(R.id.tv_gender, genderIcon)
                .setText(R.id.tv_college, item.t.getCollege())
                .setText(R.id.tv_introduction, item.t.getIntroduction() + "");

        MaterialButton mMessage = helper.getView(R.id.btn_message);
        mMessage.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, MessageActivity.class);
            intent.putExtra("uid", String.valueOf(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())));
            intent.putExtra("username", String.valueOf(item.t.getUid()));
            intent.putExtra("info", "我想加入您的 [$ " + item.t.getIntroduction() + " $] 队伍，请回复!");
            mContext.startActivity(intent);
        });
    }

}
