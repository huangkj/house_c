package com.easylife.house.customer.ui.mine.accountsafe;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.view.PasswordView;

import butterknife.Bind;

/**
 * 设置交易密码
 */
public class SetTraderPasswordActivity extends BaseActivity {

    @Bind(R.id.passwordViewInput)
    PasswordView passwordViewInput;
    @Bind(R.id.tvForgetPasswordInput)
    TextView tvForgetPasswordInput;
    private static int mRequestCode;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_set_trader_password, null);
    }


    public static void startActivity(Activity activity, int requestCode) {
        mRequestCode = requestCode;
        Intent intent = new Intent(activity, SetTraderPasswordActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void initView() {
        passwordViewInput.setPasswordListener(new PasswordView.PasswordListener() {
            @Override
            public void passwordChange(String changeText) {

            }

            @Override
            public void passwordComplete() {
                String password = passwordViewInput.getPassword();
                ConfirmTradePswActivity.startActivity(SetTraderPasswordActivity.this, password, mRequestCode);
            }

            @Override
            public void keyEnterPress(String password, boolean isComplete) {
            }
        });
    }


    @Override
    protected void setActionBarDetail() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_CANCELED:
                //2次密码不一致
                passwordViewInput.clearPassword();
                break;

            case RESULT_OK:
                setResult(RESULT_OK);
                finish();
                break;
        }

    }
}
