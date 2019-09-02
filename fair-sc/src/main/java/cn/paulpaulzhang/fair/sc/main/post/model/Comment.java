package cn.paulpaulzhang.fair.sc.main.post.model;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/26/2019
 * 创建人: zlm31
 * 描述:
 */
public class Comment {
    private String avatarUrl;

    private String username;

    private String content;

    private long time;

    private String imgUrl;

    private long uid;

    public Comment(long uid, String avatarUrl, String username, String content, String imgUrl, long time) {
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.content = content;
        this.time = time;
        this.imgUrl = imgUrl;
        this.uid = uid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
