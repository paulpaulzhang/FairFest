package cn.paulpaulzhang.fair.activities;

import androidx.annotation.NonNull;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.activities
 * 文件名：   PermissionCheckerActivity
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/28 23:19
 * 描述：     权限activity
 */
public abstract class PermissionCheckerActivity extends ProxyActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
