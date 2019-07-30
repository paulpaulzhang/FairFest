package cn.paulpaulzhang.fair.sc.main.interest.follow;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.paulpaulzhang.fair.sc.database.entity.FollowPostCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.follow
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
public class FollowItem implements MultiItemEntity {
    static final int DYNAMIC = 0;

    static final int ARTICLE = 1;

    private int itemType;

    private FollowPostCache followPostCache;

    FollowItem(int itemType, FollowPostCache followPostCache) {
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
