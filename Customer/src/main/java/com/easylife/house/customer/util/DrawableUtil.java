package com.easylife.house.customer.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by Mars on 2017/2/25 17:24.
 * 描述：
 */

public class DrawableUtil {

    public static void drawableTop(Context context, TextView tv, int imageID) {
        Drawable top = null;
        if (imageID != 0) {
            top = context.getResources().getDrawable(imageID);
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
    }

    public static void drawableLeft(Context context, TextView tv, int imageID) {
        Drawable left = null;
        if (imageID != 0) {
            left = context.getResources().getDrawable(imageID);
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
        tv.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
    }

    public static void drawableRight(Context context, TextView tv, int imageID) {
        Drawable right = null;
        if (imageID != 0) {
            right = context.getResources().getDrawable(imageID);
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
    }
}
