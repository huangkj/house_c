package com.easylife.house.customer.config;

import com.easylife.house.customer.BuildConfig;

/**
 * Created by Mars on 2017/3/14 20:24.
 * 描述：网络等配置文件
 */
public class Constants {
    //    public static final String HOST = "https://customer.lifeat.cn/";//生产环境
//        public static final String HOST = "https://dcustomer.lifeat.cn:45788/";//开发环境
//    public static final String HOST = "https://tcustomer.lifeat.cn:45788/";//测试环境
    //    public static final String HOST = "https://hcustomer.lifeat.cn:45788/";//灰度环境
    public static final String HOST = BuildConfig.HOST;

    public static final String SEARCH_HOST = HOST;
    //首页二维码扫描判断地址
    public static final String URL_AGREEMENT_REGISTER = HOST + "customer/page/userProtocol.jsp";//注册协议
    public static final String URL_AGREEMENT_LOOK_HOUSE = HOST + "customer/page/delegate.jsp";//预约看房协议

    // 装修知识url
    public static final String URL_FITMENT_KNOWLEDGE = HOST + "customer/page/delegate.jsp";
    // 设计案例url
    public static final String URL_FITMENT_DESIGN_CASE = HOST + "customer/page/delegate.jsp";

    //    public static final String QR_URL = "https://broker.lifeat.cn";//生产环境
//    public static final String QR_URL = "https://dbroker.lifeat.cn";//开发环境
//    public static final String QR_URL = "https://tbroker.lifeat.cn";//测试环境
//    public static final String QR_URL = "https://hbroker.lifeat.cn";//灰度环境
    public static final String QR_URL = BuildConfig.QR_URL;


    public static String PHONE_HELP = "01050827679";

    //    public static String CTOB = "https://page.lifeat.cn/#/";//B端地址库(生产)
//    public static String CTOB = "https://dpage.lifeat.cn:45788/#/";//B端地址库(开发)
//    public static String CTOB = "https://tpage.lifeat.cn:45788/#/";//B端地址库(测试)
//    public static String CTOB = "https://hpage.lifeat.cn:45788/#/";//B端地址库(灰度)
    public static String CTOB = BuildConfig.CTOB;


    //    public static String PUB = "https://api.lifeat.cn/";//公共api(生产)
//    public static String PUB = "https://dapi.lifeat.cn:45788/";//公共api(开发)
//    public static String PUB = "https://tapi.lifeat.cn:45788/";//公共api(测试)
//    public static String PUB = "https://hapi.lifeat.cn:45788/";//公共api(灰度)
    public static String PUB = BuildConfig.PUB;


    /**
     * 埋点地址
     */
//    public static final String URL_SELLING_POINT = "https://collection.lifeat.cn/collection";//正式
//    public static final String URL_SELLING_POINT = "https://tcollection.lifeat.cn:45788/collection";//测试
//    public static final String URL_SELLING_POINT = "https://hcollection.lifeat.cn:45788/collection";//灰度
    public static final String URL_SELLING_POINT = BuildConfig.URL_SELLING_POINT;


    public static String getCTOBUrl(String brokerName, String brokerPhone) {
        return CTOB + "recommendCustomer?brokerName=" + brokerName + "&brokerPhone=" + brokerPhone;
    }


    public static final String VERSION_CODE = "120";
    //楼栋信息weburl
    public static final String BUILD_HOUSES_URL = HOST + "customer/page/housing.jsp?";
    //楼栋信息 url
    public static final String HOUSE_NUMBER_URL = HOST + "customer/page/anchor.jsp?devId=";
    //资讯分享 url
    public static final String MSG_SHARE_URL = HOST + "customer/page/anchor.jsp?devId=";

    public static final String URL_WEB_BASE = HOST + "customer/page/news.jsp?id=";
    //保险
    public static final String URL_WEB_INSURANCE = CTOB + "insurance";

    //阿里云 ocr身份证认证接口appcode
    public static final String ID_CARD_OCR_APP_CODE = "05d90258396c4cca877cbc306330e259";
    public static final String BANK_CARD_OCR_APP_CODE = "05d90258396c4cca877cbc306330e259";
    public static final String ID_CARD_OCR_URL = "http://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json";
    public static final String BAKN_CARD_OCR_URL = "http://yhk.market.alicloudapi.com/rest/160601/ocr/ocr_bank_card.json";

    /**
     * 更新服务广播过滤字符
     */
    public static String UPDATE_SERVICES_PROGRESS = "update_services_progress";
    /**
     * 图片压缩最大宽度
     */
    public static final int IMG_ZOOM_WIDTH_MAX = 640;
    /**
     * 图片压缩最大高度
     */
    public static final int IMG_ZOOM_HEIGHT_MAX = 960;
    /**
     * 图片压缩质量
     */
    public static final int IMG_ZOOM_QUALITY = 80;
    public static final int PAGE_SIZE = 10;

    public static final int PAGE_START = 1;


    /**
     * 购房优惠说明书 需传递姓名和手机号 地址+ name=李四&phone=13001998378
     */
    public static final String URL_FAVORABLE_VIP = HOST + "customer/page/fav.jsp?";
    /**
     * 认购通知书  需传递orderCode  地址:http://10.2.15.21:13000/customer/page/buy.jsp?orderCode=10251498030902989815
     */
    public static final String URL_FAVORABLE_DEV = HOST + "customer/page/buy.jsp?orderCode=";
    /**
     * 房源锁定说明  纯静态页面 地址http://10.2.15.21:13000/customer/page/lock.jsp
     */
    public static final String URL_HOUSE_LOCK_RULE = HOST + "customer/page/lock.jsp";
    /**
     * 测试支付成功
     * order/notify?out_trade_no=595464ff00cce33a951b0dcc&trade_status=TRADE_SUCCESS
     * 595464ff00cce33a951b0dcc
     */
    public static final String URL_PAY_SUCC_TEST = HOST + "customer/order/notify?trade_status=TRADE_SUCCESS&trade_no&out_trade_no=";
    /**
     * 额度说明和常见问题 纯静态页面 地址http://10.2.15.21:13000/customer/page/limit.jsp
     */
    public static final String URL_ORDER_DESC = HOST + "customer/page/limit.jsp";
    /**
     * 户型分享 需传递devId，houseName和projectId http://10.2.15.21:13000/customer/page/share.jsp?devId=14&houseName=A&projectId=536
     */
    public static final String URL_HOUSE_TYPE_SHARE = HOST + "customer/page/share.jsp?devId=14&houseName=A&projectId=536";
    /**
     * 在线退款流程说明   http://10.2.15.21:13000/customer/page/refund-ol.jsp
     */
    public static final String URL_REFUND_DESC = HOST + "customer/page/refund-ol.jsp";
    /**
     * 城市购房管理约定
     */
    public static final String URL_CITY_RULE = HOST + "customer/page/require.jsp?city=";


    /**
     * 品牌地产H5
     */
    public static final String BRAND_HOUSE_URL = CTOB + "Branddetails";

    /**
     * 活动游戏H5
     */
    public static final String ACTIVITY_GAME_URL = CTOB + "activity?buyerId=";


    /**
     * 商铺租赁详情
     */
    public static final String SHOP_RENT_URL = CTOB + "Shopdetail";


    /**
     * 签到规则h5
     */
    public static final String SIGN_RULE_URL = CTOB + "rule";

    /**
     * 积分商城
     */
    public static final String WEB_INTEGRAL_STORE = CTOB + "mall";
    /**
     * 兑换记录
     */
    public static final String WEB_STORE_CONVERSION = CTOB + "conversion";


}
