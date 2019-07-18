package cn.paulpaulzhang.fair.sc.database.entity;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * 包名: cn.paulpaulzhang.fair.sc.database.entity
 * 创建时间: 7/16/2019
 * 创建人: zlm31
 * 描述:
 */
@Entity
public class User {
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

    private Date time; //注册时间

    public User() {
    }

    public User(long id,
                String username,
                String birthday,
                String gender,
                int followers,
                int fans,
                String school,
                String introduction,
                String avatar,
                String background,
                Date time) {
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
