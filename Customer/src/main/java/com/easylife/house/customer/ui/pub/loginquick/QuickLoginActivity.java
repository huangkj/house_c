package com.easylife.house.customer.ui.pub.loginquick;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.pub.loginaccount.AccountLoginActivity;
import com.easylife.house.customer.ui.pub.registeraccount.RegisterAccountActivity;
import com.easylife.house.customer.util.SoftKeyBoardListener;
import com.easylife.house.customer.util.StatusBarUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.EditTextClearAble;
import com.easylife.house.customer.view.ImageViewTouch;

import butterknife.Bind;
import butterknife.OnClick;

public class QuickLoginActivity extends MVPBaseActivity<QuickLoginContract.View, QuickLoginPresenter> implements QuickLoginContract.View {


    @Bind(R.id.llTitle)
    LinearLayout llTitle;
    @Bind(R.id.llLogin)
    LinearLayout llLogin;
    @Bind(R.id.etPhoneNumber)
    EditTextClearAble etPhoneNumber;
    @Bind(R.id.tlPhone)
    TextInputLayout tlPhone;
    @Bind(R.id.btnGetVerifyCode)
    TextView btnGetVerifyCode;
    @Bind(R.id.etVerificationCode)
    EditTextClearAble etVerificationCode;
    @Bind(R.id.cbAgreement)
    CheckBox cbAgreement;
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

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, QuickLoginActivity.class), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_quick_login, null);
    }

    @Override
    protected void initView() {
        logonAnim();

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

        SoftKeyBoardListener.setListener(QuickLoginActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
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
                RegisterAccountActivity.startActivity(activity, 0);
            }
        });
    }


    @OnClick({R.id.tvPassLogin})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvPassLogin:
                AccountLoginActivity.startActivity(this, 0);
                finish();
                break;

        }
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }


}
