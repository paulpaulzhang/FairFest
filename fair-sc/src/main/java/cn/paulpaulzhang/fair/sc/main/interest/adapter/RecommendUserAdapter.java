package cn.paulpaulzhang.fair.sc.main.interest.adapter;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.interest.model.RecommendUser;
import cn.paulpaulzhang.fair.util.image.ImageUtil;
import es.dmoral.toasty.Toasty;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.interest.discovery
 * 创建时间: 7/22/2019
 * 创建人: zlm31
 * 描述: 推荐用户 RecyclerView adapter
 */
public class RecommendUserAdapter extends BaseQuickAdapter<RecommendUser, BaseViewHolder> {
    RecommendUserAdapter(int layoutResId, @Nullable List<RecommendUser> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendUser item) {
        SimpleDraweeView mDraweeView = helper.getView(R.id.user_bg);
        String url = item.getUserCache().getAvatar();
        ImageUtil.setBlurImage(mContext, mDraweeView, url, 10);
        helper.setText(R.id.tv_user, item.getUserCache().getUsername())
                .setText(R.id.tv_time, "来 校园π 187天了")
                .setText(R.id.tv_follow, item.getUserCache().getFollowers() + " 关注")
                .setText(R.id.tv_fans, item.getUserCache().getFans() + "粉丝");
        MaterialButton mButton = helper.getView(R.id.btn_follow);
        mButton.setOnClickListener(v -> RestClient.builder()
                .url("follow")
                .params("uid", item.getUserCache().getId())
                .success(r -> {
                    if (JSON.parseObject(r).getBoolean("result")) {
                        Toasty.success(mContext, "关注成功", Toasty.LENGTH_SHORT).show();
                    }
                })
                .build());
    }
}
