package com.easylife.house.customer.http.factory;

/**
 * 类描述:网络请求工厂
 * Created by zgm on 2017/2/22 0021.
 */

public class RequestFactory {

    public static OkhttpManager getOkManager(){
        return OkhttpManager.getInstance();
    }
}
