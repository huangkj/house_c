package com.easylife.house.customer.bean;

import java.util.List;

public class ApplyBrokerRequest {

    /**
     * userId : 518
     * applyUserName : 敏德测试
     * brokerOders : ["1680"]
     * belongToBank : 中国银行
     * bankCardNum : 666666
     * applyBrokerage : 3000.000000
     * cardPhotoFront : 身份证正面照
     * cardPhotoReverse : 身份证反面照
     * openBranchBank : 什么支行
     * applyUserCardNum:申请人证件号码
     */

    private String userId;
    private String applyUserName;
    private String belongToBank;
    private String bankCardNum;
    private String applyBrokerage;
    private String cardPhotoFront;
    private String cardPhotoReverse;
    private String openBranchBank;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public String getBankBranchNum() {
        return bankBranchNum;
    }

    public void setBankBranchNum(String bankBranchNum) {
        this.bankBranchNum = bankBranchNum;
    }

    private String bankBranchNum;

    public String getApplyUserCardNum() {
        return applyUserCardNum;
    }

    public void setApplyUserCardNum(String applyUserCardNum) {
        this.applyUserCardNum = applyUserCardNum;
    }

    private String applyUserCardNum;
    private List<SelectBrokerOrderBean> brokerOders;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getBelongToBank() {
        return belongToBank;
    }

    public void setBelongToBank(String belongToBank) {
        this.belongToBank = belongToBank;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getApplyBrokerage() {
        return applyBrokerage;
    }

    public void setApplyBrokerage(String applyBrokerage) {
        this.applyBrokerage = applyBrokerage;
    }

    public String getCardPhotoFront() {
        return cardPhotoFront;
    }

    public void setCardPhotoFront(String cardPhotoFront) {
        this.cardPhotoFront = cardPhotoFront;
    }

    public String getCardPhotoReverse() {
        return cardPhotoReverse;
    }

    public void setCardPhotoReverse(String cardPhotoReverse) {
        this.cardPhotoReverse = cardPhotoReverse;
    }

    public String getOpenBranchBank() {
        return openBranchBank;
    }

    public void setOpenBranchBank(String openBranchBank) {
        this.openBranchBank = openBranchBank;
    }

    public List<SelectBrokerOrderBean> getBrokerOders() {
        return brokerOders;
    }

    public void setBrokerOders(List<SelectBrokerOrderBean> brokerOders) {
        this.brokerOders = brokerOders;
    }
}
