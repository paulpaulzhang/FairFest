package cn.paulpaulzhang.fair.sc.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.paulpaulzhang.fair.app.AccountManager;
import cn.paulpaulzhang.fair.app.IUserChecker;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.User;
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
        final JSONObject object = JSON.parseObject(response).getJSONObject("data");
        final long id = object.getLong("id");
        final long phone = object.getLong("phone");
        final String username = object.getString("username");

        final User user = new User(id, phone, username);
        Box<User> userBox = ObjectBox.get().boxFor(User.class);
        userBox.put(user);

        AccountManager.setSignState(true);
        signUpListener.onSignUpSuccess();
    }

    public static void onSignIn(String response, ISignInListener signInListener) {
        //TODO 需要与后端协商后修改
        final JSONObject object = JSON.parseObject(response).getJSONObject("data");
        final long id = object.getLong("id");
        final long phone = object.getLong("phone");
        final String username = object.getString("username");

        final User user = new User(id, phone, username);
        Box<User> userBox = ObjectBox.get().boxFor(User.class);
        userBox.put(user);

        AccountManager.setSignState(true);
        signInListener.onSignInSuccess();
    }
}
