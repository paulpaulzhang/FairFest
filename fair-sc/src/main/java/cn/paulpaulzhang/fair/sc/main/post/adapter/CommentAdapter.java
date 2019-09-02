package cn.paulpaulzhang.fair.sc.main.post.adapter;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.ctetin.expandabletextviewlibrary.app.LinkType;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.PhotoPreviewActivity;
import cn.paulpaulzhang.fair.sc.main.post.model.Comment;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.text.TextUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/26/2019
 * 创建人: zlm31
 * 描述:
 */
public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    public CommentAdapter(int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        CircleImageView mUser = helper.getView(R.id.civ_user);
        Glide.with(mContext).load(item.getAvatarUrl()).into(mUser);

        helper.setText(R.id.tv_user, item.getUsername())
                .setText(R.id.tv_time, DateUtil.getTime(item.getTime()));

        mUser.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, UserCenterActivity.class);
            intent.putExtra("uid", item.getUid());
            mContext.startActivity(intent);
        });

        ExpandableTextView mContent = helper.getView(R.id.tv_content);
        mContent.setContent(TextUtil.textHightLightTopic(item.getContent()));
        mContent.setLinkClickListener((t, c, selfContent) -> {
            if (t.equals(LinkType.LINK_TYPE)) {
                Toast.makeText(mContext, "你点击了链接 内容是：" + c, Toast.LENGTH_SHORT).show();
            } else if (t.equals(LinkType.MENTION_TYPE)) {
                Toast.makeText(mContext, "你点击了@用户 内容是：" + c, Toast.LENGTH_SHORT).show();
            } else if (t.equals(LinkType.SELF)) {
                Intent intent = new Intent(mContext, TopicDetailActivity.class);
                intent.putExtra("name", TextUtil.getTopicName(c));
                mContext.startActivity(intent);
            }
        });
    }
}
