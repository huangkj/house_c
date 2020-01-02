package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mars on 2017/3/27 19:31.
 * 描述：
 */

public class HouseColletion implements Serializable {
    /**
     * "severalRoom": "三居室",
     * "sum": "1",
     * "houseName": "A01户型",
     * "hourseImg": [
     * {
     * "id": "1",
     * "title": "1",
     * "url": "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489837083866&di=b54c38370abebe67560cefa81f3d3de5&imgtype=0&src=http%3A%2F%2Fwww.sinaimg.cn%2Fdy%2Fslidenews%2F4_img%2F2010_01%2F163_34386_637985.jpg"
     * }
     * ],
     * "id": "A01户型",
     * "tag": "不要钱",
     * "mTotalPrice": "1",
     * "devsquaremetre": "100",
     * "salesStatus": "0"//0在售，1可售,2待售，3已售完，4不可售，5未知
     */
    public String severalRoom;
    public String sum;
    public String devId;
    public String projectID;
    public String houseName;
    public String houseCount;// 当前户型房源数量
    public String structure;
    public String id;
    public String tag;
    public String mTotalPrice;
    public String introduce;
    public String fArea;
    public String salesStatus;// 0在售，1可售,2待售，3已售完，4不可售，5未知
    public String avgprice;
    public String devName;
    public String averPrice;  // 楼盘均价
    public String addressDistrict;  // 区
    public String addressTown;  // 镇
    public String propertyType; // 物业类型
    public String houseId;
    public List<TypeImageBean> houseImg;


    @Override
    public String toString() {
        return "HouseColletion{" +
                "severalRoom='" + severalRoom + '\'' +
                ", sum='" + sum + '\'' +
                ", devId='" + devId + '\'' +
                ", projectID='" + projectID + '\'' +
                ", houseName='" + houseName + '\'' +
                ", structure='" + structure + '\'' +
                ", id='" + id + '\'' +
                ", tag='" + tag + '\'' +
                ", mTotalPrice='" + mTotalPrice + '\'' +
                ", introduce='" + introduce + '\'' +
                ", fArea='" + fArea + '\'' +
                ", salesStatus='" + salesStatus + '\'' +
                ", avgprice='" + avgprice + '\'' +
                ", devName='" + devName + '\'' +
                ", averPrice='" + averPrice + '\'' +
                ", addressDistrict='" + addressDistrict + '\'' +
                ", addressTown='" + addressTown + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", houseImg=" + houseImg +
                '}';
    }
}
