package com.easylife.house.customer.ui.homesearch.allhouse;

import com.easylife.house.customer.bean.CityAreaBean;
import com.easylife.house.customer.bean.HomeSearchRequestBean;
import com.easylife.house.customer.bean.HomeSearchResponseBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class AllHousePresenter extends BasePresenterImpl<AllHouseContract.View> implements AllHouseContract.Presenter, RequestManagerImpl {


    @Override
    public void requestHomeSearch(int requestType, int pageNum, HomeSearchRequestBean bean) {
        mDao.homeSearchV3(ServerDao.HOUSES_SEARCH_DATA, pageNum, bean, this);
    }

    @Override
    public void requestArea(int requestType, String type, String id) {
        mDao.selectArea(11, "2", id, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case ServerDao.HOUSES_SEARCH_DATA:
                List<HomeSearchResponseBean> baseBeanList = new Gson().fromJson(response, new TypeToken<List<HomeSearchResponseBean>>() {
                }.getType());
                if (mView != null && baseBeanList != null)
                    mView.showSuccessData(baseBeanList);
                break;
            case 11://请求区域
                List<CityAreaBean> areas = new Gson().fromJson(response, new TypeToken<List<CityAreaBean>>() {
                }.getType());
                if (mView != null && areas != null)
                    mView.areasSuccessData(areas);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null && code != null)
            mView.showFail(code.msg);
    }
}
