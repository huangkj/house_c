package com.easylife.house.customer.ui.houses.exportshop.hisidc;

import com.easylife.house.customer.bean.ExportHisIdcBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 */

public class HisIdcContract {

    interface View extends BaseView{
        void showIdcData(List<ExportHisIdcBean> exportHisIdcBeanList);
        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void requestHisIdc(String brokeCode);
    }
}
