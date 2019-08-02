package cn.paulpaulzhang.fair.sc.main.chat.fixtures;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
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
public class Transform {
    public static Message getMessage(Conversation conversation) {

        cn.jpush.im.android.api.model.Message message = conversation.getLatestMessage();
        switch (message.getContentType()) {
            case text:
                TextContent textContent = (TextContent) message.getContent();
                return new Message(message.getId() + "",
                        Transform.getUser((UserInfo) message.getTargetInfo()),
                        textContent.getText(),
                        new Date(message.getCreateTime()));
            case image:
                ImageContent imageContent = (ImageContent) message.getContent();
                return new Message(message.getId() + "",
                        Transform.getUser((UserInfo) message.getTargetInfo()),
                        imageContent.getLocalThumbnailPath(),
                        new Date(message.getCreateTime()));
            default:
        }
        return null;
    }

    public static User getUser(UserInfo userInfo) {
        File avatar = userInfo.getAvatarFile();
        return new User(userInfo.getUserName(),
                userInfo.getNickname(),
                avatar != null ? avatar.getPath() : "",
                true);
    }

    public static Dialog getDialog(Conversation conversation, ArrayList<User> users, Message message) {
        File avatar = conversation.getAvatarFile();
        return new Dialog(conversation.getId() + "",
                conversation.getTitle(),
                avatar != null ? avatar.getPath() : "",
                users,
                message,
                conversation.getUnReadMsgCnt());
    }

    public static cn.jpush.im.android.api.model.Message getJMessage(Message msg) {
        return null;
    }

    public static UserInfo getUserInfo(User user) {
        return null;
    }

    public static Conversation getConversation(Dialog dialog) {
        return null;
    }
}
