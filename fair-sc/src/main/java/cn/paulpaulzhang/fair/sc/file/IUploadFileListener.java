package cn.paulpaulzhang.fair.sc.file;

import java.util.List;

/**
 * 包名: cn.paulpaulzhang.fair.util.file
 * 创建时间: 7/31/2019
 * 创建人: zlm31
 * 描述:
 */
public interface IUploadFileListener {
    void onSuccess(List<String> paths);
    void onError();
}
