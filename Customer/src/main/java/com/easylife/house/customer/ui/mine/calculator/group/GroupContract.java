package com.easylife.house.customer.ui.mine.calculator.group;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;


public class GroupContract {
    interface View extends BaseView {
        void initChart(float f1, float f2,
                       String moneyAll, String moneyAccrual, String paymentFirst, String paymentOther
        );
    }

    interface Presenter extends BasePresenter<View> {
        void showResult(boolean isAccrual,
                        String moneyAcc, String yearAcc, double rateAcc,
                        String moneyBusiness, String yearBusiness, double rateBusiness
        );
    }
}
