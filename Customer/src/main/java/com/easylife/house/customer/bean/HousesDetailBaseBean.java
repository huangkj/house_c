package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zgm on 2017/3/21.
 * 楼盘详情
 */

public class HousesDetailBaseBean implements Serializable {
    /**
     * projectCoordinate : [116.38,39.9]
     * zipCode : 100000
     * devStatus : 1
     * imgCount : 12
     * companyName : 合生
     * projectType : loft
     * addressDetail : 珠江帝景
     * decorateLevel : 装修标准
     * salesStage :
     * beginTime : 1490770376586
     * id : 58db59ca8d6a952b6a2bd0d2
     * propertyFee : 1000
     * devId : 2
     * mainHouse : 三居
     * coPo : 1
     * fixedPhone : 010-110
     * salesNum : 0
     * devName : 珠江帝景A
     * devSquareMetre : 100-120
     * faxNum : 110
     * buildType : 2
     * addressDistrict : 朝阳
     * projectName : 市政府
     * estateProjectId : 2
     * reportTime : 100
     * estateDevBuildHouses : [{"avgprice":"78000","introduce":"nice","buildCode":"123","structure":"三室一厅一卫","devsquaremetre":"80000","houseName":"A02户型","uArea":"0","createTime":1490770376586,"price":"10000","houseImg":[{"id":"1","title":"1","url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489837083866&di=b54c38370abebe67560cefa81f3d3de5&imgtype=0&src=http%3A%2F%2Fwww.sinaimg.cn%2Fdy%2Fslidenews%2F4_img%2F2010_01%2F163_34386_637985.jpg"}],"salesStatus":"0","tag":"南北通透","gArea":"0"},{"avgprice":"78000","introduce":"nice","buildCode":"123","structure":"三室一厅一卫","devsquaremetre":"80000","houseName":"A02户型","uArea":"0","createTime":1490770376586,"price":"10000","houseImg":[{"id":"1","title":"1","url":"http://pic.yesky.com/uploadImages/2016/336/33/69VN0ZT5JG5G.JPG"}],"salesStatus":"0","tag":"南北通透","gArea":"0"}]
     * expires : 产权
     * coOp : 0
     * devCreateTime : 1490770376586
     * lookTime : 1
     * city : 北京
     * devCode : 101-9991
     * examine : 1
     * hotline : 110
     * estateUserId : 3
     * contactPerson : 中国人
     * privilege : 优惠五万
     * salesOfficesCoordinate : [116.38,39.9]
     * distribution : [{"thumbnailImage":"http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg","url":"http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg"}]
     * plotRatio : 1%
     * addressTown : 黄村
     * greenRatio : 1%
     * projectCreateTime : 1490770376585
     * parkRatio : 10%
     * customerRule : 0
     * estatePhone : 10010
     * feature : 不限购
     * estateName : 项目名称1
     * projectCode : 1234567890
     * propertyType : 物业类型
     * liveTime : 1490770376585
     * agreementImg :
     * share : http://easylife.aihsh.cn?devId=2
     * estateBuildingList : []
     * openTime : 1490770376585
     * devBedroom : 一室-两室
     * propertyCompany : 地球自己找
     * contactEmail : 110@11.com
     * salesLicence : 销售许可证
     * houseType : 2
     * saleStatus : 期房
     * showProject : 品牌楼盘
     * projectStatus : 1
     * subway : 一号线
     * averPrice : 17000
     * openingRange : 3# 4#
     * companyAddress : 地球中国北京
     * endTime : 1490770376586
     * contactPhone : 110
     * projectImg :
     */

    /**
     * isTransparent 是否明房源 0否1是
     */
    public String isTransparent;

    //    public String subscription;
//    public String amapid;
//    public String ruleId;
//    public String cooperation;
//    public String minHouseSize;
//    public String zipCode;
//    public int devStatus;
//    public int imgCount;
    public String companyName;
    //    public String projectType;
    public String addressDetail;
    public String decorateLevel;
    //    public String salesStage;
//    public String beginTime;
    public String id;
    public String propertyFee;
    public String devId;
    //    public String mainHouse;
//    public String coPo;
//    public String fixedPhone;
//    public String salesNum;
    public String devName;
    public String devSquareMetre;
    //    public String faxNum;
    public String buildType;
    public String addressDistrict;
    public String projectName;
    public String estateProjectId;
    //    public String reportTime;
    public String expires;
    public int coOp;
    //    public String devCreateTime;
//    public String lookTime;
    public String city;
    public String cityId;
    //    public String devCode;
//    public String examine;
    public String hotline;
    //    public String estateUserId;
//    public String contactPerson;
//    public String privilege;
    public String plotRatio;
    public String minPrice;
    //    public String maxPrice;
    public String saleAddressTown;
    public String saleAddressDistrict;
    public String saleAddressDetail;
    public String addressTown;
    public String greenRatio;
    //    public String projectCreateTime;
    public String parkRatio;
    //    public String customerRule;
    public String estatePhone;
    public String feature;
    //    public String estateName;
//    public String projectCode;
    public String propertyType;
    public String tag;
    public long liveTime;
    //    public String agreementImg;
    public String share;
    public long openTime;
    public String devBedroom;
    public String propertyCompany;
    //    public String contactEmail;
    public String salesLicence;
    //    public String houseType;
    public String saleStatus;
    //    public String showProject;
//    public String projectStatus;
//    public String subway;
    public String averPrice;
    public String openingRange;
    public String liveRange;
    //    public String companyAddress;
//    public long endTime;
    public String contactPhone;
    //    public String projectImg;
    public Dynamics dynamics;
    public List<Double> projectCoordinate;
    public List<HouseSearchBean.EstateDevBuildHousesBean> estateDevBuildHouses;
//    public List<Double> salesOfficesCoordinate;
    //TODO 返回空字符串
    public List<DistributionBean> distribution;
    public List<EffectIdBean> effectId;
    //    public List<VRBean> vR;
    //    public List<SceneBean> scene;
//    public List<MatingBean> mating;
//    public List<RealisticBean> realistic;
//    public List<TrafficBean> traffic;
//    public List<SandTableBean> sandTable;
//    public List<ExteriorBean> exterior;
//    public List<VideoBean> video;
//    public List<HouseBean> house;
//    public List<PrototypeBean> prototype;
//    public String tableId; // 城市所在图层
    public String tableDevId; // 楼盘所在图层
//    private estatePrivilege estatePrivilege;


    public static class EffectIdBean implements Serializable {
        public String thumbnailImage;
        public String url;
    }

//    public static class estatePrivilege implements Serializable {
//        public String favourable;
//        public DevFavorable.Discount discount;
//    }

//    public static class EstateDevBuildHousesBean implements Serializable {
//        /**
//         * avgprice : 78000
//         * introduce : nice
//         * buildCode : 123
//         * structure : 三室一厅一卫
//         * devsquaremetre : 80000
//         * houseName : A02户型
//         * uArea : 0
//         * createTime : 1490770376586
//         * price : 10000
//         * houseImg : [{"id":"1","title":"1","url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489837083866&di=b54c38370abebe67560cefa81f3d3de5&imgtype=0&src=http%3A%2F%2Fwww.sinaimg.cn%2Fdy%2Fslidenews%2F4_img%2F2010_01%2F163_34386_637985.jpg"}]
//         * salesStatus : 0
//         * tag : 南北通透
//         * gArea : 0
//         */
//
//        public String avgprice;
////        public String introduce;
////        public String buildCode;
//        public String structure;
//        public String fArea;
//        public String houseName;
////        public String uArea;
//        public String createTime;
//        public String price;
////        public String salesStatus;
//        public String tag;
////        public String gArea;
//        public List<HouseImgBean> houseImg;
//
//        public static class HouseImgBean implements Serializable {
//            /**
//             * id : 1
//             * title : 1
//             * url : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489837083866&di=b54c38370abebe67560cefa81f3d3de5&imgtype=0&src=http%3A%2F%2Fwww.sinaimg.cn%2Fdy%2Fslidenews%2F4_img%2F2010_01%2F163_34386_637985.jpg
//             */
//
//            public String id;
//            public String title;
//            public String url;
//        }
//    }

    public static class DistributionBean implements Serializable {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;


        public DistributionBean() {
        }

        public DistributionBean(String url) {
            this.url = url;
        }
    }

//    public static class SceneBean {
//        /**
//         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
//         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
//         */
//
//        public String thumbnailImage;
//        public String url;
//    }

    public static class MatingBean {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;
    }

    public static class RealisticBean {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;
    }

    public static class VRBean {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;
    }

    public static class TrafficBean {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;
    }

    public static class SandTableBean {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;
    }

    public static class ExteriorBean {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;
    }

    public static class VideoBean {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;
    }

    public static class HouseBean {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;
    }

    public static class EstateBuildingListBean {
        /**
         * cellNum : 1
         * unitName : 1号楼
         * opera : 录入人
         * unitNum : 2
         * buildId : 4
         * buildDate : 2017
         * infoFrom : 项目来源
         * buildCode : 123
         * floorNum : 3
         * decorateLevel : 1
         * createTime : 2017-03-29T09:10:39.815000
         * buildType : 底层
         * buildingStatus : 1
         */

//        public String cellNum;
        public String buildName;
        //        public String opera;
//        public String unitNum;
        public String buildId;
        //        public String buildDate;
//        public String infoFrom;
//        public String buildCode;
//        public String floorNum;
//        public String decorateLevel;
        public String createTime;
//        public String buildType;
//        public String buildingStatus;
    }

    public static class PrototypeBean {
        /**
         * thumbnailImage : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         * url : http://b.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c224a6a42b0cf41bd5ad6e392c.jpg
         */

        public String thumbnailImage;
        public String url;
    }

}
