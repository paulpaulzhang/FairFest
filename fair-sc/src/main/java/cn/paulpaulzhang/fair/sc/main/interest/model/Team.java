package cn.paulpaulzhang.fair.sc.main.interest.model;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.model
 * 创建时间：9/2/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class Team {

    private long uid;

    private String username;

    private String gender;

    private String avatar;

    private String background;

    private String college;

    private String introduction;

    private long time;


    public Team(long uid, String username, String gender, String avatar, String background, String college, String introduction, long time) {
        this.uid = uid;
        this.username = username;
        this.gender = gender;
        this.avatar = avatar;
        this.background = background;
        this.college = college;
        this.introduction = introduction;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
