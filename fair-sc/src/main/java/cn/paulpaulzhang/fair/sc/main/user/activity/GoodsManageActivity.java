package cn.paulpaulzhang.fair.sc.main.user.activity;

import android.content.Intent;
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
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.user.adapter.GoodsManageAdapter;
import cn.paulpaulzhang.fair.sc.main.user.adapter.UserAdapter;
import cn.paulpaulzhang.fair.sc.main.user.model.Goods;
import cn.paulpaulzhang.fair.sc.main.user.model.User;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.activity
 * 创建时间：9/18/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class GoodsManageActivity extends FairActivity {

    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    @BindView(R2.id.srl_manage)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.rv_manage)
    RecyclerView mRecyclerView;

    private GoodsManageAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_goods_manage;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initToolbar(mToolbar, "宝贝管理");
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();


        initSwipeRefresh();
        initRecycler();
        mSwipeRefresh.setRefreshing(true);
        loadData(Constant.REFRESH_DATA);

    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> loadData(Constant.REFRESH_DATA));
    }

    private void initRecycler() {
        mAdapter = new GoodsManageAdapter(R.layout.item_user_goods, new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Goods item = (Goods) adapter.getItem(position);
            if (item != null) {
//                Intent intent = new Intent(this, UserCenterActivity.class);
//                intent.putExtra("sid", item.getSid());
//                startActivity(intent);
            }
        });
    }

    private void loadData(int type) {

        if (type == Constant.REFRESH_DATA) {
            requestData(response -> {
                String result = JSON.parseObject(response).getString("result");
                if (!TextUtils.equals(result, "ok")) {
                    mSwipeRefresh.setRefreshing(false);
                    Toasty.error(this, "加载失败", Toasty.LENGTH_SHORT).show();
                    return;
                }
                List<Goods> items = new ArrayList<>();
                JSONArray users = JSON.parseObject(response).getJSONArray("list");
                for (int i = 0; i < users.size(); i++) {
                    JSONObject object = users.getJSONObject(i);
                    long sid = object.getLongValue("sid");
                    String sname = object.getString("sname");
                    String headImg = object.getString("headImg");
                    String overview = object.getString("overview");
                    float price = object.getFloatValue("price");
                    long time = object.getLongValue("time");
                    int isSold = object.getIntValue("issold");
                    long uid = object.getLongValue("uid");
                    Goods goods = new Goods(sid, sname, headImg, overview, price, time, isSold, uid);
                    items.add(goods);
                }
                mAdapter.setNewData(items);
                mSwipeRefresh.setRefreshing(false);
            });
        }
    }

    private void requestData(ISuccess success) {
        RestClient.builder()
                .url(Api.GET_STORE_BY_UID)
                .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                .success(success)
                .error((code, msg) -> mSwipeRefresh.setRefreshing(false))
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
