package com.easylife.house.customer.bean;

import java.util.List;

public class SelectBrokerOrderRequest {

    /**
     * orderBy : 1
     * userId : 518
     * devIdList : ["1315"]
     */

    private String orderBy;
    private String userId;
    private List<String> devIdList;

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getDevIdList() {
        return devIdList;
    }

    public void setDevIdList(List<String> devIdList) {
        if (devIdList.size() > 0) {
            this.devIdList = devIdList;
        } else {
            this.devIdList = null;
        }
    }
}
