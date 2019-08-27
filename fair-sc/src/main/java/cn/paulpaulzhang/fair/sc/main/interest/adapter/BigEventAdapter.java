package cn.paulpaulzhang.fair.sc.main.interest.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.interest.model.BigEvent;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.adapter
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class BigEventAdapter extends BaseQuickAdapter<BigEvent, BaseViewHolder> {
    public BigEventAdapter(int layoutResId, @Nullable List<BigEvent> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BigEvent item) {
        helper.setText(R.id.tv_event, item.getEvent());
    }
}
