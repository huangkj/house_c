package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/6/6 14:04.
 * 描述：房源
 */

public class ModelHouse implements Serializable, StatusManager {
    /**
     * 楼层位置 - 3
     */
    public int rowPosition;
    /**
     * 楼层内房间号 - 3
     */
    public int columnPosition;
    public String houseId;
    /**
     * 房源id
     */
    public String id;
    /**
     * 单元ID
     */
    public String unitId;
    public String cellName;
    public String floor;
    public String floorName;
    public String buildId;
    public String buildName;
    public String unitName;
    public String fArea;
    public String mTotalPrice;
    public String structure;
    /**
     * 房源状态
     */
    public String state = STATUS_NULL + "";


    @Override
    public String toString() {
        return "ModelHouse{" +
                "rowPosition=" + rowPosition +
                ", columnPosition=" + columnPosition +
                ", houseId='" + houseId + '\'' +
                ", id='" + id + '\'' +
                ", unitId='" + unitId + '\'' +
                ", cellName='" + cellName + '\'' +
                ", floor='" + floor + '\'' +
                ", floorName='" + floorName + '\'' +
                ", buildId='" + buildId + '\'' +
                ", buildName='" + buildName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", fArea='" + fArea + '\'' +
                ", mTotalPrice='" + mTotalPrice + '\'' +
                ", structure='" + structure + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
