package com.easylife.house.customer.ui.mine.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.PayDetailAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.bean.PayDetailResult;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.google.gson.Gson;

import butterknife.Bind;

/**
 * Created by Mars on 2017/7/4 14:27.
 * 描述：认筹支付明细
 */

public class PayDetailRaiseActivity extends BaseActivity implements RequestManagerImpl {
    public static void startActivity(Activity activity, String orderCode, int requestCode) {
        activity.startActivityForResult(new Intent(activity, PayDetailRaiseActivity.class).putExtra("orderCode", orderCode), requestCode);
    }

    @Bind(R.id.tvOrderName)
    TextView tvOrderName;
    @Bind(R.id.tvOrderStatus)
    TextView tvOrderStatus;
    @Bind(R.id.tvOrderPrice)
    TextView tvOrderPrice;
    @Bind(R.id.tvOrderNo)
    TextView tvOrderNo;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.payment_activity_favorable_pay_detail, null);
    }

    private PayDetailAdapter adapter;
    private String orderCode;

    @Override
    protected void initView() {
        orderCode = getIntent().getStringExtra("orderCode");

        adapter = new PayDetailAdapter(R.layout.item_pay_detail, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNetData();
            }
        });
        getNetData();
    }

    private void getNetData() {
        mDao.getPayDetail(1, orderCode, Order.FollowType.RAISE.name(), this);
    }

    @Override
    protected void setActionBarDetail() {

    }

    @Override
    public void onSuccess(String response, int requestType) {
        swipeLayout.setRefreshing(false);
        PayDetailResult data = new Gson().fromJson(response, PayDetailResult.class);
        if (data == null)
            return;
        tvOrderName.setText(data.name);
        tvOrderPrice.setText(data.paid);
        String status = "";
        /**
         * PLACE(0, "已下单"), PAYED(1, "已付款"), SIGNED(2, "已签约"),
         REFUND(3, "已退款"), REFUNDING(4, "退款中"), REFUNDFIAL(5, "退款失败"), DEL(9, "已删除");
         */
        if (data.orderType == Order.OrderType.PLACE.code) {
            status = Order.OrderType.PLACE.msg;
        } else if (data.orderType == Order.OrderType.PAYED.code) {
            status = Order.OrderType.PAYED.msg;
        } else if (data.orderType == Order.OrderType.SIGNED.code) {
            status = Order.OrderType.SIGNED.msg;
        } else if (data.orderType == Order.OrderType.REFUND.code) {
            status = Order.OrderType.REFUND.msg;
        } else if (data.orderType == Order.OrderType.REFUNDING.code) {
            status = Order.OrderType.REFUNDING.msg;
        } else if (data.orderType == Order.OrderType.REFUNDFIAL.code) {
            status = Order.OrderType.REFUNDFIAL.msg;
        } else if (data.orderType == Order.OrderType.DEL.code) {
            status = Order.OrderType.DEL.msg;
        }
        tvOrderStatus.setText(status);
        tvOrderNo.setText(orderCode);

        adapter.setNewData(data.data);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        swipeLayout.setRefreshing(false);
    }
}
