package cn.paulpaulzhang.fair.sc.main.interest;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.paulpaulzhang.fair.sc.database.entity.Post;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间: 7/12/2019
 * 创建人: zlm31
 * 描述:
 */
public class DiscoveryItem implements MultiItemEntity {

    public static final int DYNAMIC = 0;

    public static final int ARTICLE = 1;

    private int itemType;

    private Post post;

    public DiscoveryItem(int itemType, Post post) {
        this.itemType = itemType;
        this.post = post;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
