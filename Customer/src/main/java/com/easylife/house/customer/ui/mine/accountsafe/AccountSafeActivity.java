package com.easylife.house.customer.ui.mine.accountsafe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.card.IDCardIdentificationActivity;
import com.easylife.house.customer.ui.pub.updatepassword.UpdatePassWordActivity;
import com.easylife.house.customer.util.LogOut;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CDOE_SET_TRADE_PSW;

public class AccountSafeActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.relTradePswAccountSafe)
    RelativeLayout relTradePswAccountSafe;
    @Bind(R.id.relchangePswAccountSafe)
    RelativeLayout relchangePswAccountSafe;
    @Bind(R.id.ivMobileNumberAccountSafe)
    ImageView ivMobileNumberAccountSafe;
    @Bind(R.id.tvMobileNumberAccountSafe)
    TextView tvMobileNumberAccountSafe;
    @Bind(R.id.relMobileNumberAccountSafe)
    RelativeLayout relMobileNumberAccountSafe;
    @Bind(R.id.ivWechatAccountSafe)
    ImageView ivWechatAccountSafe;
    @Bind(R.id.tvWechatAccountSafe)
    TextView tvWechatAccountSafe;
    @Bind(R.id.relWechatAccountSafe)
    RelativeLayout relWechatAccountSafe;
    @Bind(R.id.ivQQAccountSafe)
    ImageView ivQQAccountSafe;
    @Bind(R.id.tvQQAccountSafe)
    TextView tvQQAccountSafe;
    @Bind(R.id.relQQAccountSafe)
    RelativeLayout relQQAccountSafe;
    private Customer customer;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_account_safe, null);
    }

    @Override
    protected void initView() {
        customer = mDao.getCustomer();
        tvMobileNumberAccountSafe.setText(customer.phone);

        if (!TextUtils.isEmpty(customer.phone)) {
            tvMobileNumberAccountSafe.setText(customer.phone);
            ivMobileNumberAccountSafe.setVisibility(View.INVISIBLE);
        } else {
            tvMobileNumberAccountSafe.setText(null);
            ivMobileNumberAccountSafe.setVisibility(View.VISIBLE);
        }


        if (customer.wechat.equals("1")) {
            tvWechatAccountSafe.setText("已绑定");
            ivWechatAccountSafe.setVisibility(View.INVISIBLE);
        } else {
            tvWechatAccountSafe.setText(null);
            ivWechatAccountSafe.setVisibility(View.VISIBLE);
        }

        if (customer.qq.equals("1")) {
            tvQQAccountSafe.setText("已绑定");
            ivQQAccountSafe.setVisibility(View.INVISIBLE);
        } else {
            tvQQAccountSafe.setText(null);
            ivQQAccountSafe.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void setActionBarDetail() {

    }

    @OnClick({R.id.relTradePswAccountSafe, R.id.relchangePswAccountSafe, R.id.relMobileNumberAccountSafe, R.id.relWechatAccountSafe, R.id.relQQAccountSafe,})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.relTradePswAccountSafe://交易密码

       /*
        点击进入设置支付密码页面

若未进行身份认证则提示“请先进行身份认证”

若未绑定手机号则提示“请绑定手机号”，不可进入


        //未设置交易密码
            //判断是否绑定了手机号
            if (!TextUtils.isEmpty(customer.phone)) {
                //跳转设置交易密码(验证手机号)
                startActivity(new Intent(MyBankCardActivity.this, SetTradePswVerificationCodeActivity.class));
            } else {
                //绑定手机号
                startActivity(new Intent(MyBankCardActivity.this, BindMobileActivity.class));
            }

*/


           /*     if (TextUtils.isEmpty(customer.identityCardNum)) {
                    ToastUtils.showShort("请先进行身份认证");
                    return;
                }

                if (TextUtils.isEmpty(customer.phone)) {
                    ToastUtils.showShort("请绑定手机号");
                    return;
                }
                SetTradePswVerificationCodeActivity.startActivity(this, 0);
*/

                if (TextUtils.isEmpty(customer.phone)) {//是否绑定手机号
                    new MaterialDialog.Builder(AccountSafeActivity.this).content("请先进行实名认证").positiveText("确认").negativeText("暂不").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            BindMobileActivity.startActivity(AccountSafeActivity.this, REQUEST_CDOE_SET_TRADE_PSW);
                        }
                    }).show();


                } else {
                    //已经绑定了手机号，判断是否身份认证了
                    if (TextUtils.isEmpty(customer.identityCardNum)) {
                        new MaterialDialog.Builder(AccountSafeActivity.this).content("请先进行实名认证").positiveText("确认").negativeText("暂不").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                IDCardIdentificationActivity.startActivity(AccountSafeActivity.this, REQUEST_CDOE_SET_TRADE_PSW);
                            }
                        }).show();

                    } else {
                        SetTradePswVerificationCodeActivity.startActivity(this, REQUEST_CDOE_SET_TRADE_PSW);
                    }
                }


                break;
            case R.id.relchangePswAccountSafe://修改登录密码
                startActivity(new Intent(this, UpdatePassWordActivity.class));
                break;
            case R.id.relMobileNumberAccountSafe://绑定手机号
                if (TextUtils.isEmpty(customer.phone)) {
                    BindMobileActivity.startActivity(this, 0);
                }
                break;
            case R.id.relWechatAccountSafe://绑定微信
                if (customer.wechat.equals("0") || tvWechatAccountSafe.getText().equals("未绑定")) {
                    loginByWeChat();
                }
                break;
            case R.id.relQQAccountSafe://绑定QQ
                if (customer.qq.equals("0") || tvQQAccountSafe.getText().equals("未绑定")) {
                    loginByQQ();
                }
                break;
        }
    }


    /**
     * 第三方登录QQ
     */
    public void loginByQQ() {
        UMShareAPI.get(AccountSafeActivity.this).deleteOauth(AccountSafeActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogUtils.dTag(TAG, " deleteOauth onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogOut.d("deleteOauth*****", "onComplete");
                UMShareAPI.get(AccountSafeActivity.this).getPlatformInfo(AccountSafeActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        LogUtils.dTag(TAG, "getPlatformInfo onStart");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        String uid = data.get("uid");
                        String name = data.get("name");
                        String iconUrl = data.get("iconurl");
                        mDao.securityBanded(1, 1, "", uid, "", 0, AccountSafeActivity.this);
                        LogUtils.dTag(TAG, "onComplete uid:" + uid + " name:" + name + " iconUrl" + iconUrl);

                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        if (t.getMessage().contains("2008")) {
                            ToastUtils.showShort("您未安装QQ，请先安装QQ");
                        } else {
                            ToastUtils.showShort("QQ授权失败");
                        }
                        cancleLoading();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        cancleLoading();
                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                LogUtils.dTag(TAG, "onError");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                LogUtils.dTag(TAG, "onCancel");
            }
        });
    }

    /**
     * 微信登录
     */
    public void loginByWeChat() {

        UMShareAPI.get(AccountSafeActivity.this).deleteOauth(AccountSafeActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogUtils.dTag(TAG, " deleteOauth onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogOut.d("deleteOauth*****", "onComplete");
                UMShareAPI.get(AccountSafeActivity.this).getPlatformInfo(AccountSafeActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        LogUtils.dTag(TAG, "getPlatformInfo onStart");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        ToastUtils.showShort("微信授权成功");
                        cancleLoading();
                        String uid = data.get("uid");
                        String name = data.get("name");
                        String iconUrl = data.get("iconurl");
                        LogUtils.dTag(TAG, "onComplete uid:" + uid + " name:" + name + " iconUrl" + iconUrl);
                        mDao.securityBanded(0, 0, "", uid, "", 0, AccountSafeActivity.this);
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        if (t.getMessage().contains("2008")) {
                            ToastUtils.showShort("您未安装微信，请先安装微信");
                        } else {
                            ToastUtils.showShort("微信授权失败");
                        }
                        cancleLoading();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        cancleLoading();
                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                LogUtils.dTag(TAG, "onError");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                LogUtils.dTag(TAG, "onCancel");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 0://微信绑定成功
                ToastUtils.showShort("微信绑定成功");
                if (tvWechatAccountSafe != null) {
                    tvWechatAccountSafe.setText("已绑定");
                    ivWechatAccountSafe.setVisibility(View.INVISIBLE);
                }
                break;

            case 1://QQ绑定成功
                ToastUtils.showShort("QQ绑定成功");
                if (tvQQAccountSafe != null) {
                    tvQQAccountSafe.setText("已绑定");
                    ivQQAccountSafe.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        ToastUtils.showShort(code.msg);
    }
}
