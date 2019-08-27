package cn.paulpaulzhang.fair.sc.main.interest.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryPostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.RecommendUserCache;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.banner.BannerHolderCreator;
import cn.paulpaulzhang.fair.sc.main.interest.activity.BigEventActivity;
import cn.paulpaulzhang.fair.sc.main.interest.activity.TopicDetailActivity;
import cn.paulpaulzhang.fair.sc.main.interest.adapter.DiscoveryAdapter;
import cn.paulpaulzhang.fair.sc.main.interest.model.Discovery;
import cn.paulpaulzhang.fair.sc.main.interest.model.RecommendUser;
import cn.paulpaulzhang.fair.sc.main.post.activity.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.activity.DynamicActivity;
import io.objectbox.Box;

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
                    intent.putExtra("id", item.getDiscoveryPostCache().getId());
                    startActivity(intent);
                } else if (item.getItemType() == Discovery.ARTICLE) {
                    Intent intent = new Intent(getContext(), ArticleActivity.class);
                    intent.putExtra("id", item.getDiscoveryPostCache().getId());
                    startActivity(intent);
                }

            }
        });
    }

    private void initHeader(View view) {
        List<String> data = new ArrayList<>();
        data.add("http://pic37.nipic.com/20140113/8800276_184927469000_2.png");
        data.add("http://pic40.nipic.com/20140331/9469669_142840860000_2.jpg");
        data.add("http://pic41.nipic.com/20140508/18609517_112216473140_2.jpg");

        ConvenientBanner<String> mConvenientBanner = view.findViewById(R.id.cb_discovery);
        mConvenientBanner.setPages(new BannerHolderCreator(), data)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(5000)
                .setOnItemClickListener(position -> Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.ll_big_event).setOnClickListener(v -> startActivity(new Intent(getContext(), BigEventActivity.class)));
        view.findViewById(R.id.ll_announcement).setOnClickListener(v -> {
        });
        view.findViewById(R.id.ll_course_table).setOnClickListener(v -> {
        });
        view.findViewById(R.id.ll_help).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TopicDetailActivity.class);
            intent.putExtra("name", "校园帮");
            startActivity(intent);
        });
        view.findViewById(R.id.ll_map).setOnClickListener(v -> {
        });
    }

    /**
     * 从数据库加载数据
     *
     * @param type 加载类型（刷新数据，加载更多）
     */
    public void loadData(int type) {
        Box<DiscoveryPostCache> postBox = ObjectBox.get().boxFor(DiscoveryPostCache.class);
        int position = mAdapter.getData().size();
        if (type == Constant.REFRESH_DATA) {
            requestData(0, Constant.REFRESH_DATA);
            //load post data
            List<DiscoveryPostCache> discoveryPostCaches = postBox.getAll();
            List<Discovery> items = new ArrayList<>();
            long count = Math.min(postBox.count(), Constant.LOAD_MAX_DATABASE);
            for (int i = 0; i < count; i++) {
                DiscoveryPostCache discoveryPostCache = discoveryPostCaches.get(i);
                items.add(new Discovery(discoveryPostCache.getType(), discoveryPostCache));
            }
            //load user data
            Box<RecommendUserCache> userBox = ObjectBox.get().boxFor(RecommendUserCache.class);
            List<RecommendUser> userItems = new ArrayList<>();
            for (RecommendUserCache user : userBox.getAll()) {
                userItems.add(new RecommendUser(user));
            }
            if (items.size() != 0) {
                items.add(Constant.USER_POSITION, new Discovery(2, userItems));
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
            List<DiscoveryPostCache> discoveryPostCaches = postBox.getAll();
            List<Discovery> items = new ArrayList<>();

            long count = Math.min(postBox.count() - position, Constant.LOAD_MAX_DATABASE);
            for (int i = position; i < count; i++) {
                DiscoveryPostCache discoveryPostCache = discoveryPostCaches.get(i);
                items.add(new Discovery(discoveryPostCache.getType(), discoveryPostCache));
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
                    .success(r -> JsonParseUtil.parseDiscoveryPost(r, Constant.REFRESH_DATA))
                    .build()
                    .get();
            RestClient.builder()
                    .url("recommend")
                    .success(JsonParseUtil::parseRecommendUsers)
                    .build()
                    .get();

        } else if (type == Constant.LOAD_MORE_DATA) {
            RestClient.builder()
                    .url("post")
                    .params("position", start)
                    .params("number", Constant.LOAD_MAX_SEVER)
                    .success(r -> JsonParseUtil.parseDiscoveryPost(r, Constant.REFRESH_DATA))
                    .build()
                    .get();
        }
    }

}
