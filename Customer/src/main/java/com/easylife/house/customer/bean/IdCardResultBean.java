package com.easylife.house.customer.bean;

public class IdCardResultBean {

    /**
     * id : 1
     * buyerCode : 868e9e7e-15cf-42c0-ae36-a34670493ffa
     * realName : 123123
     * identityCardNum : 123123
     * identityCardFront : 123123
     * identityCardReverse : 123123
     * createTime : 1541502174000
     * password :
     */

    private int id;
    private String buyerCode;
    private String realName;
    private String identityCardNum;
    private String identityCardFront;
    private String identityCardReverse;
    private long createTime;
    private String password;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityCardNum() {
        return identityCardNum;
    }

    public void setIdentityCardNum(String identityCardNum) {
        this.identityCardNum = identityCardNum;
    }

    public String getIdentityCardFront() {
        return identityCardFront;
    }

    public void setIdentityCardFront(String identityCardFront) {
        this.identityCardFront = identityCardFront;
    }

    public String getIdentityCardReverse() {
        return identityCardReverse;
    }

    public void setIdentityCardReverse(String identityCardReverse) {
        this.identityCardReverse = identityCardReverse;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
