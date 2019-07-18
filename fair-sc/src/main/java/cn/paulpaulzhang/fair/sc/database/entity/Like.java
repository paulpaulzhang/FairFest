package cn.paulpaulzhang.fair.sc.database.entity;

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
public class Like {
    @Id
    private long id;

    private long pid;

    private boolean like;

    public Like(long pid, boolean like) {
        this.pid = pid;
        this.like = like;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isLike() {
        return like;
    }
}
