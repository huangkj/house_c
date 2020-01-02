package com.easylife.house.customer.ui.mine.minebuyhouse;

import android.util.ArrayMap;

import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;


public class MineBuyHousePresenter extends BasePresenterImpl<MineBuyHouseContract.View> implements MineBuyHouseContract.Presenter, RequestManagerImpl {

    @Override
    public void getCityList() {
        if (mView != null) mView.showLoading();
        mDao.selectArea(2, "1", "0", this);
    }

    /**
     * "buyCity": "110100",
     * "budget": "100",预算
     * "loan": "0",0贷款，1不贷款
     * "structure": "一居室",
     * "intentDev": "意向楼盘，最多50字"
     */
    @Override
    public void submit(String buyCity, String budget, String loan, String structure, String intentDev) {
        String url = "customer/wantBuyHouse";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().userCode);
        mapData.put("token", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().token);
        mapData.put("buyCity", buyCity);
        mapData.put("budget", budget);
        mapData.put("loan", loan);
        mapData.put("structure", structure);
        mapData.put("intentDev", intentDev);
        mDao.manager.post(1, url, mapData, this, false);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView == null)
            return;
        mView.cancelLoading();
        switch (requestType) {
            case 1:
                mView.submitSucc();
                break;
            case 2:
                List<City> citys = new Gson().fromJson(response, new TypeToken<List<City>>() {
                }.getType());
                mView.initCity(citys);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null) mView.cancelLoading();
    }
}
