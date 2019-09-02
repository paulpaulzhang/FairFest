package cn.paulpaulzhang.fair.sc.main.post.model;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/26/2019
 * 创建人: zlm31
 * 描述:
 */
public class Like {

    private long uid;

    private String username;

    private String avatar;

    private long time;

    public Like(long uid, String username, String avatar, long time) {
        this.uid = uid;
        this.username = username;
        this.avatar = avatar;
        this.time = time;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
