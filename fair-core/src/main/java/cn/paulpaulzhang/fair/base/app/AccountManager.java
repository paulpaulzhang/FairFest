package cn.paulpaulzhang.fair.base.app;

import cn.paulpaulzhang.fair.util.storage.FairPreference;

/**
 * 包名：cn.paulpaulzhang.fair.app
 * 创建时间：7/3/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class AccountManager {
    private enum SignTag {
        SIGN_TAG
    }

    //保存用户登陆状态,登陆后调用
    public static void setSignState(boolean state) {
        FairPreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }

    private static boolean isSignIn() {
        return FairPreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    public static void checkAccount(IUserChecker checker) {
        if (isSignIn()) {
            checker.onSignIn();
        } else {
            checker.onNotSignIn();
        }
    }
}
