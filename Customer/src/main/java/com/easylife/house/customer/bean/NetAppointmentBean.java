package com.easylife.house.customer.bean;

import java.util.List;

/**
 * Created by zgm on 2017/3/30.
 */

public class NetAppointmentBean {


    /**
     * dev : {"devStatus":0,"imgCount":0,"saleAddressTown":"黄村","id":"2","devName":"珠江帝景A","devSquareMetre":"100-120","addressDistrict":"朝阳","estateProjectId":2,"coop":0,"estateUserId":0,"addressTown":"黄村","effectId":[{"thumbnailImage":"http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg","url":"http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg"}],"propertyType":"物业类型","devBedroom":"一室-两室","saleAddressDetail":"珠江帝景","saleAddressDistrict":"朝阳","averPrice":"17000"}
     * broker : {"brokerCode":"a748187f-f858-11e6-8472-1866dae6a5fc","headImg":"http://om4yv9x56.bkt.clouddn.com/FlqDdCdVg5Nl1yEDZqr_HZKCZw0u","phone":"13693186350","name":"张宇","count":21,"favorableRate":"98"}
     * look : {"lookTime":1490581929000,"status":0}
     */

    public DevBean dev;
    public BrokerBean broker;
    public LookBean look;

    public static class DevBean {
        /**
         * devStatus : 0
         * imgCount : 0
         * saleAddressTown : 黄村
         * id : 2
         * devName : 珠江帝景A
         * devSquareMetre : 100-120
         * addressDistrict : 朝阳
         * estateProjectId : 2
         * coOp : 0
         * estateUserId : 0
         * addressTown : 黄村
         * effectId : [{"thumbnailImage":"http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg","url":"http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg"}]
         * propertyType : 物业类型
         * devBedroom : 一室-两室
         * saleAddressDetail : 珠江帝景
         * saleAddressDistrict : 朝阳
         * averPrice : 17000
         */

        public int devStatus;
        public int imgCount;
        public String saleAddressTown;
        public String id;
        public String devName;
        public String devSquareMetre;
        public String addressDistrict;
        public int estateProjectId;
        public int coOp;
        public int estateUserId;
        public String addressTown;
        public String addressDetail;
        public String propertyType;
        public String devBedroom;
        public String saleAddressDetail;
        public String saleAddressDistrict;
        public String averPrice;
        public List<EffectIdBean> effectId;

        public static class EffectIdBean {
            /**
             * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
             * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
             */

            public String thumbnailImage;
            public String url;
        }
    }

    public static class BrokerBean {
        /**
         * brokerCode : a748187f-f858-11e6-8472-1866dae6a5fc
         * headImg : http://om4yv9x56.bkt.clouddn.com/FlqDdCdVg5Nl1yEDZqr_HZKCZw0u
         * phone : 13693186350
         * name : 张宇
         * count : 21
         * favorableRate : 98
         */

        public String brokerCode;
        public String headImg;
        public String phone;
        public String name;
        public int count;
        public String favorableRate;
    }

    public static class LookBean {
        /**
         * lookTime : 1490581929000
         * status : 0
         */
        public String purchaseTime;
        public String signTime;
        public String arriveTime;
        public String lookTime;
        public int status;
    }
}
