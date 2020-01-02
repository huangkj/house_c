package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zgm on 2017/3/28.
 * 户型列表model
 */

public class HousesTypeBean implements Serializable{


    /**
     * name : 三居
     * houseLayoutData : [{"avgprice":"78000","introduce":"nice","buildCode":"123","devName":"珠江帝景A","structure":"三室一厅一卫","devsquaremetre":"80000","houseName":"A02户型","uArea":"0","createTime":"2017-03-29 14:52:56","price":"10000","houseImg":[{"id":"1","title":"1","url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489837083866&di=b54c38370abebe67560cefa81f3d3de5&imgtype=0&src=http%3A%2F%2Fwww.sinaimg.cn%2Fdy%2Fslidenews%2F4_img%2F2010_01%2F163_34386_637985.jpg"}],"salesStatus":"0","tag":"南北通透","gArea":"0"},{"avgprice":"78000","introduce":"nice","buildCode":"123","devName":"珠江帝景A","structure":"三室一厅一卫","devsquaremetre":"80000","houseName":"A02户型","uArea":"0","createTime":"2017-03-29 14:52:56","price":"10000","houseImg":[{"id":"1","title":"1","url":"http://pic.yesky.com/uploadImages/2016/336/33/69VN0ZT5JG5G.JPG"}],"salesStatus":"0","tag":"南北通透","gArea":"0"}]
     */

    public String name;
    public List<HouseLayoutDataBean> houseLayoutData;

    public static class HouseLayoutDataBean implements Serializable{
        /**
         * avgprice : 78000
         * introduce : nice
         * buildCode : 123
         * devName : 珠江帝景A
         * structure : 三室一厅一卫
         * devsquaremetre : 80000
         * houseName : A02户型
         * uArea : 0
         * createTime : 2017-03-29 14:52:56
         * price : 10000
         * houseImg : [{"id":"1","title":"1","url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489837083866&di=b54c38370abebe67560cefa81f3d3de5&imgtype=0&src=http%3A%2F%2Fwww.sinaimg.cn%2Fdy%2Fslidenews%2F4_img%2F2010_01%2F163_34386_637985.jpg"}]
         * salesStatus : 0
         * tag : 南北通透
         * gArea : 0
         */

        public String avgprice;
        public String introduce;
        public String buildCode;
        public String devName;
        public String structure;
        public String fArea;
        public String houseName;
        public String devId;
        public String houseShare;
        public String uArea;
        public String createTime;
        public String price;
        public String salesStatus;
        public String tag;
        public String gArea;
        public String projectID;
        public String saleNum;
        public String houseId;
        public String isTransparent;

        public List<TypeImageBean> houseImg;

    }
}
