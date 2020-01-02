package com.easylife.house.customer.bean;

import java.io.Serializable;

public class BankCardListBean implements Serializable {

    /**
     * "buyerCode" : "003b6f67-102e-47bf-9a4a-b8dee8c9f769",
     * "id" : 158,
     * "belongToBank" : "招商银行",
     * "cardName" : "穆洋洋",
     * "bankCardNum" : "6214831016827492",
     * "linkNumber" : "308100005682",
     * "openBranchBank" : "招商银行股份有限公司北京回龙观支行",
     * "applyUserCardNum" : "412725199203136579",
     * "cardUserPhone" : "13592585386",
     * "createTime" : 1563971059000,
     * "flag" : 1
     */
    private boolean isSelect;
    private Integer bankImgIsExist; //0-需要补充信息 1-不需要补充信息

    public boolean isSelf() {
        return !"2".equals(flag);
    }

    public boolean isComplete() {
        return bankImgIsExist != null && bankImgIsExist == 1;
    }


    /**
     * 是否是本人银行卡，  1本人 2不是本人
     */
    private String flag;


    private int id;
    private String buyerCode;
    private String cardName;
    private String bankCardNum;

    public String getProvinceId() {
        return provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getBankImgBack() {
        return bankImgBack;
    }

    public String getBankImgFront() {
        return bankImgFront;
    }

    public String getBankCode() {
        return bankCode;
    }

    private String belongToBank; // 所属银行，如中国银行
    private String applyUserCardNum;
    private String openBranchBank;// 开户行支行名称
    private String cardUserPhone;
    private String provinceId;
    private String cityId;
    private String provinceName;
    private String cityName;

    private String bankImgBack;
    private String bankImgFront;

    private String bankCode;// 银行Code，如中国银行等

    public String getLinkNumber() {
        return linkNumber;
    }

    public void setLinkNumber(String linkNumber) {
        this.linkNumber = linkNumber;
    }

    private String linkNumber; // 开户行银行支行联行号
    private long createTime;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getBelongToBank() {
        return belongToBank;
    }

    public void setBelongToBank(String belongToBank) {
        this.belongToBank = belongToBank;
    }

    public String getApplyUserCardNum() {
        return applyUserCardNum;
    }

    public void setApplyUserCardNum(String applyUserCardNum) {
        this.applyUserCardNum = applyUserCardNum;
    }

    public String getOpenBranchBank() {
        return openBranchBank;
    }

    public void setOpenBranchBank(String openBranchBank) {
        this.openBranchBank = openBranchBank;
    }

    public String getCardUserPhone() {
        return cardUserPhone;
    }

    public void setCardUserPhone(String cardUserPhone) {
        this.cardUserPhone = cardUserPhone;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
