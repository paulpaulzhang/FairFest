package cn.paulpaulzhang.fair.sc.main.interest.adapter;

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
import java.util.Date;
import java.util.List;

import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowLikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowLikeCache_;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowPostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowUserCache;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.interest.model.Follow;
import cn.paulpaulzhang.fair.sc.main.interest.model.TopicDetail;
import cn.paulpaulzhang.fair.sc.main.nineimage.NineAdapter;
import cn.paulpaulzhang.fair.sc.main.post.activity.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.DynamicActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import cn.paulpaulzhang.fair.util.text.TextUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.follow
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
public class FollowAdapter extends BaseMultiItemQuickAdapter<Follow, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FollowAdapter(List<Follow> data) {
        super(data);
        addItemType(Follow.DYNAMIC, R.layout.item_dynamic);
        addItemType(Follow.ARTICLE, R.layout.item_article);
    }

    @Override
    protected void convert(BaseViewHolder helper, Follow item) {
        if (item.getItemType() == TopicDetail.DYNAMIC) {
            FollowPostCache postCache = item.getPostCache();
            long id = postCache.getId();
            long uid = postCache.getUid();
            long time = postCache.getTime();
            String device = postCache.getDevice();
            String content = postCache.getContent();
            ArrayList<String> imgs = JsonParseUtil.parseImgs(postCache.getImagesUrl());
            int likeCount = postCache.getLikeCount();
            int commentCount = postCache.getCommentCount();
            int shareCount = postCache.getShareCount();


            GridView mDynamicImg = helper.getView(R.id.gv_images_dynamic);
            LinearLayout mLike = helper.getView(R.id.ll_like_dynamic);
            LinearLayout mComment = helper.getView(R.id.ll_comment_dynamic);
            LinearLayout mShare = helper.getView(R.id.ll_share_dynamic);
            AppCompatTextView mLikeCount = helper.getView(R.id.tv_like_dynamic);
            CircleImageView mAvatar = helper.getView(R.id.civ_user_dynamic);

            Box<FollowUserCache> userBox = ObjectBox.get().boxFor(FollowUserCache.class);
            FollowUserCache userCache = userBox.get(uid);

            helper.setText(R.id.tv_username_dynamic, userCache.getUsername() == null ?
                    String.valueOf(userCache.getId()).substring(8) : userCache.getUsername());
            Glide.with(mContext).load(userCache.getAvatar() == null ? Constant.DEFAULT_AVATAR : userCache.getAvatar()).centerCrop().placeholder(R.mipmap.ic_launcher).into(mAvatar);

            mAvatar.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, UserCenterActivity.class);
                intent.putExtra("uid", userCache.getId());
                mContext.startActivity(intent);
            });

            if (item.isLike()) {
                helper.setImageResource(R.id.iv_like_dynamic, R.drawable.ic_liked);
                mLikeCount.setTextColor(mContext.getColor(R.color.colorAccent));
            } else {
                helper.setImageResource(R.id.iv_like_dynamic, R.drawable.ic_like);
                mLikeCount.setTextColor(mContext.getColor(R.color.font_default));
            }
            mLike.setOnClickListener(v -> {

                if (item.isLike()) {
                    int count = Integer.parseInt(mLikeCount.getText().toString().trim()) - 1;
                    RestClient.builder()
                            .url(Api.CANCEL_THUMBSUP_POST)
                            .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                            .params("pid", id)
                            .success(r -> {
                                helper.setImageResource(R.id.iv_like_dynamic, R.drawable.ic_like);
                                mLikeCount.setText(String.valueOf(count));
                                mLikeCount.setTextColor(mContext.getColor(R.color.font_default));
                                item.setLike(false);
                            })
                            .error((code, msg) -> Toasty.error(mContext, "取消失败 " + code, Toasty.LENGTH_SHORT).show())
                            .build()
                            .post();
                } else {
                    int count = Integer.parseInt(mLikeCount.getText().toString().trim().isEmpty() ?
                            "0" : mLikeCount.getText().toString().trim()) + 1;
                    RestClient.builder()
                            .url(Api.THUMBSUP_POST)
                            .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                            .params("pid", id)
                            .success(r -> {
                                helper.setImageResource(R.id.iv_like_dynamic, R.drawable.ic_liked);
                                mLikeCount.setText(String.valueOf(count));
                                mLikeCount.setTextColor(mContext.getColor(R.color.colorAccent));
                                item.setLike(true);
                            })
                            .error((code, msg) -> Toasty.error(mContext, "点赞失败 " + code, Toasty.LENGTH_SHORT).show())
                            .build()
                            .post();
                }
            });

            mComment.setOnClickListener(v -> Toast.makeText(mContext, "评论", Toast.LENGTH_SHORT).show());
            mShare.setOnClickListener(v -> Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show());
            helper.setText(R.id.tv_device_dynamic, device)
                    .setText(R.id.tv_time_dynamic, DateUtil.getTime(new Date(time)));

            if (likeCount != 0) {
                mLikeCount.setText(String.valueOf(likeCount));
            } else {
                mLikeCount.setText("");
            }

            if (commentCount != 0) {
                helper.setText(R.id.tv_comment_dynamic, String.valueOf(commentCount));
            } else {
                helper.setText(R.id.tv_comment_dynamic, "");
            }

            if (shareCount != 0) {
                helper.setText(R.id.tv_share_dynamic, String.valueOf(shareCount));
            } else {
                helper.setText(R.id.tv_share_dynamic, "");
            }

            mDynamicImg.setAdapter(new NineAdapter(imgs, mContext));
            ExpandableTextView mDynamicContent = helper.getView(R.id.etv_content_dynamic);
            mDynamicContent.setContent(TextUtil.text2Post(content));
            mDynamicContent.setLinkClickListener((t, c, selfContent) -> {
                //根据类型去判断  t:type   c:content
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
            mDynamicContent.setExpandOrContractClickListener(t -> {
                if (t.equals(StatusType.STATUS_EXPAND)) {
                    Intent intent = new Intent(mContext, DynamicActivity.class);
                    intent.putExtra("pid", item.getPostCache().getId());
                    intent.putExtra("uid", item.getPostCache().getUid());
                    intent.putExtra("isLike", item.isLike());
                    mContext.startActivity(intent);
                }
            }, false);

        } else if (item.getItemType() == TopicDetail.ARTICLE) {
            FollowPostCache postCache = item.getPostCache();
            long id = postCache.getId();
            long uid = postCache.getUid();

            long time = postCache.getTime();
            String device = postCache.getDevice();
            String content = postCache.getContent();
            String title = postCache.getTitle();
            ArrayList<String> imgs = JsonParseUtil.parseImgs(postCache.getImagesUrl());
            int likeCount = postCache.getLikeCount();
            int commentCount = postCache.getCommentCount();
            int shareCount = postCache.getShareCount();

            GridView mArticleImg = helper.getView(R.id.gv_images_article);
            LinearLayout mLike = helper.getView(R.id.ll_like_article);
            LinearLayout mComment = helper.getView(R.id.ll_comment_article);
            LinearLayout mShare = helper.getView(R.id.ll_share_article);
            AppCompatTextView mLikeCount = helper.getView(R.id.tv_like_article);
            CircleImageView mAvatar = helper.getView(R.id.civ_user_article);

            Box<FollowUserCache> userBox = ObjectBox.get().boxFor(FollowUserCache.class);
            FollowUserCache userCache = userBox.get(uid);

            helper.setText(R.id.tv_username_article, userCache.getUsername() == null ?
                    String.valueOf(userCache.getId()).substring(8) : userCache.getUsername());
            Glide.with(mContext).load(userCache.getAvatar()).centerCrop().placeholder(R.mipmap.ic_launcher).into(mAvatar);

            mAvatar.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, UserCenterActivity.class);
                intent.putExtra("uid", userCache.getId());
                mContext.startActivity(intent);
            });

            if (item.isLike()) {
                helper.setImageResource(R.id.iv_like_article, R.drawable.ic_liked);
                mLikeCount.setTextColor(mContext.getColor(R.color.colorAccent));
            } else {
                helper.setImageResource(R.id.iv_like_article, R.drawable.ic_like);
                mLikeCount.setTextColor(mContext.getColor(R.color.font_default));
            }
            mLike.setOnClickListener(v -> {
                if (item.isLike()) {
                    int count = Integer.parseInt(mLikeCount.getText().toString().trim()) - 1;
                    RestClient.builder()
                            .url(Api.CANCEL_THUMBSUP_POST)
                            .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                            .params("pid", id)
                            .success(r -> {
                                helper.setImageResource(R.id.iv_like_article, R.drawable.ic_like);
                                mLikeCount.setText(String.valueOf(count));
                                mLikeCount.setTextColor(mContext.getColor(R.color.font_default));
                                item.setLike(false);
                            })
                            .error((code, msg) -> Toasty.error(mContext, "取消失败 " + code, Toasty.LENGTH_SHORT).show())
                            .build()
                            .post();
                } else {
                    int count = Integer.parseInt(mLikeCount.getText().toString().trim().isEmpty() ?
                            "0" : mLikeCount.getText().toString().trim()) + 1;
                    RestClient.builder()
                            .url(Api.THUMBSUP_POST)
                            .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                            .params("pid", id)
                            .success(r -> {
                                helper.setImageResource(R.id.iv_like_article, R.drawable.ic_liked);
                                mLikeCount.setText(String.valueOf(count));
                                mLikeCount.setTextColor(mContext.getColor(R.color.colorAccent));
                                item.setLike(true);
                            })
                            .error((code, msg) -> Toasty.error(mContext, "点赞失败 " + code, Toasty.LENGTH_SHORT).show())
                            .build()
                            .post();
                }
            });

            mComment.setOnClickListener(v -> Toast.makeText(mContext, "评论", Toast.LENGTH_SHORT).show());
            mShare.setOnClickListener(v -> Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show());
            helper.setText(R.id.tv_device_article, device)
                    .setText(R.id.tv_time_article, DateUtil.getTime(time))
                    .setText(R.id.tv_title_article, title);

            if (likeCount != 0) {
                mLikeCount.setText(String.valueOf(likeCount));
            } else {
                mLikeCount.setText("");
            }

            if (commentCount != 0) {
                helper.setText(R.id.tv_comment_article, String.valueOf(commentCount));
            } else {
                helper.setText(R.id.tv_comment_article, "");
            }

            if (shareCount != 0) {
                helper.setText(R.id.tv_share_article, String.valueOf(shareCount));
            } else {
                helper.setText(R.id.tv_share_article, "");
            }

            mArticleImg.setAdapter(new NineAdapter(imgs, mContext));
            ExpandableTextView mArticleContent = helper.getView(R.id.etv_content_article);
            mArticleContent.setContent(TextUtil.text2Post(content));
            mArticleContent.setOnClickListener(v -> Toast.makeText(mContext, "查看更多", Toast.LENGTH_SHORT).show());
            mArticleContent.setLinkClickListener((t, c, selfContent) -> {
                //根据类型去判断  t:type   c:content
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
            mArticleContent.setExpandOrContractClickListener(t -> {
                if (t.equals(StatusType.STATUS_EXPAND)) {
                    Intent intent = new Intent(mContext, ArticleActivity.class);
                    intent.putExtra("pid", item.getPostCache().getId());
                    intent.putExtra("uid", item.getPostCache().getUid());
                    intent.putExtra("isLike", item.isLike());
                    mContext.startActivity(intent);
                }
            }, false);

        }
    }
}
