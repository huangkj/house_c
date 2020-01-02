package com.easylife.house.customer.util;

import android.util.Log;


/**
 * Created by Mars on 2017/3/16 16:04.
 * 描述：
 */

public class LogOut {

    public static boolean DEBUG = true;
    public static String TAG = "Mars";

    public static void d(String tag, Object o) {
        if (DEBUG && o != null) {
            Log.e(TAG, tag + " : \n " + o.toString());
        }
    }
}
