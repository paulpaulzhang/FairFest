package cn.paulpaulzhang.fair.sc.main.chat.model;

import com.stfalcon.chatkit.commons.models.IUser;

import cn.jpush.im.android.api.model.UserInfo;

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
    private UserInfo userInfo;

    public User(String id, String name, String avatar, boolean online, UserInfo userInfo) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
