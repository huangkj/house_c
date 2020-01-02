package com.easylife.house.customer.util;

import android.text.TextUtils;

import java.security.cert.TrustAnchor;

/**
 * Created by Mars on 2017/8/10 14:48.
 * 描述：价格数字处理工具
 */

public class PriceUtil {

    public static String formatPriceAver(String price) {
        if (TextUtils.isEmpty(price) || "0".equals(price)) {
            return "价格待定";
        }
        int p = Integer.parseInt(price);
        if (p == 0)
            return "价格待定";
        return Integer.parseInt(price) + "万起";
    }

    public static String formatPrice(String price, String unit) {
        if (TextUtils.isEmpty(price) || "0".equals(price)) {
            return "价格待定";
        }
        double p = Double.parseDouble(price);
        if (p >= 1000000) {
            p /= 10000;
            return DoubleFomat.format2(p) + "万" + unit;
        } else {
            return price + unit;
        }
    }

    public static String formatPriceUnit(String price, String unit) {
        if (TextUtils.isEmpty(price) || "0".equals(price)) {
            return "价格待定";
        }
        int p = Integer.parseInt(price);
        if (p >= 1000000) {
            p /= 10000;
            return p + "万" + unit;
        } else {
            return price + unit;
        }
    }

    public static String checkPriceValue(String price) {
        if (TextUtils.isEmpty(price))
            return "暂无数据";
        try {
            double p = Double.parseDouble(price);
            if (p == 0) {
                return "暂无数据";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "￥" + formatNum(DoubleFomat.format2(price));
    }

    public static String formatNum(String numString) {
        return formatNum(numString, true);
    }

    public static String formatNum(String numString, boolean hasPoint) {
        try {
            String d = ".00";
            int num = 0;
            if (numString.contains(".")) {
                d = numString.substring(numString.indexOf("."));
                num = Integer.parseInt(numString.substring(0, numString.indexOf(".")));
            } else {
                num = Integer.parseInt(numString);
            }
            String result = String.format("%1$,d", num);
            if (hasPoint) {
                return result + d;
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return numString;
        }
    }
}
