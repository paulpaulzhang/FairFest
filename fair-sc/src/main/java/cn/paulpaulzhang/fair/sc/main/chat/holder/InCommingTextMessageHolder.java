package cn.paulpaulzhang.fair.sc.main.chat.holder;

import android.view.View;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.RoundedImageView;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.chat.model.Message;
import es.dmoral.toasty.Toasty;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.chat.holder
 * 创建时间: 8/7/2019
 * 创建人: zlm31
 * 描述:
 */
public class InCommingTextMessageHolder extends MessageHolders.IncomingTextMessageViewHolder<Message> {
    public InCommingTextMessageHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        Glide.with(itemView).load(message.getUser().getAvatar()).into(userAvatar);
    }
}
