package com.easylife.house.customer.bean;

import java.util.List;

public class HomeSearchResponseBean {

    /**
     * devId : 1538
     * devBedroom : 一居室 - 二居室
     * expires : 70年
     * devName : 阳光星苑小区
     * showProject : 品牌地产,合生专场,珠江专场
     * devSquareMetre : 50.00-110.00
     * effectId : [{"fileName":"效果1","thumbnailImage":"http://om4yv9x56.bkt.clouddn.com/Flr5KpAjch9h966dBusnbSWZsFCD","url":"http://om4yv9x56.bkt.clouddn.com/Flr5KpAjch9h966dBusnbSWZsFCD"}]
     * decorateLevel : 简装
     * feature : 地铁房,宜居
     * averPrice : 60000.0
     * buildType : 小高层
     * propertyType : 住宅
     * isTransparent :
     * openTime : 1537459200
     */

    public String devId;
    public String devBedroom;
    public String expires;
    public String devName;
    public String showProject;
    public String devSquareMetre;
    public String decorateLevel;
    public String feature;
    public String averPrice;
    public String buildType;
    public String propertyType;
    public String isTransparent;
    public String addressDistrict;
    public long openTime;
    public int coOp;
    public List<EffectIdBean> effectId;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevBedroom() {
        return devBedroom;
    }

    public void setDevBedroom(String devBedroom) {
        this.devBedroom = devBedroom;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
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

    public String getIsTransparent() {
        return isTransparent;
    }

    public void setIsTransparent(String isTransparent) {
        this.isTransparent = isTransparent;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public List<EffectIdBean> getEffectId() {
        return effectId;
    }

    public void setEffectId(List<EffectIdBean> effectId) {
        this.effectId = effectId;
    }

    public static class EffectIdBean {
        /**
         * fileName : 效果1
         * thumbnailImage : http://om4yv9x56.bkt.clouddn.com/Flr5KpAjch9h966dBusnbSWZsFCD
         * url : http://om4yv9x56.bkt.clouddn.com/Flr5KpAjch9h966dBusnbSWZsFCD
         */

        private String fileName;
        private String thumbnailImage;
        private String url;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getThumbnailImage() {
            return thumbnailImage;
        }

        public void setThumbnailImage(String thumbnailImage) {
            this.thumbnailImage = thumbnailImage;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
