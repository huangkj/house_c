package com.easylife.house.customer.ui.houses;

import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/21.
 */

public class HousesAndTypeContract {

    public interface View extends BaseView {

        void showAlreadyHouse(List<String> alredyList);

        void showCollect();

        void showDelCollectSucc();

        void showCollectList(List<CollectionListBean> collectionList);

        void showTipDialog(String tip);
    }

    public interface Presenter extends BasePresenter<View> {

        void getLookHouseList(String userCode, String token);

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
         * 分享后增加积分
         */
        void shareIntegration();
    }
}
