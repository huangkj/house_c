package com.easylife.house.customer.bean;

import java.util.List;

public class ShopRentRequestBean {

    /**
     * cityId : 652900
     * areaId : 652901
     * tradingCode : 652901001
     * areaMix : 1
     * areaMax : 99999
     * rentMix : 1
     * rentMax : 99999
     * type : ["1","2"]
     * decorate : ["1","2"]
     */

    private String cityId;
    private String areaId;
    private String tradingCode;
    private String areaMix;
    private String areaMax;
    private String rentMix;
    private String rentMax;
    private List<String> type;
    private List<String> decorate;

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

    public String getTradingCode() {
        return tradingCode;
    }

    public void setTradingCode(String tradingCode) {
        this.tradingCode = tradingCode;
    }

    public String getAreaMix() {
        return areaMix;
    }

    public void setAreaMix(String areaMix) {
        this.areaMix = areaMix;
    }

    public String getAreaMax() {
        return areaMax;
    }

    public void setAreaMax(String areaMax) {
        this.areaMax = areaMax;
    }

    public String getRentMix() {
        return rentMix;
    }

    public void setRentMix(String rentMix) {
        this.rentMix = rentMix;
    }

    public String getRentMax() {
        return rentMax;
    }

    public void setRentMax(String rentMax) {
        this.rentMax = rentMax;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getDecorate() {
        return decorate;
    }

    public void setDecorate(List<String> decorate) {
        this.decorate = decorate;
    }
}
