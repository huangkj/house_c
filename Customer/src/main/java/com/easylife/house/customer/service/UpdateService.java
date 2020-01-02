package com.easylife.house.customer.service;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.easylife.house.customer.BuildConfig;
import com.easylife.house.customer.R;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DownLoaderUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateService extends Service {

    private OkHttpClient mOkhttpClient;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private static final String TAG = "UpdateServiceApp";

    public UpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String url = intent.getStringExtra("updateUrl");
        final String apkName = intent.getStringExtra("apkName");
        final boolean isForceUpdate = intent.getBooleanExtra("isForceUpdate", false);

        mOkhttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        if (!isForceUpdate) {
            mNotifyManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);


            String channelId = "other";
            String channelName = "其他";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(channelId, channelName, importance);
            }

            mBuilder = new NotificationCompat.Builder(this, channelId);
            mBuilder.setContentTitle("好生活升级")
                    .setContentText("准备中")
                    .setSmallIcon(R.mipmap.ic_launcher);
            mNotifyManager.notify(0, mBuilder.build());
        }


        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (isForceUpdate) {
                    Intent intent = new Intent(Constants.UPDATE_SERVICES_PROGRESS);
                    intent.putExtra("progress", -1);
                    sendBroadcast(intent);
                } else {
                    mNotifyManager.cancel(0);
                    CustomerUtils.showTip(getApplicationContext(), "更新失败，请稍后重试");
                }
                stopSelf();

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            DownLoaderUtil.writeResponseBodyToDisk(response.body(), apkName, new DownLoaderUtil.DownLoaderProgreessListener() {
                                long currentTime = 0;
                                long preTime = 0;

                                @Override
                                public void progreess(int progreess) {
                                    Log.d(TAG, "progreess:" + progreess);
                                    currentTime = System.currentTimeMillis();
                                    if (currentTime - preTime > 1000) {//每秒更新一次进度
                                        if (isForceUpdate) {
                                            //广播通知进度
                                            Intent intent = new Intent(Constants.UPDATE_SERVICES_PROGRESS);
                                            intent.putExtra("progress", progreess);
                                            sendBroadcast(intent);
                                        } else {
                                            mBuilder.setProgress(100, progreess, false);
                                            mBuilder.setContentText("下载进度：" + progreess + "%");
                                            mNotifyManager.notify(0, mBuilder.build());
                                        }
                                        preTime = System.currentTimeMillis();
                                    }
                                }

                                @Override
                                public void complete(File file) {
//                                    installApk( file);
                                    Intent intent = new Intent(Constants.UPDATE_SERVICES_PROGRESS);
                                    intent.putExtra("progress", 100);
                                    sendBroadcast(intent);

                                    if (isForceUpdate) {
//                                        Intent intent = new Intent(Constants.UPDATE_SERVICES_PROGRESS);
//                                        intent.putExtra("progress", 100);
//                                        sendBroadcast(intent);
                                    } else {
                                        mBuilder.setContentText("下载完成")
                                                .setProgress(0, 0, false);
                                        mNotifyManager.notify(0, mBuilder.build());
                                        mNotifyManager.cancel(0);
                                    }
                                }
                            });
                        } else {
                            if (isForceUpdate) {
                                Intent intent = new Intent(Constants.UPDATE_SERVICES_PROGRESS);
                                intent.putExtra("progress", -1);
                                sendBroadcast(intent);
                            } else {
                                CustomerUtils.runInMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CustomerUtils.showTip(getApplicationContext(), "更新失败，请稍后重试");
                                        Log.e(TAG, "errorBody:" + response.body());
                                        Log.e(TAG, "message:" + response.message());
                                    }
                                });
                                mNotifyManager.cancel(0);
                            }
                        }
                        stopSelf();
                    }
                }).start();
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }


    public static void installApk(Context context, File apkFile) {

      /*  Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);*/


/*        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri apkUri = null;
            //判断版本是否是 7.0 及 7.0 以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                apkUri = FileProvider.getUriForFile(context, "com.easylife.house.customer.fileprovider", apkFile);
                //添加对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                apkUri = Uri.fromFile(apkFile);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(apkUri,
                    "application/vnd.android.package-archive");
            //查询所有符合 intent 跳转目标应用类型的应用，注意此方法必须放置setDataAndType的方法之后
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            //然后全部授权
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, apkUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }


    /**
     * 安装apk
     */
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d("installApk", "版本大于 N ，开始使用 fileProvider 进行安装");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.easylife.house.customer.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.d("installApk", "正常进行安装");

            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        mNotifyManager.createNotificationChannel(channel);
    }


}

