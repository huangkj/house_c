package com.easylife.house.customer.bean;

import java.util.List;

public class HomeNewsBean {

    /**
     * cityId : 110100
     * list : [{"type":1,"locationNum":1,"pictureAddress":"http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr","titleOrLink":"www.baidu.com"},{"type":1,"locationNum":2,"pictureAddress":"http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr","titleOrLink":"www.baidu.com1"}]
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
         * type : 1
         * locationNum : 1
         * pictureAddress : http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr
         * titleOrLink : www.baidu.com
         */

        private int type;
        private int locationNum;
        private String pictureAddress;

        public int getLocalPicture() {
            return localPicture;
        }

        public void setLocalPicture(int localPicture) {
            this.localPicture = localPicture;
        }

        private int localPicture;
        private String titleOrLink;
        private String topTitle;

        public String getTopTitle() {
            return topTitle;
        }

        public void setTopTitle(String topTitle) {
            this.topTitle = topTitle;
        }


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getLocationNum() {
            return locationNum;
        }

        public void setLocationNum(int locationNum) {
            this.locationNum = locationNum;
        }

        public String getPictureAddress() {
            return pictureAddress;
        }

        public void setPictureAddress(String pictureAddress) {
            this.pictureAddress = pictureAddress;
        }

        public String getTitleOrLink() {
            return titleOrLink;
        }

        public void setTitleOrLink(String titleOrLink) {
            this.titleOrLink = titleOrLink;
        }
    }
}
