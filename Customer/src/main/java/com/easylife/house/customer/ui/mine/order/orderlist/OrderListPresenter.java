package com.easylife.house.customer.ui.mine.order.orderlist;

import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class OrderListPresenter extends BasePresenterImpl<OrderListContract.View> implements OrderListContract.Presenter, RequestManagerImpl {

    @Override
    public void getNetData(String type, int page) {
        mDao.getOrderList(1, type, page, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<Order> apps = new Gson().fromJson(response, new TypeToken<List<Order>>() {
        }.getType());
        if (mView != null) mView.initData(apps);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null) mView.getDataFail();
    }
}
