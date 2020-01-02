package com.easylife.house.customer.bean;

/**
 * Created by Mars on 2017/3/13 20:22.
 * 描述：客户
 */

public class Customer {

    // 推荐，已到访，已认购，已签约，退房
    public static final String TYPE_RECORD_RECOMMEND = "0";
    public static final String TYPE_RECORD_ARRIVED = "1";
    public static final String TYPE_RECORD_PAID = "2";
    public static final String TYPE_RECORD_SIGNED = "3";
    public static final String TYPE_RECORD_REFUND = "4";


    public String common; // 是否是网络推客  0-C端用户，1-网络推客

    public String realName;
    public String email;
    public String address;
    public String family;// id
    public String profession; // id

    public String token;

    public String username;
    public String phone;
    public String pass;
    public String headimg;
    public String userCode;
    public String qq; //0未绑定，1已绑定
    public String wechat; //0未绑定，1已绑定
    public String weibo; //0未绑定，1已绑定
    public String updatetime;
    public String sex;//0男，1女,2其他
    public String age;// 年份，显示时计算
    public String buycity;
    public String city;
    public String createtime;
    public String id;
    public String intentDev;
    public String loan;
    public String structure;
    public String easemobUserName;
    public String myPoint;
    public String qqNum;
    public String identityCardNum;
    public String transactionPassword;//交易密码
    public String point;
    /**
     * 是否全部填写了用户资料
     */
    public boolean authentication;

    /**
     * "qq": "0",//0未绑定，1绑定
     * "createtime": 1648718077000,
     * "wechat": "0",//0未绑定，1绑定
     * "token": "f956b728-e23c-4698-860f-031dfbbd07a8",
     * "password": "",
     * "weibo": "0",//0未绑定，1绑定
     * "phone": "13693186350",
     * "id": 1,
     * "updatetime": 1648718077000,
     * "userCode": "c6f986a9-411e-44b3-bee2-db6026b9beee",
     * "username": "张宇"
     * age：年份
     */

    public Customer() {
    }

    public Customer(String phone, String pass) {
        this.phone = phone;
        this.pass = pass;
    }

    public Customer(String phone, String pass, String usercode) {
        this.phone = phone;
        this.pass = pass;
        this.userCode = usercode;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", pass='" + pass + '\'' +
                ", headImg='" + headimg + '\'' +
                ", userCode='" + userCode + '\'' +
                ", qq='" + qq + '\'' +
                ", wechat='" + wechat + '\'' +
                ", weibo='" + weibo + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", buycity='" + buycity + '\'' +
                ", createtime='" + createtime + '\'' +
                ", id='" + id + '\'' +
                ", intentDev='" + intentDev + '\'' +
                ", loan='" + loan + '\'' +
                ", structure='" + structure + '\'' +
                '}';
    }
}
