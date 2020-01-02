package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mars on 2017/6/5 17:04.
 * 描述：单元
 */

public class ModelUnit implements Serializable, StatusManager {
    /**
     * 行数，也是楼层数
     */
    public int row;
    /**
     * 列数，单元内最大户数
     */
    public int column;
    /**
     * 单元名称
     */
    public String unitName;
    /**
     * 单元id
     */
    public String unitId;
    /**
     * 楼栋id
     */
    public String buildId;
    /**
     * 楼栋名称
     */
    public String buildName;

    public String devId;

    /**
     * 房源数据
     */
    public List<ModelHouse> estateRoomBeanList;

    @Override
    public String toString() {
        return "ModelUnit{" +
                "row=" + row +
                ", column=" + column +
                ", unitName='" + unitName + '\'' +
                ", unitId='" + unitId + '\'' +
                ", buildId='" + buildId + '\'' +
                ", devId='" + devId + '\'' +
                ", estateRoomBeanList=" + estateRoomBeanList +
                '}';
    }
}
