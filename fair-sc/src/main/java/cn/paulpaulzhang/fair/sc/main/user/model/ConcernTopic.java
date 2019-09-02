package cn.paulpaulzhang.fair.sc.main.user.model;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.model
 * 创建时间：9/2/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class ConcernTopic {
    private long id;

    private String name;

    private String img;

    private int follow; //关注数

    private int post; //帖子数

    public ConcernTopic(long id, String name, String img, int follow, int post) {
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
