package cn.paulpaulzhang.fair.ui.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.api.BasicCallback;
import cn.paulpaulzhang.fair.app.AccountManager;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.model.LocalUser;
import cn.paulpaulzhang.fair.ui.loader.FairLoader;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import io.objectbox.Box;

/**
 * 包名：cn.paulpaulzhang.fair.sc.sign
 * 创建时间：7/3/19
 * 创建人： paulpaulzhang
 * 描述： 登陆注册操作类
 */
public class SignHandler {
    public static void onSignUp(String response, ISignUpListener signUpListener) {

        FairLogger.d("Response", response);
        FairLoader.stopLoading();
//        final JSONObject object = JSON.parseObject(response).getJSONObject("user");
//        final long id = object.getLong("id");
//        final long phone = object.getLong("phone");
//        final String username = object.getString("username");

//        final LocalUser user = new LocalUser(id, phone, username);
//        Box<LocalUser> userBox = ObjectBox.get().boxFor(LocalUser.class);
//        userBox.put(user);

//        JMessageClient.register(String.valueOf(id), "admin", null, new BasicCallback() {
//            @Override
//            public void gotResult(int i, String s) {
//                if (i == 0) {
//                    FairPreference.addCustomAppProfile(UserConfigs.CURRENT_USER_ID.name(), id);
//                    AccountManager.setSignState(true);
//                    JMessageClient.login(String.valueOf(id), "admin", new BasicCallback() {
//                        @Override
//                        public void gotResult(int i, String s) {
//                            if (i == 0) {
//                                FairLogger.d("JMessage", "JMessage登陆成功");
//                            }
//                        }
//                    });
//                    signUpListener.onSignUpSuccess();
//                } else {
//                    userBox.removeAll();
//                    FairLoader.stopLoading();
//                    FairLogger.d(s);
//                }
//            }
//        });


    }

    public static void onSignIn(String response, ISignInListener signInListener) {
        //TODO 需要与后端协商后修改
        final JSONObject object = JSON.parseObject(response).getJSONObject("user");
        final long id = object.getLong("id");
        final long phone = object.getLong("phone");
        final String username = object.getString("username");

        final LocalUser user = new LocalUser(id, phone, username);
        Box<LocalUser> userBox = ObjectBox.get().boxFor(LocalUser.class);
        userBox.put(user);
        FairPreference.addCustomAppProfile(UserConfigs.CURRENT_USER_ID.name(), id);
        AccountManager.setSignState(true);
        JMessageClient.login(String.valueOf(id), "admin", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    FairLogger.d("JMessage登陆成功");
                }
            }
        });
        signInListener.onSignInSuccess();
    }
}
