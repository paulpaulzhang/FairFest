package cn.paulpaulzhang.fair.sc.main.market.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.market.activity
 * 创建时间：9/19/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class GoodsDetailsActivity extends FairActivity {

    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    @BindView(R2.id.tv_username)
    AppCompatTextView mUsername;

    @BindView(R2.id.tv_time)
    AppCompatTextView mTime;

    @BindView(R2.id.tv_device)
    AppCompatTextView mDevice;

    @BindView(R2.id.civ_user)
    CircleImageView mAvatar;

    @BindView(R2.id.tv_name)
    AppCompatTextView mName;

    @BindView(R2.id.tv_price)
    AppCompatTextView mPrice;

    @BindView(R2.id.tv_info)
    AppCompatTextView mInfo;

    @BindView(R2.id.dv_img)
    SimpleDraweeView mImg;

    @BindView(R2.id.srl_goods)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R2.id.btn_buy)
    MaterialButton mBuy;

    @BindView(R2.id.btn_cart)
    MaterialButton mCart;

    @BindView(R2.id.btn_delete)
    MaterialButton mDel;

    private long sid;
    private long uid;

    @Override
    public int setLayout() {
        return R.layout.activity_goods_details;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        initToolbar(mToolbar, "宝贝详情");

        Intent intent = getIntent();
        sid = intent.getLongExtra("sid", -1);
        uid = intent.getLongExtra("uid", -1);

        FairLogger.d(uid + "   " + FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        if (sid == -1 || uid == -1) {
            Toasty.error(this, "初始化失败", Toasty.LENGTH_SHORT).show();
            finish();
        }

        if (uid == FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())) {
            mBuy.setVisibility(View.GONE);
            mCart.setVisibility(View.GONE);
            mDel.setVisibility(View.VISIBLE);
        }

        initSwipeRefresh();
        loadData();
        mSwipeRefresh.setRefreshing(true);
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(this::loadData);
    }

    private void loadData() {

    }

    @OnClick(R2.id.btn_buy)
    void buy() {

    }

    @OnClick(R2.id.btn_cart)
    void addCart() {
        RestClient.builder()
                .url(Api.ADD_TO_SHOPPING_CART)
                .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                .params("sid", sid)
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        Toasty.success(this, "添加成功，请到" + getString(R.string.shopping_cart) + "查看", Toasty.LENGTH_SHORT).show();
                    } else {
                        Toasty.error(this, "添加失败", Toasty.LENGTH_SHORT).show();
                    }
                })
                .error((code, msg) -> Toasty.error(this, "添加失败 " + code, Toasty.LENGTH_SHORT).show())
                .build()
                .post();
    }

    @OnClick(R2.id.btn_delete)
    void delete() {
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("操作确认")
                .setMessage("点击确认下架并删除该宝贝")
                .setPositiveButton("确认", (dialogInterface, i) -> {
                    RestClient.builder()
                            .url(Api.DELETE_STORE)
                            .params("sid", sid)
                            .success(response -> {
                                String result = JSON.parseObject(response).getString("result");
                                if (TextUtils.equals(result, "ok")) {
                                    finish();
                                } else {
                                    Toasty.error(this, "删除失败", Toasty.LENGTH_SHORT).show();
                                }
                            })
                            .error((code, msg) -> Toasty.error(this, "删除失败 " + code, Toasty.LENGTH_SHORT).show())
                            .build()
                            .post();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(android.R.color.holo_red_light));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.font_default));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
