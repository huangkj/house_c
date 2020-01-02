package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/9/4 15:50.
 * 描述：检查用户认筹订单支付状态
 */

public class ResultCustomerIsOrder implements Serializable {
    /**
     * 跳转房源销控（纯企业付费，全额付款订单，退款订单）
     */
    public static final String STATUS_COMPLETE = "0";
    /**
     * 跳转订单详情页
     */
    public static final String STATUS_PAY_PART = "1";
    /**
     * 选择优惠
     */
    public static final String STATUS_NOT = "2";

    public String orderCode;
    public String status;
}
