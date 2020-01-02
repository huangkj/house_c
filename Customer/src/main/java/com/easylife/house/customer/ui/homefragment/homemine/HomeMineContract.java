package com.easylife.house.customer.ui.homefragment.homemine;

import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

public class HomeMineContract {
    interface View extends BaseView {
        void initUserInfo(Customer info);
    }

    interface Presenter extends BasePresenter<View> {
        boolean isLogin();

        void getUserInfoCache();

        void getUserInfo();
    }
}
