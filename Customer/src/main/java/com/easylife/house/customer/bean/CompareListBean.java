package com.easylife.house.customer.bean;

public class CompareListBean {

    /**
     * propertyCompany : 434
     * expires :
     * address : 朝阳区百环大厦百环大厦
     * companyName : 新的公司1
     * devName : test1下楼盘
     * plotRatio : 1.20
     * devSquareMetre : 45.0-50.0
     * parkRatio : 1.2
     * decorateLevel :
     * averPrice : 0.0
     * buildType :
     * propertyType :
     * liveTime : 1556812800
     * propertyFee : 1.23
     * openTime : 1556640000
     */

    private String propertyCompany;
    private String expires;
    private String address;
    private String companyName;
    private String devName;
    private String plotRatio;
    private String devSquareMetre;
    private String parkRatio;
    private String decorateLevel;
    private String averPrice;
    private String buildType;
    private String propertyType;
    private long liveTime;
    private String propertyFee;
    private long openTime;
    /**
     * 是否显示一行的头
     */
    private boolean isHeader = true;

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getPropertyCompany() {
        return propertyCompany;
    }

    public void setPropertyCompany(String propertyCompany) {
        this.propertyCompany = propertyCompany;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getPlotRatio() {
        return plotRatio;
    }

    public void setPlotRatio(String plotRatio) {
        this.plotRatio = plotRatio;
    }

    public String getDevSquareMetre() {
        return devSquareMetre;
    }

    public void setDevSquareMetre(String devSquareMetre) {
        this.devSquareMetre = devSquareMetre;
    }

    public String getParkRatio() {
        return parkRatio;
    }

    public void setParkRatio(String parkRatio) {
        this.parkRatio = parkRatio;
    }

    public String getDecorateLevel() {
        return decorateLevel;
    }

    public void setDecorateLevel(String decorateLevel) {
        this.decorateLevel = decorateLevel;
    }

    public String getAverPrice() {
        return averPrice;
    }

    public void setAverPrice(String averPrice) {
        this.averPrice = averPrice;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public long getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(long liveTime) {
        this.liveTime = liveTime;
    }

    public String getPropertyFee() {
        return propertyFee;
    }

    public void setPropertyFee(String propertyFee) {
        this.propertyFee = propertyFee;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }
}
