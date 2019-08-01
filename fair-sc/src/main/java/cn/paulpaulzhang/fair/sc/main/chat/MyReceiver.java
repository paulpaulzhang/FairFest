package cn.paulpaulzhang.fair.sc.main.chat;

import java.util.List;

import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.chat
 * 创建时间: 8/1/2019
 * 创建人: zlm31
 * 描述:
 */
public class MyReceiver {

    private static final MyReceiver RECEIVER = new MyReceiver();

    public static MyReceiver INSTANCE() {
        return RECEIVER;
    }

    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();
        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                textContent.getText();
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
        }
    }

    public void onEvent(OfflineMessageEvent event) {
        List<Message> msgs = event.getOfflineMessageList();
        for (Message msg:msgs) {
            //...
        }
    }
}
