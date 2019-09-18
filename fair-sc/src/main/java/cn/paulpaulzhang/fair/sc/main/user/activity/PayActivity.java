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
import cn.paulpaulzhang.fair.sc.main.user.adapter.UserAdapter;
import cn.paulpaulzhang.fair.sc.main.user.model.User;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.activity
 * 创建时间：9/18/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class PayActivity extends FairActivity {

    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    @BindView(R2.id.srl_user)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.rv_user)
    RecyclerView mRecyclerView;

    private long uid;

    private UserAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_user_pay_fans;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initToolbar(mToolbar, "关注");
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        uid = getIntent().getLongExtra("uid", -1);

        initSwipeRefresh();
        initRecycler();
        if (uid != -1) {
            mSwipeRefresh.setRefreshing(true);
            loadData(Constant.REFRESH_DATA);
        }

    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> loadData(Constant.REFRESH_DATA));
    }

    private void initRecycler() {
        mAdapter = new UserAdapter(R.layout.item_user_pay_fans, new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            User item = (User) adapter.getItem(position);
            if (item != null) {
                Intent intent = new Intent(this, UserCenterActivity.class);
                intent.putExtra("uid", item.getUid());
                startActivity(intent);
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
                List<User> items = new ArrayList<>();
                JSONArray users = JSON.parseObject(response).getJSONArray("list");
                for (int i = 0; i < users.size(); i++) {
                    JSONObject object = users.getJSONObject(i);
                    long uid = object.getLongValue("uid");
                    String username = object.getString("username");
                    int followers = object.getIntValue("followers");
                    int fans = object.getIntValue("fans");
                    String avatar = object.getString("avatar");
                    User user = new User(uid, username, followers, fans, avatar);
                    items.add(user);
                }
                mAdapter.setNewData(items);
                mSwipeRefresh.setRefreshing(false);
            });
        }
    }

    private void requestData(ISuccess success) {
        RestClient.builder()
                .url(Api.GET_PAY_USER_LIST)
                .params("uid", uid)
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
