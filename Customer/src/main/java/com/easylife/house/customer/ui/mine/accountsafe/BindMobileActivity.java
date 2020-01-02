package com.easylife.house.customer.ui.mine.accountsafe;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.card.IDCardIdentificationActivity;
import com.easylife.house.customer.ui.mine.card.InputTraderPasswordActivity;
import com.easylife.house.customer.util.Countdown;
import com.easylife.house.customer.util.StatusBarUtils;
import com.easylife.house.customer.util.ValidatorUtils;
import com.easylife.house.customer.view.ButtonTouch;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 绑定手机号
 */
public class BindMobileActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.etMobileNumberBind)
    EditText etMobileNumberBind;
    @Bind(R.id.etVerification)
    EditText etVerification;
    @Bind(R.id.tvGetVerificationCode)
    TextView tvGetVerificationCode;
    @Bind(R.id.btnBindBankCard)
    ButtonTouch btnBindBankCard;
    private String phone;
    private String verificationCode;

    public static final int REQUEST_CDOE_IDCARD_IDENTIFICATION = 110;//身份认证
    public static final int REQUEST_CDOE_SET_TRADE_PSW = 220;//设置交易密码
    public static final int REQUEST_CDOE_BIND_BANK_CARD = 330;//添加银行卡
    public static final int REQUEST_CODE_BIND_USER = 440;//第三方登录绑定手机号
    public static final int REQUEST_CODE_BIND_RECOMMEND = 550;//推荐有礼跳转绑定手机号，绑定完身份变为网络推客
    /**
     * js调 绑定手机号
     */
    private int type;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_bind_mobile, null);
    }

    private static int mRequestCode;

    public static void startActivity(Activity activity, int requestCode) {
        mRequestCode = requestCode;
        Intent intent = new Intent(activity, BindMobileActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void initView() {
    }

    @Override
    protected void setActionBarDetail() {
        findViewById(R.id.content).setFitsSystemWindows(true);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtils.setLightMode(this);
        layActionBar.setBackgroundColor(getResources().getColor(R.color.white));
        tvTitle.setVisibility(View.INVISIBLE);
    }


    @OnClick({R.id.tvGetVerificationCode, R.id.btnBindBankCard})
    public void onViewClick(View v) {
        phone = etMobileNumberBind.getText().toString().trim();
        verificationCode = etVerification.getText().toString().trim();
        etVerification.getText().toString().trim();
        switch (v.getId()) {
            case R.id.tvGetVerificationCode:
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if (!ValidatorUtils.isMobile(phone)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }
                mDao.getVerifyCode(1, ServerDao.TYPE_VERIFYCODE_BIND_PHONE, phone, this);
                break;
            case R.id.btnBindBankCard://提交

                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                if (!ValidatorUtils.isMobile(phone)) {
                    ToastUtils.showShort("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(verificationCode)) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
//                Log.d("type","type:"+type);
//                if (RECOMMEND_BIND_PHONG_FLAG) {
//                    mDao.securityBanded(2, 2, verificationCode, phone, "", 1, this);
//                } else {
//                    mDao.securityBanded(2, 2, verificationCode, phone, "", 0, this);
//                }
                mDao.securityBanded(2, 2, verificationCode, phone, "", 0, this);

//                setResult(RESULT_OK);
//                finish();
                break;
        }
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                ToastUtils.showShort("验证码已发送");
                Countdown countdown = new Countdown(tvGetVerificationCode, "验证码已发送(%ss)", 60);
                countdown.setCountdownListener(new Countdown.CountdownListener() {
                    @Override
                    public void onStart() {
                        Log.d(TAG, "countdown onStart");
                    }

                    @Override
                    public void onFinish() {
                        if (tvGetVerificationCode != null) {
                            tvGetVerificationCode.setText("再次发送");
                        }
                    }

                    @Override
                    public void onUpdate(int currentRemainingSeconds) {

                    }
                });
                countdown.start();

                break;
            case 2:
                //绑定手机号成功  保存到本地
                Customer customer = mDao.getCustomer();
                customer.phone = phone;
                mDao.saveCustomer(customer);
                if (mRequestCode == REQUEST_CDOE_IDCARD_IDENTIFICATION) {//身份认证
                    IDCardIdentificationActivity.startActivity(this, REQUEST_CDOE_IDCARD_IDENTIFICATION);
                } else if (mRequestCode == REQUEST_CDOE_SET_TRADE_PSW) {//设置交易密码
                    IDCardIdentificationActivity.startActivity(this, REQUEST_CDOE_SET_TRADE_PSW);
                } else if (mRequestCode == REQUEST_CDOE_BIND_BANK_CARD) {//添加银行卡
                    if (TextUtils.isEmpty(customer.identityCardNum)) {//是否身份认证
                        IDCardIdentificationActivity.startActivity(this, REQUEST_CDOE_BIND_BANK_CARD);
                    } else {
                        //已身份认证
                        if (!TextUtils.isEmpty(customer.transactionPassword)) {
                            //已经设置了交易密码
                            InputTraderPasswordActivity.startActivity(BindMobileActivity.this, ServerDao.ADD_BANK_CARD_INPUT_TRADE_PASSWORD);
                        } else {
                            // 添加银行卡页面跳转的设置交易密码流程中，不再验证手机号
                            SetTraderPasswordActivity.startActivity(BindMobileActivity.this, REQUEST_CDOE_BIND_BANK_CARD);
                        }
                    }
                } else {
//                    if(RECOMMEND_BIND_PHONG_FLAG){
//                        RECOMMEND_BIND_PHONG_FLAG = false;
//                    }
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        ToastUtils.showShort(code.msg);
    }
}
