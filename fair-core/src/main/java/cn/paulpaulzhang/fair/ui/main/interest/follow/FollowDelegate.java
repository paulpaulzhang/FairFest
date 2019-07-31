package cn.paulpaulzhang.fair.ui.main.interest.follow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.paulpaulzhang.fair.base.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.R;
import cn.paulpaulzhang.fair.R2;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.database.ObjectBox;
import cn.paulpaulzhang.fair.database.entity.FollowPostCache;
import cn.paulpaulzhang.fair.json.JsonParseUtil;
import cn.paulpaulzhang.fair.ui.main.post.ArticleActivity;
import cn.paulpaulzhang.fair.ui.main.post.DynamicActivity;
import io.objectbox.Box;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间：7/10/19
 * 创建人： paulpaulzhang
 * 描述：关注页
 */
public class FollowDelegate extends FairDelegate {
    @BindView(R2.id.rv_follow)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_follow)
    SwipeRefreshLayout mSwipeRefresh;

    private FollowAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_follow;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        initSwipeRefresh();
        initRecyclerView();
        mSwipeRefresh.setRefreshing(true);
        loadData(Constant.REFRESH_DATA);
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> loadData(Constant.REFRESH_DATA));
    }

    private void initRecyclerView() {
        mAdapter = new FollowAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            FollowItem item = (FollowItem) adapter.getItem(position);
            if (item != null) {
                if (item.getItemType() == FollowItem.DYNAMIC) {
                    Intent intent = new Intent(getContext(), DynamicActivity.class);
                    intent.putExtra("id", item.getFollowPostCache().getId());
                    startActivity(intent);
                } else if (item.getItemType() == FollowItem.ARTICLE) {
                    Intent intent = new Intent(getContext(), ArticleActivity.class);
                    intent.putExtra("id", item.getFollowPostCache().getId());
                    startActivity(intent);
                }

            }
        });
    }

    public void loadData(int type) {
        Box<FollowPostCache> postBox = ObjectBox.get().boxFor(FollowPostCache.class);
        int position = mAdapter.getData().size();
        if (type == Constant.REFRESH_DATA) {
            requestData(0, Constant.REFRESH_DATA);
            List<FollowPostCache> followPostCaches = postBox.getAll();
            List<FollowItem> items = new ArrayList<>();
            long count = Math.min(postBox.count(), Constant.LOAD_MAX_DATABASE);
            for (int i = 0; i < count; i++) {
                FollowPostCache followPostCache = followPostCaches.get(i);
                items.add(new FollowItem(followPostCache.getType(), followPostCache));
            }
            mAdapter.setNewData(items);
            mSwipeRefresh.setRefreshing(false);

        } else if (type == Constant.LOAD_MORE_DATA) {
            long size = postBox.count();
            if (position + Constant.LOAD_MAX_DATABASE > size) {
                requestData(size, Constant.LOAD_MORE_DATA);
                if (size == postBox.count()) {
                    mAdapter.loadMoreEnd(true);
                    return;
                }
            }
            List<FollowPostCache> followPostCaches = postBox.getAll();
            List<FollowItem> items = new ArrayList<>();

            long count = Math.min(postBox.count() - position, Constant.LOAD_MAX_DATABASE);
            for (int i = position; i < count; i++) {
                FollowPostCache followPostCache = followPostCaches.get(i);
                items.add(new FollowItem(followPostCache.getType(), followPostCache));
            }
            mAdapter.addData(items);
            mAdapter.loadMoreComplete();
        }
    }

    /**
     * 从服务器下载数据到数据库
     *
     * @param start 开始位置
     * @param type  加载类型（刷新数据，加载更多）
     */
    private void requestData(long start, int type) {
        if (type == Constant.REFRESH_DATA) {
            RestClient.builder()
                    .url("post")
                    .params("position", 0)
                    .params("number", Constant.LOAD_MAX_SEVER)
                    .success(r -> JsonParseUtil.parseFollowPost(r, type))
                    .build()
                    .get();
        } else if (type == Constant.LOAD_MORE_DATA) {
            RestClient.builder()
                    .url("post")
                    .params("position", start)
                    .params("number", Constant.LOAD_MAX_SEVER)
                    .success(r -> JsonParseUtil.parseFollowPost(r, type))
                    .build()
                    .get();
        }
    }
}
