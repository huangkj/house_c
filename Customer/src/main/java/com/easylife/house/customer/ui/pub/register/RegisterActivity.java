package com.easylife.house.customer.ui.pub.register;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.pub.InvitationCodeActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.SmsButtonUtil;
import com.easylife.house.customer.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class RegisterActivity extends MVPBaseActivity<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {

    @Bind(R.id.view_pager)
    NoScrollViewPager viewPager;
    private SmsButtonUtil smsButtonUtil;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_register, null);
    }

    private List<View> views;
    private TextView tvGetVerifyCode;
    private EditText edName;
    private EditText edPhone;
    private EditText edVerifyCode;
    private EditText edPass;
    private CheckBox cbShowPass;
    private CheckBox cbAgree;

    private String phone;
    private String pass;
    private boolean isRegisterSuccess;

    @Override
    protected void initView() {
        hideNoNetView();
        views = new ArrayList<>();
        View v1 = LayoutInflater.from(activity).inflate(R.layout.register_fragment_name, null);
        View v2 = LayoutInflater.from(activity).inflate(R.layout.register_fragment_phone, null);
        View v3 = LayoutInflater.from(activity).inflate(R.layout.register_fragment_verify_code, null);
        View v4 = LayoutInflater.from(activity).inflate(R.layout.register_fragment_pass, null);
        views.add(v1);
        views.add(v2);
        views.add(v3);
        views.add(v4);
        edName = (EditText) v1.findViewById(R.id.edName);
        cbAgree = (CheckBox) v1.findViewById(R.id.cbAgreement);
        edPhone = (EditText) v2.findViewById(R.id.edPhone);
        edVerifyCode = (EditText) v3.findViewById(R.id.edVerifyCode);
        tvGetVerifyCode = (TextView) v3.findViewById(R.id.btnGetVerifyCode);
        edPass = (EditText) v4.findViewById(R.id.edPass);
        cbShowPass = (CheckBox) v4.findViewById(R.id.cbShowPass);
        smsButtonUtil = new SmsButtonUtil(tvGetVerifyCode);
        smsButtonUtil.setCountDownText("已发送(%ds)");

//        edName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//            }
//        });
//        edPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//            }
//        });
//        edVerifyCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//            }
//        });
//        edPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//            }
//        });
        v1.findViewById(R.id.tvAgreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(activity, "好生活用户协议", Constants.URL_AGREEMENT_REGISTER, "我已阅读");
            }
        });
        v1.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.submitName(edName.getText().toString(), cbAgree);
            }
        });
        v2.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = edPhone.getText().toString();
                mPresenter.submitPhone(edPhone.getText().toString());
            }
        });
        v3.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.submitVerifyCode(edVerifyCode.getText().toString());
            }
        });
        v4.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPresenter.submit(edPass.getText().toString());
                pass = edPass.getText().toString();
                if (TextUtils.isEmpty(pass)) {
                    showTip("请输入密码");
                    return;
                }

                if (pass.length() < 6 || pass.length() > 16) {
                    showTip("请输入6-16位字符");
                    return;
                }
                InvitationCodeActivity.startActivity(activity, edName.getText().toString(), edPhone.getText().toString(),
                        edVerifyCode.getText().toString(), pass, 1);
            }
        });
        cbShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.exchangePassWordState(edPass, isChecked);
            }
        });
        tvGetVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                mPresenter.getVerifyCode(edPhone.getText().toString());
            }
        });

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!cbAgree.isChecked()) {
            cbAgree.setChecked(true);
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //  邀请码注册成功返回
                    isRegisterSuccess = true;
                    setResult(RESULT_OK,
                            new Intent().putExtra("phone", phone).putExtra("pass", pass));
                    finish();
                    break;
            }
        }
    }


    @Override
    protected void setActionBarDetail() {
        tvTitle.setText("注册");
        actionBar.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void submitFail(String msg) {
        cancelLoading();
        showTip(msg);
    }

    @Override
    public void submitSucc() {
        dao.pointRegister(phone);
        cancelLoading();
        showTip("注册成功");
        super.finish();
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
    public void showNextView(int position) {
        cancelLoading();
        viewPager.setCurrentItem(position);
    }

    @Override
    public int getViewIndex() {
        return viewPager.getCurrentItem();
    }

    /**
     * 密码校验成功
     */
    @Override
    public void valiSuc(String pass) {
        showLoading();
        mPresenter.register(edName.getText().toString(), edPhone.getText().toString(), edVerifyCode.getText().toString(), pass);
    }

    @Override
    public void finish() {
        if (!isRegisterSuccess && viewPager.getCurrentItem() != 0) {
            if (viewPager.getCurrentItem() == 2) {
                smsButtonUtil.cancelCountDown();
            }
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            super.finish();
        }
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

}
