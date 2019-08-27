package cn.paulpaulzhang.fair.sc.database.Entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 包名: cn.paulpaulzhang.fair.sc.database.entity
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
@Entity
public class UserCache {
    @Id(assignable = true)
    private long id; //用户id

    private String username; //用户名

    private String avatar; // 头像url

    public UserCache(long id, String username, String avatar) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
