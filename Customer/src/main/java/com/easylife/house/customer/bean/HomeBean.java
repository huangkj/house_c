package com.easylife.house.customer.bean;

import java.util.Comparator;

/**
 * Created by zgm on 2017/3/16.
 * 首页
 */

public class HomeBean implements Comparator<HomeBean>{
    /**
     * imgUrl : http://b.hiphotos.baidu.com/image/pic/item/0823dd54564e925838c205c89982d158ccbf4e26.jpg
     * devId : 1
     * price : 99999
     * Size : 60-150
     * position : A1
     * projectName : 美女A1
     */

    public String imgUrl;
    public String devId;
    public String price;
    public String Size;
    public String position;
    public String projectName;
    public String devName;
    public int coOp;

    @Override
    public int compare(HomeBean o1, HomeBean o2) {
        return o1.position.compareTo(o2.position);
    }
}
