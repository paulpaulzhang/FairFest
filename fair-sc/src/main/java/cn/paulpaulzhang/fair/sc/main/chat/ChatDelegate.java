package cn.paulpaulzhang.fair.sc.main.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;

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
    @BindView(R2.id.dv)
    SimpleDraweeView imageView;

    @Override
    public Object setLayout() {
        return R.layout.delegate_chat;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        Uri uri = Uri.parse("http://pic37.nipic.com/20140113/8800276_184927469000_2.png");
        imageView.setImageURI(uri);
    }
}
