package cn.paulpaulzhang.fair.sc.main.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
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
import cn.paulpaulzhang.fair.sc.main.common.PostAdapter;
import cn.paulpaulzhang.fair.sc.main.common.PostItem;
import cn.paulpaulzhang.fair.sc.main.post.activity.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.DynamicActivity;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.activity
 * 创建时间：9/18/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class UserDynamicActivity extends FairActivity {

    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    @BindView(R2.id.srl_dynamic)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.rv_dynamic)
    RecyclerView mRecyclerView;

    private long uid;
    private PostAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_user_dynamic;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initToolbar(mToolbar, "我的动态");
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();

        uid = FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name());

        Box<PostCache> postCacheBox = ObjectBox.get().boxFor(PostCache.class);
        Box<UserCache> userCacheBox = ObjectBox.get().boxFor(UserCache.class);
        Box<LikeCache> likeCacheBox = ObjectBox.get().boxFor(LikeCache.class);
        postCacheBox.removeAll();
        userCacheBox.removeAll();
        likeCacheBox.removeAll();
        initRecyclerView();
        initSwipeRefresh();
        mSwipeRefresh.setRefreshing(true);
        loadData(Constant.REFRESH_DATA);
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> loadData(Constant.REFRESH_DATA));
    }

    private void initRecyclerView() {
        mAdapter = new PostAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            PostItem item = (PostItem) adapter.getItem(position);
            if (item != null) {
                if (item.getItemType() == PostItem.DYNAMIC) {
                    Intent intent = new Intent(this, DynamicActivity.class);
                    intent.putExtra("pid", item.getPostCache().getId());
                    intent.putExtra("uid", item.getPostCache().getUid());
                    intent.putExtra("isLike", item.isLike());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, ArticleActivity.class);
                    intent.putExtra("pid", item.getPostCache().getId());
                    intent.putExtra("uid", item.getPostCache().getUid());
                    intent.putExtra("isLike", item.isLike());
                    startActivity(intent);
                }

            }
        });

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            PostItem item = (PostItem) adapter.getItem(position);
            if (item != null) {
                long pid = item.getPostCache().getId();
                AlertDialog dialog = new MaterialAlertDialogBuilder(Objects.requireNonNull(this))
                        .setTitle("删除确认")
                        .setMessage("点击确认将删除此动态，该操作不可撤销")
                        .setPositiveButton("确认", (dialogInterface, i) -> RestClient.builder()
                                .url(Api.DELETE_POST)
                                .params("pid", pid)
                                .success(response -> {
                                    String result = JSON.parseObject(response).getString("result");
                                    if (TextUtils.equals(result, "ok")) {
                                        Box<PostCache> box = ObjectBox.get().boxFor(PostCache.class);
                                        box.remove(pid);
                                        adapter.remove(position);
                                    }
                                })
                                .error((code, msg) -> Toasty.error(this, "删除失败", Toasty.LENGTH_SHORT).show())
                                .build()
                                .post())
                        .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Objects.requireNonNull(this).getColor(android.R.color.holo_red_light));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(this.getColor(R.color.font_default));
            }
            return true;
        });
    }

    private int page = 1;

    public void loadData(int type) {
        Box<PostCache> postBox = ObjectBox.get().boxFor(PostCache.class);
        Box<LikeCache> likeBox = ObjectBox.get().boxFor(LikeCache.class);
        if (type == Constant.REFRESH_DATA) {
            requestData(1, Constant.REFRESH_DATA, response -> {
                page = 1;
                JsonParseUtil.parsePost(response, Constant.REFRESH_DATA);
                List<PostCache> postCaches = postBox.query().orderDesc(PostCache_.time).build().find();
                List<PostItem> items = new ArrayList<>();
                for (PostCache post : postCaches) {
                    boolean isLike = Objects.requireNonNull(likeBox.query().equal(LikeCache_.pid, post.getId()).build().findUnique()).isLike();
                    items.add(new PostItem(post.getType(), post, isLike));
                }
                mSwipeRefresh.setRefreshing(false);
                mAdapter.setNewData(items);
            });

        } else if (type == Constant.LOAD_MORE_DATA) {
            long size = postBox.count();
            if (size >= Constant.LOAD_MAX_SEVER) {
                requestData(++page, Constant.LOAD_MORE_DATA, response -> {
                    JsonParseUtil.parsePost(response, Constant.LOAD_MORE_DATA);
                    List<PostItem> items = new ArrayList<>();
                    if (size == postBox.count()) {
                        mAdapter.loadMoreEnd(true);
                        return;
                    }
                    List<PostCache> postCaches = postBox.query().orderDesc(PostCache_.time).build().find(size, Constant.LOAD_MAX_DATABASE);
                    for (PostCache post : postCaches) {
                        boolean isLike = Objects.requireNonNull(likeBox.query().equal(LikeCache_.pid, post.getId()).build().findUnique()).isLike();
                        items.add(new PostItem(post.getType(), post, isLike));
                    }
                    mAdapter.addData(items);
                    mAdapter.loadMoreComplete();
                });
            } else {
                mAdapter.loadMoreEnd(true);
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
                    .params("pageNo", page)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .params("uid", uid)
                    .params("localUid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .success(success)
                    .error((code, msg) -> {
                        Toasty.error(Objects.requireNonNull(this), "加载失败" + code, Toasty.LENGTH_SHORT).show();
                        mSwipeRefresh.setRefreshing(false);
                    })
                    .build()
                    .get();
        } else if (type == Constant.LOAD_MORE_DATA) {
            RestClient.builder()
                    .url(Api.GET_POST_BY_UID)
                    .params("pageNo", page)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .params("uid", uid)
                    .params("localUid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .success(success)
                    .error((code, msg) -> Toasty.error(Objects.requireNonNull(this), "加载失败" + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .get();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
