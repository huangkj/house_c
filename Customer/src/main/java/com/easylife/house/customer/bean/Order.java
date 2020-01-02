package com.easylife.house.customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mars on 2017/4/10 15:12.
 * 描述：订单
 * 当订单状态为已支付，跟进状态为待支付时，则按钮状态为继续支付
 */

public class Order implements Serializable {
    /**
     * "houseImg": "http://olpdjpvtn.bkt.clouddn.com/FqnIBMq65tCTt907LsO7XsLKGyP4",
     * "orderType": 0,
     * "preferentialWay": "优惠方式",
     * "proccessFollowStatus": "PLACE",
     * "orderTime": "2017年05月22日 10:53:05",
     * "orderCode": "1000001413670302",
     * "vipMoney": 20000,
     * "devName": "0512三端楼盘房源楼盘名称",
     * "type": "买房优惠"
     * "purchaseMoney": 1000,
     * "information": "1-1-101",
     * "orderCode": "1000002109392384",
     * "devName": "0512三端楼盘房源楼盘名称",
     * "type": "认购下定",
     * "devsquaremetre": "100㎡"
     */


    public String customerName;
    public String brokerPhone;
    public String avgprice;
    public String addressDistrict;
    public String addressTown;
    public String addressDetail;
    public String devImg;
    public String projectName;
    public String houseImg;
    public String structure;
    public String propertyType;
    public String devbedroom;
    public String propertyName;
    public String feature;
    public String brokerName;
    public String brokerCode;
    public String createTime;
    public int coOp; // 1,(付费模式，)
    public String isTransparent; //  （是否是明房源   1 开启  ，2 关闭   空也是关闭）
    public String devsquaremetre;
    /**
     * 买房优惠-认筹；认购下定-认购
     */
    public String type;
    public String devName;
    public String devId;
    public String projectId;
    public String information;
    public String fArea;
    public String orderTime;
    public double paid;
    public String menberDiscountId;// 认筹id
    public String orderCode;
    public double purchaseMoney;
    public double vipMoney;
    public String proccessFollowStatus;
    public String preferentialWay;
    public int orderType;
    public int followType;
    public String image;
    /**
     * 未完全支付的订单App是否显示继续支付按钮的判断条件
     */
    public boolean allowed;


    //--------- 订单详情 -----------
    public OrderStatus1 first;
    public OrderStatus2 second;
    public OrderStatus3 third;


    /**
     * 订单列表中对于底部按钮的时间类型区分
     */
    public int typeBtn;

    /**
     * * "orderRefundStatus": "PLACE",(退款进度)
     * "serialNumber": "",
     * "proccessFollowStatus": "",
     * "settleStatus": "",
     * "orderRefund": "1",(1,退款   0,未退款)
     * "createTime": "1970年01月01日 08:00:00",(退款申请时间)
     * "paid": "",
     * "pay": "",
     * "endTime": "",
     * "paidTime": ""
     */
    public static class OrderStatus1 {
        public String orderRefundStatus;
        public String serialNumber;
        public String proccessFollowStatus;
        public String settleStatus;
        public String orderRefund;
        public String createTime;
        public String paid;
        public String pay;
        public String endTime;
        public String paidTime;
    }

    /**
     * "serialNumber": "akljshflasnfasfln",
     * "proccessFollowStatus": "PLACE",
     * "settleStatus": "PART",
     * "paid": 5,
     * "pay": 150.5,
     * "paidTime": "1970年01月18日 16:04:16"
     */
    public static class OrderStatus2 {
        public String serialNumber;
        public String proccessFollowStatus;
        public String settleStatus;
        public String paid;
        public String pay;
        public String paidTime;
    }

    /**
     * * "proccessFollowStatus": "",
     * "signTime": "",
     * "settleStatus": ""
     */
    public static class OrderStatus3 {
        public String proccessFollowStatus;
        public String signTime;
        public String settleStatus;
    }

    /**
     * 订单状态
     */
    public enum OrderType {
        PLACE(0, "已下单"), PAYED(1, "已付款"), SIGNED(2, "已签约"),
        REFUND(3, "已退款"), REFUNDING(4, "退款中"), REFUNDFIAL(5, "退款失败"), DEL(9, "已取消");
        public final String msg;
        public final int code;

        private OrderType(int code, String msg) {
            this.msg = msg;
            this.code = code;
        }
    }

    /**
     * 订单数据的类型
     */
    public enum FollowType {
        PURCHASE(3, "认购"),
        RAISE(5, "认筹");

        public final String msg;
        public final int code;

        private FollowType(int code, String msg) {
            this.msg = msg;
            this.code = code;
        }
    }

    /**
     * 楼盘的付费模式
     */
    public enum CoOp {
        /**
         * 认筹
         */
        MENBER(0, "会员"),
        /**
         * 认购
         */
        ENTERPRISE(1, "企业"), MENBER_ENTERPRISE(2, "会员+企业"), NULL(3, "合作模式为无"),;

        public final String msg;
        public final int code;

        private CoOp(int code, String msg) {
            this.msg = msg;
            this.code = code;
        }
    }

    /**
     * 订单的退款状态
     */
    public enum RefundStatus {
        PLACE(0, "已提交"), PART(1, "待受理"), ALL(2, "已退款"), WAIT_SIGNATURE(3, "退款失败");
        public final String msg;
        public final int code;

        private RefundStatus(int code, String msg) {
            this.msg = msg;
            this.code = code;
        }
    }

    /**
     * 订单的付款状态
     */
    public enum SettleStatus {
        NOT(0, "未结"), PART(1, "部分结"), ALL(2, "已结");
        public final String msg;
        public final int code;

        private SettleStatus(int code, String msg) {
            this.msg = msg;
            this.code = code;
        }
    }

    /**
     * 订单的跟进状态
     */
    public enum FollowStatus {
        PLACE(0, "未支付"), PART(1, "待支付"), ALL(2, "已付款"),
        SIGNATURED(3, "已签章"), WAIT_SIGN(4, "待签约"),
        SIGNED(5, "已签约"), REFUND(6, "退款");

        public final String msg;
        public final int code;

        private FollowStatus(int code, String msg) {
            this.msg = msg;
            this.code = code;
        }
    }
}
