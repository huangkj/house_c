package com.easylife.house.customer.ui.houses;

import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.ui.mine.prize.PrizeDetailActivity;
import com.easylife.house.customer.util.LogOut;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static com.easylife.house.customer.util.LogOut.TAG;

/**
 * Created by zgm on 2017/3/21.
 */

public class HousesAndTypePresenter extends BasePresenterImpl<HousesAndTypeContract.View> implements HousesAndTypeContract.Presenter, RequestManagerImpl {
    /**
     * 已经预约看房列表
     *
     * @param userCode
     * @param token
     */
    @Override
    public void getLookHouseList(String userCode, String token) {
        mDao.getLookHouseList(1, userCode, token, this);
    }


    /**
     * 收藏列表接口
     *
     * @param userCode
     * @param token
     * @param type
     */
    @Override
    public void collectHouseList(String userCode, String token, String type) {
        mDao.collectHouseList(3, userCode, token, type, this);
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
        mDao.collectHouse(0, devId, devName, userCode, token, type, houseName, this);
    }

    @Override
    public void delCollectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.delCollectHouse(2, devId, devName, userCode, token, type, houseName, this);
    }

    @Override
    public void shareIntegration() {
        if (mDao.localDao.isLogin())
            mDao.shareIntegration(4, this);
    }

    /**
     * 取消收藏
     *
     * @param response    需要使用的数据的Json字符串
     * @param requestType
     */
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
                case 3:
                    List<CollectionListBean> collectionList = new Gson().fromJson(response, new TypeToken<List<CollectionListBean>>() {
                    }.getType());
                    mView.showCollectList(collectionList);
                    break;


                case 4:
                    if (mView != null) {
                        mView.showTipDialog(response);
                    }
                    break;

            }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }

}
