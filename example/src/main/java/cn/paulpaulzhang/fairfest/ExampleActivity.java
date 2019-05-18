package cn.paulpaulzhang.fairfest;

import cn.paulpaulzhang.fair.activities.ProxyActivity;
import cn.paulpaulzhang.fair.delegates.FairDelegate;

public class ExampleActivity extends ProxyActivity {


    @Override
    public FairDelegate setRootDelegate() {
        return new ExampleDelegate();
    }
}
