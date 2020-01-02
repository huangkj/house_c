package com.easylife.house.customer.ui.houses.housesdetail;

import android.text.TextUtils;
import android.util.Log;

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
import com.easylife.house.customer.bean.HouseSubServiceBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.bean.HousesLikeBean;
import com.easylife.house.customer.bean.HousesTopImgBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.ResultCustomerIsOrder;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.BasePresenterImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.easylife.house.customer.dao.ServerDao.HOUSES_DETAIL_BASE;
import static com.easylife.house.customer.dao.ServerDao.HOUSES_DETAIL_HOUSES_LIKE;

/**
 * Created by zgm on 2017/3/21.
 * 楼盘详情
 */

public class HouseDetailPresenter extends BasePresenterImpl<HousesDetailContract.View> implements
        HousesDetailContract.Presenter, RequestManagerImpl {


    private int mDisCountPos;
    /**
     * 订阅接口 tag
     */
    private String getSubTag;
    /**
     * 取消订阅接口 tag
     */
    private String delSubTag;

    @Override
    public void requestHousesImg(String devId) {
        mDao.getHousesTopImg(mDao.HOUSES_DETAIL_TOP_IMG, devId, this);
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
    public void getDiscountList(String devId) {
        mDao.discountList(48, devId, this);
    }

    @Override
    public void getDiscountCount(String devId, String type, int pos) {
        mDisCountPos = pos;
        mDao.discountCount(49, devId, type, this);
    }

    @Override
    public void checkVipFavorableStatus(String devId) {
        mDao.checkVipFavorableStatus(47, devId, this);
    }

    /**
     * 请求基础数据
     *
     * @param devId
     */
    @Override
    public void requestHousesDetail(String devId) {
        mDao.getHousesData(HOUSES_DETAIL_BASE, devId, this);
    }

    @Override
    public void requestIsGetDis(String devId, String userCode, String token) {
        mDao.requestIsGetDis(10, devId, userCode, token, this);
    }

    /**
     * 会员专享
     *
     * @param devId
     */
    @Override
    public void requestHousesClub(String devId) {
        mDao.getHousesTopClub(mDao.HOUSES_DETAIL_HOUSES_CLUB, devId, this);
    }

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
    @Override
    public void requestHousesTeam(String devId, String pageSize) {
        mDao.getExpertTeam(mDao.HOUSES_DETAIL_HOUSES_EXPERT_TEAM, devId, pageSize, this);
    }

    /**
     * 获取相似推荐
     *
     * @param devId
     */
    @Override
    public void requestHousesLike(String devId) {
        mDao.getHousesLike(HOUSES_DETAIL_HOUSES_LIKE, devId, this);
    }

    /**
     * 进入聊天请求推送
     *
     * @param devId
     * @param brokeCode
     */
    @Override
    public void chatPush(String devId, String brokeCode) {
        mDao.getIMPush(0, devId, brokeCode, null);
    }


    /**
     * 收藏列表接口
     *
     * @param userCode
     * @param token
     * @param type
     */
    @Override
    public void collectHouseList(String userCode, String token, String type) {
        mDao.collectHouseList(333, userCode, token, type, this);
    }


    /**
     * 收藏接口
     *
     * @param devId
     * @param devName
     * @param userCode
     * @param token
     * @param type
     * @param houseName
     */
    @Override
    public void collectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.collectHouse(111, devId, devName, userCode, token, type, houseName, this);
    }

    @Override
    public void delCollectHouse(String devId, String devName, String userCode, String token, String type, String houseName) {
        mDao.delCollectHouse(222, devId, devName, userCode, token, type, houseName, this);
    }

    /**
     * 已经预约看房列表
     *
     * @param userCode
     * @param token
     */
    @Override
    public void getLookHouseList(String userCode, String token) {
        mDao.getLookHouseList(1, userCode, token, this);
    }

    @Override
    public void shareIntegration() {
        if (mDao.localDao.isLogin())
            mDao.shareIntegration(444, this);
    }

    @Override
    public void getSub(HouseInfoSubBean houseInfoSubBean) {
        getSubTag = houseInfoSubBean.tag;
        mDao.devHouseSub(666, houseInfoSubBean, this);
    }

    @Override
    public void getVerifyCode(String phone) {
        mDao.getVerifyCode(777, ServerDao.TYPE_VERIFYCODE_HOUSE, phone, this);
    }

    @Override
    public void getSubList(String userCode, String token, String projectId, String devId) {
        mDao.getHouseSubList(888, userCode, token, projectId, devId, this);
    }

    @Override
    public void getHousesSubNum(String devId, String projectId) {
        mDao.getHouseSubNum(555, devId, projectId, this);
    }

    @Override
    public void delSubscribe(String tag, String userCode, String token, String projectId, String devId) {
        delSubTag = tag;
        mDao.delSubscribe(999, userCode, token, projectId, devId, tag, this);

    }

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
                    case ServerDao.HOUSES_DETAIL_HOUSES_CLUB:
                        GitDisCountBean gitDisCountBean = new Gson().fromJson(response, GitDisCountBean.class);
                        if (gitDisCountBean != null)
                            mView.showHousesClub(gitDisCountBean);
                        break;
                    case ServerDao.HOUSES_DETAIL_MAIN_TYPE:
                        List<HousesTypeBean> beanList = new Gson().fromJson(response, new TypeToken<List<HousesTypeBean>>() {
                        }.getType());
                        mView.showMainType(beanList);
                        break;
                    case ServerDao.HOUSES_DETAIL_HOUSES_DYNAMIC:
                        List<HousesDynamicBean> dynamicList = new Gson().fromJson(response, new TypeToken<List<HousesDynamicBean>>() {
                        }.getType());
//                        if (dynamicList != null)
                            mView.showHousesDynamic(dynamicList);
                        break;
                    case ServerDao.HOUSES_DETAIL_HOUSES_COMMENT:
                        CommentListBean commentListBean = new Gson().fromJson(response, CommentListBean.class);
//                        if (commentListBean != null)
                            mView.showHousesComment(commentListBean);
                        break;
                    case ServerDao.HOUSES_DETAIL_HOUSES_EXPERT_TEAM:
                        List<ExportListBean> expertTeamList = new Gson().fromJson(response, new TypeToken<List<ExportListBean>>() {
                        }.getType());
                        if (expertTeamList != null)
                            mView.showHousesTeam(expertTeamList);
                        break;
                    case HOUSES_DETAIL_HOUSES_LIKE:
                        List<HousesLikeBean> likeList = new Gson().fromJson(response, new TypeToken<List<HousesLikeBean>>() {
                        }.getType());
                        if (likeList != null)
                            mView.showHousesLike(likeList);
                        break;
                    case 10:
                        List<DisCountIsAlreadyBean> alreadyBeanList = new Gson().fromJson(response, new TypeToken<List<DisCountIsAlreadyBean>>() {
                        }.getType());
                        mView.showRequestGetDis(alreadyBeanList);
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

                    case 48:
                        List<DiscountListBean> discountListBeans = new Gson().fromJson(response, new TypeToken<List<DiscountListBean>>() {
                        }.getType());
                        if (mView != null) {
                            mView.showDisCountList(discountListBeans);
                        }
                        break;
                    case 49:
                        if (mView != null) {
                            mView.updateDisCount(response, mDisCountPos);
                        }
                        break;
                    case 110:
                        mView.showDymanicCount(response);
                        break;

                    case 222:
                        mView.showDelCollectSucc();
                        break;
                    case 333:
                        List<CollectionListBean> collectionList = new Gson().fromJson(response, new TypeToken<List<CollectionListBean>>() {
                        }.getType());
                        mView.showCollectList(collectionList);
                        break;
                    case 111:
                        mView.showCollect();
                        break;
                    case 444:
                        if (mView != null) {
                            mView.showTipDialog(response);
                        }
                        break;

                    case 555://获取订阅人数
                        if (mView != null) {
                            HouseSubNumBean numBean = new Gson().fromJson(response, HouseSubNumBean.class);
                            mView.showHouseSubNum(numBean);
                        }
                        break;

                    case 666:
                        mView.showGutSubSucc(getSubTag, "订阅成功");
                        break;

                    case 777:
                        mView.getVerifyCodeSucc();
                        break;

                    case 888:
                        List<String> mySubList = new ArrayList<>();
                        HouseSubServiceBean houseSubServiceBean = new Gson().fromJson(response, HouseSubServiceBean.class);
                        if (houseSubServiceBean != null && !TextUtils.isEmpty(houseSubServiceBean.tag)) {
                            String[] split = houseSubServiceBean.tag.split(",");
                            for (String tag : split) {
                                mySubList.add(tag);
                            }
                        }
                        mView.showSubList(mySubList);
                        break;

                    case 999:
                        mView.showDelSubSucc(delSubTag, "取消订阅成功");
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        Log.d("@@", "onFail" + code);
        if (mView == null)
            return;
        if (requestType == mDao.HOUSES_DETAIL_MAIN_TYPE) {
            if (code != null && "9001".equals(code.code)) {
                mView.showMainFail();
            }
        }
        if (requestType == HOUSES_DETAIL_BASE) {
            mView.exit();
            return;
        }
        if (requestType == HOUSES_DETAIL_HOUSES_LIKE) {
            mView.showHousesLike(null);
            return;
        }
        if (requestType == 46) {//不弹吐司
            return;
        }


        if (code != null) {
            mView.showFail(code.msg);
        } else {
            mView.showFail("请求失败");
        }
    }
}
