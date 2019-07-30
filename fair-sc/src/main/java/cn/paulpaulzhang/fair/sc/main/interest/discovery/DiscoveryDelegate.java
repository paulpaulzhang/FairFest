package cn.paulpaulzhang.fair.sc.main.interest.discovery;

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
import java.util.Objects;

import butterknife.BindView;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.constant.Constant;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.entity.DiscoveryPostCache;
import cn.paulpaulzhang.fair.sc.database.entity.RecommendUserCache;
import cn.paulpaulzhang.fair.sc.json.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.banner.BannerHolderCreator;
import cn.paulpaulzhang.fair.sc.main.post.ArticleActivity;
import cn.paulpaulzhang.fair.sc.main.post.DynamicActivity;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间：7/10/19
 * 创建人：paulpaulzhang
 * 描述：发现页
 */
public class DiscoveryDelegate extends FairDelegate {


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
        mSwipeRefresh.setRefreshing(true);
        loadData(Constant.REFRESH_DATA);
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> loadData(Constant.REFRESH_DATA));
    }

    private void initRecycler() {
        mAdapter = new DiscoveryAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        View header = View.inflate(getContext(), R.layout.view_header_discovery, null);
        initBanner(header);
        mAdapter.addHeaderView(header);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setPreLoadNumber(3);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == Constant.USER_POSITION) {
                return;
            }
            DiscoveryItem item = (DiscoveryItem) adapter.getItem(position);
            if (item != null) {
                if (item.getItemType() == DiscoveryItem.DYNAMIC) {
                    Intent intent = new Intent(getContext(), DynamicActivity.class);
                    intent.putExtra("id", item.getDiscoveryPostCache().getId());
                    startActivity(intent);
                } else if (item.getItemType() == DiscoveryItem.ARTICLE) {
                    Intent intent = new Intent(getContext(), ArticleActivity.class);
                    intent.putExtra("id", item.getDiscoveryPostCache().getId());
                    startActivity(intent);
                }

            }
        });
    }

    private void initBanner(View view) {
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
            List<DiscoveryItem> items = new ArrayList<>();
            long count = Math.min(postBox.count(), Constant.LOAD_MAX_DATABASE);
            for (int i = 0; i < count; i++) {
                DiscoveryPostCache discoveryPostCache = discoveryPostCaches.get(i);
                items.add(new DiscoveryItem(discoveryPostCache.getType(), discoveryPostCache));
            }
            //load user data
            Box<RecommendUserCache> userBox = ObjectBox.get().boxFor(RecommendUserCache.class);
            List<RecommendUserItem> userItems = new ArrayList<>();
            for (RecommendUserCache user : userBox.getAll()) {
                userItems.add(new RecommendUserItem(user));
            }
            items.add(Constant.USER_POSITION, new DiscoveryItem(2, userItems));
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
            List<DiscoveryItem> items = new ArrayList<>();

            long count = Math.min(postBox.count() - position, Constant.LOAD_MAX_DATABASE);
            for (int i = position; i < count; i++) {
                DiscoveryPostCache discoveryPostCache = discoveryPostCaches.get(i);
                items.add(new DiscoveryItem(discoveryPostCache.getType(), discoveryPostCache));
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
