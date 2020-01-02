package com.easylife.house.customer.ui.mine.order.orderdetail;

import android.text.TextUtils;

import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_ORDER_STATUS;

public class OrderDetailPresenter extends BasePresenterImpl<OrderDetailContract.View> implements OrderDetailContract.Presenter, RequestManagerImpl {

    @Override
    public void getNetData(String orderCode) {
        mDao.getOrderDetail(1, orderCode, this);
    }

    public void cancelOrder(String orderCode) {
        mDao.cancelOrder(2, orderCode, this);
    }

    public void applyRefund(String orderCode) {
        mDao.orderRefundCheck(3, orderCode, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    Order order = new Gson().fromJson(response, Order.class);
                    mView.initData(order);
                    break;
                case 2:
                    EventBus.getDefault().post(new MessageEvent(UPDATE_ORDER_STATUS, Order.OrderType.DEL.code));
                    mView.showResultCancelOrder(true);
                    break;
                case 3:
                    if (mView != null)
                        mView.refund();
                    break;
            }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    mView.initData(null);
                    break;
                case 2:
                    mView.showResultCancelOrder(false);
                    break;
                case 3:
                    mView.showTip(TextUtils.isEmpty(code.msg) ? "订单状态查询异常，请重试" : code.msg);
                    break;
            }
    }
}
