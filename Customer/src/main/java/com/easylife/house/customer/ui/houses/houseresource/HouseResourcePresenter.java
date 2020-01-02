package com.easylife.house.customer.ui.houses.houseresource;

import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.HouseResource;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.PagerAllImageBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class HouseResourcePresenter extends BasePresenterImpl<HouseResourceContract.View> implements HouseResourceContract.Presenter, RequestManagerImpl {

    @Override
    public void getNetData(String id) {
        mDao.getHouseInfo(1, id, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    HouseResource detail = new Gson().fromJson(response, HouseResource.class);
                    mView.initDetail(detail);
                    break;
                case 45:
                    // 楼盘优惠
                    DevFavorable favorable = new Gson().fromJson(response, DevFavorable.class);
                    mView.showFavorableDev(favorable);
                    break;
                case 46:
                    // 会员优惠
                    List<FavorableVip> favorableVips = new Gson().fromJson(response, new TypeToken<List<FavorableVip>>() {
                    }.getType());
                    mView.showFavorableVip(favorableVips);
                    break;

                case 47:
                    //楼盘详情基础数据
                    HousesDetailBaseBean baseBean = new Gson().fromJson(response, HousesDetailBaseBean.class);
                    if (baseBean != null)
                        mView.showHousesDetail(baseBean);
                    break;
            }
    }

    @Override
    public void getDevFavorable(String devId) {
        mDao.selectEstateProjectDev(45, devId, this);
    }

    @Override
    public void getVipFavorable(String devId) {
        mDao.selectEstateProjectVip(46, devId, this);
    }

    @Override
    public void requestHousesDetail(String devId) {
        mDao.getHousesData(47, devId, this);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    mView.initDetail(null);
                    break;
                case 45:
                    // 楼盘优惠
                    mView.showFavorableDev(null);
                    break;
                case 46:
                    // 会员优惠
                    mView.showFavorableVip(null);
                    break;

                case 47://楼盘详情（所属楼盘）

                    break;
            }
    }
}
