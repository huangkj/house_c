package com.easylife.house.customer.ui.houses.map.maparound;

import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;


public class MapAroundContract {
    interface View extends BaseView {
        void showAlreadyHouse(List<String> alredyList);

        void showCollect();

        void showDelCollectSucc();
    }

    interface Presenter extends BasePresenter<View> {
        /**
         * 已預約的房源列表
         *
         * @param userCode
         * @param token
         */
        void getLookHouseList(String userCode, String token);

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
    }
}
