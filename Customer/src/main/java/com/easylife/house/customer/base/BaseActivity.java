package com.easylife.house.customer.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easylife.house.customer.R;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.util.InputTools;
import com.easylife.house.customer.util.LogOut;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Created by Mars on 2017/3/14 18:42.
 * 描述：Activity基类
 * 描述：Activity基类
 */

public abstract class BaseActivity extends FragmentActivity {

    protected ImageView imgBack;
    protected ImageView btnRight;
    protected TextView tvTitle;
    protected TextView btnRightText;
    protected View layActionBar;
    private ViewGroup contentLayout;

    private String key;
    protected Activity activity;
    protected ServerDao mDao;
    private AlertDialog dialog;

    protected abstract View setContentLayoutView();

    protected abstract void initView();

    protected abstract void setActionBarDetail();

    public static Map<String, Activity> activitys = new LinkedHashMap<String, Activity>();

    protected String TAG = this.getClass().getSimpleName();

    private OnShareListener mOnShareListener;

    public interface OnShareListener {
        void onShare();
    }

    public void setOnShareListener(OnShareListener l) {
        mOnShareListener = l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pub_activity_base);

        activity = this;
        mDao = new ServerDao(this);
        initActionBar();

        LogOut.d(TAG, " onCreate ");

        View contentView = setContentLayoutView();
        if (contentView != null) {
            contentLayout.addView(contentView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            ButterKnife.bind(this);
            initView();
            setActionBarDetail();
        }

        activitys.put(this.getClass().getName(), this);
    }

    private void initActionBar() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnRight = (ImageView) findViewById(R.id.btnRight);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnRightText = (TextView) findViewById(R.id.btnRightText);
        layActionBar = findViewById(R.id.layActionBar);
        contentLayout = (ViewGroup) findViewById(R.id.container);

        tvTitle.setText(getTitle());
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        InputTools.HideKeyboard(tvTitle);
        super.finish();
    }

    /**
     * 清除所有的活动的activity
     */
    protected void clearAllActivitys() {
        Set<Map.Entry<String, Activity>> entrySet = activitys.entrySet();
        for (Map.Entry<String, Activity> entry : entrySet) {
            Activity value = entry.getValue();
            if (!value.isFinishing()) {
                value.finish();
            }
        }
        activitys.clear();
    }

    public void showLoading() {
        showProgress();
    }

    public void cancleLoading() {
        if (dialog != null)
            dialog.dismiss();
    }

    public void showProgress() {
        dialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.progress_dialog);
        window.setDimAmount(0);
        SpinKitView progress = (SpinKitView) window.findViewById(R.id.progressBar);
        Circle circle = new Circle();
        progress.setIndeterminateDrawable(circle);
        dialog.setCanceledOnTouchOutside(false);
        progress.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    protected void exit() {
        clearAllActivitys();
    }

    @Override
    protected void onDestroy() {

        ButterKnife.unbind(this);
        activitys.remove(this.getClass().getName());
        super.onDestroy();

    }


    /**
     * 友盟分享
     *
     * @param url    地址
     * @param title  标题
     * @param desc   描述
     * @param imgUrl 图片地址
     */
    public void share(final String url, final String title, final String desc, String imgUrl) {

        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        UMImage image = null;
        if (!TextUtils.isEmpty(imgUrl)) {
            image = new UMImage(getApplicationContext(), imgUrl);//资源文件
        } else {
            image = new UMImage(getApplicationContext(), R.mipmap.icon_share);//资源文件
        }
        image.compressStyle = UMImage.CompressStyle.SCALE;
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(desc);//描述


        final ShareAction shareAction = new ShareAction(this)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SMS)
                .addButton("umeng_sharebutton_copy", "umeng_sharebutton_copy", "umeng_socialize_copy", "umeng_socialize_copy");


        shareAction.setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media == null) {
                    if (snsPlatform.mKeyword.equals("umeng_sharebutton_copy")) {
                        Toast.makeText(BaseActivity.this, "复制成功", Toast.LENGTH_LONG).show();
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(url);
                    }
                } else {
                    if (snsPlatform.mPlatform == SHARE_MEDIA.SMS) {
                        Intent intentFinalMessage = new Intent(Intent.ACTION_VIEW);
                        intentFinalMessage.setType("vnd.android-dir/mms-sms");
                        intentFinalMessage.putExtra("sms_body", title + url);
                        startActivity(intentFinalMessage);
                    } else {
                        shareAction.setPlatform(share_media).setCallback(umShareListener)
                                .share();
                    }
                }
            }
        });

        //分享面板配置
        ShareBoardConfig config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        config.setTitleVisibility(false);
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
        config.setCancelButtonVisibility(true);

        //打开分享面板
        shareAction.open(config);


    }


    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA platform) {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mOnShareListener != null) {
                        mOnShareListener.onShare();
                    }
                }
            }, 1500);


            //分享开始的回调
//            Toast.makeText(getApplicationContext(), " 分享onStart", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
//            Toast.makeText(getApplicationContext(), " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getApplicationContext(), " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(getApplicationContext(), " 取消分享", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(activity.getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(activity.getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }
}