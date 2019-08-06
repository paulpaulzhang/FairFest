package cn.paulpaulzhang.fair.sc.main.chat.fixtures;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import cn.paulpaulzhang.fair.util.log.FairLogger;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.chat.fixtures
 * 创建时间: 8/1/2019
 * 创建人: zlm31
 * 描述:
 */
public class DialogFixtures {
    public static List<Dialog> getDialogs() {
        return getDialogs(null);
    }

    public static List<Dialog> getDialogs(SwipeRefreshLayout swipeRefreshLayout) {
        List<Conversation> conversations = JMessageClient.getConversationList();
        if (conversations == null || conversations.size() == 0) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
            }
            return new ArrayList<>();
        }
        ArrayList<Dialog> dialogs = new ArrayList<>();

        for (Conversation conversation : conversations) {
            dialogs.add(Transform.getDialog(conversation));
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        return dialogs;
    }
}
