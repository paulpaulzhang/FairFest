package cn.paulpaulzhang.fair.sc.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.chat.fixtures.Transform;
import cn.paulpaulzhang.fair.sc.main.chat.model.Dialog;
import cn.paulpaulzhang.fair.sc.main.chat.model.Message;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import es.dmoral.toasty.Toasty;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.chat
 * 创建时间: 8/3/2019
 * 创建人: zlm31
 * 描述:
 */
public class MessageActivity extends FairActivity implements
        MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageInput.TypingListener,
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener {

    @BindView(R2.id.tb_message)
    MaterialToolbar mToolbar;

    @BindView(R2.id.message_list)
    MessagesList mMessageList;

    @BindView(R2.id.message_input)
    MessageInput mMessageInput;

    private ImageLoader mImageLoader;
    private MessagesListAdapter<Message> mMessageListAdapter;

    private Conversation conversation;
    private BackgroundHandler mHandler;
    private String cid;
    private String uid;
    private String appkey;

    @Override
    public int setLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        cid = intent.getStringExtra("cid");
        appkey = intent.getStringExtra("appkey");
        JMessageClient.registerEventReceiver(this);
        JMessageClient.enterSingleConversation(cid);
        mHandler = new BackgroundHandler(this);
        initToolbar(mToolbar);
        conversation = JMessageClient.getSingleConversation(cid, appkey);
        mToolbar.setTitle(conversation.getTitle());
        conversation.setUnReadMessageCnt(0);
        mImageLoader = (imageView, url, payload) -> Glide.with(this).load(url).placeholder(R.drawable.default_placeholder).into(imageView);
        initAdapter(uid);
        mMessageInput.setAttachmentsListener(this);
        mMessageInput.setInputListener(this);
        mMessageInput.setTypingListener(this);
    }

    private void initAdapter(String uid) {
        mMessageListAdapter = new MessagesListAdapter<>(uid, mImageLoader);
        mMessageListAdapter.enableSelectionMode(this);
        mMessageListAdapter.setLoadMoreListener(this);
        mMessageListAdapter.registerViewClickListener(R.id.messageUserAvatar, (view, message) -> {
            Toasty.info(MessageActivity.this, message.getUser().getName(), Toasty.LENGTH_SHORT).show();
        });
        mMessageList.setAdapter(mMessageListAdapter);
        mMessageListAdapter.onLoadMore(10, 0);
    }

    @Override
    public void onAddAttachments() {

    }

    @Override
    public boolean onSubmit(CharSequence input) {
        Message message = Transform.getMessage(JMessageClient.createSingleTextMessage(cid, appkey, input.toString()));
        if (message != null) {
            mMessageListAdapter.addToStart(message, true);
        }
        return true;
    }

    @Override
    public void onStartTyping() {

    }

    @Override
    public void onStopTyping() {

    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        List<cn.jpush.im.android.api.model.Message> messages =
                conversation.getMessagesFromNewest(totalItemsCount, totalItemsCount + page);
        ArrayList<Message> messageArrayList = new ArrayList<>();
        for (cn.jpush.im.android.api.model.Message msg : messages) {
            messageArrayList.add(Transform.getMessage(msg));
        }

        FairLogger.d("page:" + page + " totalItemsCount:" + totalItemsCount + " messages:" + messages.size());

        mMessageListAdapter.addToEnd(messageArrayList, false);
    }

    @Override
    public void onSelectionChanged(int count) {

    }

    public void onEvent(MessageEvent event) {
        cn.jpush.im.android.api.model.Message message = event.getMessage();
        mHandler.sendMessage(mHandler.obtainMessage(Constant.NEW_MESSAGE, message));
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
        JMessageClient.exitConversation();
    }

    private static class BackgroundHandler extends Handler {
        private final WeakReference<MessageActivity> messageActivity;

        BackgroundHandler(MessageActivity messageActivity) {
            this.messageActivity = new WeakReference<>(messageActivity);
        }

        @Override
        public void handleMessage(@NotNull android.os.Message msg) {
            super.handleMessage(msg);
            MessageActivity activity = messageActivity.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case Constant.NEW_MESSAGE:
                    cn.jpush.im.android.api.model.Message message = (cn.jpush.im.android.api.model.Message) msg.obj;
                    Message message1 = Transform.getMessage(message);
                    assert message1 != null;
                    activity.mMessageListAdapter.addToStart(message1, true);
                    break;
                default:
            }
        }
    }
}
