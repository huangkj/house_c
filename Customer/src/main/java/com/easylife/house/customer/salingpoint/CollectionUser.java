package com.easylife.house.customer.salingpoint;

public class CollectionUser {

    String userId;
    String userName;
    USER_TYPE userType;
    String userCity;
    String userCityCode;
    String phone;

    public CollectionUser() {
        // TODO Auto-generated constructor stub
    }


    public CollectionUser(String userId, String userName, USER_TYPE userType,
                          String userCity, String userCityCode) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.userCity = userCity;
        this.userCityCode = userCityCode;
    }

    public CollectionUser(String userId, String userName, USER_TYPE userType
    ) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "CollectionUser{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userType=" + userType +
                ", userCity='" + userCity + '\'' +
                ", userCityCode='" + userCityCode + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public USER_TYPE getUserType() {
        return userType;
    }


    public void setUserType(USER_TYPE userType) {
        this.userType = userType;
    }


    public String getUserCity() {
        return userCity;
    }


    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }


    public String getUserCityCode() {
        return userCityCode;
    }


    public void setUserCityCode(String userCityCode) {
        this.userCityCode = userCityCode;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public static enum USER_TYPE {
        B("经纪人端", 0), C("购房者端", 1), CMS("运营端", 2);

        public final String msg;
        public final int code;


        private USER_TYPE(String msg, int code) {
            this.msg = msg;
            this.code = code;
        }
    }
}
