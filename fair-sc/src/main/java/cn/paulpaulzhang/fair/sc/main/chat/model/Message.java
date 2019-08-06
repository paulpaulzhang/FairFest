package cn.paulpaulzhang.fair.sc.main.chat.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main
 * 创建时间：8/1/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class Message implements IMessage,
        MessageContentType.Image, /*this is for default image messages implementation*/
        MessageContentType /*and this one is for custom content type (in this case - voice message)*/ {

    private String id;
    private String text;
    private Date createdAt;
    private User user;
    private Image image;
    private Voice voice;
    private cn.jpush.im.android.api.model.Message message;

    public Message(String id, User user, String text, cn.jpush.im.android.api.model.Message message) {
        this(id, user, text, new Date(), message);
    }

    public Message(String id, User user, String text, Date createdAt, cn.jpush.im.android.api.model.Message message) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
        this.message = message;
    }

    public cn.jpush.im.android.api.model.Message getMessage() {
        return message;
    }

    public void setMessage(cn.jpush.im.android.api.model.Message message) {
        this.message = message;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Voice getVoice() {
        return voice;
    }

    public String getStatus() {
        return "Sent";
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }

    public static class Voice {

        private String url;
        private int duration;

        public Voice(String url, int duration) {
            this.url = url;
            this.duration = duration;
        }

        public String getUrl() {
            return url;
        }

        public int getDuration() {
            return duration;
        }
    }
}
