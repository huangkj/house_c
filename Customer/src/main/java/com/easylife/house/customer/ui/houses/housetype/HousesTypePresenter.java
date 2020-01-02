package com.easylife.house.customer.ui.houses.housetype;

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

public class HousesTypePresenter extends BasePresenterImpl<HousesTypeContract.View> implements HousesTypeContract.Presenter, RequestManagerImpl {

    @Override
    public void requestHousesTypeList(String devId) {
        if (mDao != null)
            mDao.getTypeList(mDao.HOUSES_DETAIL_MAIN_TYPE, devId, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {

        switch (requestType) {
            case 2://户型列表
                List<HousesTypeBean> beanList = new Gson().fromJson(response, new TypeToken<List<HousesTypeBean>>() {
                }.getType());
                if (mView != null)
                    mView.showSuccess(beanList);

                break;

            case 3:
                mView.showCollect();
                break;

            case 4:
                mView.showDelCollectSucc();
                break;
        }


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


    /**
     * 收藏接口
     *
     * @param devId
     * @param devName
     * @param userCode
     * @param token
     * @param type
     * @param houseName
     */
    @Override
    public void collectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.collectHouse(3, devId, devName, userCode, token, type, houseName, this);
    }

    @Override
    public void delCollectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.delCollectHouse(4, devId, devName, userCode, token, type, houseName, this);
    }
}
