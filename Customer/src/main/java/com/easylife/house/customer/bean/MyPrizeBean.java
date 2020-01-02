package com.easylife.house.customer.bean;


import java.io.Serializable;

public class MyPrizeBean implements Serializable {


    /**
     * id : 1
     * prizeId : 1
     * img : https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1973513322,4033890055&fm=173&app=25&f=JPEG?w=640&h=565&s=2DF6EC12474262E0044C55CE0200E032
     * name : 奖品1809181429
     * winTime : 1537340487000
     * prizeCategory : 1
     * redeemCode : null
     */

    private int id;
    private int prizeId;
    private String img;
    private String name;
    private long winTime;
    private int prizeCategory;
    /**
     * 兑换码 序列号
     */
    private String redeemCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWinTime() {
        return winTime;
    }

    public void setWinTime(long winTime) {
        this.winTime = winTime;
    }

    public int getPrizeCategory() {
        return prizeCategory;
    }

    public void setPrizeCategory(int prizeCategory) {
        this.prizeCategory = prizeCategory;
    }

    public String getRedeemCode() {
        return redeemCode;
    }

    public void setRedeemCode(String redeemCode) {
        this.redeemCode = redeemCode;
    }
}
