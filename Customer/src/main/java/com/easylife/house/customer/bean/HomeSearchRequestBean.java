package com.easylife.house.customer.bean;

import java.util.List;

public class HomeSearchRequestBean {


    /**
     * cityId : 110100
     * areaId : 110106
     * devName : 公园
     * avgPrice : [{"minAvgPrice":"50000","maxAvgPrice":"80000"}]
     * houseSize : [{"minHouseSize":"20","maxHouseSize":"70"}]
     * propertyType : ["0"]
     * devBedroomInfo : ["一居室"]
     */

    private String cityId;
    private String areaId;
    private String devName;


    private String type;


    private String isTransparent;
    private List<AvgPriceBean> avgPrice;
    private List<HouseSizeBean> houseSize;
    private List<String> propertyType;
    private List<String> devBedroomInfo;

    public String getIsTransparent() {
        return isTransparent;
    }

    public void setIsTransparent(String isTransparent) {
        this.isTransparent = isTransparent;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public List<AvgPriceBean> getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(List<AvgPriceBean> avgPrice) {
        this.avgPrice = avgPrice;
    }

    public List<HouseSizeBean> getHouseSize() {
        return houseSize;
    }

    public void setHouseSize(List<HouseSizeBean> houseSize) {
        this.houseSize = houseSize;
    }

    public List<String> getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(List<String> propertyType) {
        this.propertyType = propertyType;
    }

    public List<String> getDevBedroomInfo() {
        return devBedroomInfo;
    }

    public void setDevBedroomInfo(List<String> devBedroomInfo) {
        this.devBedroomInfo = devBedroomInfo;
    }

    public static class AvgPriceBean {

        public AvgPriceBean() {
        }

        public AvgPriceBean(String minAvgPrice, String maxAvgPrice) {
            this.minAvgPrice = minAvgPrice;
            this.maxAvgPrice = maxAvgPrice;
        }

        /**
         * minAvgPrice : 50000
         * maxAvgPrice : 80000
         */

        private String minAvgPrice;
        private String maxAvgPrice;

        public String getMinAvgPrice() {
            return minAvgPrice;
        }

        public void setMinAvgPrice(String minAvgPrice) {
            this.minAvgPrice = minAvgPrice;
        }

        public String getMaxAvgPrice() {
            return maxAvgPrice;
        }

        public void setMaxAvgPrice(String maxAvgPrice) {
            this.maxAvgPrice = maxAvgPrice;
        }
    }

    public static class HouseSizeBean {

        public HouseSizeBean() {
        }

        public HouseSizeBean(String minHouseSize, String maxHouseSize) {
            this.minHouseSize = minHouseSize;
            this.maxHouseSize = maxHouseSize;
        }


        /**
         * minHouseSize : 20
         * maxHouseSize : 70
         */

        private String minHouseSize;
        private String maxHouseSize;

        public String getMinHouseSize() {
            return minHouseSize;
        }

        public void setMinHouseSize(String minHouseSize) {
            this.minHouseSize = minHouseSize;
        }

        public String getMaxHouseSize() {
            return maxHouseSize;
        }

        public void setMaxHouseSize(String maxHouseSize) {
            this.maxHouseSize = maxHouseSize;
        }
    }
}
