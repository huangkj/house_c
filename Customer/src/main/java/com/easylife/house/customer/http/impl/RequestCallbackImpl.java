package com.easylife.house.customer.http.impl;

import android.util.ArrayMap;

import java.util.Map;

/**
 * 类描述:请求接口管理
 * Created by zgm on 2017/2/22 0021.
 */

public interface RequestCallbackImpl {

    /**
     * get请求
     *
     * @param url
     * @param requestManager
     */
    void get(int requestType, String url, ArrayMap<String, Object> arrayMap, RequestManagerImpl requestManager);

    /**
     * post json参数请求
     *
     * @param jsonStr
     */
    void postSellingPointRequest(String jsonStr);

    /**
     * post请求
     *
     * @param url
     * @param isImg          是不是图片上传/头像除外
     * @param arrayMap
     * @param requestManager
     */
    void post(int requestType, String url, Map<String, Object> arrayMap, RequestManagerImpl requestManager, boolean isImg);

    void post(int requestType, String url, String json, RequestManagerImpl requestManager, boolean isImg);

}
