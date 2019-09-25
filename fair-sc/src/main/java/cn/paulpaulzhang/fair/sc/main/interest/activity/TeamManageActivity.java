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
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TeamManageAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.TeamManage;
import cn.paulpaulzhang.fair.sc.main.market.activity.GoodsDetailsActivity;
import cn.paulpaulzhang.fair.sc.main.user.adapter.GoodsManageAdapter;
import cn.paulpaulzhang.fair.sc.main.user.model.Goods;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.activity
 * 创建时间：9/25/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TeamManageActivity extends FairActivity {

    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    @BindView(R2.id.srl_manage)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.rv_manage)
    RecyclerView mRecyclerView;

    private TeamManageAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_team_manage;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initToolbar(mToolbar, getString(R.string.team_manage));
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();


        initSwipeRefresh();
        initRecycler();
        mSwipeRefresh.setRefreshing(true);
        loadData();
    }


    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(this::loadData);
    }

    private void initRecycler() {
        mAdapter = new TeamManageAdapter(R.layout.item_team_manage, new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> Toasty.info(this, "长按取消该组队申请", Toasty.LENGTH_SHORT).show());
        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            TeamManage item = (TeamManage) adapter.getItem(position);
            if (item != null) {
                RestClient.builder()
                        .url(Api.CANCEL_TEAM)
                        .params("tid", item.getTid())
                        .success(response -> {
                            String result = JSON.parseObject(response).getString("result");
                            if (TextUtils.equals(result, "ok")) {
                                mAdapter.remove(position);
                            }
                        })
                        .error((code, msg) -> Toasty.error(this, "取消失败" + code, Toasty.LENGTH_SHORT).show())
                        .build()
                        .post();
            }
            return true;
        });
    }

    private void loadData() {
        RestClient.builder()
                .url(Api.GET_TEAM_LIST_BY_USER)
                .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        JSONArray array = JSONObject.parseObject(response).getJSONArray("teammessage");
                        List<TeamManage> items = new ArrayList<>();
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            long tid = object.getLongValue("tid");
                            String info = object.getString("message");
                            long time = object.getLongValue("time");
                            items.add(new TeamManage(tid, info, time));
                        }
                        mAdapter.setNewData(items);
                        mSwipeRefresh.setRefreshing(false);
                    }
                })
                .error((code, msg) -> Toasty.error(this, "加载失败 " + code, Toasty.LENGTH_SHORT).show())
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
