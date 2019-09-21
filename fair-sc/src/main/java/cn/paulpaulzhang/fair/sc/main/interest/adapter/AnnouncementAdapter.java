package cn.paulpaulzhang.fair.sc.main.interest.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.interest.model.Announcement;
import cn.paulpaulzhang.fair.util.date.DateUtil;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.adapter
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class AnnouncementAdapter extends BaseQuickAdapter<Announcement, BaseViewHolder> {
    public AnnouncementAdapter(int layoutResId, @Nullable List<Announcement> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Announcement item) {
        helper.setText(R.id.tv_text, item.getText())
                .setText(R.id.tv_time, DateUtil.getTime(item.getTime()));
    }
}
