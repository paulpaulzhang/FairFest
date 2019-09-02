package cn.paulpaulzhang.fair.sc.main.post.delegate;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.post.activity.PostActivity;
import cn.paulpaulzhang.fair.sc.main.post.model.Share;
import cn.paulpaulzhang.fair.sc.main.post.adapter.ShareAdapter;
import es.dmoral.toasty.Toasty;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/26/2019
 * 创建人: zlm31
 * 描述:
 */
public class ShareDelegate extends FairDelegate {

    @BindView(R2.id.rv_post)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_post)
    SwipeRefreshLayout mSwipeRefresh;

    private ShareAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_post;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        initSwipeRefresh();
        initRecyclerView();
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> {
            PostActivity activity = (PostActivity) getActivity();
            if (activity != null) {
                activity.initHeader();
            }
        });
    }


    private void initRecyclerView() {
        List<Share> items = new ArrayList<>();


        mAdapter = new ShareAdapter(R.layout.item_share, items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Toasty.info(Objects.requireNonNull(getContext()), "跳转个人主页", Toasty.LENGTH_SHORT).show();
        });
    }
}
