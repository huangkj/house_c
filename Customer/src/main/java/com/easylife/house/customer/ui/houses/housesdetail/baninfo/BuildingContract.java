package com.easylife.house.customer.ui.houses.housesdetail.baninfo;

import com.easylife.house.customer.bean.BuildingInfoBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/25.
 */

public class BuildingContract {

    interface View extends BaseView{
        void showBanInfo(List<BuildingInfoBean> mData);

        void showFail(String code);
    }

    interface Presenter extends BasePresenter<View>{
        void requestBanInfo(String devId);
    }
}
