package com.easylife.house.customer.ui.mine.order.orderlist;

import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class OrderListContract {
    interface View extends BaseView {
        void initData(List<Order> data);

        void getDataFail();
    }

    interface Presenter extends BasePresenter<View> {
        void getNetData(String type, int page);
    }
}
