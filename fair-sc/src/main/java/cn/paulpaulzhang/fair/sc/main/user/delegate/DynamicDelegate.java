package cn.paulpaulzhang.fair.sc.main.user.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.LikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.LikeCache_;
import cn.paulpaulzhang.fair.sc.database.Entity.PostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.PostCache_;
import cn.paulpaulzhang.fair.sc.database.Entity.UserCache;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.main.interest.model.TopicDetail;
import cn.paulpaulzhang.fair.sc.main.post.activity.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.DynamicActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.sc.main.user.adapter.DynamicAdapter;
import cn.paulpaulzhang.fair.sc.main.user.model.Dynamic;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user.delegate
 * 创建时间: 8/13/2019
 * 创建人: zlm31
 * 描述:
 */
public class DynamicDelegate extends FairDelegate {

    @BindView(R2.id.rv_dynamic)
    RecyclerView mRecyclerView;

    private DynamicAdapter mAdapter;
    private long uid = -1;

    @Override
    public Object setLayout() {
        return R.layout.delegate_dynamic;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        UserCenterActivity activity = (UserCenterActivity) getActivity();
        if (activity != null) {
            uid = activity.getUid();
        }

        initRecyclerView();
        Box<PostCache> postCacheBox = ObjectBox.get().boxFor(PostCache.class);
        Box<UserCache> userCacheBox = ObjectBox.get().boxFor(UserCache.class);
        Box<LikeCache> likeCacheBox = ObjectBox.get().boxFor(LikeCache.class);
        postCacheBox.removeAll();
        userCacheBox.removeAll();
        likeCacheBox.removeAll();
        loadData(Constant.REFRESH_DATA);
    }

    private void initRecyclerView() {
        mAdapter = new DynamicAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Dynamic item = (Dynamic) adapter.getItem(position);
            if (item != null) {
                if (item.getItemType() == Dynamic.DYNAMIC) {
                    Intent intent = new Intent(getContext(), DynamicActivity.class);
                    intent.putExtra("pid", item.getPostCache().getId());
                    intent.putExtra("uid", item.getPostCache().getUid());
                    intent.putExtra("isLike", item.isLike());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), ArticleActivity.class);
                    intent.putExtra("pid", item.getPostCache().getId());
                    intent.putExtra("uid", item.getPostCache().getUid());
                    intent.putExtra("isLike", item.isLike());
                    startActivity(intent);
                }

            }
        });
    }

    private int page = 0;

    public void loadData(int type) {
        Box<PostCache> postBox = ObjectBox.get().boxFor(PostCache.class);
        Box<LikeCache> likeBox = ObjectBox.get().boxFor(LikeCache.class);
        int position = mAdapter.getData().size();
        if (type == Constant.REFRESH_DATA) {
            requestData(0, Constant.REFRESH_DATA, response -> {

                page = 0;
                JsonParseUtil.parsePost(response, Constant.REFRESH_DATA);
                List<PostCache> postCaches = postBox.query().orderDesc(PostCache_.time).build().find();
                List<Dynamic> items = new ArrayList<>();
                long count = Math.min(postBox.count(), Constant.LOAD_MAX_DATABASE);
                for (int i = 0; i < count; i++) {
                    PostCache postCache = postCaches.get(i);
                    boolean isLike = Objects.requireNonNull(likeBox.query().equal(LikeCache_.pid, postCache.getId()).build().findUnique()).isLike();
                    items.add(new Dynamic(postCache.getType(), postCache, isLike));
                }
                mAdapter.setNewData(items);
            });

        } else if (type == Constant.LOAD_MORE_DATA) {
            long size = postBox.count();
            if (position + Constant.LOAD_MAX_DATABASE > size) {
                requestData(page, Constant.LOAD_MORE_DATA, response -> {
                    page += 1;
                    JsonParseUtil.parsePost(response, Constant.LOAD_MORE_DATA);
                    List<PostCache> postCaches = postBox.getAll();
                    List<Dynamic> items = new ArrayList<>();
                    if (size == postBox.count()) {
                        mAdapter.loadMoreEnd(true);
                        return;
                    }
                    long count = Math.min(postBox.count() - position, Constant.LOAD_MAX_DATABASE);
                    for (int i = position; i < count; i++) {
                        PostCache postCache = postCaches.get(i);
                        boolean isLike = Objects.requireNonNull(likeBox.query().equal(LikeCache_.pid, postCache.getId()).build().findUnique()).isLike();
                        items.add(new Dynamic(postCache.getType(), postCache, isLike));
                    }
                    mAdapter.addData(items);
                    mAdapter.loadMoreComplete();
                });
            }

        }
    }

    /**
     * 从服务器下载数据到数据库
     *
     * @param page 页数
     * @param type 加载类型（刷新数据，加载更多）
     */
    private void requestData(int page, int type, ISuccess success) {
        if (type == Constant.REFRESH_DATA) {
            RestClient.builder()
                    .url(Api.GET_POST_BY_UID)
                    .params("pageNo", 0)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .params("uid", uid)
                    .success(success)
                    .error((code, msg) -> Toasty.error(Objects.requireNonNull(getContext()), "加载失败" + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .get();
        } else if (type == Constant.LOAD_MORE_DATA) {
            RestClient.builder()
                    .url(Api.GET_POST_BY_UID)
                    .params("pageNo", page)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .params("uid", uid)
                    .success(success)
                    .error((code, msg) -> Toasty.error(Objects.requireNonNull(getContext()), "加载失败" + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .get();
        }
    }


}
