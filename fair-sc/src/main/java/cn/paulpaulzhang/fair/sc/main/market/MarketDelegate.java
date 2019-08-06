package cn.paulpaulzhang.fair.sc.main.market;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;

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
public class MarketDelegate extends FairDelegate {
    @BindView(R2.id.imageView)
    AppCompatImageView imageView;

    @Override
    public Object setLayout() {
        return R.layout.delegate_market;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {
        Glide.with(this).load("http://www.matchstickmen.club/test1.jpg").into(imageView);
    }
}
