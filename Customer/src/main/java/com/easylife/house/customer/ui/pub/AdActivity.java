package com.easylife.house.customer.ui.pub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.AdBean;
import com.easylife.house.customer.util.UiUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.easylife.house.customer.ui.pub.PubWebViewActivity.AD;

public class AdActivity extends AppCompatActivity {


    @Bind(R.id.ivAd)
    ImageView ivAd;
    @Bind(R.id.tvCancel)
    TextView tvCancel;

    private boolean isAdClicked;

    public static void startActivity(Context context, AdBean adBean) {
        context.startActivity(new Intent(context, AdActivity.class)
                .putExtra("adBean", adBean)
        );
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        UiUtil.setNoTitle(this);
        setContentView(R.layout.activity_ad);
        ButterKnife.bind(this);
        final AdBean adBean = (AdBean) getIntent().getSerializableExtra("adBean");
        Glide.with(this).load(Uri.fromFile(new File(adBean.getLocalImagePath()))).into(ivAd);
        ivAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(adBean.getLinkAddress())) {
                    isAdClicked = true;
                    PubWebViewActivity.startActivity(AdActivity.this, "", adBean.getLinkAddress(), AD);
                }
            }
        });


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (isAdClicked) {
                    return;
                } else {
                    startMain();
                }
            }
        }, 3000);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAdClicked) {
            startMain();
        }

        isAdClicked = false;
    }

    private void startMain() {
        startActivity(new Intent(AdActivity.this, MainActivity.class));
        finish();
    }

    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_ad, null);
    }

    protected void initView() {
//        Glide.with(this).load(adBean.getAppImg().get(0).getUrl()).into(ivAd);
    }


    @OnClick({R.id.tvCancel,})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                startMain();
                break;

        }
    }


}
