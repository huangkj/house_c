package com.easylife.house.customer.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IntentUtils {

    /**
     * 外置存储拍照请求
     */
    public static final int REQUEST_IMAGE_CAPTURE_OUTPUT = 0x101;

    /**
     * 拍照并保存到指定路径
     *
     * @param activity
     * @return 文件保存的路径uri
     */
    public static Uri takePhotoCustomerPath(Activity activity, String cacheDir, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String format = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        Uri uri = Uri.fromFile(new File(cacheDir + format + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
        return uri;
    }

    /**
     * 显示拨号提示框
     *
     * @param phone
     * @param context
     */
    public static void call(final String phone, final Context context) {
        if (TextUtils.isEmpty(phone)) {
            CustomerUtils.showTip(context, "手机号错误");
            return;
        }
        new AlertDialog.Builder(context).setTitle("提示")
                .setMessage("是否拨打 " + phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 发送短信
     *
     * @param context
     * @param phone
     */
    public static void sendMessage(Context context, String phone, String content) {
        if (TextUtils.isEmpty(phone)) {
            CustomerUtils.showTip(context, "手机号错误");
            return;
        }
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }
}
