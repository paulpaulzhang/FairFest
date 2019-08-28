package cn.paulpaulzhang.fair.sc.main.interest.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryPostCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间: 7/12/2019
 * 创建人: zlm31
 * 描述:
 */
public class Discovery implements MultiItemEntity {

    public static final int DYNAMIC = 0;

    public static final int ARTICLE = 1;

    public static final int USER = 2;

    private int itemType;

    private DiscoveryPostCache postCache;

    private List<RecommendUser> userItems;

    private boolean isLike;

    public Discovery(int itemType, DiscoveryPostCache postCache, boolean isLike) {
        this.itemType = itemType;
        this.postCache = postCache;
        this.isLike = isLike;
    }

    public Discovery(int itemType, List<RecommendUser> userItems) {
        this.itemType = itemType;
        this.userItems = userItems;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public DiscoveryPostCache getPostCache() {
        return postCache;
    }

    public List<RecommendUser> getUserItems() {
        return userItems;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
