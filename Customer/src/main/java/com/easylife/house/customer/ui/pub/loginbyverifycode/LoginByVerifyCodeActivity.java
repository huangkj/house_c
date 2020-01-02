package com.easylife.house.customer.ui.pub.loginbyverifycode;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.ui.pub.loginaccount.AccountLoginActivity;
import com.easylife.house.customer.ui.pub.registeraccount.RegisterAccountActivity;
import com.easylife.house.customer.util.AfterTextWatch;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.SmsButtonUtil;
import com.easylife.house.customer.util.SoftKeyBoardListener;
import com.easylife.house.customer.util.StatusBarUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.EditTextClearAble;
import com.easylife.house.customer.view.ImageViewTouch;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CODE_BIND_USER;


public class LoginByVerifyCodeActivity extends MVPBaseActivity<LoginByVerifyCodeContract.View, LoginByVerifyCodePresenter> implements LoginByVerifyCodeContract.View {
    @Bind(R.id.edPhone)
    EditTextClearAble edPhone;
    @Bind(R.id.edVerifyCode)
    EditText edVerifyCode;
    @Bind(R.id.btnGetVerifyCode)
    TextView btnSmsCode;
    @Bind(R.id.cbAgreement)
    CheckBox cbAgreement;
    @Bind(R.id.llTitle)
    LinearLayout llTitle;
    @Bind(R.id.tlPhone)
    TextInputLayout tlPhone;
    @Bind(R.id.tvAgreement)
    TextView tvAgreement;
    @Bind(R.id.btnLogin)
    ButtonTouch btnLogin;
    @Bind(R.id.tvPassLogin)
    TextView tvPassLogin;
    @Bind(R.id.imgQQ)
    ImageViewTouch imgQQ;
    @Bind(R.id.imgWeChat)
    ImageViewTouch imgWeChat;
    @Bind(R.id.imgMicroblog)
    ImageViewTouch imgMicroblog;
    @Bind(R.id.llLogin)
    LinearLayout llLogin;

    private SmsButtonUtil smsButtonUtil;
    private String easeUsername;
    private String easePassword;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_login_verifycode, null);
    }

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, LoginByVerifyCodeActivity.class), requestCode);
    }

    public static void startActivity(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), LoginByVerifyCodeActivity.class), requestCode);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        smsButtonUtil = new SmsButtonUtil(btnSmsCode);
        smsButtonUtil.setCountDownText("已发送(%ds)");


        edPhone.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                btnLogin.setEnabled(checkDataValue(edPhone.getText().toString(), edVerifyCode.getText().toString()));
                btnSmsCode.setEnabled(!TextUtils.isEmpty(edPhone.getText().toString()));

            }
        });

        edVerifyCode.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                btnLogin.setEnabled(checkDataValue(edPhone.getText().toString(), edVerifyCode.getText().toString()));
            }
        });


    }

    public boolean checkDataValue(String phone, String code) {
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void setActionBarDetail() {
        findViewById(R.id.content).setFitsSystemWindows(true);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarUtils.setLightMode(this);
        actionBar.setBackgroundColor(getResources().getColor(R.color.white));
        tvTitle.setVisibility(View.INVISIBLE);
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setText("注册");
        btnRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterAccountActivity.startActivity(activity, 111);
            }
        });
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void loginFail(String msg) {
        cancelLoading();
        showTip(msg);
    }

    @Override
    public void loginSucc() {
        userInfoSuc();
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
    public void userInfoSuc() {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGIN_STATE_CHANGE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.HOUSES_INDEXT_COLLECTION_LOGIN));
        dao.localDao.saveIsClear("1");
        showTip("登录成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void loginThirdSucc() {
        showTip("登录成功");
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGIN_STATE_CHANGE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.HOUSES_INDEXT_COLLECTION_LOGIN));
        cancelLoading();
        setResult(RESULT_OK);
        finish();
    }

    @OnClick({R.id.btnGetVerifyCode, R.id.btnLogin, R.id.tvAgreement, R.id.tvPassLogin, R.id.imgQQ, R.id.imgWeChat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetVerifyCode:
                mPresenter.getVerifyCode(edPhone.getText().toString());
                break;
            case R.id.btnLogin:
                mPresenter.loginByVerifyCode(edPhone.getText().toString(), edVerifyCode.getText().toString(), cbAgreement.isChecked());
                break;
            case R.id.tvAgreement:
                WebViewActivity.startActivity(activity, "好生活用户协议", Constants.URL_AGREEMENT_REGISTER, "我已阅读");
                break;

            case R.id.tvPassLogin:
                AccountLoginActivity.startActivity(LoginByVerifyCodeActivity.this, 0);
                finish();
                break;

            case R.id.imgQQ:
                loginByQQ(activity, this);
                break;
            case R.id.imgWeChat:
                loginByWeChat(activity, this);
                break;


        }
    }

    @Override
    public void showTip(final String msg) {
        btnSmsCode.post(new Runnable() {
            @Override
            public void run() {
                CustomerUtils.showTip(activity, msg);
            }
        });
    }



    /**
     * 第三方登录QQ  防止mView报空
     *
     * @param activity
     * @param view
     */
    public void loginByQQ(final Activity activity, final LoginByVerifyCodeContract.View view) {
        UMShareAPI.get(view.getContext()).deleteOauth(activity, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogOut.d("deleteOauth*****", "onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogOut.d("deleteOauth*****", "onComplete");


                UMShareAPI.get(view.getContext()).getPlatformInfo(activity, SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        LogOut.d("@@", "onStart");
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        LogOut.d("@@", "QQ授权成功");

                        showTip("QQ授权成功");
                        String uid = data.get("uid");
                        String name = data.get("name");
                        String iconUrl = data.get("iconurl");
                        mPresenter.loginByOther(name, uid, iconUrl, "0");
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        LogOut.d("@@", t.getMessage() + " " + t.getCause());

                        if (t.getMessage().contains("2008")) {
                            showTip("您未安装QQ，请先安装QQ");
                        } else {
                            showTip("QQ授权失败");
                        }
                        cancelLoading();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        LogOut.d("@@", "onCancel");
                        cancelLoading();
                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                LogOut.d("deleteOauth*****", "onError");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                LogOut.d("deleteOauth*****", "onCancel");
            }
        });
    }


    /**
     * 微信登录
     *
     * @param activity
     * @param view
     */
    public void loginByWeChat(final Activity activity, final LoginByVerifyCodeContract.View view) {
        UMShareAPI.get(view.getContext()).deleteOauth(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogOut.d("deleteOauth*****", "onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogOut.d("deleteOauth*****", "onComplete");
                UMShareAPI.get(view.getContext()).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                        showTip("微信授权成功");
                        cancelLoading();
                        String uid = data.get("uid");
                        String name = data.get("name");
                        String iconUrl = data.get("iconurl");
                        mPresenter.loginByOther(name, uid, iconUrl, "1");
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                        if (t.getMessage().contains("2008")) {
                            showTip("您未安装微信，请先安装微信");
                        } else {
                            showTip("微信授权失败");
                        }
                        cancelLoading();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform, int action) {
                        cancelLoading();
                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                LogOut.d("deleteOauth*****", "onError");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                LogOut.d("deleteOauth*****", "onCancel");
            }
        });
    }

    @Override
    public void startBindPhone() {
        BindMobileActivity.startActivity(activity, REQUEST_CODE_BIND_USER);

    }

    /**
     * 登录页面logo移动动画
     */
    private void logonAnim() {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        llTitle.measure(width, height);


//        KeyboardUtils.toggleSoftInput();

        SoftKeyBoardListener.setListener(LoginByVerifyCodeActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //显示
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(llTitle, "scaleX", 1f, 0f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(llTitle, "scaleY", 1f, 0f);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(llTitle, "alpha", 1f, 0f);
                ObjectAnimator translationY = ObjectAnimator.ofFloat(llTitle, "translationY", 0f, -100f);
                ObjectAnimator translationYLl = ObjectAnimator.ofFloat(llLogin, "translationY", 0f, -llTitle.getMeasuredHeight());
                AnimatorSet set = new AnimatorSet();
                set.play(scaleX).with(scaleY).with(alpha).with(translationYLl);
                set.setDuration(300);
                set.start();
            }

            @Override
            public void keyBoardHide(int height) {
                //隐藏
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(llTitle, "scaleX", 0f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(llTitle, "scaleY", 0f, 1f);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(llTitle, "alpha", 0f, 1f);
                ObjectAnimator translationY = ObjectAnimator.ofFloat(llTitle, "translationY", -100f, 0f);
                ObjectAnimator translationYLl = ObjectAnimator.ofFloat(llLogin, "translationY", -llTitle.getMeasuredHeight(), 0f);
                AnimatorSet set = new AnimatorSet();
                set.play(scaleX).with(scaleY).with(alpha).with(translationYLl);
                set.setDuration(300);
                set.start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BIND_USER) {
            // 第三方登录成功后注册手机号返回
            if (resultCode == RESULT_OK) {
                loginThirdSucc();
            } else {
                // 绑定手机号失败，退出登录状态
                dao.loginOut();
            }
        } else if (requestCode == 111 && resultCode == RESULT_OK) {
            //注册成功
            finish();
        }
    }

}
