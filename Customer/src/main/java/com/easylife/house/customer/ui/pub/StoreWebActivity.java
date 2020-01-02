package com.easylife.house.customer.ui.pub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.LogUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by Mars on 2017/3/21 11:29.
 * 描述： 积分商城web
 */

public class StoreWebActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.webView)
    WebView webview;
    @Bind(R.id.btnAgree)
    Button btnAgree;
    @Bind(R.id.wv_progressbar)
    ProgressBar wvProgressbar;

    private String mUrl;
    private String title;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_activity_web, null);
    }

    public static void startActivity(Activity activity, String title, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        activity.startActivity(new Intent(activity, StoreWebActivity.class).putExtra("title", title).putExtra("mUrl", url));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == MessageEvent.LOGIN_STATE_CHANGE) {//登录成功
            webview.reload();
//            Customer customer = mDao.getCustomer();
//            String url = Constants.WEB_INTEGRAL_STORE + "?userId=" + customer.id + "&userName=" + customer.username + "&userPhone=" + customer.phone;
//            Log.d("@@",url);
//            webview.clearHistory();
//            webview.loadUrl(url);
        }
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        title = getIntent().getStringExtra("title");
        mUrl = getIntent().getStringExtra("mUrl");

        tvTitle.setText(title);
        webview.requestFocusFromTouch();
        // 设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebPlanJsInterface(this, mDao), "android");
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//可以debug
            webview.setWebContentsDebuggingEnabled(true);
        }
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //接受证书
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.dTag(TAG, "onPageStarted url:" + url);
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
                LogUtils.dTag(TAG, "shouldOverrideUrlLoading url:" + url);
                webview.loadUrl(url);// 点击超链接的时候重新在原来进程上加载URL
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.dTag(TAG, "onPageFinished url:" + url);
                cancleLoading();
                if (url.contains("#/mall")) {
                    btnRightText.setVisibility(View.VISIBLE);
                } else {
                    btnRightText.setVisibility(View.GONE);
                }
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {

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


            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(title)) {
                    tvTitle.setText(title);
                }
                LogUtils.dTag(TAG, "onReceivedTitle title:" + title);
            }
        });


        // 加载需要显示的网页
        webview.loadUrl(mUrl);

        setOnShareListener(new OnShareListener() {
            @Override
            public void onShare() {
                if (mDao.localDao.isLogin())
                    mDao.shareIntegration(0, StoreWebActivity.this);
            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack();
            }
        });

    }

    @Override
    protected void setActionBarDetail() {
        btnRightText.setText("兑换记录");
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDao.isLogin()) {
                    Customer customer = mDao.localDao.getCustomer();
                    webview.loadUrl(Constants.WEB_STORE_CONVERSION + "?userId=" + customer.id + "&userName=" + customer.username + "&userPhone=" + customer.phone);
                } else {
                    startActivityForResult(new Intent(StoreWebActivity.this, LoginByVerifyCodeActivity.class), 0);
                }
            }
        });
    }

    @Override
    public void onSuccess(String response, int requestType) {
        DialogUtil.showTip(this, response);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}


