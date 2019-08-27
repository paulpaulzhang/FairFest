package cn.paulpaulzhang.fair.sc.database.Entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

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

    private String headImg;

    private String overview;

    private float price;

    public ProductCache() {
    }

    public ProductCache(long id, String headImg, String overview, float price) {
        this.id = id;
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
