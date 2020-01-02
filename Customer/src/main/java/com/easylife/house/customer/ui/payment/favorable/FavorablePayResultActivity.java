package com.easylife.house.customer.ui.payment.favorable;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.ResultCheckOrder;
import com.easylife.house.customer.ui.houses.housepincontrol.HousePinControlActivity;
import com.easylife.house.customer.ui.mine.order.orderdetail.OrderDetailActivity;
import com.easylife.house.customer.view.ButtonTouch;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Mars on 2017/4/7 15:02.
 * 描述：支付结果页面
 */

public class FavorablePayResultActivity extends BaseActivity {
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvUserPhone)
    TextView tvUserPhone;
    @Bind(R.id.tvMoneyPay)
    TextView tvMoneyPay;
    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.btnReturn)
    ButtonTouch btnReturn;
    @Bind(R.id.btnOrder)
    ButtonTouch btnOrder;

    public static void startActivity(Activity activity, String orderCode, ResultCheckOrder resultCheckOrder, int requestCode) {
        activity.startActivityForResult(new Intent(activity, FavorablePayResultActivity.class)
                        .putExtra("orderCode", orderCode)
                        .putExtra("resultCheckOrder", resultCheckOrder)
                , requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.payment_activity_pay_succ, null);
    }

    private ResultCheckOrder result;
    private String orderCode;

    @Override
    protected void initView() {
        orderCode = getIntent().getStringExtra("orderCode");
        result = (ResultCheckOrder) getIntent().getSerializableExtra("resultCheckOrder");
        initOrderCheckResult(result);
    }

    @Override
    protected void setActionBarDetail() {
    }

    @OnClick({R.id.btnReturn, R.id.btnOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReturn:
                if (result != null && ResultCheckOrder.CheckState.ALL.name().equals(result.settleStatus)
                        && "2".equals(result.coop) && "1".equals(result.isTransparent)) {
                    HousePinControlActivity.startActivity(activity, result.devId, 0);
                } else {
                    finish();
                }
                break;
            case R.id.btnOrder:
                OrderDetailActivity.startActivity(activity, orderCode, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }


    private void initOrderCheckResult(ResultCheckOrder checkOrder) {
        boolean paidAll = ResultCheckOrder.CheckState.ALL.name().equals(checkOrder.settleStatus);
        if (paidAll) {
            setResult(RESULT_OK, new Intent().putExtra("paidAll", false));
            if ("2".equals(result.coop) && "1".equals(checkOrder.isTransparent)) {
                btnReturn.setText("认购下定");
            } else {
                btnReturn.setText("完成");
            }
        } else {
            setResult(RESULT_OK, new Intent().putExtra("paidAll", true));
            btnReturn.setText("继续支付");
        }

        tvUserName.setText(checkOrder.customerName);
        tvUserPhone.setText(checkOrder.customerPhone);
        tvMoneyPay.setText("￥" + checkOrder.payOnThisTime);
    }

}
