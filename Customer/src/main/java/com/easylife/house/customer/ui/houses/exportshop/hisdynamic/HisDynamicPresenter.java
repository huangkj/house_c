package com.easylife.house.customer.ui.houses.exportshop.hisdynamic;

import com.easylife.house.customer.bean.ExportHisDymanicBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 * TA的动态
 */

public class HisDynamicPresenter extends BasePresenterImpl<HisDynamicContract.View> implements HisDynamicContract.Presenter, RequestManagerImpl {

    @Override
    public void requestHisDymanic(String brokeCode, String pageSize) {
        mDao.getHisDynamic(1, brokeCode, pageSize, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<ExportHisDymanicBean> exportHisDymanicBeanList = new Gson().fromJson(response, new TypeToken<List<ExportHisDymanicBean>>() {
        }.getType());
        if (mView != null) mView.showDynamic(exportHisDymanicBeanList);
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
