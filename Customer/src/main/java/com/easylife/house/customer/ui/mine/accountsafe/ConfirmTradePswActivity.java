package com.easylife.house.customer.ui.mine.accountsafe;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.card.BindBankCardActivity;
import com.easylife.house.customer.ui.mine.card.InputTraderPasswordActivity;
import com.easylife.house.customer.util.Countdown;
import com.easylife.house.customer.util.DES;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.PasswordView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.SET_TRADE_PASSWORD_SUCC;
import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_BIND_BANK_CARD;


/**
 * 设置交易密码 再次输入
 */
public class ConfirmTradePswActivity extends BaseActivity {

    @Bind(R.id.passwordViewInput)
    PasswordView passwordViewInput;
    @Bind(R.id.tvForgetPasswordInput)
    TextView tvForgetPasswordInput;
    @Bind(R.id.btnConfirm)
    ButtonTouch btnConfirm;
    /**
     * 上个页面输入的密码
     */
    private String prePsw;
    private static int mRequestCode;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_confirm_trade_psw, null);
    }

    public static void startActivity(Activity activity, String prePsw, int requestCode) {
        mRequestCode = requestCode;
        Intent intent = new Intent(activity, ConfirmTradePswActivity.class);
        intent.putExtra("prePsw", prePsw);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void initView() {
        prePsw = getIntent().getStringExtra("prePsw");
        passwordViewInput.setPasswordListener(new PasswordView.PasswordListener() {
            @Override
            public void passwordChange(String changeText) {

            }

            @Override
            public void passwordComplete() {

            }

            @Override
            public void keyEnterPress(String password, boolean isComplete) {
            }
        });
    }


    @Override
    protected void setActionBarDetail() {

    }


    @OnClick({R.id.btnConfirm})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                final String password = passwordViewInput.getPassword();
                if (password.length() == 6) {
                    if (password.equals(prePsw)) {
                        //设置交易密码
                        final String enPsw = DES.encryptDES(password);
                        LogUtils.dTag(TAG, enPsw);
                        mDao.setTradePassword(0, enPsw, new RequestManagerImpl() {
                            @Override
                            public void onSuccess(String response, int requestType) {
                                ToastUtils.showShort("设置交易密码成功");
                                Customer customer = mDao.getCustomer();
                                customer.transactionPassword = enPsw;
                                mDao.saveCustomer(customer);
                                EventBus.getDefault().post(new MessageEvent(SET_TRADE_PASSWORD_SUCC));

                                if (mRequestCode == REQUEST_CDOE_BIND_BANK_CARD) {
                                    BindBankCardActivity.startActivity(ConfirmTradePswActivity.this, 0);//添加银行卡
                                }


                                setResult(RESULT_OK);
                                finish();

                            }


                            @Override
                            public void onFail(NetBaseStatus code, int requestType) {
                                ToastUtils.showShort(code.msg);
                            }
                        });
                    } else {
                        //2次不一致
                        ToastUtils.showLong("2次密码不一致，请重新设置");
                        setResult(RESULT_CANCELED);
                        finish();

                    }
                }


                break;
        }
    }

}
