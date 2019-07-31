package cn.paulpaulzhang.fair.ui.main.interest;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.base.delegates.FairDelegate;
import cn.paulpaulzhang.fair.ui.main.interest.discovery.DiscoveryDelegate;
import cn.paulpaulzhang.fair.ui.main.interest.follow.FollowDelegate;
import cn.paulpaulzhang.fair.ui.main.interest.topic.TopicDelegate;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.interest
 * 创建时间：7/8/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private List<FairDelegate> delegates = new ArrayList<>();
    private FairDelegate currentDelegate;

    public TabViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        delegates.add(new FollowDelegate());
        delegates.add(new DiscoveryDelegate());
        delegates.add(new TopicDelegate());
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
        if (getCurrentDelegate() != object) {
            currentDelegate = (FairDelegate) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    public FairDelegate getCurrentDelegate() {
        return currentDelegate;
    }
}
