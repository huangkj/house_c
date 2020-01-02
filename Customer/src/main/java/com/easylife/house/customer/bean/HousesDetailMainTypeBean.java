package com.easylife.house.customer.bean;

import java.util.List;

/**
 * Created by zgm on 2017/3/21.
 * 主要户型
 */

public class HousesDetailMainTypeBean {

    /**
     * cellName : 101
     * hourseImg : [ { "title" : "1" , "url" : "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489837083866&di=b54c38370abebe67560cefa81f3d3de5&imgtype=0&src=http%3A%2F%2Fwww.sinaimg.cn%2Fdy%2Fslidenews%2F4_img%2F2010_01%2F163_34386_637985.jpg" , "id" : "1"}]
     * id : 3室2厅1卫1厨
     * floor : 1
     * projectID : 6
     * devId : 2
     * mTotalPrice : 1
     * buildingId : 3
     * devsquaremetre : 100
     * unit : 1
     * salesStatus : 0
     */

    public String cellName;
    public List<TypeImageBean> hourseImg;
    public String id;
    public String floor;
    public String projectID;
    public String devId;
    public String mTotalPrice;
    public String buildingId;
    public String fArea;
    public String unit;
    public String salesStatus;




}
