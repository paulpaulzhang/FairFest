package cn.paulpaulzhang.fair.sc.database.Entity;

import androidx.annotation.NonNull;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 包名: cn.paulpaulzhang.fair.sc.database.entity
 * 创建时间: 7/21/2019
 * 创建人: zlm31
 * 描述:
 */
@Entity
public class LikeCache {
    @Id
    private long id;

    private long pid;

    private boolean isLike;

    public LikeCache() {
    }

    public LikeCache(long pid, boolean isLike) {
        this.pid = pid;
        this.isLike = isLike;
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
        this.isLike = like;
    }

    public boolean isLike() {
        return isLike;
    }

    @NonNull
    @Override
    public String toString() {
        return pid + "  " + isLike;
    }
}
