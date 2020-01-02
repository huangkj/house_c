package com.easylife.house.customer.ui.mine.calculator.rate;

import com.easylife.house.customer.bean.ResultRate;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

public class RateContract {
    interface View extends BaseView {
        void initNetData(List<ResultRate> data);
    }

    interface Presenter extends BasePresenter<View> {
        void getNetRate(boolean isBusiness);
    }
}
