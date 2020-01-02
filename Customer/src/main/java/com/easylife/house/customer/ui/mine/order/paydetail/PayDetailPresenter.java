package com.easylife.house.customer.ui.mine.order.paydetail;

import com.easylife.house.customer.bean.PayDetailResult;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;

import static android.R.attr.data;

public class PayDetailPresenter extends BasePresenterImpl<PayDetailContract.View> implements PayDetailContract.Presenter, RequestManagerImpl {
    @Override
    public void getNetData(String type, String orderCode) {
        mDao.getPayDetail(1, orderCode, type, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView == null)
            return;
        PayDetailResult data = new Gson().fromJson(response, PayDetailResult.class);
        if (data == null) {
            mView.initData(null);
            return;
        }
        mView.initData(data.data);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null) mView.getDataFail();
    }
}
