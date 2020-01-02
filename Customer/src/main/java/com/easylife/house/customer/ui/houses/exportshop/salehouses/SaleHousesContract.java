package com.easylife.house.customer.ui.houses.exportshop.salehouses;

import com.easylife.house.customer.bean.ExportSaleHousesBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 */

public class SaleHousesContract {

    interface View extends BaseView{
        void showHousesData(List<ExportSaleHousesBean> saleHousesBeanList);

        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void requestSaleHouses(String brokeCode,String pageSize);
    }
}
