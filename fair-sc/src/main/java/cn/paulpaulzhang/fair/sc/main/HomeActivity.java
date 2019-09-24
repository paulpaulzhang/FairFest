package cn.paulpaulzhang.fair.sc.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.Features;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.main.user.activity.EditActivity;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import io.objectbox.Box;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/3/19
 * 创建人： paulpaulzhang
 * 描述：首页activity
 */
public class HomeActivity extends FairActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R2.id.bottom_navigation)
    AHBottomNavigation mBottomNavigation;

    @BindView(R2.id.view_pager)
    AHBottomNavigationViewPager mViewPager;


    private AHBottomNavigationAdapter mAdapter;
    private BottomNavViewPagerAdapter mViewPagerAdapter;
    private List<AHBottomNavigationItem> items = new ArrayList<>();
    private FairDelegate currentDelegate;

    @Override
    public int setLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initBottomNavigation();
        requestPermissions();
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();
        setUserFeatures();
    }

    private void judgeUserInfo() {
        RestClient.builder()
                .url(Api.USER_INFO)
                .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                .success(r -> {
                    JsonParseUtil.parseUser(r);
                    Box<User> userBox = ObjectBox.get().boxFor(User.class);
                    User user = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
                    if (user.getSchool() == null || user.getSchool().isEmpty() ||
                            user.getStudentId() == 0 ||
                            user.getCollege() == null || user.getCollege().isEmpty()) {
                        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                                .setTitle("完善用户信息")
                                .setMessage("应用检测到你未完善用户信息，请前往个人中心完善信息(学校、学号、专业)，否则应用无法正常工作")
                                .setNegativeButton("退出", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    finish();
                                })
                                .setPositiveButton("完善资料", (dialogInterface, i) -> {
                                    Intent intent = new Intent(this, EditActivity.class);
                                    intent.putExtra("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
                                    startActivity(intent);
                                    dialogInterface.dismiss();
                                    FairLogger.d("dialog关闭");
                                })
                                .setCancelable(false)
                                .show();
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(android.R.color.holo_red_light));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorAccent));
                    }
                })
                .error((code, msg) -> FairLogger.d(code))
                .build()
                .get();
    }

    private void setUserFeatures() {
        Box<Features> featuresBox = ObjectBox.get().boxFor(Features.class);
        RestClient.builder()
                .url(Api.GET_FEATURES)
                .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        String feature = JSON.parseObject(response).getString("features");
                        featuresBox.removeAll();
                        featuresBox.put(new Features(feature));
                    }
                })
                .build().get();
    }

    private void requestPermissions() {
        EasyPermissions.requestPermissions(this, "应用需要一些必要权限", 1000,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void initBottomNavigation() {

        AHBottomNavigationItem interest = new AHBottomNavigationItem
                (getString(R.string.interest), R.drawable.ic_interest);
        AHBottomNavigationItem market = new AHBottomNavigationItem
                (getString(R.string.market), R.drawable.ic_mall);
        AHBottomNavigationItem message = new AHBottomNavigationItem
                (getString(R.string.message), R.drawable.ic_message);
        AHBottomNavigationItem user = new AHBottomNavigationItem
                (getString(R.string.mine), R.drawable.ic_me);

        items.add(interest);
        items.add(market);
        items.add(message);
        items.add(user);

        mBottomNavigation.addItems(items);
        mBottomNavigation.setTranslucentNavigationEnabled(true);
        mBottomNavigation.setBehaviorTranslationEnabled(false);
        mBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        mBottomNavigation.setAccentColor(getColor(R.color.colorAccent));
        mBottomNavigation.setInactiveColor(getColor(R.color.font_default));

        mBottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if (currentDelegate == null) {
                currentDelegate = mViewPagerAdapter.getCurrentDelegate();
            }

            if (wasSelected) {
                //TODO
            }

            mViewPager.setCurrentItem(position, false);

            if (currentDelegate == null) {
                return true;
            }

            return true;

        });

        mViewPager.setOffscreenPageLimit(3);
        mViewPagerAdapter = new BottomNavViewPagerAdapter
                (getSupportFragmentManager(),
                        BottomNavViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);
        mBottomNavigation.setCurrentItem(0);

        currentDelegate = mViewPagerAdapter.getCurrentDelegate();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "拒绝权限可能会影响使用，请前往设置开启所需权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeUserInfo();
    }
}
