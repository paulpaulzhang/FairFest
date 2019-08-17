package cn.paulpaulzhang.fair.sc.main.chat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.chat.fixtures.Transform;
import cn.paulpaulzhang.fair.sc.main.chat.holder.InCommingImageMessageHolder;
import cn.paulpaulzhang.fair.sc.main.chat.holder.InCommingTextMessageHolder;
import cn.paulpaulzhang.fair.sc.main.chat.holder.OutCommingImageMessageHolder;
import cn.paulpaulzhang.fair.sc.main.chat.holder.OutCommingTextMessageHolder;
import cn.paulpaulzhang.fair.sc.main.chat.model.Message;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.image.Glide4Engine;
import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.EasyPermissions;

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
        MessagesListAdapter.OnMessageLongClickListener<Message>,
        MessagesListAdapter.OnMessageClickListener<Message>,
        MessagesListAdapter.OnLoadMoreListener,
        DateFormatter.Formatter {

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
    private String username;
    private String appkey;

    @Override
    public int setLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");  //发送方 id
        username = intent.getStringExtra("username"); //接收方 id
        appkey = intent.getStringExtra("appkey");

        JMessageClient.registerEventReceiver(this);
        JMessageClient.enterSingleConversation(username);

        mHandler = new BackgroundHandler(this);

        conversation = JMessageClient.getSingleConversation(username, appkey);
        conversation.setUnReadMessageCnt(0);

        initToolbar(mToolbar, conversation.getTitle());

        mImageLoader = (imageView, url, payload) -> Glide.with(this).load(url).placeholder(R.drawable.default_placeholder).into(imageView);
        initAdapter(uid);
        mMessageInput.setAttachmentsListener(this);
        mMessageInput.setInputListener(this);
        mMessageInput.setTypingListener(this);
    }

    private void initAdapter(String uid) {
        MessageHolders holderConfigs = new MessageHolders()
                .setIncomingImageConfig(InCommingImageMessageHolder.class, R.layout.item_custom_incoming_image_message)
                .setIncomingTextConfig(InCommingTextMessageHolder.class, R.layout.item_custom_incoming_text_message)
                .setOutcomingImageConfig(OutCommingImageMessageHolder.class, R.layout.item_custom_outcoming_image_message)
                .setOutcomingTextConfig(OutCommingTextMessageHolder.class, R.layout.item_custom_outcoming_text_message);
        mMessageListAdapter = new MessagesListAdapter<>(uid, holderConfigs, mImageLoader);
        mMessageListAdapter.setOnMessageLongClickListener(this);
        mMessageListAdapter.setOnMessageClickListener(this);
        mMessageListAdapter.setLoadMoreListener(this);
        mMessageListAdapter.registerViewClickListener(R.id.messageUserAvatar, (view, message) -> {

            Toasty.info(this, message.getUser().getName(), Toasty.LENGTH_SHORT).show();
        });

        mMessageListAdapter.setDateHeadersFormatter(this);
        mMessageList.setAdapter(mMessageListAdapter);
        mMessageListAdapter.onLoadMore(10, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            List<String> paths = Matisse.obtainPathResult(data);
            try {
                Message message = Transform.getMessage(JMessageClient.createSingleImageMessage(username, appkey, new File(paths.get(0))));

                assert message != null;
                mMessageListAdapter.addToStart(message, true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toasty.error(MessageActivity.this, "图片不存在", Toasty.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAddAttachments() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .maxSelectable(1)
                    .countable(true)
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true, "cn.paulpaulzhang.fairfest.fileprovider"))
                    .gridExpectedSize(getResources()
                            .getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
                    .theme(R.style.Matisse_Zhihu_Custom)
                    .imageEngine(new Glide4Engine())
                    .forResult(Constant.REQUEST_CODE_CHOOSE);
        } else {
            EasyPermissions.requestPermissions(this, "打开图库需要存储读取权限", 1001,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        Message message = Transform.getMessage(JMessageClient.createSingleTextMessage(username, appkey, input.toString()));

        assert message != null;
        mMessageListAdapter.addToStart(message, true);

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

        mMessageListAdapter.addToEnd(messageArrayList, false);
    }

    @Override
    public void onMessageClick(Message message) {
        if (message.getImageUrl() != null) {
            Intent intent = new Intent(this, PhotoActivity.class);
            UserInfo userInfo = (UserInfo) message.getMessage().getTargetInfo();
            String username = userInfo.getUserName();
            String appkey = userInfo.getAppKey();
            int mid = message.getMessage().getId();
            intent.putExtra("username", username);
            intent.putExtra("appkey", appkey);
            intent.putExtra("mid", mid);
            startActivity(intent);
        }
    }

    @Override
    public void onMessageLongClick(Message message) {
        MaterialDialog dialog = new MaterialDialog(this, MaterialDialog.getDEFAULT_BEHAVIOR());

        if (message.getImageUrl() == null) {
            DialogCustomViewExtKt.customView(dialog, R.layout.view_custom_select_dialog,
                    null, false, true, false, true);
            View customerView = DialogCustomViewExtKt.getCustomView(dialog);
            customerView.findViewById(R.id.tv_copy).setOnClickListener(v -> {
                ClipData clipData = ClipData.newPlainText("", message.getText());
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                assert clipboardManager != null;
                clipboardManager.setPrimaryClip(clipData);
                Toasty.success(MessageActivity.this, "内容已复制", Toasty.LENGTH_SHORT).show();
                dialog.dismiss();
            });
            customerView.findViewById(R.id.tv_delete).setOnClickListener(v -> {
                conversation.deleteMessage(Integer.parseInt(message.getId()));
                mMessageListAdapter.delete(message);
                dialog.dismiss();
            });
        } else {
            DialogCustomViewExtKt.customView(dialog, R.layout.view_custom_delete_dialog,
                    null, false, true, false, true);
            View customerView = DialogCustomViewExtKt.getCustomView(dialog);
            customerView.findViewById(R.id.tv_delete).setOnClickListener(v -> {
                conversation.deleteMessage(Integer.parseInt(message.getId()));
                mMessageListAdapter.delete(message);
                dialog.dismiss();
            });
        }

        LifecycleExtKt.lifecycleOwner(dialog, this);
        dialog.cornerRadius(4f, null);
        dialog.show();
    }

    @Override
    public String format(Date date) {
        return DateUtil.getTime(date);
    }

    public void onEvent(MessageEvent event) {
        cn.jpush.im.android.api.model.Message message = event.getMessage();
        mHandler.sendMessage(mHandler.obtainMessage(Constant.NEW_MESSAGE, message));
    }

    public void onEvent(OfflineMessageEvent event) {
        Conversation conversation = event.getConversation();
        UserInfo userInfo = (UserInfo) conversation.getTargetInfo();
        if (userInfo.getUserName().equals(username)) {
            List<cn.jpush.im.android.api.model.Message> messages = event.getOfflineMessageList();
            for (cn.jpush.im.android.api.model.Message msg : messages) {
                Message message = Transform.getMessage(msg);
                assert message != null;
                mMessageListAdapter.addToStart(message, true);
            }
        }
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
            if (msg.what == Constant.NEW_MESSAGE) {
                cn.jpush.im.android.api.model.Message message = (cn.jpush.im.android.api.model.Message) msg.obj;
                Message message1 = Transform.getMessage(message);
                assert message1 != null;
                activity.mMessageListAdapter.addToStart(message1, true);
            }
        }
    }
}
