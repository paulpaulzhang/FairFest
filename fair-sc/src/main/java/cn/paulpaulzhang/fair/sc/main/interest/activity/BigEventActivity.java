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
import java.util.List;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.BigEventAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.BigEvent;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.activity
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class BigEventActivity extends FairActivity {

    @BindView(R2.id.rv_big_event)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_big_event)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.tb_big_event)
    MaterialToolbar mToolbar;

    private BigEventAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_big_event;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        initToolbar(mToolbar, getString(R.string.big_event));

        mAdapter = new BigEventAdapter(R.layout.item_big_event, create());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefresh.setOnRefreshListener(() -> mSwipeRefresh.setRefreshing(false));

    }

    private List<BigEvent> create() {
        List<BigEvent> events = new ArrayList<>();
        events.add(new BigEvent("校园大事件由校园派官方团队统计并发布，包括校园热点新闻，社会热点，师生关注度较高的内容，每日定时更新"));
        return events;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
