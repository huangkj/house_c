package com.easylife.house.customer.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mars on 2017/4/6 20:01.
 * 描述：时间数据换算
 */

public class DateUtil {
    public static String phpToDate(String phpTime, String format) {
        try {
            if (phpTime.length() == 10)
                phpTime = phpTime + "000";
            return new SimpleDateFormat(format).format(new Date(Long.parseLong(phpTime)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToStr(Date date, String format) {
        try {

            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToLong(String dateStr, String format) {
        LogOut.d("dateToLong:", dateStr);
        try {
            return new SimpleDateFormat(format).parse(dateStr).getTime() + "";
        } catch (Exception e) {
            LogOut.d("dateToLong - Exception :", e.toString());
        }
        return new Date().getTime() + "";
    }
}
