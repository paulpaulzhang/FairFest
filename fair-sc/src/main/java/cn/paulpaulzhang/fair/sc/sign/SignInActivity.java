package cn.paulpaulzhang.fair.sc.sign;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cn.paulpaulzhang.fair.activities.FairActivity;
import cn.paulpaulzhang.fair.sc.R;
import cn.paulpaulzhang.fair.sc.R2;

public class SignInActivity extends FairActivity {
    @BindView(R2.id.tb_sign_in)
    Toolbar mToolbar;
    @BindView(R2.id.et_phone)
    TextInputEditText mPhone;
    @BindView(R2.id.et_code)
    TextInputEditText mCode;
    @BindView(R2.id.bt_get_code)
    MaterialButton mGetCode;
    @BindView(R2.id.bt_sign_in)
    MaterialButton mSignIn;
    @BindView(R2.id.tv_forget_psd)
    TextView mForgetPsd;

    @Override
    public int setLayout() {
        return R.layout.activity_sign_in;
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

        if (code.isEmpty() || code.length() != 4 || !code.matches("[0-9]")) {
            mCode.setError(getString(R.string.error_code));
            isPass = false;
        } else {
            mCode.setError(null);
        }
        return isPass;
    }

    @OnClick(R2.id.bt_get_code)
    void getCode() {
        final String phone = Objects.requireNonNull(mPhone.getText()).toString().trim();

        if (phone.isEmpty() || phone.length() != 11 || !Patterns.PHONE.matcher(phone).matches()) {
            mPhone.setError(getString(R.string.error_phone_number));
        } else {
            mPhone.setError(null);

            //            RestClient.builder()
//                    .url("get_code")
//                    .params("", "")
//                    .success(new ISuccess() {
//                        @Override
//                        public void onSuccess(String response) {
//
//                        }
//                    })
//                    .build()
//                    .post();

            Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R2.id.bt_sign_in)
    void signIn() {
        if (checkForm()) {
            //TODO 登陆逻辑
        }
    }

    @OnClick(R2.id.tv_forget_psd)
    void toForgetPsd() {

    }
}
