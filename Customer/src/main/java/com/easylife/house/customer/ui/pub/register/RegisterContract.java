package com.easylife.house.customer.ui.pub.register;

import android.widget.CheckBox;
import android.widget.EditText;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

public class RegisterContract {
    interface View extends BaseView {
        void submitFail(String msg);

        void submitSucc();

        void getVerifyCodeFail(String msg);

        void getVerifyCodeSucc();

        void showNextView(int position);

        int getViewIndex();

        void valiSuc(String pass);
    }

    interface Presenter extends BasePresenter<View> {

        void submitName(String name, CheckBox cbAgree);

        void submitPhone(String phone);

        void submitVerifyCode(String verifyCode);

        void submit(String pass);

        void exchangePassWordState(EditText edPass, boolean isShowing);

        void getVerifyCode(String phone);

        void register(String name, String phone, String verifyCode, String pass);
    }
}
