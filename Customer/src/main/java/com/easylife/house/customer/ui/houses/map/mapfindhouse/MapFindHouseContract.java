package com.easylife.house.customer.ui.houses.map.mapfindhouse;

import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.core.LatLonPoint;
import com.easylife.house.customer.bean.AreaHousesNum;
import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class MapFindHouseContract {
    interface View extends BaseView {
        void moveToCityCenter();

        void initCity(List<City> data);

        void initCloudResult(CloudResult result);

        void initSearchResultDev(List<HousesDetailBaseBean> results);

        void initSearchResultArea(List<AreaHousesNum> results);

        void addMarker(LatLonPoint latLonPoint, String title, String snippet);

        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View> {

        void getCityList();

        void getSearchResultDev(SearchRequestBean searchRequestBean);

        void getSearchResultArea(SearchRequestBean searchRequestBean);
    }
}
