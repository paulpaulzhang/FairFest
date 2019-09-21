package cn.paulpaulzhang.fair.sc.main.interest.model;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.model
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class Announcement {

    private long aid;

    private String text;

    private long time;

    public Announcement(long aid, String text, long time) {
        this.aid = aid;
        this.text = text;
        this.time = time;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
