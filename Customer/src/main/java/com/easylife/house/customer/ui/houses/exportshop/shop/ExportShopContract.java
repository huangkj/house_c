package com.easylife.house.customer.ui.houses.exportshop.shop;

import com.easylife.house.customer.bean.ExportCompeleteHomeBean;
import com.easylife.house.customer.bean.ExportHisDymanicBean;
import com.easylife.house.customer.bean.ExportHisIdcBean;
import com.easylife.house.customer.bean.ExportSaleHousesBean;
import com.easylife.house.customer.bean.ExportShopBaseBean;
import com.easylife.house.customer.bean.ExportUserCommentBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/4/1.
 * 专家店铺
 */

public class ExportShopContract {

    interface View extends BaseView{
        void showShopBaseData(ExportShopBaseBean baseBean);

        void showUserCommentList(List<ExportUserCommentBean> commentBeanList);

        void showCompleteHome(List<ExportCompeleteHomeBean> saleHousesBeanList);

        void showHisIdc(List<ExportHisIdcBean> exportHisIdcBeanList);

        void showHisDynamic(List<ExportHisDymanicBean> exportHisDymanicBeanList);

        void showSaleHouses(List<ExportSaleHousesBean> exportSaleHousesBeanList);



        void showFail(String msg);
    }

    interface Presenter extends BasePresenter<View>{
        /**
         * 请求基础信息
         * @param brokeCode
         */
        void requestShopBase(String brokeCode);

        /**
         * 用户评价
         * @param brokeCode
         */
        void requestUserComment(String brokeCode);

        /**
         * 获取成交房源
         * @param brokeCode
         */
        void requestCompleteHome(String brokeCode);

        /**
         * 获取所售楼盘
         * @param brokeCode
         */
        void requestSaleHouses(String brokeCode, String sort);


        /**
         * 获取TA的证书
         * @param brokeCode
         */
        void requestHisIdc(String brokeCode);

        /**
         * 获取TA的动态
         * @param brokeCode
         */
        void requestHisDymanic(String brokeCode);
    }
}
