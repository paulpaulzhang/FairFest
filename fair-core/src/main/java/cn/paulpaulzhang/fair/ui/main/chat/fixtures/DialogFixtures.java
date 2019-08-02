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
        if (conversations == null || conversations.size() == 0) {
            return new ArrayList<>();
        }
        ArrayList<Dialog> dialogs = new ArrayList<>();
        ArrayList<User> users;

        for (Conversation conversation : conversations) {
            users = new ArrayList<>();
            users.add(Transform.getUser(JMessageClient.getMyInfo()));
            users.add(Transform.getUser((UserInfo) conversation.getTargetInfo()));
            Message message = Transform.getMessage(conversation);
            dialogs.add(Transform.getDialog(conversation, users, message));
        }
        return dialogs;
    }
}
