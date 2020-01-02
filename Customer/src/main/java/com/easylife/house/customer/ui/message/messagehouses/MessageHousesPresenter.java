package com.easylife.house.customer.ui.message.messagehouses;

import com.easylife.house.customer.bean.MsgDevSub;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MessageHousesPresenter extends BasePresenterImpl<MessageHousesContract.View> implements MessageHousesContract.Presenter, RequestManagerImpl {

    @Override
    public void getNetData(int page) {
        mDao.devMessage(1, page, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<MsgDevSub> list = new Gson().fromJson(response, new TypeToken<List<MsgDevSub>>() {
        }.getType());
        if (mView != null) mView.initData(list);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
    }
}