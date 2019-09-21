package cn.paulpaulzhang.fair.sc.main.user.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zhihu.matisse.Matisse;

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
import cn.paulpaulzhang.fair.sc.main.common.PostAdapter;
import cn.paulpaulzhang.fair.sc.main.common.PostCommentUtil;
import cn.paulpaulzhang.fair.sc.main.common.PostItem;
import cn.paulpaulzhang.fair.sc.main.post.activity.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.DynamicActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

import static android.app.Activity.RESULT_OK;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user.delegate
 * 创建时间: 8/13/2019
 * 创建人: zlm31
 * 描述:
 */
public class DynamicDelegate extends FairDelegate {

    @BindView(R2.id.rv_dynamic)
    RecyclerView mRecyclerView;

    private PostAdapter mAdapter;
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
        Box<PostCache> postCacheBox = ObjectBox.get().boxFor(PostCache.class);
        Box<UserCache> userCacheBox = ObjectBox.get().boxFor(UserCache.class);
        Box<LikeCache> likeCacheBox = ObjectBox.get().boxFor(LikeCache.class);
        postCacheBox.removeAll();
        userCacheBox.removeAll();
        likeCacheBox.removeAll();
        initRecyclerView();
        loadData(Constant.REFRESH_DATA);
    }

    private void initRecyclerView() {
        mAdapter = new PostAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            PostItem item = (PostItem) adapter.getItem(position);
            if (item != null) {
                if (item.getItemType() == PostItem.DYNAMIC) {
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
            PostItem item = (PostItem) adapter.getItem(position);
            if (item == null) {
                return;
            }
            if (view.getId() == R.id.ll_comment_dynamic || view.getId() == R.id.ll_comment_article) {
                if (item.getPostCache().getCommentCount() == 0) {
                    PostCommentUtil.INSTANCE().bottomDialog(item.getPostCache().getId(), (AppCompatActivity) getActivity(), getContext(), this);
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

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            PostCommentUtil.INSTANCE().compressPhoto(Matisse.obtainResult(data).get(0), getContext(), (AppCompatActivity) Objects.requireNonNull(getActivity()));
        }
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
                    .error((code, msg) -> Toasty.error(Objects.requireNonNull(getContext()), "加载失败" + code, Toasty.LENGTH_SHORT).show())
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
                    .error((code, msg) -> Toasty.error(Objects.requireNonNull(getContext()), "加载失败" + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .get();
        }
    }


}
