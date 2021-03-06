package cn.paulpaulzhang.fair.sc.main.interest.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import cn.paulpaulzhang.fair.sc.main.interest.adapter.AnnouncementAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Announcement;
import es.dmoral.toasty.Toasty;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.activity
 * 创建时间：9/3/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class AnnouncementActivity extends FairActivity {

    @BindView(R2.id.rv_announcement)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_announcement)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    private AnnouncementAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_announcement;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        initToolbar(mToolbar, getString(R.string.announcement));

        initSwipeRefresh();
        initRecyclerView();
        loadData();
        mSwipeRefresh.setRefreshing(true);

    }

    private void initRecyclerView() {
        mAdapter = new AnnouncementAdapter(R.layout.item_announcement, new ArrayList<>());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        RestClient.builder()
                .url(Api.GET_ANNOUNCEMENT_LIST)
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        List<Announcement> announcements = new ArrayList<>();
                        JSONArray array = JSON.parseObject(response).getJSONArray("list");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long aid = object.getLongValue("aid");
                            String text = object.getString("acontent");
                            long time = object.getLongValue("time");
                            announcements.add(new Announcement(aid, text, time));
                        }
                        mAdapter.setNewData(announcements);
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
