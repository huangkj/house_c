package com.easylife.house.customer.ui.houses.housesdetail.baninfo;

import com.easylife.house.customer.bean.BuildingInfoBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/3/25.
 */

public class BuildingPresenter extends BasePresenterImpl<BuildingContract.View> implements BuildingContract.Presenter, RequestManagerImpl {
    @Override
    public void requestBanInfo(String devId) {
        mDao.getBanInfo(mDao.HOUSES_DETAIL_HOUSES_BAN_INFO, devId, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<BuildingInfoBean> data = new Gson().fromJson(response, new TypeToken<List<BuildingInfoBean>>() {
        }.getType());
        if (data != null)
            if (mView != null) mView.showBanInfo(data);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (code != null) {
            if (mView != null) mView.showFail(code.code);
        }
    }
}
