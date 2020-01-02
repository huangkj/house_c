package com.easylife.house.customer.http.impl;


import com.easylife.house.customer.http.bean.NetBaseStatus;

/**
 * 类描述：请求接口回调
 * Created by zgm on 2017/2/22 0021.
 */

public interface RequestManagerImpl {
    /**
     * @param response    需要使用的数据的Json字符串
     * @param requestType
     */
    void onSuccess(String response, int requestType);

    /**
     * @param code        携带错误码以及错误文本
     * @param requestType
     */
    void onFail(NetBaseStatus code, int requestType);
}
