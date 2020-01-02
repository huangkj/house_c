package com.easylife.house.customer.ui.houses.exportshop.salehouses;

import com.easylife.house.customer.bean.ExportSaleHousesBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/4/6.
 */

public class SaleHousesPresenter extends BasePresenterImpl<SaleHousesContract.View> implements SaleHousesContract.Presenter, RequestManagerImpl {

    @Override
    public void requestSaleHouses(String brokeCode, String pageSize) {
        mDao.getCompleteHome(1, brokeCode, "0", pageSize, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<ExportSaleHousesBean> exportSaleHousesBeanList = new Gson().fromJson(response, new TypeToken<List<ExportSaleHousesBean>>() {
        }.getType());
        if (mView != null) mView.showHousesData(exportSaleHousesBeanList);
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
