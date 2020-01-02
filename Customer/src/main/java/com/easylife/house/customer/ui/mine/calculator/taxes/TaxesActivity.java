package com.easylife.house.customer.ui.mine.calculator.taxes;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.NumberLimitEditText;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.easylife.house.customer.R.id.btnAccrual;
import static com.easylife.house.customer.R.id.layResult;
import static com.easylife.house.customer.R.id.tvResultAccrual;
import static com.easylife.house.customer.R.id.tvResultDescPayment;
import static com.easylife.house.customer.R.id.tvResultPaymentFirst;
import static com.easylife.house.customer.R.id.tvResultPaymentOther;
import static com.easylife.house.customer.R.id.tvResultRate;

/**
 * 税费计算器
 */
public class TaxesActivity extends MVPBaseActivity<TaxesContract.View, TaxesPresenter> implements TaxesContract.View {
    @Bind(R.id.btnYes)
    RadioButton btnYes;
    @Bind(R.id.btnNo)
    RadioButton btnNo;
    @Bind(R.id.edHouseArea)
    EditText edHouseArea;
    @Bind(R.id.edHousePriceUnit)
    EditText edHousePriceUnit;
    @Bind(R.id.btnCalculator)
    ButtonTouch btnCalculator;
    @Bind(R.id.tvResultAll)
    TextView tvResultAll;
    @Bind(R.id.tvResultYinHua)
    TextView tvResultYinHua;
    @Bind(R.id.tvResultQiShui)
    TextView tvResultQiShui;
    @Bind(R.id.tvResultQuanShu)
    TextView tvResultQuanShu;
    @Bind(R.id.tvResultAllTaxes)
    TextView tvResultAllTaxes;
    @Bind(R.id.layResultPaymentOther)
    LinearLayout layResultPaymentOther;
    @Bind(R.id.chartDount)
    PieChart chartDount;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.layResult)
    LinearLayout layResult;

    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, TaxesActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(this, TextUtils.isEmpty(msg) ? "" : msg);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_calculator_taxes, null);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setActionBarDetail() {

    }

    @Override
    protected void tryRequestData() {

    }

    @OnClick(R.id.btnCalculator)
    public void onClick() {
        mPresenter.showResult(btnYes.isChecked(), edHouseArea.getText().toString(), edHousePriceUnit.getText().toString());
    }

    @Override
    public void initChart(float f1, float f2, String moneyAll, String moneyYinHua, String moneyQiShui, String moneyQuanShu, String moneyAllTaxes) {
        if (layResult.getVisibility() != View.VISIBLE)
            layResult.setVisibility(View.VISIBLE);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部

        tvResultAll.setText(moneyAll);
        tvResultYinHua.setText(moneyYinHua);
        tvResultQiShui.setText(moneyQiShui);
        tvResultQuanShu.setText(moneyQuanShu);
        tvResultAllTaxes.setText(moneyAllTaxes);


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(f1, f1 + ""));
        entries.add(new PieEntry(f2, f2 + ""));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setDrawValues(false);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#5bbd6d"));
        colors.add(Color.parseColor("#6a86e1"));
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());

        chartDount.setDrawCenterText(false);// 圆环中心文字
        chartDount.setDrawEntryLabels(false);//  圆环部分的文字说明
        chartDount.setDrawHoleEnabled(true); // 是否有内环
        chartDount.setHoleColor(Color.WHITE);// 内环颜色
        chartDount.setRotationEnabled(false);  // 禁止旋转
        chartDount.setTransparentCircleRadius(0f); // 内环透明宽度
        chartDount.setDescription(null);
        chartDount.setHoleRadius(60f); //半径
        chartDount.animateY(1400, Easing.EasingOption.EaseInOutQuad);// 绘画动画

        // 不显示图例介绍
        Legend l = chartDount.getLegend();
        l.setEnabled(false);

        chartDount.setData(data);
        chartDount.highlightValues(null);
        chartDount.invalidate();

        chartDount.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
