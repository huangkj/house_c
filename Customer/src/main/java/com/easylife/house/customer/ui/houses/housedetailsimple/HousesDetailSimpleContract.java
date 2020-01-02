package com.easylife.house.customer.ui.houses.housedetailsimple;

import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.DisCountIsAlreadyBean;
import com.easylife.house.customer.bean.ExportListBean;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.GitDisCountBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.bean.HousesLikeBean;
import com.easylife.house.customer.bean.HousesTopImgBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.ResultCustomerIsOrder;
import com.easylife.house.customer.mvp.BasePresenter;
import com.easylife.house.customer.mvp.BaseView;

import java.util.List;

/**
 * Created by zgm on 2017/3/21.
 */

public class HousesDetailSimpleContract {

    interface View extends BaseView {
        void exit();

        void showHousesTopImg(List<HousesTopImgBean> topImgBeanList);

        void showHousesDetail(HousesDetailBaseBean baseBean);

//        void showFavorableDev(DevFavorable devFavorable);

//        void checkVipFavorableStatus(ResultCustomerIsOrder resultCustomerIsOrder);

//        void showFavorableVip(List<FavorableVip> favorableVips);

//        void showHousesClub(GitDisCountBean gitDisCountBean);

//        void showRequestGetDis(List<DisCountIsAlreadyBean> alreadyBeanList);

        void showHousesDynamic(List<HousesDynamicBean> dynamicList);

        void showHousesComment(CommentListBean commentBean);

        void showMainType(List<HousesTypeBean> beanList);

//        void showHousesTeam(List<ExportListBean> expertTeamBeen);

        void showHousesLike(List<HousesLikeBean> likeBean);

        void showFail(String msg);
        void showMainFail();

        void showDymanicCount(String count);

    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 请求楼盘详情头部图片
         *
         * @param devId
         */
        void requestHousesImg(String devId);

        /**
         * 获取楼盘优惠
         *
         * @param devId
         */
//        void getDevFavorable(String devId);

        /**
         * 获取会员优惠
         *
         * @param devId
         */
//        void getVipFavorable(String devId);

        /**
         * 检查用户是否已经完成会员优惠的购买
         *
         * @param devId
         */
//        void checkVipFavorableStatus(String devId);

        /**
         * 楼盘详情基础数据
         *
         * @param devId
         */
        void requestHousesDetail(String devId);

        /**
         * 是否获取会员专享
         */
//        void requestIsGetDis(String devId, String userCode, String token);

        /**
         * 会员专享
         *
         * @param devId
         */
//        void requestHousesClub(String devId);

        /**
         * 楼盘详情主力户型
         *
         * @param devId
         */
        void requestMainType(String devId);

        /**
         * 获取楼盘最新动态
         *
         * @param projectId
         * @param pageSize
         */
        void requestHousesDynamic(String projectId, String pageSize);

        /**
         * 获取动态数量
         * @param projectId
         */
        void requestHousesDynamicCount(String projectId);

        /**
         * 获取评价内容
         *
         * @param projectId
         * @param pageSize
         */
        void requestHousesComment(String projectId, String pageSize);

        /**
         * 获取专家团队
         *
         * @param devId
         * @param pageSize
         */
//        void requestHousesTeam(String devId, String pageSize);

        /**
         * 获取相似推荐
         *
         * @param devId
         */
        void requestHousesLike(String devId);

        /**
         * 聊天发送推送
         *
         * @param devId
         * @param brokeCode
         */
//        void chatPush(String devId, String brokeCode);


    }
}
