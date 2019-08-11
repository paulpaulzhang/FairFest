package cn.paulpaulzhang.fair.sc.main.data;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.sunhapper.x.spedit.mention.span.BreakableSpan;

import cn.paulpaulzhang.fair.app.Fair;
import cn.paulpaulzhang.fair.sc.R;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.post.model
 * 创建时间: 8/11/2019
 * 创建人: zlm31
 * 描述:
 */
public class MentionUser implements BreakableSpan {
    private String text;
    private long uid;

    private ForegroundColorSpan styleSpan;
    private Spannable spannableString;

    public MentionUser(String text, long uid) {
        this.text = text;
        this.uid = uid;
    }

    public Spannable getSpannableString() {
        styleSpan = new ForegroundColorSpan(Fair.getApplicationContext().getColor(R.color.colorAccent));
        spannableString = new SpannableString(getDisplayText());
        spannableString.setSpan(styleSpan, 0, spannableString.length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(this, 0, spannableString.length()
                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        return builder.append(spannableString).append(" ");
    }

    public String getDisplayText() {
        return "@" + text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean isBreak(Spannable spannable) {
        int spanStart = spannable.getSpanStart(this);
        int spanEnd = spannable.getSpanEnd(this);
        boolean isBreak = spanStart >= 0 && spanEnd >= 0 &&
                !spannable.subSequence(spanStart, spanEnd).toString().equals(getDisplayText());
        if (isBreak && styleSpan != null) {
            spannable.removeSpan(styleSpan);
            styleSpan = null;
        }
        return isBreak;
    }
}
