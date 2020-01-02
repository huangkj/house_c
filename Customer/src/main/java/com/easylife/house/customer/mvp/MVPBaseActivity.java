package com.easylife.house.customer.mvp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easylife.house.customer.R;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.util.InputTools;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.NetworkState;
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

import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * @param <V>
 * @param <T>
 */
public abstract class MVPBaseActivity<V extends BaseView, T extends BasePresenterImpl<V>> extends FragmentActivity implements BaseView {
    protected ImageView imgBack;
    protected ImageView btnRight;
    protected TextView btnRightText;
    protected TextView tvTitle;
    protected View actionBar;
    protected ViewGroup contentLayout;
    protected RelativeLayout rlNoNet;
    protected Button btnTry;

    private String key;
    protected Activity activity;

    protected ServerDao dao;


    protected abstract View setContentLayoutView();

    protected abstract void initView();

    protected abstract void setActionBarDetail();

    protected abstract void tryRequestData();

    public static Map<String, Activity> activitys = new LinkedHashMap<String, Activity>();

    protected String TAG = this.getClass().getSimpleName();

    protected Bundle savedInstanceState;

    public T mPresenter;
    public AlertDialog dialog;
    protected int page = Constants.PAGE_START;

    private OnShareListener mOnShareListener;

    public interface OnShareListener {
        void onShare();
    }

    public void setOnShareListener(OnShareListener l) {
        mOnShareListener = l;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = setContentLayoutView();
        setContentView(R.layout.pub_activity_base);
        mPresenter = getInstance(this, 1);
        mPresenter.attachView((V) this);

        activity = this;
        this.savedInstanceState = savedInstanceState;
        dao = new ServerDao(this);
        initActionBar();

        LogOut.d(TAG, " onCreate ");

        if (contentView != null) {
            contentLayout.addView(contentView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            ButterKnife.bind(this);
            checkNetWork();
            initView();
            setActionBarDetail();
        }
        activitys.put(this.getClass().getName(), this);


    }

    /**
     * 检查是否有网
     */
    public void checkNetWork() {
        if (NetworkState.networkConnected(this)) {
            rlNoNet.setVisibility(View.GONE);
        } else {
            if (dialog != null && dialog.isShowing()) {
                cancelLoading();
            }
            rlNoNet.setVisibility(View.VISIBLE);
            btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tryRequestData();
                    if (NetworkState.networkConnected(MVPBaseActivity.this)) {
                        rlNoNet.setVisibility(View.GONE);
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            cancelLoading();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        activitys.remove(this.getClass().getName());
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public void showLoading() {
        showProgress();
    }

    @Override
    public void cancelLoading() {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initActionBar() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnRight = (ImageView) findViewById(R.id.btnRight);
        btnRightText = (TextView) findViewById(R.id.btnRightText);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        actionBar = findViewById(R.id.layActionBar);
        contentLayout = (ViewGroup) findViewById(R.id.container);
        rlNoNet = (RelativeLayout) findViewById(R.id.rl_no_net);
        btnTry = (Button) findViewById(R.id.btn_no_net_try);
        tvTitle.setText(getTitle());
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void hideNoNetView() {
        rlNoNet.setVisibility(View.GONE);
    }

    public void showProgress() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).create();
        }
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.progress_dialog);
        window.setDimAmount(0);
        dialog.setOnKeyListener(keylistener);
        SpinKitView progress = (SpinKitView) window.findViewById(R.id.progressBar);
        Circle circle = new Circle();
        progress.setIndeterminateDrawable(circle);
        dialog.setCanceledOnTouchOutside(false);
        progress.setBackgroundColor(getResources().getColor(R.color.transparent));
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

    protected void exit() {
        clearAllActivitys();
    }

    @Override
    public void finish() {
        InputTools.HideKeyboard(tvTitle);
        super.finish();
    }


    @Override
    public Context getContext() {
        return this;
    }

    public <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                finish();
                return false;
            } else {
                return false;
            }
        }
    };


    /**
     * 友盟分享
     *
     * @param url    地址
     * @param title  标题
     * @param desc   描述
     * @param imgUrl 图片地址
     */
    public void share(final String url, final String title, final String devName, String titleDesc, final String desc, String imgUrl) {

        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        UMImage image = null;
        if (!TextUtils.isEmpty(imgUrl)) {
            image = new UMImage(getApplicationContext(), imgUrl);//资源文件
        } else {
            image = new UMImage(getApplicationContext(), R.mipmap.ic_launcher);//资源文件
        }
        image.compressStyle = UMImage.CompressStyle.SCALE;
        UMWeb web = new UMWeb(url);
        web.setTitle(title + devName + titleDesc);//标题
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
                        Toast.makeText(MVPBaseActivity.this, "复制成功", Toast.LENGTH_LONG).show();
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(url);
                    }
                } else {
                    if (snsPlatform.mPlatform == SHARE_MEDIA.SMS) {
                        Intent intentFinalMessage = new Intent(Intent.ACTION_VIEW);
                        intentFinalMessage.setType("vnd.android-dir/mms-sms");
                        intentFinalMessage.putExtra("sms_body", "这个楼盘不错哦，" + devName + title + url);
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
            //分享开始的回调
            if (mOnShareListener != null) {
                mOnShareListener.onShare();
            }
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            Toast.makeText(getApplicationContext(), " 分享成功啦", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), " 取消分享", Toast.LENGTH_SHORT).show();
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
