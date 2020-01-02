package com.easylife.house.customer.ui.homesearch.allhouse;

import com.easylife.house.customer.bean.CityAreaBean;
import com.easylife.house.customer.bean.HomeSearchRequestBean;
import com.easylife.house.customer.bean.HomeSearchResponseBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

public class AllHouseContract {

    interface View extends BaseView {
        void showSuccessData(List<HomeSearchResponseBean> baseBeanList);

        void areasSuccessData(List<CityAreaBean> areas);

        void showFail(String code);
    }
    interface Presenter extends BasePresenter<View> {
        void requestHomeSearch(int requestType, int pageNum, HomeSearchRequestBean bean);

        void requestArea(int requestType, String type, String id);
    }


}
