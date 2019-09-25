package cn.paulpaulzhang.fair.sc.main.market.activity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
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
import cn.paulpaulzhang.fair.sc.main.chat.MessageActivity;
import cn.paulpaulzhang.fair.sc.main.common.PhotoActivity;
import cn.paulpaulzhang.fair.sc.main.user.activity.UserCenterActivity;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.dimen.DimenUtil;
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

    @BindView(R2.id.tv_college)
    AppCompatTextView mCollege;

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

    @BindView(R2.id.btn_follow)
    MaterialButton mFollow;

    private long sid;
    private long uid;

    private String imgUrl;
    private String name;

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

        if (sid == -1 || uid == -1) {
            Toasty.error(this, "初始化失败", Toasty.LENGTH_SHORT).show();
            finish();
        }

        if (uid == FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())) {
            mFollow.setVisibility(View.INVISIBLE);
        } else {
            RestClient.builder()
                    .url(Api.IS_PAY_USER)
                    .params("focuserId", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("focusedId", uid)
                    .success(r -> {
                        String result = JSON.parseObject(r).getString("result");
                        if (TextUtils.equals(result, "已关注")) {
                            mFollow.setText("已关注");
                        } else {
                            mFollow.setText("关注");
                        }
                    })
                    .error(((code, msg) -> FairLogger.d(code)))
                    .build()
                    .get();
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
        RestClient.builder()
                .url(Api.USER_INFO)
                .params("uid", uid)
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        JSONObject user = JSON.parseObject(response).getJSONObject("user");
                        String username = user.getString("username");
                        String avatar = user.getString("avatar");
                        String college = user.getString("college");
                        mUsername.setText(username == null ? String.valueOf(uid) : username);
                        Glide.with(this)
                                .load(avatar == null ? Constant.DEFAULT_AVATAR : avatar)
                                .into(mAvatar);
                        mCollege.setText(String.format("%s专业", college));
                    } else {
                        Toasty.error(this, "加载失败", Toasty.LENGTH_SHORT).show();
                    }
                })
                .error((code, msg) -> {
                    Toasty.error(this, "加载失败 " + code, Toasty.LENGTH_SHORT).show();
                    mSwipeRefresh.setRefreshing(false);
                })
                .build()
                .get();

        RestClient.builder()
                .url(Api.GET_STORE_INFO)
                .params("sid", sid)
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        JSONObject goods = JSON.parseObject(response).getJSONObject("store");
                        name = goods.getString("sname");
                        imgUrl = goods.getString("headImg");
                        String overview = goods.getString("overview");
                        float price = goods.getFloatValue("price");
                        long time = goods.getLongValue("time");

                        mName.setText(name);
                        mInfo.setText(overview);
                        mPrice.setText(String.valueOf(price));
                        mTime.setText(DateUtil.getTime(time));

                        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setOldController(mImg.getController())
                                .setControllerListener(controllerListener)
                                .setUri(Uri.parse(imgUrl))
                                .build();
                        mImg.setController(controller);
                        mSwipeRefresh.setRefreshing(false);
                    } else {
                        Toasty.error(this, "加载失败", Toasty.LENGTH_SHORT).show();
                    }
                })
                .error((code, msg) -> {
                    Toasty.error(this, "加载失败 " + code, Toasty.LENGTH_SHORT).show();
                    mSwipeRefresh.setRefreshing(false);
                })
                .build()
                .get();
    }


    private ControllerListener<ImageInfo> controllerListener = new ControllerListener<ImageInfo>() {
        @Override
        public void onSubmit(String id, Object callerContext) {

        }

        @Override
        public void onFinalImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo, @javax.annotation.Nullable Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            int height = imageInfo.getHeight();
            int width = imageInfo.getWidth();
            int imgWidth = DimenUtil.getScreenWidth() - DimenUtil.dip2px(20);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImg.getLayoutParams();
            params.width = imgWidth;
            params.height = (int) ((float) imgWidth * height / width);
            mImg.setLayoutParams(params);
        }

        @Override
        public void onIntermediateImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo) {

        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {

        }

        @Override
        public void onFailure(String id, Throwable throwable) {

        }

        @Override
        public void onRelease(String id) {

        }
    };

    @OnClick(R2.id.dv_img)
    void toPhoto() {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("path", imgUrl);
        //noinspection unchecked
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    @OnClick(R2.id.btn_follow)
    void follow() {
        if (TextUtils.equals(mFollow.getText().toString(), "关注")) {
            RestClient.builder()
                    .url(Api.PAY_USER)
                    .params("focuserId", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("focusedId", uid)
                    .success(r -> {
                        String result = JSON.parseObject(r).getString("result");
                        if (TextUtils.equals(result, "ok")) {
                            mFollow.setText("已关注");
                        } else {
                            Toasty.error(this, "关注失败 ", Toasty.LENGTH_SHORT).show();
                        }
                    })
                    .error((code, msg) -> Toasty.error(this, "关注失败 " + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .post();
        } else {
            RestClient.builder()
                    .url(Api.CANCEL_PAY_USER)
                    .params("focuserId", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                    .params("focusedId", uid)
                    .success(r -> {
                        String result = JSON.parseObject(r).getString("result");
                        if (TextUtils.equals(result, "ok")) {
                            mFollow.setText("关注");
                        } else {
                            Toasty.error(this, "取消失败 ", Toasty.LENGTH_SHORT).show();
                        }
                    })
                    .error((code, msg) -> Toasty.error(this, "取消失败 " + code, Toasty.LENGTH_SHORT).show())
                    .build()
                    .post();
        }
    }

    @OnClick(R2.id.civ_user)
    void userCenter() {
        Intent intent = new Intent(this, UserCenterActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    @OnClick(R2.id.btn_buy)
    void buy() {
        String info = "您的宝贝 [~ " + name + " ~] 我看上啦，请尽快回复!";
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()) + "");
        intent.putExtra("username", uid + "");
        intent.putExtra("info", info);
        startActivity(intent);
    }

    @OnClick(R2.id.btn_cart)
    void addCart() {
        RestClient.builder()
                .url(Api.IS_IN_SHOPPING_CART)
                .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                .params("sid", sid)
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "不想买")) {
                        RestClient.builder()
                                .url(Api.ADD_TO_SHOPPING_CART)
                                .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                                .params("sid", sid)
                                .success(r -> {
                                    String res = JSON.parseObject(r).getString("result");
                                    if (TextUtils.equals(res, "ok")) {
                                        Toasty.success(this, "添加成功，请到" + getString(R.string.shopping_cart) + "查看", Toasty.LENGTH_SHORT).show();
                                    } else {
                                        Toasty.error(this, "添加失败", Toasty.LENGTH_SHORT).show();
                                    }
                                })
                                .error((code, msg) -> Toasty.error(this, "添加失败 " + code, Toasty.LENGTH_SHORT).show())
                                .build()
                                .post();
                    } else {
                        Toasty.info(this, "宝贝已经在" + getString(R.string.shopping_cart) + "里啦!", Toasty.LENGTH_SHORT).show();
                    }
                })
                .error((code, msg) -> Toasty.error(this, "添加失败 " + code, Toasty.LENGTH_SHORT).show())
                .build()
                .post();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
