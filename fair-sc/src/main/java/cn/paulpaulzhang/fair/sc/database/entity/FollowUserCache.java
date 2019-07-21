package cn.paulpaulzhang.fair.sc.database.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 包名: cn.paulpaulzhang.fair.sc.database.entity
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
@Entity
public class FollowUserCache {
    @Id(assignable = true)
    private long id; //用户id

    private String username; //用户名

    private String birthday; //生日

    private String gender; //性别

    private int followers; //关注数

    private int fans; //粉丝数

    private String school; //学校

    private String introduction; // 个人简介

    private String avatar; // 头像url

    private String background; // 背景图片url

    private String time; //注册时间

    public FollowUserCache(long id,
                           String username,
                           String birthday,
                           String gender,
                           int followers,
                           int fans,
                           String school,
                           String introduction,
                           String avatar,
                           String background,
                           String time) {
        this.id = id;
        this.username = username;
        this.birthday = birthday;
        this.gender = gender;
        this.followers = followers;
        this.fans = fans;
        this.school = school;
        this.introduction = introduction;
        this.avatar = avatar;
        this.background = background;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
