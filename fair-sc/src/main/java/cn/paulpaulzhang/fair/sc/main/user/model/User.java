package cn.paulpaulzhang.fair.sc.main.user.model;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.model
 * 创建时间：9/18/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class User {
    private long uid;

    private String username;

    private int followers;

    private int fans;

    private String avatar;

    public User(long uid, String username, int followers, int fans, String avatar) {
        this.uid = uid;
        this.username = username;
        this.followers = followers;
        this.fans = fans;
        this.avatar = avatar;
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

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
