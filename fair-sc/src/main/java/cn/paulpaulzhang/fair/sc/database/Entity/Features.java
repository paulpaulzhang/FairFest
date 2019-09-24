package cn.paulpaulzhang.fair.sc.database.Entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 包名：cn.paulpaulzhang.fair.sc.database.Entity
 * 创建时间：9/23/19
 * 创建人： paulpaulzhang
 * 描述：
 */
@Entity
public class Features {
    @Id
    private long id;

    private String features;

    public Features() {
    }

    public Features(String features) {
        this.features = features;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }
}
