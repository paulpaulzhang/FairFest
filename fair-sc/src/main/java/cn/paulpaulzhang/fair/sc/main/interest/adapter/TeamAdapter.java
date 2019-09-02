package cn.paulpaulzhang.fair.sc.main.interest.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.main.interest.model.Team;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest.adapter
 * 创建时间：9/2/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TeamAdapter extends BaseQuickAdapter<Team, BaseViewHolder> {
    public TeamAdapter(int layoutResId, @Nullable List<Team> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Team item) {

    }
}
