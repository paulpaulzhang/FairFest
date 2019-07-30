package cn.paulpaulzhang.fair.sc.main.interest.topic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.constant.Constant;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.entity.FollowPostCache;
import cn.paulpaulzhang.fair.sc.database.entity.TopicPostCache;
import cn.paulpaulzhang.fair.sc.json.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.interest.follow.FollowAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.follow.FollowItem;
import cn.paulpaulzhang.fair.util.image.ImageUtil;
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

    private TopicDetailAdapter mAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_topic_detail;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        initToolbar(mToolbar);
        initCollapsing();
        initSwipeRefresh();
        initRecyclerView();
        mSwipeRefresh.setRefreshing(true);
        loadData(Constant.REFRESH_DATA);
    }

    private void initCollapsing() {
        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setTitle("考研数学");
        String url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-oi9W7QkQ4yu3xHUYOngWam_JckiY4ic0SeESz8oGjbXvLDEH";
        ImageUtil.setBlurImage(this, mBg, url, 25);
        mAvatar.setImageURI(Uri.parse(url));
        mDiscuss.setText("1234 讨论");
        mFollow.setText("234 关注");
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        //TODO 刷新数据时需要刷新话题概览的内容
        mSwipeRefresh.setOnRefreshListener(() -> loadData(Constant.REFRESH_DATA));
    }

    private void initRecyclerView() {
        mAdapter = new TopicDetailAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            TopicDetailItem item = (TopicDetailItem) adapter.getItem(position);
            if (item != null) {
                Toasty.info(this, item.getTopicPostCache().getId() + "", Toasty.LENGTH_SHORT, true).show();
            }
        });
    }

    private void loadData(int type) {
        Box<TopicPostCache> postBox = ObjectBox.get().boxFor(TopicPostCache.class);
        int position = mAdapter.getData().size();
        if (type == Constant.REFRESH_DATA) {
            requestData(0, Constant.REFRESH_DATA);
            List<TopicPostCache> topicPostCaches = postBox.getAll();
            List<TopicDetailItem> items = new ArrayList<>();
            long count = Math.min(postBox.count(), Constant.LOAD_MAX_DATABASE);
            for (int i = 0; i < count; i++) {
                TopicPostCache topicPostCache = topicPostCaches.get(i);
                items.add(new TopicDetailItem(topicPostCache.getType(), topicPostCache));
            }
            mAdapter.setNewData(items);
            mSwipeRefresh.setRefreshing(false);

        } else if (type == Constant.LOAD_MORE_DATA) {
            long size = postBox.count();
            if (position + Constant.LOAD_MAX_DATABASE > size) {
                requestData(size, Constant.LOAD_MORE_DATA);
                if (size == postBox.count()) {
                    mAdapter.loadMoreEnd(true);
                    return;
                }
            }
            List<TopicPostCache> topicPostCaches = postBox.getAll();
            List<TopicDetailItem> items = new ArrayList<>();

            long count = Math.min(postBox.count() - position, Constant.LOAD_MAX_DATABASE);
            for (int i = position; i < count; i++) {
                TopicPostCache topicPostCache = topicPostCaches.get(i);
                items.add(new TopicDetailItem(topicPostCache.getType(), topicPostCache));
            }
            mAdapter.addData(items);
            mAdapter.loadMoreComplete();
        }
    }

    /**
     * 从服务器下载数据到数据库
     *
     * @param start 开始位置
     * @param type  加载类型（刷新数据，加载更多）
     */
    private void requestData(long start, int type) {
        if (type == Constant.REFRESH_DATA) {
            RestClient.builder()
                    .url("post")
                    .params("position", 0)
                    .params("number", Constant.LOAD_MAX_SEVER)
                    .success(r -> JsonParseUtil.parseTopicPost(r, type))
                    .build()
                    .get();
        } else if (type == Constant.LOAD_MORE_DATA) {
            RestClient.builder()
                    .url("post")
                    .params("position", start)
                    .params("number", Constant.LOAD_MAX_SEVER)
                    .success(r -> JsonParseUtil.parseTopicPost(r, type))
                    .build()
                    .get();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
