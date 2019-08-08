package cn.paulpaulzhang.fair.sc.main.interest.delegate;

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
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicCache;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TopicAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.interest.model.TopicI;
import io.objectbox.Box;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间：7/10/19
 * 创建人： paulpaulzhang
 * 描述：话题页
 */
public class TopicDelegate extends AbstractDelegate {
    @BindView(R2.id.rv_topic)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_topic)
    SwipeRefreshLayout mSwipeRefresh;

    private TopicAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_topic;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        initSwipeRefresh();
        initRecycler();
        refresh();
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> loadData(Constant.REFRESH_DATA));
    }

    @Override
    public void refresh() {
        mSwipeRefresh.setRefreshing(true);
        loadData(Constant.REFRESH_DATA);
    }

    private void initRecycler() {
        mAdapter = new TopicAdapter(R.layout.view_topic_item, new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            TopicI item = (TopicI) adapter.getItem(position);
            if (item != null) {
                Intent intent = new Intent(getContext(), TopicDetailActivity.class);
                intent.putExtra("name", item.getTopicCache().getName());
                startActivity(intent);
            }
        });
    }

    public void loadData(int type) {
        Box<TopicCache> topicBox = ObjectBox.get().boxFor(TopicCache.class);
        int position = mAdapter.getData().size();

        if (type == Constant.REFRESH_DATA) {
            requestData(0, Constant.REFRESH_DATA);
            List<TopicCache> topicCaches = topicBox.getAll();
            List<TopicI> items = new ArrayList<>();
            long count = Math.min(topicBox.count(), Constant.LOAD_MAX_DATABASE);
            for (int i = 0; i < count; i++) {
                TopicCache topicCache = topicCaches.get(i);
                items.add(new TopicI(topicCache));
            }
            mAdapter.setNewData(items);
            mSwipeRefresh.setRefreshing(false);
        } else if (type == Constant.LOAD_MORE_DATA) {
            long size = topicBox.count();
            if (position + Constant.LOAD_MAX_DATABASE > size) {
                requestData(size, Constant.LOAD_MORE_DATA);
                if (size == topicBox.count()) {
                    mAdapter.loadMoreEnd(true);
                    return;
                }
            }
            List<TopicCache> topicCaches = topicBox.getAll();
            List<TopicI> items = new ArrayList<>();

            long count = Math.min(topicBox.count() - position, Constant.LOAD_MAX_DATABASE);
            for (int i = position; i < count; i++) {
                TopicCache topicCache = topicCaches.get(i);
                items.add(new TopicI(topicCache));
            }
            mAdapter.addData(items);
            mAdapter.loadMoreComplete();
        }
    }

    private void requestData(long start, int type) {
        if (type == Constant.REFRESH_DATA) {
            RestClient.builder()
                    .url("ic_topic")
                    .params("position", 0)
                    .params("number", Constant.LOAD_MAX_SEVER)
                    .success(r -> JsonParseUtil.parseTopic(r, type))
                    .build()
                    .get();
        } else if (type == Constant.LOAD_MORE_DATA) {
            RestClient.builder()
                    .url("ic_topic")
                    .params("position", start)
                    .params("number", Constant.LOAD_MAX_SEVER)
                    .success(r -> JsonParseUtil.parseTopic(r, type))
                    .build()
                    .get();
        }
    }


}
