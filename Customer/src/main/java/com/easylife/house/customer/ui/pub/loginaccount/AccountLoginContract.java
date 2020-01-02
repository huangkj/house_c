package com.easylife.house.customer.ui.pub.loginaccount;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

public class AccountLoginContract {
    interface View extends BaseView {
        void loginFail(String msg);

        void loginSucc();

        void userInfoSuc();

        void loginThirdSucc();


        void startBindPhone(String easeUsername, String easePassword);
    }


    interface Presenter extends BasePresenter<View> {
        void loginByNormal(String phone, String pass);

        void getUserInfo();

        void loginByOther(String name, String tid, String headImg, String type);
    }


}
