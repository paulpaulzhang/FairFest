package cn.paulpaulzhang.fair.sc.main.interest.follow;

import android.content.Intent;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.ctetin.expandabletextviewlibrary.app.LinkType;
import com.ctetin.expandabletextviewlibrary.app.StatusType;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.model.FollowLikeCache;
import cn.paulpaulzhang.fair.sc.database.model.FollowLikeCache_;
import cn.paulpaulzhang.fair.sc.database.model.FollowPostCache;
import cn.paulpaulzhang.fair.sc.database.model.FollowUserCache;
import cn.paulpaulzhang.fair.sc.database.model.LocalUser;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.nineimage.NineAdapter;
import cn.paulpaulzhang.fair.sc.main.post.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.DynamicActivity;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.follow
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
public class FollowAdapter extends BaseMultiItemQuickAdapter<FollowItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    FollowAdapter(List<FollowItem> data) {
        super(data);
        addItemType(FollowItem.DYNAMIC, R.layout.view_dynamic_item);
        addItemType(FollowItem.ARTICLE, R.layout.view_article_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowItem item) {
        if (item.getItemType() == FollowItem.DYNAMIC) {
            FollowPostCache followPostCache = item.getFollowPostCache();
            long id = followPostCache.getId();
            long uid = followPostCache.getUid();
            Box<LocalUser> localUserBox = ObjectBox.get().boxFor(LocalUser.class);
            LocalUser current = localUserBox.get(
                    FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
            String time = followPostCache.getTime();
            String device = followPostCache.getDevice();
            String content = followPostCache.getContent();
            ArrayList<String> imgs = JsonParseUtil.parseImgs(followPostCache.getImagesUrl());
            int likeCount = followPostCache.getLikeCount();
            int commentCount = followPostCache.getCommentCount();
            int shareCount = followPostCache.getShareCount();

            //判断当前用户是否对当前文章点赞点赞
            Box<FollowLikeCache> likeBox = ObjectBox.get().boxFor(FollowLikeCache.class);
            FollowLikeCache followLikeCache = likeBox.query().equal(FollowLikeCache_.pid, id).build().findFirst();
            boolean isLike = false;
            if (followLikeCache != null) {
                isLike = followLikeCache.isLike();
            }

            GridView mDynamicImg = helper.getView(R.id.gv_images_dynamic);
            LinearLayout mLike = helper.getView(R.id.ll_like_dynamic);
            LinearLayout mComment = helper.getView(R.id.ll_comment_dynamic);
            LinearLayout mShare = helper.getView(R.id.ll_share_dynamic);
            AppCompatTextView mLikeCount = helper.getView(R.id.tv_like_dynamic);
            CircleImageView mAvatar = helper.getView(R.id.civ_user_dynamic);

            Box<FollowUserCache> userBox = ObjectBox.get().boxFor(FollowUserCache.class);
            FollowUserCache followUserCache = userBox.get(uid);

            helper.setText(R.id.tv_username_dynamic, followUserCache.getUsername());
            Glide.with(mContext).load(followUserCache.getAvatar()).centerCrop().placeholder(R.mipmap.ic_launcher).into(mAvatar);

            mAvatar.setOnClickListener(v -> Toast.makeText(mContext, "用户详情", Toast.LENGTH_SHORT).show());

            boolean finalIsLike = isLike;
            if (finalIsLike) {
                helper.setImageResource(R.id.iv_like_dynamic, R.drawable.ic_liked);
                mLikeCount.setTextColor(mContext.getColor(R.color.colorAccent));
            } else {
                helper.setImageResource(R.id.iv_like_dynamic, R.drawable.ic_like);
                mLikeCount.setTextColor(mContext.getColor(R.color.font_default));
            }
            mLike.setOnClickListener(v -> {

                if (finalIsLike) {
                    helper.setImageResource(R.id.iv_like_dynamic, R.drawable.ic_like);
                    int count = Integer.parseInt(mLikeCount.getText().toString().trim()) - 1;
                    mLikeCount.setText(String.valueOf(count));
                    mLikeCount.setTextColor(mContext.getColor(R.color.font_default));
                    RestClient.builder()
                            //TODO 点赞请求
                            .url("set_dislike")
                            .params("id", id)
                            .params("uid", current.getId())
                            .build().post();
                } else {
                    helper.setImageResource(R.id.iv_like_dynamic, R.drawable.ic_liked);
                    int count = Integer.parseInt(mLikeCount.getText().toString().trim()) + 1;
                    mLikeCount.setText(String.valueOf(count));
                    mLikeCount.setTextColor(mContext.getColor(R.color.colorAccent));
                    RestClient.builder()
                            //TODO 点赞请求
                            .url("set_like")
                            .params("id", id)
                            .params("count", count)
                            .params("uid", current.getId())
                            .build().post();
                }
            });

            mComment.setOnClickListener(v -> Toast.makeText(mContext, "评论", Toast.LENGTH_SHORT).show());
            mShare.setOnClickListener(v -> Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show());
            helper.setText(R.id.tv_device_dynamic, device)
                    .setText(R.id.tv_time_dynamic, time);

            if (likeCount != 0) {
                mLikeCount.setText(String.valueOf(likeCount));
            }
            if (commentCount != 0) {
                helper.setText(R.id.tv_comment_dynamic, String.valueOf(commentCount));
            }

            if (shareCount != 0) {
                helper.setText(R.id.tv_share_dynamic, String.valueOf(shareCount));
            }

            mDynamicImg.setAdapter(new NineAdapter(imgs, mContext));
            ExpandableTextView mDynamicContent = helper.getView(R.id.etv_content_dynamic);
            mDynamicContent.setContent(content);
            mDynamicContent.setLinkClickListener((t, c, selfContent) -> {
                //根据类型去判断  t:type   c:content
                if (t.equals(LinkType.LINK_TYPE)) {
                    Toast.makeText(mContext, "你点击了链接 内容是：" + c, Toast.LENGTH_SHORT).show();
                } else if (t.equals(LinkType.MENTION_TYPE)) {
                    Toast.makeText(mContext, "你点击了@用户 内容是：" + c, Toast.LENGTH_SHORT).show();
                } else if (t.equals(LinkType.SELF)) {
                    Toast.makeText(mContext, "你点击了自定义规则 内容是：" + c + " " + selfContent, Toast.LENGTH_SHORT).show();
                }
            });
            mDynamicContent.setExpandOrContractClickListener(t -> {
                if (t.equals(StatusType.STATUS_EXPAND)) {
                    Intent intent = new Intent(mContext, DynamicActivity.class);
                    intent.putExtra("id", id);
                    mContext.startActivity(intent);
                }
            }, false);
        } else if (item.getItemType() == FollowItem.ARTICLE) {
            FollowPostCache followPostCache = item.getFollowPostCache();
            long id = followPostCache.getId();
            long uid = followPostCache.getUid();
            Box<LocalUser> localUserBox = ObjectBox.get().boxFor(LocalUser.class);
            LocalUser current = localUserBox.get(
                    FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));

            String time = followPostCache.getTime();
            String device = followPostCache.getDevice();
            String content = followPostCache.getContent();
            String title = followPostCache.getTitle();
            ArrayList<String> imgs = JsonParseUtil.parseImgs(followPostCache.getImagesUrl());
            int likeCount = followPostCache.getLikeCount();
            int commentCount = followPostCache.getCommentCount();
            int shareCount = followPostCache.getShareCount();

            //判断当前用户是否对当前文章点赞点赞
            Box<FollowLikeCache> likeBox = ObjectBox.get().boxFor(FollowLikeCache.class);
            FollowLikeCache followLikeCache = likeBox.query().equal(FollowLikeCache_.pid, id).build().findFirst();
            boolean isLike = false;
            if (followLikeCache != null) {
                isLike = followLikeCache.isLike();
            }

            GridView mArticleImg = helper.getView(R.id.gv_images_article);
            LinearLayout mLike = helper.getView(R.id.ll_like_article);
            LinearLayout mComment = helper.getView(R.id.ll_comment_article);
            LinearLayout mShare = helper.getView(R.id.ll_share_article);
            AppCompatTextView mLikeCount = helper.getView(R.id.tv_like_article);
            CircleImageView mAvatar = helper.getView(R.id.civ_user_article);

            Box<FollowUserCache> userBox = ObjectBox.get().boxFor(FollowUserCache.class);
            FollowUserCache followUserCache = userBox.get(uid);

            helper.setText(R.id.tv_username_article, followUserCache.getUsername());
            Glide.with(mContext).load(followUserCache.getAvatar()).centerCrop().placeholder(R.mipmap.ic_launcher).into(mAvatar);

            mAvatar.setOnClickListener(v -> Toast.makeText(mContext, "用户详情", Toast.LENGTH_SHORT).show());

            boolean finalIsLike = isLike;
            if (finalIsLike) {
                helper.setImageResource(R.id.iv_like_article, R.drawable.ic_liked);
                mLikeCount.setTextColor(mContext.getColor(R.color.colorAccent));
            } else {
                helper.setImageResource(R.id.iv_like_article, R.drawable.ic_like);
                mLikeCount.setTextColor(mContext.getColor(R.color.font_default));
            }
            mLike.setOnClickListener(v -> {
                if (finalIsLike) {
                    helper.setImageResource(R.id.iv_like_article, R.drawable.ic_like);
                    int count = Integer.parseInt(mLikeCount.getText().toString().trim()) - 1;
                    mLikeCount.setText(String.valueOf(count));
                    mLikeCount.setTextColor(mContext.getColor(R.color.font_default));
                    RestClient.builder()
                            //TODO 点赞请求
                            .url("set_dislike")
                            .params("id", id)
                            .params("uid", current.getId())
                            .build().post();
                } else {
                    helper.setImageResource(R.id.iv_like_article, R.drawable.ic_liked);
                    int count = Integer.parseInt(mLikeCount.getText().toString().trim()) + 1;
                    mLikeCount.setText(String.valueOf(count));
                    mLikeCount.setTextColor(mContext.getColor(R.color.colorAccent));
                    RestClient.builder()
                            //TODO 点赞请求
                            .url("set_like")
                            .params("id", id)
                            .params("count", count)
                            .params("uid", current.getId())
                            .build().post();
                }
            });

            mComment.setOnClickListener(v -> Toast.makeText(mContext, "评论", Toast.LENGTH_SHORT).show());
            mShare.setOnClickListener(v -> Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show());
            helper.setText(R.id.tv_device_article, device)
                    .setText(R.id.tv_time_article, time)
                    .setText(R.id.tv_title_article, title);

            if (likeCount != 0) {
                mLikeCount.setText(String.valueOf(likeCount));
            }
            if (commentCount != 0) {
                helper.setText(R.id.tv_comment_article, String.valueOf(commentCount));
            }

            if (shareCount != 0) {
                helper.setText(R.id.tv_share_article, String.valueOf(shareCount));
            }

            mArticleImg.setAdapter(new NineAdapter(imgs, mContext));
            ExpandableTextView mArticleContent = helper.getView(R.id.etv_content_article);
            mArticleContent.setContent(content);
            mArticleContent.setOnClickListener(v -> Toast.makeText(mContext, "查看更多", Toast.LENGTH_SHORT).show());
            mArticleContent.setLinkClickListener((t, c, selfContent) -> {
                //根据类型去判断  t:type   c:content
                if (t.equals(LinkType.LINK_TYPE)) {
                    Toast.makeText(mContext, "你点击了链接 内容是：" + c, Toast.LENGTH_SHORT).show();
                } else if (t.equals(LinkType.MENTION_TYPE)) {
                    Toast.makeText(mContext, "你点击了@用户 内容是：" + c, Toast.LENGTH_SHORT).show();
                } else if (t.equals(LinkType.SELF)) {
                    Toast.makeText(mContext, "你点击了自定义规则 内容是：" + c + " " + selfContent, Toast.LENGTH_SHORT).show();
                }
            });
            mArticleContent.setExpandOrContractClickListener(t -> {
                if (t.equals(StatusType.STATUS_EXPAND)) {
                    Intent intent = new Intent(mContext, ArticleActivity.class);
                    intent.putExtra("id", id);
                    mContext.startActivity(intent);
                }
            }, false);
        }
    }
}
