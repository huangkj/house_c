package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mars on 2017/6/28 17:27.
 * 描述：楼盘优惠信息-认购下单页面使用
 */

public class DevFavorable implements Serializable {
//     "favourable": [
//    {
//        "value": "ceqw",
//            "key": "ceqw"
//    }
//     ],
//             "cityName": "北京市",
//             "discount": {
//        "discountAll": "12",
//                "discountDaiType": 0,
//                "discountAllType": "1",
//                "discountDai": ""
//    },
//            "cityId": "110100"

    public String cityId;
    public String subscription; // 认购定金
    public String cityName;
    public Discount discount;
    public List<Favorable> favourable;

    public class Favorable implements Serializable {
        public String value;
        public String key;
    }

    public class Discount implements Serializable {
        public String discountAll;
        public String discountAllType;
        public String discountDai;
        public String discountDaiType;
    }
}
