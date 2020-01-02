package com.easylife.house.customer.ui.mine.calculator.business;

import android.content.Context;

import com.easylife.house.customer.bean.ResultRate;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class BusinessContract {
    interface View extends BaseView {

        void initNetData(List<ResultRate> data);

        void initChart(float f1, float f2, String moneyAll, String moneyAccrual, String paymentFirst, String paymentOther);
    }

    interface Presenter extends BasePresenter<View> {

        void getRate();

        void showResult(boolean isAccrual, String money, String year, double rate);
    }
}
