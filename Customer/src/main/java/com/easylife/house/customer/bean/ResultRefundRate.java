package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mars on 2017/7/18 19:26.
 * 描述：退款进度
 */

public class ResultRefundRate implements Serializable {

    public String refund;// 退款金额

    public String bankAddress;
    public String bankNum;
    public String userName;
    public List<BeanFile> pdf;
    public List<String> img;
    public String comments;
    public String refundStatus;

    public List<RefundRate> data;


    @Override
    public String toString() {
        return "ResultRefundRate{" +
                "refund='" + refund + '\'' +
                ", data=" + data +
                '}';
    }

    public static class RefundRate {
        public String createTime;
        public String refundStatus;


        @Override
        public String toString() {
            return "RefundRate{" +
                    "createTime='" + createTime + '\'' +
                    ", refundStatus='" + refundStatus + '\'' +
                    '}';
        }
    }

    public enum RefundStatus {
        PLACE(0, "已提交"), ALL(2, "已退款"), WAIT_SIGNATURE(3, "退款失败");

        private int code;
        private String msg;

        private RefundStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
