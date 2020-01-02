package com.easylife.house.customer.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.easylife.house.customer.App;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.text.TextUtils.isEmpty;

/**
 * Created by zgm on 2017/3/14.
 */

public class CustomerUtils {

    /**
     * snackBar 提示
     *
     * @param context activity 上下文
     * @param text    提示消息
     */
    public static void showTip(Context context, String text) {
//        Snackbar.with(context).color(Color.RED)
//                .duration(1000)
//                .text(text)
//                .textColor(Color.WHITE)
//                .show(context);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    /**
     * 中间4位带*手机号
     *
     * @param phone
     * @return
     */
    public static String splitPhoneNum(String phone) {
        try {
            String firstNum = phone.substring(0, 3);
            String endNum = phone.substring(7, 11);
            return firstNum + "****" + endNum;
        } catch (Exception e) {
            e.printStackTrace();
            return phone;
        }
    }
    /**
     * 日期转换
     *
     * @param time
     * @return
     */
    public static String dateTransSdf(long time) {
        String strTime = time+"";
        if((strTime).length() <= 10){
            strTime = time+"000";
        }

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(strTime));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    /**
     * 日期转换 时分
     *
     * @param time
     * @return
     */
    public static String dateTransSdfMinutes(long time) {
        String strTime = time+"";
        if((strTime).length() <= 10){
            strTime = time+"000";
        }

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(strTime));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(c.getTime());
    }

    /**
     * 图片上传base64加密
     *
     * @param imgPath
     * @return
     */
    public static String base64Recode(String imgPath) {
        byte[] bytes = FileToBytesUtil.File2byte(imgPath);
        String picStr = Base64Util.encode(bytes);

        return picStr;
    }

    /**
     * 时间转换成秒
     *
     * @param expireDate 时间
     * @param format     指定格式
     * @return
     */
    public static int getSecondsFromDate(String expireDate, String format) {

        if (expireDate == null || expireDate.trim().equals(""))
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        java.util.Date date = null;
        try {
            date = sdf.parse(expireDate);
            return (int) (date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /****
     * 传入具体日期 ，返回具体日期减几个月。
     *
     * @param date     日期(2014-04-20)
     * @param position 减几个月
     * @return 2014-03-20
     * @throws ParseException
     */
    public static String subMonth(String date, int position) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);

        rightNow.add(Calendar.MONTH, position);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);

        return reStr;
    }

    public static long getMainThreadId() {
        return App.getMainThreadId();
    }

    // 判断当前的线程是不是在主线程
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return App.getMainThreadHandler();
    }


    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if(!isEmpty(wifiMac)){
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if(!isEmpty(imei)){
                deviceId.append("imei");
                deviceId.append(imei);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if(!isEmpty(sn)){
                deviceId.append("sn");
                deviceId.append(sn);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
            //如果上面都没有， 则生成一个id：随机码
//            String uuid = getUUID(context);
//            if(!isEmpty(uuid)){
//                deviceId.append("id");
//                deviceId.append(uuid);
//                Log.e("getDeviceId : ", deviceId.toString());
//                return deviceId.toString();
//            }
        } catch (Exception e) {
            e.printStackTrace();
//            deviceId.append("id").append(getUUID(context));
        }
        Log.e("getDeviceId : ", deviceId.toString());
        return deviceId.toString();
    }


    /**
     * TextView 设置默认数据
     * @param textView
     * @param content
     */
//    public static void isEmptyData(TextView textView,String content,String unit){
//        if(content != null && !TextUtils.isEmpty(content)){
//            textView.setText(content+unit);
//        }else {
//            textView.setText("暂无数据");
//        }
//    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置TextView空数据
     * @param tv
     * @param content 设置内容
     * @param unit 单位
     * @param emptyText 数据为空时的内容
     */
    public static void setEmptyTv(TextView tv,String content,String unit,String emptyText){
        if(!TextUtils.isEmpty(content)){
            tv.setText(content+unit);
        }else {
            tv.setText(emptyText);
        }
    }



}
