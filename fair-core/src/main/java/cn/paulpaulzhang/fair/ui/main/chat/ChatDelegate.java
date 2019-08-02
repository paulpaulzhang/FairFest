package cn.paulpaulzhang.fair.ui.main.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.DeviceInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.HomeActivity;
import cn.paulpaulzhang.fair.sc.main.chat.fixtures.DialogFixtures;
import cn.paulpaulzhang.fair.sc.main.chat.model.Dialog;
import cn.paulpaulzhang.fair.sc.main.chat.model.User;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;


/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/8/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class ChatDelegate extends FairDelegate
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    @BindView(R2.id.dialog_list)
    DialogsList mDialogList;

    private ImageLoader mImageLoader;
    private DialogsListAdapter<Dialog> mDialogListAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_message;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        JMessageClient.registerEventReceiver(this);
        login();
        mImageLoader = (imageView, url, payload) -> Glide.with(this).load(url).placeholder(R.mipmap.ic_launcher_round).into(imageView);
        initAdapter();
    }

    private void login() {
        JMessageClient.login("123456", "admin", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                FairLogger.d("登陆成功");
            }
        });
    }

    private void initAdapter() {
        mDialogListAdapter = new DialogsListAdapter<>(mImageLoader);
        mDialogListAdapter.setItems(DialogFixtures.getDialogs());
        mDialogListAdapter.setOnDialogClickListener(this);
        mDialogListAdapter.setOnDialogLongClickListener(this);
        mDialogList.setAdapter(mDialogListAdapter);
    }

    @Override
    public void onDialogClick(Dialog dialog) {

    }

    @Override
    public void onDialogLongClick(Dialog dialog) {

    }

    public void onEvent(MessageEvent event) {
        Message message = event.getMessage();
        switch (message.getContentType()) {
            case text:

                break;
            case image:

                break;
        }
    }

    public void onEvent(NotificationClickEvent event) {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        Objects.requireNonNull(getContext()).startActivity(intent);
    }

    public void onEvent(OfflineMessageEvent event) {
        List<Message> msgs = event.getOfflineMessageList();
        Conversation conversation = event.getConversation();
        for (Message msg : msgs) {
            //...
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }
}
