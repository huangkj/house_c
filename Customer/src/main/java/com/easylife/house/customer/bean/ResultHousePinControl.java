package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mars on 2017/6/13 13:53.
 * 描述：房源销控接口数据
 */

public class ResultHousePinControl implements Serializable {
    public List<ModelUnit> estateUnitBeanList;
    public List<FloorName> floorList;
    public int hight;
    public int width;
    public String devName;

    public static class FloorName implements Serializable {
        public String floorName;
        public String floor;
    }
}
