package com.easylife.house.customer.ui.mine.sign;

import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;

public class SignPresenter extends BasePresenterImpl<SignContract.View> implements SignContract.Presenter, RequestManagerImpl {

    @Override
    public void onSuccess(String response, int requestType) {

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
