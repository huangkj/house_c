package com.easylife.house.customer.ui.pub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
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
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.DialogUtil;

import butterknife.Bind;

/**
 * Created by Mars on 2017/3/21 11:29.
 * 描述：
 */

public class WebViewActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.webView)
    WebView webview;
    @Bind(R.id.btnAgree)
    Button btnAgree;
    private boolean isShowRight;
    MsgHeadLine messageBean = null;
    @Bind(R.id.wv_progressbar)
    ProgressBar wvProgressbar;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_activity_web, null);
    }

    public static void startActivity(Context activity, String title, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        activity.startActivity(new Intent(activity, WebViewActivity.class).putExtra("title", title).putExtra("mUrl", url));
    }

    public static void startActivity(Activity activity, String title, String url, boolean isShowRight, MsgHeadLine messageBean) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        activity.startActivity(new Intent(activity, WebViewActivity.class).putExtra("title", title).putExtra("mUrl", url).putExtra("IS_SHOW_RIGHT", isShowRight)
                .putExtra("messageBean", messageBean));
    }

    public static void startActivity(Activity activity, String title, String url, String bottomText) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        activity.startActivity(new Intent(activity, WebViewActivity.class).putExtra("title", title).putExtra("mUrl", url).putExtra("bottomText", bottomText));
    }

    private String mUrl;
    private String title;
    private String bottomText;

    @Override
    protected void initView() {
        title = getIntent().getStringExtra("title");
        mUrl = getIntent().getStringExtra("mUrl");
        bottomText = getIntent().getStringExtra("bottomText");
        isShowRight = getIntent().getBooleanExtra("IS_SHOW_RIGHT", false);
        messageBean = (MsgHeadLine) getIntent().getSerializableExtra("messageBean");

        tvTitle.setText(title);
        if (!TextUtils.isEmpty(bottomText)) {
            layActionBar.setVisibility(View.GONE);
            btnAgree.setVisibility(View.VISIBLE);
            btnAgree.setText(bottomText);
            btnAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


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

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);// 点击超链接的时候重新在原来进程上加载URL
                return true;
            }
        });
        // 设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebPlanJsInterface(this, mDao), "android");
        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//可以debug
            webview.setWebContentsDebuggingEnabled(true);
        }
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //接受证书
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(WebViewActivity.this.title) && view != null) {
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
        // 加载需要显示的网页
        webview.loadUrl(mUrl);

        if (isShowRight) {
            setOnShareListener(new OnShareListener() {
                @Override
                public void onShare() {
                    if (mDao.localDao.isLogin())
                        mDao.shareIntegration(1, WebViewActivity.this);
                }
            });
        }
    }

    @Override
    protected void setActionBarDetail() {
        if (isShowRight) {
            btnRight.setImageResource(R.mipmap.share);
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 分享固定显示icon
                    share(mUrl, messageBean.title + " - 好生活", Html.fromHtml(messageBean.text) + "", null);
                }
            });
        }

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
    public void onFail(NetBaseStatus code, int requestType) {

    }


}
