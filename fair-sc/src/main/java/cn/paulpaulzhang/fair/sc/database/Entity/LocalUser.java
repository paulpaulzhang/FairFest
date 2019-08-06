package cn.paulpaulzhang.fair.sc.database.Entity;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 包名: cn.paulpaulzhang.fair.sc.database.entity
 * 创建时间: 7/3/2019
 * 创建人: zlm31
 * 描述: 用户实体类
 */
@Entity
public class LocalUser {
    @Id(assignable = true)
    private long id; //用户id

    private String username; //用户名

    private String password; //密码

    private String birthday; //生日

    private String gender; //性别

    private int followers; //关注数

    private int fans; //粉丝数

    private String phone; //电话号

    private String email; // 邮箱

    private String school; //学校

    private long studentId; // 学号

    private int permission; //权限组

    private String introduction; // 个人简介

    private String avatar; // 头像url

    private String background; // 背景图片url

    private String time; //注册时间

    private String features; //特征集合

    public LocalUser() {
    }

    public LocalUser(long id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    public LocalUser(long id,
                     String username,
                     String password,
                     String birthday,
                     String gender,
                     int followers,
                     int fans,
                     String phone,
                     String email,
                     String school,
                     long studentId,
                     int permission,
                     String introduction,
                     String avatar,
                     String background,
                     String time,
                     String features) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.followers = followers;
        this.fans = fans;
        this.phone = phone;
        this.email = email;
        this.school = school;
        this.studentId = studentId;
        this.permission = permission;
        this.introduction = introduction;
        this.avatar = avatar;
        this.background = background;
        this.time = time;
        this.features = features;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
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

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }
}
