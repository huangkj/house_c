package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mars on 2017/6/29 15:03.
 * 描述：下单参数
 */

public class OrderParameter implements Serializable {

    /**
     * 认购
     * "realName": "陆思",
     * "customerPhone": "18210324011",
     * "customerIdCardNum": "110107198402010052",
     * "followType": "3",
     * "estateProjectDevId": "790",
     * "remarks": "",
     * "estateProjectDevName": "珠江帝景A区",
     * "orderLog": {
     * "payOnThisTime": "1000",
     * "pay": "20000"
     * },
     * "customerPurchase": {
     * "cellName": "101",
     * "unitId": "1",
     * "floor": "1",
     * "buildId": "1",
     * "buildName": "1"
     * },
     * "orderDiscount": [{
     * "discountType": "HOME",
     * "content": "贷款99折",
     * "discount": "0.98",
     * "pay": "0"
     * },{
     * "discountType": "HOME",
     * "content": "贷款99折",
     * "discount": "0.98",
     * "pay": "0"
     * }]
     */


    public String realName;
    public String customerPhone;
    public String customerIdCardNum;
    /**
     * 3- 认购
     * 5- 认筹
     */
    public String followType;
    public String estateProjectDevId;
    public String remarks = "";
    public String estateProjectDevName;
    public String houseNo;
    /**
     * 认筹的优惠id
     */
    public String menberDiscountId = "0";
    public OrderLog orderLog = new OrderLog();
    public CustomerPurchase customerPurchase = new CustomerPurchase();
    public List<Favorable> orderDiscount = new ArrayList<>();

    public class OrderLog implements Serializable {
        public String payOnThisTime;
        public String pay;
    }

    /**
     * MENBER(0, "会员优惠"), HOME(1, "置业优惠");
     */
    public static class Favorable implements Serializable {
        public static final String TYPE_VIP = "MENBER";
        public static final String TYPE_DEV = "HOME";

        public String discountType;
        public String content;
        public String discount;
        public String pay = "0";
    }

    public class CustomerPurchase implements Serializable {
        public String cellName;
        public String unitId;
        public String floor;
        public String buildId;
        public String buildName;
    }
}
