package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/9/11 17:53.
 * 描述： 退款信息上传
 */

public class ParamRefundInfo implements Serializable {

    public String userName;
    public String cardNum;
    public String bankAddress;
    public String bankNum;
    public String linkNumber;
    public String comments;
    public String phone;
    public String orderCode;
    public String[] img;
}
