package com.easylife.house.customer.ui.houses.exportshop.completehouse;

import com.easylife.house.customer.bean.ExportCompeleteHomeBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 */

public class CompleteHousePresenter extends BasePresenterImpl<CompleteHouseContract.View> implements CompleteHouseContract.Presenter, RequestManagerImpl {

    @Override
    public void requestCompleteHome(String brokeCode, String pageSize) {
        mDao.getSaleHouses(1, brokeCode, pageSize, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<ExportCompeleteHomeBean> saleHousesBeanList = new Gson().fromJson(response, new TypeToken<List<ExportCompeleteHomeBean>>() {
        }.getType());
        if (mView != null) mView.showCompleteHome(saleHousesBeanList);
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
