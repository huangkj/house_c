package com.easylife.house.customer.ui.homefragment.homeindexv3;

import com.easylife.house.customer.bean.BannerListBean;
import com.easylife.house.customer.bean.CityPriceBean;
import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.HeadLineBean;
import com.easylife.house.customer.bean.HomeBean;
import com.easylife.house.customer.bean.HomeNewsBean;
import com.easylife.house.customer.bean.HotDevBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

public class HomeIndexContract {
    interface View extends BaseView {
        void showSuccess(List<HomeBean> homeList);

        void showFail(String msg);

        void showCollectList(List<CollectionListBean> collectionList);

        void showCollect();

        void showDelCollectSucc();

        void jumpToCommitUserInfo(String params);

        void showBannerList(BannerListBean bannerListBean);

        void showHeadLine(HeadLineBean headLineBean);

        void showHomeNews(HomeNewsBean homeNewsBean);

        void setRefresh(boolean refresh);

        void showHotDevList(List<HotDevBean> list);

        void showCityPrice(CityPriceBean cityPriceBean);
    }

    interface Presenter extends BasePresenter<View> {

        void requestHotDev(String cityId);

        void requestBannerList(String cityId);

        /**
         * 获取滚动头条
         *
         * @param cityId
         */
        void requestHeadList(String cityId);

        /**
         * 获取首页消息资讯
         *
         * @param cityId
         */
        void requestHomeNews(String cityId);

        void requestIndexMsg();

        void getCacheData();

        /**
         * 收藏列表
         *
         * @param userCode
         * @param token
         * @param type
         */
        void collectHouseList(String userCode, String token, String type);

        /**
         * 收藏
         *
         * @param devId
         * @param userCode
         * @param token
         * @param type
         * @param houseName
         */
        void collectHouse(String devId, String devName, String userCode, String token, String type, String houseName);

        /**
         * 取消收藏
         *
         * @param devId
         * @param userCode
         * @param token
         * @param type
         * @param houseName
         */
        void delCollectHouse(String devId, String devName, String userCode, String token, String type, String houseName);

        /**
         * 扫一扫地址解析 参数
         *
         * @param urlParams
         */
        void resolveQrParams(String urlParams);

        /**
         * @param cityId
         */
        void getCityHousePrice(String cityId);
    }
}
