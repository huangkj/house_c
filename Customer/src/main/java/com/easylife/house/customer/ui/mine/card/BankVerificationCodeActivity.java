package com.easylife.house.customer.ui.mine.card;

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
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.Countdown;
import com.easylife.house.customer.view.ButtonTouch;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.REFRESH_BANK_CARD;

/**
 * 绑定银行卡 获取验证码 hkj
 */
public class BankVerificationCodeActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.etVerification)
    EditText etVerification;
    @Bind(R.id.tvGetVerificationCode)
    TextView tvGetVerificationCode;
    @Bind(R.id.btnBindBankCard)
    ButtonTouch btnBindBankCard;
    private String userName;
    private String bankCardNumber;
    private String createBank;
    private String idCardNumber;
    private String openBranchBank;
    private String bankBranchNum;
    private String bankPhoneNumber;
    private String bankImgFont;
    private String bankImgBack;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_bank_verification_code, null);
    }

    public static void startActivity(Activity activity, String userName, String bankCardNumber, String createBank, String idCardNumber,
                                     String openBranchBank, String bankBranchNum, String bankPhoneNumber, String bankImgFont, String bankImgBack,
                                     int requestCode) {
        Intent intent = new Intent(activity, BankVerificationCodeActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("bankCardNumber", bankCardNumber);
        intent.putExtra("createBank", createBank);
        intent.putExtra("idCardNumber", idCardNumber);
        intent.putExtra("openBranchBank", openBranchBank);
        intent.putExtra("bankBranchNum", bankBranchNum);
        intent.putExtra("bankPhoneNumber", bankPhoneNumber);
        intent.putExtra("bankImgFont", bankImgFont);
        intent.putExtra("bankImgBack", bankImgBack);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void initView() {
        Intent getIntent = getIntent();

        userName = getIntent.getStringExtra("userName");
        bankCardNumber = getIntent.getStringExtra("bankCardNumber");
        createBank = getIntent.getStringExtra("createBank");
        idCardNumber = getIntent.getStringExtra("idCardNumber");
        openBranchBank = getIntent.getStringExtra("openBranchBank");
        bankBranchNum = getIntent.getStringExtra("bankBranchNum");
        bankPhoneNumber = getIntent.getStringExtra("bankPhoneNumber");
        bankImgFont = getIntent.getStringExtra("bankImgFont");
        bankImgBack = getIntent.getStringExtra("bankImgBack");
    }

    @Override
    protected void setActionBarDetail() {

    }


    @OnClick({R.id.tvGetVerificationCode, R.id.btnBindBankCard})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetVerificationCode:
                mDao.getVerifyCode(1, "11", bankPhoneNumber, this);
                break;

            case R.id.btnBindBankCard://提交
                String verificationCode = etVerification.getText().toString().trim();
                if (TextUtils.isEmpty(verificationCode)) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                mDao.verification(2, bankPhoneNumber, verificationCode, "11", this);
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

            case 2://验证验证码成功
                mDao.addBankCard(3, userName, bankCardNumber, createBank, idCardNumber, openBranchBank, bankBranchNum, bankPhoneNumber, bankImgFont, bankImgBack, this);
                break;
            case 3:
                ToastUtils.showShort("绑定成功");
                EventBus.getDefault().post(new MessageEvent(REFRESH_BANK_CARD));
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        ToastUtils.showShort(code.msg);
    }
}
