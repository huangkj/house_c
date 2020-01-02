package com.easylife.house.customer.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by Mars on 2017/6/19 14:27.
 * 描述：
 */

public class DrawableUil {

    public static void setDrawableLeft(Context context, int icon, TextView textView) {
        Drawable drawable = context.getResources().getDrawable(icon);

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        textView.setCompoundDrawables(drawable, null, null, null);
    }
}
