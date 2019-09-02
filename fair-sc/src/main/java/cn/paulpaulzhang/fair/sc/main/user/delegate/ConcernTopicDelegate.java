package cn.paulpaulzhang.fair.sc.main.user.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicCache;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.TopicAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Topic;
import cn.paulpaulzhang.fair.sc.main.user.adapter.ConcernTopicAdapter;
import cn.paulpaulzhang.fair.sc.main.user.model.ConcernTopic;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user.delegate
 * 创建时间: 8/13/2019
 * 创建人: zlm31
 * 描述:
 */
public class ConcernTopicDelegate extends FairDelegate {

    @BindView(R2.id.rv_topic)
    RecyclerView mRecyclerView;

    private ConcernTopicAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_concern_topic;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        initRecycler();
        loadData();
    }

    private void initRecycler() {
        mAdapter = new ConcernTopicAdapter(R.layout.item_topic, new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ConcernTopic item = (ConcernTopic) adapter.getItem(position);
            if (item != null) {
                Intent intent = new Intent(getContext(), TopicDetailActivity.class);
                intent.putExtra("name", item.getName());
                startActivity(intent);
            }
        });
    }

    public void loadData() {
        requestData(response -> {
            String result = JSON.parseObject(response).getString("result");
            if (TextUtils.equals(result, "ok")) {
                JSONArray array = JSON.parseObject(response).getJSONArray("topics");
                List<ConcernTopic> topics = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    long id = object.getLong("tid");
                    String name = object.getString("tname");
                    String img = object.getString("imageUrl");
                    int follow = object.getIntValue("payCount");
                    int post = object.getIntValue("postCount");
                    topics.add(new ConcernTopic(id, name, img, follow, post));
                }
                mAdapter.setNewData(topics);
            }
        });

    }

    private void requestData(ISuccess success) {
        RestClient.builder()
                .url(Api.GET_TOPICS_BY_UID)
                .success(success)
                .error((code, msg) -> Toasty.error(Objects.requireNonNull(getContext()), "加载失败 " + code, Toasty.LENGTH_SHORT).show())
                .build()
                .get();

    }
}
