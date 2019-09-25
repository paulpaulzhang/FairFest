package cn.paulpaulzhang.fair.sc.main.interest.model;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.model
 * 创建时间：9/25/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TeamManage {

    private long tid;

    private String info;

    private long time;

    public TeamManage(long tid, String info, long time) {
        this.tid = tid;
        this.info = info;
        this.time = time;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
