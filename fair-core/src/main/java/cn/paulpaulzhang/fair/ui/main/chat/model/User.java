package cn.paulpaulzhang.fair.sc.main.chat.model;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：8/1/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class User implements IUser {

    private String id;
    private String name;
    private String avatar;
    private boolean online;

    public User(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }
}
