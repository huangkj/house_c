package com.easylife.house.customer.ui.mine.recommenapp;

import android.util.ArrayMap;

import com.easylife.house.customer.bean.RecommenApp;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class RecommenAppPresenter extends BasePresenterImpl<RecommenAppContract.View> implements RecommenAppContract.Presenter, RequestManagerImpl {


    @Override
    public void getNetData() {
        String url = "customer/selectRecommendedApp";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().userCode);
        mapData.put("token", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().token);
        mDao.manager.post(1, url, mapData, this, false);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<RecommenApp> apps = new Gson().fromJson(response, new TypeToken<List<RecommenApp>>() {
        }.getType());
        if (mView != null) mView.initData(apps);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
    }
}
