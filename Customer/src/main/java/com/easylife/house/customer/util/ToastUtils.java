package com.easylife.house.customer.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 公用Toast类
 */
public class ToastUtils {

    public static void showShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
