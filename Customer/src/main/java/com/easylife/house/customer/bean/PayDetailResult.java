package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mars on 2017/7/4 14:55.
 * 描述：支付明细接口回调结果
 */

public class PayDetailResult implements Serializable {
    /**
     * "data": [
     * {
     * "serialNumber": "akljshflasnfasfln",
     * "proccessFollowStatus": "PLACE",
     * "money": 52.2,
     * "createTime": "1970年01月18日 16:04:16"
     * }
     * ],
     * "orderType": 0,
     * "name": "0512三端楼盘房源楼盘名称会员服务费",
     * "paid": 5
     */

    public String orderCode;
    public int orderType;
    public String name;
    public String paid;
    public List<PayDetail> data;
}
