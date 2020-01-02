package com.easylife.house.customer.ui.pub.loginbyverifycode;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;


public class LoginByVerifyCodeContract {
    interface View extends BaseView {
        void loginFail(String msg);

        void loginSucc();

        void getVerifyCodeFail(String msg);

        void getVerifyCodeSucc();

        void userInfoSuc();


        void loginThirdSucc();

        void startBindPhone();
    }

    interface Presenter extends BasePresenter<View> {
        void getVerifyCode(String phone);

        void loginByVerifyCode(String phone, String verifyCode, boolean isAgree);

        void getUserInfo();

        void loginByOther(String name, String tid, String headImg, String type);

    }
}
