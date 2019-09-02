package cn.paulpaulzhang.fair.util.text;

/**
 * 包名: cn.paulpaulzhang.fair.util.text
 * 创建时间: 8/20/2019
 * 创建人: zlm31
 * 描述:
 */
public class TextUtil {
    public static String textHightLightTopic(String text) {
        char brackets = '[';
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '#' && brackets == '[') {
                builder.append(brackets).append(c);
                brackets = ']';
            } else if (c == '#') {
                builder.append(c).append(brackets).append("()");
                brackets = '[';
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        String s = textHightLightTopic("#你好啊# 啊#啊#啊啊");
        System.out.println(s);
    }

    public static String getTopicName(String topic) {
        return topic.substring(1, topic.length() - 1);
    }
}
