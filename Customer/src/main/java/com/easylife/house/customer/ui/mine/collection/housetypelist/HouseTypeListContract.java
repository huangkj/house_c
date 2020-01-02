package com.easylife.house.customer.ui.mine.collection.housetypelist;

import com.easylife.house.customer.bean.HouseColletion;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class HouseTypeListContract {
    interface View extends BaseView {
        void initData(List<HouseColletion> apps);

        void showFail();
    }

    interface Presenter extends BasePresenter<HouseTypeListContract.View> {
        void getNetData();
    }
}
