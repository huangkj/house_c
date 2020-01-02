package com.easylife.house.customer.bean;

/**
 * - @Description:  我的佣金
 * - @Author:  hkj
 * - @Time:  2018/5/14 10:46
 */
public class MyBrokerageBean {


    /**
     * estimateAmount : 2230.0
     * shouldAmount : 11600.0
     * surplusAmount : -9370.0
     * taxAmount : 1065.0
     */

    private String estimateAmount;
    private String shouldAmount;
    private String surplusAmount;
    private String taxAmount;

    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public String getShouldAmount() {
        return shouldAmount;
    }

    public void setShouldAmount(String shouldAmount) {
        this.shouldAmount = shouldAmount;
    }

    public String getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(String surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }
}
