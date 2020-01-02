/*
 * Copyright 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easylife.house.customer.ui.pub;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.deange.ropeprogressview.RopeProgressBar;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.MainPagerAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.AdBean;
import com.easylife.house.customer.bean.AdListBean;
import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.bean.UpdateBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.dao.LocalDao;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.service.UpdateService;
import com.easylife.house.customer.ui.homefragment.MessageFragment;
import com.easylife.house.customer.ui.homefragment.homeindexv3.HomeIndexFragment;
import com.easylife.house.customer.ui.homefragment.homelookhouse.HomeLookHouseFragment;
import com.easylife.house.customer.ui.homefragment.homemine.HomeMineFragment;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.FileUtils;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.util.InstallUtil;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.SettingsUtil;
import com.easylife.house.customer.view.NoScrollViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.transformer.DepthPageTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.config.ItemSelectManager.update_download_apk_name;
import static com.easylife.house.customer.dao.LocalDao.FOLDER_CACHE_PUB;
import static com.easylife.house.customer.dao.LocalDao.KEY_PUB_AD_DIALOG_COUNT;
import static com.easylife.house.customer.dao.LocalDao.KEY_UN_READ_MSG_COUNT;
import static com.easylife.house.customer.event.MessageEvent.LOOK_HOUSE_CHANGE_SEE;
import static com.easylife.house.customer.event.MessageEvent.PUSH_MSG;
import static com.easylife.house.customer.event.MessageEvent.PUSH_MSG_REFRESH;

/**
 * Created by Mars on 2017/3/15 16:23.
 * 描述：首页
 */
@RuntimePermissions
public class MainActivity extends BaseActivity implements RequestManagerImpl, AMapLocationListener {

    private static final int INSTALL_PACKAGES_REQUEST_CODE = 1322;
    @Bind(R.id.view_pager)
    NoScrollViewPager viewPager;
    @Bind(R.id.tabHome)
    RadioButton tabHome;
    @Bind(R.id.tabLookHouse)
    RadioButton tabLookHouse;
    @Bind(R.id.tabMessage)
    RadioButton tabMessage;
    @Bind(R.id.tabMine)
    RadioButton tabMine;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.unread_msg_number)
    TextView tvUnreadMsgNumber;
    public boolean isConflict = false;
    private boolean isExceptionDialogShow = false;
    private android.app.AlertDialog.Builder exceptionBuilder;
    private int currentTabIndex;
    private RopeProgressBar ropeProgressBar;
    private AlertDialog updateDialog;
    private List<City> citys;
    /**
     * 广告弹框次数map
     */
    private HashMap<String, String> adCountMap;
    private HomeIndexFragment homeIndexFragment;

    @Override
    protected View setContentLayoutView() {
        EventBus.getDefault().register(this);
        return getLayoutInflater().inflate(R.layout.pub_fragment_main, null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uridata = getIntent().getData();
        if (null != uridata) {
            String tab = uridata.getQueryParameter("tab");
            if ("lookHouse".equals(tab)) {
                tabLookHouse.setChecked(true);
                viewPager.setCurrentItem(1);
            } else if ("mine".equals(tab)) {
                tabMine.setChecked(true);
                viewPager.setCurrentItem(3);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean toMessage = intent.getBooleanExtra("toMessage", false);
        boolean toMine = intent.getBooleanExtra("toMine", false);
        if (toMessage) {
            if (tabMessage.isChecked())
                return;
            tabMessage.setChecked(true);
            viewPager.setCurrentItem(2);
        } else if (toMine) {
            if (tabMine.isChecked())
                return;
            tabMine.setChecked(true);
            viewPager.setCurrentItem(3);
        }
    }

    private MainPagerAdapter adapter;

    private UpdateBean updateBean;
    protected boolean isRegister;
//    protected ProgressDialog updateProgressDialog;

    @Override
    protected void initView() {
        //请求所有城市列表
        try {
            if (mDao != null) {
                if (TextUtils.isEmpty(mDao.localDao.getCityCache())) {
                    mDao.selectArea(0, "1", "0", this);
                } else {
                    citys = new Gson().fromJson(mDao.localDao.getCityCache(), new TypeToken<List<City>>() {
                    }.getType());
                    MainActivityPermissionsDispatcher.startLocateWithCheck(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewPager.setOffscreenPageLimit(4);
        homeIndexFragment = HomeIndexFragment.newInstance();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(homeIndexFragment);
        fragments.add(HomeLookHouseFragment.newInstance());
        fragments.add(MessageFragment.newInstance());
        fragments.add(HomeMineFragment.newInstance());
        adapter = new MainPagerAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        if (!TextUtils.isEmpty(mDao.getClientID())) {
            mDao.bindClientID(1, null);
        }
        mDao.updateAppVersion(1, this);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.tabHome);
                        currentTabIndex = 0;
                        break;
                    case 1:
                        radioGroup.check(R.id.tabLookHouse);
                        currentTabIndex = 1;
                        break;
                    case 2:
                        radioGroup.check(R.id.tabMessage);
                        currentTabIndex = 2;
                        break;
                    case 3:
                        radioGroup.check(R.id.tabMine);
                        currentTabIndex = 3;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        showMsgCount();
        getAdList();
        MainActivityPermissionsDispatcher.getAdImageWithCheck(MainActivity.this);
    }



    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void getAdImage() {
        mDao.selectOneEnableAd(0, 1, new RequestManagerImpl() {
            @Override
            public void onSuccess(final String response, int requestType) {
                Log.d("@@@", "onSuccess:" + response);
                final AdBean adBean = GsonUtils.fromJson(response, AdBean.class);
                if (adBean != null) {
                    List<AdBean.AppImgBean> appImgs = adBean.getAppImg();
                    for (final AdBean.AppImgBean img : appImgs
                    ) {
                        if (img.getName() == 2) {
                            //缓存图片
                            new Thread(
                            ) {
                                @Override
                                public void run() {
                                    try {
                                        final File adImg = new File(com.easylife.house.customer.util.FileUtils.SDPATH, "adImg.jpg");
                                        File file = Glide.with(MainActivity.this).load(img.getUrl()).downloadOnly(0, 0).get();
                                        com.blankj.utilcode.util.FileUtils.copyFile(file, adImg);
                                        adBean.setLocalImagePath(adImg.getAbsolutePath());
                                        SPUtils.getInstance().put("adImg", GsonUtils.toJson(adBean));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();

                        }

                    }

                } else {
                    AdBean emptyAdBean = new AdBean();
                    SPUtils.getInstance().put("adImg", GsonUtils.toJson(emptyAdBean));
                }
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                Log.d("@@@", "onFail: " + code.msg);
            }
        });


    }

    public void setHomeTabIcon(int d, boolean isGoneText) {
        Drawable drawable = ContextCompat.getDrawable(this, d);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        tabHome.setCompoundDrawables(null, drawable, null, null);
        if (isGoneText) {
            tabHome.setText("");
        } else {
            tabHome.setText("首页");

        }
    }

    private void getAdList() {
        mDao.getAdList(2, this);
    }

    private void initAdDialog(List<AdListBean> listData) {
        if (listData == null || listData.size() == 0) {
            return;
        }
        ArrayList advList = new ArrayList<>();
        for (AdListBean bean : listData) {
            AdInfo adInfo = new AdInfo();
            adInfo.setActivityImg(bean.img);
            adInfo.setUrl(bean.url);
            advList.add(adInfo);
        }

        AdManager adManager = new AdManager(MainActivity.this, advList);
        adManager.setWidthPerHeight(0.75f);
        adManager.setOverScreen(true)
                .setPageTransformer(new DepthPageTransformer());

        adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
            @Override
            public void onImageClick(View view, AdInfo advInfo) {
                WebViewActivity.startActivity(MainActivity.this, advInfo.getTitle(), advInfo.getUrl());
            }
        });
        HashMap<String, Double> adCountMap = null;

        String json = mDao.localDao.getString(FOLDER_CACHE_PUB, KEY_PUB_AD_DIALOG_COUNT, "");
        if (TextUtils.isEmpty(json)) {
            adCountMap = new HashMap<String, Double>();
        } else {
            adCountMap = ((HashMap<String, Double>) GsonUtils.fromJson(json, HashMap.class));
        }
        long nowMills = TimeUtils.getNowMills();
        String nowDateString = TimeUtils.millis2String(nowMills, new SimpleDateFormat("yyyy-MM-dd"));
        Double count = adCountMap.get(nowDateString);
        if (count == null) {
            count = 1d;
            adCountMap.put(nowDateString, count);
            adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);
            String newJson = GsonUtils.toJson(adCountMap);
            mDao.localDao.saveString(FOLDER_CACHE_PUB, KEY_PUB_AD_DIALOG_COUNT, newJson);
            LogUtils.dTag(TAG, "date:" + nowDateString + " count:" + count);
        } else {
            if (count < 3) {
                count++;
                adCountMap.put(nowDateString, count);
                adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);
                String newJson = GsonUtils.toJson(adCountMap);
                mDao.localDao.saveString(FOLDER_CACHE_PUB, KEY_PUB_AD_DIALOG_COUNT, newJson);
                LogUtils.dTag(TAG, "date:" + nowDateString + " count:" + count);
            } else {
                LogUtils.dTag(TAG, "今日超过3次了" + "date:" + nowDateString + " count:" + count);
            }
        }
    }

    /**
     * @param apkUrl        下载地址
     * @param apkName       下载完成文件名
     * @param isForceUpdate 是否强制更新
     */
    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
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
                    MainActivityPermissionsDispatcher.downloadNewApkWithCheck(MainActivity.this, updateBean.updataUrl, update_download_apk_name, true);
                } else {
                    MainActivityPermissionsDispatcher.downloadNewApkWithCheck(MainActivity.this, updateBean.updataUrl, update_download_apk_name, false);
                }
            }
        });
        switch (isForce) {
            //强制更新
            case "1":
                alertDialog.setCancelable(false);
                break;
            //非强制更新
            default:
                alertDialog.setNegativeButton("取消", null);
                break;
        }


        alertDialog.show();

    }

    /**
     * 预约看房的时候  查看预约状态切换到看房页面 否则继续看房
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent event) {
        switch (event.MsgType) {
            case PUSH_MSG:
                showMsgCount();
                break;
            case LOOK_HOUSE_CHANGE_SEE:
                viewPager.setCurrentItem(1, false);
                tabLookHouse.setChecked(true);
                break;
            case MessageEvent.RESTART_LOCATION:
                MainActivityPermissionsDispatcher.startLocateWithCheck(this);
                break;
            case MessageEvent.FORCE_LOGIN_OUT://单点登录 退出登录
                AlertDialog.Builder exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                exceptionBuilder.setTitle("提示");
                exceptionBuilder.setMessage("您的账号已在其他设备登录");
                exceptionBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDao.loginOut();
                        LoginByVerifyCodeActivity.startActivity(MainActivity.this, 0);
                        dialog.dismiss();

                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                break;
        }
    }


    /**
     * 安装apk
     */
    private void installApk() {
        InstallUtil.install(activity, FileUtils.SDPATH + update_download_apk_name);
    }


    private void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                //安装应用的逻辑(写自己的就可以)
                installApk();
            } else {
                //设置安装未知应用来源的权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 119);
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
            if (progress >= 100) {//下载完成   强制、非强制 apk下载完成 都回调这
                if ("1".equals(updateBean.isforce)) {
                    ropeProgressBar.setProgress(100);
                    updateDialog.dismiss();
                }
                checkIsAndroidO();
            } else if (progress == -1) {//更新出错
                updateDialog.dismiss();
                CustomerUtils.showTip(getApplicationContext(), "更新出错，程序即将关闭，请稍后再试,或前往官网下载最新版apk。");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        clearAllActivitys();
                    }
                }, 3000);
            } else {//更新进度
                ropeProgressBar.setProgress(progress);
            }
        }
    };

    public void showUpdateDialog() {
        updateDialog = new AlertDialog.Builder(this).create();
        updateDialog.show();
        Window window = updateDialog.getWindow();
        window.setContentView(R.layout.update_progress_dialog);
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.setCancelable(false);
        ropeProgressBar = (RopeProgressBar) window.findViewById(R.id.update_progress);


//        updateProgressDialog = new ProgressDialog(this);
//        updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        updateProgressDialog.setTitle("好生活升级");
//        updateProgressDialog.setIndeterminate(true);
//        updateProgressDialog.setCancelable(false);
//        updateProgressDialog.show();
    }

    /**
     * 显示拨号提示框
     *
     * @param phone
     */


    public void call(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            CustomerUtils.showTip(this, "手机号错误");
            return;
        }
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否拨打 " + phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivityPermissionsDispatcher.jumpCallPhoneWithCheck(MainActivity.this, phone);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void jumpCallPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
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
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void setActionBarDetail() {
        layActionBar.setVisibility(View.GONE);
    }


    @OnClick({R.id.tabHome, R.id.tabLookHouse, R.id.tabMessage, R.id.tabMine})
    public void onClick(View view) {
        //刷新消息内容
        HashMap<String, String> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.tabHome:
                map.put("tab_name", "首页");
                MobclickAgent.onEvent(activity, "tab", map);
                if (currentTabIndex == 0 && homeIndexFragment.isShowQuickTop) {
                    homeIndexFragment.scroll2Top();
                }
                viewPager.setCurrentItem(0, false);
                currentTabIndex = 0;

                break;
            case R.id.tabLookHouse:
                map.put("tab_name", "看房");
                MobclickAgent.onEvent(activity, "tab", map);
                viewPager.setCurrentItem(1, false);
                currentTabIndex = 1;
                break;
            case R.id.tabMessage:
                map.put("tab_name", "消息");
                MobclickAgent.onEvent(activity, "tab", map);
                viewPager.setCurrentItem(2, false);
                currentTabIndex = 2;
                clearMsgCount();
                break;
            case R.id.tabMine:
                map.put("tab_name", "我的");
                MobclickAgent.onEvent(activity, "tab", map);
                viewPager.setCurrentItem(3, false);
                currentTabIndex = 3;
                break;
        }
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
        switch (requestType) {
            case 0:
                citys = new Gson().fromJson(response, new TypeToken<List<City>>() {
                }.getType());
                mDao.localDao.saveCityCache(response);
                MainActivityPermissionsDispatcher.startLocateWithCheck(this);
                break;
            case 1:
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
                break;
            case 2:
                List<AdListBean> listData = new Gson().fromJson(response, new TypeToken<List<AdListBean>>() {
                }.getType());
                initAdDialog(listData);
                break;
        }


    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
    }


    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption;
    private AMapLocationClient mlocationClient;

    @NeedsPermission((Manifest.permission.ACCESS_FINE_LOCATION))
    public void startLocate() {
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mDao.saveLocateCache(amapLocation.getCity(), amapLocation.getAdCode(),
                        amapLocation.getLatitude(), amapLocation.getLongitude());
                for (City city : citys) {
                    if (amapLocation.getCity().equals(city.city)) {
                        SearchSingleton singletonSearch = SearchSingleton.getIstance();
                        singletonSearch.city = city.city;
                        singletonSearch.cityId = city.cityid;
                        singletonSearch.buyWhere = city.city;
                        mDao.localDao.saveCityId(city.cityid);
                        mDao.localDao.saveCity(amapLocation.getCity());
                    }
                }
                mlocationClient.stopLocation();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                LogOut.d("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 119) {
            checkIsAndroidO();
        }


        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case 100:
                    viewPager.setCurrentItem(0);
                    break;
            }
    }


    private void clearMsgCount() {
        tvUnreadMsgNumber.setVisibility(View.GONE);
        EventBus.getDefault().post(new MessageEvent(PUSH_MSG_REFRESH));
        LocalDao localDao = new LocalDao(this);
        localDao.saveInt(FOLDER_CACHE_PUB, KEY_UN_READ_MSG_COUNT, 0);
    }

    private void showMsgCount() {
        LocalDao localDao = new LocalDao(this);
        int unReadMsgCount = localDao.getInt(FOLDER_CACHE_PUB, KEY_UN_READ_MSG_COUNT, 0);
        if (unReadMsgCount > 0) {
            tvUnreadMsgNumber.setVisibility(View.VISIBLE);
            tvUnreadMsgNumber.setText("+" + unReadMsgCount);
        } else {
            tvUnreadMsgNumber.setVisibility(View.GONE);
        }
    }
}


