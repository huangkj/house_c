package com.easylife.house.customer.ui.homefragment.homeindexv3;

import android.text.TextUtils;

import com.blankj.utilcode.util.EncodeUtils;
import com.easylife.house.customer.bean.BannerListBean;
import com.easylife.house.customer.bean.CityPriceBean;
import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.HeadLineBean;
import com.easylife.house.customer.bean.HomeBean;
import com.easylife.house.customer.bean.HomeNewsBean;
import com.easylife.house.customer.bean.HotDevBean;
import com.easylife.house.customer.dao.LocalDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.INetResultCode;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.easylife.house.customer.util.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class HomeIndexPresenter extends BasePresenterImpl<HomeIndexContract.View> implements HomeIndexContract.Presenter, RequestManagerImpl {

    @Override
    public void requestHotDev(String cityId) {
        mDao.getHotDevs(7, cityId, this);
    }

    @Override
    public void requestBannerList(String cityId) {
        mDao.bannerList(4, cityId, this);
    }

    @Override
    public void requestHeadList(String cityId) {
        mDao.headLine(5, cityId, this);
    }

    @Override
    public void requestHomeNews(String cityId) {
        mDao.messageInfo(6, cityId, this);
    }

    @Override
    public void requestIndexMsg() {
        mDao.getHomeDate(1, this);
    }

    @Override
    public void getCacheData() {
        String cache = mDao.localDao.getCacheStr(LocalDao.FOLDER_CACHE_CUSTOMER, "HomeBean");
        if (!TextUtils.isEmpty(cache)) {
            List<HomeBean> homeList = new Gson().fromJson(cache, new TypeToken<List<HomeBean>>() {
            }.getType());
            if (mView != null)
                mView.showSuccess(homeList);
        }

    }

    @Override
    public void collectHouseList(String userCode, String token, String type) {
        mDao.collectHouseList(3, userCode, token, type, this);
    }

    @Override
    public void collectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.collectHouse(0, devId, devName, userCode, token, type, houseName, this);
    }

    @Override
    public void delCollectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.delCollectHouse(2, devId, devName, userCode, token, type, houseName, this);
    }

    @Override
    public void resolveQrParams(String urlParams) {
        if (!urlParams.contains("?")) {
            if (mView != null) mView.showTip("二维码无效");
            return;
        }
        String substring = urlParams.substring(urlParams.indexOf("?") + 1, urlParams.length());
        if (!substring.contains("&")) {
            if (mView != null) mView.showTip("二维码无效");
            return;
        }
        if (mView != null)
            mView.jumpToCommitUserInfo(substring);
    }

    @Override
    public void getCityHousePrice(String cityId) {
        mDao.getCityHousePrice(8, cityId, this);
    }


    private boolean hasSaveCache;

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView == null)
            return;

        if (mView != null) {
            mView.setRefresh(false);
        }
        switch (requestType) {
            case 0:
                mView.showCollect();
                break;
            case 1:
                List<HomeBean> homeList = new Gson().fromJson(response, new TypeToken<List<HomeBean>>() {
                }.getType());
                if (!hasSaveCache && homeList != null && homeList.size() != 0) {
                    mDao.localDao.saveString(LocalDao.FOLDER_CACHE_CUSTOMER, "HomeBean", response);
                    hasSaveCache = true;
                }
                mView.showSuccess(homeList);
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
                BannerListBean bannerListBean = GsonUtils.fromJson(response, BannerListBean.class);
                if (mView != null) {
                    mView.showBannerList(bannerListBean);
                }
                break;
            case 5:
                HeadLineBean headLineBean = GsonUtils.fromJson(response, HeadLineBean.class);
                if (mView != null) {
                    mView.showHeadLine(headLineBean);
                }
                break;


            case 6:
                HomeNewsBean homeNewsBean = GsonUtils.fromJson(response, HomeNewsBean.class);
                if (mView != null) {
                    mView.showHomeNews(homeNewsBean);
                }
                break;

            case 7:
                List<HotDevBean> list = new Gson().fromJson(response, new TypeToken<List<HotDevBean>>() {
                }.getType());
                mView.showHotDevList(list);
                break;

            case 8:
                CityPriceBean cityPriceBean = GsonUtils.fromJson(response, CityPriceBean.class);
                mView.showCityPrice(cityPriceBean);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null) {
            mView.setRefresh(false);
        }
        if (code == null)
            return;
        //账户登录失效(暂时收藏列表登录状态错误  TODO  需后台配合解决接口问题)
        if (requestType != 3 && code != null && !code.isConnetError && !TextUtils.isEmpty(code.code) && code.code.equals(INetResultCode.CODE_NET_RESULT_LOGIN_INVALID)) {
            if (mDao != null) {
                mDao.loginOut();
            }
        }

    }

    public boolean isLogin() {
        return mDao.isLogin();
    }
}
