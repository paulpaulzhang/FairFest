package cn.paulpaulzhang.fair.sc.main.interest;

import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.ctetin.expandabletextviewlibrary.app.LinkType;
import com.ctetin.expandabletextviewlibrary.app.StatusType;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.entity.MyObjectBox;
import cn.paulpaulzhang.fair.sc.database.entity.Post;
import cn.paulpaulzhang.fair.sc.database.entity.User;
import cn.paulpaulzhang.fair.sc.json.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.nineimage.NineAdapter;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间: 7/12/2019
 * 创建人: zlm31
 * 描述:
 */
public class DiscoveryAdapter extends BaseMultiItemQuickAdapter<DiscoveryItem, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public DiscoveryAdapter(List<DiscoveryItem> data) {
        super(data);
        addItemType(DiscoveryItem.DYNAMIC, R.layout.view_dynamic_item);
        addItemType(DiscoveryItem.ARTICLE, R.layout.view_article_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoveryItem item) {
        Post post = item.getPost();
        long id = post.getId();
        long uid = post.getUid();
        Box<User> userBox = ObjectBox.get().boxFor(User.class);
        User current = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));

        //请求user并加载用户名和头像
        RestClient.builder()
                //TODO User请求
                .url("user")
                .params("id", uid)
                .success(response -> {
                    User user = JsonParseUtil.parseUser(response);
                    if (item.getItemType() == DiscoveryItem.DYNAMIC) {
                        helper.setText(R.id.tv_username_dynamic, user.getUsername());
                        CircleImageView mAvatar = helper.getView(R.id.civ_user_dynamic);
                        Glide.with(mContext).load(user.getAvatar()).centerCrop().placeholder(R.mipmap.ic_launcher).into(mAvatar);
                    } else {
                        helper.setText(R.id.tv_username_article, user.getUsername());
                        CircleImageView mAvatar = helper.getView(R.id.civ_user_article);
                        Glide.with(mContext).load(user.getAvatar()).centerCrop().placeholder(R.mipmap.ic_launcher).into(mAvatar);
                    }
                })
                .build()
                .get();

        int type = post.getType();
        String topic = post.getTopic();
        String time = post.getTime();
        String device = post.getDevice();
        String content = post.getContent();
        String title = post.getTitle();
        ArrayList<String> imgs = getImgs(post.getImagesUrl());
        int likeCount = post.getLikeCount();
        int commentCount = post.getCommentCount();
        int shareCount = post.getShareCount();

        //判断当前用户是否对当前文章点赞点赞
        RestClient.builder()
                //TODO 点赞请求
                .url("like")
                .params("id", id)
                .params("uid", current.getId())
                .success(r -> {
                    boolean like = JSON.parseObject(r).getBoolean("like");
                    if (like) {
                        if (item.getItemType() == DiscoveryItem.DYNAMIC) {
                            helper.setImageResource(R.id.iv_like_dynamic, R.drawable.liked);
                            AppCompatTextView textView = helper.getView(R.id.tv_like_dynamic);
                            textView.setTextColor(mContext.getColor(R.color.colorAccent));
                        } else {
                            helper.setImageResource(R.id.iv_like_article, R.drawable.liked);
                            AppCompatTextView textView = helper.getView(R.id.tv_like_article);
                            textView.setTextColor(mContext.getColor(R.color.colorAccent));
                        }
                    } else {
                        if (item.getItemType() == DiscoveryItem.DYNAMIC) {
                            helper.setImageResource(R.id.iv_like_dynamic, R.drawable.like);
                            AppCompatTextView textView = helper.getView(R.id.tv_like_dynamic);
                            textView.setTextColor(mContext.getColor(R.color.font_default));
                        } else {
                            helper.setImageResource(R.id.iv_like_article, R.drawable.like);
                            AppCompatTextView textView = helper.getView(R.id.tv_like_article);
                            textView.setTextColor(mContext.getColor(R.color.font_default));
                        }
                    }
                })
                .build()
                .get();

        if (item.getItemType() == DiscoveryItem.DYNAMIC) {
            GridView mDynamicImg = helper.getView(R.id.gv_images_dynamic);
            LinearLayout mLike = helper.getView(R.id.ll_like_dynamic);
            LinearLayout mComment = helper.getView(R.id.ll_comment_dynamic);
            LinearLayout mShare = helper.getView(R.id.ll_share_dynamic);
            AppCompatTextView mLikeCount = helper.getView(R.id.tv_like_dynamic);

            CircleImageView mAvatar = helper.getView(R.id.civ_user_dynamic);
            mAvatar.setOnClickListener(v -> Toast.makeText(mContext, "用户详情", Toast.LENGTH_SHORT).show());

            mLike.setOnClickListener(v -> RestClient.builder()
                    //TODO 点赞请求
                    .url("like")
                    .params("id", id)
                    .params("uid", current.getId())
                    .success(r -> {
                        boolean like = JSON.parseObject(r).getBoolean("like");
                        Toast.makeText(mContext, like + "", Toast.LENGTH_SHORT).show();
                        if (like) {
                            helper.setImageResource(R.id.iv_like_dynamic, R.drawable.like);
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
                            helper.setImageResource(R.id.iv_like_dynamic, R.drawable.liked);
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
                    })
                    .build()
                    .post());

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
                    Toast.makeText(mContext, "展开操作，不真正触发展开操作", Toast.LENGTH_SHORT).show();
                }
            }, false);
        } else if (item.getItemType() == DiscoveryItem.ARTICLE) {
            GridView mArticleImg = helper.getView(R.id.gv_images_article);
            LinearLayout mLike = helper.getView(R.id.ll_like_article);
            LinearLayout mComment = helper.getView(R.id.ll_comment_article);
            LinearLayout mShare = helper.getView(R.id.ll_share_article);
            AppCompatTextView mLikeCount = helper.getView(R.id.tv_like_article);

            CircleImageView mAvatar = helper.getView(R.id.civ_user_article);
            mAvatar.setOnClickListener(v -> Toast.makeText(mContext, "用户详情", Toast.LENGTH_SHORT).show());

            mLike.setOnClickListener(v -> RestClient.builder()
                    .url("like")
                    .params("id", id)
                    .params("uid", current.getId())
                    .success(r -> {
                        boolean like = JSON.parseObject(r).getBoolean("like");
                        Toast.makeText(mContext, like + "", Toast.LENGTH_SHORT).show();
                        if (like) {
                            helper.setImageResource(R.id.iv_like_article, R.drawable.like);
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
                            helper.setImageResource(R.id.iv_like_article, R.drawable.liked);
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
                    })
                    .build()
                    .post());

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
                    Toast.makeText(mContext, "展开操作，不真正触发展开操作", Toast.LENGTH_SHORT).show();
                }
            }, false);
        }

    }

    private ArrayList<String> getImgs(String imgUrl) {
        JSONArray array = JSON.parseArray(imgUrl);
        ArrayList<String> imgs = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            imgs.add(array.getString(i));
        }
        return imgs;
    }
}
