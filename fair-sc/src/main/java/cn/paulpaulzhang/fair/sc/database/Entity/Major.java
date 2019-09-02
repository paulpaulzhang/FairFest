package cn.paulpaulzhang.fair.sc.database.Entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 包名：cn.paulpaulzhang.fair.sc.database.Entity
 * 创建时间：9/1/19
 * 创建人： paulpaulzhang
 * 描述：
 */
@Entity
public class Major {
    @Id
    private long id;

    private String name;

    public Major() {
    }

    public Major(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
