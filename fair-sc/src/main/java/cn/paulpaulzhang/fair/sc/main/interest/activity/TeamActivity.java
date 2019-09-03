package cn.paulpaulzhang.fair.sc.main.interest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TeamAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Team;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;

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

        initSwipeRefresh();
        initRecyclerView();

    }

    private void initRecyclerView() {
        mAdapter = new TeamAdapter(R.layout.item_team, new ArrayList<>());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Team item = (Team) adapter.getItem(position);
            if (item != null) {
                Intent intent = new Intent(this, UserCenterActivity.class);
                intent.putExtra("uid", item.getUid());
                startActivity(intent);
            }
        });
    }

    private void initSwipeRefresh() {
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
