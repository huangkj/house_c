package com.easylife.house.customer.ui.mine.collection.houselist;

import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

public class HouseListContract {
    interface View extends BaseView {
        void initData(List<HousesDetailBaseBean> apps);
        void showFail();
    }

    interface Presenter extends BasePresenter<View> {
        void getNetData();
    }
}
