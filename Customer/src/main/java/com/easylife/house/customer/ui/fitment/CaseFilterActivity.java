package com.easylife.house.customer.ui.fitment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;

public class CaseFilterActivity extends BaseActivity {

    public static void startActivity(Activity activity, String type, String area, String style, int requestCode) {
        activity.startActivityForResult(new Intent(activity, CaseFilterActivity.class)
                        .putExtra("type", type)
                        .putExtra("area", area)
                        .putExtra("style", style),
                requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.fitment_activity_filter, null);
    }

    private String type, area, style;
    private int state = 0;

    @Override
    protected void initView() {
        type = getIntent().getStringExtra("type");
        area = getIntent().getStringExtra("area");
        style = getIntent().getStringExtra("style");
    }

    @Override
    public void finish() {
        switch (state) {
            case 1:
                // 展示第一项- 居室
                state = 0;
                break;
            case 2:
                // 展示第二项- 面积
                state = 1;
                break;
            default:
                showExitTip();
                break;
        }
    }

    private void showExitTip() {

        finish();
    }

    @Override
    protected void setActionBarDetail() {

    }
}
