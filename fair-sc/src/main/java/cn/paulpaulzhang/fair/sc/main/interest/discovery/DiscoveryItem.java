package cn.paulpaulzhang.fair.sc.main.interest.discovery;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.paulpaulzhang.fair.sc.database.entity.DiscoveryPostCache;
import cn.paulpaulzhang.fair.sc.database.entity.RecommendUserCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间: 7/12/2019
 * 创建人: zlm31
 * 描述:
 */
public class DiscoveryItem implements MultiItemEntity {

    static final int DYNAMIC = 0;

    static final int ARTICLE = 1;

    static final int USER = 2;

    private int itemType;

    private DiscoveryPostCache discoveryPostCache;

    private List<RecommendUserCache> userCaches;

    DiscoveryItem(int itemType, DiscoveryPostCache discoveryPostCache) {
        this.itemType = itemType;
        this.discoveryPostCache = discoveryPostCache;
    }

    DiscoveryItem(int itemType, List<RecommendUserCache> userCaches) {
        this.itemType = itemType;
        this.userCaches = userCaches;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    DiscoveryPostCache getDiscoveryPostCache() {
        return discoveryPostCache;
    }

    public List<RecommendUserCache> getUserCaches() {
        return userCaches;
    }
}
