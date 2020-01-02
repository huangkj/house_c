package com.easylife.house.customer.ui.mine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.UpdateBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.service.UpdateService;
import com.easylife.house.customer.ui.pub.AboutUsActivity;
import com.easylife.house.customer.ui.pub.LiabilityActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.ui.pub.updatepassword.UpdatePassWordActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.FileUtils;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.util.SettingsUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.config.ItemSelectManager.update_download_apk_name;

/**
 * Created by Mars on 2017/3/20 10:13.
 * 描述：设置
 */
@RuntimePermissions
public class SettingsActivity extends BaseActivity implements RequestManagerImpl {
    @Bind(R.id.cbPush)
    CheckBox cbPush;
    @Bind(R.id.btnClearCache)
    TextView btnClearCache;
    @Bind(R.id.btnAbout)
    TextView btnAbout;
    @Bind(R.id.btnHelp)
    TextView btnHelp;
    @Bind(R.id.btnUpdatePassWord)
    TextView btnUpdatePassWord;
    @Bind(R.id.btnStatement)
    TextView btnStatement;
    @Bind(R.id.btnVersion)
    TextView btnVersion;
    @Bind(R.id.tvVersion)
    TextView tvVersion;
    @Bind(R.id.layUpdatePassWord)
    LinearLayout layUpdatePassWord;
    @Bind(R.id.btnScore)
    TextView btnScore;

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, SettingsActivity.class), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_settings, null);
    }

    @Override
    protected void initView() {
        cbPush.setChecked(mDao.showPushMessage());

        tvVersion.setText("v" + SettingsUtil.getVersionName(activity));
    }

    @Override
    protected void setActionBarDetail() {
    }

    private PubTipDialog dialog;

    private void showTip() {
        if (dialog == null) {
            dialog = new PubTipDialog(this, new PubTipDialog.InsideListener() {

                @Override
                public void note(boolean isOK) {
                    Glide.getPhotoCacheDir(activity).delete();
                }
            });
        }
        dialog.showdialog("提示", "确定清除缓存吗？", null, "取消", "确定");
    }

    @OnClick({R.id.cbPush, R.id.btnClearCache, R.id.btnAbout, R.id.btnScore, R.id.btnHelp, R.id.btnUpdatePassWord, R.id.btnStatement, R.id.btnVersion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cbPush:
                mDao.saveShowPushMessage(cbPush.isChecked());
                break;
            //好生活评分
            case R.id.btnScore:
                String mAddress = "market://details?id=" + getPackageName();
                Intent marketIntent = new Intent("android.intent.action.VIEW");
                marketIntent.setData(Uri.parse(mAddress));
                startActivity(marketIntent);
                break;
            case R.id.btnClearCache:
                showTip();
                break;
            case R.id.btnAbout:
                startActivity(new Intent(activity, AboutUsActivity.class));
                break;
            case R.id.btnHelp:
                showCallTip(Constants.PHONE_HELP);
                break;
            case R.id.btnUpdatePassWord:
                if (!mDao.isLogin()) {
                    startActivityForResult(new Intent(activity, LoginByVerifyCodeActivity.class), 0);
                    return;
                }
                startActivity(new Intent(activity, UpdatePassWordActivity.class));
                break;
            case R.id.btnStatement:
                startActivity(new Intent(activity, LiabilityActivity.class));
                break;
            case R.id.btnVersion:
                mDao.updateAppVersion(1, this);
                break;
        }
    }

    /**
     * 显示拨号提示框
     *
     * @param phone
     */
    public void showCallTip(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            CustomerUtils.showTip(this, "手机号错误");
            return;
        }
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否拨打 " + phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SettingsActivityPermissionsDispatcher.callWithCheck(SettingsActivity.this, phone);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SettingsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    private UpdateBean updateBean;
    protected boolean isRegister;
    protected ProgressDialog updateProgressDialog;

    /**
     * @param apkUrl        下载地址
     * @param apkName       下载完成文件名
     * @param isForceUpdate 是否强制更新
     */
    public void downloadNewApk(String apkUrl, String apkName, boolean isForceUpdate) {
        //启动服务下载文件
        Intent intent = new Intent(this, UpdateService.class);
        intent.putExtra("updateUrl", apkUrl);
        intent.putExtra("apkName", apkName);
        intent.putExtra("isForceUpdate", isForceUpdate);
        startService(intent);
    }

    public void updateDialog(final String isForce) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.UpdateAlertDialogStyle);
        alertDialog.setTitle("新版本提示");
        alertDialog.setMessage("发现了新版本，更多惊喜在这里!\n\n版本号:" + updateBean.appVersion);
        alertDialog.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IntentFilter filter = new IntentFilter(Constants.UPDATE_SERVICES_PROGRESS);
                filter.setPriority(1000);
                registerReceiver(updateProgressReciver, filter);
                isRegister = true;//判断广播是否注册过  防止应用崩溃
                if ("1".equals(isForce)) {
                    showUpdateDialog();
                    downloadNewApk(updateBean.updataUrl, update_download_apk_name, true);
                } else {
                    downloadNewApk(updateBean.updataUrl, update_download_apk_name, false);
                }
            }
        });
        switch (isForce) {
            //强制更新
            case "1":
                alertDialog.setCancelable(false);
                break;
            //非强制更新
            case "2":
                alertDialog.setNegativeButton("取消", null);
                break;
        }


        alertDialog.show();

    }

    /**
     * 安装apk
     */
    private void installApk() {
        File file = new File(FileUtils.SDPATH, "goodLifeCustomer.apk");
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


    private void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                installApk();
                //安装应用的逻辑(写自己的就可以)
            } else {
                //设置安装未知应用来源的权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 10012);
            }
        } else {
            installApk();
        }
    }


    /**
     * 强制更新时，回传进度的广播
     */
    protected BroadcastReceiver updateProgressReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra("progress", -1);
            if (progress >= 100) {//下载完成
                if ("1".equals(updateBean.isforce)) {
                    updateProgressDialog.setProgress(100);
                    updateProgressDialog.dismiss();
                }
                checkIsAndroidO();
            } else if (progress == -1) {//更新出错
                updateProgressDialog.dismiss();
                CustomerUtils.showTip(getApplicationContext(), "更新出错，程序即将关闭，请稍后再试,或前往官网下载最新版apk。");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        clearAllActivitys();
                    }
                }, 3000);
            } else {//更新进度
                updateProgressDialog.setProgress(progress);
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10012) {
            checkIsAndroidO();
        }
    }

    public void showUpdateDialog() {
        updateProgressDialog = new ProgressDialog(this);
        updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        updateProgressDialog.setTitle("好生活升级");
        updateProgressDialog.setIndeterminate(false);
        updateProgressDialog.setCancelable(false);
        updateProgressDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegister) {
            unregisterReceiver(updateProgressReciver);
            isRegister = false;
        }

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        try {
            updateBean = new Gson().fromJson(response, UpdateBean.class);
            if (updateBean != null) {
                if (SettingsUtil.getVersionCode(activity) != 1) {
                    if (SettingsUtil.getVersionCode(activity) < updateBean.versionCode) {
                        updateDialog(updateBean.isforce);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
    }
}
