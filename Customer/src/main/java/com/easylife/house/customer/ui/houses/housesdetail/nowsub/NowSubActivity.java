package com.easylife.house.customer.ui.houses.housesdetail.nowsub;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.HouseInfoSubBean;
import com.easylife.house.customer.bean.HouseSubNumBean;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 楼盘订阅页面
 */
public class NowSubActivity extends MVPBaseActivity<NowSubContract.View, NowSubPresenter> implements NowSubContract.View {

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
    @Bind(R.id.floviewgroup)
    FlowViewGroup flowViewGroup;
    @Bind(R.id.btn_sub)
    TextView btnSub;
    private HousesDetailBaseBean baseBean;
    private String userCode;
    private String token;
    private Customer customer;
    private Map<String, String> tagMap = new HashMap<>();

    private String[] mDynamic = new String[]
            {"价格变动", "开盘通知", "动态"};
    private CheckBox cb;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.activity_now_sub, null);
    }

    @Override
    protected void initView() {
        baseBean = (HousesDetailBaseBean) getIntent().getSerializableExtra("MORE_INFO");
        showLoading();
        mPresenter.getHousesSubNum(baseBean.devId,baseBean.estateProjectId);
        for (int i = 0; i < mDynamic.length; i++) {
            cb = (CheckBox) LayoutInflater.from(this).inflate(R.layout.flow_house_sub, flowViewGroup, false);
            cb.setText(mDynamic[i]);
            flowViewGroup.addView(cb);
        }
        checkedTag((CheckBox) flowViewGroup.getChildAt(0), "0");
        checkedTag((CheckBox) flowViewGroup.getChildAt(1), "1");
        checkedTag((CheckBox) flowViewGroup.getChildAt(2), "2");

        if (baseBean != null) {
            tvHousesName.setText(baseBean.devName);
            try {
                if(baseBean.effectId == null && baseBean.effectId.size() == 0){
                    return;
                }
                CacheManager.initImageClientList(this, ivTop, baseBean.effectId.get(0).url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        infoSet();
    }

    private void checkedTag(final CheckBox child, final String i) {
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (child.isChecked()) {
                    child.setTextColor(getResources().getColor(R.color.white));

                    if (!tagMap.containsValue(child.getText().toString().trim())) {
                        tagMap.put(i, i);
                    }
                } else {
                    child.setTextColor(getResources().getColor(R.color.gradient_end));
                    if (tagMap.containsKey(i)) {
                        tagMap.remove(i);
                    }
                }
            }
        });
    }

    public void infoSet() {
        customer = dao.getCustomer();
        LoginResult loginCache = dao.getLoginCache();
        if (loginCache != null) {
            userCode = loginCache.userCode;
            token = loginCache.token;
            mPresenter.getSubList(userCode, token, baseBean.estateProjectId,baseBean.devId);
        }
        if (customer != null) {
            if (!TextUtils.isEmpty(customer.username)) {
                etName.setHint(customer.username);
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
        tvTitle.setText("信息订阅");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {
        showLoading();
        if(baseBean == null)
            return;
        mPresenter.getHousesSubNum(baseBean.devId,baseBean.estateProjectId);
    }

    @Override
    public void showTip(String msg) {

    }

    private boolean isSubSuc;
    @OnClick({R.id.btn_sub, R.id.tv_get_sms})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sub:
                if (dao.isLogin()) {
                    if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                        CustomerUtils.showTip(this, "请输入手机号码");
                        return;
                    } else {
                        if (!ValidatorUtils.isMobile(etPhone.getText().toString().trim())) {
                            CustomerUtils.showTip(this, "请输入正确的手机号码");
                        } else {
                            if (etSmscode.getVisibility() == View.VISIBLE) {
                                if (TextUtils.isEmpty(etSmscode.getText().toString().trim())) {
                                    CustomerUtils.showTip(this, "请输入验证码");
                                    return;
                                } else {
                                    //保存手机号
                                    if (customer != null && TextUtils.isEmpty(customer.phone)) {
                                        customer.phone = etPhone.getText().toString().trim();
                                        dao.saveCustomer(customer);
                                    }

                                    HouseInfoSubBean houseInfoSubBean = new HouseInfoSubBean();
                                    houseInfoSubBean.devId = baseBean.devId;
                                    houseInfoSubBean.token = token;
                                    houseInfoSubBean.userCode = userCode;
                                    houseInfoSubBean.projectId = baseBean.estateProjectId;
                                    houseInfoSubBean.name = etName.getText().toString().trim();
                                    houseInfoSubBean.phone = etPhone.getText().toString().trim();
                                    houseInfoSubBean.varifyCode = etSmscode.getText().toString().trim();
                                    if (tagMap != null && tagMap.size() != 0) {
                                        StringBuffer sb = new StringBuffer();
                                        if(tagMap.size() == 1){
                                            for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                                                if(entry.getKey().equals("-1")){
                                                    isSubSuc = false;
                                                }else {
                                                    isSubSuc = true;
                                                }
                                            }
                                        }else {
                                            isSubSuc = true;
                                        }
                                        for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                                            sb.append(entry.getKey()).append(",");
                                        }
                                        String tagSub = sb.toString().substring(0, sb.toString().length() - 1);
                                        houseInfoSubBean.tag = tagSub;
                                    } else {
                                        houseInfoSubBean.tag = "";
                                    }
                                    showLoading();
                                    mPresenter.getSub(houseInfoSubBean);
                                }

                            } else {
                                HouseInfoSubBean houseInfoSubBean = new HouseInfoSubBean();
                                houseInfoSubBean.devId = baseBean.devId;
                                houseInfoSubBean.token = token;
                                houseInfoSubBean.userCode = userCode;
                                houseInfoSubBean.projectId = baseBean.estateProjectId;
                                houseInfoSubBean.name = etName.getHint().toString().trim();
                                houseInfoSubBean.phone = etPhone.getText().toString().trim();
                                houseInfoSubBean.varifyCode = "";
                                if (tagMap != null && tagMap.size() != 0) {
                                    StringBuffer sb = new StringBuffer();
                                    if(tagMap.size() == 1){
                                        for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                                            if(entry.getKey().equals("-1")){
                                                isSubSuc = false;
                                            }else {
                                                isSubSuc = true;
                                            }
                                        }
                                    }else {
                                        isSubSuc = true;
                                    }
                                    for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                                        sb.append(entry.getKey()).append(",");
                                    }
                                    String tagSub = sb.toString().substring(0, sb.toString().length() - 1);
                                    houseInfoSubBean.tag = tagSub;
                                } else {
                                    houseInfoSubBean.tag = "-1";
                                }

                                showLoading();
                                mPresenter.getSub(houseInfoSubBean);
                            }

                        }
                    }
                } else {
                    startActivityForResult(new Intent(NowSubActivity.this, LoginByVerifyCodeActivity.class), 1);
                }
                break;
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            infoSet();
            LoginResult loginCache = dao.getLoginCache();
            if (loginCache != null) {
                userCode = loginCache.userCode;
                token = loginCache.token;
            }
        }
    }

    @Override
    public void showSuccess() {
        cancelLoading();
//        if(isSubSuc){
            CustomerUtils.showTip(this, "操作成功");
//        }else {
//            CustomerUtils.showTip(this, "暂无订阅");
//        }
        finish();
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
//        CustomerUtils.showTip(this, msg);
    }

    @Override
    public void getVerifyCodeSucc() {
    }

    /**
     * 我的订阅
     *
     * @param mySubList
     */
    @Override
    public void showSubList(List<String> mySubList) {
        cancelLoading();
        if (mySubList != null) {

            for (String tag :
                    mySubList) {
                tagMap.put(tag, tag);
            }

            if (mySubList.contains("0")) {
                ((CheckBox) flowViewGroup.getChildAt(0)).setChecked(true);
                ((CheckBox) flowViewGroup.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
            }

            if (mySubList.contains("1")) {
                ((CheckBox) flowViewGroup.getChildAt(1)).setChecked(true);
                ((CheckBox) flowViewGroup.getChildAt(1)).setTextColor(getResources().getColor(R.color.white));
            }

            if (mySubList.contains("2")) {
                ((CheckBox) flowViewGroup.getChildAt(2)).setChecked(true);
                ((CheckBox) flowViewGroup.getChildAt(2)).setTextColor(getResources().getColor(R.color.white));
            }
        }

    }

    @Override
    public void showHouseSubNum(HouseSubNumBean numBean) {
//        tvPeople.setText(numBean.count + "人");
    }
}
