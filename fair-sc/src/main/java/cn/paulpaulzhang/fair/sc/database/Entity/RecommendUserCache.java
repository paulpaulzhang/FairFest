package cn.paulpaulzhang.fair.sc.database.Entity;

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

    private int payCount; //关注数

    private int fansCount; //粉丝数

    private int dynamicCount; //动态数

    private String avatar; //头像url

    private String time; //注册时间

    public RecommendUserCache(long id,
                              String username,
                              int payCount,
                              int fansCount,
                              String avatar,
                              String time) {
        this.id = id;
        this.username = username;
        this.payCount = payCount;
        this.fansCount = fansCount;
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

    public int getPayCount() {
        return payCount;
    }

    public void setPayCount(int payCount) {
        this.payCount = payCount;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getDynamicCount() {
        return dynamicCount;
    }

    public void setDynamicCount(int dynamicCount) {
        this.dynamicCount = dynamicCount;
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
