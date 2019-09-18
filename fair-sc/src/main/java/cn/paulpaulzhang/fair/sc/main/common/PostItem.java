package cn.paulpaulzhang.fair.sc.main.common;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.paulpaulzhang.fair.sc.database.Entity.PostCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.follow
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
public class PostItem implements MultiItemEntity {
    public static final int DYNAMIC = 0;

    public static final int ARTICLE = 1;

    private int itemType;

    private PostCache postCache;

    private boolean isLike;

    public PostItem(int itemType, PostCache postCache, boolean isLike) {
        this.itemType = itemType;
        this.postCache = postCache;
        this.isLike = isLike;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public PostCache getPostCache() {
        return postCache;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isLike() {
        return isLike;
    }
}
