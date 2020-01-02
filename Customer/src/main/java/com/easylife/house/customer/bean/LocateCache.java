package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/3/31 17:54.
 * 描述：定位缓存
 */

public class LocateCache implements Serializable {
    public double lat;
    public double lon;
    public String city;
    public String cityId;

    @Override
    public String toString() {
        return "LocateCache{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", city='" + city + '\'' +
                ", cityId='" + cityId + '\'' +
                '}';
    }
}
