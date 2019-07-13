package cn.paulpaulzhang.fair.sc.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

import butterknife.BindView;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.entity.Post;
import cn.paulpaulzhang.fair.sc.json.JsonParseUtil;
import cn.paulpaulzhang.fair.util.system.SystemUtil;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/8/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class ChatDelegate extends FairDelegate {
    @BindView(R2.id.text)
    TextView textView;

    @Override
    public Object setLayout() {
        return R.layout.delegate_chat;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        RestClient.builder()
                .url("post")
                .params("", "")
                .success(r -> {
                    List<Post> posts = JsonParseUtil.parsePost(r);
                    String imgs = posts.get(0).getImagesUrl();
                    JSONArray array = JSON.parseArray(imgs);
                    String s = array.getString(0);
                    textView.setText(SystemUtil.getSystemModel());
                })
                .build()
                .get();
    }
}
