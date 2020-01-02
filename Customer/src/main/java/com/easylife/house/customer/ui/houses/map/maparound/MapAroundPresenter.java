package com.easylife.house.customer.ui.houses.map.maparound;

import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class MapAroundPresenter extends BasePresenterImpl<MapAroundContract.View> implements MapAroundContract.Presenter, RequestManagerImpl {
    @Override
    public void collectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.collectHouse(0, devId, devName, userCode, token, type, houseName, this);
    }

    @Override
    public void delCollectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.delCollectHouse(2, devId, devName, userCode, token, type, houseName, this);
    }

    @Override
    public void getLookHouseList(String userCode, String token) {
        mDao.getLookHouseList(1, userCode, token, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
            case 0:
                mView.showCollect();
                break;
            case 1:
                List<String> alreadyList = new Gson().fromJson(response, new TypeToken<List<String>>() {
                }.getType());
                mView.showAlreadyHouse(alreadyList);
                break;
            case 2:
                mView.showDelCollectSucc();
                break;
        }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
