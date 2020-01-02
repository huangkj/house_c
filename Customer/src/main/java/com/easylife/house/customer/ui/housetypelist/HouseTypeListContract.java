package com.easylife.house.customer.ui.housetypelist;

import com.easylife.house.customer.bean.HouseColletion;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class HouseTypeListContract {
    interface View extends BaseView {
        void initData(List<HouseColletion> apps);
    }

    interface Presenter extends BasePresenter<HouseTypeListContract.View> {
        void getNetData();
    }
}
