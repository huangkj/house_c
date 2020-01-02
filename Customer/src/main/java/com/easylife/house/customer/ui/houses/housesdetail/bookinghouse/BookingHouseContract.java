package com.easylife.house.customer.ui.houses.housesdetail.bookinghouse;

import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

/**
 * Created by zgm on 2017/3/29.
 */

public class BookingHouseContract {

    interface  View extends BaseView{
        void  showSuccess();

        void showFail(NetBaseStatus code);

        void getVerifyCodeSucc();
    }

    interface Presenter extends BasePresenter<View>{

        void requestBookHouse(String devId,String userCode,String varifyCode,String token,String realName,String phone,String lookTime);

        void getVerifyCode(String phone);
    }
}
