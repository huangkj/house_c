package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/7/4 14:55.
 * 描述：支付明细列表
 */

public class PayDetail implements Serializable {

    /**
     * "msg": "支付成功",
     * "serialNumber": "4000742001201707262760379286",
     * "money": 10000,
     * "createTime": "2017年07月26日 14:47:44"
     */
    public String serialNumber;
    public String msg;
    public String money;
    public String createTime;
}
