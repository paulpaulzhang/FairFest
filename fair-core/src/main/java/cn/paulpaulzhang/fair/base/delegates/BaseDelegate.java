package cn.paulpaulzhang.fair.base.delegates;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maning.imagebrowserlibrary.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 项目名：   FairFest
 * 包名：     cn.paulpaulzhang.fair.delegates
 * 文件名：   BaseDelegate
 * 创建者：   PaulZhang
 * 创建时间： 2019/5/18 19:40
 * 描述：     基础Delegate，继承自fragment，
 * 已绑定ButterKnife，其子类可直接使用，不需要再次绑定或解绑，使用时请继承FairDelegate
 */
public abstract class BaseDelegate extends Fragment {

    private Unbinder mUnbinder = null;

    /**
     * 绑定布局id，子类实现该方法传入布局id即可，不需要再实现onCreateView绑定布局
     * @return id
     */
    public abstract Object setLayout();

    /**
     * 重写该方法实现自己的业务逻辑，该方法在onCreateView调用
     * @param savedInstanceState savedInstanceState
     * @param view view
     */
    public abstract void initView(@Nullable Bundle savedInstanceState, View view);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;

        if (setLayout() instanceof Integer) {
            rootView = inflater.inflate((Integer) setLayout(), container, false);
        } else if (setLayout() instanceof View) {
            rootView = (View) setLayout();
        } else {
            throw new ClassCastException("setLayout() type must be int or View!");
        }

        mUnbinder = ButterKnife.bind(this, rootView);
        initView(savedInstanceState, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
