package cn.paulpaulzhang.fair.sc.main.interest.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.interest.model.TeamManage;
import cn.paulpaulzhang.fair.util.date.DateUtil;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.adapter
 * 创建时间：9/25/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TeamManageAdapter extends BaseQuickAdapter<TeamManage, BaseViewHolder> {
    public TeamManageAdapter(int layoutResId, @Nullable List<TeamManage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TeamManage item) {
        helper.setText(R.id.tv_text, item.getInfo())
                .setText(R.id.tv_time, DateUtil.getTime(item.getTime()));
    }
}
