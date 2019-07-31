package cn.paulpaulzhang.fair.database.entity;

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
public class TopicCache {
    @Id(assignable = true)
    private long id;

    private String name;

    private String img;

    private int follow; //关注数

    private int post; //帖子数

    public TopicCache(long id, String name, String img, int follow, int post) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.follow = follow;
        this.post = post;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }
}
