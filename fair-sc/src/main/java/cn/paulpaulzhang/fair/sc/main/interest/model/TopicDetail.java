package cn.paulpaulzhang.fair.sc.main.interest.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.paulpaulzhang.fair.sc.database.Entity.TopicPostCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.follow
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
public class TopicDetail implements MultiItemEntity {
    public static final int DYNAMIC = 0;

    public static final int ARTICLE = 1;

    private int itemType;

    private TopicPostCache topicPostCache;

    private boolean isLike;

    public TopicDetail(int itemType, TopicPostCache topicPostCache, boolean isLike) {
        this.itemType = itemType;
        this.topicPostCache = topicPostCache;
        this.isLike = isLike;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public TopicPostCache getTopicPostCache() {
        return topicPostCache;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isLike() {
        return isLike;
    }
}
