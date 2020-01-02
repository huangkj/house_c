package com.easylife.house.customer.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.easylife.house.customer.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zgm on 2017/3/29.
 */

public class TimeUtils {
    private int time=60;

    private Timer timer;

    private TextView btnSure;

    private String btnText;
    private Context context;

    public TimeUtils(TextView btnSure, String btnText, Context context) {
        super();
        this.context = context;
        this.btnSure = btnSure;
        this.btnText = btnText;
    }





    public void RunTimer(){
        timer=new Timer();
        TimerTask task=new TimerTask() {

            @Override
            public void run(){
                time--;
                Message msg=handler.obtainMessage();
                msg.what=1;
                handler.sendMessage(msg);

            }
        };


        timer.schedule(task, 100, 1000);
    }


    private Handler handler =new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:

                    if(time>0){
                        btnSure.setEnabled(false);
                        btnSure.setText("已发送("+time+"s)");
                        btnSure.setTextSize(12);
                        btnSure.setTextColor(context.getResources().getColor(R.color._165165165));
                    }else{

                        timer.cancel();
                        btnSure.setText(btnText);
                        btnSure.setEnabled(true);
                        btnSure.setTextSize(12);
                        btnSure.setTextColor(context.getResources().getColor(R.color.gradient_end));
                    }

                    break;


                default:
                    break;
            }

        };
    };


    /**
     * 将时间戳(毫秒值)转换为时间
     */
    public static String stampToDate(String timeStr, String format) {
        if (TextUtils.isEmpty(timeStr) || "0".equals(timeStr))
            return null;
        long ltime = Long.parseLong(timeStr);
        Date date = new Date(ltime);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 将时间戳(毫秒值)转换为时间,如果是当天，使用时分，否则使用年月日
     *
     * @param timeStr
     * @return
     */
    public static String stampToDateWithDay(String timeStr) {
        if (TextUtils.isEmpty(timeStr) || "0".equals(timeStr))
            return null;
        long ltime = Long.parseLong(timeStr);
        Date date = new Date(ltime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(date);
        if (TextUtils.isEmpty(day))
            return null;
        if (sdf.format(new Date()).equals(day)) {
            // 是今天。
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            return sdfTime.format(date);
        } else {
            return day;
        }
    }


}
