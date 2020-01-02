package com.easylife.house.customer.ui.houses.housepincontrol;

import com.easylife.house.customer.bean.HouseStatistics;
import com.easylife.house.customer.bean.ResultBuild;
import com.easylife.house.customer.bean.ResultHousePinControl;
import com.easylife.house.customer.bean.ResultStructure;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class HousePinControlPresenter extends BasePresenterImpl<HousePinControlContract.View> implements HousePinControlContract.Presenter, RequestManagerImpl {

    @Override
    public void getHouseData(String devId, String buildId, String structure) {
        mDao.houseList(1, devId, buildId, structure, this);
    }

    @Override
    public void getBuildList(String devId) {
        mDao.buildList(2, devId, this);
    }

    @Override
    public void getStructureList(String devId, String buildId) {
        mDao.structureList(3, devId, buildId, this);
    }

    @Override
    public void getHouseStatistics(String devId) {
        mDao.selectHouseStatistics(4, devId, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    ResultHousePinControl data = new Gson().fromJson(response, ResultHousePinControl.class);
                    mView.initListData(data);
                    break;
                case 2:
                    List<ResultBuild> builds = new Gson().fromJson(response, new TypeToken<List<ResultBuild>>() {
                    }.getType());
                    mView.initListResultBuild(builds);
                    break;
                case 3:
                    List<ResultStructure> structures = new Gson().fromJson(response, new TypeToken<List<ResultStructure>>() {
                    }.getType());
                    mView.initListResultStructure(structures);
                    break;
                case 4:
                    HouseStatistics statistics = new Gson().fromJson(response, HouseStatistics.class);
                    mView.initListHouseStatistics(statistics);
                    break;
            }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    mView.initListData(null);
                    break;
                default:
                    mView.loadFail(code);
                    break;
            }
    }
}
