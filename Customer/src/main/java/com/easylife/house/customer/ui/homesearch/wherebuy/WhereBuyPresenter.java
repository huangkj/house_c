package com.easylife.house.customer.ui.homesearch.wherebuy;

import android.text.TextUtils;

import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.bean.SearchHistoryBean;
import com.easylife.house.customer.dao.LocalDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgm on 2017/3/20.
 */

public class WhereBuyPresenter extends BasePresenterImpl<WhereBuyContract.View> implements WhereBuyContract.Presenter, RequestManagerImpl {


    @Override
    public void getCityList() {
        mDao.selectSaleCity(2, this);
    }

    @Override
    public void getSearchHistory(LocalDao localDao) {
        List<SearchHistoryBean> historyList = null;
        String searchHistoryJson = localDao.getString(localDao.SEARCH_HISTORY, localDao.SEARCH_HISTORY_VALUE, "");
        if (!TextUtils.isEmpty(searchHistoryJson)) {
//            SearchHistoryBean searchHistoryBean = new Gson().fromJson(searchHistoryJson, SearchHistoryBean.class);
            historyList = new Gson().fromJson(searchHistoryJson, new TypeToken<List<SearchHistoryBean>>() {
            }.getType());
//            historyList =  searchHistoryBean.historyList;
        }

        if (mView != null) mView.showSearchHistory(historyList);
    }

    @Override
    public void saveSearchHistory(LocalDao localDao, SearchHistoryBean historyBean) {
        if (historyBean != null) {
            List<SearchHistoryBean> historyList = null;

            String searchHistoryJson = localDao.getString(localDao.SEARCH_HISTORY, localDao.SEARCH_HISTORY_VALUE, "");

            if (TextUtils.isEmpty(searchHistoryJson)) {
                historyList = new ArrayList<>();
            } else {
                historyList = new Gson().fromJson(searchHistoryJson, new TypeToken<List<SearchHistoryBean>>() {
                }.getType());
//                historyList = searchHistoryBean.historyList;
            }

            if (historyList.size() >= 5) {
                historyList.remove(4);
                historyList.add(0, historyBean);
            } else {
                historyList.add(historyBean);
            }
//            Map<String,List<String>> historyMap = new HashMap<>();
//            historyMap.put("historyList",historyList);
            String historyStr = new Gson().toJson(historyList);
            localDao.saveString(localDao.SEARCH_HISTORY, localDao.SEARCH_HISTORY_VALUE, historyStr);
        }
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<City> citys = new Gson().fromJson(response, new TypeToken<List<City>>() {
        }.getType());
        if (mView != null) {
            mView.initCity(citys);
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
