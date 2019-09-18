package cn.paulpaulzhang.fair.sc.main.user.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.gyf.immersionbar.ImmersionBar;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.file.IUploadFileListener;
import cn.paulpaulzhang.fair.sc.file.UploadUtil;
import cn.paulpaulzhang.fair.sc.main.chat.MessageActivity;
import cn.paulpaulzhang.fair.sc.main.user.adapter.ViewPagerAdapter;
import cn.paulpaulzhang.fair.sc.main.user.delegate.AboutDelegate;
import cn.paulpaulzhang.fair.sc.main.user.delegate.ConcernTopicDelegate;
import cn.paulpaulzhang.fair.sc.main.user.delegate.DynamicDelegate;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.dimen.DimenUtil;
import cn.paulpaulzhang.fair.util.file.FileUtil;
import cn.paulpaulzhang.fair.util.image.Glide4Engine;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user
 * 创建时间: 7/12/2019
 * 创建人: zlm31
 * 描述:
 */
public class UserCenterActivity extends FairActivity {
    @BindView(R2.id.abl_user)
    AppBarLayout mAppBarLayout;

    @BindView(R2.id.ctl_user)
    CollapsingToolbarLayout mCollapsingLayout;

    @BindView(R2.id.tb_user)
    MaterialToolbar mToolbar;

    @BindView(R2.id.stl_user)
    SlidingTabLayout mTabLayout;

    @BindView(R2.id.iv_bg)
    AppCompatImageView mBackground;

    @BindView(R2.id.civ_avatar_title)
    CircleImageView mTitleAvatar;

    @BindView(R2.id.tv_name_title)
    AppCompatTextView mTitleName;

    @BindView(R2.id.vp_user)
    ViewPager mViewPager;

    @BindView(R2.id.cl_user)
    ConstraintLayout mConstraintLayout;

    @BindView(R2.id.civ_avatar)
    CircleImageView mAvatar;

    @BindView(R2.id.tv_name)
    AppCompatTextView mName;

    @BindView(R2.id.btn_edit)
    MaterialButton mEdit;

    @BindView(R2.id.btn_follow)
    MaterialButton mFollow;

    @BindView(R2.id.tv_introduction)
    AppCompatTextView mIntroduction;

    @BindView(R2.id.tv_pay_count)
    AppCompatTextView mPayCount;

    @BindView(R2.id.tv_fans_count)
    AppCompatTextView mFansCount;

    @BindView(R2.id.tv_pay)
    AppCompatTextView mPay;

    @BindView(R2.id.tv_fans)
    AppCompatTextView mFans;

    @BindView(R2.id.tv_gender_era)
    AppCompatTextView mGenderEra;

    @BindView(R2.id.tv_constellation)
    AppCompatTextView mConstellation;

    @BindView(R2.id.tv_level)
    AppCompatTextView mLevel;

    @BindView(R2.id.tv_college)
    AppCompatTextView mCollege;

    private ViewPagerAdapter mPagerAdapter;

    private int lastPosition = 0;
    private long uid;
    private User user;


    @Override
    public int setLayout() {
        return R.layout.activity_user_center;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).titleBar(mToolbar).init();
        Intent intent = getIntent();
        uid = intent.getLongExtra("uid", -1);
        user = new User();
        initToolbar(mToolbar);
        initHeader();
        initTab();

        if (uid != -1) {
            requestData();
        }

    }

    @Override
    public void initToolbar(Toolbar mToolbar) {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initHeader() {
        if (uid == FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())) {
            mEdit.setVisibility(View.VISIBLE);
            mFollow.setVisibility(View.GONE);
        } else {
            mEdit.setVisibility(View.GONE);
            mFollow.setVisibility(View.VISIBLE);
        }

        mBackground.setOnClickListener(v -> {
            if (uid == FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())) {
                MaterialDialog materialDialog = new MaterialDialog(this, MaterialDialog.getDEFAULT_BEHAVIOR());
                DialogCustomViewExtKt.customView(materialDialog, R.layout.dialog_change_bg,
                        null, false, true, false, true);
                LifecycleExtKt.lifecycleOwner(materialDialog, this);
                materialDialog.cornerRadius(4f, null);
                materialDialog.show();

                View customerView = DialogCustomViewExtKt.getCustomView(materialDialog);

                customerView.findViewById(R.id.tv_change_bg).setOnClickListener(v2 -> {
                    openAlbum();
                    materialDialog.dismiss();
                });
            }
        });

        mAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra("path", user.getAvatar() == null ? Constant.DEFAULT_AVATAR : user.getAvatar());
            //noinspection unchecked
            startActivity(intent,  ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        });

        mEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        mPay.setOnClickListener(v->{
            Intent intent = new Intent(this, PayActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        mFans.setOnClickListener(v->{
            Intent intent = new Intent(this, FansActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        mFollow.setOnClickListener(v -> {
            if (TextUtils.equals(mFollow.getText().toString(), "关注")) {
                RestClient.builder()
                        .url(Api.PAY_USER)
                        .params("focuserId", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                        .params("focusedId", uid)
                        .success(r -> {
                            String result = JSON.parseObject(r).getString("result");
                            int count = Integer.parseInt(mFansCount.getText().toString());
                            if (TextUtils.equals(result, "ok")) {
                                mFollow.setText("已关注");
                                mFansCount.setText(String.valueOf(count + 1));
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
                            int count = Integer.parseInt(mFansCount.getText().toString());
                            if (TextUtils.equals(result, "ok")) {
                                mFollow.setText("关注");
                                mFansCount.setText(String.valueOf(count - 1));
                            } else {
                                Toasty.error(this, "取消失败 ", Toasty.LENGTH_SHORT).show();
                            }
                        })
                        .error((code, msg) -> Toasty.error(this, "取消失败 " + code, Toasty.LENGTH_SHORT).show())
                        .build()
                        .post();
            }

        });

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                //展开
                mConstraintLayout.setAlpha(1.0f);
                mTitleAvatar.setVisibility(View.GONE);
                mTitleName.setVisibility(View.GONE);
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                //折叠
                mTitleAvatar.setVisibility(View.VISIBLE);
                mTitleName.setVisibility(View.VISIBLE);
                mConstraintLayout.setAlpha(0.0f);
            } else {
                //中间过程
                float alpha = (float) (appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset)) /
                        appBarLayout.getTotalScrollRange();
                mConstraintLayout.setAlpha(alpha);
                mTitleAvatar.setVisibility(View.GONE);
                mTitleName.setVisibility(View.GONE);
                mBackground.setColorFilter(Color.parseColor("#" + getAlpha(Math.abs(verticalOffset)) + "000000"));
                mAvatar.setColorFilter(Color.parseColor("#" + getAlpha(Math.abs(verticalOffset)) + "000000"));
            }
        });
    }

    private void initTab() {
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        String[] titles = new String[]{getString(R.string.dynamic),
                getString(R.string.concern_topic),
                getString(R.string.about)};
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setViewPager(mViewPager, titles);
        mTabLayout.getTitleView(0).setTextSize(18);
        mTabLayout.getTitleView(0).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabLayout.getTitleView(position).setTextSize(18);
                mTabLayout.getTitleView(position).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                if (lastPosition != position) {
                    mTabLayout.getTitleView(lastPosition).setTextSize(16);
                    mTabLayout.getTitleView(lastPosition).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                lastPosition = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.getTitleView(position).setTextSize(18);
                mTabLayout.getTitleView(position).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                if (lastPosition != position) {
                    mTabLayout.getTitleView(lastPosition).setTextSize(16);
                    mTabLayout.getTitleView(lastPosition).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadUserData() {
        Glide.with(this)
                .load(user.getAvatar() != null ? user.getAvatar() : Constant.DEFAULT_AVATAR)
                .into(mAvatar);
        Glide.with(this)
                .load(user.getAvatar() != null ? user.getAvatar() : Constant.DEFAULT_AVATAR)
                .into(mTitleAvatar);
        Glide.with(this)
                .load(user.getBackground() != null ? user.getBackground() : Constant.DEFAULT_BACKGROUND)
                .into(mBackground);
        mName.setText(user.getUsername() == null ?
                String.valueOf(user.getId()) : user.getUsername());
        mTitleName.setText(user.getUsername() != null ? user.getUsername() : user.getPhone());
        mIntroduction.setText(user.getIntroduction() != null ? user.getIntroduction() : "这个人很懒，什么也没有写");
        mPayCount.setText(String.valueOf(user.getPaysCount()));
        mFansCount.setText(String.valueOf(user.getFansCount()));
        String genderIcon = "";
        if (user.getGender() == null) {
            genderIcon = "⚥";
        } else if (TextUtils.equals(user.getGender(), "男")) {
            genderIcon = "♂";
        } else if (TextUtils.equals(user.getGender(), "女")) {
            genderIcon = "♀";
        }
        String era = DateUtil.getEra(user.getBirthday());
        if (TextUtils.equals(genderIcon, "") && TextUtils.equals(era, "")) {
            mGenderEra.setVisibility(View.GONE);
        } else {
            mGenderEra.setText(String.format("%s %s", genderIcon, era));
        }

        String constellation = DateUtil.getConstellation(user.getBirthday());
        if (TextUtils.equals(constellation, "")) {
            mConstellation.setVisibility(View.GONE);
        } else {
            mConstellation.setText(constellation);
        }

        if (user.getCollege() == null || TextUtils.equals(user.getCollege(), "")) {
            mCollege.setVisibility(View.GONE);
        } else {
            mCollege.setText(user.getCollege());
        }

        mLevel.setText("Lv.1");
    }

    private void requestData() {
        RestClient.builder()
                .url(Api.USER_INFO)
                .params("uid", uid)
                .success(r -> {
                    JSONObject object = JSON.parseObject(r).getJSONObject("user");
                    user.setId(uid);
                    user.setAvatar(object.getString("avatar"));
                    user.setBackground(object.getString("background"));
                    user.setBirthday(object.getLongValue("birthday"));
                    user.setDynamicCount(object.getInteger("dynamicCount"));
                    user.setFansCount(object.getInteger("fans"));
                    user.setGender(object.getString("gender"));
                    user.setUsername(object.getString("username"));
                    user.setPaysCount(object.getInteger("followers"));
                    user.setSchool(object.getString("school"));
                    user.setCollege(object.getString("college"));
                    user.setPhone(object.getString("phone"));
                    user.setIntroduction(object.getString("introduction"));
                    user.setTime(object.getLong("time"));
                    loadUserData();
                    loadChildData();
                })
                .error((code, msg) -> FairLogger.d(code))
                .build()
                .get();

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

    //加载子fragment的数据
    private void loadChildData() {
        DynamicDelegate dynamicDelegate = (DynamicDelegate) mPagerAdapter.getItem(0);
        dynamicDelegate.loadData(Constant.REFRESH_DATA);

        AboutDelegate aboutDelegate = (AboutDelegate) mPagerAdapter.getItem(2);
        aboutDelegate.loadUserData(user);

        ConcernTopicDelegate topicDelegate = (ConcernTopicDelegate) mPagerAdapter.getItem(1);
        topicDelegate.loadData();
    }

    private String getAlpha(int i) {
        int max = 85;
        float rate = 0.15f;
        float temp;
        if (i * rate < max) {
            temp = 255 * i * rate * 1.0f / 100f;
        } else {
            temp = 255 * max * 1.0f / 100f;
        }
        int alpha = Math.round(temp);
        String hexStr = Integer.toHexString(alpha);
        if (hexStr.length() < 2)
            hexStr = "0" + hexStr;
        return hexStr.toUpperCase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            cropPhoto(Matisse.obtainResult(data).get(0));
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
            final Uri uri = UCrop.getOutput(data);
            if (uri != null) {
                Glide.with(UserCenterActivity.this).load(uri).into(mBackground);
                String path = uri.getPath();
                if (path != null) {
                    UploadUtil util = UploadUtil.INSTANCE();
                    List<File> files = new ArrayList<>();
                    files.add(new File(path));
                    util.uploadFile(this, files, new IUploadFileListener() {
                        @Override
                        public void onSuccess(List<String> paths) {
                            if (paths == null || paths.size() != 1) {
                                Toasty.error(UserCenterActivity.this, "上传失败", Toasty.LENGTH_SHORT).show();
                                return;
                            }

                            RestClient.builder()
                                    .url(Api.EDIT_BACKGROUND)
                                    .params("uid", uid)
                                    .params("background", paths.get(0))
                                    .success(response -> {
                                        String result = JSON.parseObject(response).getString("result");
                                        if (TextUtils.equals(result, "ok")) {
                                            Box<User> userBox = ObjectBox.get().boxFor(User.class);
                                            User user = userBox.get(uid);
                                            user.setBackground(paths.get(0));
                                            userBox.put(user);
                                        }
                                    })
                                    .error((code, msg) ->
                                            Toasty.error(UserCenterActivity.this, "上传失败 " + code, Toasty.LENGTH_SHORT).show())
                                    .build()
                                    .post();
                        }

                        @Override
                        public void onError() {
                            Toasty.error(UserCenterActivity.this, "上传失败", Toasty.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        } else if (requestCode == UCrop.RESULT_ERROR) {
            Toasty.error(UserCenterActivity.this, "无法裁剪图片", Toasty.LENGTH_SHORT).show();
        }
    }

    private void openAlbum() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Matisse.from(this)
                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                    .maxSelectable(1)
                    .countable(false)
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true, "cn.paulpaulzhang.fairfest.fileprovider"))
                    .gridExpectedSize(getResources()
                            .getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .theme(R.style.Matisse_Zhihu_Custom)
                    .imageEngine(new Glide4Engine())
                    .forResult(Constant.REQUEST_CODE_CHOOSE);
        } else {
            EasyPermissions.requestPermissions(this, "打开图库需要存储读取权限", 1001,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void cropPhoto(Uri uri) {
        Uri destinationUri = Uri.fromFile(new File(getExternalCacheDir(),
                FileUtil.getFileNameByTime("FairSchool", FileUtil.getExtension(uri.getPath()))));
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getColor(R.color.colorAccent)); // 设置标题栏颜色
        options.setStatusBarColor(getColor(R.color.colorAccent)); //设置状态栏颜色
        options.setToolbarWidgetColor(getColor(android.R.color.white));
        UCrop.of(uri, destinationUri)
                .withAspectRatio(DimenUtil.getScreenWidthByDp(), 360)
                .withMaxResultSize(1080, 1080)
                .withOptions(options)
                .start(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_center_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (uid == FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())) {
            menu.findItem(R.id.message).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.refresh) {
            if (uid != -1) {
                requestData();
                Toasty.info(this, "正在刷新", Toasty.LENGTH_SHORT).show();
            }

        } else if (item.getItemId() == R.id.message) {
            if (uid != -1) {
                Intent intent = new Intent(UserCenterActivity.this, MessageActivity.class);
                intent.putExtra("uid", String.valueOf(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name())));
                intent.putExtra("username", String.valueOf(user.getId()));
                startActivity(intent);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public long getUid() {
        return uid;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
