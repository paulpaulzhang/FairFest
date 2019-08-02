package cn.paulpaulzhang.fair.ui.main;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.main.chat.ChatDelegate;
import cn.paulpaulzhang.fair.sc.main.interest.InterestDelegate;
import cn.paulpaulzhang.fair.sc.main.market.MarketDelegate;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：7/8/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class BottomNavViewPagerAdapter extends FragmentPagerAdapter {

    private List<FairDelegate> delegates = new ArrayList<>();
    private FairDelegate currentDelegate;

    public BottomNavViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        delegates.clear();
        delegates.add(new InterestDelegate());
        delegates.add(new MarketDelegate());
        delegates.add(new ChatDelegate());
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
