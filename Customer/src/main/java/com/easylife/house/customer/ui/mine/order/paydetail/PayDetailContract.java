package com.easylife.house.customer.ui.mine.order.paydetail;

import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.bean.PayDetail;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class PayDetailContract {
    interface View extends BaseView {
        void initData(List<PayDetail> data);

        void getDataFail();
    }

    interface Presenter extends BasePresenter<View> {
        void getNetData(String type, String orderCode);
    }
}
