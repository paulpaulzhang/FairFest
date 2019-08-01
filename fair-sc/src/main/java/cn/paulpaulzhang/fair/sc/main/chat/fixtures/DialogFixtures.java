package cn.paulpaulzhang.fair.sc.main.chat.fixtures;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.paulpaulzhang.fair.sc.main.chat.model.Dialog;
import cn.paulpaulzhang.fair.sc.main.chat.model.Message;
import cn.paulpaulzhang.fair.sc.main.chat.model.User;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.chat.fixtures
 * 创建时间: 8/1/2019
 * 创建人: zlm31
 * 描述:
 */
public class DialogFixtures {
    public static List<Dialog> getDialogs() {
        List<Conversation> conversations = JMessageClient.getConversationList();
        ArrayList<Dialog> dialogs = new ArrayList<>();
        ArrayList<User> users;
        UserInfo userInfo;
        File avatar;
        for (Conversation conversation : conversations) {
            users = new ArrayList<>();
            userInfo = JMessageClient.getMyInfo();
            avatar = userInfo.getAvatarFile();
            User mine = new User(userInfo.getUserID() + "", userInfo.getNickname(), avatar != null ? avatar.getPath() : "", true);
            users.add(mine);
            userInfo = (UserInfo) conversation.getTargetInfo();
            avatar = userInfo.getAvatarFile();
            User user = new User(userInfo.getUserID() + "", userInfo.getNickname(), avatar != null ? avatar.getPath() : "", true);
            users.add(user);
            TextContent content = (TextContent) conversation.getLatestMessage().getContent();
            Message message = new Message(conversation.getLatestMessage().getId() + "", user, content.getText(), new Date(conversation.getLatestMessage().getCreateTime()));
            avatar = conversation.getAvatarFile();
            dialogs.add(new Dialog(conversation.getId() + "",
                    conversation.getTitle(),
                    avatar != null ? avatar.getPath() : "",
                    users,
                    message,
                    conversation.getUnReadMsgCnt()));
        }
        return dialogs;
    }
}
