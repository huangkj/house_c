package com.easylife.house.customer.ui.houses.housetype;

import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/28.
 * 户型列表
 */

public class HousesTypeContract {

    public interface View extends BaseView {
        void showSuccess(List<HousesTypeBean> beanList);

        void showFail(String msg);

        void showCollect();

        void showDelCollectSucc();
    }

    public interface Presenter extends BasePresenter<View> {
        void requestHousesTypeList(String devId);

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
