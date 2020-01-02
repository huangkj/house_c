package com.easylife.house.customer.ui.houses.housesdetail.newdynamic;

import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/3/25.
 */

public class NewDynamicPresenter extends BasePresenterImpl<NewDynamicContract.View> implements NewDynamicContract.Presenter, RequestManagerImpl {
    @Override
    public void loadMoreData(String projectId, String pageSize) {
        mDao.getNewDynamic(mDao.HOUSES_DETAIL_HOUSES_DYNAMIC, projectId, pageSize, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<HousesDynamicBean> dynamicList = new Gson().fromJson(response, new TypeToken<List<HousesDynamicBean>>() {
        }.getType());
        if (dynamicList != null)
            if (mView != null) mView.showMoreData(dynamicList);
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
