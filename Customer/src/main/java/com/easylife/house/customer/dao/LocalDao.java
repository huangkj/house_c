package com.easylife.house.customer.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.LocateCache;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.SettingsUtil;
import com.google.gson.Gson;

/**
 * 本地缓存管理
 */
public class LocalDao {
    private Context context;

    public static String FOLDER_CACHE_PUB = "folder_cache_pub";
    public static String FOLDER_CACHE_IM_SUB_TIME = "folder_cache_im_sub_time";
    public static String FOLDER_CACHE_IM_TITLE_TIME = "folder_cache_im_title_time";
    public static String FOLDER_CACHE_CUSTOMER = "folder_cache_Customer";
    public static String FOLDER_CACHE_IS_CLEAR = "folder_cache_is_clear";
    public static String FOLDER_CACHE_CITY_ID = "folder_cache_city_id";
    public static String FOLDER_CACHE_CITY = "folder_cache_city";
    public static String FOLDER_CACHE_IS_FIRST_INTO = "folder_cache_is_first_into";
    public static String FOLDER_CACHE_CITY_BEAN = "folder_cache_city_bean";

    public static String KEY_VERSION_INT = "version";
    public static String KEY_PUB_PHONE_LOGIN_LAST = "tag_pub_phone_login_last";
    public static String KEY_PUB_AD_DIALOG_COUNT = "key_pub_ad_dialog_count";
    public static String KEY_LAUNCH_FIRST = "KEY_LAUNCH_FIRST";
    public static String KEY_SHOW_PUSH_MESSAGE = "KEY_SHOW_PUSH_MESSAGE";
    public static String KEY_PUSH_POINT_TIP_HEADLINE = "KEY_PUSH_POINT_TIP_HEADLINE";
    public static String KEY_PUSH_POINT_TIP_HOUSE = "KEY_PUSH_POINT_TIP_HOUSE";
    public static String KEY_PUB_TIP_CONTACT = "tip_contact";
    public static String KEY_LOCATE_CACHE = "key_locate_cache";

    public static String KEY_Customer = "key_Customer";
    public static String KEY_LOGIN_CACHE = "key_Customer_code";
    public static String KEY_CLIENT_ID = "KEY_CLIENT_ID";
    public static String KEY_IS_CLEAR_ID = "KEY_IS_CLEAR_ID";
    public static String KEY_IS_CITY_ID = "KEY_IS_CITY_ID";
    public static String KEY_IS_CITY = "KEY_IS_CITY";
    public static String KEY_IS_FIRST = "KEY_IS_FIRST";
    public static String KEY_IS_CITY_BEAN_ID = "KEY_IS_CITY_BEAN_ID";
    public static String KEY_IM_SUB_TIME_ID = "KEY_IM_SUB_TIME_ID";
    public static String KEY_IM_TITLE_TIME_ID = "KEY_IM_TITLE_TIME_ID";

    //搜索历史记录列表保存
    public static final String SEARCH_HISTORY = "SEARCH_HISTORY";
    public static final String SEARCH_HISTORY_VALUE = "SEARCH_HISTORY_VALUE";

    public static String KEY_UN_READ_MSG_COUNT = "KEY_UN_READ_MSG_COUNT";//未读消息数量

    public LocalDao(Context context) {
        this.context = context;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getSaveVersion() {
        return context.getSharedPreferences(FOLDER_CACHE_PUB, 0).getInt(
                KEY_VERSION_INT, 0);
    }

    /**
     * 保存当前版本号
     */
    public void saveCurrentVersions(int version) {
        context.getSharedPreferences(FOLDER_CACHE_PUB, 0).edit()
                .putInt(KEY_VERSION_INT, version).commit();
    }

    /**
     * 保存文本数据
     *
     * @param spName 文件名
     * @param key    键
     * @param value  值
     */
    public void saveString(String spName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(spName, 0);
        boolean commit = sp.edit().putString(key, value).commit();
    }

    /**
     * 获取指定路径文本数据
     *
     * @param spName
     * @param key      键
     * @param defValue 值
     * @return
     */
    public String getString(String spName, String key, String defValue) {
        String string = context.getSharedPreferences(spName, 0).getString(key, defValue);
        return string;
    }

    /**
     * 保存int数据
     *
     * @param spName 文件名
     * @param key    键
     * @param value  值
     */
    public void saveInt(String spName, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(spName, 0);
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 获取保存的Int数据
     *
     * @param spName
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String spName, String key, int defValue) {
        return context.getSharedPreferences(spName, 0).getInt(key, defValue);
    }

    /**
     * 删除指定的key
     *
     * @param spName
     * @param key
     */
    public void remove(String spName, String key) {
        context.getSharedPreferences(spName, 0).edit().remove(key).commit();
    }

    /**
     * 缓存用户信息
     *
     * @param Customer
     */
    public void saveCustomer(Customer Customer) {
        try {
            String str = new Gson().toJson(Customer);
            saveString(FOLDER_CACHE_CUSTOMER, KEY_Customer, str);
        } catch (Exception e) {
        }
    }

    /**
     * 获取用户缓存
     *
     * @return
     */
    public Customer getCustomer() {
        String strSelf = getString(FOLDER_CACHE_CUSTOMER, KEY_Customer, null);
        return new Gson().fromJson(strSelf, Customer.class);
    }

    /**
     * 当前用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        if (getLoginCache() == null)
            return false;
        if (TextUtils.isEmpty(getLoginCache().userCode))
            return false;
        return true;
    }

    /**
     * 是否是第一次启动，如果是第一次启动则打开引导页
     *
     * @return
     */
    public boolean isFirst() {
        String cache = getCacheStr(FOLDER_CACHE_PUB, KEY_LAUNCH_FIRST);
        boolean isFirst = true;
        if (TextUtils.isEmpty(cache)) {
            isFirst = true;
        } else {
            if (cache.equals(SettingsUtil.getVersionName(context))) {
                isFirst = false;
            } else {
                isFirst = true;
            }
        }
        saveString(FOLDER_CACHE_PUB, KEY_LAUNCH_FIRST, SettingsUtil.getVersionName(context));
        return isFirst;
    }

    /**
     * 推送开关
     *
     * @return
     */
    public boolean showPushMessage() {
        boolean showPushMessage = TextUtils.isEmpty(getCacheStr(FOLDER_CACHE_PUB, KEY_SHOW_PUSH_MESSAGE));
        return showPushMessage;
    }

    public void saveShowPushMessage(boolean show) {
        saveString(FOLDER_CACHE_PUB, KEY_SHOW_PUSH_MESSAGE, show ? null : "KEY_SHOW_PUSH_MESSAGE");
    }

    /**
     * 头条消息未读状态
     *
     * @return
     */
    public boolean showPushTipHeadline() {
        boolean showPushMessage = TextUtils.isEmpty(getCacheStr(FOLDER_CACHE_PUB, KEY_PUSH_POINT_TIP_HEADLINE));
        return showPushMessage;
    }

    public void savePushTipStatusHeadline(boolean hasRead) {
        saveString(FOLDER_CACHE_PUB, KEY_PUSH_POINT_TIP_HEADLINE, !hasRead ? "" : "KEY_PUSH_POINT_TIP_HEADLINE");
    }

    /**
     * 楼盘订阅消息未读状态
     *
     * @return
     */
    public boolean showPushTipHouse() {
        boolean showPushMessage = TextUtils.isEmpty(getCacheStr(FOLDER_CACHE_PUB, KEY_PUSH_POINT_TIP_HOUSE));
        return showPushMessage;
    }

    public void savePushTipStatusHouse(boolean hasRead) {
        saveString(FOLDER_CACHE_PUB, KEY_PUSH_POINT_TIP_HOUSE, !hasRead ? "" : "KEY_PUSH_POINT_TIP_HOUSE");
    }


    /**
     * 退出登录
     */
    public void loginOut() {
        remove(FOLDER_CACHE_CUSTOMER, KEY_Customer);
        remove(FOLDER_CACHE_CUSTOMER, KEY_LOGIN_CACHE);
    }

    public LoginResult getLoginCache() {
        try {
            return new Gson().fromJson(getCacheStr(FOLDER_CACHE_CUSTOMER, KEY_LOGIN_CACHE), LoginResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveLoginCache(String loginCache) {
        LogOut.d("用户登录缓存", loginCache);
        saveString(FOLDER_CACHE_CUSTOMER, KEY_LOGIN_CACHE, loginCache);
    }

    public String getClientID() {
        return getCacheStr(FOLDER_CACHE_PUB, KEY_CLIENT_ID);
    }

    public void saveClientID(String clientID) {
        saveString(FOLDER_CACHE_PUB, KEY_CLIENT_ID, clientID);
    }

    public String getIsClear() {
        return getCacheStr(FOLDER_CACHE_IS_CLEAR, KEY_IS_CLEAR_ID);
    }

    public void saveIsClear(String isClear) {
        saveString(FOLDER_CACHE_IS_CLEAR, KEY_IS_CLEAR_ID, isClear);
    }

    public String getCityId() {
        return getCacheStr(FOLDER_CACHE_CITY_ID, KEY_IS_CITY_ID);
    }

    public void saveCityId(String cityId) {
        saveString(FOLDER_CACHE_CITY_ID, KEY_IS_CITY_ID, cityId);
    }

    public String getCity() {
        return getCacheStr(FOLDER_CACHE_CITY, KEY_IS_CITY);
    }

    public void saveCity(String city) {
        saveString(FOLDER_CACHE_CITY, KEY_IS_CITY, city);
    }

    public String getIsFirst() {
        return getCacheStr(FOLDER_CACHE_IS_FIRST_INTO, KEY_IS_FIRST);
    }

    public void saveIsFirst(String isFirst) {
        saveString(FOLDER_CACHE_IS_FIRST_INTO, KEY_IS_FIRST, isFirst);
    }

    public String getCityCache() {
        return getCacheStr(FOLDER_CACHE_CITY_BEAN, KEY_IS_CITY_BEAN_ID);
    }

    public void saveCityCache(String cityId) {
        saveString(FOLDER_CACHE_CITY_BEAN, KEY_IS_CITY_BEAN_ID, cityId);
    }

    public LocateCache getLocateCache() {
        try {
            return new Gson().fromJson(getCacheStr(FOLDER_CACHE_PUB, KEY_LOCATE_CACHE), LocateCache.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveLocateCache(String CustomerCode) {
        saveString(FOLDER_CACHE_PUB, KEY_LOCATE_CACHE, CustomerCode);
    }

    /**
     * 查询缓存数据
     *
     * @param spName
     * @param key
     * @return
     */
    public String getCacheStr(String spName, String key) {
        return getString(spName, key, null);
    }

    /**
     * 获取最后一个登陆的手机号
     *
     * @return
     */
    public String getLoginLastPhone() {
        return getString(FOLDER_CACHE_PUB, KEY_PUB_PHONE_LOGIN_LAST, null);
    }

    /**
     * 保存最后一个登陆的手机号
     *
     * @param phone
     */
    public void saveLoiginLastPhone(String phone) {
        saveString(FOLDER_CACHE_PUB, KEY_PUB_PHONE_LOGIN_LAST, phone);
    }

    /**
     * 是否已提示过导入通讯录
     *
     * @return
     */
    public boolean getTipContact() {
        return !TextUtils.isEmpty(getString(FOLDER_CACHE_PUB, KEY_PUB_TIP_CONTACT, null));
    }

    /**
     * 获取通讯录数据已提示过
     */
    public void saveTipContact() {
        saveString(FOLDER_CACHE_PUB, KEY_PUB_TIP_CONTACT, "1");
    }

    /**
     * 保存list列表
     *
     * @param fileName 文件名称
     * @param key      key值
     * @param json     要保存的json串
     */
    public void saveListCache(String fileName, String key, String json) {
        saveString(fileName, key, json);
    }

    /**
     * 获取list列表json串
     *
     * @param fileName 文件名
     * @param key      key值
     * @return 返回json串内容
     */
    public String getListCache(String fileName, String key) {
        return getString(fileName, key, "");
    }

}
