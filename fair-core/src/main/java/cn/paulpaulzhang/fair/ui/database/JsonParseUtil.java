package cn.paulpaulzhang.fair.sc.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryLikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryPostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryUserCache;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowLikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowPostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowUserCache;
import cn.paulpaulzhang.fair.sc.database.Entity.LocalUser;
import cn.paulpaulzhang.fair.sc.database.Entity.RecommendUserCache;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicCache;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicLikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicPostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicUserCache;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.json
 * 创建时间: 7/13/2019
 * 创建人: zlm31
 * 描述: Json解析工具类
 */
public final class JsonParseUtil {
    public static void parseDiscoveryPost(String response, int requestType) {
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
            String avatar = object.getString("avatar");

            DiscoveryUserCache discoveryUserCache = new DiscoveryUserCache(id, username, avatar);
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

    public static void parseFollowPost(String response, int requestType) {
        List<FollowPostCache> followPostCaches = new ArrayList<>();
        List<FollowUserCache> followUserCaches = new ArrayList<>();
        List<FollowLikeCache> followLikeCaches = new ArrayList<>();
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
            FollowPostCache followPostCache = new FollowPostCache(id, uid, type, title, content, imagesUrl,
                    likeCount, commentCount, shareCount, time, device);
            followPostCaches.add(followPostCache);
        }

        for (int i = 0; i < userArr.size(); i++) {
            JSONObject object = userArr.getJSONObject(i);
            long id = object.getLong("id");
            String username = object.getString("username");
            String avatar = object.getString("avatar");

            FollowUserCache followUserCache = new FollowUserCache(id, username, avatar);
            followUserCaches.add(followUserCache);
        }

        for (int i = 0; i < likeArr.size(); i++) {
            JSONObject object = likeArr.getJSONObject(i);
            long pid = object.getLong("pid");
            boolean isLike = object.getBoolean("isLike");
            FollowLikeCache followLikeCache = new FollowLikeCache(pid, isLike);
            followLikeCaches.add(followLikeCache);
        }

        Box<FollowPostCache> postBox = ObjectBox.get().boxFor(FollowPostCache.class);
        Box<FollowUserCache> userBox = ObjectBox.get().boxFor(FollowUserCache.class);
        Box<FollowLikeCache> likeBox = ObjectBox.get().boxFor(FollowLikeCache.class);
        if (requestType == Constant.REFRESH_DATA) {
            postBox.removeAll();
            userBox.removeAll();
            likeBox.removeAll();
        }

        postBox.put(followPostCaches);
        userBox.put(followUserCaches);
        likeBox.put(followLikeCaches);
    }

    public static void parseTopicPost(String response, int requestType) {
        List<TopicPostCache> topicPostCaches = new ArrayList<>();
        List<TopicUserCache> topicUserCaches = new ArrayList<>();
        List<TopicLikeCache> topicLikeCaches = new ArrayList<>();
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
            TopicPostCache topicPostCache = new TopicPostCache(id, uid, type, title, content, imagesUrl,
                    likeCount, commentCount, shareCount, time, device);
            topicPostCaches.add(topicPostCache);
        }

        for (int i = 0; i < userArr.size(); i++) {
            JSONObject object = userArr.getJSONObject(i);
            long id = object.getLong("id");
            String username = object.getString("username");
            String avatar = object.getString("avatar");

            TopicUserCache topicUserCache = new TopicUserCache(id, username, avatar);
            topicUserCaches.add(topicUserCache);
        }

        for (int i = 0; i < likeArr.size(); i++) {
            JSONObject object = likeArr.getJSONObject(i);
            long pid = object.getLong("pid");
            boolean isLike = object.getBoolean("isLike");
            TopicLikeCache topicLikeCache = new TopicLikeCache(pid, isLike);
            topicLikeCaches.add(topicLikeCache);
        }

        Box<TopicPostCache> postBox = ObjectBox.get().boxFor(TopicPostCache.class);
        Box<TopicUserCache> userBox = ObjectBox.get().boxFor(TopicUserCache.class);
        Box<TopicLikeCache> likeBox = ObjectBox.get().boxFor(TopicLikeCache.class);
        if (requestType == Constant.REFRESH_DATA) {
            postBox.removeAll();
            userBox.removeAll();
            likeBox.removeAll();
        }

        postBox.put(topicPostCaches);
        userBox.put(topicUserCaches);
        likeBox.put(topicLikeCaches);
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
        String phone = object.getString("phone");
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

    public static void parseRecommendUsers(String response) {
        JSONObject object = JSON.parseObject(response).getJSONObject("result");
        Box<RecommendUserCache> userCacheBox = ObjectBox.get().boxFor(RecommendUserCache.class);
        userCacheBox.removeAll();
        JSONArray array = object.getJSONArray("users");
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            long id = jsonObject.getLong("id");
            String username = jsonObject.getString("username");
            int followers = jsonObject.getInteger("followers");
            int fans = jsonObject.getInteger("fans");
            String avatar = jsonObject.getString("avatar");
            String time = jsonObject.getString("time");

            RecommendUserCache user = new RecommendUserCache(id, username, followers, fans, avatar, time);
            userCacheBox.put(user);
        }
    }
}
