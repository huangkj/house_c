package com.easylife.house.customer.bean;

public class CityPriceBean {

    /**
     * id : 1
     * cityId : 110100
     * cityName : 北京
     * price : 59889
     */

    private int id;
    private String cityId;
    private String cityName;
    private String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
