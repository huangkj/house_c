package com.easylife.house.customer.bean;

import java.util.List;


public class TopMessageBean {

    /**
     * cityId : 110100
     * list : [{"id":"5b9f4b97e4b0c2b7165f3efd","title":"0917004","count":0,"createTime":1537166231,"pictureUrl":["http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr"]},{"id":"5b9f4ac9e4b0c2b7165f3efc","title":"0917003","count":0,"createTime":1537166025,"pictureUrl":["http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr"]},{"id":"5b9f4a61e4b0c2b7165f3efb","title":"0917002","count":0,"createTime":1537165921,"pictureUrl":["http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr"]},{"id":"5b9f49f9e4b0c2b7165f3efa","title":"0917001","count":0,"createTime":1537165817,"pictureUrl":["http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr","http://om4yv9x56.bkt.clouddn.com/Fj4NUz-QAbLDaL1-SG-HrHohLYq4"]}]
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
         * id : 5b9f4b97e4b0c2b7165f3efd
         * title : 0917004
         * count : 0
         * createTime : 1537166231
         * pictureUrl : ["http://om4yv9x56.bkt.clouddn.com/FiK6tCDWN0EwHlqZEdCSC31kAtKr"]
         */

        private String id;
        private String title;
        private int count;
        private String createTime;
        private List<String> pictureUrl;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        private String text;

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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public List<String> getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(List<String> pictureUrl) {
            this.pictureUrl = pictureUrl;
        }
    }
}
