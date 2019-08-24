package cn.paulpaulzhang.fair.sc.main.user.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.app.AccountManager;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.sign.SignUpActivity;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user.activity
 * 创建时间: 8/17/2019
 * 创建人: zlm31
 * 描述:
 */
public class SettingActivity extends FairActivity {
    @BindView(R2.id.toolbar)
    MaterialToolbar mToolbar;

    @Override
    public int setLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initToolbar(mToolbar);

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();
    }

    @OnClick(R2.id.btn_logout)
    void logout() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("退出登录")
                .setMessage("点击确认将退出当前登录")
                .setPositiveButton("确认", (dialogInterface, i) -> {
                    Box<User> userBox = ObjectBox.get().boxFor(User.class);
                    userBox.remove(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
                    FairPreference.removeCustomAppProfile(UserConfigs.CURRENT_USER_ID.name());
                    AccountManager.setSignState(false);
                    dialogInterface.dismiss();
                    Intent intent = new Intent(SettingActivity.this, SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
