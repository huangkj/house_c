package com.easylife.house.customer.ui.houses.map.mapfindhouse;

import android.util.ArrayMap;

import com.easylife.house.customer.bean.AreaHousesNum;
import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;


public class MapFindHousePresenter extends BasePresenterImpl<MapFindHouseContract.View> implements MapFindHouseContract.Presenter, RequestManagerImpl {

    @Override
    public void getCityList() {
        if (mView != null) mView.showLoading();
        mDao.selectSaleCity(2, this);
    }

    @Override
    public void getSearchResultDev(SearchRequestBean searchRequestBean) {
        if (mView != null)  mView.showLoading();
        mDao.getSearchData(1, searchRequestBean, SearchSingleton.getIstance().searchtype,null, "1", this);
    }

    @Override
    public void getSearchResultArea(SearchRequestBean searchRequestBean) {
        if (mView != null)   mView.showLoading();
        String url = Constants.SEARCH_HOST + "customer/districtDevNum";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityName", searchRequestBean.city);
        mDao.manager.post(3, url, mapData, this, false);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
            case 1:
                // 搜索结果
                List<HousesDetailBaseBean> houses = new Gson().fromJson(response, new TypeToken<List<HousesDetailBaseBean>>() {
                }.getType());
                mView.initSearchResultDev(houses);
                break;
            case 2:
                List<City> citys = new Gson().fromJson(response, new TypeToken<List<City>>() {
                }.getType());
                mView.initCity(citys);
                break;
            case 3:
                List<AreaHousesNum> results = new Gson().fromJson(response, new TypeToken<List<AreaHousesNum>>() {
                }.getType());
                mView.initSearchResultArea(results);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView == null)
            return ;
        if (code != null) {
            mView.showFail(code.msg);
        } else {
            mView.showFail("请求失败");
        }
        switch (requestType) {
            case 1:
                mView.initSearchResultDev(null);
                break;
            case 2:
                mView.initCity(null);
                break;
            case 3:
                mView.initSearchResultArea(null);
                break;
        }
    }
}
