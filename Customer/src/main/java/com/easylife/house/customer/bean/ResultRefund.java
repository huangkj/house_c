package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/9/11 17:00.
 * 描述：提交申请退款的接口回调结果
 */

public class ResultRefund implements Serializable {

    /**
     * "isPurchase" :true,(是否认购)
     * "isOnline" :true,(是否线上)
     * "isOvertime" :true(是否超期)
     */

    public boolean isPurchase;
    public boolean isOnline;
    public boolean isOvertime;

}
