package com.easylife.house.customer.ui.houses.housetype.housetypefragmentsimple;

import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/3/28.
 * 户型列表
 */

public class HousesTypePresenterSimple extends BasePresenterImpl<HousesTypeContractSimple.View> implements HousesTypeContractSimple.Presenter, RequestManagerImpl {

    @Override
    public void requestHousesTypeList(String devId) {
        if (mDao != null)
            mDao.getTypeList(mDao.HOUSES_DETAIL_MAIN_TYPE, devId, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<HousesTypeBean> beanList = new Gson().fromJson(response, new TypeToken<List<HousesTypeBean>>() {
        }.getType());
        if (mView != null)
            mView.showSuccess(beanList);
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
