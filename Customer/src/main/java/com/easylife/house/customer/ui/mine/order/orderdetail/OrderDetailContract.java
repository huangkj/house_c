package com.easylife.house.customer.ui.mine.order.orderdetail;

import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.bean.RecommenApp;
import com.easylife.house.customer.bean.ResultCheckOrder;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class OrderDetailContract {
    interface View extends BaseView {
        void initData(Order order);

        void refund();

        void showResultCancelOrder(boolean isSuccess);
    }

    interface Presenter extends BasePresenter<View> {
        void getNetData(String orderCode);

        void cancelOrder(String orderCode);
    }
}
