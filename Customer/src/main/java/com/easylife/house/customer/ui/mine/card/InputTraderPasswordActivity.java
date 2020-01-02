package com.easylife.house.customer.ui.mine.card;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BankCardListBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.accountsafe.SetTraderPasswordActivity;
import com.easylife.house.customer.ui.mine.accountsafe.TradeForgetPswActivity;
import com.easylife.house.customer.util.DES;
import com.easylife.house.customer.view.PasswordView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.dao.ServerDao.ADD_BANK_CARD_INPUT_TRADE_PASSWORD;
import static com.easylife.house.customer.dao.ServerDao.BROKER_INPUT_TRADE_PASSWORD;
import static com.easylife.house.customer.dao.ServerDao.UNBIND_BANK_CARD_INPUT_TRADE_PASSWORD;
import static com.easylife.house.customer.dao.ServerDao.UPDETE_INPUT_TRADE_PASSWORD;
import static com.easylife.house.customer.event.MessageEvent.CHECK_TRADE_PASSWORD;
import static com.easylife.house.customer.event.MessageEvent.REFRESH_BANK_CARD;
import static com.easylife.house.customer.event.MessageEvent.SET_TRADE_PASSWORD_SUCC;

/**
 * 输入交易密码
 */
public class InputTraderPasswordActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.passwordViewInput)
    PasswordView passwordViewInput;
    @Bind(R.id.tvForgetPasswordInput)
    TextView tvForgetPasswordInput;
    @Bind(R.id.tvTip)
    TextView tvTip;
    private int requestCode;
    private BankCardListBean bankCardItem;


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_input_trader_password, null);
    }


    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, InputTraderPasswordActivity.class);
        intent.putExtra("requestCode", requestCode);
        activity.startActivityForResult(intent, requestCode);
    }


    public static void startActivity(Activity activity, BankCardListBean bankCardItem, int requestCode) {
        Intent intent = new Intent(activity, InputTraderPasswordActivity.class);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("bankCardItem", bankCardItem);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        requestCode = getIntent().getIntExtra("requestCode", 0);
        bankCardItem = (BankCardListBean) getIntent().getSerializableExtra("bankCardItem");


        if (UPDETE_INPUT_TRADE_PASSWORD == requestCode) {
            //修改密码
            tvTip.setText("请输入当前交易密码");
        } else {
            tvTip.setText("请输入6位数字交易密码");
        }

        passwordViewInput.setPasswordListener(new PasswordView.PasswordListener() {
            @Override
            public void passwordChange(String changeText) {

            }

            @Override
            public void passwordComplete() {
                String password = passwordViewInput.getPassword();
                checkPassword(password);
            }

            @Override
            public void keyEnterPress(String password, boolean isComplete) {

            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent event) {
        switch (event.MsgType) {
            case SET_TRADE_PASSWORD_SUCC:
                passwordViewInput.clearPassword();
                break;
        }
    }


    private void checkPassword(String password) {
        password = DES.encryptDES(password);
        mDao.checkTradePssword(0, password, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                if (requestCode == ADD_BANK_CARD_INPUT_TRADE_PASSWORD) {
                    BindBankCardActivity.startActivity(InputTraderPasswordActivity.this, 0);//从添加银行卡来的
                    setResult(RESULT_OK);
                    finish();
                } else if (UNBIND_BANK_CARD_INPUT_TRADE_PASSWORD == requestCode) {
                    //解绑银行卡
                    mDao.bankCardDel(1, bankCardItem.getId(), InputTraderPasswordActivity.this);
                } else if (UPDETE_INPUT_TRADE_PASSWORD == requestCode) {
                    //修改交易密码场景,下一步跳转设置密码
                    //进入设置交易密码
                    startActivity(new Intent(InputTraderPasswordActivity.this, SetTraderPasswordActivity.class));
                    finish();
                } else if (BROKER_INPUT_TRADE_PASSWORD == requestCode) {
                    //申请佣金
                    EventBus.getDefault().post(new MessageEvent(CHECK_TRADE_PASSWORD));
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                ToastUtils.showShort(code.msg);
            }
        });
    }


    @OnClick({R.id.tvForgetPasswordInput})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tvForgetPasswordInput://忘记密码
                TradeForgetPswActivity.startActivity(this, 0);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void setActionBarDetail() {

    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                ToastUtils.showShort("解绑成功");
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
