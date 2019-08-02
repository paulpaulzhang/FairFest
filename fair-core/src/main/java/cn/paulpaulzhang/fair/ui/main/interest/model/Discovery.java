package cn.paulpaulzhang.fair.sc.main.interest.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.paulpaulzhang.fair.sc.database.model.DiscoveryPostCache;

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

    private DiscoveryPostCache discoveryPostCache;

    private List<RecommendUser> userItems;

    public Discovery(int itemType, DiscoveryPostCache discoveryPostCache) {
        this.itemType = itemType;
        this.discoveryPostCache = discoveryPostCache;
    }

    public Discovery(int itemType, List<RecommendUser> userItems) {
        this.itemType = itemType;
        this.userItems = userItems;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public DiscoveryPostCache getDiscoveryPostCache() {
        return discoveryPostCache;
    }

    public List<RecommendUser> getUserItems() {
        return userItems;
    }
}
