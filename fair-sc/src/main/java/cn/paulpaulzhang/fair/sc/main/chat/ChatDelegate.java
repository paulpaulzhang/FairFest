package cn.paulpaulzhang.fair.sc.main.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.chatkit.dialogs.DialogsList;

import butterknife.BindView;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;


/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/8/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class ChatDelegate extends FairDelegate {
    @BindView(R2.id.dialog_list)
    DialogsList mDialogList;

    @Override
    public Object setLayout() {
        return R.layout.delegate_message;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {

    }
}
