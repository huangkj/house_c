package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/4/1 14:06.
 * 描述：区域楼盘数量统计返回结果
 */

public class AreaHousesNum implements Serializable {
    public String name;
    public String count;

    public AreaHousesNum(String areaName, String number) {
        this.name = areaName;
        this.count = number;
    }

}

