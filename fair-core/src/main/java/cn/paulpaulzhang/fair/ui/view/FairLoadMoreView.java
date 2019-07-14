package cn.paulpaulzhang.fair.ui.view;

import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * 包名: cn.paulpaulzhang.fair.ui.view
 * 创建时间: 7/14/2019
 * 创建人: zlm31
 * 描述: 自定义上拉加载更多布局
 */
public class FairLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected int getLoadingViewId() {
        return 0;
    }

    @Override
    protected int getLoadFailViewId() {
        return 0;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }
}
