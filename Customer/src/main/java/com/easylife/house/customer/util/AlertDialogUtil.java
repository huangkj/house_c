package com.easylife.house.customer.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.easylife.house.customer.R;


public class AlertDialogUtil {
    /**
     * 获取弹出的对话框
     *
     * @param context
     * @param view
     * @return
     */
    public static Dialog getAlertDialog(Context context, View view) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        view.setMinimumWidth(display.getWidth());

        Dialog alertDialog = new Dialog(context, R.style.dialogStyle);
        alertDialog.setContentView(view);
        Window dialogWindow = alertDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = display.getWidth() - DimenUtils.dip2px(context, 50) * 2;
        lp.height = display.getHeight() - DimenUtils.dip2px(context, 150) * 2;
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return alertDialog;
    }
}
