package com.easylife.house.customer.bean;

import java.io.Serializable;

/**
 * Created by Mars on 2017/3/30 14:54.
 * 描述： 贷款计算结果
 */

public class LoanResult implements Serializable {

    public boolean isAccrual;// 是否 等额本息
    public String moneyCapitalStr;//本金            单位：万元
    public String moneyAccrualStr;// 利息        单位：万元
    public String paymentMonthStr; // 月供        单位：元/月
    public String paymentOtherStr; // 每月递减   单位：元

    public double moneyCapital;//本金            单位：元
    public double moneyAccrual;// 利息        单位：元
    public double paymentMonth; // 月供        单位：元/月
    public double paymentOther; // 每月递减   单位：元

    public float rateCapital; // 本金所占比例   绘图用
    public float rateAccrual; // 利息所占比例   绘图用

    @Override
    public String toString() {
        return "LoanResult{" +
                "isAccrual=" + isAccrual +
                ", moneyCapitalStr='" + moneyCapitalStr + '\'' +
                ", moneyAccrualStr='" + moneyAccrualStr + '\'' +
                ", paymentMonthStr='" + paymentMonthStr + '\'' +
                ", paymentOtherStr='" + paymentOtherStr + '\'' +
                ", moneyCapital=" + moneyCapital +
                ", moneyAccrual=" + moneyAccrual +
                ", paymentMonth=" + paymentMonth +
                ", paymentOther=" + paymentOther +
                ", rateCapital=" + rateCapital +
                ", rateAccrual=" + rateAccrual +
                '}';
    }
}
