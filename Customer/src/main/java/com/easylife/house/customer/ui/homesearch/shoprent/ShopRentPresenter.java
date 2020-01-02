package com.easylife.house.customer.ui.homesearch.shoprent;

import com.easylife.house.customer.bean.AreaDoubleBean;
import com.easylife.house.customer.bean.ShopRentRequestBean;
import com.easylife.house.customer.bean.ShopRentResponseBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ShopRentPresenter extends BasePresenterImpl<ShopRentContract.View> implements ShopRentContract.Presenter, RequestManagerImpl {


    @Override
    public void selectTradingByCityId(String cityId) {
        mDao.selectTradingByCityId(0, cityId, this);
    }

    @Override
    public void requestShopRentList(ShopRentRequestBean bean) {
        mDao.shopRentList(1, bean, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 0://选择区域
                List<AreaDoubleBean> list = new Gson().fromJson(response, new TypeToken<List<AreaDoubleBean>>() {
                }.getType());
                if (mView != null) {
                    mView.tradingListSuccess(list);
                }
                break;
            case 1://租赁列表

                List<ShopRentResponseBean> shopList = new Gson().fromJson(response, new TypeToken<List<ShopRentResponseBean>>() {
                }.getType());
                if (mView != null) {
                    mView.shopRentListSuccess(shopList);
                }
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null && code != null)
            mView.showFail(code.msg);
    }
}
