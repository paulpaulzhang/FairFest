package cn.paulpaulzhang.fair.sc.sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.gyf.immersionbar.ImmersionBar;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.constant.Api;
import cn.paulpaulzhang.fair.constant.Constant;
import cn.paulpaulzhang.fair.net.RestClient;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;
import cn.paulpaulzhang.fair.sc.main.HomeActivity;
import cn.paulpaulzhang.fair.ui.loader.FairLoader;
import cn.paulpaulzhang.fair.util.log.FairLogger;
import cn.paulpaulzhang.fair.util.timer.BaseTimerTask;
import cn.paulpaulzhang.fair.util.timer.ITimerListener;
import es.dmoral.toasty.Toasty;

public class SignUpActivity extends FairActivity implements ITimerListener {
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

    private String sessionId = null;  //验证码返回的session id 注册需要

    @Override
    public int setLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    private boolean checkForm() {

        final String phone = Objects.requireNonNull(mPhone.getText()).toString().trim();
        final String code = Objects.requireNonNull(mCode.getText()).toString().trim();

        boolean isPass = true;
        if (phone.length() != 11 || !Patterns.PHONE.matcher(phone).matches()) {
            mPhone.setError(getString(R.string.error_phone_number));
            isPass = false;
        } else {
            mPhone.setError(null);

        }

        if (code.isEmpty() || !code.matches("^\\d{6}$")) {
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

        if (phone.length() != 11 || !Patterns.PHONE.matcher(phone).matches()) {
            mPhone.setError(getString(R.string.error_phone_number));
        } else {
            mPhone.setError(null);
            FairLoader.showLoading(this);
            RestClient.builder()
                    .url(Api.REGISTER_SMS)
                    .params("phoneNumber", phone)
                    .success(response -> {
                        initTimer();
                        FairLoader.stopLoading();
                        String result = JSON.parseObject(response).getString("result");
                        if (!TextUtils.equals(result, "ok")) {
                            Toasty.info(this, result, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sessionId = JSON.parseObject(response).getString("sessionId");
                    })
                    .error((code, msg) -> {
                        FairLoader.stopLoading();
                        FairLogger.d(code + msg);
                        Toasty.error(this, "发送失败 " + code + " " + msg, Toast.LENGTH_SHORT).show();
                    })
                    .build()
                    .post();
        }
    }

    @OnClick(R2.id.bt_quick_sign_up)
    void quickSignUp() {
        if (checkForm()) {
            final String phone = Objects.requireNonNull(mPhone.getText()).toString().trim();
            final String code = Objects.requireNonNull(mCode.getText()).toString().trim();
            FairLoader.showLoading(this);
            RestClient.builder()
                    .url(Api.REGISTER)
                    .header("JSESSIONID=" + sessionId)
                    .params("phoneNumber", phone)
                    .params("verifyCode", code)
                    .success(response -> SignHandler.onSignUp(response, new ISignUpListener() {
                        @Override
                        public void onSignUpSuccess() {
                            FairLoader.stopLoading();
                            Toasty.success(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            finish();
                        }

                        @Override
                        public void onSignUpFailure(String msg) {
                            FairLoader.stopLoading();
                            Toasty.error(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }))
                    .error((c, m) -> {
                        FairLoader.stopLoading();
                        Toasty.error(this, c + " " + m, Toast.LENGTH_SHORT).show();
                    })
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
            if (mGetCode != null) {
                mGetCode.setText(MessageFormat.format("重新获取{0}s", mCount));
                mGetCode.setEnabled(false);
                mGetCode.setStrokeColorResource(android.R.color.darker_gray);
                mGetCode.setTextColor(getColor(R.color.font_default));
                mGetCode.setTextColor(getColor(android.R.color.darker_gray));
                mCount--;
                if (mCount < 0) {
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                        mGetCode.setText("发送验证码");
                        mGetCode.setStrokeColorResource(R.color.colorAccent);
                        mGetCode.setTextColor(getColor(R.color.colorAccent));
                        mGetCode.setEnabled(true);
                        mCount = 30;
                    }
                }
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
