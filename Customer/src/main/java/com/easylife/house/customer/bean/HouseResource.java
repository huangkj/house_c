package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mars on 2017/6/15 18:58.
 * 描述：房源详情
 */

public class HouseResource implements Serializable {
    /**
     * "devId": "97",
     * "devName": " 影姐测试专用",
     * "houseImg": [],
     * "structure": "0室0厅0卫0厨",
     * "mUnitPrice": "14000",
     * "mTotalPrice": "1900000",
     * "fArea": "150",
     * "toward": "1",
     * "buildName": "A栋",
     * "unitName": "一单元",
     * "floor": "25",
     * "uArea": "140",
     * "unitNum": "2",
     * "floorNum": "32",
     * "introduce": "",
     * "cellName": "2501",
     * "address": "1",
     * "buildType": "高层",
     * "cityId": "110100",
     * "cityName": "北京市",
     * "buildId": "1",
     * "projectName": " 影姐测试专用",
     * "addressDetail": "1小店1",
     * "distribution": "",
     * "sroh": "1：2户"
     */

    public String devId;
    public String devName;
    public String cellName;
    public String buildName;
    public String fArea;
    public String floor;
    public String buildId;
    public String cityId;
    public String cityName;
    public String floorNum;
    public String mTotalPrice;
    public String mUnitPrice;
    public String sROH;
    public String structure;
    public String propertyType;
    public String addressTown;
    public String addressDistrict;
    public String toward;
    public String uArea;
    public String unitName;
    public String floorName;
    public String unitId;
    public String unitNum;
    public String address;
    public String buildType;
    public String introduce;
    public String addressDetail;
    public String projectName;
    public List<HousesDetailBaseBean.DistributionBean> houseImg;
}
