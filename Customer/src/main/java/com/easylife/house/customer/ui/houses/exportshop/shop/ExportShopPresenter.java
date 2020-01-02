package com.easylife.house.customer.ui.houses.exportshop.shop;

import com.easylife.house.customer.bean.ExportCompeleteHomeBean;
import com.easylife.house.customer.bean.ExportHisDymanicBean;
import com.easylife.house.customer.bean.ExportHisIdcBean;
import com.easylife.house.customer.bean.ExportSaleHousesBean;
import com.easylife.house.customer.bean.ExportShopBaseBean;
import com.easylife.house.customer.bean.ExportUserCommentBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/4/1.
 * 专家店铺
 */

public class ExportShopPresenter extends BasePresenterImpl<ExportShopContract.View> implements ExportShopContract.Presenter, RequestManagerImpl {

    /**
     * 请求专家店铺基础信息
     *
     * @param brokeCode
     */
    @Override
    public void requestShopBase(String brokeCode) {
        mDao.getExportBase(1, brokeCode, this);
    }

    /**
     * 获取用户评价
     *
     * @param brokeCode
     */
    @Override
    public void requestUserComment(String brokeCode) {
        mDao.getExportComment(2, brokeCode, "1", this);
    }

    /**
     * 获取成交房源
     *
     * @param brokeCode
     */
    @Override
    public void requestCompleteHome(String brokeCode) {
        mDao.getSaleHouses(3, brokeCode, "1", this);
    }

    /**
     * 获取所售楼盘
     *
     * @param brokeCode
     * @param sort
     */
    @Override
    public void requestSaleHouses(String brokeCode, String sort) {
        mDao.getCompleteHome(6, brokeCode, sort, "1", this);
    }

    /**
     * 获取TA的证书
     *
     * @param brokeCode
     */
    @Override
    public void requestHisIdc(String brokeCode) {
        mDao.getHisIdc(4, brokeCode, this);
    }

    /**
     * TA的动态
     *
     * @param brokeCode
     */
    @Override
    public void requestHisDymanic(String brokeCode) {
        mDao.getHisDynamic(5, brokeCode, "1", this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    ExportShopBaseBean baseBean = new Gson().fromJson(response, ExportShopBaseBean.class);
                    mView.showShopBaseData(baseBean);
                    break;
                case 2:
                    List<ExportUserCommentBean> commentBeanList = new Gson().fromJson(response, new TypeToken<List<ExportUserCommentBean>>() {
                    }.getType());
                    mView.showUserCommentList(commentBeanList);
                    break;
                case 3:
                    List<ExportCompeleteHomeBean> saleHousesBeanList = new Gson().fromJson(response, new TypeToken<List<ExportCompeleteHomeBean>>() {
                    }.getType());
                    mView.showCompleteHome(saleHousesBeanList);
                    break;
                case 4:
                    List<ExportHisIdcBean> exportHisIdcBeanList = new Gson().fromJson(response, new TypeToken<List<ExportHisIdcBean>>() {
                    }.getType());
                    mView.showHisIdc(exportHisIdcBeanList);
                    break;
                case 5:
                    List<ExportHisDymanicBean> exportHisDymanicBeanList = new Gson().fromJson(response, new TypeToken<List<ExportHisDymanicBean>>() {
                    }.getType());
                    mView.showHisDynamic(exportHisDymanicBeanList);
                    break;
                case 6:
                    List<ExportSaleHousesBean> exportSaleHousesBeanList = new Gson().fromJson(response, new TypeToken<List<ExportSaleHousesBean>>() {
                    }.getType());
                    mView.showSaleHouses(exportSaleHousesBeanList);
                    break;
            }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null)
            if (code != null) {
                switch (requestType) {
                    case 4:
                        mView.showFail(code.code);
                        break;
                    case 6:
                        mView.showFail(code.code);
                        break;
                    default:
                        break;
                }
            } else {
                mView.showFail("请求失败");
            }
    }
}
