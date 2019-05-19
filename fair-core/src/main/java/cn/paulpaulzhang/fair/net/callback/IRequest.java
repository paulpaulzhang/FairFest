package cn.paulpaulzhang.fair.net.callback;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.net.callback
 * 文件名：   IRequest
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/19 12:51
 * 描述：     请求接口
 */
public interface IRequest {

    void onRequestStart();

    void onRequestEnd();
}
