package com.easylife.house.customer.bean;

import java.util.List;

public class FiltrateHouseBean {

    /**
     * total : 7
     * list : [{"devId":"1315","area":"丰台区","devSquareMetre":"47.0-68.0","averPrice":60000,"feature":"地铁房,宜居","houseType":"普通住宅","isTransparent":1,"devName":"公园懿府","url":"http://kfcdn.lifeat.cn/Fnnvy1SEYiFu0CwIUWajJ7PRgXhM"},{"devId":"306","area":"通州区","devSquareMetre":"100.0-555.0","averPrice":0,"feature":"地铁房,投资","houseType":"商住楼","isTransparent":0,"devName":"合生麒麟公馆","url":"http://cdn.lifeat.cn/Forv8LH8KT3EYNCY_XeAgUBiAlKh"},{"devId":"17","area":"朝阳区","devSquareMetre":"0.5-888.88","averPrice":1578391,"feature":"地铁房,学区房,小户型,宜居","houseType":"普通住宅","isTransparent":1,"devName":"合生霄云路8号","url":"http://cdn.lifeat.cn/FhmHD1eMuDqSm5iGK-xHWrkORh1x"},{"devId":"10280","area":"朝阳区","devSquareMetre":"83.4-114.47","averPrice":95000,"feature":"投资,宜居","houseType":"普通住宅","isTransparent":1,"devName":"测试楼盘（龚总演示专用）","url":"http://kfcdn.lifeat.cn/FmN7T3tgDrPyJfPwUQvwBMp8_TD2"},{"devId":"10072","area":"朝阳区","devSquareMetre":"89.0-120.0","averPrice":1.2345678E7,"feature":"地铁房,学区房,现房","houseType":"普通住宅","isTransparent":"","devName":"关灯锁门洋楼2栋","url":""},{"devId":"10273","area":"西城区","devSquareMetre":"100.0","averPrice":0,"feature":"","houseType":"","isTransparent":"","devName":"杨氏小楼盘","url":""},{"devId":"1325","area":"朝阳区","devSquareMetre":"70.55-100.0","averPrice":50000,"feature":"地铁房,学区房,现房,小户型","houseType":"普通住宅","isTransparent":"","devName":"大郊亭金茂府","url":""}]
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * devId : 1315
         * area : 丰台区
         * devSquareMetre : 47.0-68.0
         * averPrice : 60000.0
         * feature : 地铁房,宜居
         * houseType : 普通住宅
         * isTransparent : 1
         * devName : 公园懿府
         * url : http://kfcdn.lifeat.cn/Fnnvy1SEYiFu0CwIUWajJ7PRgXhM
         */

        private String devId;
        private String area;
        public String devSquareMetre;
        public String averPrice;
        private String feature;
        private String houseType;
        private String isTransparent;
        private String devName;
        private String url;
        public boolean isCompare;

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getDevSquareMetre() {
            return devSquareMetre;
        }

        public void setDevSquareMetre(String devSquareMetre) {
            this.devSquareMetre = devSquareMetre;
        }

        public String getAverPrice() {
            return averPrice;
        }

        public void setAverPrice(String averPrice) {
            this.averPrice = averPrice;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getHouseType() {
            return houseType;
        }

        public void setHouseType(String houseType) {
            this.houseType = houseType;
        }

        public String getIsTransparent() {
            return isTransparent;
        }

        public void setIsTransparent(String isTransparent) {
            this.isTransparent = isTransparent;
        }

        public String getDevName() {
            return devName;
        }

        public void setDevName(String devName) {
            this.devName = devName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
