package com.easylife.house.customer.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.mine.accountsafe.AccountSafeActivity;
import com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity;
import com.easylife.house.customer.ui.mine.card.IDCardIdentificationActivity;
import com.easylife.house.customer.ui.mine.card.MyBankCardActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_IDCARD_IDENTIFICATION;

/**
 * 账户设置
 */
public class AccountSettingsActivity extends BaseActivity {

    @Bind(R.id.tvChecked)
    TextView tvChecked;
    @Bind(R.id.btnExitLogin)
    TextView btnExitLogin;
    @Bind(R.id.iv1)
    ImageView iv1;
    private Customer customer;

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, AccountSettingsActivity.class), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_account_settings, null);
    }

    @Override
    protected void initView() {
        if (mDao.isLogin()) {
            setIdCardNum();
//            tvChecked.setText(TextUtils.isEmpty(identityCardNum) ? "未认证" : identityCardNum);
        }

        btnExitLogin.setVisibility(mDao.isLogin() ? View.VISIBLE : View.GONE);
    }

    private void setIdCardNum() {
        if (!mDao.isLogin()) {
            return;
        }

        customer = mDao.getCustomer();
        String identityCardNum = customer.identityCardNum;
        LogUtils.dTag(TAG, identityCardNum);
        if (!TextUtils.isEmpty(identityCardNum)) {
            StringBuilder stringBuilder = new StringBuilder();
            char c = identityCardNum.charAt(0);
            stringBuilder.append(c);
            stringBuilder.append("****");
            char c1 = identityCardNum.charAt(17);
            stringBuilder.append(c1);
            tvChecked.setText(stringBuilder);
            iv1.setVisibility(View.INVISIBLE);
        } else {
            iv1.setVisibility(View.VISIBLE);
            tvChecked.setText("未认证");
        }
    }

    @Override
    protected void setActionBarDetail() {

    }


    @Override
    public void onResume() {
        super.onResume();
        setIdCardNum();
    }

    @OnClick({R.id.btnCheck, R.id.btnMyCard, R.id.btnAccountSafe, R.id.btnAddressManager, R.id.btnSysSettings, R.id.btnExitLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCheck:
                //    身份认证
                if (!mDao.isLogin()) {
                    startActivity(new Intent(this, LoginByVerifyCodeActivity.class));
                    return;
                }


                if (TextUtils.isEmpty(customer.identityCardNum)) {
                    if (TextUtils.isEmpty(customer.phone)) {//是否绑定手机号
                        new MaterialDialog.Builder(AccountSettingsActivity.this).content("请先绑定手机号").positiveText("确认").negativeText("暂不").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                BindMobileActivity.startActivity(AccountSettingsActivity.this, REQUEST_CDOE_IDCARD_IDENTIFICATION);
                            }
                        }).show();


                    } else {
                        IDCardIdentificationActivity.startActivity(this, REQUEST_CDOE_IDCARD_IDENTIFICATION);
                    }
                }

                break;
            case R.id.btnMyCard:
                //    我的银行卡
                if (!mDao.isLogin()) {
                    startActivity(new Intent(this, LoginByVerifyCodeActivity.class));
                    return;
                }

                startActivity(new Intent(this, MyBankCardActivity.class));
                break;
            case R.id.btnAccountSafe:
                if (!mDao.isLogin()) {
                    startActivity(new Intent(this, LoginByVerifyCodeActivity.class));
                    return;
                }

                startActivity(new Intent(this, AccountSafeActivity.class));

                break;
            case R.id.btnAddressManager:
                if (!mDao.isLogin()) {
                    startActivity(new Intent(this, LoginByVerifyCodeActivity.class));
                    return;
                }
                AddressManagerActivity.startActivity(activity, 0);
                break;
            case R.id.btnSysSettings:
                SettingsActivity.startActivity(activity, 0);
                break;
            case R.id.btnExitLogin:
                SearchSingleton.getIstance().collectList.clear();
                SearchSingleton istance = SearchSingleton.getIstance();
                istance = null;
                mDao.loginOut();
                CustomerUtils.showTip(this, "退出成功");
                finish();
                break;
        }
    }
}
