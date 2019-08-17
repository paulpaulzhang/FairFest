package cn.paulpaulzhang.fair.sc.main.user.delegate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import butterknife.BindView;
import cn.paulpaulzhang.fair.delegates.FairDelegate;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.Entity.User;
import cn.paulpaulzhang.fair.util.date.DateUtil;
import cn.paulpaulzhang.fair.util.log.FairLogger;

/**
 * 包名: cn.paulpaulzhang.fair.sc.main.user.delegate
 * 创建时间: 8/13/2019
 * 创建人: zlm31
 * 描述:
 */
public class AboutDelegate extends FairDelegate {
    @BindView(R2.id.tv_level)
    AppCompatTextView mLevel;

    @BindView(R2.id.tv_age)
    AppCompatTextView mAge;

    @BindView(R2.id.tv_gender)
    AppCompatTextView mGender;

    @BindView(R2.id.tv_school)
    AppCompatTextView mSchool;

    @BindView(R2.id.tv_college)
    AppCompatTextView mCollege;

    @BindView(R2.id.tv_introduction)
    AppCompatTextView mIntroduction;

    @Override
    public Object setLayout() {
        return R.layout.delegate_about;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, View view) {

    }

    public void loadUserData(User user) {
        mIntroduction.setText(user.getIntroduction() != null ? user.getIntroduction() : "这个人很懒，什么也没有写");
        if (user.getGender() == null) {
            mGender.setText("");
        } else if (TextUtils.equals(user.getGender(), "男")) {
            mGender.setText("男");
        } else if (TextUtils.equals(user.getGender(), "女")) {
            mGender.setText("女");
        }
        String era = DateUtil.getEra(user.getBirthday());
        String constellation = DateUtil.getConstellation(user.getBirthday());
        mAge.setText(String.format("%s %s", era, constellation));
        mCollege.setText(user.getCollege());
        mSchool.setText(user.getSchool());
        mLevel.setText("Lv.1");
    }
}
