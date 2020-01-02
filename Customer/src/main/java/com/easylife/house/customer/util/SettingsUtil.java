package com.easylife.house.customer.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.WindowManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 设置相关
 */
public class SettingsUtil {
    private static final String CONFIG_NAME = "config_settings";

    public static void statusColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));
        }
    }

    /**
     * 设置是否在WIFI环境下自动下载
     *
     * @param context
     * @param open
     */
    public static void setWIFIAutoDownload(Context context, boolean open) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME, 0);
        sp.edit().putBoolean("wifi_down", open).commit();
    }

    /**
     * 获取是否在wifi环境下自动下载
     * 默认 开启
     *
     * @param context
     * @return
     */
    public static boolean getWIFIAutoDownLoad(Context context) {
        return context.getSharedPreferences(CONFIG_NAME, 0).getBoolean("wifi_down", true);
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }


    /**
     * get App versionCode
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取GPS地址（流量链接）
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        LogOut.d("getLocalIpAddress", "ipAddress:" + inetAddress.getHostAddress().toString());
                        return inetAddress.getHostAddress().toString();
                    }
                    ;
                }
            }
        } catch (SocketException ex) {
            LogOut.d("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    /**
     * 获取wifi地址
     *
     * @param context
     * @return
     */
    public static String getWIFIIp(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            LogOut.d("isWifiEnabled", "ipAddress:" + ipAddress);
            return intToIp(ipAddress);
        } else {
            return getLocalIpAddress();
        }
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }
}
