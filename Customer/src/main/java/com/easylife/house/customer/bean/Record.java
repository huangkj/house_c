package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/10/19 17:24.
 * 描述：客户跟进
 */

public class Record implements Serializable {
    public String phone;
    public String id;
    public String devId;
    public String devName;
    public String name;
    public String city;
    public String rate;


    public String recommendDate;
    public String arrivedData;
    public String arrivedLocation;
    public String paidHouseNo;
    public String paidDate;
    public String signedDate;
}
