package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/9/26 10:49.
 * 描述：通联返回参数
 */

public class ParamPay implements Serializable {

    /**
     * "inputCharset": "1",
     * "receiveUrl": "http://10.2.15.21:13000/customer/tlt/notify",
     * "version": "v1.0",
     * "signType": "0",
     * "merchantId": "008110189990422",
     * "orderNo": "asdhoahsfkasnf",
     * "orderAmount": "100",
     * "orderCurrency": "0",
     * "orderDatetime": "20173525033509",
     * "productName": "商品你名称",
     * "ext1": "<USER>170925304105934</USER>",
     * "payType": "0",
     * "signMsg": "5451748227BA7239380651EA6DD434F1"
     */

    public String inputCharset;
    public String receiveUrl;
    public String version;
    public String signType;
    public String merchantId;
    public String orderNo;
    public String orderAmount;
    public String orderCurrency;
    public String orderDatetime;
    public String productName;
    public String ext1;
    public String payType;
    public String signMsg;

}
