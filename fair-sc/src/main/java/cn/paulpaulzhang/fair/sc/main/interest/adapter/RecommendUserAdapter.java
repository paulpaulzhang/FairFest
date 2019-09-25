package cn.paulpaulzhang.fair.sc.main.interest.adapter;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.interest.model.RecommendUser;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.image.ImageUtil;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
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
        String url = item.getUserCache().getAvatar() == null ? Constant.DEFAULT_AVATAR : item.getUserCache().getAvatar();
        String username = item.getUserCache().getUsername() == null ? item.getUserCache().getId() + "" : item.getUserCache().getUsername();
        ImageUtil.setBlurImage(mContext, mDraweeView, url, 20);
        helper.setText(R.id.tv_user, username)
                .setText(R.id.tv_time, "来 " + mContext.getString(R.string.app_name)  + " " + (DateUtil.getIntervalAsDay(item.getUserCache().getTime(), System.currentTimeMillis()) + 1) + "天了")
                .setText(R.id.tv_dynamic, item.getUserCache().getDynamicCount() + "动态")
                .setText(R.id.tv_fans, item.getUserCache().getFansCount() + "粉丝");

        MaterialButton mButton = helper.getView(R.id.btn_follow);
        if (item.getUserCache().isFollowed()) {
            mButton.setText("已关注");
        } else {
            mButton.setText("关注");
        }

        mButton.setOnClickListener(v->{
            if (TextUtils.equals(mButton.getText().toString(), "关注")) {
                RestClient.builder()
                        .url(Api.PAY_USER)
                        .params("focuserId", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                        .params("focusedId", item.getUserCache().getId())
                        .success(r -> {
                            String result = JSON.parseObject(r).getString("result");
                            if (TextUtils.equals(result, "ok")) {
                                mButton.setText("已关注");
                            } else {
                                Toasty.error(mContext, "关注失败 ", Toasty.LENGTH_SHORT).show();
                            }
                        })
                        .error((code, msg) -> Toasty.error(mContext, "关注失败 " + code, Toasty.LENGTH_SHORT).show())
                        .build()
                        .post();
            } else {
                RestClient.builder()
                        .url(Api.CANCEL_PAY_USER)
                        .params("focuserId", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                        .params("focusedId", item.getUserCache().getId())
                        .success(r -> {
                            String result = JSON.parseObject(r).getString("result");
                            if (TextUtils.equals(result, "ok")) {
                                mButton.setText("关注");
                            } else {
                                Toasty.error(mContext, "取消失败 ", Toasty.LENGTH_SHORT).show();
                            }
                        })
                        .error((code, msg) -> Toasty.error(mContext, "取消失败 " + code, Toasty.LENGTH_SHORT).show())
                        .build()
                        .post();
            }
        });
    }
}
