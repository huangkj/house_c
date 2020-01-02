package com.easylife.house.customer.bean;

import java.util.ArrayList;
import java.util.List;

public class HeadLineBean {


    /**
     * cityId : 110100
     * list : [{"id":"5b9658d4e4b01eef8fda3e20","title":"0910001","text":"","logo":"","logoUrl":"","url":"","createTime":1536579796,"listLog":"","tag":"","role":"","delStatus":"","pushPer":"","type":"","checkCount":"","cityCode":"","policyType":"","terminal":"","headline":"","sortType":"","count":0},{"id":"5b965047e4b049381579c691","title":"0910001","text":"","logo":"","logoUrl":"","url":"","createTime":1536577607,"listLog":"","tag":"","role":"","delStatus":"","pushPer":"","type":"","checkCount":"","cityCode":"","policyType":"","terminal":"","headline":"","sortType":"","count":0}]
     */

    private String cityId;
    private ArrayList<ListBean> list;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public ArrayList<ListBean> getList() {
        return list;
    }

    public void setList(ArrayList<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 5b9658d4e4b01eef8fda3e20
         * title : 0910001
         * text :
         * logo :
         * logoUrl :
         * url :
         * createTime : 1536579796
         * listLog :
         * tag :
         * role :
         * delStatus :
         * pushPer :
         * type :
         * checkCount :
         * cityCode :
         * policyType :
         * terminal :
         * headline :
         * sortType :
         * count : 0
         */

        private String id;
        private String title;
        private String text;
        private String logo;
        private String logoUrl;
        private String url;
        private int createTime;
        private String listLog;
        private String tag;
        private String role;
        private String delStatus;
        private String pushPer;
        private String type;
        private String checkCount;
        private String cityCode;
        private String policyType;
        private String terminal;
        private String headline;
        private String sortType;
        private int count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public String getListLog() {
            return listLog;
        }

        public void setListLog(String listLog) {
            this.listLog = listLog;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getDelStatus() {
            return delStatus;
        }

        public void setDelStatus(String delStatus) {
            this.delStatus = delStatus;
        }

        public String getPushPer() {
            return pushPer;
        }

        public void setPushPer(String pushPer) {
            this.pushPer = pushPer;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCheckCount() {
            return checkCount;
        }

        public void setCheckCount(String checkCount) {
            this.checkCount = checkCount;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getPolicyType() {
            return policyType;
        }

        public void setPolicyType(String policyType) {
            this.policyType = policyType;
        }

        public String getTerminal() {
            return terminal;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public String getSortType() {
            return sortType;
        }

        public void setSortType(String sortType) {
            this.sortType = sortType;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
