package cn.paulpaulzhang.fair.sc.main.interest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.BreakingNewsAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.BreakingNews;
import cn.paulpaulzhang.fair.sc.main.web.WebActivity;
import es.dmoral.toasty.Toasty;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.activity
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class BreakingNewsActivity extends FairActivity {

    @BindView(R2.id.rv_big_event)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_big_event)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.tb_big_event)
    MaterialToolbar mToolbar;

    private BreakingNewsAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_big_event;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        initToolbar(mToolbar, getString(R.string.big_event));

        initSwipeRefresh();
        initRecyclerView();
        loadData();
        mSwipeRefresh.setRefreshing(true);

    }

    private void initRecyclerView() {
        mAdapter = new BreakingNewsAdapter(R.layout.item_event, new ArrayList<>());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            BreakingNews news = (BreakingNews) adapter.getItem(position);
            if (news != null) {
                if (news.getUrl() == null || news.getUrl().isEmpty()) {
                    return;
                }
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", news.getUrl());
                startActivity(intent);
            }
        });
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        RestClient.builder()
                .url(Api.GET_BREAKING_NEWS_LIST)
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        List<BreakingNews> breakingNews = new ArrayList<>();
                        JSONArray array = JSON.parseObject(response).getJSONArray("list");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long bid = object.getLongValue("bid");
                            String text = object.getString("content");
                            String url = object.getString("url");
                            long time = object.getLongValue("time");
                            breakingNews.add(new BreakingNews(bid, text, url, time));
                        }
                        mAdapter.setNewData(breakingNews);
                    } else {
                        Toasty.error(this, "加载失败 ", Toasty.LENGTH_SHORT).show();
                    }
                    mSwipeRefresh.setRefreshing(false);
                })
                .error((code, msg) -> {
                    Toasty.error(this, "加载失败 " + code, Toasty.LENGTH_SHORT).show();
                    mSwipeRefresh.setRefreshing(false);
                })
                .build()
                .get();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
