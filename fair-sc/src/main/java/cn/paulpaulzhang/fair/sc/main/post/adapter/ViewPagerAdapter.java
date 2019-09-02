package cn.paulpaulzhang.fair.sc.main.post.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.main.post.delegate.CommentDelegate;
import cn.paulpaulzhang.fair.sc.main.post.delegate.LikeDelegate;
import cn.paulpaulzhang.fair.sc.main.post.delegate.ShareDelegate;
import cn.paulpaulzhang.fair.sc.main.user.delegate.AboutDelegate;
import cn.paulpaulzhang.fair.sc.main.user.delegate.ConcernTopicDelegate;
import cn.paulpaulzhang.fair.sc.main.user.delegate.DynamicDelegate;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user.adapter
 * 创建时间: 8/17/2019
 * 创建人: zlm31
 * 描述:
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> delegates = new ArrayList<>();
    private FairDelegate currentDelegate;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        delegates.add(new LikeDelegate());
        delegates.add(new CommentDelegate());
        delegates.add(new ShareDelegate());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return delegates.get(position);
    }

    @Override
    public int getCount() {
        return delegates.size();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        currentDelegate = (FairDelegate) object;
        super.setPrimaryItem(container, position, object);
    }

    public FairDelegate getCurrentDelegate() {
        return currentDelegate;
    }
}
