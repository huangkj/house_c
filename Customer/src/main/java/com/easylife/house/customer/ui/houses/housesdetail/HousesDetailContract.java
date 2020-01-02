package com.easylife.house.customer.ui.houses.housesdetail;

import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.DisCountIsAlreadyBean;
import com.easylife.house.customer.bean.DiscountListBean;
import com.easylife.house.customer.bean.ExportListBean;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.GitDisCountBean;
import com.easylife.house.customer.bean.HouseInfoSubBean;
import com.easylife.house.customer.bean.HouseSubNumBean;
import com.easylife.house.customer.bean.HousesClubBean;
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

public class HousesDetailContract {

    public interface View extends BaseView {
        void exit();

        void showHousesTopImg(List<HousesTopImgBean> topImgBeanList);

        void showHousesDetail(HousesDetailBaseBean baseBean);

        void showFavorableDev(DevFavorable devFavorable);

        void checkVipFavorableStatus(ResultCustomerIsOrder resultCustomerIsOrder);

        void showFavorableVip(List<FavorableVip> favorableVips);

        void showHousesClub(GitDisCountBean gitDisCountBean);

        void showRequestGetDis(List<DisCountIsAlreadyBean> alreadyBeanList);

        void showHousesDynamic(List<HousesDynamicBean> dynamicList);

        void showHousesComment(CommentListBean commentBean);

        void showMainType(List<HousesTypeBean> beanList);

        void showHousesTeam(List<ExportListBean> expertTeamBeen);

        void showHousesLike(List<HousesLikeBean> likeBean);

        void showFail(String msg);

        void showMainFail();

        void showDymanicCount(String count);

        void showDisCountList(List<DiscountListBean> list);

        void updateDisCount(String msg, int pos);


        void showCollect();

        void showDelCollectSucc();

        void showCollectList(List<CollectionListBean> collectionList);

        void showAlreadyHouse(List<String> alredyList);

        void showTipDialog(String tip);


        void getVerifyCodeSucc();

        void showSubList(List<String> mySubList);

        void showHouseSubNum(HouseSubNumBean numBean);

        void showGutSubSucc(String tag, String msg);

        void showDelSubSucc(String tag, String msg);
    }

    public interface Presenter extends BasePresenter<View> {

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
         * 楼盘详情基础数据
         *
         * @param devId
         */
        void requestHousesDetail(String devId);

        /**
         * 是否获取会员专享
         */
        void requestIsGetDis(String devId, String userCode, String token);

        /**
         * 会员专享
         *
         * @param devId
         */
        void requestHousesClub(String devId);

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
         *
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
        void requestHousesTeam(String devId, String pageSize);

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
        void chatPush(String devId, String brokeCode);

        /**
         * 1.40改版 获取优惠信息列表
         *
         * @param devId
         */
        void getDiscountList(String devId);

        /**
         * 1.40改版 领取优惠信息
         *
         * @param devId
         */
        void getDiscountCount(String devId, String type, int pos);

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

        void getLookHouseList(String userCode, String token);

        /**
         * 分享后增加积分
         */
        void shareIntegration();


        void getSub(HouseInfoSubBean houseInfoSubBean);

        void getVerifyCode(String phone);

        /**
         * 获取订阅状态
         *
         * @param userCode
         * @param token
         * @param projectId
         * @param devId
         */
        void getSubList(String userCode, String token, String projectId, String devId);

        void getHousesSubNum(String devId, String projectId);

        /**
         * 取消订阅
         *
         * @param userCode
         * @param token
         * @param projectId
         * @param devId
         * @param tag
         */
        void delSubscribe(String tag, String userCode, String token, String projectId, String devId);
    }
}
