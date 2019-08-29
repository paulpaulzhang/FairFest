package cn.paulpaulzhang.fair.sc.main.chat.fixtures;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.main.chat.model.Dialog;
import cn.paulpaulzhang.fair.sc.main.chat.model.Message;
import cn.paulpaulzhang.fair.sc.main.chat.model.User;
import cn.paulpaulzhang.fair.util.log.FairLogger;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.chat.fixtures
 * 创建时间: 8/1/2019
 * 创建人: zlm31
 * 描述:
 */
public class Transform {
    public static Message getLatestMessage(Conversation conversation) {

        cn.jpush.im.android.api.model.Message message = conversation.getLatestMessage();
        if (message == null) {
            return null;
        }
        switch (message.getContentType()) {
            case text:
                TextContent textContent = (TextContent) message.getContent();
                return new Message(message.getId() + "",
                        Transform.getUser(message.getFromUser()),
                        textContent.getText(),
                        new Date(message.getCreateTime()), message);
            case image:
                return new Message(message.getId() + "",
                        Transform.getUser(message.getFromUser()),
                        "[图片]",
                        new Date(message.getCreateTime()), message);
            default:
        }
        return null;
    }

    public static Message getMessage(cn.jpush.im.android.api.model.Message message) {
        switch (message.getContentType()) {
            case text:
                TextContent textContent = (TextContent) message.getContent();
                return new Message(message.getId() + "",
                        Transform.getUser(message.getFromUser()),
                        textContent.getText(),
                        new Date(message.getCreateTime()), message);
            case image:
                ImageContent imageContent = (ImageContent) message.getContent();
                Message msg = new Message(message.getId() + "",
                        Transform.getUser(message.getFromUser()),
                        null,
                        new Date(message.getCreateTime()), message);
                msg.setImage(new Message.Image(imageContent.getLocalThumbnailPath()));

                return msg;
            default:
        }
        return null;
    }

    public static User getUser(UserInfo userInfo) {
        File avatar = userInfo.getAvatarFile();
        return new User(userInfo.getUserName(),
                userInfo.getNickname(),
                avatar != null ? avatar.getPath() : Constant.DEFAULT_AVATAR,
                true, userInfo);
    }

    public static Dialog getDialog(Conversation conversation) {
        File avatar = conversation.getAvatarFile();
        ArrayList<User> users = new ArrayList<>();
        users.add(Transform.getUser((UserInfo) conversation.getTargetInfo()));
        Message message = getLatestMessage(conversation);
        return new Dialog(((UserInfo) conversation.getTargetInfo()).getUserName(),
                conversation.getTitle(),
                avatar != null ? avatar.getPath() : Constant.DEFAULT_AVATAR,
                users,
                message,
                conversation.getUnReadMsgCnt(), conversation);
    }

}
