package com.easylife.house.customer.ui.houses.housesdetail.newdynamic;

import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/25.
 */

public class NewDynamicContract {
    interface View extends BaseView{
        void showMoreData(List<HousesDynamicBean> dynamicList);
        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void loadMoreData(String projectId,String pageSize);
    }
}
