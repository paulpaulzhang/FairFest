package cn.paulpaulzhang.fair.sc.main.user.model;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.model
 * 创建时间：9/18/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class Goods {
    private long sid;

    private String sname;

    private String headImg;

    private String overview;

    private float price;

    private long time;

    private int isSold;

    private long uid;

    public Goods(long sid, String sname, String headImg, String overview, float price, long time, int isSold, long uid) {
        this.sid = sid;
        this.sname = sname;
        this.headImg = headImg;
        this.overview = overview;
        this.price = price;
        this.time = time;
        this.isSold = isSold;
        this.uid = uid;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getIsSold() {
        return isSold;
    }

    public void setIsSold(int isSold) {
        this.isSold = isSold;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
