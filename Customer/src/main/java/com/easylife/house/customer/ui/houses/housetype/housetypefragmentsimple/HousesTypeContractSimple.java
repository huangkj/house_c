package com.easylife.house.customer.ui.houses.housetype.housetypefragmentsimple;

import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/28.
 * 户型列表
 */

public class HousesTypeContractSimple {

   public interface View extends BaseView {
        void showSuccess(List<HousesTypeBean> beanList);

        void showFail(String msg);
    }

    public interface Presenter extends BasePresenter<View> {
        void requestHousesTypeList(String devId);
    }
}
