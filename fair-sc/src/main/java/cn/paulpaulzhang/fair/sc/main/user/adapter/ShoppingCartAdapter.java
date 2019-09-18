package cn.paulpaulzhang.fair.sc.main.user.adapter;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.main.user.model.Goods;
import es.dmoral.toasty.Toasty;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.user.adapter
 * 创建时间：9/18/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class ShoppingCartAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    public ShoppingCartAdapter(int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        helper.setText(R.id.tv_name, item.getSname())
                .setText(R.id.tv_price, String.valueOf(item.getPrice()))
                .setText(R.id.tv_info, item.getOverview());

        AppCompatTextView mStatus = helper.getView(R.id.tv_status);
        SimpleDraweeView mPicture = helper.getView(R.id.dv_picture);
        MaterialButton mBuy = helper.getView(R.id.btn_buy);
        MaterialButton mDelete = helper.getView(R.id.btn_delete);

        int status = item.getIsSold();
        if (status == 0) {
            mStatus.setText("售卖中");
        } else if (status == 1) {
            mStatus.setText("已售出");
        } else if (status == 2) {
            mStatus.setText("已下架");
        }

        mPicture.setImageURI(Uri.parse(item.getHeadImg()));

        mBuy.setOnClickListener(v -> {

        });

        mDelete.setOnClickListener(v -> {
            AlertDialog dialog = new MaterialAlertDialogBuilder(mContext)
                    .setTitle("操作确认")
                    .setMessage("点击确认删除该宝贝")
                    .setPositiveButton("确认", (dialogInterface, i) -> {
                        RestClient.builder()
                                .url(Api.DELETE_STORE)
                                .params("sid", item.getSid())
                                .success(response -> {
                                    String result = JSON.parseObject(response).getString("result");
                                    if (TextUtils.equals(result, "ok")) {
                                        remove(helper.getLayoutPosition());
                                    } else {
                                        Toasty.error(mContext, "删除失败", Toasty.LENGTH_SHORT).show();
                                    }
                                })
                                .error((code, msg) -> Toasty.error(mContext, "删除失败 " + code, Toasty.LENGTH_SHORT).show())
                                .build()
                                .post();
                        dialogInterface.dismiss();
                    })
                    .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getColor(android.R.color.holo_red_light));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getColor(R.color.font_default));
        });
    }
}
