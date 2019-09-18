package cn.paulpaulzhang.fair.sc.database.Entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * 包名：cn.paulpaulzhang.fair.sc.database.Entity
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */

@Entity
public class ProductCache {

    @Id(assignable = true)
    private long id;

    private long uid;

    private long time;

    private String sname;  //商品名称

    private String headImg;  //商品image

    private String overview; //商品信息

    private float price;  //商品价格

    private int isSold;  //是否售出

    public ProductCache() {
    }

    public ProductCache(long id, long uid, long time, String sname, String headImg, String overview, float price, int isSold) {
        this.id = id;
        this.uid = uid;
        this.time = time;
        this.sname = sname;
        this.headImg = headImg;
        this.overview = overview;
        this.price = price;
        this.isSold = isSold;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public int getIsSold() {
        return isSold;
    }

    public void setIsSold(int isSold) {
        this.isSold = isSold;
    }
}
