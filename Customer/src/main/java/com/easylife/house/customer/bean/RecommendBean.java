package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * 我的推荐
 */
public class RecommendBean implements Serializable {

    /**
     * devId : 1496
     * followTime : 1537428105000
     * followStatus : 0
     * followType : 1
     * assistant : 13121535150
     * followStatusName : 已过期
     * brokerCustomerId : 4848
     * nameAndPhone : 笔墨(136****5432)
     * followTypeName : 报备
     * devName : 东方雅苑会员
     */

    public String devId;
    public String followTime;
    public int followStatus; // 过期状态枚举
    public String followStatusName; // 过期状态
    public String assistant; // 管家电话
    public String brokerCustomerId;
    public String nameAndPhone;
    public String followType; // 跟进状态枚举
    public String followTypeName; // 跟进状态
    public String devName;
}
