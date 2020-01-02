package com.easylife.house.customer.ui.houses.housesdetail.nowsub;

import com.easylife.house.customer.bean.HouseInfoSubBean;
import com.easylife.house.customer.bean.HouseSubNumBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/4/7.
 */

public class NowSubContract {

    interface View extends BaseView{
        void showSuccess();

        void showFail(String msg);

        void getVerifyCodeSucc();

        void showSubList(List<String> mySubList);

        void showHouseSubNum(HouseSubNumBean numBean);
    }

    interface Presenter extends BasePresenter<View>{
        void getSub(HouseInfoSubBean houseInfoSubBean);

        void getVerifyCode(String phone);

        void getSubList(String userCode,String token,String projectId,String devId);

        void getHousesSubNum(String devId,String projectId);
    }


}
