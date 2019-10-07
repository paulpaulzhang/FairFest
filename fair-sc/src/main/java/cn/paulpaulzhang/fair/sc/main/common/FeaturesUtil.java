package cn.paulpaulzhang.fair.sc.main.common;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.JsonParseUtil;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import io.objectbox.Box;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.common
 * 创建时间：9/23/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class FeaturesUtil {
    public static void update(long pid) {
        RestClient.builder()
                .url(Api.GET_TOPICS_BY_PID)
                .params("pid", pid)
                .success(response -> {
                    List<String> topics = JsonParseUtil.parseTopicName(response);
                    Box<User> userBox = ObjectBox.get().boxFor(User.class);
                    User user = userBox.get(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
                    String features = user.getFeatures();

                    Map<String, Object> map = JsonParseUtil.parseFeatures(features);
                    for (String topic : topics) {
                        if (!map.containsKey(topic) || map.get(topic) == null) {
                            map.put(topic, 0f);
                        }

                        map.put(topic, Float.valueOf(String.valueOf(map.get(topic))) + 1.0);
                    }

                    RestClient.builder()
                            .url(Api.SET_FEATURES)
                            .params("uid", FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()))
                            .params("features", JSON.toJSONString(map))
                            .success(response1 -> {
                                user.setFeatures(JSON.toJSONString(map));
                                userBox.put(user);
                            })
                            .build()
                            .post();
                })
                .build()
                .get();
    }
}
