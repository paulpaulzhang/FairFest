package cn.paulpaulzhang.fair.sc.main.user.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.paulpaulzhang.fair.sc.database.Entity.PostCache;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.model
 * 创建时间：8/26/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class Dynamic implements MultiItemEntity {

    public static final int DYNAMIC = 0;

    public static final int ARTICLE = 1;

    private int itemType;

    private PostCache postCache;

    private boolean isLike;

    public Dynamic(int itemType, PostCache postCache, boolean isLike) {
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

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
