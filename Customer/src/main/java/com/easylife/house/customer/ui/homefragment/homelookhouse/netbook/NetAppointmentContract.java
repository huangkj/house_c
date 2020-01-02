package com.easylife.house.customer.ui.homefragment.homelookhouse.netbook;

import com.easylife.house.customer.bean.NetAppointmentBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/29.
 */

public class NetAppointmentContract {
    interface  View extends BaseView{
        void showNetData(List<NetAppointmentBean> netList);

        void showDelSuc();

        void showFail();
    }

    interface Presenter extends BasePresenter<View>{
        void requestLookHouse(String userCode,String token,String type);

        void delLookHouse(String userCode,String token,String devId);
    }
}