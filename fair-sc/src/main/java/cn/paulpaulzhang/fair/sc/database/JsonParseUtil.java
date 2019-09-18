package cn.paulpaulzhang.fair.sc.database;

import android.text.TextUtils;
import android.util.ArraySet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.paulpaulzhang.fair.constant.UserConfigs;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryLikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryPostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.DiscoveryUserCache;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowLikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowPostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.FollowUserCache;
import cn.paulpaulzhang.fair.sc.database.Entity.LikeCache;
import cn.paulpaulzhang.fair.sc.database.Entity.Major;
import cn.paulpaulzhang.fair.sc.database.Entity.PostCache;
import cn.paulpaulzhang.fair.sc.database.Entity.ProductCache;
import cn.paulpaulzhang.fair.sc.database.Entity.School;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.sc.database.Entity.RecommendUserCache;
import cn.paulpaulzhang.fair.sc.database.Entity.TopicCache;
import cn.paulpaulzhang.fair.sc.database.Entity.UserCache;
import cn.paulpaulzhang.fair.sc.main.interest.model.Discovery;
import cn.paulpaulzhang.fair.util.file.FileUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.storage.FairPreference;
import cn.paulpaulzhang.fair.util.text.TextUtil;
import io.objectbox.Box;

/**
 * 包名: cn.paulpaulzhang.fair.sc.json
 * 创建时间: 7/13/2019
 * 创建人: zlm31
 * 描述: Json解析工具类
 */
public final class JsonParseUtil {
    public static void parseDiscoveryPost(String response, int requestType) {
        List<DiscoveryPostCache> postCaches = new ArrayList<>();
        List<DiscoveryUserCache> userCaches = new ArrayList<>();
        List<DiscoveryLikeCache> likeCaches = new ArrayList<>();

        String result = JSON.parseObject(response).getString("result");
        if (TextUtils.equals(result, "未知错误")) {
            return;
        }

        Map<String, Object> postMap = JSON.parseObject(response).getJSONObject("post").getInnerMap();
        if (postMap != null) {
            for (String key : postMap.keySet()) {
                JSONObject object = (JSONObject) postMap.get(key);
                if (object != null) {
                    long id = object.getLongValue("pid");
                    long uid = object.getLongValue("uid");
                    int type = object.getIntValue("type");
                    String title = object.getString("title");
                    String content = object.getString("content");
                    String imagesUrl = object.getJSONObject("imagesUrl").toJSONString();
                    int likeCount = object.getIntValue("likeCount");
                    int commentCount = object.getIntValue("commentCount");
                    int shareCount = object.getIntValue("shareCount");
                    long time = object.getLongValue("time");
                    String device = object.getString("device");
                    DiscoveryPostCache postCache = new DiscoveryPostCache(id, uid, type, title, content, imagesUrl,
                            likeCount, commentCount, shareCount, time, device);
                    postCaches.add(postCache);
                }

            }
        }

        Map<String, Object> userMap = JSON.parseObject(response).getJSONObject("user").getInnerMap();
        if (userMap != null) {
            for (String key : userMap.keySet()) {
                JSONObject object = (JSONObject) userMap.get(key);
                if (object != null) {
                    long uid = object.getLongValue("uid");
                    String username = object.getString("username");
                    String avatar = object.getString("avatar");

                    DiscoveryUserCache userCache = new DiscoveryUserCache(uid, username, avatar);
                    userCaches.add(userCache);
                }

            }
        }

        Map<String, Object> isLikeMap = JSON.parseObject(response).getJSONObject("isLike").getInnerMap();
        if (isLikeMap != null) {
            for (String key : isLikeMap.keySet()) {
                boolean isLike = (boolean) isLikeMap.get(key);
                long pid = Long.parseLong(key);
                DiscoveryLikeCache likeCache = new DiscoveryLikeCache(pid, isLike);
                likeCaches.add(likeCache);
            }
        }

        Box<DiscoveryPostCache> postBox = ObjectBox.get().boxFor(DiscoveryPostCache.class);
        Box<DiscoveryUserCache> userBox = ObjectBox.get().boxFor(DiscoveryUserCache.class);
        Box<DiscoveryLikeCache> likeBox = ObjectBox.get().boxFor(DiscoveryLikeCache.class);
        if (requestType == Constant.REFRESH_DATA) {
            postBox.removeAll();
            userBox.removeAll();
            likeBox.removeAll();
        }
        postBox.put(postCaches);
        userBox.put(userCaches);
        likeBox.put(likeCaches);
    }

    public static void parseFollowPost(String response, int requestType) {
        List<FollowPostCache> postCaches = new ArrayList<>();
        List<FollowUserCache> userCaches = new ArrayList<>();
        List<FollowLikeCache> likeCaches = new ArrayList<>();

        String result = JSON.parseObject(response).getString("result");
        if (TextUtils.equals(result, "未知错误")) {
            FairLogger.d("FOLLOW-POST", "未知错误");
            return;
        }

        Map<String, Object> postMap = JSON.parseObject(response).getJSONObject("post").getInnerMap();
        if (postMap != null) {
            for (String key : postMap.keySet()) {
                JSONObject object = (JSONObject) postMap.get(key);
                if (object != null) {
                    long id = object.getLongValue("pid");
                    long uid = object.getLongValue("uid");
                    int type = object.getIntValue("type");
                    String title = object.getString("title");
                    String content = object.getString("content");
                    String imagesUrl = object.getJSONObject("imagesUrl").toJSONString();
                    int likeCount = object.getIntValue("likeCount");
                    int commentCount = object.getIntValue("commentCount");
                    int shareCount = object.getIntValue("shareCount");
                    long time = object.getLongValue("time");
                    String device = object.getString("device");
                    FollowPostCache postCache = new FollowPostCache(id, uid, type, title, content, imagesUrl,
                            likeCount, commentCount, shareCount, time, device);
                    postCaches.add(postCache);
                }
            }
        }

        Map<String, Object> userMap = JSON.parseObject(response).getJSONObject("user").getInnerMap();
        if (userMap != null) {
            for (String key : userMap.keySet()) {
                JSONObject object = (JSONObject) userMap.get(key);
                if (object != null) {
                    long uid = object.getLongValue("uid");
                    String username = object.getString("username");
                    String avatar = object.getString("avatar");

                    FollowUserCache userCache = new FollowUserCache(uid, username, avatar);
                    userCaches.add(userCache);
                }
            }
        }

        Map<String, Object> isLikeMap = JSON.parseObject(response).getJSONObject("isLike").getInnerMap();
        if (isLikeMap != null) {
            for (String key : isLikeMap.keySet()) {
                boolean isLike = (boolean) isLikeMap.get(key);
                long pid = Long.parseLong(key);
                FollowLikeCache likeCache = new FollowLikeCache(pid, isLike);
                likeCaches.add(likeCache);
            }
        }

        Box<FollowPostCache> postBox = ObjectBox.get().boxFor(FollowPostCache.class);
        Box<FollowUserCache> userBox = ObjectBox.get().boxFor(FollowUserCache.class);
        Box<FollowLikeCache> likeBox = ObjectBox.get().boxFor(FollowLikeCache.class);
        if (requestType == Constant.REFRESH_DATA) {
            postBox.removeAll();
            userBox.removeAll();
            likeBox.removeAll();
        }
        postBox.put(postCaches);
        userBox.put(userCaches);
        likeBox.put(likeCaches);
    }

    public static void parsePost(String response, int requestType) {
        List<PostCache> postCaches = new ArrayList<>();
        List<UserCache> userCaches = new ArrayList<>();
        List<LikeCache> likeCaches = new ArrayList<>();

        String result = JSON.parseObject(response).getString("result");
        if (TextUtils.equals(result, "未知错误")) {
            return;
        }

        Map<String, Object> postMap = JSON.parseObject(response).getJSONObject("post").getInnerMap();
        if (postMap != null) {
            for (String key : postMap.keySet()) {
                JSONObject object = (JSONObject) postMap.get(key);
                if (object != null) {
                    long id = object.getLongValue("pid");
                    long uid = object.getLongValue("uid");
                    int type = object.getIntValue("type");
                    String title = object.getString("title");
                    String content = object.getString("content");
                    String imagesUrl = object.getJSONObject("imagesUrl").toJSONString();
                    int likeCount = object.getIntValue("likeCount");
                    int commentCount = object.getIntValue("commentCount");
                    int shareCount = object.getIntValue("shareCount");
                    long time = object.getLongValue("time");
                    String device = object.getString("device");
                    PostCache postCache = new PostCache(id, uid, type, title, content, imagesUrl,
                            likeCount, commentCount, shareCount, time, device);
                    postCaches.add(postCache);
                }
            }
        }

        Map<String, Object> userMap = JSON.parseObject(response).getJSONObject("user").getInnerMap();
        if (userMap != null) {
            for (String key : userMap.keySet()) {
                JSONObject object = (JSONObject) userMap.get(key);
                if (object != null) {
                    long uid = object.getLongValue("uid");
                    String username = object.getString("username");
                    String avatar = object.getString("avatar");
                    UserCache userCache = new UserCache(uid, username, avatar);
                    userCaches.add(userCache);
                }
            }
        }

        Map<String, Object> isLikeMap = JSON.parseObject(response).getJSONObject("isLike").getInnerMap();
        if (isLikeMap != null) {
            for (String key : isLikeMap.keySet()) {
                boolean isLike = (boolean) isLikeMap.get(key);
                long pid = Long.parseLong(key);
                LikeCache likeCache = new LikeCache(pid, isLike);
                likeCaches.add(likeCache);
            }
        }

        Box<PostCache> postBox = ObjectBox.get().boxFor(PostCache.class);
        Box<UserCache> userBox = ObjectBox.get().boxFor(UserCache.class);
        Box<LikeCache> likeBox = ObjectBox.get().boxFor(LikeCache.class);
        if (requestType == Constant.REFRESH_DATA) {
            postBox.removeAll();
            userBox.removeAll();
            likeBox.removeAll();
        }
        postBox.put(postCaches);
        userBox.put(userCaches);
        likeBox.put(likeCaches);
    }

    public static void parseUser(String response) {
        JSONObject object = JSON.parseObject(response).getJSONObject("user");

        long id = object.getLongValue("uid");
        String username = object.getString("username");
        String password = object.getString("password");
        long birthday = object.getLongValue("birthday");
        String gender = object.getString("gender");
        int followers = object.getIntValue("followers");
        int fans = object.getIntValue("fans");
        int dynamicCount = object.getIntValue("dynamicCount");
        String phone = object.getString("phone");
        String email = object.getString("email");
        String school = object.getString("school");
        String college = object.getString("college");
        long studentId = object.getLongValue("studentId");
        int permission = object.getIntValue("permission");
        String introduction = object.getString("introduction");
        String avatar = object.getString("avatar");
        String background = object.getString("background");
        long time = object.getLongValue("time");
        String features = object.getString("features");

        User user = new User(id, username, password, birthday, gender,
                followers, fans, dynamicCount, phone, email, school, college, studentId,
                permission, introduction, avatar, background, time, features);
        Box<User> localUserBox = ObjectBox.get().boxFor(User.class);
        localUserBox.remove(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        localUserBox.put(user);
    }

    public static void parseTopic(String response, int type) {
        JSONArray array = JSON.parseArray(response);
        List<TopicCache> topicCaches = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            long id = object.getLong("tid");
            String name = object.getString("tname");
            String img = object.getString("imageUrl");
            int follow = object.getIntValue("payCount");
            int post = object.getIntValue("postCount");
            topicCaches.add(new TopicCache(id, name, img, follow, post));
        }

        Box<TopicCache> topicBox = ObjectBox.get().boxFor(TopicCache.class);
        if (type == Constant.REFRESH_DATA) {
            topicBox.removeAll();
        }
        topicBox.put(topicCaches);
    }

    public static ArrayList<String> parseImgs(String imgUrl) {
        Map<String, Object> map = JSON.parseObject(imgUrl).getJSONObject("images").getInnerMap();
        ArrayList<String> imgs = new ArrayList<>();
        for (String key : map.keySet()) {
            imgs.add((String) map.get(key));
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
            long id = jsonObject.getLongValue("id");
            String username = jsonObject.getString("username");
            int followers = jsonObject.getIntValue("followers");
            int fans = jsonObject.getIntValue("fans");
            String avatar = jsonObject.getString("avatar");
            long time = jsonObject.getLongValue("time");

            RecommendUserCache user = new RecommendUserCache(id, username, followers, fans, avatar, time);
            userCacheBox.put(user);
        }
    }

    public static void parseSchoolList(String json) {
        Set<String> schoolSet = new ArraySet<>();
        JSONArray schoolList = JSON.parseObject(json).getJSONArray("schoolList");
        for (int i = 0; i < schoolList.size(); i++) {
            JSONObject schoolsObject = schoolList.getJSONObject(i);
            JSONArray schools = schoolsObject.getJSONArray("school");
            for (int j = 0; j < schools.size(); j++) {
                JSONObject schoolObject = schools.getJSONObject(j);
                String name = schoolObject.getString("name");
                schoolSet.add(name);
            }
        }

        Box<School> schoolBox = ObjectBox.get().boxFor(School.class);
        for (String str : schoolSet) {
            schoolBox.put(new School(str));
        }
    }

    public static void parseMajorList(String json) {
        Set<String> majorSet = new ArraySet<>();
        JSONArray majorList = JSON.parseObject(json).getJSONArray("majorList");
        for (int i = 0; i < majorList.size(); i++) {
            JSONObject majorsObject = majorList.getJSONObject(i);
            JSONArray majors = majorsObject.getJSONArray("class");
            for (int j = 0; j < majors.size(); j++) {
                String name = majors.getJSONObject(j).getString("name");
                majorSet.add(name);
            }
        }

        Box<Major> majorBox = ObjectBox.get().boxFor(Major.class);
        for (String str : majorSet) {
            majorBox.put(new Major(str));
        }
    }

    public static void parseProduct(String response, int type) {
        JSONObject object = JSON.parseObject(response);
        String result = object.getString("result");
        if (!TextUtils.equals(result, "ok")) {
            return;
        }
        JSONArray array = object.getJSONArray("storeList");
        List<ProductCache> productCaches = new ArrayList<>();
        Box<ProductCache> productCacheBox = ObjectBox.get().boxFor(ProductCache.class);
        for (int i = 0; i < array.size(); i++) {
            JSONObject product = array.getJSONObject(i);
            long sid = product.getLongValue("sid");
            String sname = product.getString("sname");
            String headImg = product.getString("headImg");
            String overview = product.getString("overview");
            float price = product.getFloatValue("price");
            long uid = product.getLongValue("uid");
            long time = product.getLongValue("time");
            int isSold = product.getIntValue("issold");
            productCaches.add(new ProductCache(sid, uid, time, sname, headImg, overview, price, isSold));
        }
        if (type == Constant.REFRESH_DATA) {
            productCacheBox.removeAll();
        }
        productCacheBox.put(productCaches);
    }
}
