package cn.paulpaulzhang.fair.sc.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.sc.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.database.Constant;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.entity.DiscoveryLikeCache;
import cn.paulpaulzhang.fair.sc.database.entity.DiscoveryPostCache;
import cn.paulpaulzhang.fair.sc.database.entity.DiscoveryUserCache;
import cn.paulpaulzhang.fair.sc.database.entity.LocalUser;
import cn.paulpaulzhang.fair.sc.database.entity.TopicCache;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.json
 * 创建时间: 7/13/2019
 * 创建人: zlm31
 * 描述: Json解析工具类
 */
public final class JsonParseUtil {
    public static void parsePost(String response, int requestType) {
        List<DiscoveryPostCache> discoveryPostCaches = new ArrayList<>();
        List<DiscoveryUserCache> discoveryUserCaches = new ArrayList<>();
        List<DiscoveryLikeCache> discoveryLikeCaches = new ArrayList<>();
        JSONObject result = JSON.parseObject(response).getJSONObject("result");
        JSONArray postArr = result.getJSONArray("post");
        JSONArray userArr = result.getJSONArray("user");
        JSONArray likeArr = result.getJSONArray("like");

        for (int i = 0; i < postArr.size(); i++) {
            JSONObject object = postArr.getJSONObject(i);
            long id = object.getLong("id");
            long uid = object.getLong("uid");
            int type = object.getInteger("type");
            String title = object.getString("title");
            String content = object.getString("content");
            String imagesUrl = object.getJSONArray("imagesUrl").toJSONString();
            int likeCount = object.getInteger("likeCount");
            int commentCount = object.getInteger("commentCount");
            int shareCount = object.getInteger("shareCount");
            String time = object.getString("time");
            String device = object.getString("device");
            DiscoveryPostCache discoveryPostCache = new DiscoveryPostCache(id, uid, type, title, content, imagesUrl,
                    likeCount, commentCount, shareCount, time, device);
            discoveryPostCaches.add(discoveryPostCache);


        }

        for (int i = 0; i < userArr.size(); i++) {
            JSONObject object = userArr.getJSONObject(i);
            long id = object.getLong("id");
            String username = object.getString("username");
            String birthday = object.getString("birthday");
            String gender = object.getString("gender");
            int followers = object.getInteger("followers");
            int fans = object.getInteger("fans");
            String school = object.getString("school");
            String introduction = object.getString("introduction");
            String avatar = object.getString("avatar");
            String background = object.getString("background");
            String time = object.getString("time");

            DiscoveryUserCache discoveryUserCache = new DiscoveryUserCache(id, username, birthday, gender, followers,
                    fans, school, introduction, avatar, background, time);
            discoveryUserCaches.add(discoveryUserCache);
        }

        for (int i = 0; i < likeArr.size(); i++) {
            JSONObject object = likeArr.getJSONObject(i);
            long pid = object.getLong("pid");
            boolean isLike = object.getBoolean("isLike");
            DiscoveryLikeCache discoveryLikeCache = new DiscoveryLikeCache(pid, isLike);
            discoveryLikeCaches.add(discoveryLikeCache);
        }

        Box<DiscoveryPostCache> postBox = ObjectBox.get().boxFor(DiscoveryPostCache.class);
        Box<DiscoveryUserCache> userBox = ObjectBox.get().boxFor(DiscoveryUserCache.class);
        Box<DiscoveryLikeCache> likeBox = ObjectBox.get().boxFor(DiscoveryLikeCache.class);
        if (requestType == Constant.REFRESH_DATA) {
            postBox.removeAll();
            userBox.removeAll();
            likeBox.removeAll();
        }
        postBox.put(discoveryPostCaches);
        userBox.put(discoveryUserCaches);
        likeBox.put(discoveryLikeCaches);
    }

    public static void parseLocalUser(String response) {
        JSONObject object = JSON.parseObject(response).getJSONObject("localUser");

        long id = object.getLong("id");
        String username = object.getString("username");
        String password = object.getString("password");
        String birthday = object.getString("birthday");
        String gender = object.getString("gender");
        int followers = object.getInteger("followers");
        int fans = object.getInteger("fans");
        long phone = object.getLong("phone");
        String email = object.getString("email");
        String school = object.getString("school");
        long studentId = object.getLong("studentId");
        int permission = object.getInteger("permission");
        String introduction = object.getString("introduction");
        String avatar = object.getString("avatar");
        String background = object.getString("background");
        String time = object.getString("time");
        String features = object.getString("features");

        LocalUser user = new LocalUser(id, username, password, birthday, gender,
                followers, fans, phone, email, school, studentId,
                permission, introduction, avatar, background, time, features);
        Box<LocalUser> localUserBox = ObjectBox.get().boxFor(LocalUser.class);
        localUserBox.remove(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        localUserBox.put(user);
    }

    public static void parseTopic(String response, int type) {
        JSONArray array = JSON.parseObject(response).getJSONArray("result");
        List<TopicCache> topicCaches = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            long id = object.getLong("id");
            String name = object.getString("name");
            String img = object.getString("img");
            int follow = object.getInteger("follow");
            int post = object.getInteger("post");
            topicCaches.add(new TopicCache(id, name, img, follow, post));
        }

        Box<TopicCache> topicBox = ObjectBox.get().boxFor(TopicCache.class);
        if (type == Constant.REFRESH_DATA) {
            topicBox.removeAll();
        }
        topicBox.put(topicCaches);
    }

    public static ArrayList<String> parseImgs(String imgUrl) {
        JSONArray array = JSON.parseArray(imgUrl);
        ArrayList<String> imgs = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            imgs.add(array.getString(i));
        }
        return imgs;
    }
}
