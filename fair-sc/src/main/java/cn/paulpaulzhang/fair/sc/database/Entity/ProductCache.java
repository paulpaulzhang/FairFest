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

    private String headImg;  //商品image

    private String overview; //商品名称 etc

    private float price;  //商品价格

    public ProductCache() {
    }

    public ProductCache(long id, long uid, long time, String headImg, String overview, float price) {
        this.id = id;
        this.uid = uid;
        this.time = time;
        this.headImg = headImg;
        this.overview = overview;
        this.price = price;
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
}
