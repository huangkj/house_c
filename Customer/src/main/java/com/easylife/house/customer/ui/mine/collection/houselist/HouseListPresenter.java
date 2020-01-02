package com.easylife.house.customer.ui.mine.collection.houselist;

import android.util.ArrayMap;

import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class HouseListPresenter extends BasePresenterImpl<HouseListContract.View> implements HouseListContract.Presenter, RequestManagerImpl {

    @Override
    public void getNetData() {
        String url = "customer/selectMyCollection";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().userCode);
        mapData.put("token", mDao.getLoginCache() == null ? "" : mDao.getLoginCache().token);
        mapData.put("type", "0");
        mDao.manager.post(1, url, mapData, this, false);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<HousesDetailBaseBean> apps = new Gson().fromJson(response, new TypeToken<List<HousesDetailBaseBean>>() {
        }.getType());
        if (mView != null) mView.initData(apps);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null) mView.showFail();
    }
}
