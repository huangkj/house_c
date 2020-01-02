package com.easylife.house.customer.ui.pub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.App;
import com.easylife.house.customer.bean.CheckLogInJsBean;
import com.easylife.house.customer.bean.JsBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.ui.mine.AddressManagerActivity;
import com.easylife.house.customer.ui.mine.RecommendDetailActivity;
import com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity;
import com.easylife.house.customer.ui.mine.integral.IntegralActivity;
import com.easylife.house.customer.ui.mine.prize.PrizeActivity;
import com.easylife.house.customer.ui.mine.userinfo.UserInfoActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.GsonUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import static com.easylife.house.customer.ui.mine.accountsafe.BindMobileActivity.REQUEST_CODE_BIND_RECOMMEND;

/**
 * 创建时间：2017年10月26日 09:21:12
 * 类描述：Js调用安卓方法
 */

public class WebPlanJsInterface {
    private final Activity context;
    private Intent clientIntent;
    private ServerDao dao;
    private SaveImageCallBack mSaveImageCallBack;

    public WebPlanJsInterface(Activity context) {
        this.context = context;
    }

    public WebPlanJsInterface(Activity context, ServerDao dao) {
        this.context = context;
        this.dao = dao;
    }

    public WebPlanJsInterface(Activity context, Intent intent) {
        this.context = context;
        clientIntent = intent;
    }


    interface SaveImageCallBack {
        void saveImageCallBack(String url);
    }

    public void setSaveImageCallBack(SaveImageCallBack callBack) {
        mSaveImageCallBack = callBack;
    }

    /**
     * 跳转户型详情页面
     *
     * @param devId
     */
    @JavascriptInterface
    public void detailestate(String devId) {
        context.startActivity(new Intent(context, HouseDetailActivity.class).putExtra("DEV_ID", devId));
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MobclickAgent.onEvent(context, "brand_house");
            }
        });
    }

    /**
     * 联系我们 打电话
     *
     * @param phone
     */

    @JavascriptInterface
    public void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.applicationContext.startActivity(intent);
    }


    /**
     * 跳转我的奖品页面
     */
    @JavascriptInterface
    public void IntoMyPrizesListVC() {
        if (dao.isLogin()) {
            Intent intent = new Intent(context, PrizeActivity.class);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, LoginByVerifyCodeActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转推荐详情
     */
    @JavascriptInterface
    public void intoRecommendDetail(String brokerCustomerId) {
        RecommendDetailActivity.startActivity(context, brokerCustomerId);
    }


    /**
     * 是否在app环境
     */
    @JavascriptInterface
    public boolean isApp() {
        return true;
    }

    /**
     * 跳转推荐详情
     */
    @JavascriptInterface
    public void IntoRecommendDetail(String brokerCustomerID) {
        RecommendDetailActivity.startActivity(context, brokerCustomerID);
    }

    /**
     * 跳转我的积分
     */
    @JavascriptInterface
    public void IntoMyScoreVC() {
        if (dao.isLogin()) {
            Intent intent = new Intent(context, IntegralActivity.class);
            context.startActivity(intent);
        } else {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort("请先登录");
                }
            });
        }
    }

    /**
     * 跳转地址管理列表
     */
    @JavascriptInterface
    public void IntoMyAddress() {
        AddressManagerActivity.startActivity(context);
    }

    /**
     * 获取登录账号基本信息-用户姓名和手机号
     *
     * @return
     */
    @JavascriptInterface
    public String isLogin() {
        Intent intent = new Intent(context, LoginByVerifyCodeActivity.class);
        if (dao == null)
            return null;
        if (!dao.isLogin()) {
//            RECOMMEND_BIND_PHONG_FLAG = true;
            context.startActivity(intent);
            return null;
        }
        JsBean addressBean = new JsBean();
        addressBean.name = dao.getCustomer().username;
        addressBean.phone = dao.getCustomer().phone;
        return GsonUtils.toJson(addressBean);
    }

    /**
     * 绑定手机号  推荐有礼调
     *
     * @return
     */
    @JavascriptInterface
    public String bindPhone() {
        if (dao == null) {
            Intent intent = new Intent(context, BindMobileActivity.class);
            context.startActivityForResult(intent, REQUEST_CODE_BIND_RECOMMEND);
            return null;
        }
        String phone = dao.getCustomer().phone;
        if (TextUtils.isEmpty(phone)) {
//            RECOMMEND_BIND_PHONG_FLAG = true;
            Intent intent = new Intent(context, BindMobileActivity.class);
            intent.putExtra("type", REQUEST_CODE_BIND_RECOMMEND);
            context.startActivity(intent);
//            BindMobileActivity.startActivity(context,REQUEST_CODE_BIND_RECOMMEND, REQUEST_CODE_BIND_RECOMMEND);
            return null;
        } else {
            return phone;
        }
    }


    /**
     * 返回  积分商城用
     */
    @JavascriptInterface
    public void PopToScoreShopVC() {
        if (context instanceof StoreWebActivity) {
            ((StoreWebActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (((StoreWebActivity) context).webview.canGoBack()) {
                        ((StoreWebActivity) context).webview.goBack();
                    }
                }
            });
        }
    }


    /**
     * 积分商城 判断是否登录
     *
     * @return
     */
    @JavascriptInterface
    public String CheckLogin() {
        Intent intent = new Intent(context, LoginByVerifyCodeActivity.class);
        if (dao == null)
            return null;
        if (!dao.isLogin()) {
            context.startActivity(intent);
            return null;
        }
        CheckLogInJsBean bean = new CheckLogInJsBean();
        bean.userId = dao.getCustomer().id;
        bean.userPhone = dao.getCustomer().phone;
        bean.userName = dao.getCustomer().username;
        return GsonUtils.toJson(bean);
    }


    @JavascriptInterface
    public String getUserData() {

        CheckLogInJsBean bean = new CheckLogInJsBean();

        if (dao == null)
            return GsonUtils.toJson(bean);

        if (!dao.isLogin()) {
            return GsonUtils.toJson(bean);
        }

        bean.userId = dao.getCustomer().id;
        bean.userPhone = dao.getCustomer().phone;
        bean.userName = dao.getCustomer().username;
        return GsonUtils.toJson(bean);
    }


    /**
     * 是否完善了用户信息
     *
     * @return
     */
    @JavascriptInterface
    public String isPerfect() {
        if (dao == null)
            return "0";
        if (dao.isLogin()) {
            boolean authentication = dao.getCustomer().authentication;
            return authentication ? "1" : "0";
        } else {
            return "0";
        }


    }


    /**
     * 跳转到我的账号页面
     *
     * @return
     */
    @JavascriptInterface
    public void IntoPerfectInfoVC() {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void saveImg(String url) {
        if (mSaveImageCallBack != null) {
            mSaveImageCallBack.saveImageCallBack(url);
        }
//
//        //使用兼容库就无需判断系统版本
//        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(App.applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
//            //拥有权限，执行操作
//
//            String imgName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//            FileUtils.createDirs();
//            File file = new File(SDPATH, imgName);
//            File img = null;
//            try {
//                img = Glide.with(context).load(url).downloadOnly(210, 210).get();
//                com.blankj.utilcode.util.FileUtils.copyFile(img, file);
//
//                ((Activity) context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtils.showShort("图片已保存到" + SDPATH);
//                    }
//                });
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//
//
//        } else {
//            //没有权限，向用户请求权限
//            ActivityCompat.requestPermissions(((Activity) context), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_FOR_WRITE_PERMISSION);
//        }
//

    }


    /**
     * 获取城市
     *
     * @return
     */
    @JavascriptInterface
    public String GetLocateCityName() {
        if (SearchSingleton.getIstance() != null) {
            return SearchSingleton.getIstance().city;
        } else {
            return "";
        }
    }

    /**
     * 保险H5 统计方法
     *
     * @return
     */
    @JavascriptInterface
    public void insuranceItemMobClick(final String insName) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("Insurance_item", insName);
                MobclickAgent.onEvent(context, "Insurance_item", map);

            }
        });
    }


}