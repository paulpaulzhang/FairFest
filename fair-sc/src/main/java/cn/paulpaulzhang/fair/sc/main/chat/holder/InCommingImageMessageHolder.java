package cn.paulpaulzhang.fair.sc.main.chat.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.messages.MessageHolders;

import cn.paulpaulzhang.fair.sc.main.post.activity.PhotoPreviewActivity;
import cn.paulpaulzhang.fair.sc.main.chat.model.Message;
import es.dmoral.toasty.Toasty;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.chat.holder
 * 创建时间: 8/7/2019
 * 创建人: zlm31
 * 描述:
 */
public class InCommingImageMessageHolder extends MessageHolders.IncomingImageMessageViewHolder<Message> {

    public InCommingImageMessageHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        Glide.with(itemView).load(message.getUser().getAvatar()).into(userAvatar);
        userAvatar.setOnClickListener(v -> {
            Toasty.info(itemView.getContext(), message.getUser().getName(), Toasty.LENGTH_SHORT).show();
        });
    }
}
