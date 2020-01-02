package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/4/10 17:09.
 * 描述：订单状态记录
 */
public class OrderRecord implements Serializable {
    public String status; // 状态

    public String createTime; //  下单时间
    public String orderNum; // 订单编号

    public String favorable; // 优惠信息
    public String payment; // 待支付-已支付金额

    public String favorablePayHad; // 优惠信息-已支付
    public String paymentHad; // 已支付
    public String paymentNum;//  支付流水号
    public String paymentTime; //  支付时间

    public String completeTime; // 签约时间

    public boolean isShowing = true;
}
