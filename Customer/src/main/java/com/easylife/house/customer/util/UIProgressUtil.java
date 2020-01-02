package com.easylife.house.customer.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.easylife.house.customer.R;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/27/027.
 */

public class UIProgressUtil {
    protected static AlertDialog alert;
    protected static Dialog dialog;
    private static TextView tvProgress;

    public static void showProgressDialog(Context ctx) {
        showProgressDialogWithTxt(ctx, null);
    }

    @SuppressLint("InflateParams")
    public static void showProgressDialogWithTxt(Context ctx, String msg) {
        if (dialog == null) {
            try {
                dialog = new AlertDialog.Builder(ctx, R.style.MyProgressBar).create();
                dialog.show();
                LayoutInflater inflater = (LayoutInflater) ctx
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(
                        R.layout.pub_loading_process_dialog_anim, null);
                tvProgress = (TextView) view.findViewById(R.id.tvProgress);
                dialog.setContentView(view);
                dialog.setCanceledOnTouchOutside(false);
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(msg)) {
            tvProgress.setText(msg);
        } else {
            tvProgress.setText("");
        }
        if (!dialog.isShowing()) {
            try {
                dialog.show();
            } catch (Exception e) {
                LogOut.d("UIUtil", "网络数据获取进度框显示失败");
            }
        }
    }

    public static void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     *
     * @param context
     * @param title
     * @param items
     * @param listener
     */
    public static void showAlertDialog(Context context, String title,
                                       String[] items, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context).setItems(items, listener).show();
    }

    /**
     *
     * @param context
     * @param title
     * @param message
     * @param listener
     */
    public static void showAlertDialog(Context context, String title,
                                       String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        builder.setNegativeButton("取消", null).setPositiveButton("确定", listener)
                .show();
    }

    /**
     * 只有确定按钮
     * @param context
     * @param title
     * @param positiveButtonTitle 确定按钮的显示内容，为空时，默认显示”确定“
     * @param message
     * @param listener
     */
    public static void showAlertDialogOnlyOk(Context context, String title,
                                             String positiveButtonTitle, String message,
                                             DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (TextUtils.isEmpty(positiveButtonTitle)) {
            builder.setPositiveButton("确定", listener).show();
        } else {
            builder.setPositiveButton(positiveButtonTitle, listener).show();
        }

    }

    /**
     *
     * @param context
     * @param title
     * @param listener
     */
    public static void showAlertDialogOk(Context context, String title,
                                         String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        builder.setPositiveButton("确定", listener).show();
    }

    public static void hideInput(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void closeAlert() {
        if (null != alert)
            alert.cancel();
        alert = null;
    }

    private static int noticeIndex;// 通知次数

    public static void clearNotificationMsg(Context ctx) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) ctx
                .getSystemService(ns);
        mNotificationManager.cancel(noticeIndex);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public static void notificationMsg(Context ctx, String title,
                                       String message, boolean isNew) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) ctx
                .getSystemService(ns);

        int icon = R.mipmap.ic_launcher;
        CharSequence tickerText = title;
        long when = System.currentTimeMillis();
//		Notification notification = new Notification(icon, tickerText, when);
        Notification.Builder notification = new Notification.Builder(ctx);
        notification.setSmallIcon(icon);
        notification.setContentText(tickerText);
        notification.setWhen(when);
        Context context = ctx.getApplicationContext();


        // CharSequence contentTitle = message;

        // 设置跳转
        // Intent notificationIntent = new Intent(ctx, MainActivity.class);
        // PendingIntent contentIntent = PendingIntent.getActivity(ctx,
        // 0,notificationIntent, 0);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // intent.setClass(ctx, MainActivity.class);
        // intent.setComponent(new ComponentName(ctx.getPackageName(),
        // ctx.getPackageName() + "." + ctx.getLocalClassName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        Context mContext = ctx.getApplicationContext();
        PendingIntent contextIntent = PendingIntent.getActivity(mContext, 0,
                intent, 0);

//		notification.setLatestEventInfo(context, title, message, contextIntent);
        notification.setContentTitle(title);
        notification.setContentText(message);
        notification.setContentIntent(contextIntent);
        notification.build().flags|= Notification.FLAG_AUTO_CANCEL;
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
        if (isNew)
            mNotificationManager.notify(noticeIndex++, notification.build());
        else
            mNotificationManager.notify(noticeIndex, notification.build());
    }

    public interface TimePicker {
        public void pickTime(String time);
    }

    public interface MulitySelecter {
        public void selectOver(HashMap<Integer, Boolean> isSelected);
    }

    public static boolean isEmptyString(String str) {
        return null == str || "".equals(str);
    }
}
