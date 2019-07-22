package cn.paulpaulzhang.fair.sc.main.interest.discovery;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.util.image.ImageUtil;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.discovery
 * 创建时间: 7/22/2019
 * 创建人: zlm31
 * 描述: 推荐用户 RecyclerView adapter
 */
public class RecommendUserAdapter extends BaseQuickAdapter<RecommendUserItem, BaseViewHolder> {
    public RecommendUserAdapter(int layoutResId, @Nullable List<RecommendUserItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendUserItem item) {
        SimpleDraweeView mDraweeView = helper.getView(R.id.user_bg);
        String url = "http://pic25.nipic.com/20121205/10197997_003647426000_2.jpg";
        ImageUtil.setBlurImage(mContext, mDraweeView, url);
    }
}
