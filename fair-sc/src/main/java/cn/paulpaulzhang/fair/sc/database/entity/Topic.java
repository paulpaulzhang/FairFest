package cn.paulpaulzhang.fair.sc.database.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 包名: cn.paulpaulzhang.fair.sc.database.entity
 * 创建时间: 7/16/2019
 * 创建人: zlm31
 * 描述:
 */
@Entity
public class Topic {
    @Id
    private long id;

    private String name;

    private int follow; //关注数

    private int post; //帖子数

    public Topic(String name, int follow, int post) {
        this.name = name;
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
