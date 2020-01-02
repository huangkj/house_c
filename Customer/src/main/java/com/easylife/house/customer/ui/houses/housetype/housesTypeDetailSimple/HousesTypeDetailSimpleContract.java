package com.easylife.house.customer.ui.houses.housetype.housesTypeDetailSimple;

import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.HouseTypeDetailBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.ResultCustomerIsOrder;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/28.
 */

public class HousesTypeDetailSimpleContract {

    interface View extends BaseView {
        void showBaseData(HouseTypeDetailBean housesTypeBean);

        void showHousesComment(final CommentListBean commentBean);

        void showCollect();

        void checkVipFavorableStatus(ResultCustomerIsOrder resultCustomerIsOrder);

        void showCollectList(List<CollectionListBean> collectionList);

        void showHousesDetail(HousesDetailBaseBean baseBean);

        void showDelCollectSucc();

        void showFavorableDev(DevFavorable devFavorable);

        void showFavorableVip(List<FavorableVip> favorableVips);

        void showFial(String msg);
    }

    interface Presenter extends BasePresenter<View> {
//        void getHousesTypeDetail(String devId, String housesName);

        /**
         * 获取楼盘优惠
         *
         * @param devId
         */
        void getDevFavorable(String devId);

        /**
         * 获取会员优惠
         *
         * @param devId
         */
        void getVipFavorable(String devId);

        /**
         * 检查用户是否已经完成会员优惠的购买
         *
         * @param devId
         */
        void checkVipFavorableStatus(String devId);

        /**
         * 获取评价内容
         *
         * @param projectId
         * @param pageSize
         */
        void requestHousesComment(String projectId, String pageSize);

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
         * 获取楼盘详情数据
         *
         * @param devId
         */
        void requestHousesDetail(String devId);

        /**
         * 获取户型详情
         *
         * @param devId
         * @param houseName
         */
        void requestTypeDetail(String devId, String houseName);
    }
}
