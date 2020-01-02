package com.easylife.house.customer.ui.houses.housesdetail.getdiscount;

import com.easylife.house.customer.bean.GitDisCountBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

/**
 * Created by zgm on 2017/3/29.
 */

public class GetDiscountContract {

    interface View extends BaseView{
        void showFail(String msg);

        void showDisCount(GitDisCountBean gitDisCountBean);

        void showSuccDis(String msg);

        void getVerifyCodeSucc();
    }

    interface Presenter extends BasePresenter<View>{
        void getVerifyCode(String phone);

        void commitGetDis(String devId,String userCode,String token,String phone,String varifyCode,String naem,String ruleId);

        void getDisCount(String devId);
    }
}
