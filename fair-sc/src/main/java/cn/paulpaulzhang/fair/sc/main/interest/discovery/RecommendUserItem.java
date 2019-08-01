package cn.paulpaulzhang.fair.sc.main.interest.discovery;

import cn.paulpaulzhang.fair.sc.database.model.RecommendUserCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.discovery
 * 创建时间: 7/22/2019
 * 创建人: zlm31
 * 描述:
 */
public class RecommendUserItem {
    private RecommendUserCache userCache;

    RecommendUserItem(RecommendUserCache userCache) {
        this.userCache = userCache;
    }

    RecommendUserCache getUserCache() {
        return userCache;
    }
}
