package com.easylife.house.customer.ui.mine.calculator.rate;

import com.easylife.house.customer.bean.ResultRate;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class RatePresenter extends BasePresenterImpl<RateContract.View> implements RateContract.Presenter, RequestManagerImpl {

    @Override
    public void getNetRate(boolean isBusiness) {
        mDao.selectRate(1, isBusiness ? "1" : "0", "1", this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<ResultRate> list = new Gson().fromJson(response, new TypeToken<List<ResultRate>>() {
        }.getType());
        if (mView != null) mView.initNetData(list);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
