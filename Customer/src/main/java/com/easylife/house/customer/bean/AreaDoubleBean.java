package com.easylife.house.customer.bean;

import java.util.List;

public class AreaDoubleBean {

    /**
     * areaId : 110105
     * areaName : 朝阳区
     * list : [{"tradingName":"奥林匹克公园","tradingCode":"110105001"},{"tradingName":"安贞","tradingCode":"110105002"},{"tradingName":"北苑","tradingCode":"110105003"}]
     */

    private String areaId;
    private String areaName;
    private List<ListBean> list;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * tradingName : 奥林匹克公园
         * tradingCode : 110105001
         */

        private String tradingName;
        private String tradingCode;

        public String getTradingName() {
            return tradingName;
        }

        public void setTradingName(String tradingName) {
            this.tradingName = tradingName;
        }

        public String getTradingCode() {
            return tradingCode;
        }

        public void setTradingCode(String tradingCode) {
            this.tradingCode = tradingCode;
        }
    }
}

