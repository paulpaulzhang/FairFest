package cn.paulpaulzhang.fair.util.common;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 包名: cn.paulpaulzhang.fair.util
 * 创建时间: 7/29/2019
 * 创建人: zlm31
 * 描述:
 */
public class CommonUtil {
    public static void showKeyboard(AppCompatEditText editText) {
        //其中editText为dialog中的输入框的 EditText
        if (editText != null) {
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }
    }
}
