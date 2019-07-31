package cn.paulpaulzhang.fair.ui.main.post;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.paulpaulzhang.fair.R;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post
 * 创建时间: 7/25/2019
 * 创建人: zlm31
 * 描述:
 */
public class TestAdapter extends BaseQuickAdapter<TestItem, BaseViewHolder> {
    public TestAdapter(int layoutResId, @Nullable List<TestItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestItem item) {
    }
}
