package cn.paulpaulzhang.fair.sc.main.interest.model;

import cn.paulpaulzhang.fair.sc.database.Entity.RecommendUserCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.discovery
 * 创建时间: 7/22/2019
 * 创建人: zlm31
 * 描述:
 */
public class RecommendUser {
    private RecommendUserCache userCache;

    public RecommendUser(RecommendUserCache userCache) {
        this.userCache = userCache;
    }

    public RecommendUserCache getUserCache() {
        return userCache;
    }
}
