package com.easylife.house.customer.bean;

/**
 * - @Description:  推送实体
 * - @Author:  hkj
 * - @Time:  2018/5/4 14:45
 */
public class PushMsgBean {

    /**
     * msgType : 1
     * platformType : BUTLER
     * text : 案场肖玮（136****0626），发起补卡申请，去查看；
     * title : 消息提示（待审核）
     * subType : 1
     * url :
     * time : 122322335
     * logo :
     * detailId :
     */

    public int msgType;
    public String platformType;
    public String text;
    public String title;
    public int subType;
    public String url;
    public String time;
    public String logo;
    public String detailId;
    public String customerCode;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
