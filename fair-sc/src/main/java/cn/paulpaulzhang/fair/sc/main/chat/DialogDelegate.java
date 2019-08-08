package cn.paulpaulzhang.fair.sc.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.afollestad.materialdialogs.lifecycle.LifecycleExtKt;
import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Objects;

import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.HomeActivity;
import cn.paulpaulzhang.fair.sc.main.chat.fixtures.DialogFixtures;
import cn.paulpaulzhang.fair.sc.main.chat.fixtures.Transform;
import cn.paulpaulzhang.fair.sc.main.chat.model.Dialog;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import es.dmoral.toasty.Toasty;


/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/8/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class DialogDelegate extends FairDelegate
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    @BindView(R2.id.dialog_list)
    DialogsList mDialogList;

    @BindView(R2.id.srl_chat)
    SwipeRefreshLayout mSwipeRefresh;

    private ImageLoader mImageLoader;
    private DialogsListAdapter<Dialog> mDialogListAdapter;
    private BackgroundHandler mHandler;

    @Override
    public Object setLayout() {
        return R.layout.delegate_dialog;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        JMessageClient.registerEventReceiver(this);
        mHandler = new BackgroundHandler(this);
        mImageLoader = (imageView, url, payload) -> Glide.with(this).load(url).placeholder(R.drawable.default_placeholder).into(imageView);
        initAdapter();
        initSwipeRefresh();
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_light);
        mSwipeRefresh.setOnRefreshListener(() -> mDialogListAdapter.setItems(DialogFixtures.getDialogs(mSwipeRefresh)));
    }

    private void initAdapter() {
        mDialogListAdapter = new DialogsListAdapter<>(R.layout.item_custom_dialog, mImageLoader);
        mDialogListAdapter.setItems(DialogFixtures.getDialogs());
        mDialogListAdapter.setOnDialogClickListener(this);
        mDialogListAdapter.setOnDialogLongClickListener(this);
        mDialogList.setAdapter(mDialogListAdapter);
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        dialog.setUnreadCount(0);
        dialog.getConversation().setUnReadMessageCnt(0);
        if (dialog.getUsers().size() == 1) {
            Intent intent = new Intent(getContext(), MessageActivity.class);
            intent.putExtra("uid", JMessageClient.getMyInfo().getUserName());
            intent.putExtra("username", dialog.getId());
            intent.putExtra("appkey", dialog.getConversation().getTargetAppKey());
            startActivity(intent);
        }

    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
        MaterialDialog materialDialog = new MaterialDialog(Objects.requireNonNull(getContext()), MaterialDialog.getDEFAULT_BEHAVIOR());
        DialogCustomViewExtKt.customView(materialDialog, R.layout.view_custom_remove_dialog,
                null, false, true, false, true);
        LifecycleExtKt.lifecycleOwner(materialDialog, this);
        materialDialog.cornerRadius(8f, null);
        materialDialog.show();

        View customerView = DialogCustomViewExtKt.getCustomView(materialDialog);

        customerView.findViewById(R.id.tv_delete).setOnClickListener(v2 -> {
            JMessageClient.deleteSingleConversation(dialog.getId(), dialog.getConversation().getTargetAppKey());
            mDialogListAdapter.deleteById(dialog.getId());
            materialDialog.dismiss();
        });
    }

    public void onEvent(MessageEvent event) {
        Message message = event.getMessage();
        final UserInfo userInfo = (UserInfo) message.getTargetInfo();
        String targetId = userInfo.getUserName();
        Conversation conv = JMessageClient.getSingleConversation(targetId, userInfo.getAppKey());
        mHandler.sendMessage(mHandler.obtainMessage(Constant.REFRESH_CONVERSATION_LIST, conv));

    }

    public void onEvent(NotificationClickEvent event) {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        Objects.requireNonNull(getContext()).startActivity(intent);
    }

    public void onEvent(OfflineMessageEvent event) {
        Conversation conversation = event.getConversation();
        mHandler.sendMessage(mHandler.obtainMessage(Constant.REFRESH_CONVERSATION_LIST, conversation));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    private static class BackgroundHandler extends Handler {
        private final WeakReference<DialogDelegate> chatDelegate;

        BackgroundHandler(DialogDelegate dialogDelegate) {
            this.chatDelegate = new WeakReference<>(dialogDelegate);
        }

        @Override
        public void handleMessage(@NotNull android.os.Message msg) {
            super.handleMessage(msg);
            DialogDelegate delegate = chatDelegate.get();
            if (delegate == null) {
                return;
            }
            if (msg.what == Constant.REFRESH_CONVERSATION_LIST) {
                Conversation conv = (Conversation) msg.obj;
                Dialog dialog = Transform.getDialog(conv);
                delegate.mDialogListAdapter.deleteById(dialog.getId());
                delegate.mDialogListAdapter.addItem(0, dialog);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDialogListAdapter.setItems(DialogFixtures.getDialogs());
    }
}
