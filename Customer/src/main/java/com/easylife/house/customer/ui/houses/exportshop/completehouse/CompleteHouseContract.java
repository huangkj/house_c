package com.easylife.house.customer.ui.houses.exportshop.completehouse;

import com.easylife.house.customer.bean.ExportCompeleteHomeBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 */

public class CompleteHouseContract {

    interface View extends BaseView{

        void showCompleteHome(List<ExportCompeleteHomeBean> saleHousesBeanList);

        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void requestCompleteHome(String brokeCode,String pageSize);
    }
}
