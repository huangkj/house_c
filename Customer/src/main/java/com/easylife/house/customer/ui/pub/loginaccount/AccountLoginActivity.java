package com.easylife.house.customer.ui.pub.loginaccount;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity;
import com.easylife.house.customer.ui.pub.forgetpassword.ForgetPassWordActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.ui.pub.registeraccount.RegisterAccountActivity;
import com.easylife.house.customer.util.AfterTextWatch;
import com.easylife.house.customer.util.LogOut;
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

public class AccountLoginActivity extends MVPBaseActivity<AccountLoginContract.View, AccountLoginPresenter> implements AccountLoginContract.View {

    @Bind(R.id.etPhoneNumber)
    EditTextClearAble etPhoneNumber;
    @Bind(R.id.etPassword)
    EditTextClearAble etPassword;
    @Bind(R.id.btnLogin)
    ButtonTouch btnLogin;
    @Bind(R.id.tvQuickLogin)
    TextView tvQuickLogin;
    @Bind(R.id.tvForgetPass)
    TextView tvForgetPass;
    @Bind(R.id.imgQQ)
    ImageViewTouch imgQQ;
    @Bind(R.id.imgWeChat)
    ImageViewTouch imgWeChat;
    @Bind(R.id.imgMicroblog)
    ImageViewTouch imgMicroblog;
    @Bind(R.id.llTitle)
    LinearLayout llTitle;
    @Bind(R.id.llContent)
    LinearLayout llContent;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_account_login, null);
    }

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, AccountLoginActivity.class), requestCode);
    }

    @Override
    protected void initView() {
//        logonAnim();
        etPhoneNumber.addTextChangedListener(new AfterTextWatch() {
            @Override
            public void afterTextChange(Editable s) {
                btnLogin.setEnabled(checkDataValue(etPhoneNumber.getText().toString(), etPassword.getText().toString()));
            }
        });

        etPassword.addTextChangedListener(new AfterTextWatch() {

            @Override
            public void afterTextChange(Editable s) {
                btnLogin.setEnabled(checkDataValue(etPhoneNumber.getText().toString(), etPassword.getText().toString()));
            }
        });
    }


    public boolean checkDataValue(String phone, String pass) {
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pass)) {
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


    @OnClick({R.id.btnLogin, R.id.tvQuickLogin, R.id.tvForgetPass, R.id.imgQQ, R.id.imgWeChat})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                mPresenter.loginByNormal(etPhoneNumber.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.tvQuickLogin:
                LoginByVerifyCodeActivity.startActivity(activity, 0);
                finish();
                break;
            case R.id.tvForgetPass:
                startActivity(new Intent(this, ForgetPassWordActivity.class));
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
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void loginFail(String msg) {

    }

    @Override
    public void loginSucc( ) {
        userInfoSuc();
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

    @Override
    public void startBindPhone(String easeUsername, String easePassword) {
        BindMobileActivity.startActivity(activity, REQUEST_CODE_BIND_USER);
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

    /**
     * 第三方登录QQ  防止mView报空
     *
     * @param activity
     * @param view
     */
    public void loginByQQ(final Activity activity, final AccountLoginContract.View view) {
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
    public void loginByWeChat(final Activity activity, final AccountLoginContract.View view) {
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

        SoftKeyBoardListener.setListener(AccountLoginActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //显示
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(llTitle, "scaleX", 1f, 0f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(llTitle, "scaleY", 1f, 0f);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(llTitle, "alpha", 1f, 0f);
                ObjectAnimator translationY = ObjectAnimator.ofFloat(llTitle, "translationY", 0f, -100f);
                ObjectAnimator translationYLl = ObjectAnimator.ofFloat(llContent, "translationY", 0f, -llTitle.getMeasuredHeight());
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
                ObjectAnimator translationYLl = ObjectAnimator.ofFloat(llContent, "translationY", -llTitle.getMeasuredHeight(), 0f);
                AnimatorSet set = new AnimatorSet();
                set.play(scaleX).with(scaleY).with(alpha).with(translationYLl);
                set.setDuration(300);
                set.start();
            }
        });
    }

}
