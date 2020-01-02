package com.easylife.house.customer.ui.payment.subscription;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.TextureView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.bean.OrderParameter;
import com.easylife.house.customer.bean.ResultCheckOrder;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.payment.PayTypeActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.util.PubTipDialog;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.easylife.house.customer.R.id.btnGetVerifyCode;
import static com.easylife.house.customer.R.id.layVerifyCode;
import static com.easylife.house.customer.R.id.tvPayment;
import static com.easylife.house.customer.R.id.tvPaymentAll;
import static com.easylife.house.customer.R.id.tvPhone;
import static com.easylife.house.customer.R.id.tvSelectArea;
import static com.easylife.house.customer.R.mipmap.phone;
import static com.easylife.house.customer.util.DoubleFomat.format2;

/**
 * Created by Mars on 2017/6/27 15:31.
 * 描述：选房购买-支付认购-流程2
 */

public class HousePayActivity extends BaseActivity implements RequestManagerImpl {
    public static void startActivity(Activity activity,
                                     OrderParameter parameter, String orderCode,
                                     int requestCode) {
        activity.startActivityForResult(new Intent(activity, HousePayActivity.class)
                        .putExtra("parameter", parameter)
                        .putExtra("orderCode", orderCode)
                , requestCode);
    }

    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.tvHouseName)
    TextView tvHouseName;
    @Bind(R.id.tvHousePrice)
    TextView tvHousePrice;
    @Bind(R.id.tvHousePriceHad)
    TextView tvHousePriceHad;
    @Bind(R.id.tvHousePriceWait)
    TextView tvHousePriceWait;
    @Bind(R.id.tvPriceTotal)
    TextView tvPriceTotal;
    @Bind(R.id.btnPayAll)
    RadioButton btnPayAll;
    @Bind(R.id.btnPayBatches)
    RadioButton btnPayBatches;
    @Bind(R.id.tvMoneyReal)
    EditText tvMoneyReal;
    @Bind(R.id.tvMoneySurplus)
    TextView tvMoneySurplus;
    @Bind(R.id.layPayBatches)
    LinearLayout layPayBatches;
    @Bind(R.id.cbAgreement)
    CheckBox cbAgreement;
    @Bind(R.id.tvAgreement)
    TextView tvAgreement;
    @Bind(R.id.tvMoneyBottom)
    TextView tvMoneyBottom;
    @Bind(R.id.tvDesc)
    TextView tvDesc;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.payment_activity_house_pay, null);
    }

    private OrderParameter parameter;
    private ResultCheckOrder orderData;
    private String orderCode;
    private String localSerial;
    private String temp;
    private String paymentStr, paymentRealStr;
    private String devName;

    @Override
    protected void initView() {
        parameter = (OrderParameter) getIntent().getSerializableExtra("parameter");
        orderCode = getIntent().getStringExtra("orderCode");
        localSerial = getIntent().getStringExtra("localSerial");

        if (TextUtils.isEmpty(orderCode)) {
            initData();
        } else {
            // 订单列表或者订单详情进入，查询订单当前状态
            mDao.checkOrderState(1, orderCode, this);
        }


        tvMoneyReal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 随时计算
                if (TextUtils.isEmpty(s.toString()))
                    return;
                double m = Double.parseDouble(paymentStr);
                double cur = Double.parseDouble(s.toString());
                if (cur > m) {
                    tvMoneyReal.setText(temp);
                    tvMoneyBottom.setText(temp);
                    return;
                }
                tvMoneySurplus.setText(temp);
                paymentRealStr = format2(cur);
                tvMoneyBottom.setText(format2(m - cur));
            }
        });
    }

    private void initData() {
        if (parameter != null) {
            devName = parameter.estateProjectDevName;

            tvHouseName.setText(parameter.estateProjectDevName + parameter.houseNo);
            tvHousePrice.setText(parameter.orderLog.pay);
            tvHousePriceHad.setText("0");
            tvHousePriceWait.setText(parameter.orderLog.pay);
            tvPriceTotal.setText(parameter.orderLog.pay);


            paymentStr = paymentRealStr = parameter.orderLog.pay;
            tvMoneyReal.setText(parameter.orderLog.pay);
            tvMoneyBottom.setText(parameter.orderLog.pay);
        } else if (!TextUtils.isEmpty(orderCode)) {
            // TODO check
            devName = orderData.devName;
            //                "customerPhone": "13811857732",
//               "payOnThisTime": "1000.0",
//               "settleStatus": "NOT",
//               "cellName": "1#1单元-1-101室",
//               "paid": "0.0",
//               "pay": "20000.0",
//               "localSerial": "594c8c0900cc0b420581dbe9",
//               "tobepay": "20000.0",
//               "bankSerial": "null",
//               "customerName": "袁静",
//               "isSuccess": "false"

            tvHouseName.setText(orderData.devName + orderData.cellName);
            tvHousePrice.setText(orderData.pay);
            tvHousePriceHad.setText(orderData.paid);
            tvHousePriceWait.setText(orderData.tobepay);
            tvPriceTotal.setText(orderData.pay);

            paymentStr = paymentRealStr = orderData.tobepay;
            tvMoneyReal.setText(orderData.tobepay);
            tvMoneyBottom.setText(orderData.tobepay);
        }
    }

    @Override
    protected void setActionBarDetail() {

    }

    private PubTipDialog dialog;

    private void showTip() {
        if (dialog == null) {
            dialog = new PubTipDialog(this, null);
        }
        dialog.showPayTip(R.layout.pub_toast_tip_pay_desc);
    }

    @OnClick({R.id.btnPayAll, R.id.btnPayBatches, R.id.cbAgreement, R.id.tvAgreement, R.id.btnSubmit, R.id.tvDesc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDesc:
                showTip();
                break;
            case R.id.btnPayAll:
                tvMoneyReal.setFocusable(false);
                tvMoneyReal.setFocusableInTouchMode(false);

                tvMoneyReal.setText(paymentRealStr);
                break;
            case R.id.btnPayBatches:
                tvMoneyReal.setFocusable(true);
                tvMoneyReal.setFocusableInTouchMode(true);

                tvMoneyReal.setText("0");
                tvMoneyBottom.setText(paymentRealStr);
                break;
            case R.id.tvAgreement:
                //   跳转认购通知书web
                WebViewActivity.startActivity(activity, devName + " 认购通知书",
                        Constants.URL_FAVORABLE_DEV + orderCode);
                break;
            case R.id.btnSubmit:
                if (btnPayAll.isChecked()) {
                    // 全款
                    parameter.orderLog.payOnThisTime = parameter.orderLog.pay;
                } else {
                    // 分批
                    String pay = tvMoneyReal.getText().toString();
                    if (TextUtils.isEmpty(pay)) {
                        CustomerUtils.showTip(activity, "请输入本次支付的金额");
                        return;
                    }
                    parameter.orderLog.payOnThisTime = pay;
                }
                if (!cbAgreement.isChecked()) {
                    CustomerUtils.showTip(activity, "请先查看并同意认购通知书");
                    return;
                }
                String orderName = parameter.estateProjectDevName + "楼盘预定金";
                String orderMark = "预购认订金-[" + parameter.estateProjectDevName + parameter.houseNo + "]";
                PayTypeActivity.startActivity(activity, orderCode, localSerial, orderName, orderMark, parameter, 0);
                break;
        }
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                // 检查订单
                ResultCheckOrder data = new Gson().fromJson(response, ResultCheckOrder.class);
                if (data == null)
                    return;
                orderData = data;
                initData();
                break;
        }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
