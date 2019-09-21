package cn.paulpaulzhang.fair.sc.main.market.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.ProductCache;
import cn.paulpaulzhang.fair.sc.database.Entity.ProductCache_;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.main.interest.delegate.AbstractDelegate;
import cn.paulpaulzhang.fair.sc.main.market.activity.GoodsDetailsActivity;
import cn.paulpaulzhang.fair.sc.main.market.activity.PublishActivity;
import cn.paulpaulzhang.fair.sc.main.market.adapter.ProductAdapter;
import cn.paulpaulzhang.fair.sc.main.market.model.Product;
import cn.paulpaulzhang.fair.sc.main.search.SearchActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/8/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class MarketDelegate extends AbstractDelegate {
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    @BindView(R2.id.civ_user)
    CircleImageView mUser;

    @BindView(R2.id.rv_market)
    RecyclerView mRecyclerView;

    @BindView(R2.id.srl_market)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.ll_search)
    LinearLayout mSearch;

    private ProductAdapter mAdapter;


    @Override
    public Object setLayout() {
        return R.layout.delegate_market;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        setHasOptionsMenu(true);
        mToolbar.inflateMenu(R.menu.market_menu);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.want_buy) {
                Toasty.info(Objects.requireNonNull(getContext()), "想买", Toasty.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.publish) {
                startActivity(new Intent(getContext(), PublishActivity.class));
            }
            return true;
        });
        loadUser();
        initRecyclerView();
        initSwipeRefresh();
        mSwipeRefresh.setRefreshing(true);
        loadData(Constant.REFRESH_DATA);

        mSearch.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle()));

    }

    private void loadUser() {
        Box<User> userBox = ObjectBox.get().boxFor(User.class);
        String avatar = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())).getAvatar();
        Glide.with(this).load(avatar == null ? Constant.DEFAULT_AVATAR : avatar).into(mUser);
    }

    private void initRecyclerView() {
        mAdapter = new ProductAdapter(R.layout.item_market, new ArrayList<>());
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);
        mAdapter.setOnLoadMoreListener(() -> {
            loadData(Constant.LOAD_MORE_DATA);
        }, mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Product item = (Product) adapter.getItem(position);
            if (item != null) {
                Intent intent = new Intent(getContext(), GoodsDetailsActivity.class);
                intent.putExtra("sid", item.getProductCache().getId());
                intent.putExtra("uid", item.getProductCache().getUid());
                startActivity(intent);
            }
        });

    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> loadData(Constant.REFRESH_DATA));
    }

    private int page = 0;

    private void loadData(int type) {
        Box<ProductCache> productCacheBox = ObjectBox.get().boxFor(ProductCache.class);
        int position = mAdapter.getData().size();
        if (type == Constant.REFRESH_DATA) {
            requestData(0, Constant.REFRESH_DATA, response -> {
                page = 0;
                JsonParseUtil.parseProduct(response, Constant.REFRESH_DATA);
                List<ProductCache> productCaches = productCacheBox.query().orderDesc(ProductCache_.time).build().find();
                List<Product> items = new ArrayList<>();
                long count = Math.min(productCacheBox.count(), Constant.LOAD_MAX_DATABASE);
                for (int i = 0; i < count; i++) {
                    ProductCache productCache = productCaches.get(i);
                    if (productCache.getIsSold() == 0) {
                        items.add(new Product(productCache));
                    }
                }
                mAdapter.setNewData(items);
                mSwipeRefresh.setRefreshing(false);
            });

        } else if (type == Constant.LOAD_MORE_DATA) {
            long size = productCacheBox.count();
            if (position + Constant.LOAD_MAX_DATABASE > size) {
                requestData(page, Constant.LOAD_MORE_DATA, response -> {
                    page += 1;
                    JsonParseUtil.parseProduct(response, Constant.LOAD_MORE_DATA);
                    List<ProductCache> productCaches = productCacheBox.getAll();
                    List<Product> items = new ArrayList<>();
                    if (size == productCacheBox.count()) {
                        mAdapter.loadMoreEnd(true);
                        return;
                    }
                    long count = Math.min(productCacheBox.count() - position, Constant.LOAD_MAX_DATABASE);
                    for (int i = position; i < count; i++) {
                        ProductCache productCache = productCaches.get(i);
                        items.add(new Product(productCache));
                    }
                    mAdapter.addData(items);
                    mAdapter.loadMoreComplete();
                });
            }

        }
    }

    private void requestData(int page, int type, ISuccess success) {
        if (type == Constant.REFRESH_DATA) {
            RestClient.builder()
                    .url(Api.LIST_STORE_BY_PAGE)
                    .params("pageNo", 0)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .success(success)
                    .error((code, msg) -> {
                        Toasty.error(Objects.requireNonNull(getContext()), "请求失败 " + code, Toasty.LENGTH_SHORT).show();
                        mSwipeRefresh.setRefreshing(false);
                    })
                    .build()
                    .get();
        } else {
            RestClient.builder()
                    .url(Api.LIST_STORE_BY_PAGE)
                    .params("pageNo", page)
                    .params("pageSize", Constant.LOAD_MAX_SEVER)
                    .success(success)
                    .error((code, msg) -> {
                        Toasty.error(Objects.requireNonNull(getContext()), "请求失败 " + code, Toasty.LENGTH_SHORT).show();
                        mSwipeRefresh.setRefreshing(false);
                    })
                    .build()
                    .get();
        }

    }

    @Override
    public void refresh() {
        mSwipeRefresh.setRefreshing(true);
    }

    @OnClick(R2.id.civ_user)
    void user() {
        Intent intent = new Intent(getContext(), UserCenterActivity.class);
        intent.putExtra("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
    }
}
