package cn.paulpaulzhang.fair.sc.main.post.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.main.post.adapter.ViewPagerAdapter;
import cn.paulpaulzhang.fair.util.log.FairLogger;

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
    protected boolean isCollect;

    protected ViewPagerAdapter mPagerAdapter;

    public long getPid() {
        return pid;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
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
