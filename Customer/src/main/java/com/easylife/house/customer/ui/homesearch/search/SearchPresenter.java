package com.easylife.house.customer.ui.homesearch.search;

import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/3/30.
 */

public class SearchPresenter extends BasePresenterImpl<SearchContract.View> implements SearchContract.Presenter, RequestManagerImpl {

    @Override
    public void requestSearchData(SearchRequestBean requestBean,String type,String isTransparent,String pageSize) {
        mDao.getSearchData(mDao.HOUSES_SEARCH_DATA, requestBean,type,isTransparent,pageSize, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case ServerDao.HOUSES_SEARCH_DATA:
                List<HousesDetailBaseBean> baseBeanList = new Gson().fromJson(response, new TypeToken<List<HousesDetailBaseBean>>() {
                }.getType());
                if (mView != null && baseBeanList != null)
                    mView.showSuccessData(baseBeanList);
                break;
        }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null && code != null)
            mView.showFail(code.msg);
    }
}
