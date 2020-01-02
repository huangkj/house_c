package com.easylife.house.customer.util;

public class CommonUtils {
    public static String hidePhoneNumber(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
