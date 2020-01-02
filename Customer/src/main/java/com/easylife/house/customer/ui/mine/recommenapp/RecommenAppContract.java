package com.easylife.house.customer.ui.mine.recommenapp;

import com.easylife.house.customer.bean.RecommenApp;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

public class RecommenAppContract {
    interface View extends BaseView {
        void initData(List<RecommenApp> apps);
    }

    interface Presenter extends BasePresenter<RecommenAppContract.View> {
        void getNetData();
    }
}
