package com.easylife.house.customer.ui.mine.order;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Voucher;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.google.gson.Gson;

import butterknife.Bind;

/**
 * Created by Mars on 2017/6/26 10:30.
 * 描述：优惠凭证
 */

public class FavorableCertificateActivity extends BaseActivity implements RequestManagerImpl {
    public static void startActivity(Activity activity, String orderCode, int requestCode) {
        activity.startActivityForResult(new Intent(activity, FavorableCertificateActivity.class).putExtra("orderCode", orderCode), requestCode);
    }

    @Bind(R.id.tvStatus)
    TextView tvStatus;
    @Bind(R.id.tvOrderNo)
    TextView tvOrderNo;
    @Bind(R.id.tvDevName)
    TextView tvDevName;
    @Bind(R.id.tvTextFavorable)
    TextView tvTextFavorable;
    @Bind(R.id.tvCustomerName)
    TextView tvCustomerName;
    @Bind(R.id.tvCustomerPhone)
    TextView tvCustomerPhone;
    @Bind(R.id.tvPayType)
    TextView tvPayType;
    @Bind(R.id.tvPayReal)
    TextView tvPayReal;
    @Bind(R.id.tvPayDate)
    TextView tvPayDate;
    @Bind(R.id.tvNameReceipt)
    TextView tvNameReceipt;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.order_activity_favorable_certificate, null);
    }

    private String orderCode;

    @Override
    protected void initView() {
        orderCode = getIntent().getStringExtra("orderCode");

        mDao.orderVoucher(1, orderCode, this);
    }

    @Override
    protected void setActionBarDetail() {

    }

    @Override
    public void onSuccess(String response, int requestType) {
        /**
         * /**
         * "orderType": 0,
         * "customerPhone": "",
         * "payType": "WECHAT",
         * "createTime": "1970年01月18日 16:04:16",
         * "paid": 5,
         * "orderCode": "1000002109392384",
         * "devName": "0512三端楼盘房源楼盘名称",
         * "Payee": "好生活",
         * "customerName": "111"
         */

        Voucher vo = new Gson().fromJson(response, Voucher.class);
        if (vo != null) {
            tvDevName.setText(vo.devName);
            tvCustomerName.setText(vo.customerName);
            tvCustomerPhone.setText(vo.customerPhone);
            tvStatus.setText(vo.orderType);

            tvOrderNo.setText(vo.orderCode);
            tvNameReceipt.setText(vo.Payee);
            tvTextFavorable.setText(vo.preferentialWay);
            tvPayType.setText(vo.payType);
            tvPayDate.setText(vo.createTime);
            tvPayReal.setText(vo.paid + "元");
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
