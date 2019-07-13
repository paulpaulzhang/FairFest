package cn.paulpaulzhang.fair.sc.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.paulpaulzhang.fair.sc.database.entity.Post;
import cn.paulpaulzhang.fair.sc.database.entity.User;

/**
 * 包名: cn.paulpaulzhang.fair.sc.json
 * 创建时间: 7/13/2019
 * 创建人: zlm31
 * 描述: Json解析工具类
 */
public final class JsonParseUtil {
    public static List<Post> parsePost(String response) {
        List<Post> posts = new ArrayList<>();
        JSONArray array = JSON.parseObject(response).getJSONArray("content");
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            long id = object.getLong("id");
            long uid = object.getLong("uid");
            int type = object.getInteger("type");
            String topic = object.getString("topic");
            String title = object.getString("title");
            String content = object.getString("content");
            String imagesUrl = object.getJSONArray("imagesUrl").toJSONString();
            int likeCount = object.getInteger("likeCount");
            int commentCount = object.getInteger("commentCount");
            int shareCount = object.getInteger("shareCount");
            String time = object.getString("time");
            String device = object.getString("device");

            Post post = new Post(id, uid, type, topic, title,
                    content, imagesUrl, likeCount,
                    commentCount, shareCount, time, device);
            posts.add(post);
        }
        return posts;
    }

    public static User parseUser(String response) {
        JSONObject object = JSON.parseObject(response).getJSONObject("user");
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

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setBirthday(birthday);
        user.setGender(gender);
        user.setFollowers(followers);
        user.setFans(fans);
        user.setSchool(school);
        user.setAvatar(avatar);
        user.setBackground(background);
        user.setIntroduction(introduction);
        return user;
    }
}
