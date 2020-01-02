package com.easylife.house.customer.ui.homefragment.homelookhouse.netbook;

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

public class NetAppointmentPresenter extends BasePresenterImpl<NetAppointmentContract.View> implements NetAppointmentContract.Presenter, RequestManagerImpl {
    @Override
    public void requestLookHouse(String userCode, String token, String type) {
        mDao.getLookHouse(0, userCode, token, type, this);
    }

    @Override
    public void delLookHouse(String userCode, String token, String devId) {
        mDao.delLookHouse(1, userCode, token, devId, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 0:
                    List<NetAppointmentBean> netList = new Gson().fromJson(response, new TypeToken<List<NetAppointmentBean>>() {
                    }.getType());
                    mView.showNetData(netList);
                    break;
                case 1:
                    mView.showDelSuc();
                    break;
            }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            mView.showFail();
    }
}
