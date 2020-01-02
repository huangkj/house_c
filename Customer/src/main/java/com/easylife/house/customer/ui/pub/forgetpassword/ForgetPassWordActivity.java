package com.easylife.house.customer.ui.pub.forgetpassword;


import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.AfterTextWatch;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.util.SmsButtonUtil;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.EditTextClearAble;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ForgetPassWordActivity extends MVPBaseActivity<ForgetPassWordContract.View, ForgetPassWordPresenter> implements ForgetPassWordContract.View {

    @Bind(R.id.edPhone)
    EditTextClearAble edPhone;
    @Bind(R.id.edVerifyCode)
    EditTextClearAble edVerifyCode;
    @Bind(R.id.btnGetVerifyCode)
    TextView btnGetVerifyCode;
    @Bind(R.id.edPass)
    EditTextClearAble edPass;
    @Bind(R.id.edPassAgain)
    EditTextClearAble edPassAgain;
    @Bind(R.id.btnSubmit)
    ButtonTouch btnSubmit;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_forget_pass, null);
    }

    private SmsButtonUtil smsButtonUtil;

    @Override
    protected void initView() {
        hideNoNetView();
        smsButtonUtil = new SmsButtonUtil(btnGetVerifyCode);
        smsButtonUtil.setCountDownText("已发送(%ds)");

        edPhone.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                changeBtnState();
                btnGetVerifyCode.setEnabled(!TextUtils.isEmpty(edPhone.getText().toString()));
            }
        });
        edVerifyCode.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                changeBtnState();
            }
        });
        edPass.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                changeBtnState();
            }
        });
        edPassAgain.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                changeBtnState();
            }
        });


    }

    private void changeBtnState() {
        btnSubmit.setEnabled(checkDataValue(edPhone.getText().toString(), edVerifyCode.getText().toString(),
                edPass.getText().toString(), edPassAgain.getText().toString()));
    }

    public boolean checkDataValue(String phone, String code, String pass, String passAgain) {
        return !TextUtils.isEmpty(passAgain) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(pass);
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void tryRequestData() {

    }


    @Override
    public void getVerifyCodeFail(String msg) {
        cancelLoading();
        showTip(msg);
    }

    @Override
    public void getVerifyCodeSucc() {
        cancelLoading();
        smsButtonUtil.startCountDown();
    }

    @Override
    public void updatePsw(String phone, String verifyCode, String pass) {
        showLoading();
        mPresenter.update(phone, verifyCode, pass);
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @Override
    public void submitFail(String msg) {
        cancelLoading();
        showTip(msg);
    }

    private PubTipDialog dialog;

    private void showTip() {
        if (dialog == null) {
            dialog = new PubTipDialog(this, new PubTipDialog.InsideListener() {

                @Override
                public void note(boolean isOK) {
                    finish();
                }
            });
        }
        dialog.showDialogUpdatePassWord();
    }

    @Override
    public void submitSucc() {
        setResult(RESULT_OK);
        cancelLoading();
        showTip();
    }

    @OnClick({R.id.btnGetVerifyCode, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetVerifyCode:
                showLoading();
                mPresenter.getVerifyCode(edPhone.getText().toString());
                break;
            case R.id.btnSubmit:
                mPresenter.submit(edPhone.getText().toString(), edVerifyCode.getText().toString(), edPass.getText().toString(), edPassAgain.getText().toString());
                break;
        }
    }
}
