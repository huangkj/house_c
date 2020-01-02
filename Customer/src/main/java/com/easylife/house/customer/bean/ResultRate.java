package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/4/5 20:46.
 * 描述：房贷利率返回数据
 */

public class ResultRate implements Serializable {
//    {
//        "rateName": "2017年7月24利率上限（0.8折）",
//            "createTime": "2017-04-05 20:32:43",
//            "rateNum": "0.028",
//            "id": 8,
//            "type": 1,
//            "status": 1
//    }

    public String rateName;
    public String createTime;
    public String rateNum;
    public String id;
    public String type;
    public String status;

    @Override
    public String toString() {
        return "ResultRate{" +
                "rateName='" + rateName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", rateNum='" + rateNum + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
