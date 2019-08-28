package cn.paulpaulzhang.fair.sc.main.interest.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicCache;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TopicAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.interest.model.Topic;
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
        mAdapter = new TopicAdapter(R.layout.item_topic, new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Topic item = (Topic) adapter.getItem(position);
            if (item != null) {
                Intent intent = new Intent(getContext(), TopicDetailActivity.class);
                intent.putExtra("name", item.getTopicCache().getName());
                startActivity(intent);
            }
        });
    }

    private void loadData(int type) {
        Box<TopicCache> topicBox = ObjectBox.get().boxFor(TopicCache.class);

        if (type == Constant.REFRESH_DATA) {
            requestData(response -> {
                JsonParseUtil.parseTopic(response, Constant.REFRESH_DATA);
                List<TopicCache> topicCaches = topicBox.getAll();
                List<Topic> items = new ArrayList<>();
                long count = Math.min(topicBox.count(), Constant.LOAD_MAX_DATABASE);
                for (int i = 0; i < count; i++) {
                    TopicCache topicCache = topicCaches.get(i);
                    items.add(new Topic(topicCache));
                }
                mAdapter.setNewData(items);
                mSwipeRefresh.setRefreshing(false);
            });
        }
    }

    private void requestData(ISuccess success) {
        RestClient.builder()
                .url(Api.TOPIC_LIST)
                .success(success)
                .error((code, msg) -> mSwipeRefresh.setRefreshing(false))
                .build()
                .get();

    }

}
