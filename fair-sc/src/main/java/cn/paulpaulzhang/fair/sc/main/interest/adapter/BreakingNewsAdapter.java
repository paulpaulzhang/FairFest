package cn.paulpaulzhang.fair.sc.main.interest.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.interest.model.BreakingNews;
import cn.paulpaulzhang.fair.util.date.DateUtil;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.adapter
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class BreakingNewsAdapter extends BaseQuickAdapter<BreakingNews, BaseViewHolder> {
    public BreakingNewsAdapter(int layoutResId, @Nullable List<BreakingNews> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BreakingNews item) {
        helper.setText(R.id.tv_text, item.getText())
                .setText(R.id.tv_time, DateUtil.getTime(item.getTime()));
    }
}
