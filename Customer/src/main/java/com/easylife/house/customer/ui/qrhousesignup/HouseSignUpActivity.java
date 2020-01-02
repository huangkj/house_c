package com.easylife.house.customer.ui.qrhousesignup;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.CommitSignInfoBean;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.HouseSignBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.ValidatorUtils;
import com.easylife.house.customer.view.LineEditText;
import com.easylife.house.customer.view.TimeUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 扫一扫报名页面
 */
public class HouseSignUpActivity extends MVPBaseActivity<HouseSignUpContract.View, HouseSignUpPresenter> implements HouseSignUpContract.View {

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
    @Bind(R.id.btn_sub)
    TextView btnSub;
    @Bind(R.id.tv_sub)
    TextView tvSub;
    @Bind(R.id.tv_sub_value)
    TextView tvSubValue;
    private String qr_str;

    private String devId = "";
    private String devName = "";
    private String brokerUserCode = "";
    private HouseSignBean houseSignBean;
    private Customer customer;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.activity_house_sign_up, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        qr_str = getIntent().getStringExtra("QR_STR");
        if (!TextUtils.isEmpty(qr_str)) {
            Map<String, String> paramsMap = new HashMap<>();
            String[] split = qr_str.split("&");
            for (String params : split) {
                String[] paramsKeyValue = params.split("=");
                paramsMap.put(paramsKeyValue[0], paramsKeyValue[1]);
            }

            devId = paramsMap.get("devId");
            devName = paramsMap.get("devName");
            brokerUserCode = paramsMap.get("brokerCode");
            customer = dao.getCustomer();
            if (customer == null)
                return;
            mPresenter.requestSignInfo(customer.userCode, devId, customer.token, brokerUserCode);
        }
//        mPresenter.requestSignInfo("822261f9-e221-47ec-88c2-099531a7c776","53","b4ca1f29-41a8-43ca-b10a-57c38395aae2","a748187f-f858-11e6-8472-1866dae6a5fc");


        Customer customer = dao.getCustomer();
        if (customer != null) {
            if (TextUtils.isEmpty(customer.phone)) {
                tvGetSms.setVisibility(View.VISIBLE);
                etSmscode.setVisibility(View.VISIBLE);
                tvSmscode.setVisibility(View.VISIBLE);
                etName.setClickable(true);
                etPhone.setClickable(true);
            } else {
                tvGetSms.setVisibility(View.GONE);
                etSmscode.setVisibility(View.GONE);
                tvSmscode.setVisibility(View.GONE);
                etPhone.setClickable(false);
                etPhone.setFocusable(false);
                etName.setFocusable(false);
                etPhone.setText(customer.phone);
                etName.setClickable(false);
                etName.setText(customer.username);
            }

        }
    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("楼盘报名");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {

    }

    @OnClick({R.id.btn_sub, R.id.tv_get_sms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sub:
                String phoneNum = etPhone.getText().toString().trim();
                String name = etName.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    CustomerUtils.showTip(this, "请填写手机号");
                    return;
                }
                if (!ValidatorUtils.isMobile(phoneNum)) {
                    CustomerUtils.showTip(this, "请填写正确的手机号");
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    CustomerUtils.showTip(this, "请填写姓名");
                    return;
                }


                if (houseSignBean == null) {
                    return;
                }
                CommitSignInfoBean commitSignInfoBean = new CommitSignInfoBean();
                commitSignInfoBean.brokerUserCode = brokerUserCode;
                commitSignInfoBean.devId = devId;
                commitSignInfoBean.name = etName.getText().toString().trim();
                if (customer != null) {
                    commitSignInfoBean.userCode = customer.userCode;
                    commitSignInfoBean.token = customer.token;
                }
                commitSignInfoBean.phone = etPhone.getText().toString().trim();
                if (etSmscode.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etSmscode.getText().toString().trim())) {
                    CustomerUtils.showTip(this, "请填写短信验证码");
                    return;
                } else {
                    commitSignInfoBean.varifyCode = etSmscode.getText().toString().trim();
                }

                mPresenter.commitSignInfo(commitSignInfoBean);
                break;
            case R.id.tv_get_sms:
                String phone = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    CustomerUtils.showTip(this, "请填写手机号");
                    return;
                }
                if (!ValidatorUtils.isMobile(phone)) {
                    CustomerUtils.showTip(this, "请填写正确的手机号");
                    return;
                }
                mPresenter.getVerifyCode(etPhone.getText().toString().trim());
                TimeUtils timeUtils = new TimeUtils(tvGetSms, "获取验证码", this);
                timeUtils.RunTimer();
                break;
        }
    }

    @Override
    public void getVerifyCodeSucc() {

    }

    @Override
    public void showFail(String msg) {
        CustomerUtils.showTip(this, msg);
    }

    @Override
    public void showSign(HouseSignBean houseSignBean) {
        this.houseSignBean = houseSignBean;
        if (houseSignBean == null)
            return;

        CacheManager.initImagePubAdd(this, ivTop, houseSignBean.effect.url);
        tvHousesName.setText(houseSignBean.devName);
        tvPeople.setText(houseSignBean.count + "人");
        tvSubValue.setText(houseSignBean.brokerName);
    }

    @Override
    public void showCommitSuc(String msg) {
        dao.pointApply(devId, devName);
        CustomerUtils.showTip(this, msg);
        finish();
    }
}
