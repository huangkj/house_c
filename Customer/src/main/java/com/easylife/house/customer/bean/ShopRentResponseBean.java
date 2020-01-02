package com.easylife.house.customer.bean;

public class ShopRentResponseBean {

    /**
     * area : 400.00
     * imageUrl : 这是图片地址
     * title : 测试租赁
     * devName : 测试楼盘
     * rent : 10000.00
     */

    private String area;
    private String imageUrl;
    private String title;
    private String devName;
    private String rent;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }
}
