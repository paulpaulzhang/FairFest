package cn.paulpaulzhang.fair.sc.database.Entity;

import androidx.annotation.NonNull;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * 包名: cn.paulpaulzhang.fair.sc.database.entity
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述: 帖子实体类
 */

@Entity
public class TopicPostCache {

    @Id(assignable = true)
    private long id; //帖子id

    private long uid; //用户id

    private int type; //帖子类型 0为动态 1为文章

    private String title; //标题 动态为空 文章不为空

    private String content; //内容

    private String imagesUrl; //图片url

    private int likeCount; //点赞数

    private int commentCount; //评论数

    private int shareCount; //分享数

    private long time; //发布时间

    private String device; //设备型号

    public TopicPostCache(long id,
                          long uid,
                          int type,
                          String title,
                          String content,
                          String imagesUrl,
                          int likeCount,
                          int commentCount,
                          int shareCount,
                          long time,
                          String device) {
        this.id = id;
        this.uid = uid;
        this.type = type;
        this.title = title;
        this.content = content;
        this.imagesUrl = imagesUrl;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.time = time;
        this.device = device;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @NonNull
    @Override
    public String toString() {
        return "id:" + id + "\n" +
                "uid:" + uid + "\n" +
                "type:" + type + "\n" +
                "title:" + title + "\n" +
                "content:" + content + "\n" +
                "imagesUrl:" + imagesUrl + "\n" +
                "likeCount:" + likeCount + "\n" +
                "commentCount:" + commentCount + "\n" +
                "shareCount:" + shareCount + "\n" +
                "time:" + time + "\n" +
                "device:" + device;
    }
}
