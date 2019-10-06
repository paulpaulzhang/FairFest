package cn.paulpaulzhang.fair.sc.main.user.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.date.DatePicker;
import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.MaterialToolbar;
import com.gyf.immersionbar.ImmersionBar;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.Major;
import cn.paulpaulzhang.fair.sc.database.Entity.Major_;
import cn.paulpaulzhang.fair.sc.database.Entity.School;
import cn.paulpaulzhang.fair.sc.database.Entity.School_;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.file.IUploadFileListener;
import cn.paulpaulzhang.fair.sc.file.UploadUtil;
import cn.paulpaulzhang.fair.sc.main.user.adapter.StringAdapter;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.dimen.DimenUtil;
import cn.paulpaulzhang.fair.util.file.FileUtil;
import cn.paulpaulzhang.fair.util.image.Glide4Engine;
import cn.paulpaulzhang.fair.util.keyboard.KeyBoardUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.activity
 * 创建时间：8/31/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class EditActivity extends FairActivity {

    @BindView(R2.id.civ_avatar)
    CircleImageView mAvatar;

    @BindView(R2.id.dv_background)
    SimpleDraweeView mBackground;

    @BindView(R2.id.tv_username)
    AppCompatTextView mUsername;

    @BindView(R2.id.tv_gender)
    AppCompatTextView mGender;

    @BindView(R2.id.tv_birthday)
    AppCompatTextView mBirthday;

    @BindView(R2.id.tv_constellation)
    AppCompatTextView mConstellation;

    @BindView(R2.id.tv_school)
    AppCompatTextView mSchool;

    @BindView(R2.id.tv_sid)
    AppCompatTextView mSid;

    @BindView(R2.id.tv_college)
    AppCompatTextView mCollege;

    @BindView(R2.id.tv_introduction)
    AppCompatTextView mIntroduction;

    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    private long uid;
    private User user;
    private Box<User> userBox;


    @Override
    public int setLayout() {
        return R.layout.activity_edit;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();

        initToolbar(mToolbar, "编辑资料");

        Intent intent = getIntent();
        uid = intent.getLongExtra("uid", -1);
        if (uid == -1) {
            Toasty.info(this, "未知错误", Toasty.LENGTH_SHORT).show();
            finish();
        }

        userBox = ObjectBox.get().boxFor(User.class);
        user = userBox.get(uid);

        initUserInfo();
    }

    private void initUserInfo() {
        if (user.getAvatar() != null) {
            Glide.with(this).load(user.getAvatar()).into(mAvatar);
        } else {
            Glide.with(this).load(Constant.DEFAULT_AVATAR).into(mAvatar);
        }

        if (user.getBackground() != null) {
            mBackground.setImageURI(Uri.parse(user.getBackground()));
        } else {
            mBackground.setImageURI(Uri.parse(Constant.DEFAULT_BACKGROUND));
        }

        if (user.getUsername() != null) {
            mUsername.setText(user.getUsername());
        }

        if (user.getGender() != null) {
            mGender.setText(user.getGender());
        }

        if (user.getBirthday() != 0) {
            mBirthday.setText(DateUtil.date2String(new Date(user.getBirthday()), "YYYY-MM-dd"));
            mConstellation.setText(DateUtil.getConstellation(user.getBirthday()));
        }

        if (user.getSchool() != null) {
            mSchool.setText(user.getSchool());
        }

        if (user.getStudentId() != 0) {
            mSid.setText(String.valueOf(user.getStudentId()));
        }

        if (user.getCollege() != null) {
            mCollege.setText(user.getCollege());
        }

        if (user.getIntroduction() != null) {
            mIntroduction.setText(user.getIntroduction());
        }

    }

    @OnClick(R2.id.cl_avatar)
    void editAvatar() {
        openAlbum(Constant.REQUEST_CODE_AVATAR);
    }

    @OnClick(R2.id.cl_background)
    void editBackground() {
        openAlbum(Constant.REQUEST_CODE_BACKGROUND);
    }

    @OnClick(R2.id.cl_username)
    void editUsername() {
        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_edit_username,
                null, false, false, true, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        AppCompatEditText mEdit = customerView.findViewById(R.id.et_edit);
        AppCompatTextView mLen = customerView.findViewById(R.id.tv_len);
        if (user.getUsername() != null) {
            mEdit.setText(user.getUsername());
            mEdit.setSelection(user.getUsername().length());
        }
        mLen.setText(String.format(Locale.CHINA, "%d/18", Objects.requireNonNull(mEdit.getText()).length()));
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLen.setText(String.format(Locale.CHINA, "%d/18", i2 + i));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        customerView.findViewById(R.id.btn_ok).setOnClickListener(v -> RestClient.builder()
                .url(Api.EDIT_USERNAME)
                .params("uid", uid)
                .params("username", Objects.requireNonNull(mEdit.getText()).toString().trim())
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        UserInfo userInfo = JMessageClient.getMyInfo();
                        userInfo.setNickname(mEdit.getText().toString().trim());
                        JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {

                            }
                        });
                        mUsername.setText(mEdit.getText().toString().trim());
                        user.setUsername(mEdit.getText().toString().trim());
                        userBox.put(user);
                        dialog.dismiss();
                    } else {
                        Toasty.error(EditActivity.this, "修改失败 ", Toasty.LENGTH_SHORT).show();
                    }
                })
                .error((code, msg) -> Toasty.error(EditActivity.this, "修改失败 " + code, Toasty.LENGTH_SHORT).show())
                .build()
                .post());

        customerView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        new Handler().postDelayed(() -> KeyBoardUtil.showKeyboard(mEdit), 10);
    }

    @OnClick(R2.id.cl_gender)
    void editGender() {
        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_select_gender,
                null, false, false, true, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customView = DialogCustomViewExtKt.getCustomView(dialog);
        RadioGroup radioGroup = customView.findViewById(R.id.radio_group);
        if (user.getGender() != null && user.getGender().equals("男")) {
            radioGroup.check(R.id.male);
        } else if (user.getGender() != null && user.getGender().equals("女")) {
            radioGroup.check(R.id.female);
        }

        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            RadioButton button = customView.findViewById(i);
            RestClient.builder()
                    .url(Api.EDIT_GENDER)
                    .params("uid", uid)
                    .params("gender", button.getText().toString())
                    .success(response -> {
                        String result = JSON.parseObject(response).getString("result");
                        if (TextUtils.equals(result, "ok")) {
                            user.setGender(button.getText().toString());
                            mGender.setText(button.getText().toString());
                            userBox.put(user);
                            dialog.dismiss();
                        }
                    })
                    .error((code, msg) -> Toasty.error(this, "未知错误", Toasty.LENGTH_SHORT).show())
                    .build()
                    .post();
        });
    }

    @OnClick(R2.id.cl_birthday)
    void editBirthday() {
        Calendar now = Calendar.getInstance();
        now.set(1999, 1, 1);
        Calendar min = Calendar.getInstance();
        min.set(1990, 1, 1);
        Calendar max = Calendar.getInstance();

        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_date_picker,
                null, false, false, false, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customView = DialogCustomViewExtKt.getCustomView(dialog);
        DatePicker datePicker = customView.findViewById(R.id.date_picker);
        if (user.getBirthday() != 0) {
            Date date = new Date(user.getBirthday());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            datePicker.setDate(calendar, true);
        } else {
            datePicker.setDate(now, true);
        }
        datePicker.setMaxDate(max);
        datePicker.setMinDate(min);
        customView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        customView.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            Calendar newDate = datePicker.getDate();
            if (newDate != null) {
                String date = DateUtil.date2String(newDate.getTime(), "YYYY-MM-dd");
                RestClient.builder()
                        .url(Api.EDIT_BIRTHDAY)
                        .params("uid", uid)
                        .params("birthday", date)
                        .success(response -> {
                            String result = JSON.parseObject(response).getString("result");
                            if (TextUtils.equals(result, "ok")) {
                                mBirthday.setText(date);
                                mConstellation.setText(DateUtil.getConstellation(newDate.getTimeInMillis()));
                                user.setBirthday(newDate.getTimeInMillis());
                                userBox.put(user);
                                dialog.dismiss();
                            } else {
                                Toasty.error(this, "未知错误", Toasty.LENGTH_SHORT).show();
                            }
                        })
                        .error((code, msg) -> Toasty.error(this, "未知错误" + code, Toasty.LENGTH_SHORT).show())
                        .build()
                        .post();
            }
        });

    }

    @OnClick(R2.id.cl_school)
    void editSchool() {
        if (user.getSchool() != null) {
            return;
        }

        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet());
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_str_list,
                null, false, false, true, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        Box<School> schoolBox = ObjectBox.get().boxFor(School.class);
        StringAdapter mAdapter = new StringAdapter
                (R.layout.item_string_list, Arrays.asList(schoolBox.query().build().property(School_.name).findStrings()));
        if (schoolBox.count() == 0) {
            Toasty.info(EditActivity.this, "正在加载学校列表...", Toasty.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    JsonParseUtil.parseSchoolList(FileUtil.getRawFile(R.raw.china_university_list));
                    runOnUiThread(() -> mAdapter.setNewData(Arrays.asList(schoolBox.query().build().property(School_.name).findStrings())));
                }
            }.start();
        }

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        RecyclerView mRecyclerView = customerView.findViewById(R.id.rv_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(customerView.getContext()));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            String school = (String) adapter.getItem(position);
            if (school != null) {
                RestClient.builder()
                        .url(Api.EDIT_SCHOOL)
                        .params("uid", uid)
                        .params("school", school)
                        .success(response -> {
                            String result = JSON.parseObject(response).getString("result");
                            if (TextUtils.equals(result, "ok")) {
                                mSchool.setText(school);
                                user.setSchool(school);
                                userBox.put(user);
                                dialog.dismiss();
                            } else {
                                Toasty.error(this, "未知错误", Toasty.LENGTH_SHORT).show();
                            }
                        })
                        .error((code, msg) -> Toasty.error(this, "未知错误" + code, Toasty.LENGTH_SHORT).show())
                        .build()
                        .post();
            }
        });

        AppCompatEditText mSearch = customerView.findViewById(R.id.et_search);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String[] schools = schoolBox.query().contains(School_.name, charSequence.toString()).build().property(School_.name).findStrings();
                Set<String> set = new ArraySet<>();
                set.addAll(Arrays.asList(schools));
                mAdapter.setNewData(new ArrayList<>(set));

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @OnClick(R2.id.cl_sid)
    void editSid() {
        if (user.getStudentId() != 0) {
            return;
        }
        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_edit_sid,
                null, false, false, true, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        AppCompatEditText mEdit = customerView.findViewById(R.id.et_edit);

        customerView.findViewById(R.id.btn_ok).setOnClickListener(v -> RestClient.builder()
                .url(Api.EDIT_STUDENT_ID)
                .params("uid", uid)
                .params("studentId", Objects.requireNonNull(mEdit.getText()).toString().trim())
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        mSid.setText(mEdit.getText().toString().trim());
                        user.setStudentId(Long.parseLong(mEdit.getText().toString().trim()));
                        userBox.put(user);
                        dialog.dismiss();
                    } else {
                        Toasty.error(EditActivity.this, "添加失败 ", Toasty.LENGTH_SHORT).show();
                    }
                })
                .error((code, msg) -> Toasty.error(EditActivity.this, "添加失败 " + code, Toasty.LENGTH_SHORT).show())
                .build()
                .post());

        customerView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        new Handler().postDelayed(() -> KeyBoardUtil.showKeyboard(mEdit), 10);
    }

    @OnClick(R2.id.cl_college)
    void editCollege() {
        if (user.getCollege() != null) {
            return;
        }

        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet());
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_str_list,
                null, false, false, true, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        Box<Major> majorBox = ObjectBox.get().boxFor(Major.class);
        StringAdapter mAdapter = new StringAdapter
                (R.layout.item_string_list, Arrays.asList(majorBox.query().build().property(Major_.name).findStrings()));
        if (majorBox.count() == 0) {
            Toasty.info(EditActivity.this, "正在加载专业列表...", Toasty.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    JsonParseUtil.parseMajorList(FileUtil.getRawFile(R.raw.major_list));
                    runOnUiThread(() -> mAdapter.setNewData(Arrays.asList(majorBox.query().build().property(Major_.name).findStrings())));
                }
            }.start();
        }

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        RecyclerView mRecyclerView = customerView.findViewById(R.id.rv_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(customerView.getContext()));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            String major = (String) adapter.getItem(position);
            if (major != null) {
                RestClient.builder()
                        .url(Api.EDIT_COLLEGE)
                        .params("uid", uid)
                        .params("college", major)
                        .success(response -> {
                            String result = JSON.parseObject(response).getString("result");
                            if (TextUtils.equals(result, "ok")) {
                                mCollege.setText(major);
                                user.setCollege(major);
                                userBox.put(user);
                                dialog.dismiss();
                            } else {
                                Toasty.error(this, "未知错误", Toasty.LENGTH_SHORT).show();
                            }
                        })
                        .error((code, msg) -> Toasty.error(this, "未知错误" + code, Toasty.LENGTH_SHORT).show())
                        .build()
                        .post();
            }
        });

        AppCompatEditText mSearch = customerView.findViewById(R.id.et_search);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String[] majors = majorBox.query().contains(Major_.name, charSequence.toString()).build().property(Major_.name).findStrings();
                Set<String> set = new ArraySet<>();
                set.addAll(Arrays.asList(majors));
                mAdapter.setNewData(new ArrayList<>(set));

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @OnClick(R2.id.ll_introduction)
    void editIntroduction() {
        MaterialDialog dialog = new MaterialDialog(this, new BottomSheet(LayoutMode.WRAP_CONTENT));
        DialogCustomViewExtKt.customView(dialog, R.layout.dialog_edit_introduction,
                null, false, false, true, true);
        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(8f, null);
        dialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(dialog);
        AppCompatEditText mEdit = customerView.findViewById(R.id.et_edit);
        AppCompatTextView mLen = customerView.findViewById(R.id.tv_len);
        if (user.getIntroduction() != null) {
            mEdit.setText(user.getIntroduction());
            mEdit.setSelection(user.getIntroduction().length());
        }
        mLen.setText(String.format(Locale.CHINA, "%d/30", Objects.requireNonNull(mEdit.getText()).length()));
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLen.setText(String.format(Locale.CHINA, "%d/30", i2 + i));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        customerView.findViewById(R.id.btn_ok).setOnClickListener(v -> RestClient.builder()
                .url(Api.EDIT_INTRODUCTION)
                .params("uid", uid)
                .params("introduction", Objects.requireNonNull(mEdit.getText()).toString().trim())
                .success(response -> {
                    String result = JSON.parseObject(response).getString("result");
                    if (TextUtils.equals(result, "ok")) {
                        mIntroduction.setText(mEdit.getText().toString().trim());
                        user.setIntroduction(mEdit.getText().toString().trim());
                        userBox.put(user);
                        dialog.dismiss();
                    } else {
                        Toasty.error(EditActivity.this, "修改失败 ", Toasty.LENGTH_SHORT).show();
                    }
                })
                .error((code, msg) -> Toasty.error(EditActivity.this, "修改失败 " + code, Toasty.LENGTH_SHORT).show())
                .build()
                .post());

        customerView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        new Handler().postDelayed(() -> KeyBoardUtil.showKeyboard(mEdit), 10);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        if (requestCode == Constant.REQUEST_CODE_BACKGROUND) {
            cropPhoto(Matisse.obtainResult(data).get(0), DimenUtil.getScreenWidthByDp(), 360, Constant.CROP_REQUEST_CODE_BACKGROUND);
        } else if (requestCode == Constant.REQUEST_CODE_AVATAR) {
            cropPhoto(Matisse.obtainResult(data).get(0), 1, 1, Constant.CROP_REQUEST_CODE_AVATAR);
        } else if (requestCode == Constant.CROP_REQUEST_CODE_BACKGROUND) {
            final Uri uri = UCrop.getOutput(data);
            if (uri != null) {
                String path = uri.getPath();
                if (path != null) {
                    UploadUtil util = UploadUtil.INSTANCE();
                    List<File> files = new ArrayList<>();
                    files.add(new File(path));
                    util.uploadFile(this, files, new IUploadFileListener() {
                        @Override
                        public void onSuccess(List<String> paths) {
                            if (paths == null || paths.size() != 1) {
                                Toasty.error(EditActivity.this, "上传失败", Toasty.LENGTH_SHORT).show();
                                return;
                            }
                            RestClient.builder()
                                    .url(Api.EDIT_BACKGROUND)
                                    .params("uid", uid)
                                    .params("background", paths.get(0))
                                    .success(response -> {
                                        String result = JSON.parseObject(response).getString("result");
                                        if (TextUtils.equals(result, "ok")) {
                                            user.setBackground(paths.get(0));
                                            userBox.put(user);
                                            mBackground.setImageURI(uri);
                                        }
                                    })
                                    .error((code, msg) ->
                                            Toasty.error(EditActivity.this, "上传失败 " + code, Toasty.LENGTH_SHORT).show())
                                    .build()
                                    .post();
                        }

                        @Override
                        public void onError() {
                            Toasty.error(EditActivity.this, "上传失败", Toasty.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        } else if (requestCode == Constant.CROP_REQUEST_CODE_AVATAR) {
            final Uri uri = UCrop.getOutput(data);
            if (uri != null) {
                String path = uri.getPath();
                if (path != null) {
                    UploadUtil util = UploadUtil.INSTANCE();
                    List<File> files = new ArrayList<>();
                    files.add(new File(path));
                    util.uploadFile(this, files, new IUploadFileListener() {
                        @Override
                        public void onSuccess(List<String> paths) {
                            if (paths == null || paths.size() != 1) {
                                Toasty.error(EditActivity.this, "上传失败", Toasty.LENGTH_SHORT).show();
                                return;
                            }
                            RestClient.builder()
                                    .url(Api.EDIT_AVATAR)
                                    .params("uid", uid)
                                    .params("avatar", paths.get(0))
                                    .success(response -> {
                                        String result = JSON.parseObject(response).getString("result");
                                        if (TextUtils.equals(result, "ok")) {
                                            user.setAvatar(paths.get(0));
                                            userBox.put(user);
                                            JMessageClient.updateUserAvatar(new File(path), new BasicCallback() {
                                                @Override
                                                public void gotResult(int i, String s) {
                                                    FairLogger.d(i + "   " + s);
                                                }
                                            });
                                            Glide.with(EditActivity.this).load(uri).into(mAvatar);
                                        }
                                    })
                                    .error((code, msg) ->
                                            Toasty.error(EditActivity.this, "上传失败 " + code, Toasty.LENGTH_SHORT).show())
                                    .build()
                                    .post();
                        }

                        @Override
                        public void onError() {
                            Toasty.error(EditActivity.this, "上传失败", Toasty.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }
    }

    private void openAlbum(int requestCode) {
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
                    .forResult(requestCode);
        } else {
            EasyPermissions.requestPermissions(this, "打开图库需要存储读取权限", 1001,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    /**
     * 裁剪图片
     *
     * @param uri         图片uri
     * @param x           比例
     * @param y           比例
     * @param requestCode 请求码
     */
    private void cropPhoto(Uri uri, float x, float y, int requestCode) {
        Uri destinationUri = Uri.fromFile(new File(getExternalCacheDir(),
                FileUtil.getFileNameByTime("FairSchool", FileUtil.getExtension(uri.getPath()))));
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getColor(R.color.colorAccent)); // 设置标题栏颜色
        options.setStatusBarColor(getColor(R.color.colorAccent)); //设置状态栏颜色
        options.setToolbarWidgetColor(getColor(android.R.color.white));
        UCrop.of(uri, destinationUri)
                .withAspectRatio(x, y)
                .withMaxResultSize(1080, 1080)
                .withOptions(options)
                .start(this, requestCode);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(501);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(501);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        setResult(501);
        finish();
    }
}
