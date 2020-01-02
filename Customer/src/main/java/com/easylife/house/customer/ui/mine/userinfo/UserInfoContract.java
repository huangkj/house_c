package com.easylife.house.customer.ui.mine.userinfo;

import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

public class UserInfoContract {
    interface View extends BaseView {

        void initUserInfo(Customer info);

        void saveSucc();

        void saveFail();

        void updateUserHeaderSucc(String imgPath);
    }

    interface Presenter extends BasePresenter<View> {
        void getCacheUserInfo();

        void uploadUserHeader(String imgPath);

        void saveUserInfo(Customer customer);
    }
}
