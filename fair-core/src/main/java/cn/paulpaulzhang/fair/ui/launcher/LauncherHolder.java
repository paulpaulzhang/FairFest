package cn.paulpaulzhang.fair.ui.launcher;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.bigkoo.convenientbanner.holder.Holder;

import cn.paulpaulzhang.fair.R;


public class LauncherHolder extends Holder<Integer> {

    private AppCompatImageView mImageView;

    public LauncherHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void initView(View itemView) {
        mImageView = itemView.findViewById(R.id.scroll_iv);
    }

    @Override
    public void updateUI(Integer data) {
        mImageView.setImageResource(data);
    }
}
