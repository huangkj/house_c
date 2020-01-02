package com.easylife.house.customer.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MoneyUtils {
    /**
     * 给金钱添加,
     *
     * @param money
     * @return
     */
    public static String moneyFormat(String money) {
        if (!TextUtils.isEmpty(money)) {
            NumberFormat nf = null;
//            if (money.startsWith("-")) {
//                nf = new DecimalFormat("￥,###.##");
//            } else {
//            }
            nf = new DecimalFormat(",###.##");
            BigDecimal bd = new BigDecimal(money);
            String format = nf.format(bd);

            if (format.contains("-￥")) {
                format = format.replace("-￥", "￥-");
            }
            return format;
        } else {
            return "";
        }

    }

    /**
     * 主页佣金价格展示格式化
     * 不包含￥,为正补+
     *
     * @param money
     * @return
     */
    public static String moneyFormat1(String money) {
        if (!TextUtils.isEmpty(money)) {
            money = money.replace(",", "");

            NumberFormat nf = null;
            nf = new DecimalFormat("###,###,###,###,###,###.##");
            BigDecimal bd = new BigDecimal(money);
            String format = nf.format(bd);

            if (format.contains("￥")) {
                format = format.replace("￥", "");
            }
            double f = Double.parseDouble(money);
            if (f >= 0) {
                format = "+" + format;
            }
            return format;
        } else {
            return money;
        }

    }


    /**
     * 主页佣金价格展示格式化
     * 不包含￥,为正补-
     *
     * @param money
     * @return
     */
    public static String moneyFormat4(String money) {
        if (!TextUtils.isEmpty(money)) {
            money = money.replace(",", "");

            NumberFormat nf = null;
            nf = new DecimalFormat("###,###,###,###,###,###.##");
            BigDecimal bd = new BigDecimal(money);
            String format = nf.format(bd);

            if (format.contains("￥")) {
                format = format.replace("￥", "");
            }
            double f = Double.parseDouble(money);
            if (f >= 0) {
                format = "-" + format;
            }
            return format;
        } else {
            return money;
        }

    }

    /**
     * 主页佣金价格展示格式化
     * 不包含￥
     *
     * @param money
     * @return
     */
    public static String moneyFormat2(String money) {
        if (!TextUtils.isEmpty(money)) {
            NumberFormat nf = null;
            nf = new DecimalFormat("###,###,###,###,###,###.##");
            BigDecimal bd = new BigDecimal(money);
            String format = nf.format(bd);

            if (format.contains("￥")) {
                format = format.replace("￥", "");
            }
            return format;
        } else {
            return money;
        }

    }


    /**
     * 给金钱添加,
     *
     * @param money
     * @return
     */
    public static String moneyFormat3(String money) {
        String m = DoubleFomat.format2(money);
        NumberFormat nf = null;
        nf = new DecimalFormat("###,###,###,###,###,##0.00");
        BigDecimal bd = new BigDecimal(m);
        String format = nf.format(bd);
        return format;
    }

    /**
     * 保留2位小数 四舍五入
     *
     * @param d
     * @return
     */
    public static double formatDouble(double d) {


        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);


        return bg.doubleValue();
    }


}
