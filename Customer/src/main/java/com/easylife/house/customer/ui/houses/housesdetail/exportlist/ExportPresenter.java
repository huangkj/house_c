package com.easylife.house.customer.ui.houses.housesdetail.exportlist;

import com.easylife.house.customer.bean.ExportListBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/3/27.
 */

public class ExportPresenter extends BasePresenterImpl<ExportContract.View> implements ExportContract.Presenter, RequestManagerImpl {
    @Override
    public void getExportList(String devId, String pageSize) {
        mDao.getExpertTeam(mDao.HOUSES_DETAIL_HOUSES_EXPERT_TEAM, devId, pageSize, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<ExportListBean> exportList = new Gson().fromJson(response, new TypeToken<List<ExportListBean>>() {
        }.getType());
        if (mView != null) mView.showExportList(exportList);
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
