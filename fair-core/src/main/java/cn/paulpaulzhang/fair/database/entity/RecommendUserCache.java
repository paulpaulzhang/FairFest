package cn.paulpaulzhang.fair.database.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * 包名: cn.paulpaulzhang.fair.sc.database.entity
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
@Entity
public class RecommendUserCache {
    @Id(assignable = true)
    private long id; //用户id

    private String username; //用户名

    private int followers; //关注数

    private int fans; //粉丝数

    private String avatar; //头像url

    private String time; //注册时间

    public RecommendUserCache(long id,
                              String username,
                              int followers,
                              int fans,
                              String avatar,
                              String time) {
        this.id = id;
        this.username = username;
        this.followers = followers;
        this.fans = fans;
        this.avatar = avatar;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
