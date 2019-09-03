package cn.paulpaulzhang.fair.sc.main.interest.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.BigEventAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TeamAdapter;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.activity
 * 创建时间：9/2/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TeamActivity extends FairActivity {

    @BindView(R2.id.rv_team)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_team)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.tb_team)
    MaterialToolbar mToolbar;

    private TeamAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_team;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        initToolbar(mToolbar, getString(R.string.team));

        mAdapter = new TeamAdapter(R.layout.item_team, new ArrayList<>());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefresh.setOnRefreshListener(() -> mSwipeRefresh.setRefreshing(false));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
