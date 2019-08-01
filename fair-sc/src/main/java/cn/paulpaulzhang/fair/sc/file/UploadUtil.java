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

    private boolean uploadFile(String filename, InputStream input) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            ftp.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 使用被动模式设为默认
        ftp.enterLocalPassiveMode();
        // 二进制文件支持
        try {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置缓存
        ftp.setBufferSize(1024);
        //设置编码格式，防止中文乱码
        ftp.setControlEncoding("UTF-8");
        //设置连接超时时间
        ftp.setConnectTimeout(10 * 1000);
        //设置数据传输超时时间
        ftp.setDataTimeout(10*1000);

        FairLogger.d(FTP_HOST + FTP_PORT + FTP_USERNAME + FTP_PASSWORD + FTP_PATH);

        try {
            int reply;
            ftp.connect(FTP_HOST, FTP_PORT);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(FTP_USERNAME, FTP_PASSWORD);//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);//上传上去的图片数据格式（）一定要写这玩意，不然在服务器就打不开了
            if (!ftp.changeWorkingDirectory(FTP_PATH)) {
                if (ftp.makeDirectory(FTP_PATH)) {
                    ftp.changeWorkingDirectory(FTP_PATH);
                }
            }
            //  ftp.changeWorkingDirectory(path);
            //设置成其他端口的时候要添加这句话
            //  ftp.enterLocalPassiveMode();
            ftp.storeFile(filename, input);
            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ignored) {
                }
            }
        }
        return success;
    }

    public void uploadFile(Activity activity, List<File> files, IUploadFileListener listener) {
        new Thread(() -> {
            Box<LocalUser> userBox = ObjectBox.get().boxFor(LocalUser.class);
            LocalUser user = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
            for (File file : files) {
                try {
                    FileInputStream in = new FileInputStream(file);
                    String extension = FileUtil.getExtension(file.getPath());
                    String nameByTime = FileUtil.getFileNameByTime(user.getUsername(), extension);
                    boolean flag = uploadFile(nameByTime, in);
                    if (!flag) {
                        activity.runOnUiThread(listener::onError);
                        return;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            activity.runOnUiThread(listener::onSuccess);
        }).start();
    }
}
