package com.easylife.house.customer.dao;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.easylife.house.customer.App;
import com.easylife.house.customer.bean.ApplyBrokerRequest;
import com.easylife.house.customer.bean.BankCardOcrRequestBean;
import com.easylife.house.customer.bean.CommitSignInfoBean;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.ExportCommentBean;
import com.easylife.house.customer.bean.HomeSearchRequestBean;
import com.easylife.house.customer.bean.HouseInfoSubBean;
import com.easylife.house.customer.bean.IdCardOcrRequestBean;
import com.easylife.house.customer.bean.ImageBean;
import com.easylife.house.customer.bean.ImageResult;
import com.easylife.house.customer.bean.LocateCache;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.bean.OrderParameter;
import com.easylife.house.customer.bean.ParamRefundInfo;
import com.easylife.house.customer.bean.SearchRequestBean;
import com.easylife.house.customer.bean.SelectBrokerOrderRequest;
import com.easylife.house.customer.bean.ShopRentRequestBean;
import com.easylife.house.customer.bean.UserCommentBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.factory.OkhttpManager;
import com.easylife.house.customer.http.factory.RequestFactory;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.salingpoint.Collection;
import com.easylife.house.customer.salingpoint.CollectionAction;
import com.easylife.house.customer.salingpoint.CollectionObject;
import com.easylife.house.customer.salingpoint.CollectionUser;
import com.easylife.house.customer.util.Base64Util;
import com.easylife.house.customer.util.FileToBytesUtil;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.SettingsUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.easylife.house.customer.config.Constants.BAKN_CARD_OCR_URL;
import static com.easylife.house.customer.config.Constants.BANK_CARD_OCR_APP_CODE;
import static com.easylife.house.customer.config.Constants.ID_CARD_OCR_APP_CODE;
import static com.easylife.house.customer.config.Constants.ID_CARD_OCR_URL;
import static com.easylife.house.customer.config.Constants.PAGE_SIZE;

/**
 * 网络数据获取
 */
public class ServerDao {
    public static final String TYPE_VERIFYCODE_REGISTER = "1";
    public static final String TYPE_VERIFYCODE_UPDATE_PASSWORD = "2";
    public static final String TYPE_VERIFYCODE_LOGIN = "3";
    public static final String TYPE_VERIFYCODE_BIND_PHONE = "10";
    public static final String TYPE_VERIFYCODE_VIP = "4";  // 会员专享
    public static final String TYPE_VERIFYCODE_HOUSE = "5"; // 楼盘订阅
    public static final String TYPE_VERIFYCODE_CUSTOMER_ADD = "6"; // 二维码客户录入discount/devDiscount/count
    public static final String TYPE_VERIFYCODE_LOOK_HOUSE = "7"; // 预约看房获取验证码
    public static final String TYPE_VERIFYCODE_BIND_USER = "8"; // 绑定手机号
    public static final String TYPE_VERIFYCODE_APPLY_REFUND = "9"; // 退款申请

    public static final String TYPE_LOGIN_VARIFYCODE = "0";
    public static final String TYPE_LOGIN_NORMAL = "1";
    public static final int KEY_CITY = 8;

    public static final int KEY_REGIST_AREA = 1;
    public static final String TYPE_SELECT_CITY = "1";
    public static final String TYPE_SELECT_AREA = "2";
    public static final int HOUSES_DETAIL_TOP_IMG = 0;
    public static final int HOUSES_DETAIL_BASE = 1;
    public static final int HOUSES_DETAIL_MAIN_TYPE = 2;
    public static final int HOUSES_DETAIL_HOUSES_DYNAMIC = 3;
    public static final int HOUSES_DETAIL_HOUSES_COMMENT = 4;
    public static final int HOUSES_DETAIL_HOUSES_EXPERT_TEAM = 5;
    public static final int HOUSES_DETAIL_HOUSES_LIKE = 6;
    public static final int HOUSES_DETAIL_HOUSES_CLUB = 7;
    public static final int HOUSES_DETAIL_HOUSES_BAN_INFO = 8;
    public static final int HOUSES_SEARCH_DATA = 9;
    public static final int ADD_BANK_CARD_INPUT_TRADE_PASSWORD = 111;//添加银行卡跳输入交易密码
    public static final int UNBIND_BANK_CARD_INPUT_TRADE_PASSWORD = 222;//解绑银行卡跳输入交易密码
    public static final int UPDETE_INPUT_TRADE_PASSWORD = 333;//修改交易密码跳输入交易密码
    public static final int BROKER_INPUT_TRADE_PASSWORD = 343;//申请结佣输入交易密码
    public static boolean RECOMMEND_BIND_PHONG_FLAG = false;//绑定手机号时，如果是推荐有礼进入的


    private Context context;
    public LocalDao localDao;
    public OkhttpManager manager;

    public ServerDao(Context context) {
        this.context = context;
        localDao = new LocalDao(context);
        manager = RequestFactory.getOkManager();
    }

    public void saveCustomer(Customer Customer) {
        localDao.saveCustomer(Customer);
    }

    public Customer getCustomer() {
        return localDao.getCustomer();
    }

    public String getCustomerCode() {
        if (localDao.getCustomer() == null)
            return null;
        return localDao.getCustomer().userCode;
    }

    public boolean isLogin() {
        return localDao.getLoginCache() != null;
    }

    public boolean isFirst() {
        return localDao.isFirst();
    }

    public boolean showPushMessage() {
        return localDao.showPushMessage();
    }

    public void saveShowPushMessage(boolean show) {
        localDao.saveShowPushMessage(show);
    }

    public void loginOut() {
        unBindClientID(0, null);
        localDao.loginOut();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGIN_STATE_CHANGE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.HOUSES_INDEXT_COLLECTION));
    }

    public void saveLoginCache(String CustomerCode) {
        localDao.saveLoginCache(CustomerCode);
    }

    public LoginResult getLoginCache() {
        return localDao.getLoginCache();
    }

    public void saveClientID(String clientID) {
        localDao.saveClientID(clientID);
    }

    public String getClientID() {
        return App.CLIENT_ID;
    }

    public void saveLocateCache(String city, String cityID, double lat, double lon) {
        if (!TextUtils.isEmpty(cityID) && cityID.length() == 6) {
            cityID = cityID.substring(0, 4) + "00";
        }
        LocateCache cache = new LocateCache();
        cache.city = city;
        cache.cityId = cityID;
        cache.lat = lat;
        cache.lon = lon;
        LogOut.d("saveLocateCache:", cache.toString());
        localDao.saveLocateCache(new Gson().toJson(cache));
    }

    public LocateCache getLocateCache() {
        return localDao.getLocateCache();
    }

    /**
     * 获取验证码
     *
     * @param requestType
     * @param type           1 注册 2 更改密码 3 快捷登录 4 会员专享 5 订阅 10 绑定电话号码
     * @param phone
     * @param requestManager
     */
    public void getVerifyCode(int requestType, String type, String phone, RequestManagerImpl requestManager) {
        String url = "customer/message/sendVarifyCode";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("phone", phone);
        mapData.put("type", type);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 登录
     *
     * @param phone
     * @param varifyCode
     * @param type       0快捷登陆，1:密码登录
     * @param password
     */
    public void login(int requestType, String type, String phone, String varifyCode, String password, RequestManagerImpl requestManager) {
        String url = "customer/login";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("type", type);
        mapData.put("phone", phone);
        mapData.put("varifyCode", varifyCode);
        mapData.put("password", password);
        mapData.put("clientId", getClientID());
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 更新城市信息
     *
     * @param phone
     */
    public void updateCity(int requestType, String phone, String cityName, RequestManagerImpl requestManager) {
        String url = "customer/updateCity";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("phone", phone);
        mapData.put("cityName", cityName);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    public void bindClientID(int requestType,
                             RequestManagerImpl requestManager) {
        String url = "customer/putClientId";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("os", "0"); //0 -Android,1 -ios
        mapData.put("userCode", getCustomer() == null ? null : getCustomer().userCode);
        mapData.put("version", SettingsUtil.getVersionName(context));
        mapData.put("online", "1"); // "0"登录 1登出
        mapData.put("clientId", getClientID());
        manager.post(requestType, url, mapData, requestManager, false);
    }

    public void unBindClientID(int requestType,
                               RequestManagerImpl requestManager) {
        if (getCustomer() == null)
            return;
        String url = "customer/loginOut";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomer().userCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 第三方登录
     *
     * @param requestType
     * @param name
     * @param tid
     * @param headImg
     * @param type
     * @param requestManager
     */
    public void loginByOther(int requestType, String name, String tid, String headImg, String type, RequestManagerImpl requestManager) {
        String url = "customer/thirdLogin";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("name", name);
        mapData.put("tid", tid);
        mapData.put("headImg", headImg);
        mapData.put("type", type);
        mapData.put("clientId", getClientID());
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取首页数据
     *
     * @param requestType
     * @param requestManager
     */
    public void getHomeDate(int requestType, RequestManagerImpl requestManager) {
        String url = "customer/homepage";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomerCode());
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 收藏列表
     *
     * @param requestType
     * @param userCode
     * @param token
     * @param type           户型传1  楼盘传0
     * @param requestManager
     */
    public void collectHouseList(int requestType, String userCode, String token, String type, RequestManagerImpl requestManager) {
        String url = "customer/selectMyCollectionAll";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        mapData.put("type", type);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 楼盘报名信息
     *
     * @param requestType
     * @param devId
     * @param userCode
     * @param requestManager
     */
    public void requestSignInfo(int requestType, String userCode, String devId, String token, String brokerUserCode, RequestManagerImpl requestManager) {
        String url = "customer/dev/selectQRCode";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        mapData.put("brokerUserCode", brokerUserCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 提交楼盘报名信息
     *
     * @param requestType
     * @param commitSignInfoBean
     * @param requestManager
     */
    public void commitSignInfo(int requestType, CommitSignInfoBean commitSignInfoBean, RequestManagerImpl requestManager) {
        String url = "customer/dev/insertQRCode";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", commitSignInfoBean.devId);
        mapData.put("userCode", commitSignInfoBean.userCode);
        mapData.put("token", commitSignInfoBean.token);
        mapData.put("brokerUserCode", commitSignInfoBean.brokerUserCode);
        mapData.put("phone", commitSignInfoBean.phone);
        mapData.put("name", commitSignInfoBean.name);
        mapData.put("varifyCode", commitSignInfoBean.varifyCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 是否获取优惠
     *
     * @param requestType
     * @param devId
     * @param userCode
     * @param requestManager
     */
    public void requestIsGetDis(int requestType, String devId, String userCode, String token, RequestManagerImpl requestManager) {
        String url = "customer/dev/selectUserRule";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 提交获取优惠
     *
     * @param requestType
     * @param devId
     * @param userCode
     * @param token
     * @param phone
     * @param varifyCode
     * @param name
     * @param ruleId
     * @param requestManager
     */
    public void commitGetDis(int requestType, String devId, String userCode, String token, String phone, String varifyCode, String name, String ruleId, RequestManagerImpl requestManager) {
        String url = "customer/dev/insertDevMemberRule";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        mapData.put("phone", phone);
        mapData.put("varifyCode", varifyCode);
        mapData.put("name", name);
        mapData.put("ruleId", ruleId);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 收藏楼盘
     *
     * @param requestType
     * @param devId
     * @param userCode
     * @param token
     * @param type
     * @param houseName
     * @param requestManager
     */
    public void collectHouse(int requestType, String devId, String devName, String userCode, String token, String type, String houseName, RequestManagerImpl requestManager) {

        if ("0".equals(type)) {
            pointCollectDevAdd(devId, devName);
        } else {
            pointCollectStructureAdd(devId, devName);
        }

        String url = "customer/dev/insertCollection";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        mapData.put("type", type);
        mapData.put("houseName", houseName);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 取消收藏
     *
     * @param requestType
     * @param devId
     * @param requestManager
     */
    public void delCollectHouse(int requestType, String devId, String devName, String userCode, String token, String type, String houseName, RequestManagerImpl requestManager) {

        if ("0".equals(type)) {
            pointCollectDevCancel(devId, devName);
        } else {
            pointCollectStructureCancel(devId, devName);
        }

        String url = "customer/dev/delCollection";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        mapData.put("type", type);
        mapData.put("houseName", houseName);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * app版本更新接口
     *
     * @param requestType
     * @param requestManager
     */
    public void updateAppVersion(int requestType, RequestManagerImpl requestManager) {
//        String url = "customer/sys/updateVersion";
        String url = "customer/getAppUpdata";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("appType", "1");//0为ios，1为android
        mapData.put("app", 1);//| 0经纪人端，1 购房者端，2管家 |

        manager.post(requestType, url, mapData, requestManager, false);
    }

    private String encodeImage(String image) {
        byte[] bytes = FileToBytesUtil.File2byte(image);
        return Base64Util.encode(bytes);
    }

    /**
     * 上传图片
     *
     * @param requestType
     * @param requestManager
     */
    public void updateImg(int requestType, List<ImageBean> list, RequestManagerImpl requestManager) {
        String url = "customer/imgupload/upload/pic";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomer().userCode);
        mapData.put("token", getLoginCache().token);
        mapData.put("data", list);
        manager.post(requestType, url, mapData, requestManager, true);
    }

    /**
     * 上传单张图片
     *
     * @param requestType
     * @param requestManager
     */
    public void updateImgSingle(int requestType, String imgName, String imgPath, RequestManagerImpl requestManager) {
        List<ImageBean> beanList = new ArrayList<>();
        ImageBean imageBean = new ImageBean();
        imageBean.name = imgName;
        imageBean.pic = encodeImage(imgPath);
        beanList.add(imageBean);
        updateImg(requestType, beanList, requestManager);
    }

    /**
     * {
     * "data": [
     * {
     * "msg": "成功上传",
     * "code": "1000",
     * "name": "/storage/emulated/0/easylife/img/.img/uploadimg_20170320181343423.jpg",
     * "url": "http://om4yv9x56.bkt.clouddn.com/FkMMYIDIJ-SPY8bqlKHlcZ7HAc5R"
     * }
     * ]
     * }
     */
    public ImageResult.DataBean analyzeUploadSingleImageResult(String response) {
        ImageResult results = new Gson().fromJson(response, ImageResult.class);
        if (results == null || results.data == null || results.data.size() == 0)
            return null;
        ImageResult.DataBean result = results.data.get(0);
        if ("1000".equals(result.code))
            return result;
        return null;
    }


    /**
     * 获取楼盘详情基本数据
     *
     * @param requestType
     * @param requestManager
     */
    public void getHousesData(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/dev/devInfo";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取楼盘详情主力户型
     *
     * @param requestType
     * @param requestManager
     */
    public void getMainTypeData(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/dev/devMainHouse";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * type为0查询省份，为1查询城市，为2查询区域; id：当type为1时传省份id(传0获取全部城市)，当type为2时传城市id
     *
     * @param requestType
     * @param requestManager
     */
    public void selectArea(int requestType, String type, String id, RequestManagerImpl requestManager) {
        String url = "customer/sys/selectArea";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("type", type);
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取在售城市列表
     *
     * @param requestType
     * @param requestManager
     */
    public void selectSaleCity(int requestType, RequestManagerImpl requestManager) {
        String url = "customer/sys/selectSaleCity";
        Map<String, Object> mapData = new ArrayMap<>();
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 房贷利率
     *
     * @param requestType
     * @param type           0:公积金利率，1：商业贷款利率
     * @param status         0：基础利率 ， 1：利率列表
     * @param requestManager
     */
    public void selectRate(int requestType, String type, String status, RequestManagerImpl requestManager) {
        String url = "customer/selectRate";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("type", type);
        mapData.put("status", status);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 获取楼盘详情最新动态
     *
     * @param requestType
     * @param requestManager
     */
    public void getNewDynamic(int requestType, String projectId, String pagesize, RequestManagerImpl requestManager) {
        String url = "customer/dev/estateDynamic?pageNum=" + pagesize;
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("projectId", projectId);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 获取楼盘详情最新动态数量
     *
     * @param requestType
     * @param requestManager
     */
    public void getNewDynamicCount(int requestType, String projectId, RequestManagerImpl requestManager) {
        String url = "customer/dev/estateDynamicCount";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("projectId", projectId);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 获取楼盘详情评价
     *
     * @param requestType
     * @param requestManager
     */
    public void getComment(int requestType, String projectId, String pagesize, RequestManagerImpl requestManager) {
        String url = "customer/dev/estateReview?pageNum=" + pagesize;
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("projectId", projectId);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 获取专家团队
     *
     * @param requestType
     * @param requestManager
     */
    public void getExpertTeam(int requestType, String devId, String pagesize, RequestManagerImpl requestManager) {
        String url = "customer/dev/proTeam?pageNum=" + pagesize;
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 获取楼盘相似推荐
     *
     * @param requestType
     * @param requestManager
     */
    public void getHousesLike(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/dev/recommendation";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取楼盘详情top图片
     *
     * @param requestType
     * @param requestManager
     */
    public void getHousesTopImg(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/dev/devImg";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取楼盘详情会员专享
     *
     * @param requestType
     * @param requestManager
     */
    public void getHousesTopClub(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/dev/selectDevMemberRule";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, true);
    }

    /**
     * 获取楼栋信息
     *
     * @param requestType
     * @param requestManager
     */
    public void getBanInfo(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/dev/buildingInfo";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 楼栋详情提交评价
     *
     * @param requestType
     * @param requestManager
     */
    public void commitComment(int requestType, UserCommentBean userCommentBean, RequestManagerImpl requestManager) {
        String url = "customer/dev/addReview";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userId", userCommentBean.userId);
        mapData.put("userName", userCommentBean.userName);
        mapData.put("userType", userCommentBean.userType);
        mapData.put("content", userCommentBean.content);
        mapData.put("headImg", userCommentBean.headImg);
        mapData.put("scorePrice", userCommentBean.scorePrice);
        mapData.put("scoreDistrict", userCommentBean.scoreDistrict);
        mapData.put("scoreEnvi", userCommentBean.scoreEnvi);
        mapData.put("scoreMating", userCommentBean.scoreMating);
        mapData.put("scoreTraffic", userCommentBean.scoreTraffic);
        mapData.put("projectId", userCommentBean.projectId);
        mapData.put("reviewType", userCommentBean.reviewType);
        mapData.put("reviewImg", userCommentBean.reviewimg);
        mapData.put("AVGscores", userCommentBean.AVGscores);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取主力户型
     *
     * @param requestType
     * @param requestManager
     */
    public void getMainList(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/dev/devMainHouse";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取户型列表
     *
     * @param requestType
     * @param requestManager
     */
    public void getTypeList(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/houseLayout/houseLayoutList";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取户型详情
     *
     * @param requestType
     * @param requestManager
     */
    public void getTypeDetail(int requestType, String devId, String houseName, RequestManagerImpl requestManager) {
        String url = "customer/houseLayout/houseLayoutInfo";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("houseName", houseName);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 已预约看房列表
     *
     * @param requestType
     * @param requestManager
     */
    public void getLookHouseList(int requestType, String userCode, String token, RequestManagerImpl requestManager) {
        String url = "customer/appointment/selectAppointmentAll";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 预约看房
     *
     * @param requestType
     * @param requestManager
     */
    public void bookHouse(int requestType, String devId, String userCode, String smsCode, String token, String realName, String phone, String lookTime, RequestManagerImpl requestManager) {
        String url = "customer/appointment/insertHouse";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        mapData.put("realName", realName);
        mapData.put("phone", phone);
        mapData.put("varifyCode", smsCode);
        mapData.put("lookTime", lookTime);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 已预约看房列表
     *
     * @param requestType
     * @param requestManager
     */
    public void delLookHouse(int requestType, String userCode, String token, String devId, RequestManagerImpl requestManager) {
        String url = "customer/appointment/delAppointmentHouse";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 看房
     *
     * @param requestType
     * @param requestManager
     */
    public void getLookHouse(int requestType, String userCode, String token, String type, RequestManagerImpl requestManager) {
        String url = "customer/appointment/lookAppointment";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("type", type);
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 搜索请求
     *
     * @param requestType
     * @param requestManager
     */
    public void getSearchData(int requestType, SearchRequestBean searchRequestBean, String type, String isTransparent, String pageSize, RequestManagerImpl requestManager) {
        String url = Constants.SEARCH_HOST + "customer/homeSearchV2?pageNum=" + pageSize;
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("type", type);
        mapData.put("version", Constants.VERSION_CODE);
        mapData.put("userCode", searchRequestBean.userCode);
        mapData.put("isTransparent", isTransparent);
        mapData.put("city", searchRequestBean.city);
        mapData.put("cityId", searchRequestBean.cityId);
        mapData.put("devName", searchRequestBean.devName);
        mapData.put("beforeTime", searchRequestBean.beforeTime);
        mapData.put("areaId", searchRequestBean.addressDistrict);
        mapData.put("subway", searchRequestBean.subway);
        mapData.put("price", searchRequestBean.priceMapList);
        mapData.put("avgPrice", searchRequestBean.bugetMapList);
        mapData.put("houseSize", searchRequestBean.areaMapList);
        mapData.put("propertyType", searchRequestBean.propertyType);
        mapData.put("sort", searchRequestBean.sort);
        mapData.put("devBedroomInfo", searchRequestBean.devRoomInfo);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 查询城市区域
     *
     * @param requestType
     * @param requestManager
     */
    public void getCityArea(int requestType, String type, String id, RequestManagerImpl requestManager) {
        String url = "customer/sys/selectArea";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("type", type);
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取城市地铁
     *
     * @param requestType
     * @param cityId
     * @param requestManager
     */
    public void selectSubway(int requestType, String cityId, RequestManagerImpl requestManager) {
        String url = Constants.HOST + "customer/sys/selectSubway";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityId", cityId);
        mapData.put("userCode", getCustomer() == null ? null : getCustomer().userCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取专家店铺基础信息
     *
     * @param requestType
     * @param requestManager
     */
    public void getExportBase(int requestType, String brokerCode, RequestManagerImpl requestManager) {
        String url = "customer/myshop/selectmybase";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("brokerCode", brokerCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取专家店铺用户评价列表 好评率
     *
     * @param requestType
     * @param requestManager
     */
    public void getExportCommentGood(int requestType, String brokerCode, RequestManagerImpl requestManager) {
        String url = "customer/broker/customer/reply/score";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("brokerCode", brokerCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取专家店铺用户评价
     *
     * @param requestType
     * @param requestManager
     */
    public void getExportComment(int requestType, String brokerCode, String pageSize, RequestManagerImpl requestManager) {
        String url = "customer/myshop/evaluate?pageNum=" + pageSize;
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("brokerCode", brokerCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取成交房源
     *
     * @param requestType
     * @param requestManager
     */
    public void getSaleHouses(int requestType, String brokerCode, String pageSize, RequestManagerImpl requestManager) {
        String url = "customer/myshop/selectSoldEstate?pageNum=" + pageSize;
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("brokerCode", brokerCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取所售楼盘
     *
     * @param requestType
     * @param requestManager
     */
    public void getCompleteHome(int requestType, String brokerCode, String sort, String pageSize, RequestManagerImpl requestManager) {
        String url = "customer/sale/myselldevlist?pageNum=" + pageSize;
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("brokerCode", brokerCode);
        mapData.put("sort", sort);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取TA的证书
     *
     * @param requestType
     * @param requestManager
     */
    public void getHisIdc(int requestType, String brokerCode, RequestManagerImpl requestManager) {
        String url = "customer/myshop/selectCertificate";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("brokerCode", brokerCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取TA的动态
     *
     * @param requestType
     * @param requestManager
     */
    public void getHisDynamic(int requestType, String brokerCode, String pageSize, RequestManagerImpl requestManager) {
        String url = "customer/myshop/mydynamic?pageNum=" + pageSize;
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("brokerCode", brokerCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取TA的动态
     *
     * @param requestType
     * @param requestManager
     */
    public void commitExportComment(int requestType, ExportCommentBean commentBean, RequestManagerImpl requestManager) {
        String url = "customer/myshop/insertEvaluate";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("brokerCode", commentBean.brokerCode);
        mapData.put("userCode", commentBean.userCode);
        mapData.put("score", commentBean.score);
        mapData.put("token", commentBean.token);
        mapData.put("customerComent", commentBean.customerComent);
        mapData.put("scoreService", commentBean.scoreService);
        mapData.put("scorePro", commentBean.scorePro);
        mapData.put("scoreQuality", commentBean.scoreQuality);
        mapData.put("scoreEfficiency", commentBean.scoreEfficiency);
        mapData.put("scoreSatisfied", commentBean.scoreSatisfied);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 楼盘订阅我的订阅列表
     *
     * @param requestType
     * @param requestManager
     */
    public void getHouseSubList(int requestType, String userCode, String token, String projectId, String devId, RequestManagerImpl requestManager) {
        String url = "customer/dev/selectSubscribeByDevId";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        mapData.put("projectId", projectId);
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 取消订阅
     *
     * @param requestType
     */
    public void delSubscribe(int requestType, String userCode, String token, String projectId, String devId, String tag, RequestManagerImpl requestManager) {
        String url = "customer/dev/delSubscribe";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", userCode);
        mapData.put("token", token);
        mapData.put("projectId", projectId);
        mapData.put("devId", devId);
        mapData.put("tag", tag);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 楼盘订阅数量
     *
     * @param requestType
     * @param requestManager
     */
    public void getHouseSubNum(int requestType, String devId, String projectId, RequestManagerImpl requestManager) {
        String url = "customer/dev/selectSubscribeCount";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("projectId", projectId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 楼盘订阅
     *
     * @param requestType
     * @param requestManager
     */
    public void devHouseSub(int requestType, HouseInfoSubBean houseInfoSubBean, RequestManagerImpl requestManager) {
        String url = "customer/dev/installSubscribe";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", houseInfoSubBean.userCode);
        mapData.put("projectId", houseInfoSubBean.projectId);
        mapData.put("token", houseInfoSubBean.token);
        mapData.put("devId", houseInfoSubBean.devId);
        mapData.put("phone", houseInfoSubBean.phone);
        mapData.put("varifyCode", houseInfoSubBean.varifyCode);
        mapData.put("name", houseInfoSubBean.name);
        mapData.put("tag", houseInfoSubBean.tag);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 头条消息列表
     *
     * @param requestType
     * @param page
     * @param requestManager
     */
    public void headMessage(int requestType, int page, RequestManagerImpl requestManager) {
        String url = "customer/message/headMessage?pageNum=" + page;
        Map<String, Object> mapData = new ArrayMap<>();
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 头条消息列表浏览次数
     *
     * @param requestType
     * @param requestManager
     */
    public void headMessageCount(int requestType, String id, RequestManagerImpl requestManager) {
        String url = "customer/message/headMessageAddCount";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 楼盘订阅列表
     *
     * @param requestType
     * @param requestManager
     */
    public void devMessage(int requestType, int page, RequestManagerImpl requestManager) {
        String url = "customer/message/devMessage?pageNum=" + page;
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomer() == null ? "" : getCustomer().userCode);
        mapData.put("token", getCustomer() == null ? "" : getCustomer().token);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 房源销控返回房源信息
     *
     * @param requestType
     * @param devId
     * @param buildId
     * @param structure
     * @param requestManager
     */
    public void houseList(int requestType, String devId, String buildId, String structure, RequestManagerImpl requestManager) {
        String url = "customer/estate/house/List";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomer() == null ? "" : getCustomer().userCode);
        mapData.put("token", getCustomer() == null ? "" : getCustomer().token);
        mapData.put("devId", devId);
        mapData.put("buildId", buildId);
        mapData.put("structure", structure);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 房源楼栋列表
     *
     * @param requestType
     * @param devId
     * @param requestManager
     */
    public void buildList(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/house/building/list";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomer() == null ? "" : getCustomer().userCode);
        mapData.put("token", getCustomer() == null ? "" : getCustomer().token);
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 房源户型列表
     *
     * @param requestType
     * @param devId
     * @param buildId
     * @param requestManager
     */
    public void structureList(int requestType, String devId, String buildId, RequestManagerImpl requestManager) {
        String url = "customer/house/list";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomer() == null ? "" : getCustomer().userCode);
        mapData.put("token", getCustomer() == null ? "" : getCustomer().token);
        mapData.put("devId", devId);
        mapData.put("buildId", buildId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 房源统计数据
     *
     * @param requestType
     * @param devId
     * @param requestManager
     */
    public void selectHouseStatistics(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/house/selectHouseStatistics";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomer() == null ? "" : getCustomer().userCode);
        mapData.put("token", getCustomer() == null ? "" : getCustomer().token);
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 房源详情
     *
     * @param requestType
     * @param id
     * @param requestManager
     */
    public void getHouseInfo(int requestType, String id, RequestManagerImpl requestManager) {
        String url = "customer/estate/house/info";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomer() == null ? "" : getCustomer().userCode);
        mapData.put("token", getCustomer() == null ? "" : getCustomer().token);
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    // ===================埋点接口=========================
    // ===================埋点接口=========================
    // ===================埋点接口=========================
    // ===================埋点接口=========================
    // ===================埋点接口=========================
    // ===================埋点接口=========================

    /**
     * 注册HeadLineBean.ListBean
     */
    public void pointRegister(String phone) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser("",
                TextUtils.isEmpty(phone) ? "" : phone,
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject("1", "APP",
                CollectionObject.SERVICE_TYPE.APP));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.REGISTER.code + "",
                CollectionAction.ACTION_TYPE.REGISTER.msg,
                CollectionAction.ACTION_TYPE.REGISTER, new Date()
                .getTime() + ""));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    /**
     * 楼盘详情页访问
     */
    public void pointVisit(String devId, String devName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.ESTATE));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.VIEW.code + "",
                CollectionAction.ACTION_TYPE.VIEW.msg,
                CollectionAction.ACTION_TYPE.VIEW, new Date()
                .getTime() + ""));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    /**
     * 添加楼盘收藏
     *
     * @param devId
     * @param devName
     */
    public void pointCollectDevAdd(String devId, String devName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.ESTATE));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.FAVORITE.code + "",
                CollectionAction.ACTION_TYPE.FAVORITE.msg,
                CollectionAction.ACTION_TYPE.FAVORITE, new Date().getTime() + ""));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    /**
     * 取消楼盘收藏
     *
     * @param devId
     * @param devName
     */
    public void pointCollectDevCancel(String devId, String devName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.ESTATE));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.CANCEL_FAVORITE.code + "",
                CollectionAction.ACTION_TYPE.CANCEL_FAVORITE.msg,
                CollectionAction.ACTION_TYPE.CANCEL_FAVORITE, new Date()
                .getTime() + ""));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    /**
     * 添加户型收藏
     *
     * @param devId
     * @param devName
     */
    public void pointCollectStructureAdd(String devId, String devName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.HOUSE_TYPE));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.FAVORITE.code + "",
                CollectionAction.ACTION_TYPE.FAVORITE.msg,
                CollectionAction.ACTION_TYPE.FAVORITE, new Date()
                .getTime() + ""));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    /**
     * 取消户型收藏
     *
     * @param devId
     * @param devName
     */
    public void pointCollectStructureCancel(String devId, String devName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.HOUSE_TYPE));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.CANCEL_FAVORITE.code + "",
                CollectionAction.ACTION_TYPE.CANCEL_FAVORITE.msg,
                CollectionAction.ACTION_TYPE.CANCEL_FAVORITE, new Date()
                .getTime() + ""));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }


    /**
     * 客户报名
     *
     * @param devId
     * @param devName
     */
    public void pointApply(String devId, String devName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.ESTATE));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.ENROLL.code + "",
                CollectionAction.ACTION_TYPE.ENROLL.msg,
                CollectionAction.ACTION_TYPE.ENROLL, new Date()
                .getTime() + ""));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    /**
     * 购买优惠
     *
     * @param devId
     * @param devName
     */
    public void pointBuyFavorable(String devId, String devName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.BENEFIT));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.BUY.code + "",
                CollectionAction.ACTION_TYPE.BUY.msg,
                CollectionAction.ACTION_TYPE.BUY, new Date()
                .getTime() + ""));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

//    /**
//     * 认购房源
//     *
//     * @param houseId
//     * @param houseName
//     */
//    public void pointSubscription(String houseId, String houseName) {
//        Collection collection = new Collection();
//        collection.setUser(new CollectionUser(
//                getCustomer() != null ? getCustomer().userCode : "",
//                getCustomer() != null ? getCustomer().username : "",
//                CollectionUser.USER_TYPE.C, "", ""));
//        collection.setService(new CollectionObject(
//                TextUtils.isEmpty(houseId) ? "" : houseId,
//                TextUtils.isEmpty(houseName) ? "" : houseName,
//                CollectionObject.SERVICE_TYPE.HOUSE));
//        collection.setAction(new CollectionAction(
//                CollectionAction.ACTION_TYPE.BUY.code + "",
//                CollectionAction.ACTION_TYPE.BUY.msg,
//                CollectionAction.ACTION_TYPE.BUY, new Date()
//                .getTime() + ""));
//        manager.postSellingPointRequest(new Gson().toJson(collection));
//    }

    /**
     * IM聊天记录收集
     *
     * @param devId
     * @param devName
     * @param brokerCode
     * @param brokerName
     */
    public void pointIM(String devId, String devName, String brokerCode, String brokerName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.ESTATE));
        CollectionAction action = new CollectionAction(
                CollectionAction.ACTION_TYPE.IM.code + "",
                CollectionAction.ACTION_TYPE.IM.msg,
                CollectionAction.ACTION_TYPE.IM, new Date()
                .getTime() + "");
        collection.setAction(action);
        collection.setRelatedUser(new CollectionUser(
                TextUtils.isEmpty(brokerCode) ? "" : brokerCode,
                TextUtils.isEmpty(brokerName) ? "" : brokerName,
                CollectionUser.USER_TYPE.B));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    /**
     * 打电话
     *
     * @param devId
     * @param devName
     * @param brokerName 经纪人名称或者楼盘电话
     */
    public void pointCall(String devId, String devName, String brokerName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.ESTATE));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.CALL.code + "",
                CollectionAction.ACTION_TYPE.CALL.msg,
                CollectionAction.ACTION_TYPE.CALL, new Date().getTime() + ""));
        collection.setRelatedUser(new CollectionUser(
                "", TextUtils.isEmpty(brokerName) ? "" : brokerName,
                CollectionUser.USER_TYPE.B));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    /**
     * 预约看房
     *
     * @param devId
     * @param devName
     * @param duration
     * @param brokerId
     * @param brokerName
     */
    public void pointSubLook(String devId, String devName,
                             String duration, String brokerId,
                             String brokerName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                TextUtils.isEmpty(devId) ? "" : devId,
                TextUtils.isEmpty(devName) ? "" : devName,
                CollectionObject.SERVICE_TYPE.ESTATE));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.LOOK.code + "",
                CollectionAction.ACTION_TYPE.LOOK.msg,
                CollectionAction.ACTION_TYPE.LOOK, duration));
        collection.setRelatedUser(new CollectionUser(
                TextUtils.isEmpty(brokerId) ? "" : brokerId,
                TextUtils.isEmpty(brokerName) ? "" : brokerName,
                CollectionUser.USER_TYPE.B));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    /**
     * 我要买房
     *
     * @param city      购房城市
     * @param budget    购房预算
     * @param ioan      考虑贷款
     * @param houseType 居室需求
     */
    public void pointWantBuyHouse(String city, String budget, String ioan, String houseType, String devName) {
        Collection collection = new Collection();
        collection.setUser(new CollectionUser(
                getCustomer() != null ? getCustomer().userCode : "",
                getCustomer() != null ? getCustomer().username : "",
                CollectionUser.USER_TYPE.C, "", ""));
        collection.setService(new CollectionObject(
                "",
                TextUtils.isEmpty(devName) ? "" : devName,
                TextUtils.isEmpty(city) ? "" : city,
                TextUtils.isEmpty(budget) ? "" : budget,
                TextUtils.isEmpty(ioan) ? "" : ioan,
                TextUtils.isEmpty(houseType) ? "" : houseType,
                CollectionObject.SERVICE_TYPE.ESTATE));
        collection.setAction(new CollectionAction(
                CollectionAction.ACTION_TYPE.IBUYHOUSE.code + "",
                CollectionAction.ACTION_TYPE.IBUYHOUSE.msg,
                CollectionAction.ACTION_TYPE.IBUYHOUSE, new Date()
                .getTime() + ""));
        manager.postSellingPointRequest(new Gson().toJson(collection));
    }

    // ===================埋点接口=========================
    // ===================埋点接口=========================
    // ===================埋点接口=========================
    // ===================埋点接口=========================
    // ===================埋点接口=========================
    // ===================埋点接口=========================


    /**
     * 订单列表
     *
     * @param requestType
     * @param orderType
     * @param page           从1开始
     * @param requestManager
     */
    public void getOrderList(int requestType, String orderType, int page, RequestManagerImpl requestManager) {
        String url = "customer/order/list";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("customerPhone", getCustomer().phone);
        mapData.put("orderType", orderType);
        mapData.put("pageNum", page + "");
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 订单详情
     *
     * @param requestType
     * @param orderCode
     * @param requestManager
     */
    public void getOrderDetail(int requestType, String orderCode, RequestManagerImpl requestManager) {
        String url = "customer/order/detail";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 取消订单
     *
     * @param requestType
     * @param orderCode
     * @param requestManager
     */
    public void cancelOrder(int requestType, String orderCode, RequestManagerImpl requestManager) {
        String url = "customer/order/cancel";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 申请退认筹时的状态检查
     *
     * @param requestType
     * @param orderCode
     * @param requestManager
     */
    public void orderRefundCheck(int requestType, String orderCode, RequestManagerImpl requestManager) {
        String url = "/customer/order/orderRefundCheck";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 支付明细
     */
    public void getPayDetail(int requestType, String orderCode, String followType, RequestManagerImpl requestManager) {
        String url = "customer/order/payDetail";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        mapData.put("followType", followType);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 认购签章
     *
     * @param requestType
     * @param orderCode
     * @param signatureUrl
     * @param requestManager
     */
    public void signature(int requestType, String orderCode, String signatureUrl, RequestManagerImpl requestManager) {
        String url = "customer/order/signature";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        mapData.put("signatureUrl", signatureUrl);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 验证未完成订单
     *
     * @param requestType
     * @param customerIdCardNum 身份证号
     * @param requestManager
     */
    public void unfinish(int requestType, String customerIdCardNum, RequestManagerImpl requestManager) {
        String url = "customer/order/unfinish";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("customerIdCardNum", customerIdCardNum);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 会员优惠信息-认筹下单页面使用
     *
     * @param requestType
     * @param devId
     * @param requestManager
     */
    public void selectEstateProjectVip(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/estate/devMemberRule/selectEstateDevMember";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 检查用户是否已经完成会员优惠的购买
     *
     * @param requestType
     * @param devId
     * @param requestManager
     */
    public void checkVipFavorableStatus(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/estate/devMemberRule/customerIsOrder";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("userCode", getCustomer() == null ? "" : getCustomer().userCode);
        mapData.put("token", getCustomer() == null ? "" : getCustomer().token);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取通联订单返回参数
     *
     * @param requestType
     * @param body           商品名称
     * @param total_fee      商品价格
     * @param out_trade_no   商品外部订单号
     * @param requestManager
     */
    public void payData(int requestType, String body, String total_fee, String out_trade_no, RequestManagerImpl requestManager) {
        String url = "customer/tlt/payData";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomerCode());
        mapData.put("body", body);
        mapData.put("total_fee", ((int) (Double.parseDouble(total_fee) * 100)) + "");
        mapData.put("out_trade_no", out_trade_no);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取楼盘优惠信息-认购下单页面使用
     *
     * @param requestType
     * @param devId
     * @param requestManager
     */
    public void selectEstateProjectDev(int requestType, String devId, RequestManagerImpl requestManager) {
        String url = "customer/estate/projectDev/selectEstateProjectDev";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 检查支付状态返回支付信息
     *
     * @param requestType
     * @param orderCode
     * @param requestManager
     */
    public void checkOrderState(int requestType, String orderCode, RequestManagerImpl requestManager) {
        String url = "customer/order/check";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 继续支付
     *
     * @param requestType
     * @param orderCode
     * @param payOnThisTime
     * @param requestManager
     */
    public void payContinue(int requestType, String orderCode, String payOnThisTime, RequestManagerImpl requestManager) {
        String url = "customer/order/place/continue";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        mapData.put("payOnThisTime", payOnThisTime);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 支付宝获取签名
     *
     * @param requestType
     * @param out_trade_no   订单的支付流水号
     * @param subject        订单标题
     * @param total_amount   订单的支付金额
     * @param requestManager
     */
    public void alipay(int requestType, String out_trade_no, String subject, String total_amount, String version, RequestManagerImpl requestManager) {
        String url = "customer/alipay/sign";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("out_trade_no", out_trade_no);
        mapData.put("subject", subject);
        mapData.put("total_amount", total_amount);
        mapData.put("version", version);

//        mapData.put("body", body);
//        mapData.put("seller_id", seller_id);
//        mapData.put("product_code", product_code);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 退款进度
     *
     * @param requestType
     * @param orderCode
     * @param requestManager
     */
    public void refundRate(int requestType, String orderCode, RequestManagerImpl requestManager) {
        String url = "customer/order/refundLog";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 申请退款
     *
     * @param requestType
     * @param orderCode
     * @param price
     * @param desc
     * @param phone
     * @param code
     */
    public void applyRefund(int requestType, String orderCode, String price, String desc, String phone, String code,
                            String userName, String bankAddress, String bankNum, String cardNum, String linkNumber, String[] imgs, RequestManagerImpl requestManager) {
        String url = "customer/order/refund";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        mapData.put("price", price);
        mapData.put("comments", desc);
        mapData.put("phone", phone);
        mapData.put("code", code);
        mapData.put("bankAddress", bankAddress);
        mapData.put("userName", userName);
        mapData.put("bankNum", bankNum);
        mapData.put("cardNum", cardNum);
        mapData.put("linkNumber", linkNumber);
        mapData.put("img", imgs);
        manager.post(requestType, url, mapData, requestManager, false);
    }

//    /**
//     * 退认筹-第一步
//     *
//     * @param requestType
//     * @param orderCode
//     * @param userName       客户名称
//     * @param cardNum        客户身份证号
//     * @param bankAddress    银行地址
//     * @param bankNum        银行卡号
//     * @param requestManager
//     */
//    public void applyRefundOne(int requestType, String orderCode, String userName, String cardNum, String bankAddress, String bankNum, RequestManagerImpl requestManager) {
//        String url = "customer/order/refundOne";
//        Map<String, Object> mapData = new ArrayMap<>();
//        mapData.put("orderCode", orderCode);
//        mapData.put("userName", userName);
//        mapData.put("cardNum", cardNum);
//        mapData.put("bankAddress", bankAddress);
//        mapData.put("bankNum", bankNum);
//        manager.post(requestType, url, mapData, requestManager, false);
//    }

    /**
     * 退认筹-第二部
     *
     * @param requestType
     * @param param          退款材料参数
     * @param requestManager
     */
    public void applyRefundTwo(int requestType, ParamRefundInfo param, RequestManagerImpl requestManager) {
        String url = "customer/order/refundTwo";
        if (param != null)
            manager.post(requestType, url, new Gson().toJson(param, ParamRefundInfo.class), requestManager, false);
    }


    /**
     * 订单中的优惠凭证
     *
     * @param requestType
     * @param orderCode
     * @param requestManager
     */
    public void orderVoucher(int requestType, String orderCode, RequestManagerImpl requestManager) {
        String url = "customer/order/voucher";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("orderCode", orderCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 用户绑定手机号
     *
     * @param requestType
     * @param name
     * @param phone
     * @param code
     * @param requestManager
     */
    public void bindPhone(int requestType, String name, String phone, String code, RequestManagerImpl requestManager) {
        String url = "customer/order/phone/bind";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("name", name);
        mapData.put("code", code);
        mapData.put("phone", phone);
        mapData.put("userCode", getCustomer() == null ? "" : getCustomer().userCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 认筹认购下单
     * WECHAT(0, "微信"), ALIPAY(1, "支付宝"), HE(2, "合支付");
     *
     * @param requestManager
     */
    public void makeOrder(int requestType,
                          OrderParameter parameter,
                          RequestManagerImpl requestManager) {
        String url = "customer/order/place";
        manager.post(requestType, url, new Gson().toJson(parameter, OrderParameter.class), requestManager, false);
    }

    /**
     * 获取单个用户个人信息
     *
     * @param requestType
     * @param idArrStr
     * @param requestManager
     */
    public void getIMSingleUserInfo(int requestType, String idArrStr, RequestManagerImpl requestManager) {
        String url = "customer/easemob/unit/info";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("im", idArrStr);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 和专家聊天 进行推送
     *
     * @param requestType
     * @param devId
     * @param brokerCode
     * @param requestManager
     */
    public void getIMPush(int requestType, String devId, String brokerCode, RequestManagerImpl requestManager) {
        String url = "customer/dev/proTeamClick";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("brokerCode", brokerCode);
        mapData.put("userCode", getCustomer() == null ? "" : getCustomer().userCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * @param requestType
     * @param pageNum
     * @param bean
     * @param requestManager 全部楼盘 v1.3.3版本
     */
    public void homeSearchV3(int requestType, int pageNum, HomeSearchRequestBean bean, RequestManagerImpl requestManager) {
        String url = "customer/homeSearchV3?pageNum=" + pageNum;
        String json = GsonUtils.toJson(bean);
        manager.post(requestType, url, json, requestManager, false);
    }

    /**
     * 商铺租赁
     *
     * @param requestType
     * @param bean
     * @param requestManager
     */
    public void shopRentList(int requestType, ShopRentRequestBean bean, RequestManagerImpl requestManager) {
        String url = "customer/tenement/shops/appList";
        String json = GsonUtils.toJson(bean);
        manager.post(requestType, url, json, requestManager, false);
    }

    /**
     * @param requestType
     * @param cityId
     * @param requestManager
     */
    public void selectTradingByCityId(int requestType, String cityId, RequestManagerImpl requestManager) {
        String url = "customer/tenement/trading/selectTradingByCityId";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityId", cityId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 首页banner
     *
     * @param requestType
     * @param cityId
     * @param requestManager
     */
    public void bannerList(int requestType, String cityId, RequestManagerImpl requestManager) {
        String url = "customer/easylifeBanner/queryList";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityId", cityId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 首页滚动头条
     *
     * @param requestType
     * @param cityId
     * @param requestManager
     */
    public void headLine(int requestType, String cityId, RequestManagerImpl requestManager) {
        String url = "customer/messageHead/getHeadlineScroll";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityId", cityId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 首页消息资讯
     *
     * @param requestType
     * @param cityId
     * @param requestManager
     */
    public void messageInfo(int requestType, String cityId, RequestManagerImpl requestManager) {
        String url = "/customer/messageHead/getMessageInformation";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityId", cityId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 头条资讯列表
     *
     * @param requestType
     * @param cityId
     */
    public void messageInfoList(int requestType, String cityId, String sortType, int pageNum, int pageSize, RequestManagerImpl requestManager) {
        String url = "customer/messageHead/getMessageInformationDetail";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityId", cityId);
        mapData.put("sortType", sortType);
        mapData.put("pageNum", pageNum);
        mapData.put("pageSize", pageSize);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 我的奖品列表
     *
     * @param requestType
     * @param requestManager
     */
    public void myPrize(int requestType, int pageNum, int pageSize, RequestManagerImpl requestManager) {
        String url = "customer/marketing/buyer/activities/myprize";
        Map<String, Object> mapData = new ArrayMap<>();
        String id = localDao.getCustomer().id;
        mapData.put("buyerId", id);
//        mapData.put("buyerId", 545);
        mapData.put("pageNum", pageNum);
        mapData.put("pageSize", pageSize);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 我的奖品详情
     */
    public void myPrizeDetail(int requestType, int id, RequestManagerImpl requestManager) {
        String url = "customer/marketing/buyer/activities/myPrizeDetail";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * @param requestType
     * @param id              用户id
     * @param deliveryAddress 收货地址
     * @param prizeCategory   奖品类型
     * @param requestManager
     */
    public void clickClaimPrize(int requestType, String deliveryAddress, int id, String consigneeName, String consigneePhone, int prizeCategory, RequestManagerImpl requestManager) {
        String url = "customer/marketing/buyer/activities/ClickClaimPrize";
        Map<String, Object> mapData = new ArrayMap<>();
//        mapData.put("id", localDao.getCustomer().id);
        mapData.put("id", id);
        mapData.put("deliveryAddress", deliveryAddress);
        mapData.put("prizeCategory", prizeCategory);
        mapData.put("consigneeName", consigneeName);
        mapData.put("consigneePhone", consigneePhone);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 查询地址列表
     *
     * @param requestType
     * @param requestManager
     */
    public void addressList(int requestType, RequestManagerImpl requestManager) {
        String url = "customer/buyer/address/list/" + localDao.getCustomer().id;
        manager.get(requestType, url, null, requestManager);
    }

    /**
     * 查询地址详情
     *
     * @param requestType
     * @param id             地址id
     * @param requestManager
     */
    public void addressDetail(int requestType, String id, RequestManagerImpl requestManager) {
        String url = "customer/buyer/address/get/" + id;
        manager.get(requestType, url, null, requestManager);
    }

    /**
     * 删除收货地址
     *
     * @param requestType
     * @param id
     * @param requestManager
     */
    public void deleteAddress(int requestType, String id, RequestManagerImpl requestManager) {
        String url = "customer/buyer/address/delete/" + id;
        manager.get(requestType, url, null, requestManager);
    }

    /**
     * 新增收货地址
     *
     * @param requestType
     * @param addrProvince
     * @param addrCity
     * @param addrCounty
     * @param addressFull
     * @param tagName
     * @param isDefault
     * @param phoneNum
     * @param requestManager
     */
    public void addAddress(int requestType, String userName, String addrProvince, String addrCity, String addrCounty,
                           String addrProvinceId, String addrCityId, String addrCountyId, String addressFull,
                           String tagName, String isDefault, String phoneNum, RequestManagerImpl requestManager) {
        String url = "customer/buyer/address/add";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userId", localDao.getCustomer().id);
        mapData.put("userName", userName);
        mapData.put("addrProvince", addrProvince);
        mapData.put("addrCity", addrCity);
        mapData.put("addrCounty", addrCounty);
        mapData.put("provincesId", addrProvinceId);
        mapData.put("cityId", addrCityId);
        mapData.put("areaId", addrCountyId);
        mapData.put("addressFull", addressFull);
        mapData.put("tagName", tagName);
        mapData.put("isDefault", isDefault);
        mapData.put("phoneNum", phoneNum);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 编辑收货地址
     *
     * @param requestType
     * @param id
     * @param userName
     * @param addrProvince
     * @param addrCity
     * @param addrCounty
     * @param addrProvinceId
     * @param addrCityId
     * @param addrCountyId
     * @param addressFull
     * @param tagName
     * @param isDefault
     * @param phoneNum
     * @param requestManager
     */
    public void editAddress(int requestType, String id, String userName, String addrProvince, String addrCity, String addrCounty,
                            String addrProvinceId, String addrCityId, String addrCountyId, String addressFull,
                            String tagName, String isDefault, String phoneNum, RequestManagerImpl requestManager) {
        String url = "customer/buyer/address/update";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userId", localDao.getCustomer().id);
        mapData.put("id", id);
        mapData.put("userName", userName);
        mapData.put("addrProvince", addrProvince);
        mapData.put("addrCity", addrCity);
        mapData.put("addrCounty", addrCounty);
        mapData.put("provincesId", addrProvinceId);
        mapData.put("cityId", addrCityId);
        mapData.put("areaId", addrCountyId);
        mapData.put("addressFull", addressFull);
        mapData.put("tagName", tagName);
        mapData.put("isDefault", isDefault);
        mapData.put("phoneNum", phoneNum);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 邀请好友
     *
     * @param requestType
     * @param requestManager
     */
    public void invitation(int requestType, RequestManagerImpl requestManager) {
        String url = "customer/invitation";
        Map<String, Object> mapData = new ArrayMap<>();
        LoginResult loginCache = getLoginCache();
        mapData.put("userCode", loginCache.userCode);
        mapData.put("token", loginCache.token);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 会员积分
     *
     * @param requestType
     * @param requestManager
     */
    public void pointSignIn(int requestType, RequestManagerImpl requestManager) {
        String url = "/customer/pointSignin/point/detail/" + localDao.getCustomer().id;
//        String url = "/customer/pointSignin/point/detail/" + getLoginCache().;
        Map<String, Object> mapData = new ArrayMap<>();
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 获取用户信息
     *
     * @param requestType
     * @param requestManager
     */
    public void getUserInfo(int requestType, RequestManagerImpl requestManager) {
        if (getLoginCache() == null)
            return;
        String url = "customer/myInfoList";
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("token", getLoginCache().token);
        mapData.put("userCode", getLoginCache().userCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取用户信息
     *
     * @param requestType
     */
    public void getUserInfo(int requestType, String token, String userCode, RequestManagerImpl requestManager) {
        if (getLoginCache() == null)
            return;
        String url = "customer/myInfoList";
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("token", token);
        mapData.put("userCode", userCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /***
     * 注册
     * @param requestType
     * @param username
     * @param phone
     * @param varifyCode
     * @param password
     * @param invitationCode
     * @param requestManager
     */
    public void register(int requestType, String username, String phone, String varifyCode,
                         String password, String invitationCode, RequestManagerImpl requestManager) {
        String url = "customer/reg";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("username", username);
        mapData.put("phone", phone);
        mapData.put("varifyCode", varifyCode);
        mapData.put("password", password);
        mapData.put("invitationCode", invitationCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 编辑用户信息
     *
     * @param requestType
     * @param sex
     * @param headImg
     * @param age
     * @param realName
     * @param email
     * @param profession
     * @param family
     * @param address
     * @param requestManager
     */
    public void editUserInfo(int requestType, String userNick, String sex, String headImg, String age, String realName,
                             String email, String profession, String family, String address, RequestManagerImpl requestManager) {
        String url = "customer/updateMyInfo";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", localDao.getCustomer().userCode);
        mapData.put("token", getLoginCache() == null ? "" : getLoginCache().token);
        mapData.put("username", userNick);
        mapData.put("sex", sex);
        mapData.put("headImg", headImg);
        mapData.put("age", age);
        mapData.put("realName", realName);
        mapData.put("email", email);
        mapData.put("profession", profession);
        mapData.put("family", family);
        mapData.put("address", address);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 签到
     *
     * @param requestType
     * @param requestManager
     */
    public void sign(int requestType, RequestManagerImpl requestManager) {
        String url = "customer/pointSignin/point";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("memberId", localDao.getCustomer().id);
//        mapData.put("memberId", "518");
        mapData.put("operation", "SIGN_IN");
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 分享后增加积分
     *
     * @param requestType
     * @param requestManager
     */
    public void shareIntegration(int requestType, RequestManagerImpl requestManager) {
        String url = "customer/pointSignin/point";
        Map<String, Object> mapData = new ArrayMap<>();
        if (localDao.isLogin()) {
            mapData.put("memberId", localDao.getCustomer().id);
        }
//        mapData.put("memberId", "518");
        mapData.put("operation", "SHARE");
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 签到历史列表
     *
     * @param requestType
     * @param requestManager
     */
    public void signList(int requestType, RequestManagerImpl requestManager) {
        String url = "customer/pointSignin/point/signin/details/" + localDao.getCustomer().id;
        Map<String, Object> mapData = new ArrayMap<>();
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 头条资讯详情添加浏览次数
     *
     * @param requestType
     */
    public void headMessageAddCount(int requestType, String id, RequestManagerImpl requestManager) {
        String url = "customer/messageHead/headMessageAddCount";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 身份认证提交
     *
     * @param requestType
     */
    public void idCardSubmit(int requestType, String realName, String identityCardNum, String identityCardFront,
                             String identityCardReverse,
                             RequestManagerImpl requestManager) {
        String url = "customer/buyer/auth/add";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("buyerCode", getCustomer().userCode);
        mapData.put("realName", realName);
        mapData.put("identityCardNum", identityCardNum);
        mapData.put("identityCardFront", identityCardFront);
        mapData.put("identityCardReverse", identityCardReverse);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 添加银行卡
     *
     * @param requestType
     */
    public void addBankCard(int requestType, String cardName, String bankCardNum, String belongToBank,
                            String applyUserCardNum, String openBranchBank, String bankBranchNum, String cardUserPhone,
                            String bankImgFront, String bankImgBack, RequestManagerImpl requestManager) {
        String url = "customer/buyer/bindcard/add";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("buyerCode", getCustomer().userCode);
        mapData.put("cardName", cardName);
        mapData.put("bankCardNum", bankCardNum);
        mapData.put("belongToBank", belongToBank);
        mapData.put("applyUserCardNum", applyUserCardNum);
        mapData.put("openBranchBank", openBranchBank);
        mapData.put("bankBranchNum", bankBranchNum);
        mapData.put("cardUserPhone", cardUserPhone);
        mapData.put("bankImgFront", bankImgFront);
        mapData.put("bankImgBack", bankImgBack);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 银行卡列表
     *
     * @param requestType
     */
    public void bankCardList(int requestType,
                             RequestManagerImpl requestManager) {
        String url = "customer/buyer/bindcard/list";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("buyerCode", getCustomer().userCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 申请退款跳转的银行卡列表（区分是否是本人银行啊）
     *
     * @param requestType
     */
    public void bankCardListRefund(int requestType,
                                   RequestManagerImpl requestManager) {
        String url = "customer/buyer/bindcard/sortlist";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("buyerCode", getCustomer().userCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 银行卡详情
     *
     * @param requestType
     */
    public void bankCardDetail(int requestType, String id,
                               RequestManagerImpl requestManager) {
        String url = "customer/buyer/bindcard/details";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 银行卡解绑
     *
     * @param requestType
     */
    public void bankCardDel(int requestType, int id,
                            RequestManagerImpl requestManager) {
        String url = "customer/buyer/bindcard/del";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 选择结佣订单
     *
     * @param requestType
     */
    public void chooseBrokeOrders(int requestType, SelectBrokerOrderRequest request,
                                  RequestManagerImpl requestManager) {
        String url = "customer/buyer/brokerage/chooseBrokeOrders";
        String json = GsonUtils.toJson(request);
        manager.post(requestType, url, json, requestManager, false);
    }


    /**
     * 结佣记录
     *
     * @param requestType
     */
    public void brokeAmountRecord(int requestType,
                                  RequestManagerImpl requestManager) {
        String url = "customer/buyer/brokerage/brokeAmountRecord";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userId", getCustomer().id);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 楼盘订单列表接口
     *
     * @param requestType
     */
    public void devNameList(int requestType,
                            RequestManagerImpl requestManager) {
        String url = "customer/buyer/brokerage/devNameList";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userId", getCustomer().id);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 我的佣金
     *
     * @param requestType
     */
    public void myBreokerage(int requestType,
                             RequestManagerImpl requestManager) {
        String url = "customer//buyer/brokerage/myBreokerage";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userId", getCustomer().id);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 结佣详情
     *
     * @param requestType
     */
    public void brokeAmountInfo(int requestType, String id,
                                RequestManagerImpl requestManager) {
        String url = "customer/buyer/brokerage/brokeAmountInfo";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 结佣详情操作记录
     *
     * @param requestType
     */
    public void brokeOperationRecord(int requestType, String id,
                                     RequestManagerImpl requestManager) {
        String url = "customer/buyer/brokerage/brokeOperationRecord";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("id", id);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 安全绑定
     *
     * @param requestType
     * @param bandedType       绑定类型  0，微信；1、qq；2，电话号码
     * @param code             验证码 只有bandedType=2时需要上传
     * @param tokenInfo        微信，qq token 或电话号码，由bandedType确定
     * @param qqNum            qq号
     * @param conversionCommon 1 当绑定为手机号时，为1为转换为网络推客   推荐有礼跳转绑定手机号
     * @param requestManager
     */
    public void securityBanded(int requestType, int bandedType, String code, String tokenInfo, String qqNum, int conversionCommon,
                               RequestManagerImpl requestManager) {
        String url = "customer/securityBanded";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("token", getLoginCache() == null ? "" : getLoginCache().token);
        mapData.put("bandedType", bandedType);
        mapData.put("userCode", getCustomer().userCode);
        mapData.put("code", code);
        mapData.put("tokenInfo", tokenInfo);
        mapData.put("qqNum", qqNum);
        if (conversionCommon > 0) {
            mapData.put("conversionCommon", conversionCommon);
        }
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 验证验证码
     *
     * @param requestType
     * @param phone          手机号
     * @param varifyCode     验证码
     * @param type           场景： 设置、修改交易密码 传10 || 添加银行卡传11
     * @param requestManager
     */
    public void verification(int requestType, String phone, String varifyCode, String type,
                             RequestManagerImpl requestManager) {
        String url = "customer/verification";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("phone", phone);
        mapData.put("varifyCode", varifyCode);
        mapData.put("type", type);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 设置交易密码接口
     *
     * @param requestType
     * @param transactionPassword
     * @param requestManager
     */
    public void setTradePassword(int requestType, String transactionPassword,
                                 RequestManagerImpl requestManager) {
        String url = "customer/buyer/auth/password";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("buyerCode", getCustomer().userCode);
        mapData.put("transactionPassword", transactionPassword);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 验证交易密码
     *
     * @param requestType
     * @param transactionPassword
     * @param requestManager
     */
    public void checkTradePssword(int requestType, String transactionPassword,
                                  RequestManagerImpl requestManager) {
        String url = "customer/buyer/auth/validate/password";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("buyerCode", getCustomer().userCode);
        mapData.put("transactionPassword", transactionPassword);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 忘记密码 验证身份证号
     *
     * @param requestType
     * @param requestManager
     */
    public void checkIdCard(int requestType, String realName, String identityCardNum,
                            RequestManagerImpl requestManager) {
        String url = "customer/buyer/auth/validate/identity";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("buyerCode", getCustomer().userCode);
        mapData.put("realName", realName);
        mapData.put("identityCardNum", identityCardNum);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 根据userCode获取认证身份信息
     *
     * @param requestType
     * @param requestManager
     */
    public void getAuthInfo(int requestType,
                            RequestManagerImpl requestManager) {
        String url = "customer/buyer/auth/getAuthInfo";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("buyerCode", getCustomer().userCode);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 推荐列表
     *
     * @param requestType
     * @param page
     * @param requestManager
     */
    public void getRecommendList(int requestType, int page,
                                 RequestManagerImpl requestManager) {
        String url = "customer/follow/list";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("userCode", getCustomer().userCode);
        mapData.put("pageSize", PAGE_SIZE);
        mapData.put("pageNum", page);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 我的推荐详情
     *
     * @param requestType
     * @param brokerCustomerId
     * @param requestManager
     */
    public void getRecommendDetail(int requestType, String brokerCustomerId,
                                   RequestManagerImpl requestManager) {
        String url = "customer/follow/detail";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("brokerCustomerId", brokerCustomerId);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 申请结佣
     *
     * @param requestType
     */
    public void subminBrokeAmount(int requestType, ApplyBrokerRequest request,
                                  RequestManagerImpl requestManager) {
        String url = "customer/buyer/brokerage/subminBrokeAmount";

        String json = GsonUtils.toJson(request);
        manager.post(requestType, url, json, requestManager, false);
    }


    /**
     * 首页弹窗广告
     *
     * @param requestType
     */
    public void getAdList(int requestType,
                          RequestManagerImpl requestManager) {
        String url = "/customer/home/page/popupwindow/enabled";
        manager.get(requestType, url, null, requestManager);
    }


    /**
     * 银行卡四要素验证
     *
     * @param requestType
     */
    public void checkBindCard(int requestType, String bankCardNum, String applyUserCardNum, String cardUserPhone,
                              String cardName,
                              RequestManagerImpl requestManager) {
        String url = "customer/buyer/bindcard/checkBindCard";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("bankCardNum", bankCardNum);
        mapData.put("applyUserCardNum", applyUserCardNum);
        mapData.put("cardUserPhone", cardUserPhone);
        mapData.put("cardName", cardName);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 首页 获取城市行情
     *
     * @param requestType
     */
    public void getCityHousePrice(int requestType, String cityId, RequestManagerImpl requestManager) {
        String url = "customer/sys/cityPrice";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityId", cityId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 楼盘详情  领取优惠
     *
     * @param requestType
     */
    public void discountCount(int requestType, String devId, String type,
                              RequestManagerImpl requestManager) {
        String url = "customer/estate/discount/devDiscount/count";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        mapData.put("type", type);
        mapData.put("userId", getCustomer().id);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 楼盘详情优惠列表
     *
     * @param requestType
     */
    public void discountList(int requestType, String devId,
                             RequestManagerImpl requestManager) {
        String url = "customer/estate/discount/list";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devId", devId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 首页热门楼盘
     *
     * @param requestType
     */
    public void getHotDevs(int requestType, String cityId,
                           RequestManagerImpl requestManager) {
        String url = "customer/estate/customer/getHotDevs";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityId", cityId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 近期开盘 周边楼盘
     *
     * @param requestType
     */
    public void getNearDevs(int requestType, String cityId, List<Double> coordinate,
                            RequestManagerImpl requestManager) {
        String url = "/customer/estate/customer/getNearDevs";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("cityId", cityId);
        mapData.put("coordinate", coordinate);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 我要找房-条件查询
     *
     * @param requestType
     */
    public void lookHouseList(int requestType, double minBudget, double maxBudget, int room, String cityId,
                              RequestManagerImpl requestManager) {
        String url = "/customer/lookhouse/devList";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("minBudget", minBudget);
        mapData.put("maxBudget", maxBudget);
        mapData.put("room", room);
        mapData.put("cityId", cityId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 我要找房-条件查询
     *
     * @param requestType
     */
    public void devContrast(int requestType, ArrayList<String> devIds,
                            RequestManagerImpl requestManager) {
        String url = "/customer/lookhouse/devContrast";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("devIds", devIds);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 第三方接口  身份认证ocr
     *
     * @param requestType
     */
    public void iDcardOcr(int requestType, IdCardOcrRequestBean idCardOcrRequestBean, RequestManagerImpl requestManager) {
        String url = ID_CARD_OCR_URL;
        String json = GsonUtils.toJson(idCardOcrRequestBean);
        manager.ocr(requestType, url, json, ID_CARD_OCR_APP_CODE, requestManager);
    }


    /**
     * 第三方接口  银行卡ocr
     *
     * @param requestType
     */
    public void bankCardOcr(int requestType, BankCardOcrRequestBean bankCardOcrRequestBean, RequestManagerImpl requestManager) {
        String url = BAKN_CARD_OCR_URL;
        String json = GsonUtils.toJson(bankCardOcrRequestBean);
        manager.ocr(requestType, url, json, BANK_CARD_OCR_APP_CODE, requestManager);
    }


    public void editUserHeader(int requestType, String headerImage, RequestManagerImpl requestManager) {
        editUserInfo(requestType, null, null, headerImage, null, null, null, null, null, null, requestManager);
    }

    public void editUserNick(int requestType, String userNick, RequestManagerImpl requestManager) {
        editUserInfo(requestType, userNick, null, null, null, null, null, null, null, null, requestManager);
    }

    public void editUserEmail(int requestType, String email, RequestManagerImpl requestManager) {
        editUserInfo(requestType, null, null, null, null, null, email, null, null, null, requestManager);
    }

    public void editUserName(int requestType, String userName, RequestManagerImpl requestManager) {
        editUserInfo(requestType, null, null, null, null, userName, null, null, null, null, requestManager);
    }

    public void editUserSex(int requestType, String sex, RequestManagerImpl requestManager) {
        editUserInfo(requestType, null, sex, null, null, null, null, null, null, null, requestManager);
    }

    public void editUserBirthday(int requestType, String birthday, RequestManagerImpl requestManager) {
        editUserInfo(requestType, null, null, null, birthday, null, null, null, null, null, requestManager);
    }

    public void editUserProfession(int requestType, String profession, RequestManagerImpl requestManager) {
        editUserInfo(requestType, null, null, null, null, null, null, profession, null, null, requestManager);
    }

    public void editUserFamily(int requestType, String family, RequestManagerImpl requestManager) {
        editUserInfo(requestType, null, null, null, null, null, null, null, family, null, requestManager);
    }

    public void editUserAddress(int requestType, String address, RequestManagerImpl requestManager) {
        editUserInfo(requestType, null, null, null, null, null, null, null, null, address, requestManager);
    }

    /**
     * 获取银行列表
     *
     * @param requestType
     * @param requestManager
     */
    public void getBankList(int requestType, RequestManagerImpl requestManager) {
        String url = Constants.PUB + "kingdee/api/bankAll";
        Map<String, Object> mapData = new ArrayMap<>();
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取开户行支行
     *
     * @param requestType
     */
    public void getSubBank(int requestType, String bankId, String provinceId, String cityId,
                           RequestManagerImpl requestManager) {
        String url = Constants.PUB + "kingdee/api/bankBranchList";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("bankId", bankId);
        mapData.put("provinceId", provinceId);
        mapData.put("cityId", cityId);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 启动广告图
     *
     * @param requestType
     */
    public void selectOneEnableAd(int requestType, int appType,
                                  RequestManagerImpl requestManager) {
        String url = Constants.PUB + "/cms/app/screen/ad/selectOneEnableAd";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("appType", appType);
        manager.post(requestType, url, mapData, requestManager, false);
    }


    /**
     * 更新银行卡信息
     *
     * @param requestType
     * @param bankId
     * @param linkNumber     银行卡联行号
     * @param openBranchBank 开户支行
     * @param bankImgFront
     * @param bankImgBack
     * @param requestManager 返回成功/失败
     */
    public void updateBankCardInfo(int requestType, String bankId, String linkNumber, String openBranchBank,
                                   String bankImgFront, String bankImgBack,
                                   RequestManagerImpl requestManager) {
        String url = "customer/buyer/bindcard/update";
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("id", bankId);
        mapData.put("linkNumber", linkNumber);
        mapData.put("openBranchBank", openBranchBank);
        mapData.put("bankImgFront", bankImgFront);
        mapData.put("bankImgBack", bankImgBack);
        manager.post(requestType, url, mapData, requestManager, false);
    }

    private String getCityIdCache() {
        LocateCache city = getLocateCache();
        if (city == null) {
            return "110100";
        } else {
            return TextUtils.isEmpty(city.cityId) ? "100110" : city.cityId;
        }
    }

    /**
     * 消息主页列表
     *
     * @param requestType
     * @param requestManager
     */
    public void getMessageOutList(int requestType,
                                  RequestManagerImpl requestManager) {
        String id = "-1";
        if (getCustomer() != null)
            id = getCustomer().id;
        String url = Constants.PUB + "push/msgBuyerOuterList/" + id + "/1/" + getCityIdCache();
        Map<String, Object> mapData = new ArrayMap<>();
        manager.post(requestType, url, mapData, requestManager, false);
    }

    /**
     * 获取消息列表
     * 平台类型
     * BROKER(0, "经纪人端"),
     * CUSTOMER(1, "好房端"),
     * BUTLER(2, "管家端"),
     * BROKER_MINI(3, "经纪人端小程序");
     *
     * @param msgType     消息类型
     * @param requestType
     */
    public void getMessageList(int requestType, int msgType, int pageNum,
                               RequestManagerImpl requestManager) {
        String id = "-1";
        if (getCustomer() != null)
            id = getCustomer().id;
        String url = Constants.PUB + "/push/msglist/" + id + "/" + msgType + "/1/" + pageNum + "/20";
        manager.get(requestType, url, null, requestManager);
    }

}
