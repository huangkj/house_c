package com.easylife.house.customer.util;

import com.easylife.house.customer.bean.LoanResult;

/**
 * Created by Mars on 2017/3/30 14:54.
 * 描述：贷款计算
 */

public class LoanUtil {

    /**
     * @param isAccrual 是否等额本息
     * @param moneyStr  140    - 单位：万元
     * @param yearStr   20     -单位：年
     * @param rateStr   0.0325
     * @return
     */
    public static LoanResult getLoanResult(final boolean isAccrual, String moneyStr, String yearStr, String rateStr) {
        LogOut.d("LoanUtil",
                "moneyStr:" + moneyStr +
                        "\n yearStr:" + yearStr +
                        "\n rateStr:" + rateStr
        );
        LoanResult result = null;
        try {
            result = new LoanResult();
            result.isAccrual = isAccrual;

            double moneyCapital = Double.parseDouble(moneyStr); // 贷款总额
            int yearNum = Integer.parseInt(yearStr) * 12; // 按揭月数
            float rate = Float.parseFloat(rateStr);

            double moneyAccrual = 0;  // 利息
            double paymentMonth = 0; //  每月月供金额

            double paymentOther = 0; // 每月递减，如果是等额本息，则为0
            double rateMonth = rate / 12; // 月利率

            if (isAccrual) {
                // 等额本息 ，月供相等，每月递减为0
                paymentMonth = (moneyCapital * rateMonth * Math.pow(1 + rateMonth, yearNum)) / (Math.pow(1 + rateMonth, yearNum) - 1);

                moneyAccrual = yearNum * paymentMonth - moneyCapital;
            } else {
                // 等额本金
                paymentMonth = moneyCapital / yearNum + moneyCapital * rateMonth;
                paymentOther = moneyCapital / yearNum * rateMonth;

                moneyAccrual = (paymentMonth + moneyCapital / yearNum * (1 + rateMonth)) / 2 * yearNum - moneyCapital;
            }

            result.moneyCapitalStr = DoubleFomat.format(moneyCapital);
            result.moneyAccrualStr = DoubleFomat.format3(moneyAccrual);
            result.paymentMonthStr = DoubleFomat.format2(paymentMonth * 10000);
            result.paymentOtherStr = DoubleFomat.format2(paymentOther * 10000);

            result.moneyCapital = moneyCapital;
            result.moneyAccrual = moneyAccrual;
            result.paymentMonth = paymentMonth * 10000;
            result.paymentOther = paymentOther * 10000;

            result.rateCapital = Float.parseFloat(moneyCapital / (moneyCapital + moneyAccrual) + "");
            result.rateAccrual = Float.parseFloat(moneyAccrual / (moneyCapital + moneyAccrual) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 组合贷
     *
     * @param isAccrual
     * @param moneyStrAcc
     * @param yearStrAcc
     * @param rateStrAcc
     * @param moneyStrBusiness
     * @param yearStrBusiness
     * @param rateStrBusiness
     * @return
     */
    public static LoanResult getLoanGroupResult(final boolean isAccrual, String moneyStrAcc, String yearStrAcc, String rateStrAcc, String moneyStrBusiness, String yearStrBusiness, String rateStrBusiness) {
        LoanResult result = null;
        try {
            result = new LoanResult();
            result.isAccrual = isAccrual;

            LoanResult resultAcc = getLoanResult(isAccrual, moneyStrAcc, yearStrAcc, rateStrAcc);
            LoanResult resultBusiness = getLoanResult(isAccrual, moneyStrBusiness, yearStrBusiness, rateStrBusiness);

            result.moneyCapital = resultAcc.moneyCapital + resultBusiness.moneyCapital;
            result.moneyAccrual = resultAcc.moneyAccrual + resultBusiness.moneyAccrual;
            result.paymentMonth = resultAcc.paymentMonth + resultBusiness.paymentMonth;
            result.paymentOther = resultAcc.paymentOther + resultBusiness.paymentOther;

            result.moneyCapitalStr = DoubleFomat.format2(result.moneyCapital);
            result.moneyAccrualStr = DoubleFomat.format3(result.moneyAccrual);
            result.paymentMonthStr = DoubleFomat.format2(result.paymentMonth);
            result.paymentOtherStr = DoubleFomat.format2(result.paymentOther);

            result.rateCapital = Float.parseFloat(result.moneyCapital / (result.moneyCapital + result.moneyAccrual) + "");
            result.rateAccrual = Float.parseFloat(result.moneyAccrual / (result.moneyCapital + result.moneyAccrual) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
