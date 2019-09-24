package cn.paulpaulzhang.fair.sc.main.post.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.post.adapter.ViewPagerAdapter;
import cn.paulpaulzhang.fair.sc.main.post.delegate.CommentDelegate;
import cn.paulpaulzhang.fair.sc.main.post.delegate.LikeDelegate;
import cn.paulpaulzhang.fair.sc.main.post.delegate.ShareDelegate;
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

    protected void initSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout, ViewPagerAdapter pagerAdapter, AppBarLayout appBar) {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            initHeader();
            initChildData(pagerAdapter);
        });

        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset >= 0) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        });
    }

    protected void initChildData(ViewPagerAdapter pagerAdapter) {
        LikeDelegate likeDelegate = (LikeDelegate) pagerAdapter.getItem(0);
        likeDelegate.loadData();

        CommentDelegate commentDelegate = (CommentDelegate) pagerAdapter.getItem(1);
        commentDelegate.loadData();

        ShareDelegate shareDelegate = (ShareDelegate) pagerAdapter.getItem(2);
        shareDelegate.loadData();
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
