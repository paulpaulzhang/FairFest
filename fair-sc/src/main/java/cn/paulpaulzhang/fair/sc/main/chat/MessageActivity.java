package cn.paulpaulzhang.fair.sc.main.chat;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import butterknife.BindView;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;

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
        MessagesListAdapter.OnMessageLongClickListener,
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener {

    @BindView(R2.id.tb_message)
    MaterialToolbar mToolbar;

    @BindView(R2.id.message_list)
    MessagesList mMessageList;

    @BindView(R2.id.message_input)
    MessageInput mMessageInput;

    private ImageLoader mImageLoader;
    private MessagesListAdapter mMessageListAdapter;

    @Override
    public int setLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initToolbar(mToolbar);
        mImageLoader = (imageView, url, payload) -> Glide.with(this).load(url).placeholder(R.mipmap.ic_launcher_round).into(imageView);
    }

    private void initAdapter() {

    }

    @Override
    public void onAddAttachments() {

    }

    @Override
    public boolean onSubmit(CharSequence input) {
        return false;
    }

    @Override
    public void onStartTyping() {

    }

    @Override
    public void onStopTyping() {

    }

    @Override
    public void onMessageLongClick(IMessage message) {

    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }

    @Override
    public void onSelectionChanged(int count) {

    }
}
