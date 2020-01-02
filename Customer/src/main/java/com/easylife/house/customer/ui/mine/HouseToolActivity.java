package com.easylife.house.customer.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.ui.mine.calculator.CalculatorActivity;
import com.easylife.house.customer.ui.mine.calculator.taxes.TaxesActivity;
import com.easylife.house.customer.util.CustomerUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Mars on 2017/3/21 14:51.
 * 描述：房产工具
 */

public class HouseToolActivity extends BaseActivity {

    @Bind(R.id.tvHouseLoan)
    TextView tvHouseLoan;
    @Bind(R.id.tvTaxes)
    TextView tvTaxes;
    @Bind(R.id.tvEvaluate)
    TextView tvEvaluate;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, HouseToolActivity.class));
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_house_tool, null);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setActionBarDetail() {

    }

    @OnClick({R.id.tvHouseLoan, R.id.tvTaxes, R.id.tvEvaluate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvHouseLoan:
                startActivity(new Intent(activity, CalculatorActivity.class));
                break;
            case R.id.tvTaxes:
                TaxesActivity.startActivity(activity, 0);
                break;
            case R.id.tvEvaluate:
                CustomerUtils.showTip(activity, "功能研发中...");
                break;
        }
    }
}
