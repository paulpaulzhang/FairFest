package cn.paulpaulzhang.fair.sc.file;

import android.app.Activity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.model.LocalUser;
import cn.paulpaulzhang.fair.util.file.FileUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import io.objectbox.Box;

import static cn.paulpaulzhang.fair.constant.Constant.FTP_HOST;
import static cn.paulpaulzhang.fair.constant.Constant.FTP_PASSWORD;
import static cn.paulpaulzhang.fair.constant.Constant.FTP_PATH;
import static cn.paulpaulzhang.fair.constant.Constant.FTP_PORT;
import static cn.paulpaulzhang.fair.constant.Constant.FTP_USERNAME;

/**
 * 包名: cn.paulpaulzhang.fair.util.file
 * 创建时间: 7/31/2019
 * 创建人: zlm31
 * 描述:
 */
public class UploadUtil {

    private static UploadUtil uploadUtil = new UploadUtil();

    public static UploadUtil INSTANCE() {
        return uploadUtil;
    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param filename 上传到FTP服务器上的文件名,是自己定义的名字，
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */

    private String uploadFile(String filename, InputStream input) {
        String path = null;
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(FTP_HOST, FTP_PORT);// 连接FTP服务器
            FairLogger.d("ftp", ftp.isConnected());
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(FTP_USERNAME, FTP_PASSWORD);//登录

            if (!ftp.changeWorkingDirectory(FTP_PATH)) {
                if (ftp.makeDirectory(FTP_PATH)) {
                    ftp.changeWorkingDirectory(FTP_PATH);
                }
            }
            ftp.setControlEncoding("UTF-8");
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.storeFile(filename, input);
            path = "http://" + FTP_HOST + "/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    input.close();
                    ftp.logout();
                    ftp.disconnect();
                } catch (IOException ignored) {
                }
            }
        }
        return path;
    }

    public void uploadFile(Activity activity, List<File> files, IUploadFileListener listener) {
        new Thread(() -> {
            List<String> paths = new ArrayList<>();
            Box<LocalUser> userBox = ObjectBox.get().boxFor(LocalUser.class);
            LocalUser user = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
            for (File file : files) {
                try {
                    FileInputStream in = new FileInputStream(file);
                    String extension = FileUtil.getExtension(file.getPath());
                    String nameByTime = FileUtil.getFileNameByTime(user.getUsername(), extension);
                    String path = uploadFile(nameByTime, in);
                    FairLogger.d("url", path);
                    if (path == null || path.isEmpty()) {
                        activity.runOnUiThread(listener::onError);
                        return;
                    } else {
                        paths.add(path);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            activity.runOnUiThread(() -> listener.onSuccess(paths));
        }).start();
    }
}
