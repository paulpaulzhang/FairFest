package cn.paulpaulzhang.fair.sc.main.post.activity;

import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.main.post.adapter.ViewPagerAdapter;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.post.activity
 * 创建时间：9/2/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public abstract class PostActivity extends FairActivity {
    protected long pid;
    protected long uid;
    protected boolean isLike;

    protected ViewPagerAdapter mPagerAdapter;
    protected List<Long> topicIdList = new ArrayList<>();

    public long getPid() {
        return pid;
    }

    public abstract void initHeader();

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
