package cn.paulpaulzhang.fair.sc.main.chat.holder;

import android.view.View;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.messages.MessageHolders;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.chat.model.Message;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.chat.holder
 * 创建时间: 8/5/2019
 * 创建人: zlm31
 * 描述:
 */
public class OutCommingImageMessageHolder extends MessageHolders.OutcomingImageMessageViewHolder<Message> {
    private CircleImageView avatar;

    public OutCommingImageMessageHolder(View itemView, Object payload) {
        super(itemView, payload);
        avatar = itemView.findViewById(R.id.messageUserAvatar);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        Glide.with(itemView).load(message.getUser().getAvatar()).into(avatar);
    }
}
