package cn.paulpaulzhang.fair.sc.main.interest.model;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.model
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class BreakingNews {

    private long bid;

    private String text;

    private String url;

    private long time;

    public BreakingNews(long bid, String text, String url, long time) {
        this.bid = bid;
        this.text = text;
        this.url = url;
        this.time = time;
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
