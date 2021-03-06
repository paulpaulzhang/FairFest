package cn.paulpaulzhang.fair.sc.main.interest.delegate;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryLikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryLikeCache_;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryPostCache_;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryPostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.RecommendUserCache;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.banner.BannerHolderCreator;
import cn.paulpaulzhang.fair.sc.main.common.FeaturesUtil;
import cn.paulpaulzhang.fair.sc.main.common.PostCommentUtil;
import cn.paulpaulzhang.fair.sc.main.common.PostItem;
import cn.paulpaulzhang.fair.sc.main.common.PostShareUtil;
import cn.paulpaulzhang.fair.sc.main.interest.activity.AnnouncementActivity;
import cn.paulpaulzhang.fair.sc.main.interest.activity.BreakingNewsActivity;
import cn.paulpaulzhang.fair.sc.main.interest.activity.MapActivity;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TeamActivity;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.DiscoveryAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Discovery;
import cn.paulpaulzhang.fair.sc.main.interest.model.RecommendUser;
import cn.paulpaulzhang.fair.sc.main.post.activity.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.DynamicActivity;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间：7/10/19
 * 创建人：paulpaulzhang
 * 描述：发现页
 */
public class DiscoveryDelegate extends AbstractDelegate {


    @BindView(R2.id.rv_discovery)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_discovery)
    SwipeRefreshLayout mSwipeRefresh;

    private DiscoveryAdapter mAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_discovery;
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
        mAdapter = new DiscoveryAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        View header = View.inflate(getContext(), R.layout.view_header_discovery, null);
        initHeader(header);
        mAdapter.addHeaderView(header);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == Constant.USER_POSITION) {
                return;
            }
            Discovery item = (Discovery) adapter.getItem(position);
            if (item != null) {
                if (item.getItemType() == Discovery.DYNAMIC) {
                    Intent intent = new Intent(getContext(), DynamicActivity.class);
                    intent.putExtra("pid", item.getPostCache().getId());
                    intent.putExtra("uid", item.getPostCache().getUid());
                    intent.putExtra("isLike", item.isLike());
                    startActivity(intent);
                } else if (item.getItemType() == Discovery.ARTICLE){
                    Intent intent = new Intent(getContext(), ArticleActivity.class);
                    intent.putExtra("pid", item.getPostCache().getId());
                    intent.putExtra("uid", item.getPostCache().getUid());
                    intent.putExtra("isLike", item.isLike());
                    startActivity(intent);
                }
            }

        });


        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            Discovery item = (Discovery) adapter.getItem(position);
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
                        .share((AppCompatActivity) getActivity(), getContext(), adapter.getViewByPosition(position + adapter.getHeaderLayoutCount(), R.id.card_content), 400);
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


    private void initHeader(View view) {
        List<String> data = new ArrayList<>();
        data.add("http:/www.matchstickmen.club/banner/banner1.jpg");
        data.add("http:/www.matchstickmen.club/banner/banner2.jpg");
        data.add("http:/www.matchstickmen.club/banner/banner3.jpg");
        data.add("http:/www.matchstickmen.club/banner/banner4.jpg");

        ConvenientBanner<String> mConvenientBanner = view.findViewById(R.id.cb_discovery);
        mConvenientBanner.setPages(new BannerHolderCreator(), data)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(5000)
                .setOnItemClickListener(position -> Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.ll_big_event).setOnClickListener(v -> startActivity(new Intent(getContext(), BreakingNewsActivity.class)));
        view.findViewById(R.id.ll_announcement).setOnClickListener(v -> startActivity(new Intent(getContext(), AnnouncementActivity.class)));
        view.findViewById(R.id.ll_team).setOnClickListener(v -> startActivity(new Intent(getContext(), TeamActivity.class)));
        view.findViewById(R.id.ll_help).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TopicDetailActivity.class);
            intent.putExtra("name", "校园帮");
            startActivity(intent);
        });
        view.findViewById(R.id.ll_map).setOnClickListener(v -> {
            if (EasyPermissions.hasPermissions(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                startActivity(new Intent(getContext(), MapActivity.class));
            } else {
                EasyPermissions.requestPermissions(this, "该功能需要定位权限", 1002,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
            }

        });
    }

    private int page = 1;

    private void loadData(int type) {
        Box<DiscoveryPostCache> postBox = ObjectBox.get().boxFor(DiscoveryPostCache.class);
        Box<DiscoveryLikeCache> likeBox = ObjectBox.get().boxFor(DiscoveryLikeCache.class);
        if (type == Constant.REFRESH_DATA) {
            requestData(1, Constant.REFRESH_DATA, response -> {
                page = 1;
                JsonParseUtil.parseDiscoveryPost(response, Constant.REFRESH_DATA);
                List<DiscoveryPostCache> postCaches = postBox.query().orderDesc(DiscoveryPostCache_.time).build().find();
                List<Discovery> items = new ArrayList<>();
                for (DiscoveryPostCache post : postCaches) {
                    boolean isLike = Objects.requireNonNull(likeBox.query().equal(DiscoveryLikeCache_.pid, post.getId()).build().findUnique()).isLike();
                    items.add(new Discovery(post.getType(), post, isLike));
                }

                RestClient.builder()
                        .url(Api.RECOMMEND_USERS)
                        .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                        .success(response1 -> {
                            JsonParseUtil.parseRecommendUsers(response1);
                            Box<RecommendUserCache> userBox = ObjectBox.get().boxFor(RecommendUserCache.class);
                            List<RecommendUser> userItems = new ArrayList<>();
                            for (RecommendUserCache user : userBox.getAll()) {
                                userItems.add(new RecommendUser(user));
                            }
                            if (items.size() != 0 && userItems.size() != 0) {
                                items.add(Constant.USER_POSITION, new Discovery(2, userItems));
                            }
                            mAdapter.setNewData(items);
                            mSwipeRefresh.setRefreshing(false);
                        })
                        .build()
                        .get();
            });

        } else if (type == Constant.LOAD_MORE_DATA) {
            long size = postBox.count();
            if (size >= Constant.LOAD_MAX_SEVER) {
                requestData(++page, Constant.LOAD_MORE_DATA, response -> {
                    JsonParseUtil.parseDiscoveryPost(response, Constant.LOAD_MORE_DATA);
                    List<Discovery> items = new ArrayList<>();
                    if (size == postBox.count()) {
                        mAdapter.loadMoreEnd(true);
                        return;
                    }
                    List<DiscoveryPostCache> postCaches = postBox.query().orderDesc(DiscoveryPostCache_.time).build().find(size, Constant.LOAD_MAX_DATABASE);
                    for (DiscoveryPostCache post : postCaches) {
                        boolean isLike = Objects.requireNonNull(likeBox.query().equal(DiscoveryLikeCache_.pid, post.getId()).build().findUnique()).isLike();
                        items.add(new Discovery(post.getType(), post, isLike));
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
                    .url(Api.GET_POST_BY_ALGORITHMS)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
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
                    .url(Api.GET_POST_BY_ALGORITHMS)
                    .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("pageNo", page)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .success(success)
                    .error((code, msg) -> {
                        Toasty.error(Objects.requireNonNull(getContext()), "加载失败" + code + "  " + msg, Toasty.LENGTH_SHORT).show();
                        mSwipeRefresh.setRefreshing(false);
                    })
                    .build()
                    .get();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            PostCommentUtil.INSTANCE().compressPhoto(Matisse.obtainResult(data).get(0), getContext(), (AppCompatActivity) Objects.requireNonNull(getActivity()));
        }
    }

}
