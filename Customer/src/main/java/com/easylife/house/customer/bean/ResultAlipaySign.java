package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/7/31 16:37.
 * 描述：支付宝签名回调信息
 */

public class ResultAlipaySign implements Serializable {
    public String Result;

    @Override
    public String toString() {
        return "ResultAlipaySign{" +
                "Result='" + Result + '\'' +
                '}';
    }
}
