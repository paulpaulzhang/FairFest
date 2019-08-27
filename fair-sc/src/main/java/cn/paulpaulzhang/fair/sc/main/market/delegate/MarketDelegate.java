package cn.paulpaulzhang.fair.sc.main.market.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.ProductCache;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.main.interest.delegate.AbstractDelegate;
import cn.paulpaulzhang.fair.sc.main.market.adapter.ProductAdapter;
import cn.paulpaulzhang.fair.sc.main.market.model.Product;
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
            if (item.getItemId() == R.id.order) {
                Toasty.info(getContext(), "订单", Toasty.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.want_buy) {
                Toasty.info(getContext(), "想买", Toasty.LENGTH_SHORT).show();
            }
            return true;
        });
        loadUser();

        initRecyclerView();
        initSwipeRefresh();
    }

    private void loadUser() {
        Box<User> userBox = ObjectBox.get().boxFor(User.class);
        String avatar = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())).getAvatar();
        Glide.with(this).load(avatar == null ? Constant.DEFAULT_AVATAR : avatar).into(mUser);
    }

    private void initRecyclerView() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            products.add(new Product(new ProductCache()));
        }
        mAdapter = new ProductAdapter(R.layout.item_market, products);
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(manager);

    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> {
            mSwipeRefresh.setRefreshing(false);
        });
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
