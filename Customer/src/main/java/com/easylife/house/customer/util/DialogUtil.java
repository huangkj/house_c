package com.easylife.house.customer.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.ui.mine.prize.PrizeDetailActivity;

/**
 *
 */
public class DialogUtil {

    private Context mContext;
    public Dialog mDialog_tip;
    private boolean mIs_customdialog;

    public DialogUtil(Context context) {
        mContext = context;
    }

    /**
     * 选择客户录入方式
     */
    @SuppressWarnings("deprecation")
    public PopupWindow getRecordMenuPop(final OnEventClickListener listener) {
        final View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.pop_menu_commission, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        contentView.findViewById(R.id.tvCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                listener.click(0);
            }
        });
        contentView.findViewById(R.id.tvDetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                listener.click(1);
            }
        });

        // 设置后子控件可点击
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                /**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    if (!((Activity) mContext).isFinishing()
                            && null != popupWindow && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        return popupWindow;
    }


    public Dialog getLoadingProgressDialog(boolean isCustom, View view,
                                           int styleId) {
        // MyLog.e("获取dialog", "=========>" + isCustom + "    " + styleId);
        if (isCustom && null != view) {
            Dialog dialog;
            if (styleId <= 0) {
                dialog = new Dialog(mContext);
            } else {
                dialog = new Dialog(mContext, styleId);
            }
            dialog.setContentView(view);
            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            mIs_customdialog = isCustom;
            return dialog;
        } else {
            ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("加载中...请稍后");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            mIs_customdialog = isCustom;
            return progressDialog;
        }
    }

    public boolean getLoadingProgressDialogState() {
        return mIs_customdialog;
    }

    public void showTipTakePhoto(final OnDialogTipListener onDialogStateListener) {

        mDialog_tip = new AlertDialog.Builder(mContext)
                .setTitle("请选择发布照片途径")
                .setCancelable(false)
                .setPositiveButton("手机拍照",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                onDialogStateListener.onOkBtn();
                            }
                        })
                .setNegativeButton("相册选择",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                onDialogStateListener.onCancelBtn();
                            }
                        }).create();
        mDialog_tip.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                onDialogStateListener.dismiss();
            }
        });
        mDialog_tip.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                onDialogStateListener.dismiss();
            }
        });
        mDialog_tip.setCancelable(true);
        mDialog_tip.setCanceledOnTouchOutside(true);
        if (!((Activity) mContext).isFinishing() && !mDialog_tip.isShowing()) {
            mDialog_tip.show();
        }

    }

    public void showTipDialog(final OnDialogTipListener onDialogStateListener,
                              String message) {
        mDialog_tip = new AlertDialog.Builder(mContext).setTitle("提示")
                .setMessage(message).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        onDialogStateListener.onOkBtn();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        onDialogStateListener.onCancelBtn();
                    }
                }).create();
        mDialog_tip.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                onDialogStateListener.dismiss();
            }
        });
        mDialog_tip.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                onDialogStateListener.dismiss();
            }
        });
        mDialog_tip.setCancelable(true);
        mDialog_tip.setCanceledOnTouchOutside(true);
        if (!((Activity) mContext).isFinishing() && !mDialog_tip.isShowing()) {
            mDialog_tip.show();
        }
    }

    /**
     * 自动帮你过滤dialog开启的条件
     */
    public static void showDialog(Context context, Dialog dialog) {
        if (!((Activity) context).isFinishing() && null != dialog
                && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 自动帮你过滤popwindow开启的条件
     */
    public static void showPopWindow(Context context, PopupWindow popupWindow,
                                     View anchor, int xoff, int yoff, int gravity) {
        if (!((Activity) context).isFinishing() && null != popupWindow
                && !popupWindow.isShowing()) {
            // MyLog.e("弹出菜单", "");
            popupWindow.showAsDropDown(anchor);
        }
    }

    /**
     * 自动帮你过滤dialog关闭的条件
     */
    public static void dismissDialog(Context context, Dialog dialog) {
        if (!((Activity) context).isFinishing() && null != dialog
                && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 自动帮你过滤popwindow开启的条件
     */
    public static void dismissPopWindow(Context context, PopupWindow popupWindow) {
        if (!((Activity) context).isFinishing() && null != popupWindow
                && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public interface OnDialogStateListener {
        void onDismiss();
    }

    // public interface OnDialogItemSelectListener {
    // void onCheckPosition(int index, FirstPartsPopuwindowdAdapter adapter);
    //
    // }

    public interface OnEventClickListener {
        void click(int position);
    }

    public interface OnDialogTipListener {
        void onOkBtn();

        void onCancelBtn();

        void dismiss();
    }


    public static void showTip(Context context, String tip) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.positiveText("确定");
        builder.content(tip);
        builder.show();
    }


}
