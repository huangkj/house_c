package com.easylife.house.customer.ui.pub;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.DialogUtil;
import com.easylife.house.customer.util.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.util.FileUtils.SDPATH;

/**
 * Created by Mars on 2017/3/21 11:29.
 * 描述：
 */
@RuntimePermissions
public class PubWebViewActivity extends BaseActivity implements RequestManagerImpl {


    //----------

    @Bind(R.id.webView)
    WebView webview;
    @Bind(R.id.btnAgree)
    Button btnAgree;
    private boolean isShowRight;
    MsgHeadLine messageBean = null;
    @Bind(R.id.wv_progressbar)
    ProgressBar wvProgressbar;
    private int type;
    public static final int HOUSE_TOPIC = 1; //楼盘专题
    public static final int GAME_ACTIVITY = 2; //游戏活动
    public static final int HOUSE_BRAND_HOPSON = 3; //品牌地产 合生
    public static final int HOUSE_BRAND_ZHUJIANG = 4; //品牌地产 珠江
    public static final int INSURANCE = 5; //保险
    public static final int AD = 6; //保险
    private WebPlanJsInterface webPlanJsInterface;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_activity_web, null);
    }

    public static void startActivity(Activity activity, String title, String url, int type) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        activity.startActivity(new Intent(activity, PubWebViewActivity.class).putExtra("title", title).putExtra("mUrl", url).putExtra("type", type));
    }


    private String mUrl;
    private String title;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == MessageEvent.RESTART_ACTIVITY_GAME && type == GAME_ACTIVITY) {//填完用户资料刷新游戏活动
            webview.reload();
        } else if (event.MsgType == MessageEvent.LOGIN_STATE_CHANGE && type == GAME_ACTIVITY) {//登录成功刷新游戏活动
            webview.reload();
        }
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        title = getIntent().getStringExtra("title");
        mUrl = getIntent().getStringExtra("mUrl");
        type = getIntent().getIntExtra("type", 0);


        tvTitle.setText(title);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack();
            }
        });

        webview.requestFocusFromTouch();
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                cancleLoading();
            }


            @Override
            public void doUpdateVisitedHistory(WebView view, String url,
                                               boolean isReload) {
                if (wvProgressbar != null)
                    wvProgressbar.setVisibility(View.GONE);
                super.doUpdateVisitedHistory(view, url, isReload);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 处理自定义scheme
                if (!url.startsWith("http")) {
//                    Log.i("shouldOverrideUrlLoading", "处理自定义scheme");
//                    Toast.makeText(PubWebViewActivity.this, "需要下载客户端收看", Toast.LENGTH_LONG)
//                            .show();
                    try {
                        // 以下固定写法
                        final Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PubWebViewActivity.this.startActivity(intent);
                    } catch (Exception e) {
                        // 防止没有安装的情况
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;


            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //接受证书
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        webPlanJsInterface = new WebPlanJsInterface(this, mDao);
        // 设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(webPlanJsInterface, "android");
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
/*
DOM storage 是HTML5提供的一种标准接口，主要将键值对存储在本地，在页面加载完毕后可以通过JavaScript来操作这些数据，但是Android 默认是没有开启这个功能的，则导致H5页面加载失败，错误日志：
[INFO:CONSOLE(186)] "Uncaught TypeError: Cannot read property 'getItem' of null"
* */
        webview.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//可以debug
            webview.setWebContentsDebuggingEnabled(true);
        }

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(PubWebViewActivity.this.title) && view != null) {
                    tvTitle.setText(title);
                }
            }

            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                if (wvProgressbar != null) {
                    wvProgressbar.setMax(100);
                    if (progress < 100) {
                        wvProgressbar.setVisibility(View.VISIBLE);
                        if (progress < 10) {
                            wvProgressbar.setProgress(10);
                        } else {
                            wvProgressbar.setProgress(progress);
                        }
                    } else {
                        wvProgressbar.setProgress(100);
                        wvProgressbar.setVisibility(View.GONE);
                    }
                }
            }
        });
        initType();
        Log.d(TAG, "url:" + mUrl);
        // 加载需要显示的网页
    }

    private void initType() {
        switch (type) {
            case HOUSE_TOPIC:
                //楼盘专题页
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setImageResource(R.mipmap.share);
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        webview.post(new Runnable() {
                            @Override
                            public void run() {
                                webview.evaluateJavascript("javascript:getThematicTitle()", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        if (!TextUtils.isEmpty(value) && value.contains("$")) {
                                            String[] titles = value.split("\\$");
                                            Log.d("###", "value:" + value + "titles:" + titles[0] + "  -------" + titles[1]);
                                            share(mUrl, titles[0], titles[1], "");
                                        }
                                    }
                                });
//                                webview.loadUrl("javascript:getThematicTitle()");
                            }
                        });
                    }
                });
                webview.loadUrl(mUrl + "&isApp=1");
                break;

            case GAME_ACTIVITY://游戏活动
                webview.loadUrl(mUrl);
                break;

            case HOUSE_BRAND_HOPSON:
                webview.loadUrl(mUrl);
                break;
            case INSURANCE://保险
                webview.loadUrl(mUrl);
                break;
            case HOUSE_BRAND_ZHUJIANG:
                webPlanJsInterface.setSaveImageCallBack(new WebPlanJsInterface.SaveImageCallBack() {
                    @Override
                    public void saveImageCallBack(String url) {
                        PubWebViewActivityPermissionsDispatcher.saveImgWithCheck(PubWebViewActivity.this, url);
                    }
                });
                webview.loadUrl(mUrl);
                break;

            default:
                webview.loadUrl(mUrl);
                break;
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void saveImg(final String url) {
        String imgName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        FileUtils.createDirs();
        final File file = new File(SDPATH, imgName);
        new Thread(
        ) {
            @Override
            public void run() {
                File img = null;
                try {
                    img = Glide.with(PubWebViewActivity.this).load(url).downloadOnly(210, 210).get();
                    com.blankj.utilcode.util.FileUtils.copyFile(img, file);
                    MediaStore.Images.Media.insertImage(PubWebViewActivity.this.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
                    Uri uri = Uri.fromFile(file);
                    PubWebViewActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        ((Activity) this).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showLong("图片已保存到" + SDPATH);
            }
        });


    }


    @Override
    protected void setActionBarDetail() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            btnBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void btnBack() {
        if (webview != null && webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                DialogUtil.showTip(this, response);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }


    /**
     * 6.0权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PubWebViewActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
