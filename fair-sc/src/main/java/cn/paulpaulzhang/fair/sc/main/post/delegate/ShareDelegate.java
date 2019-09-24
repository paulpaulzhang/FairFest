package cn.paulpaulzhang.fair.sc.main.post.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
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
import cn.paulpaulzhang.fair.sc.main.post.activity.PostActivity;
import cn.paulpaulzhang.fair.sc.main.post.adapter.LikeAdapter;
import cn.paulpaulzhang.fair.sc.main.post.model.Like;
import cn.paulpaulzhang.fair.sc.main.post.model.Share;
import cn.paulpaulzhang.fair.sc.main.post.adapter.ShareAdapter;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
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

    private ShareAdapter mAdapter;
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
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new ShareAdapter(R.layout.item_share, new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Share item = (Share) adapter.getItem(position);
            if (item != null) {
                Intent intent = new Intent(getContext(), UserCenterActivity.class);
                intent.putExtra("uid", item.getUid());
                startActivity(intent);
            }

        });
    }

    public void loadData() {
        requestData(response -> {
            String result = JSON.parseObject(response).getString("result");
            if (TextUtils.equals(result, "ok")) {
                List<Share> shares = new ArrayList<>();
                JSONArray array = JSON.parseObject(response).getJSONArray("user");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String avatar = object.getString("avatar");
                    long time = object.getLong("time");
                    long uid = object.getLong("uid");
                    String username = object.getString("username");
                    shares.add(new Share(uid, username, avatar, time));
                }
                mAdapter.setNewData(shares);
            }
        });
    }

    private void requestData(ISuccess success) {
        RestClient.builder()
                .url(Api.GET_SHARE_USERS)
                .params("pid", pid)
                .success(success)
                .error((code, msg) -> Toasty.error(Objects.requireNonNull(getContext()), "加载失败", Toasty.LENGTH_SHORT).show())
                .build()
                .get();
    }
}
