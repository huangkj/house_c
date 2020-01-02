package com.easylife.house.customer.util;

import java.text.DecimalFormat;

/**
 * Created by Mars on 2017/3/30 11:19.
 * 描述：价格文本格式化
 */

public class DoubleFomat {

    public static String format(double d1) {
        DecimalFormat decimalFormat = new DecimalFormat("0");
        return decimalFormat.format(d1);
    }

    public static String format(String d1) {
        double d = Double.parseDouble(d1);
//        DecimalFormat decimalFormat = new DecimalFormat("0");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(d);
    }


    public static String format2(String d1) {
        double d = Double.parseDouble(d1);
        return format2(d);
    }

    public static String format2(double d1) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(d1);
    }

    public static String format3(double d1) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        return decimalFormat.format(d1);
    }
}
