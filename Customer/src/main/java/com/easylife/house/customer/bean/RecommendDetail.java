package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的推荐详情
 */
public class RecommendDetail implements Serializable {


    /**
     * devId : 1588
     * customerFollow : [{"createTime":1538214117518,"followType":1,"id":"5baf48e5e4b053e8e1e0aba6","check":"成功","followTypeName":"报备"}]
     * assistant : 13121535150
     * nameAndPhone : 小乔(133****7946)
     * devName : 东北大家园
     */

    public int devId;
    public String assistant;
    public String nameAndPhone;
    public String devName;
    public List<RecommendRateBean> customerFollow;

}
