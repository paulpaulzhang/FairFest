package cn.paulpaulzhang.fair.sc.main.post.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.common.PhotoActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.PostActivity;
import cn.paulpaulzhang.fair.sc.main.post.model.Comment;
import cn.paulpaulzhang.fair.sc.main.post.adapter.CommentAdapter;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import es.dmoral.toasty.Toasty;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/26/2019
 * 创建人: zlm31
 * 描述:
 */
public class CommentDelegate extends FairDelegate {

    @BindView(R2.id.rv_post)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_post)
    SwipeRefreshLayout mSwipeRefresh;

    private CommentAdapter mAdapter;
    private long pid;

    @Override
    public Object setLayout() {
        return R.layout.delegate_post;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        PostActivity activity = (PostActivity) getActivity();
        if (activity != null) {
            pid = activity.getPid();
        }

        initSwipeRefresh();
        initRecyclerView();
        loadData();
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> {
            loadData();
            PostActivity activity = (PostActivity) getActivity();
            if (activity != null) {
                activity.initHeader();
            }
        });
    }


    private void initRecyclerView() {
        List<Comment> items = new ArrayList<>();
        mAdapter = new CommentAdapter(R.layout.item_comment, items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Comment item = (Comment) adapter.getItem(position);
            if (item != null) {
                if (view.getId() == R.id.dv_img) {
                    Intent intent = new Intent(getContext(), PhotoActivity.class);
                    intent.putExtra("path", item.getImgUrl());
                    //noinspection unchecked
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity())).toBundle());
                }

            }
        });
    }

    private void loadData() {
        requestData(response -> {
            String result = JSON.parseObject(response).getString("result");
            if (TextUtils.equals(result, "ok")) {
                List<Comment> comments = new ArrayList<>();
                JSONArray array = JSON.parseObject(response).getJSONArray("comment");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String avatar = object.getString("avatar");
                    String content = object.getString("content");
                    String img = object.getString("imagesUrl");
                    long time = object.getLong("commentTime");
                    long uid = object.getLong("uid");
                    String username = object.getString("username");
                    comments.add(new Comment(uid, avatar, username, content, img, time));
                }
                mAdapter.setNewData(comments);
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void requestData(ISuccess success) {
        RestClient.builder()
                .url(Api.GET_USER_COMMENT_BY_PID)
                .params("pid", pid)
                .success(success)
                .error((code, msg) -> Toasty.error(Objects.requireNonNull(getContext()), "加载失败", Toasty.LENGTH_SHORT).show())
                .build()
                .get();
    }
}
