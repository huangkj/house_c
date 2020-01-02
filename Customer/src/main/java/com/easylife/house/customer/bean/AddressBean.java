package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * 收货地址
 */
public class AddressBean implements Serializable {

    /**
     * id : 4
     * userId : 382
     * userType :
     * addressFull : 西红门路62号公园懿府四号楼1单元602室
     * isDefault : 1
     * tagId : 1
     * addrProvince : 北京市
     * addrCity : 市辖区
     * addrCounty : 丰台区
     * userName : 卫小雨     17017511669
     * phoneNum : 18201354073
     */

    private String id;
    private String userId;
    private String userType;
    private String addressFull;
    private String isDefault;
    private String tagId;
    private String addrProvince;
    private String addrCity;
    private String addrCounty;
    private String provincesId;
    private String cityId;
    private String areaId;
    private String userName;
    private String phoneNum;
    private String tagName;


    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAddressFull() {
        return addressFull;
    }

    public void setAddressFull(String addressFull) {
        this.addressFull = addressFull;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getAddrProvince() {
        return addrProvince;
    }

    public void setAddrProvince(String addrProvince) {
        this.addrProvince = addrProvince;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
    }

    public String getAddrCounty() {
        return addrCounty;
    }

    public void setAddrCounty(String addrCounty) {
        this.addrCounty = addrCounty;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


    public String getAddrProvinceId() {
        return provincesId;
    }

    public String getAddrCityId() {
        return cityId;
    }

    public String getAddrCountyId() {
        return areaId;
    }
}
