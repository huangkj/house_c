package com.easylife.house.customer.bean;

import java.util.List;

public class HotDevBean {

    /**
     * devId : 10072
     * expires : 70年
     * coOp : 1
     * cityId : 110100
     * devName : 关灯锁门洋楼2栋
     * showProject : 特别推荐,教育楼盘
     * devSquareMetre : 60-150
     * effectId : []
     * decorateLevel : 精装修
     * feature : 地铁房,学区房,现房
     * averPrice : 0
     * propertyType : 住宅,公寓
     * isTransparent :
     * addressDistrict : 朝阳区
     * openTime : 1545753600
     * devBedroom : 一居室 - 三居室
     * buildType : 小高层
     */

    public String devId;
    public String expires;
    public int coOp;
    public String cityId;
    public String devName;
    public String showProject;
    public String devSquareMetre;
    public String decorateLevel;
    public String feature;
    public String averPrice;
    public String propertyType;
    public String isTransparent;
    public String addressDistrict;
    public int openTime;
    public String devBedroom;
    public String buildType;
    public List<HomeSearchResponseBean.EffectIdBean> effectId;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public int getCoOp() {
        return coOp;
    }

    public void setCoOp(int coOp) {
        this.coOp = coOp;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getShowProject() {
        return showProject;
    }

    public void setShowProject(String showProject) {
        this.showProject = showProject;
    }

    public String getDevSquareMetre() {
        return devSquareMetre;
    }

    public void setDevSquareMetre(String devSquareMetre) {
        this.devSquareMetre = devSquareMetre;
    }

    public String getDecorateLevel() {
        return decorateLevel;
    }

    public void setDecorateLevel(String decorateLevel) {
        this.decorateLevel = decorateLevel;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getAverPrice() {
        return averPrice;
    }

    public void setAverPrice(String averPrice) {
        this.averPrice = averPrice;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getIsTransparent() {
        return isTransparent;
    }

    public void setIsTransparent(String isTransparent) {
        this.isTransparent = isTransparent;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public String getDevBedroom() {
        return devBedroom;
    }

    public void setDevBedroom(String devBedroom) {
        this.devBedroom = devBedroom;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public List<HomeSearchResponseBean.EffectIdBean> getEffectId() {
        return effectId;
    }

    public void setEffectId(List<HomeSearchResponseBean.EffectIdBean> effectId) {
        this.effectId = effectId;
    }
}
