package cn.paulpaulzhang.fair.sc.main.user.delegate;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.LocalUser;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
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

    @Override
    public Object setLayout() {
        return R.layout.delegate_user;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        initSwipeRefresh();
        loadUserData();
    }

    private void loadUserData() {
        Box<LocalUser> userBox = ObjectBox.get().boxFor(LocalUser.class);
        LocalUser user = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        if (user != null) {
            Glide.with(this)
                    .load(user.getAvatar() != null ? user.getAvatar() : Constant.DEFAULT_AVATAR)
                    .into(mUserAvatar);
            mUsername.setText(user.getUsername() != null ? user.getUsername() : user.getPhone());
            mDynamicCount.setText(user.getDynamicCount());
            mPayCount.setText(user.getPaysCount());
            mFansCount.setText(user.getFansCount());
        }
        mSwipeRefresh.setRefreshing(false);
    }

    private void requestUserData() {
        //TODO  请求服务器端用户信息后调用loadUserData()跟新用户数据
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(this::loadUserData);
    }

    @OnClick(R2.id.civ_setting)
    void setting() {

    }

    @OnClick(R2.id.ll_search)
    void search() {

    }

    @OnClick(R2.id.cl_user)
    void userCenter() {

    }

    @OnClick(R2.id.ll_dynamic)
    void dynamic() {

    }

    @OnClick(R2.id.ll_pay)
    void pay() {

    }

    @OnClick(R2.id.ll_fans)
    void fans() {

    }

    @OnClick(R2.id.cl_my_collection)
    void collection() {

    }

    @OnClick(R2.id.cl_my_like)
    void like() {

    }

    @OnClick(R2.id.cl_my_want_buy)
    void wantBuy() {

    }

    @OnClick(R2.id.cl_my_order)
    void order() {

    }

    @OnClick(R2.id.cl_my_record)
    void browseRecord() {

    }
}
