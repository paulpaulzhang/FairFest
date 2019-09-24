package cn.paulpaulzhang.fair.sc.main.interest.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.gyf.immersionbar.ImmersionBar;
import com.zhihu.matisse.Matisse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.database.Entity.LikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.LikeCache_;
import cn.paulpaulzhang.fair.sc.database.Entity.PostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.PostCache_;
import cn.paulpaulzhang.fair.sc.database.Entity.UserCache;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.common.FeaturesUtil;
import cn.paulpaulzhang.fair.sc.main.common.PostAdapter;
import cn.paulpaulzhang.fair.sc.main.common.PostCommentUtil;
import cn.paulpaulzhang.fair.sc.main.common.PostItem;
import cn.paulpaulzhang.fair.sc.main.common.PostShareUtil;
import cn.paulpaulzhang.fair.sc.main.post.activity.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.CreateArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.CreateDynamicActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.DynamicActivity;
import cn.paulpaulzhang.fair.util.image.ImageUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.ic_topic
 * 创建时间: 7/23/2019
 * 创建人: zlm31
 * 描述:
 */
public class TopicDetailActivity extends FairActivity {
    @BindView(R2.id.ctl_topic)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R2.id.dv_topic_bg)
    SimpleDraweeView mBg;

    @BindView(R2.id.dv_topic_avatar)
    SimpleDraweeView mAvatar;

    @BindView(R2.id.tv_topic_discuss)
    AppCompatTextView mDiscuss;

    @BindView(R2.id.tv_topic_follow)
    AppCompatTextView mFollow;

    @BindView(R2.id.btn_follow)
    MaterialButton mButtonFollow;

    @BindView(R2.id.tb_topic)
    MaterialToolbar mToolbar;

    @BindView(R2.id.rv_topic)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_topic)
    SwipeRefreshLayout mSwipeRefresh;

    private PostAdapter mAdapter;
    private String name;
    private long tid;

    @Override
    public int setLayout() {
        return R.layout.activity_topic_detail;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        initToolbar(mToolbar);
        initCollapsing();
        initSwipeRefresh();
        initRecyclerView();
        Box<PostCache> postCacheBox = ObjectBox.get().boxFor(PostCache.class);
        Box<UserCache> userCacheBox = ObjectBox.get().boxFor(UserCache.class);
        Box<LikeCache> likeCacheBox = ObjectBox.get().boxFor(LikeCache.class);
        postCacheBox.removeAll();
        userCacheBox.removeAll();
        likeCacheBox.removeAll();
        mSwipeRefresh.setRefreshing(true);
        loadHeaderData();

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();
    }

    @SuppressLint("SetTextI18n")
    private void initCollapsing() {
        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setTitle(name);
        mAvatar.setImageResource(R.drawable.ic_topic_128);
        mDiscuss.setText(0 + " 讨论");
        mFollow.setText(0 + " 关注");
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
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setPreLoadNumber(3);
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

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            PostItem item = (PostItem) adapter.getItem(position);
            if (item == null) {
                return;
            }
            if (view.getId() == R.id.ll_comment_dynamic || view.getId() == R.id.ll_comment_article) {
                if (item.getPostCache().getCommentCount() == 0) {
                    PostCommentUtil.INSTANCE().comment(item.getPostCache().getId(), this, this, null);
                    FeaturesUtil.update(item.getPostCache().getId());
                } else {
                    if (item.getItemType() == PostItem.DYNAMIC) {
                        Intent intent = new Intent(this, DynamicActivity.class);
                        intent.putExtra("pid", item.getPostCache().getId());
                        intent.putExtra("uid", item.getPostCache().getUid());
                        intent.putExtra("fold", true);
                        intent.putExtra("isLike", item.isLike());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, ArticleActivity.class);
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
                        .share(this, this, adapter.getViewByPosition(position, R.id.card_content), 400);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            PostCommentUtil.INSTANCE().compressPhoto(Matisse.obtainResult(data).get(0), this, this);
        }
    }

    @OnClick(R2.id.fab_article_topic)
    void createArticle() {
        Intent intent = new Intent(this, CreateArticleActivity.class);
        intent.putExtra("topic", name);
        intent.putExtra("tid", tid);
        startActivity(intent);
    }

    @OnClick(R2.id.fab_dynamic_topic)
    void createDynamic() {
        Intent intent = new Intent(this, CreateDynamicActivity.class);
        intent.putExtra("topic", name);
        intent.putExtra("tid", tid);
        startActivity(intent);
    }

    @OnClick(R2.id.btn_follow)
    void follow() {
        boolean followed = !mButtonFollow.getText().toString().equals("关注");
        if (!followed) {
            RestClient.builder()
                    .url(Api.PAY_TOPIC)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("tid", tid)
                    .success(r -> {
                        String result = JSON.parseObject(r).getString("result");
                        if (TextUtils.equals(result, "ok")) {
                            mButtonFollow.setText("已关注");
                            String[] subString = mFollow.getText().toString().split(" ");
                            mFollow.setText(String.format(Locale.CHINA, "%d %s", Integer.parseInt(subString[0]) + 1, subString[1]));
                        } else {
                            Toasty.error(TopicDetailActivity.this, "关注失败", Toasty.LENGTH_SHORT).show();
                        }
                    })
                    .error(((code, msg) -> Toasty.error(TopicDetailActivity.this, "关注失败 " + code, Toasty.LENGTH_SHORT).show()))
                    .build()
                    .post();
        } else {
            RestClient.builder()
                    .url(Api.CANCEL_PAY_TOPIC)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("tid", tid)
                    .success(r -> {
                        String result = JSON.parseObject(r).getString("result");
                        if (TextUtils.equals(result, "ok")) {
                            mButtonFollow.setText("关注");
                            String[] subString = mFollow.getText().toString().split(" ");
                            mFollow.setText(String.format(Locale.CHINA, "%d %s", Integer.parseInt(subString[0]) - 1, subString[1]));
                        } else {
                            Toasty.error(TopicDetailActivity.this, "操作失败 ", Toasty.LENGTH_SHORT).show();
                        }

                    })
                    .error(((code, msg) -> Toasty.error(TopicDetailActivity.this, "操作失败 " + code, Toasty.LENGTH_SHORT).show()))
                    .build()
                    .post();
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadHeaderData() {
        RestClient.builder()
                .url(Api.SINGLE_TOPIC)
                .params("tname", name)
                .success(r -> {
                    JSONObject jsonObject = JSON.parseObject(r);
                    String imgUrl = jsonObject.getString("imageUrl");
                    int payCount = jsonObject.getIntValue("payCount");
                    int postCount = jsonObject.getIntValue("postCount");
                    tid = jsonObject.getLong("tid");
                    if (imgUrl == null || TextUtils.equals(imgUrl, "")) {
                        ImageUtil.setBlurImage(this, mBg, Constant.DEFAULT_AVATAR, 25);
                        mAvatar.setImageResource(R.drawable.ic_topic_128);
                    } else {
                        ImageUtil.setBlurImage(this, mBg, imgUrl, 25);
                        mAvatar.setImageURI(Uri.parse(imgUrl));
                    }
                    mDiscuss.setText(postCount + " 讨论");
                    mFollow.setText(payCount + " 关注");
                    loadData(Constant.REFRESH_DATA);

                    RestClient.builder()
                            .url(Api.IS_PAY_TOPIC)
                            .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                            .params("tid", tid)
                            .success(r1 -> {
                                String result = JSON.parseObject(r1).getString("result");
                                if (TextUtils.equals(result, "已关注")) {
                                    mButtonFollow.setText("已关注");
                                } else {
                                    mButtonFollow.setText("关注");
                                }
                            })
                            .error(((code, msg) -> FairLogger.d(code)))
                            .build()
                            .get();
                })
                .error((code, msg) -> FairLogger.d("Header", code))
                .build()
                .get();
    }

    private int page = 1;

    private void loadData(int type) {
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
                mSwipeRefresh.setRefreshing(false);
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
                    .url(Api.GET_POST_BY_TID)
                    .params("pageNo", page)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .params("tid", tid)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .success(success)
                    .error(((code, msg) -> {
                        Toasty.error(this, "加载失败" + code, Toasty.LENGTH_SHORT).show();
                        mSwipeRefresh.setRefreshing(false);
                    }))
                    .build()
                    .get();
        } else if (type == Constant.LOAD_MORE_DATA) {
            RestClient.builder()
                    .url(Api.GET_POST_BY_TID)
                    .params("pageNo", page)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .params("tid", tid)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .success(success)
                    .error(((code, msg) -> {
                        Toasty.error(this, "加载失败" + code, Toasty.LENGTH_SHORT).show();
                        mSwipeRefresh.setRefreshing(false);
                    }))
                    .build()
                    .get();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
