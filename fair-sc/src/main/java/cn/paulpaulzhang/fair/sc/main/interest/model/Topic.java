package cn.paulpaulzhang.fair.sc.main.interest.model;

import cn.paulpaulzhang.fair.sc.database.Entity.TopicCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.topicCache
 * 创建时间: 7/20/2019
 * 创建人: zlm31
 * 描述:
 */
public class Topic {
    private TopicCache topicCache;

    public Topic(TopicCache topicCache) {
        this.topicCache = topicCache;
    }

    public TopicCache getTopicCache() {
        return topicCache;
    }

    public void setTopicCache(TopicCache topicCache) {
        this.topicCache = topicCache;
    }
}
