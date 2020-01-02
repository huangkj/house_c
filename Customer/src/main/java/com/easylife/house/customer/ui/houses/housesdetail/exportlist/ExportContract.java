package com.easylife.house.customer.ui.houses.housesdetail.exportlist;

import com.easylife.house.customer.bean.ExportListBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/27.
 */

public class ExportContract {

    interface View extends BaseView{
        void showExportList(List<ExportListBean> exportList);

        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void getExportList(String devId,String pageSize);
    }
}
