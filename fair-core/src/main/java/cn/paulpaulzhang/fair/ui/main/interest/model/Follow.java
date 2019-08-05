package cn.paulpaulzhang.fair.sc.main.interest.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.paulpaulzhang.fair.sc.database.Entity.FollowPostCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.follow
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
public class Follow implements MultiItemEntity {
    public static final int DYNAMIC = 0;

    public static final int ARTICLE = 1;

    private int itemType;

    private FollowPostCache followPostCache;

    public Follow(int itemType, FollowPostCache followPostCache) {
        this.itemType = itemType;
        this.followPostCache = followPostCache;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public FollowPostCache getFollowPostCache() {
        return followPostCache;
    }
}
