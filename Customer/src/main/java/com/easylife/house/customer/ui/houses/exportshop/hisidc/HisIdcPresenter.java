package com.easylife.house.customer.ui.houses.exportshop.hisidc;

import com.easylife.house.customer.bean.ExportHisIdcBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 */

public class HisIdcPresenter extends BasePresenterImpl<HisIdcContract.View> implements HisIdcContract.Presenter, RequestManagerImpl {
    @Override
    public void requestHisIdc(String brokeCode) {
        mDao.getHisIdc(1, brokeCode, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<ExportHisIdcBean> exportHisIdcBeanList = new Gson().fromJson(response, new TypeToken<List<ExportHisIdcBean>>() {
        }.getType());
        if (mView != null) mView.showIdcData(exportHisIdcBeanList);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            if (code != null) {
                mView.showFail(code.msg);
            } else {
                mView.showFail("请求失败");
            }
    }
}
