package com.easylife.house.customer.bean;

import java.util.List;

public class BannerListBean {

    /**
     * cityId : 110100
     * list : [{"id":12,"pushTerminal":1,"cityId":"110100","recommendPosition":0,"cityName":"北京市","locationNum":2,"linkAddress":"www.baidu.com1","bannerAddress":"http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr","createTime":1536573580000},{"id":11,"pushTerminal":1,"cityId":"110100","recommendPosition":0,"cityName":"北京市","locationNum":1,"linkAddress":"www.baidu.com","bannerAddress":"http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr","createTime":1536573542000}]
     */

    private String cityId;
    private List<ListBean> list;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 12
         * pushTerminal : 1
         * cityId : 110100
         * recommendPosition : 0
         * cityName : 北京市
         * locationNum : 2
         * linkAddress : www.baidu.com1
         * bannerAddress : http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr
         * createTime : 1536573580000
         */

        private int id;
        private int pushTerminal;
        private String cityId;
        private int recommendPosition;
        private String cityName;
        private int locationNum;
        private String linkAddress;
        private String bannerAddress;
        private long createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPushTerminal() {
            return pushTerminal;
        }

        public void setPushTerminal(int pushTerminal) {
            this.pushTerminal = pushTerminal;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public int getRecommendPosition() {
            return recommendPosition;
        }

        public void setRecommendPosition(int recommendPosition) {
            this.recommendPosition = recommendPosition;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public int getLocationNum() {
            return locationNum;
        }

        public void setLocationNum(int locationNum) {
            this.locationNum = locationNum;
        }

        public String getLinkAddress() {
            return linkAddress;
        }

        public void setLinkAddress(String linkAddress) {
            this.linkAddress = linkAddress;
        }

        public String getBannerAddress() {
            return bannerAddress;
        }

        public void setBannerAddress(String bannerAddress) {
            this.bannerAddress = bannerAddress;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
