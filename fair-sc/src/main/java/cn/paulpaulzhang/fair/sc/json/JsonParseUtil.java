package cn.paulpaulzhang.fair.sc.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.paulpaulzhang.fair.sc.constant.UserConfigs;
import cn.paulpaulzhang.fair.sc.database.Constant;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.entity.Like;
import cn.paulpaulzhang.fair.sc.database.entity.LocalUser;
import cn.paulpaulzhang.fair.sc.database.entity.Post;
import cn.paulpaulzhang.fair.sc.database.entity.Topic;
import cn.paulpaulzhang.fair.sc.database.entity.User;
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
        List<Post> posts = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<Like> likes = new ArrayList<>();
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
            Date time = object.getDate("time");
            String device = object.getString("device");
            Post post = new Post(id, uid, type, title, content, imagesUrl,
                    likeCount, commentCount, shareCount, time, device);
            posts.add(post);
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
            Date time = object.getDate("time");

            User user = new User(id, username, birthday, gender, followers,
                    fans, school, introduction, avatar, background, time);
            users.add(user);
        }

        for (int i = 0; i < likeArr.size(); i++) {
            JSONObject object = likeArr.getJSONObject(i);
            long pid = object.getLong("pid");
            boolean isLike = object.getBoolean("like");
            Like like = new Like(pid, isLike);
            likes.add(like);
        }

        Box<Post> postBox = ObjectBox.get().boxFor(Post.class);
        Box<User> userBox = ObjectBox.get().boxFor(User.class);
        Box<Like> likeBox = ObjectBox.get().boxFor(Like.class);
        if (requestType == Constant.REFRESH_DATA) {
            postBox.removeAll();
            userBox.removeAll();
            likeBox.removeAll();
        }
        postBox.put(posts);
        userBox.put(users);
        likeBox.put(likes);
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
        Date time = object.getDate("time");
        String features = object.getString("features");

        LocalUser user = new LocalUser(id, username, password, birthday, gender,
                followers, fans, phone, email, school, studentId,
                permission, introduction, avatar, background, time, features);
        Box<LocalUser> localUserBox = ObjectBox.get().boxFor(LocalUser.class);
        localUserBox.remove(FairPreference.getCustomAppProfileL(UserConfigs.CURRENT_USER_ID.name()));
        localUserBox.put(user);
    }

    public static void parseTopic(String response) {
        JSONArray array = JSON.parseArray("result");
        List<Topic> topics = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            String name = object.getString("name");
            int follow = object.getInteger("follow");
            int post = object.getInteger("post");
            topics.add(new Topic(name, follow, post));
        }

        Box<Topic> topicBox = ObjectBox.get().boxFor(Topic.class);
        topicBox.put(topics);
    }
}
