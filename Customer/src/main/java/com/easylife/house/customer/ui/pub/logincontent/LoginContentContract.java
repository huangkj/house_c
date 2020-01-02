package com.easylife.house.customer.ui.pub.logincontent;

import android.app.Activity;
import android.os.Bundle;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

public class LoginContentContract {
    interface View extends BaseView {

        void loginFail();

        void loginSuccIm(String easeUsername,String easePassword);

        void loginSucc();

    }

    interface Presenter extends BasePresenter<View> {
    }
}
