package com.easylife.house.customer.ui.mine.brokerage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BankCardListBean;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.MyBrokerageBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.card.MyBankCardActivity;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.util.MoneyUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MyBrokerageActivity extends BaseActivity {

    @Bind(R.id.tvShouldBrokerageTx)
    TextView tvShouldBrokerageTx;
    @Bind(R.id.tvShouldBrokerage)
    TextView tvShouldBrokerage;
    @Bind(R.id.tvPredictBrokerage)
    TextView tvPredictBrokerage;
    @Bind(R.id.tvWaitBrokerage)
    TextView tvWaitBrokerage;
    @Bind(R.id.tvShouldTax)
    TextView tvShouldTax;
    @Bind(R.id.relBrokerage)
    RelativeLayout relBrokerage;
    @Bind(R.id.btnConfirm)
    ButtonTouch btnConfirm;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_my_brokerage, null);
    }

    @Override
    protected void initView() {
//        findViewById(R.id.content).setFitsSystemWindows(true);
//        StatusBarUtils.setColor(this,getResources().getColor(R.color.bg_main_gray),0);
//        StatusBarUtils.setLightMode(this);
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setText("结佣记录");

        mDao.myBreokerage(0, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                MyBrokerageBean data = GsonUtils.fromJson(response, MyBrokerageBean.class);
                if (data != null) {

                    tvShouldBrokerage.setText(MoneyUtils.moneyFormat(data.getShouldAmount()));

                    String estimateAmount = data.getEstimateAmount();
                    tvPredictBrokerage.setText(MoneyUtils.moneyFormat1(estimateAmount));

                    String surplusAmount = data.getSurplusAmount();
                    tvWaitBrokerage.setText(MoneyUtils.moneyFormat1(surplusAmount));

                    String taxAmount = data.getTaxAmount();
                    tvShouldTax.setText(MoneyUtils.moneyFormat4(taxAmount));
                }
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {

            }
        });
    }


    @Override
    protected void setActionBarDetail() {

    }

    @OnClick({R.id.btnRightText, R.id.btnConfirm})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btnRightText://结佣记录
                startActivity(new Intent(this, BrokerageRecordActivity.class));
                break;

            case R.id.btnConfirm://申请结佣
                Customer customer = mDao.getCustomer();

//                startActivity(new Intent(MyBrokerageActivity.this, ApplyBrokerageActivity.class));

//
//                if (TextUtils.isEmpty(customer.identityCardNum)) {
//                    ToastUtils.showShort("请先进行身份认证");
//                    return;
//                }
//
//                if (TextUtils.isEmpty(customer.transactionPassword)) {
//                    ToastUtils.showShort("设置交易密码");
//                    return;
//                }


                showLoading();
                mDao.bankCardList(0, new RequestManagerImpl() {
                    @Override
                    public void onSuccess(String response, int requestType) {
                        cancleLoading();
                        List<BankCardListBean> listData = new Gson().fromJson(response, new TypeToken<List<BankCardListBean>>() {
                        }.getType());
                        if (listData != null && listData.size() > 0) {
                            ApplyBrokerageActivity.startActivity(MyBrokerageActivity.this, 0);
                        } else {
//                            ToastUtils.showShort("请先绑定银行卡");
                            showDialog();
                        }

                    }

                    @Override
                    public void onFail(NetBaseStatus code, int requestType) {
                        cancleLoading();
                        showDialog();
//                        ToastUtils.showShort(code.msg);
//                        ToastUtils.showShort("请先绑定银行卡");
                    }
                });

                break;
        }
    }

    private void showDialog() {
        new MaterialDialog.Builder(MyBrokerageActivity.this).content("请先绑定结佣银行卡").positiveText("确认").negativeText("暂不").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                startActivity(new Intent(MyBrokerageActivity.this, MyBankCardActivity.class));
            }
        }).show();
    }
}
