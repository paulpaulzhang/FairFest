package cn.paulpaulzhang.fair.ui.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.paulpaulzhang.fair.base.app.AccountManager;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.database.ObjectBox;
import cn.paulpaulzhang.fair.database.entity.LocalUser;
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
        signUpListener.onSignUpSuccess();
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
        signInListener.onSignInSuccess();
    }
}
