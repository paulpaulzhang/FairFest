package cn.paulpaulzhang.fair.sc.main.interest;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.entity.Post;
import cn.paulpaulzhang.fair.sc.json.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.main.banner.BannerHolderCreator;
import cn.paulpaulzhang.fair.util.system.SystemUtil;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间：7/10/19
 * 创建人：paulpaulzhang
 * 描述：发现页
 */
public class DiscoveryDelegate extends FairDelegate {


    @BindView(R2.id.rv_discovery)
    RecyclerView mRecyclerView;

    private DiscoveryAdapter adapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_discovery;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {

        List<String> list = new ArrayList<>();
        list.add("http://pic37.nipic.com/20140113/8800276_184927469000_2.png");
        list.add("http://pic40.nipic.com/20140331/9469669_142840860000_2.jpg");
        list.add("http://pic41.nipic.com/20140508/18609517_112216473140_2.jpg");
        adapter = new DiscoveryAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        View header = View.inflate(getContext(), R.layout.view_header_discovery, null);
        initBanner(header, list);
        adapter.addHeaderView(header);
        mRecyclerView.setAdapter(adapter);

        requestData();
    }

    private void initBanner(View view, List<String> data) {
        ConvenientBanner<String> mConvenientBanner = view.findViewById(R.id.cb_discovery);
        mConvenientBanner.setPages(new BannerHolderCreator(), data)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(5000)
                .setOnItemClickListener(position -> Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show());
    }

    private void requestData() {
        RestClient.builder()
                .url("post")
                .success(r -> {
                    List<Post> posts = JsonParseUtil.parsePost(r);
                    List<DiscoveryItem> list = new ArrayList<>();
                    for (Post p : posts) {
                        list.add(new DiscoveryItem(p.getType(), p));
                    }
                    adapter.addData(list);
                    adapter.notifyDataSetChanged();
                })
                .build()
                .get();
    }
}
