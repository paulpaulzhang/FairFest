package cn.paulpaulzhang.fair.ui.sign;

/**
 * 包名：cn.paulpaulzhang.fair.sc.sign
 * 创建时间：7/3/19
 * 创建人： paulpaulzhang
 * 描述：登陆接口
 */
public interface ISignInListener {

    void onSignInSuccess();

    void onSignUpFailure(String msg);
}
