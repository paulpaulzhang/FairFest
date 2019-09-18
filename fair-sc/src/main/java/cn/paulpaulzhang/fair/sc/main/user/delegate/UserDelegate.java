package cn.paulpaulzhang.fair.sc.main.user.delegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.main.market.activity.PublishActivity;
import cn.paulpaulzhang.fair.sc.main.search.SearchActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.FansActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.GoodsManageActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.PayActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.SettingActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.ShoppingCartActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserDynamicActivity;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user
 * 创建时间: 8/11/2019
 * 创建人: zlm31
 * 描述:
 */
public class UserDelegate extends FairDelegate {
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    @BindView(R2.id.srl_user)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.civ_user)
    CircleImageView mUserAvatar;

    @BindView(R2.id.tv_user)
    AppCompatTextView mUsername;

    @BindView(R2.id.tv_dynamic_count)
    AppCompatTextView mDynamicCount;

    @BindView(R2.id.tv_pay_count)
    AppCompatTextView mPayCount;

    @BindView(R2.id.tv_fans_count)
    AppCompatTextView mFansCount;

    @BindView(R2.id.ll_search)
    LinearLayout mSearch;

    @Override
    public Object setLayout() {
        return R.layout.delegate_user;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        initSwipeRefresh();
        mSearch.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle()));

    }

    private void loadUserData() {
        Box<User> userBox = ObjectBox.get().boxFor(User.class);
        User user = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        if (user != null) {
            Glide.with(this)
                    .load(user.getAvatar() != null ? user.getAvatar() : Constant.DEFAULT_AVATAR)
                    .into(mUserAvatar);
            mUsername.setText(user.getUsername() == null ?
                    String.valueOf(user.getId()) : user.getUsername());
            mDynamicCount.setText(String.valueOf(user.getDynamicCount()));
            mPayCount.setText(String.valueOf(user.getPaysCount()));
            mFansCount.setText(String.valueOf(user.getFansCount()));
        }
        mSwipeRefresh.setRefreshing(false);
    }

    private void requestUserData() {
        RestClient.builder()
                .url(Api.USER_INFO)
                .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                .success(r -> {
                    JsonParseUtil.parseUser(r);
                    loadUserData();
                })
                .error((code, msg) -> {
                    FairLogger.d(code);
                    mSwipeRefresh.setRefreshing(false);
                })
                .build()
                .get();
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(this::requestUserData);
    }

    @OnClick(R2.id.civ_setting)
    void setting() {
        startActivity(new Intent(getContext(), SettingActivity.class));
    }

    @OnClick(R2.id.ll_search)
    void search() {

    }

    @OnClick(R2.id.cl_user)
    void userCenter() {
        Intent intent = new Intent(getContext(), UserCenterActivity.class);
        intent.putExtra("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        startActivity(intent);
    }

    @OnClick(R2.id.ll_dynamic)
    void dynamic() {
        startActivity(new Intent(getContext(), UserDynamicActivity.class));
    }

    @OnClick(R2.id.ll_pay)
    void pay() {
        Intent intent = new Intent(getContext(), PayActivity.class);
        intent.putExtra("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        startActivity(intent);
    }

    @OnClick(R2.id.ll_fans)
    void fans() {
        Intent intent = new Intent(getContext(), FansActivity.class);
        intent.putExtra("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        startActivity(intent);
    }

    @OnClick(R2.id.cl_my_collection)
    void collection() {

    }

    @OnClick(R2.id.cl_my_like)
    void like() {

    }

    @OnClick(R2.id.cl_my_want_buy)
    void wantBuy() {
        startActivity(new Intent(getContext(), ShoppingCartActivity.class));
    }

    @OnClick(R2.id.cl_my_free)
    void free() {
        startActivity(new Intent(getContext(), GoodsManageActivity.class));
    }

    @OnClick(R2.id.cl_sold)
    void sold() {
        startActivity(new Intent(getContext(), PublishActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        requestUserData();
    }
}
