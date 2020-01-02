package com.easylife.house.customer.bean;

public class IdCardImageBase64 {
    public String base64PicFrontString;
    public String base64PicBackString;

    public IdCardImageBase64(String base64PicFrontString, String base64PicBackString) {
        this.base64PicFrontString = base64PicFrontString;
        this.base64PicBackString = base64PicBackString;
    }
}
