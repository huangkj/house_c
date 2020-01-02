package com.easylife.house.customer.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 屏幕分辨率和像素转换
 */
public class DimenUtils {

    /**
     * O
     * 获取屏幕的尺寸
     *
     * @param context
     * @return int[0]=screenWidth int[1]=screenHeight
     */
    public static int[] getScreenSize(Activity context) {
        // 获取屏幕密度（方法3）
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        float density = dm.density;      // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
//		int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）  
//		float xdpi = dm.xdpi;           
//		float ydpi = dm.ydpi;  

//		Log.e(TAG + "  DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);  
//		Log.e(TAG + "  DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);  

//		int screenWidthDip = dm.widthPixels;        // 屏幕宽（dip，如：320dip）  
//		int screenHeightDip = dm.heightPixels;      // 屏幕宽（dip，如：533dip）  

//		Log.e(TAG + "  DisplayMetrics(222)", "screenWidthDip=" + screenWidthDip + "; screenHeightDip=" + screenHeightDip);  

        int screenWidth = (int) (dm.widthPixels * density + 0.5f);      // 屏幕宽（px，如：480px）
        int screenHeight = (int) (dm.heightPixels * density + 0.5f);     // 屏幕高（px，如：800px）

        //Log.e(TAG + "  DisplayMetrics(222)", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);
        return new int[]{dm.widthPixels, dm.heightPixels};
//		return new int[]{screenWidth,screenHeight};

//		Display display = context.getWindowManager().getDefaultDisplay();
//		return new int[]{display.getWidth(),display.getHeight()};
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}