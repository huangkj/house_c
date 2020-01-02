package com.easylife.house.customer.ui.pub.registeraccount;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.AfterTextWatch;
import com.easylife.house.customer.util.SmsButtonUtil;
import com.easylife.house.customer.util.StatusBarUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.EditTextClearAble;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 注册账号
 */
public class RegisterAccountActivity extends MVPBaseActivity<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {

    @Bind(R.id.etUserName)
    EditTextClearAble etUserName;
    @Bind(R.id.etPhoneNumber)
    EditTextClearAble etPhoneNumber;
    @Bind(R.id.tlPhone)
    TextInputLayout tlPhone;
    @Bind(R.id.btnGetVerifyCode)
    TextView btnGetVerifyCode;
    @Bind(R.id.etVerificationCode)
    EditTextClearAble etVerificationCode;
    @Bind(R.id.etPassword)
    EditTextClearAble etPassword;
    @Bind(R.id.cbAgreement)
    CheckBox cbAgreement;
    @Bind(R.id.tvAgreement)
    TextView tvAgreement;
    @Bind(R.id.btnLogin)
    ButtonTouch btnLogin;
    private SmsButtonUtil smsButtonUtil;

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, RegisterAccountActivity.class), requestCode);
    }


    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_register_account, null);
    }


    @Override
    protected void initView() {
        smsButtonUtil = new SmsButtonUtil(btnGetVerifyCode);
        smsButtonUtil.setCountDownText("已发送(%ds)");


        etUserName.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                changeBtnState();
            }
        });

        etPhoneNumber.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                changeBtnState();
                btnGetVerifyCode.setEnabled(!TextUtils.isEmpty(etPhoneNumber.getText().toString()));
            }
        });

        etVerificationCode.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                changeBtnState();
            }
        });

        etPassword.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                changeBtnState();
            }
        });



    }

    private void changeBtnState() {
        btnLogin.setEnabled(checkDataValue(etUserName.getText().toString(), etPhoneNumber.getText().toString(),
                etVerificationCode.getText().toString(), etPassword.getText().toString()));
    }


    public boolean checkDataValue(String name, String phone, String code, String pass) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(pass);
    }

    @Override
    protected void setActionBarDetail() {
        findViewById(R.id.content).setFitsSystemWindows(true);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtils.setLightMode(this);
        actionBar.setBackgroundColor(getResources().getColor(R.color.white));
        tvTitle.setVisibility(View.INVISIBLE);
    }


    @OnClick({R.id.btnGetVerifyCode, R.id.tvAgreement, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetVerifyCode:
                mPresenter.getVerifyCode(etPhoneNumber.getText().toString());
                break;
            case R.id.tvAgreement:
                WebViewActivity.startActivity(activity, "好生活用户协议", Constants.URL_AGREEMENT_REGISTER, "我已阅读");
                break;

            case R.id.btnLogin://注册并登录
                mPresenter.register(etUserName.getText().toString(), etPhoneNumber.getText().toString(), etVerificationCode.getText().toString()
                        , etPassword.getText().toString(), cbAgreement.isChecked());
                break;

        }
    }


    @Override
    public void getVerifyCodeSucc() {
        cancelLoading();
        smsButtonUtil.startCountDown();
    }


    @Override
    public void userInfoSuc() {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGIN_STATE_CHANGE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.HOUSES_INDEXT_COLLECTION_LOGIN));
        dao.localDao.saveIsClear("1");
        showTip("注册成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showTip(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void getVerifyCodeFail(String msg) {

    }

    @Override
    protected void tryRequestData() {

    }


}
