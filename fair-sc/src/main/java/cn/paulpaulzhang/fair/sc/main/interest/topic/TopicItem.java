package cn.paulpaulzhang.fair.sc.main.interest.topic;

import cn.paulpaulzhang.fair.sc.database.entity.TopicCache;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.topicCache
 * 创建时间: 7/20/2019
 * 创建人: zlm31
 * 描述:
 */
public class TopicItem {
    private TopicCache topicCache;

    public TopicItem(TopicCache topicCache) {
        this.topicCache = topicCache;
    }

    public TopicCache getTopicCache() {
        return topicCache;
    }

    public void setTopicCache(TopicCache topicCache) {
        this.topicCache = topicCache;
    }
}
