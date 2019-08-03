package cn.paulpaulzhang.fair.sc.sign;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;
import cn.paulpaulzhang.fair.app.AccountManager;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.model.LocalUser;
import cn.paulpaulzhang.fair.ui.loader.FairLoader;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import es.dmoral.toasty.Toasty;
import io.objectbox.Box;

/**
 * 包名：cn.paulpaulzhang.fair.sc.sign
 * 创建时间：7/3/19
 * 创建人： paulpaulzhang
 * 描述： 登陆注册操作类
 */
public class SignHandler {
    public static void onSignUp(String response, ISignUpListener signUpListener) {

        final String result = JSON.parseObject(response).getString("result");
        if (!TextUtils.equals(result, "ok")) {
            signUpListener.onSignUpFailure(result);
            return;
        }
        final long id = JSON.parseObject(response).getLong("uid");
        final String phone = JSON.parseObject(response).getString("phone");

        final LocalUser user = new LocalUser(id, phone);
        Box<LocalUser> userBox = ObjectBox.get().boxFor(LocalUser.class);
        userBox.put(user);

        RegisterOptionalUserInfo optionalUserInfo = new RegisterOptionalUserInfo();
        optionalUserInfo.setNickname(phone);
        JMessageClient.register(String.valueOf(id), "admin", optionalUserInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    FairPreference.addCustomAppProfile(UserConfigs.CURRENT_USER_ID.name(), id);
                    AccountManager.setSignState(true);
                    JMessageClient.login(String.valueOf(id), "admin", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                FairLogger.d("JMessage", "JMessage登陆成功");
                            }
                        }
                    });
                    signUpListener.onSignUpSuccess();
                } else {
                    userBox.removeAll();
                    FairLoader.stopLoading();
                    FairLogger.d(s);
                }
            }
        });


    }

    public static void onSignIn(String response, ISignInListener signInListener) {

        final String result = JSON.parseObject(response).getString("result");
        if (!TextUtils.equals(result, "ok")) {
            signInListener.onSignUpFailure(result);
            return;
        }
        final long id = JSON.parseObject(response).getLong("uid");
        final String phone = JSON.parseObject(response).getString("phone");

        final LocalUser user = new LocalUser(id, phone);
        Box<LocalUser> userBox = ObjectBox.get().boxFor(LocalUser.class);
        userBox.put(user);

        FairPreference.addCustomAppProfile(UserConfigs.CURRENT_USER_ID.name(), id);
        AccountManager.setSignState(true);
        JMessageClient.login(String.valueOf(id), "admin", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    FairLogger.d("JMessage", "JMessage登陆成功");
                }
            }
        });
        signInListener.onSignInSuccess();
    }
}
