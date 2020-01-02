package com.easylife.house.customer.ui.homefragment.homeindex;

import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.HomeBean;
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
    }

    interface Presenter extends BasePresenter<View> {
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
    }
}
