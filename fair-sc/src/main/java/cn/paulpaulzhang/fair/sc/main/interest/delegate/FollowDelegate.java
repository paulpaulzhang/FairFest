package cn.paulpaulzhang.fair.sc.main.interest.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowLikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowLikeCache_;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowPostCache_;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowPostCache;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.common.FeaturesUtil;
import cn.paulpaulzhang.fair.sc.main.common.PostCommentUtil;
import cn.paulpaulzhang.fair.sc.main.common.PostItem;
import cn.paulpaulzhang.fair.sc.main.common.PostShareUtil;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.FollowAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Follow;
import cn.paulpaulzhang.fair.sc.main.post.activity.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.DynamicActivity;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

import static android.app.Activity.RESULT_OK;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间：7/10/19
 * 创建人： paulpaulzhang
 * 描述：关注页
 */
public class FollowDelegate extends AbstractDelegate {
    @BindView(R2.id.rv_follow)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_follow)
    SwipeRefreshLayout mSwipeRefresh;

    private FollowAdapter mAdapter;

    private long uid = FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name());

    @Override
    public Object setLayout() {
        return R.layout.delegate_follow;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        initSwipeRefresh();
        initRecyclerView();
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

    private void initRecyclerView() {
        mAdapter = new FollowAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Follow item = (Follow) adapter.getItem(position);
            if (item != null) {
                if (item.getItemType() == Follow.DYNAMIC) {
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

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Follow item = (Follow) adapter.getItem(position);
            if (item == null) {
                return;
            }
            if (view.getId() == R.id.ll_comment_dynamic || view.getId() == R.id.ll_comment_article) {
                if (item.getPostCache().getCommentCount() == 0) {
                    PostCommentUtil.INSTANCE().comment(item.getPostCache().getId(), (AppCompatActivity) getActivity(), getContext(), this);
                    FeaturesUtil.update(item.getPostCache().getId());
                } else {
                    if (item.getItemType() == PostItem.DYNAMIC) {
                        Intent intent = new Intent(getContext(), DynamicActivity.class);
                        intent.putExtra("pid", item.getPostCache().getId());
                        intent.putExtra("uid", item.getPostCache().getUid());
                        intent.putExtra("fold", true);
                        intent.putExtra("isLike", item.isLike());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), ArticleActivity.class);
                        intent.putExtra("pid", item.getPostCache().getId());
                        intent.putExtra("uid", item.getPostCache().getUid());
                        intent.putExtra("fold", true);
                        intent.putExtra("isLike", item.isLike());
                        startActivity(intent);
                    }
                }
            } else if (view.getId() == R.id.ll_share_dynamic || view.getId() == R.id.ll_share_article) {
                PostShareUtil
                        .INSTANCE()
                        .share((AppCompatActivity) getActivity(), getContext(), adapter.getViewByPosition(position, R.id.card_content), 400);
                FeaturesUtil.update(item.getPostCache().getId());
                RestClient.builder()
                        .url(Api.SHARE_POST)
                        .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                        .params("pid", item.getPostCache().getId())
                        .build()
                        .post();
            }
        });
    }

    private int page = 1;

    private void loadData(int type) {
        Box<FollowPostCache> postBox = ObjectBox.get().boxFor(FollowPostCache.class);
        Box<FollowLikeCache> likeBox = ObjectBox.get().boxFor(FollowLikeCache.class);
        if (type == Constant.REFRESH_DATA) {
            requestData(1, Constant.REFRESH_DATA, response -> {
                page = 1;
                JsonParseUtil.parseFollowPost(response, Constant.REFRESH_DATA);
                List<FollowPostCache> postCaches = postBox.query().orderDesc(FollowPostCache_.time).build().find();
                List<Follow> items = new ArrayList<>();
                for (FollowPostCache post : postCaches) {
                    boolean isLike = Objects.requireNonNull(likeBox.query().equal(FollowLikeCache_.pid, post.getId()).build().findUnique()).isLike();
                    items.add(new Follow(post.getType(), post, isLike));
                }
                mAdapter.setNewData(items);
                mSwipeRefresh.setRefreshing(false);
            });

        } else if (type == Constant.LOAD_MORE_DATA) {
            long size = postBox.count();
            if (size >= Constant.LOAD_MAX_SEVER) {
                requestData(++page, Constant.LOAD_MORE_DATA, response -> {
                    JsonParseUtil.parseFollowPost(response, Constant.LOAD_MORE_DATA);
                    List<Follow> items = new ArrayList<>();
                    if (size == postBox.count()) {
                        mAdapter.loadMoreEnd(true);
                        return;
                    }
                    List<FollowPostCache> postCaches = postBox.query().orderDesc(FollowPostCache_.time).build().find(size, Constant.LOAD_MAX_DATABASE);
                    for (FollowPostCache post : postCaches) {
                        boolean isLike = Objects.requireNonNull(likeBox.query().equal(FollowLikeCache_.pid, post.getId()).build().findUnique()).isLike();
                        items.add(new Follow(post.getType(), post, isLike));
                    }
                    mAdapter.addData(items);
                    mAdapter.loadMoreComplete();
                });
            } else {
                mAdapter.loadMoreEnd(true);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            PostCommentUtil.INSTANCE().compressPhoto(Matisse.obtainResult(data).get(0), getContext(), (AppCompatActivity) Objects.requireNonNull(getActivity()));
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
                    .url(Api.GET_POST_BY_UID_PAY_ALL_UID)
                    .params("uid", uid)
                    .params("pageNo", page)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .success(success)
                    .error((code, msg) -> {
                        Toasty.error(Objects.requireNonNull(getContext()), "加载失败" + code, Toasty.LENGTH_SHORT).show();
                        mSwipeRefresh.setRefreshing(false);
                    })
                    .build()
                    .get();
        } else if (type == Constant.LOAD_MORE_DATA) {
            RestClient.builder()
                    .url(Api.GET_POST_BY_UID_PAY_ALL_UID)
                    .params("uid", uid)
                    .params("pageNo", page)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .success(success)
                    .error((code, msg) -> {
                        Toasty.error(Objects.requireNonNull(getContext()), "加载失败" + code, Toasty.LENGTH_SHORT).show();
                        mSwipeRefresh.setRefreshing(false);
                    })
                    .build()
                    .get();
        }
    }
}
