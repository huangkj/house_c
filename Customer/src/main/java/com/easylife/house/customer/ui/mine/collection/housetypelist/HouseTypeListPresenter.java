package com.easylife.house.customer.ui.mine.collection.housetypelist;

import android.util.ArrayMap;

import com.easylife.house.customer.bean.HouseColletion;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;


public class HouseTypeListPresenter extends BasePresenterImpl<HouseTypeListContract.View> implements HouseTypeListContract.Presenter, RequestManagerImpl {

    @Override
    public void getNetData() {
        String url = "customer/selectMyCollection";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().userCode);
        mapData.put("token", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().token);
        mapData.put("type", "1");
        mDao.manager.post(1, url, mapData, this, false);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<HouseColletion> apps = new Gson().fromJson(response, new TypeToken<List<HouseColletion>>() {
        }.getType());
        if (mView != null)
            mView.initData(apps);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null) mView.showFail();
    }
}
