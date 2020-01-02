package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/6/28 18:52.
 * 描述：检查支付状态返回支付信息
 */

public class ResultCheckOrder implements Serializable {
// "customerPhone": "18210324011",
//         "payOnThisTime": "1000.0",
//         "settleStatus": "NOT",
//         "cellName": "1#1单元-1-101室",
//         "paid": "0.0",
//         "pay": "20000.0",
//         "localSerial": "595464ff00cce33a951b0dcc",
//         "tobepay": "20000.0",
//         "bankSerial": "null",
//         "customerName": "陆思",
//         "isSuccess": "WAIT"

    public String customerPhone;
    public String payOnThisTime;
    public String settleStatus;
    public String devId;
    public String devName;
    public String cellName;
    public String paid;
    public String pay;
    public String localSerial;
    public String tobepay;
    public String bankSerial;
    public String customerName;
    public String isSuccess;
    public String coop; // 0-会员，1-企业，2-会员+企业
    public String isTransparent; //  （是否是明房源   1 开启  ，2 关闭   空也是关闭）

    public enum CheckState {
        NOT(0, "未结"), PART(1, "部分结"), ALL(2, "已结");

        public int code;
        public String msg;

        private CheckState(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
