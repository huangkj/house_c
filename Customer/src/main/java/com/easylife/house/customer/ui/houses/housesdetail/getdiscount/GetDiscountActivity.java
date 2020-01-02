package com.easylife.house.customer.ui.houses.housesdetail.getdiscount;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.GitDisCountBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.ValidatorUtils;
import com.easylife.house.customer.view.FlowViewGroup;
import com.easylife.house.customer.view.LineEditText;
import com.easylife.house.customer.view.TimeUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 楼盘详情获取优惠
 */
public class GetDiscountActivity extends MVPBaseActivity<GetDiscountContract.View, GetDiscountPresenter> implements GetDiscountContract.View {

    @Bind(R.id.iv_top)
    ImageView ivTop;
    @Bind(R.id.tv_houses_name)
    TextView tvHousesName;
    @Bind(R.id.tv_people)
    TextView tvPeople;
    @Bind(R.id.tv_people_name)
    TextView tvPeopleName;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.et_name)
    LineEditText etName;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.et_phone)
    LineEditText etPhone;
    @Bind(R.id.tv_smscode)
    TextView tvSmscode;
    @Bind(R.id.et_smscode)
    LineEditText etSmscode;
    @Bind(R.id.tv_get_sms)
    TextView tvGetSms;
    @Bind(R.id.tv_bottom)
    TextView tvBottom;
    @Bind(R.id.cb_group)
    FlowViewGroup cbGroup;
    @Bind(R.id.tv_loan)
    TextView tvLoan;
    @Bind(R.id.cb_agreen)
    CheckBox cbAgreen;
    @Bind(R.id.tv_agree)
    TextView tvAgree;

    private String userCode;
    private String token;
    private String[] disStr = new String[]{"5万抵20万           适用于          三居室", "5万抵20万           适用于          四居室"};
    private HousesDetailBaseBean baseBean;
    private Customer customer;
    private GitDisCountBean gitDisCountBean;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_get_discount, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        baseBean = (HousesDetailBaseBean) getIntent().getSerializableExtra("MORE_INFO");
        mPresenter.getDisCount(baseBean.devId);

        if (baseBean != null) {
            tvHousesName.setText(baseBean.devName);
            try {
                if (baseBean.effectId == null && baseBean.effectId.size() == 0) {
                    return;
                }
                CacheManager.initImageClientList(this, ivTop, baseBean.effectId.get(0).url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        infoSet();
    }

    public void infoSet() {
        customer = dao.getCustomer();
        LoginResult loginCache = dao.getLoginCache();
        if (loginCache != null) {
            userCode = loginCache.userCode;
            token = loginCache.token;
        }
        if (customer != null) {
            if (!TextUtils.isEmpty(customer.username)) {
                etName.setText(customer.username);
                etName.setClickable(false);
                etName.setEnabled(false);
            } else {
                etName.setClickable(true);
                etName.setEnabled(true);
            }


            if (!TextUtils.isEmpty(customer.phone)) {
                etPhone.setText(customer.phone);
                etPhone.setClickable(false);
                etPhone.setEnabled(false);
                tvSmscode.setVisibility(View.GONE);
                etSmscode.setVisibility(View.GONE);
                tvGetSms.setVisibility(View.GONE);
            } else {
                etPhone.setClickable(true);
                etPhone.setEnabled(true);
                tvSmscode.setVisibility(View.VISIBLE);
                etSmscode.setVisibility(View.VISIBLE);
                tvGetSms.setVisibility(View.VISIBLE);
            }
        } else {
            etName.setClickable(true);
            etName.setEnabled(true);
            etPhone.setClickable(true);
            etPhone.setEnabled(true);
            tvSmscode.setVisibility(View.VISIBLE);
            etSmscode.setVisibility(View.VISIBLE);
            tvGetSms.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("获取优惠");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));

    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }

    @OnClick({R.id.tv_get_sms, R.id.tv_bottom, R.id.cb_agreen, R.id.tv_agree, R.id.tv_get_discount})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_sms:
                String phone = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    CustomerUtils.showTip(this, "请输入手机号");
                    return;
                }
                if (!ValidatorUtils.isMobile(phone)) {
                    CustomerUtils.showTip(this, "请输入正确的手机号");
                    return;
                }
                mPresenter.getVerifyCode(etPhone.getText().toString().trim());
                TimeUtils timeUtils = new TimeUtils(tvGetSms, "获取验证码", this);
                timeUtils.RunTimer();
                break;
            //获取优惠
            case R.id.tv_get_discount:
                if (cbAgreen.isChecked()) {
                    if (dao.isLogin()) {
                        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                            CustomerUtils.showTip(this, "请输入手机号码");
                            return;
                        } else {
                            if (!ValidatorUtils.isMobile(etPhone.getText().toString().trim())) {
                                CustomerUtils.showTip(this, "请输入正确的手机号码");
                            } else {
                                // TODO 二期优惠券将被替换掉
                                if (etSmscode.getVisibility() == View.VISIBLE) {
                                    if (TextUtils.isEmpty(etSmscode.getText().toString().trim())) {
                                        CustomerUtils.showTip(this, "请输入验证码");
                                        return;
                                    } else {
                                        //  跳转获取优惠
                                        String chooseDis = ((RadioButton) findViewById(cbGroup.getCheckedRadioButtonId())).getText().toString();
                                        String id = "";
                                        if (gitDisCountBean != null) {
                                            for (int i = 0; i < gitDisCountBean.data.size(); i++) {

                                                if (chooseDis.equals(gitDisCountBean.data.get(i).privilege)) {
                                                    id = gitDisCountBean.data.get(i).id;
                                                }
                                            }
                                        }

                                        mPresenter.commitGetDis(baseBean.devId, customer.userCode, customer.token, etPhone.getText().toString().trim(),
                                                etSmscode.getText().toString().trim(), etName.getText().toString().trim(), id);
                                        //保存手机号
                                        if (customer != null && TextUtils.isEmpty(customer.phone)) {
                                            customer.phone = etPhone.getText().toString().trim();
                                            dao.saveCustomer(customer);
                                        }
                                    }

                                } else {
                                    //  登录页面完成后跳转获取优惠
                                    String chooseDis = ((RadioButton) findViewById(cbGroup.getCheckedRadioButtonId())).getText().toString().trim();
                                    String id = "";
                                    if (gitDisCountBean != null) {
                                        for (int i = 0; i < gitDisCountBean.data.size(); i++) {

                                            if (chooseDis.equals(gitDisCountBean.data.get(i).privilege)) {
                                                id = gitDisCountBean.data.get(i).id;
                                            }

                                        }
                                    }
                                    mPresenter.commitGetDis(baseBean.devId, customer.userCode, customer.token, etPhone.getText().toString().trim(),
                                            etSmscode.getText().toString().trim(), etName.getText().toString().trim(), id);
                                }

                            }
                        }
                    } else {
                        startActivityForResult(new Intent(GetDiscountActivity.this, LoginByVerifyCodeActivity.class), 1);
                    }
                } else {
                    CustomerUtils.showTip(this, "请同意优惠说明书");
                }

                break;
            //选中阅读条例
            case R.id.cb_agreen:
                break;
            //跳转优惠说明书
            case R.id.tv_agree:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            infoSet();
            userCode = dao.getLoginCache().userCode;
            token = dao.getLoginCache().token;
        }
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
        CustomerUtils.showTip(this, msg);
    }

    @Override
    public void showDisCount(GitDisCountBean gitDisCountBean) {
        if (gitDisCountBean == null) {
            return;
        }
        this.gitDisCountBean = gitDisCountBean;
        tvPeople.setText(gitDisCountBean.count);

        for (int i = 0; i < gitDisCountBean.data.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioButton.setLayoutParams(params);
            ColorStateList csl = getResources().getColorStateList(R.color.gradient_end);
            radioButton.setButtonTintList(csl);
            radioButton.setTextSize(16);
            radioButton.setTextColor(getResources().getColor(R.color._134134134));
            radioButton.setText(gitDisCountBean.data.get(i).privilege);
            cbGroup.addView(radioButton);
        }
        ((RadioButton) cbGroup.getChildAt(0)).setChecked(true);
    }

    @Override
    public void showSuccDis(String msg) {
        CustomerUtils.showTip(this, msg);
        finish();
    }

    @Override
    public void getVerifyCodeSucc() {

    }
}
