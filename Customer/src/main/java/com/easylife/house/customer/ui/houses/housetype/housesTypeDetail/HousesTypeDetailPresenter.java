package com.easylife.house.customer.ui.houses.housetype.housesTypeDetail;

import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.HouseTypeDetailBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.ResultCustomerIsOrder;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zgm on 2017/3/28.
 */

public class HousesTypeDetailPresenter extends BasePresenterImpl<HousesTypeDetailContract.View> implements HousesTypeDetailContract.Presenter, RequestManagerImpl {

    /**
     * 获取户型详情
     *
     * @param devId
     * @param housesName
     */
    @Override
    public void requestTypeDetail(String devId, String housesName) {
        mDao.getTypeDetail(6, devId, housesName, this);
    }

    @Override
    public void shareIntegration() {
        if (mDao.localDao.isLogin())
            mDao.shareIntegration(55, this);
    }

    @Override
    public void getDevFavorable(String devId) {
        mDao.selectEstateProjectDev(45, devId, this);
    }

    @Override
    public void getVipFavorable(String devId) {
        mDao.selectEstateProjectVip(46, devId, this);
    }

    @Override
    public void checkVipFavorableStatus(String devId) {
        mDao.checkVipFavorableStatus(47, devId, this);
    }

    /**
     * 主力户型
     *
     * @param devId
     */
    @Override
    public void requestMainType(String devId) {
        mDao.getMainList(66, devId, this);
    }

    /**
     * 首页 获取城市行情
     *
     * @param cityId
     */
    @Override
    public void getCityHousePrice(String cityId) {
        mDao.getCityHousePrice(77, cityId, this);
    }


    /**
     * 获取楼盘评价列表
     *
     * @param projectId
     * @param pageSize
     */
    @Override
    public void requestHousesComment(String projectId, String pageSize) {
        mDao.getComment(4, projectId, pageSize, this);
    }

    @Override
    public void collectHouseList(String userCode, String token, String type) {
        mDao.collectHouseList(3, userCode, token, type, this);
    }

    @Override
    public void collectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.collectHouse(1, devId, devName, userCode, token, type, houseName, this);
    }

    @Override
    public void delCollectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.delCollectHouse(2, devId, devName, userCode, token, type, houseName, this);
    }

    /**
     * 获取楼盘详情数据
     *
     * @param devId
     */
    @Override
    public void requestHousesDetail(String devId) {
        mDao.getHousesData(5, devId, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            switch (requestType) {
                case 1:
                    mView.showCollect();
                    break;
                case 2:
                    mView.showDelCollectSucc();
                    break;
                case 3:
                    List<CollectionListBean> collectionList = new Gson().fromJson(response, new TypeToken<List<CollectionListBean>>() {
                    }.getType());
                    mView.showCollectList(collectionList);
                    break;
                case 4:
                    CommentListBean commentListBean = new Gson().fromJson(response, CommentListBean.class);
                    if (commentListBean != null)
                        mView.showHousesComment(commentListBean);
                    break;
                case 5:
                    HousesDetailBaseBean baseBean = new Gson().fromJson(response, HousesDetailBaseBean.class);
                    if (baseBean != null)
                        mView.showHousesDetail(baseBean);
                    break;
                case 6:
                    HouseTypeDetailBean housesTypeBean = new Gson().fromJson(response, HouseTypeDetailBean.class);
                    if (housesTypeBean != null)
                        mView.showBaseData(housesTypeBean);
                    break;
                case 45:
                    // 楼盘优惠
                    DevFavorable favorable = new Gson().fromJson(response, DevFavorable.class);
                    mView.showFavorableDev(favorable);
                    break;
                case 46:
                    // 会员优惠
                    List<FavorableVip> favorableVips = new Gson().fromJson(response, new TypeToken<List<FavorableVip>>() {
                    }.getType());
                    mView.showFavorableVip(favorableVips);
                    break;
                case 47:
                    //  判断认筹支付是否完成
                    //   {"data":1}
                    ResultCustomerIsOrder resultCustomerIsOrder = new Gson().fromJson(response, ResultCustomerIsOrder.class);
                    mView.checkVipFavorableStatus(resultCustomerIsOrder);
                    break;

                case 55:
                    if (mView != null) {
                        mView.showTipDialog(response);
                    }
                    break;
                case 66:
                    List<HousesTypeBean> beanList = new Gson().fromJson(response, new TypeToken<List<HousesTypeBean>>() {
                    }.getType());
                    mView.showMainType(beanList);
                    break;
            }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
//        if (code != null && !TextUtils.isEmpty(code.msg)) {
//            mView.showFial(code.msg);
//        } else {
        if (mView != null) mView.showFial("请求失败");
//        }
    }
}
