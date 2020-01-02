package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/6/29 20:00.
 * 描述：会员优惠信息-认筹
 */

public class FavorableVip implements Serializable {
    /**
     * "id": 41,
     * "scope": "三居",
     * "num": 50000,
     * "privilege": "五万低十万",
     * "beginTime": "2017-05-09",
     * "endTime": "2017-05-31",
     * "operator": "李娜",
     * "createTime": "2017-05-09 19:17:38",
     * "status": "1",
     * "estateProjectDevId": 599
     */
    public String id;
    public String scope;
    public double num;
    public String privilege;
    public String beginTime;
    public String endTime;
    public String operator;
    public String createTime;
    public String status;
    public String estateProjectDevId;
}
