package com.easylife.house.customer.ui.houses.housedetailsimple;

import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.bean.HousesLikeBean;
import com.easylife.house.customer.bean.HousesTopImgBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static com.easylife.house.customer.dao.ServerDao.HOUSES_DETAIL_BASE;
import static com.easylife.house.customer.dao.ServerDao.HOUSES_DETAIL_HOUSES_LIKE;

/**
 * Created by zgm on 2017/3/21.
 * 楼盘详情
 */

public class HouseDetailSimplePresenter extends BasePresenterImpl<HousesDetailSimpleContract.View> implements
        HousesDetailSimpleContract.Presenter, RequestManagerImpl {


    @Override
    public void requestHousesImg(String devId) {
        mDao.getHousesTopImg(mDao.HOUSES_DETAIL_TOP_IMG, devId, this);
    }

//    @Override
//    public void getDevFavorable(String devId) {
//        mDao.selectEstateProjectDev(45, devId, this);
//    }

//    @Override
//    public void getVipFavorable(String devId) {
//        mDao.selectEstateProjectVip(46, devId, this);
//    }

//    @Override
//    public void checkVipFavorableStatus(String devId) {
//        mDao.checkVipFavorableStatus(47, devId, this);
//    }

    /**
     * 请求基础数据
     *
     * @param devId
     */
    @Override
    public void requestHousesDetail(String devId) {
        mDao.getHousesData(HOUSES_DETAIL_BASE, devId, this);
    }

//    @Override
//    public void requestIsGetDis(String devId, String userCode, String token) {
//        mDao.requestIsGetDis(10, devId, userCode, token, this);
//    }

    /**
     * 会员专享
     *
     * @param devId
     */
//    @Override
//    public void requestHousesClub(String devId) {
//        mDao.getHousesTopClub(mDao.HOUSES_DETAIL_HOUSES_CLUB, devId, this);
//    }

    /**
     * 主力户型
     *
     * @param devId
     */
    @Override
    public void requestMainType(String devId) {
        mDao.getMainList(mDao.HOUSES_DETAIL_MAIN_TYPE, devId, this);
    }

    /**
     * 获取楼盘最新动态
     *
     * @param projectId
     * @param pageSize
     */
    @Override
    public void requestHousesDynamic(String projectId, String pageSize) {
        mDao.getNewDynamic(mDao.HOUSES_DETAIL_HOUSES_DYNAMIC, projectId, pageSize, this);
    }

    @Override
    public void requestHousesDynamicCount(String projectId) {
        mDao.getNewDynamicCount(110, projectId, this);
    }

    /**
     * 获取楼盘评价列表
     *
     * @param projectId
     * @param pageSize
     */
    @Override
    public void requestHousesComment(String projectId, String pageSize) {
        mDao.getComment(mDao.HOUSES_DETAIL_HOUSES_COMMENT, projectId, pageSize, this);
    }

    /**
     * 获取专家团队
     *
     * @param devId
     * @param pageSize
     */
//    @Override
//    public void requestHousesTeam(String devId, String pageSize) {
//        mDao.getExpertTeam(mDao.HOUSES_DETAIL_HOUSES_EXPERT_TEAM, devId, pageSize, this);
//    }

    /**
     * 获取相似推荐
     */
    @Override
    public void requestHousesLike(String devId) {
        mDao.getHousesLike(HOUSES_DETAIL_HOUSES_LIKE, devId, this);
    }

    /**
     * 进入聊天请求推送
     */
//    @Override
//    public void chatPush(String devId, String brokeCode) {
//        mDao.getIMPush(47, devId, brokeCode, this);
//    }
    @Override
    public void onSuccess(String response, int requestType) {
        if (mView != null)
            try {
                switch (requestType) {
                    case ServerDao.HOUSES_DETAIL_TOP_IMG:
                        List<HousesTopImgBean> topImgBeanList = new Gson().fromJson(response, new TypeToken<List<HousesTopImgBean>>() {
                        }.getType());
                        for (HousesTopImgBean topBean : topImgBeanList) {
                            if (topBean.img != null && topBean.img.size() != 0) {
                                for (HousesTopImgBean.ImgBean imgbean : topBean.img) {
                                    imgbean.name = topBean.name;
                                }
                            }

                        }
                        if (topImgBeanList != null)
                            mView.showHousesTopImg(topImgBeanList);
                        break;
                    case HOUSES_DETAIL_BASE:
                        //楼盘详情基础数据
                        HousesDetailBaseBean baseBean = new Gson().fromJson(response, HousesDetailBaseBean.class);
                        if (baseBean != null)
                            mView.showHousesDetail(baseBean);
                        break;
//                case ServerDao.HOUSES_DETAIL_HOUSES_CLUB:
//                    GitDisCountBean gitDisCountBean = new Gson().fromJson(response, GitDisCountBean.class);
//                    if (gitDisCountBean != null)
//                        mView.showHousesClub(gitDisCountBean);
//                    break;
                    case ServerDao.HOUSES_DETAIL_MAIN_TYPE:
                        List<HousesTypeBean> beanList = new Gson().fromJson(response, new TypeToken<List<HousesTypeBean>>() {
                        }.getType());
                        mView.showMainType(beanList);
                        break;
                    case ServerDao.HOUSES_DETAIL_HOUSES_DYNAMIC:
                        List<HousesDynamicBean> dynamicList = new Gson().fromJson(response, new TypeToken<List<HousesDynamicBean>>() {
                        }.getType());
                        if (dynamicList != null)
                            mView.showHousesDynamic(dynamicList);
                        break;
                    case ServerDao.HOUSES_DETAIL_HOUSES_COMMENT:
                        CommentListBean commentListBean = new Gson().fromJson(response, CommentListBean.class);
                        if (commentListBean != null)
                            mView.showHousesComment(commentListBean);
                        break;
//                case ServerDao.HOUSES_DETAIL_HOUSES_EXPERT_TEAM:
//                    List<ExportListBean> expertTeamList = new Gson().fromJson(response, new TypeToken<List<ExportListBean>>() {
//                    }.getType());
//                    if (expertTeamList != null)
//                        mView.showHousesTeam(expertTeamList);
//                    break;
                    case HOUSES_DETAIL_HOUSES_LIKE:
                        List<HousesLikeBean> likeList = new Gson().fromJson(response, new TypeToken<List<HousesLikeBean>>() {
                        }.getType());
                        if (likeList != null)
                            mView.showHousesLike(likeList);
                        break;
//                case 10:
//                    List<DisCountIsAlreadyBean> alreadyBeanList = new Gson().fromJson(response, new TypeToken<List<DisCountIsAlreadyBean>>() {
//                    }.getType());
//                    mView.showRequestGetDis(alreadyBeanList);
//                    break;
//                case 45:
//                    // 楼盘优惠
//                    DevFavorable favorable = new Gson().fromJson(response, DevFavorable.class);
//                    mView.showFavorableDev(favorable);
//                    break;
//                case 46:
//                    // 会员优惠
//                    List<FavorableVip> favorableVips = new Gson().fromJson(response, new TypeToken<List<FavorableVip>>() {
//                    }.getType());
//                    mView.showFavorableVip(favorableVips);
//                    break;
//                case 47:
//                    //  判断认筹支付是否完成
//                    //   {"data":1}
//                    ResultCustomerIsOrder resultCustomerIsOrder = new Gson().fromJson(response, ResultCustomerIsOrder.class);
//                    mView.checkVipFavorableStatus(resultCustomerIsOrder);
//                    break;
                    case 110:
                        mView.showDymanicCount(response);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (mView != null) {
            if (requestType == HOUSES_DETAIL_BASE) {
                mView.exit();
                return;
            }
            if (requestType == mDao.HOUSES_DETAIL_MAIN_TYPE) {
                if (code != null && "9001".equals(code.code)) {
                    mView.showMainFail();
                }
            }
            if (requestType == HOUSES_DETAIL_HOUSES_LIKE) {
                mView.showHousesLike(null);
            }
            if (code != null) {
                mView.showFail(code.msg);
            } else {
                mView.showFail("请求失败");
            }
        }
    }
}
