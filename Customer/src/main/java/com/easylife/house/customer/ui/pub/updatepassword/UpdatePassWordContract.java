package com.easylife.house.customer.ui.pub.updatepassword;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;


public class UpdatePassWordContract {
    interface View extends BaseView {
        void updateSuccess();

        void updateFail(String msg);
    }

    interface Presenter extends BasePresenter<View> {
        void updatePassWord(String pass, String passNew, String passNewAgain);
    }
}
