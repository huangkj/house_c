package com.easylife.house.customer.ui.pub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.AdBean;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.service.GeTuiIntentService;
import com.easylife.house.customer.service.GeTuiPushService;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.util.UiUtil;
import com.igexin.sdk.PushManager;

/**
 * Created by Mars on 2017/3/15 16:23.
 * 描述：启动页
 */
public class LaunchActivity extends AppCompatActivity {
    private ServerDao mDao;
    private boolean is2Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        UiUtil.setNoTitle(this);
        setContentView(R.layout.pub_activity_launch);

        PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);
        mDao = new ServerDao(this);
        if (mDao.isFirst()) {
            startActivity(new Intent(LaunchActivity.this, GuideActivity.class));
            finish();
        } else {
            String cacheAdBeanJson = SPUtils.getInstance().getString("adImg");
            AdBean cacheAdBean = GsonUtils.fromJson(cacheAdBeanJson, AdBean.class);
            if (cacheAdBean != null) {
                AdBean.AppImgBean cacheImageBean = cacheAdBean.getAndroidImageBean();
                if (cacheImageBean != null && !TextUtils.isEmpty(cacheAdBean.getLocalImagePath())) {//有缓存，显示广告图
                    AdActivity.startActivity(LaunchActivity.this, cacheAdBean);
                    finish();
//                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
//                    finish();
                } else {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    finish();
                }
            } else {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                finish();
            }



//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }, 500);
        }


    }


}
