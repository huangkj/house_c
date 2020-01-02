package com.easylife.house.customer.ui.pub;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.DialogUtil;

import butterknife.Bind;

/**
 * Created by Mars on 2017/3/21 11:29.
 * 描述：
 */

public class ShopRentDetailWebActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.webView)
    WebView webview;
    @Bind(R.id.btnAgree)
    Button btnAgree;
    @Bind(R.id.wv_progressbar)
    ProgressBar wvProgressbar;
    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_activity_web, null);
    }

    public static void startActivity(Activity activity, String title, String url, String devName) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        activity.startActivity(new Intent(activity, ShopRentDetailWebActivity.class).putExtra("title", title).putExtra("mUrl", url)
                .putExtra("devName", devName));
    }


    public static void startActivity(Activity activity, String title, String url, boolean isShowRight, MsgHeadLine messageBean) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        activity.startActivity(new Intent(activity, ShopRentDetailWebActivity.class).putExtra("title", title).putExtra("mUrl", url).putExtra("IS_SHOW_RIGHT", isShowRight)
                .putExtra("messageBean", messageBean));
    }


    private String mUrl;
    private String title;
    private String devName;

    @Override
    protected void initView() {
        title = getIntent().getStringExtra("title");
        mUrl = getIntent().getStringExtra("mUrl");
        devName = getIntent().getStringExtra("devName");

        tvTitle.setText(title);
        webview.requestFocusFromTouch();
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                cancleLoading();
                if (TextUtils.isEmpty(title) && view != null) {
                    tvTitle.setText(view.getTitle());
                }
            }
        });
        // 设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebPlanJsInterface(this), "android");
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
            public void doUpdateVisitedHistory(WebView view, String url,
                                               boolean isReload) {
                if (wvProgressbar != null)
                    wvProgressbar.setVisibility(View.GONE);
                super.doUpdateVisitedHistory(view, url, isReload);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);// 点击超链接的时候重新在原来进程上加载URL
                return true;
            }

        });

        webview.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                if (wvProgressbar == null) {
                    return;
                }
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
        });


        // 加载需要显示的网页
        webview.loadUrl(mUrl);


        setOnShareListener(new BaseActivity.OnShareListener() {
            @Override
            public void onShare() {
                if (mDao.localDao.isLogin())
                    mDao.shareIntegration(0, ShopRentDetailWebActivity.this);
            }
        });
    }

    @Override
    protected void setActionBarDetail() {
        btnRight.setImageResource(R.mipmap.share);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 分享固定显示icon
                share(mUrl, "商铺详情", devName, null);
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
}


