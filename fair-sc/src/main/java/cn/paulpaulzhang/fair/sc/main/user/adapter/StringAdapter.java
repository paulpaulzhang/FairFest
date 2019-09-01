package cn.paulpaulzhang.fair.sc.main.user.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.database.Entity.School;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.adapter
 * 创建时间：9/1/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class StringAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public StringAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item);
    }
}
