package com.easylife.house.customer.ui.pub.forgetpassword;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;


public class ForgetPassWordContract {
    interface View extends BaseView {
        void submitFail(String msg);

        void submitSucc();

        void getVerifyCodeFail(String msg);

        void getVerifyCodeSucc();

        void updatePsw(String phone, String verifyCode,String pass);

    }

    interface Presenter extends BasePresenter<View> {

        void getVerifyCode(String phone);

        void submit(String phone, String verifyCode, String pass, String passAgain);

        void update(String phone, String verifyCode,String pass);
    }
}
