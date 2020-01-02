package com.easylife.house.customer.ui.homefragment.homelookhouse.buyhouse;

import com.easylife.house.customer.bean.NetAppointmentBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/3/29.
 */

public class BuyHousePresenter extends BasePresenterImpl<BuyHouseContract.View> implements BuyHouseContract.Presenter, RequestManagerImpl {
    @Override
    public void requestLookHouse(String userCode, String token, String type) {
        mDao.getLookHouse(mDao.HOUSES_DETAIL_MAIN_TYPE, userCode, token, type, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<NetAppointmentBean> netList = new Gson().fromJson(response, new TypeToken<List<NetAppointmentBean>>() {
        }.getType());
        if (mView != null)
            mView.showNetData(netList);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            mView.showFail();
    }
}
