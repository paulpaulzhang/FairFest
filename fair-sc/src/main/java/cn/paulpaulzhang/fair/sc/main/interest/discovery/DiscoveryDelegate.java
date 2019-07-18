package cn.paulpaulzhang.fair.sc.main.interest.discovery;

import android.os.Bundle;
import android.os.Handler;
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
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Constant;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.entity.Post;
import cn.paulpaulzhang.fair.sc.json.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.banner.BannerHolderCreator;
import cn.paulpaulzhang.fair.util.log.FairLogger;
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
        mSwipeRefresh.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            mSwipeRefresh.setRefreshing(false);
            loadData(Constant.REFRESH_DATA);
        }, 1000));
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
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mAdapter.setOnLoadMoreListener(() -> loadData(Constant.LOAD_MORE_DATA), mRecyclerView);
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
        Box<Post> postBox = ObjectBox.get().boxFor(Post.class);
        int position = mAdapter.getData().size();
        if (type == Constant.REFRESH_DATA) {
            requestData(0, Constant.REFRESH_DATA);
            List<Post> posts = postBox.getAll();
            //TODO 加载列表用户数据
            List<DiscoveryItem> items = new ArrayList<>();
            long count = Math.min(postBox.count(), Constant.LOAD_MAX_DATABASE);
            for (int i = 0; i < count; i++) {
                Post post = posts.get(i);
                items.add(new DiscoveryItem(post.getType(), post));
            }
            mAdapter.setNewData(items);
            mSwipeRefresh.setRefreshing(false);

        } else if (type == Constant.LOAD_MORE_DATA) {
            long size = postBox.count();
            if (position + Constant.LOAD_MAX_DATABASE > size) {
                requestData(size, Constant.LOAD_MORE_DATA);
                if (size == postBox.count()) {
                    FairLogger.d("没有更多数据");
                    mAdapter.loadMoreEnd(true);
                    return;
                }
            }
            List<Post> posts = postBox.getAll();
            List<DiscoveryItem> items = new ArrayList<>();

            long count = Math.min(postBox.count() - position, Constant.LOAD_MAX_DATABASE);
            for (int i = position; i < count; i++) {
                Post post = posts.get(i);
                items.add(new DiscoveryItem(post.getType(), post));
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
     *              <p>
     *              新建一个浏览用户表
     *              每一次请求帖子数据成功后在ISuccess回调接口里面
     *              请求发贴用户的数据并保存或更新至数据库的浏览用户表
     */
    private void requestData(long start, int type) {
        Box<Post> postBox = ObjectBox.get().boxFor(Post.class);
        if (type == Constant.REFRESH_DATA) {
            RestClient.builder()
                    .url("post")
                    .params("position", 0)
                    .params("number", Constant.LOAD_MAX_SEVER)
                    .success(r -> JsonParseUtil.parsePost(r, type))
                    .build()
                    .get();
        } else if (type == Constant.LOAD_MORE_DATA) {
            RestClient.builder()
                    .url("post")
                    .params("position", start)
                    .params("number", Constant.LOAD_MAX_SEVER)
                    .success(r -> JsonParseUtil.parsePost(r, type))
                    .build()
                    .get();
        }
    }

}
