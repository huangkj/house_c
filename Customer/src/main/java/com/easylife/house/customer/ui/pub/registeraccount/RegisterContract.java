package com.easylife.house.customer.ui.pub.registeraccount;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

public class RegisterContract {
    interface View extends BaseView {


        void getVerifyCodeFail(String msg);

        void getVerifyCodeSucc();


        void userInfoSuc();
    }

    interface Presenter extends BasePresenter<View> {




        void submit(String pass);


        void getVerifyCode(String phone);

        void register(String name, String phone, String verifyCode, String pass, boolean isAgree);
    }
}
