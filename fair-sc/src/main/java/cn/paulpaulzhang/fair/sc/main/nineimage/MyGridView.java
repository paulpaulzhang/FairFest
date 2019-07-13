package cn.paulpaulzhang.fair.sc.main.nineimage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.nineimage
 * 创建时间: 7/13/2019
 * 创建人: zlm31
 * 描述:
 */
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
