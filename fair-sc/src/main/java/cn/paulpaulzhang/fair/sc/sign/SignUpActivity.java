package cn.paulpaulzhang.fair.sc.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.orhanobut.logger.Logger;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.app.AccountManager;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.net.RestClientBuilder;
import cn.paulpaulzhang.fair.net.callback.ISuccess;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.database.ObjectBox;
import cn.paulpaulzhang.fair.sc.database.User;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.timer.BaseTimerTask;
import cn.paulpaulzhang.fair.util.timer.ITimerListener;
import io.objectbox.Box;

public class SignUpActivity extends FairActivity implements ITimerListener {
    @BindView(R2.id.tb_sign_up)
    Toolbar mToolbar;
    @BindView(R2.id.et_phone)
    TextInputEditText mPhone;
    @BindView(R2.id.et_code)
    TextInputEditText mCode;
    @BindView(R2.id.bt_get_code)
    MaterialButton mGetCode;
    @BindView(R2.id.bt_quick_sign_up)
    MaterialButton mQuickSignUp;
    @BindView(R2.id.tv_sign_in)
    TextView mSignIn;

    private Timer mTimer = null;
    private int mCount = 30;

    @Override
    public int setLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
    }

    private boolean checkForm() {
        //TODO 电话号码正则  验证码位数验证(暂定四位数)

        final String phone = Objects.requireNonNull(mPhone.getText()).toString().trim();
        final String code = Objects.requireNonNull(mCode.getText()).toString().trim();

        boolean isPass = true;
        if (phone.isEmpty() || phone.length() != 11 || !Patterns.PHONE.matcher(phone).matches()) {
            mPhone.setError(getString(R.string.error_phone_number));
            isPass = false;
        } else {
            mPhone.setError(null);

        }

        if (code.isEmpty() || code.length() != 4) {
            mCode.setError(getString(R.string.error_code));
            isPass = false;
        } else {
            mCode.setError(null);
        }
        return isPass;
    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);

    }

    @OnClick(R2.id.bt_get_code)
    void getCode() {
        final String phone = Objects.requireNonNull(mPhone.getText()).toString().trim();

        if (phone.isEmpty() || phone.length() != 11 || !Patterns.PHONE.matcher(phone).matches()) {
            mPhone.setError(getString(R.string.error_phone_number));
        } else {
            mPhone.setError(null);

            RestClient.builder()
                    .url("get_code")
                    .params("phone", phone)
                    .success(response -> {
                        initTimer();
                        Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    })
                    .error((code, msg) -> {
                        initTimer();
                        Toast.makeText(this, "发送失败 " + msg, Toast.LENGTH_SHORT).show();
                    })
                    .build()
                    .post();
        }
    }

    @OnClick(R2.id.bt_quick_sign_up)
    void quickSignUp() {
        if (checkForm()) {
            //TODO 注册逻辑
            final String phone = Objects.requireNonNull(mPhone.getText()).toString().trim();
            final String code = Objects.requireNonNull(mCode.getText()).toString().trim();
            RestClient.builder()
                    .url("sign_up")
                    .params("phone", phone)
                    .params("code", code)
                    .success(response -> {
                        FairLogger.json("USER", response);
                        SignHandler.onSignUp(response, () -> {
                            //TODO 跳转逻辑
                            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .error((c, m) -> Toast.makeText(this, c + " " + m, Toast.LENGTH_SHORT).show())
                    .build()
                    .post();
        }
    }

    @OnClick(R2.id.tv_sign_in)
    void toSignIn() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    @Override
    public void onTimer() {
        runOnUiThread(() -> {
            assert mGetCode != null;
            mGetCode.setText(MessageFormat.format("重新获取{0}s", mCount));
            mGetCode.setEnabled(false);
            mGetCode.setTextColor(getColor(android.R.color.darker_gray));
            mCount--;
            if (mCount < 0) {
                assert mTimer != null;
                mTimer.cancel();
                mTimer = null;
                mGetCode.setText("发送验证码");
                mGetCode.setTextColor(getColor(R.color.colorAccent));
                mGetCode.setEnabled(true);
                mCount = 30;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}