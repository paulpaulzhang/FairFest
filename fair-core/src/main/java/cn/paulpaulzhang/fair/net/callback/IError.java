package cn.paulpaulzhang.fair.net.callback;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.net.callback
 * 文件名：   IError
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/19 12:49
 * 描述：     请求错误
 */
public interface IError {

    void onError(int code, String msg);
}
