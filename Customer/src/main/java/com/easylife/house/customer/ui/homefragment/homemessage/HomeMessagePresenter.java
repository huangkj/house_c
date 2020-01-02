package com.easylife.house.customer.ui.homefragment.homemessage;

import com.easylife.house.customer.bean.MsgDevSub;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class HomeMessagePresenter extends BasePresenterImpl<HomeMessageContract.View> implements HomeMessageContract.Presenter, RequestManagerImpl {

    @Override
    public void getDataHeadLine() {
        mDao.headMessage(1, Constants.PAGE_START, this);
    }

    @Override
    public void getDataDevSub() {
        mDao.devMessage(2, Constants.PAGE_START, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView == null)
            return;
        switch (requestType) {
            case 1:
                List<MsgHeadLine> data = new Gson().fromJson(response, new TypeToken<List<MsgHeadLine>>() {
                }.getType());
                mView.initDataHeadLine(data);
                break;
            case 2:
                List<MsgDevSub> data1 = new Gson().fromJson(response, new TypeToken<List<MsgDevSub>>() {
                }.getType());
                mView.initDataDevSub(data1);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
    }
}
