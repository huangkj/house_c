package com.easylife.house.customer.ui.homesearch.recenthouse;

import com.easylife.house.customer.bean.CityAreaBean;
import com.easylife.house.customer.bean.HomeSearchRequestBean;
import com.easylife.house.customer.bean.HomeSearchResponseBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

public class RecentHouseContract {

    interface View extends BaseView {
        void showSuccessData(List<HomeSearchResponseBean> baseBeanList);

        void areasSuccessData(List<CityAreaBean> areas);

        void showFail(String code);

        void showNearDevs(List<HomeSearchResponseBean> baseBeanList);
    }

    interface Presenter extends BasePresenter<View> {
        void requestHomeSearch(int requestType, int pageNum, HomeSearchRequestBean bean);

        void requestArea(int requestType, String type, String id);

        void requestNearDevs(int requestType, String cityId, List<Double> coordinate);
    }


}
