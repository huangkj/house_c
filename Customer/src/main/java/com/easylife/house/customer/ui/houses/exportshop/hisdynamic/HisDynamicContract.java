package com.easylife.house.customer.ui.houses.exportshop.hisdynamic;

import com.easylife.house.customer.bean.ExportHisDymanicBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 */

public class HisDynamicContract {

    interface View extends BaseView{
        void showDynamic(List<ExportHisDymanicBean> exportHisDymanicBeanList);

        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void requestHisDymanic(String brokeCode,String pageSize);
    }
}
