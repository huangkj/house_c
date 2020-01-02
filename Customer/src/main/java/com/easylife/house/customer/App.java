package com.easylife.house.customer;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.os.StrictMode;
import android.util.DisplayMetrics;

import com.easylife.house.customer.config.ConstantsKeys;
import com.easylife.house.customer.service.CrashHandler;
import com.easylife.house.customer.service.GeTuiIntentService;
import com.easylife.house.customer.service.GeTuiPushService;
import com.easylife.house.customer.util.CustomerUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.igexin.sdk.PushManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuch.adlibrary.utils.DisplayUtil;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import static com.easylife.house.customer.config.ConstantsKeys.QQ_APPID;
import static com.easylife.house.customer.config.ConstantsKeys.QQ_APPKEY;
import static com.easylife.house.customer.config.ConstantsKeys.WX_APPID;
import static com.easylife.house.customer.config.ConstantsKeys.WX_APPSECRET;
import static com.easylife.house.customer.config.ConstantsKeys.Weibo_APPID;
import static com.easylife.house.customer.config.ConstantsKeys.Weibo_APPSECRET;
import static com.easylife.house.customer.config.ConstantsKeys.Weibo_RedirectUrl;

public class App extends Application {

    private static App instance;
    public static Context applicationContext;
    public static Handler mMainThreadHandler;
    public static int mMainThreadId;
    public static String CLIENT_ID = "";

    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this); 5.0不需要
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationContext = this;
        this.mMainThreadHandler = new Handler();
        this.mMainThreadId = Process.myTid();

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        Config.DEBUG = false;
        new Thread(
        ) {
            @Override
            public void run() {
                super.run();
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                initCrash();
                ZXingLibrary.initDisplayOpinion(App.this);

                Config.DEBUG = false;
                UMShareAPI.get(App.this);

                initUmeng();
                initGeTui();
                //初始化环信
//                LifeHelper.getInstance().init(applicationContext);

                //  错误日志捕获以及保存到本地
                CrashHandler crashHandler = CrashHandler.getInstance();
                crashHandler.init(getApplicationContext());

                IWXAPI msgApi = WXAPIFactory.createWXAPI(App.this, null);
                msgApi.registerApp(ConstantsKeys.WX_APPID);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                }

                initAdDialog();
            }
        }.start();


    }


    private void initAdDialog() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
        Fresco.initialize(this);
    }

    /**
     * bugly崩溃统计
     */
    public void initCrash() {
        // 获取当前包名
        String packageName = applicationContext.getPackageName();
        // 获取当前进程名
        String processName = CustomerUtils.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(applicationContext);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //第三个参数  是否打开debug模式 崩溃信息
        CrashReport.initCrashReport(getApplicationContext(), ConstantsKeys.BUGLY_ID, false);
    }

    private void initGeTui() {
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiPushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);
    }

    private void initUmeng() {

//初始化友盟统计
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(false);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        PlatformConfig.setWeixin(WX_APPID, WX_APPSECRET);
        PlatformConfig.setQQZone(QQ_APPID, QQ_APPKEY);
        PlatformConfig.setSinaWeibo(Weibo_APPID, Weibo_APPSECRET, Weibo_RedirectUrl);

    }

    // 对外暴露主线程id
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    // 对外暴露主线程的handler
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static App getInstance() {
        return instance;
    }
}
