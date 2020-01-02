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
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.card.InputTraderPasswordActivity;
import com.easylife.house.customer.util.Countdown;
import com.easylife.house.customer.util.ValidatorUtils;
import com.easylife.house.customer.view.ButtonTouch;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.dao.ServerDao.UPDETE_INPUT_TRADE_PASSWORD;
import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_BIND_BANK_CARD;


/**
 * 设置交易密码  验证手机号
 */
public class SetTradePswVerificationCodeActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.etMobileNumberBind)
    EditText etMobileNumberBind;
    @Bind(R.id.etVerification)
    EditText etVerification;
    @Bind(R.id.tvGetVerificationCode)
    TextView tvGetVerificationCode;
    @Bind(R.id.btnBindBankCard)
    ButtonTouch btnBindBankCard;
    private static int mRequestCode;
    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_set_trande_psw, null);
    }


    public static void startActivity(Activity activity, int requestCode) {
        mRequestCode = requestCode;
        Intent intent = new Intent(activity, SetTradePswVerificationCodeActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initView() {
        etMobileNumberBind.setText(mDao.getCustomer().phone);
    }

    @Override
    protected void setActionBarDetail() {

    }

    @OnClick({R.id.tvGetVerificationCode, R.id.btnBindBankCard})
    public void onViewClick(View v) {
        String phone = etMobileNumberBind.getText().toString().trim();
        String verificationCode = etVerification.getText().toString().trim();
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
                mDao.getVerifyCode(1, "10", phone, this);//设置  修改 交易密码之前的验证手机号  type穿10


                break;
            case R.id.btnBindBankCard://下一步

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

                mDao.verification(2, phone, verificationCode, "10", this);
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
                ToastUtils.showShort("验证码验证成功");
                if (!TextUtils.isEmpty(mDao.getCustomer().transactionPassword)) {
                    //修改密码
                    InputTraderPasswordActivity.startActivity(this, UPDETE_INPUT_TRADE_PASSWORD);
                } else {
                    //进入设置交易密码
                    SetTraderPasswordActivity.startActivity(this, mRequestCode);
                }
                finish();
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        ToastUtils.showShort(code.msg);
    }

}
