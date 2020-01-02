package com.easylife.house.customer.ui.mine.calculator.accumulation;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.bean.ResultRate;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.mine.calculator.CalculatorActivity;
import com.easylife.house.customer.ui.mine.calculator.rate.RateActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.util.PubTipDialog;
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
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 公积金贷款
 */
public class AccumulationFragment extends MVPBaseFragment<AccumulationContract.View, AccumulationPresenter> implements AccumulationContract.View {

    @Bind(R.id.btnAccrual)
    RadioButton btnAccrual;
    @Bind(R.id.btnMoney)
    RadioButton btnMoney;
    @Bind(R.id.edMoney)
    NumberLimitEditText edMoney;
    @Bind(R.id.tvDate)
    TextView tvDate;
    @Bind(R.id.tvRate)
    TextView tvRate;
    @Bind(R.id.tvRateBase)
    TextView tvRateBase;
    @Bind(R.id.btnCalculator)
    ButtonTouch btnCalculator;
    @Bind(R.id.tvResultAll)
    TextView tvResultAll;
    @Bind(R.id.tvResultAccrual)
    TextView tvResultAccrual;
    @Bind(R.id.tvResultRate)
    TextView tvResultRate;
    @Bind(R.id.tvResultPaymentFirst)
    TextView tvResultPaymentFirst;
    @Bind(R.id.tvResultPaymentOther)
    TextView tvResultPaymentOther;
    @Bind(R.id.chartDount)
    PieChart chartDount;
    @Bind(R.id.layResult)
    LinearLayout layResult;
    @Bind(R.id.layResultPaymentOther)
    LinearLayout layResultPaymentOther;
    @Bind(R.id.tvResultDescPayment)
    TextView tvResultDescPayment;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    public static AccumulationFragment newInstance() {
        AccumulationFragment fragment = new AccumulationFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.calculator_fragment_accumulation;
    }

    @Override
    public void initViews() {
        layResult.setVisibility(View.GONE);
        edMoney.setMaxDouble(10000);

        rateBase = ((CalculatorActivity) getActivity()).rate1;
        rateCurrent = rateBase;
        tvRate.setText(DoubleFomat.format2(rateBase) + "%");
        tvRateBase.setText(DoubleFomat.format2(rateBase) + "%");

        tvDate.setText(year);

        yearList = new ArrayList<>();
        for (int i = 30; i > 0; i--) {
            yearList.add(new ItemSelect(i + "年(" + i * 12 + "期)", i + ""));
        }

        mPresenter.getRate();
    }

    private PubTipDialog dialog;
    private String year = "20年(240期)";
    private String yearId = "20";
    private List<ItemSelect> yearList;
    private float rateBase;
    private float rateCurrent;
    private String temp;

    @Override
    public void initNetData(List<ResultRate> data) {
        if (data == null || data.size() == 0)
            return;
        rateBase = Float.parseFloat(data.get(0).rateNum);
        rateCurrent = rateBase;
        tvRate.setText(DoubleFomat.format2(rateBase * 100) + "%");
        tvRateBase.setText(DoubleFomat.format2(rateBase * 100) + "%");
    }

    @Override
    public void initChart(float f1, float f2, String moneyAll, String moneyAccrual, String paymentFirst, String paymentOther) {
        layResult.setVisibility(View.VISIBLE);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部

        tvResultAll.setText(moneyAll);
        tvResultAccrual.setText(moneyAccrual);
        tvResultPaymentFirst.setText(paymentFirst);

        tvResultRate.setText(DoubleFomat.format2(rateCurrent * 100) + "%");

        if (btnAccrual.isChecked()) {
            tvResultDescPayment.setText("参考月供：");
            layResultPaymentOther.setVisibility(View.GONE);
        } else {
            tvResultDescPayment.setText("首月月供：");
            layResultPaymentOther.setVisibility(View.VISIBLE);
            tvResultPaymentOther.setText(paymentOther);
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(f1, Math.round(f1 / (f1 + f2) * 100) + "%"));
        entries.add(new PieEntry(f2, Math.round(f2 / (f1 + f2) * 100) + "%"));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setDrawValues(false);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FF6800"));
        colors.add(Color.parseColor("#FFAD00"));
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

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }

    @OnClick({R.id.btnAccrual, R.id.btnMoney, R.id.tvDate, R.id.tvRate, R.id.btnCalculator})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAccrual:
                mPresenter.showResult(true, edMoney.getText().toString(), yearId, rateCurrent);
                break;
            case R.id.btnMoney:
                mPresenter.showResult(false, edMoney.getText().toString(), yearId, rateCurrent);
                break;
            case R.id.tvDate:
                if (dialog == null) {
                    dialog = new PubTipDialog(getActivity());
                }
                dialog.showDialogList("请选择按揭年数", year, yearList, new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        year = date.text;
                        yearId = date.getId();
                        tvDate.setText(year);
                    }
                });
                break;
            case R.id.tvRate:
                RateActivity.startActivity(AccumulationFragment.this, rateBase, rateCurrent, false, 2);
                break;
            case R.id.btnCalculator:
                mPresenter.showResult(btnAccrual.isChecked(), edMoney.getText().toString(), yearId, rateCurrent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 2:
                    rateCurrent = data.getFloatExtra("data", 0);
                    tvRate.setText(DoubleFomat.format2(rateCurrent * 100) + "%");
                    break;
            }
        }
    }

}
