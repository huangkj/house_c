package com.easylife.house.customer.ui.mine.calculator.group;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.mine.calculator.CalculatorActivity;
import com.easylife.house.customer.ui.mine.calculator.rate.RateActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.util.LogOut;
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

public class GroupFragment extends MVPBaseFragment<GroupContract.View, GroupPresenter> implements GroupContract.View {

    @Bind(R.id.btnAccrual)
    RadioButton btnAccrual;
    @Bind(R.id.btnMoney)
    RadioButton btnMoney;
    @Bind(R.id.edMoneyAcc)
    NumberLimitEditText edMoneyAcc;
    @Bind(R.id.tvDateAcc)
    TextView tvDateAcc;
    @Bind(R.id.tvRateAcc)
    TextView tvRateAcc;
    @Bind(R.id.edMoneyBusiness)
    NumberLimitEditText edMoneyBusiness;
    @Bind(R.id.tvDateBusiness)
    TextView tvDateBusiness;
    @Bind(R.id.tvRateBusiness)
    TextView tvRateBusiness;
    @Bind(R.id.tvRateBase)
    TextView tvRateBase;
    @Bind(R.id.tvRateBaseBusiness)
    TextView tvRateBaseBusiness;
    @Bind(R.id.btnCalculator)
    ButtonTouch btnCalculator;
    @Bind(R.id.tvResultAll)
    TextView tvResultAll;
    @Bind(R.id.tvResultAccrual)
    TextView tvResultAccrual;
    @Bind(R.id.tvResultRateAcc)
    TextView tvResultRateAcc;
    @Bind(R.id.tvResultRateBusiness)
    TextView tvResultRateBusiness;
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

    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.calculator_fragment_group;
    }

    private boolean hasLoad = false;

    @Override
    public void onResume() {
        super.onResume();
        LogOut.d("onResume :", hasLoad);
        if (!hasLoad) {

            rateBaseAcc = ((CalculatorActivity) getActivity()).rate1;
            rateBaseBusiness = ((CalculatorActivity) getActivity()).rate2;

            rateCurrentAcc = rateBaseAcc;
            tvRateAcc.setText(DoubleFomat.format2(rateBaseAcc * 100) + "%");
            tvRateBase.setText(DoubleFomat.format2(rateBaseAcc * 100) + "%");

            rateCurrentBusiness = rateBaseBusiness;
            tvRateBusiness.setText(DoubleFomat.format2(rateBaseBusiness * 100) + "%");
            tvRateBaseBusiness.setText(DoubleFomat.format2(rateBaseBusiness * 100) + "%");

            hasLoad = true;
        }
    }

    @Override
    public void initViews() {
        layResult.setVisibility(View.GONE);
        edMoneyAcc.setMaxDouble(10000);
        edMoneyBusiness.setMaxDouble(10000);

        rateBaseAcc = ((CalculatorActivity) getActivity()).rate1;
        rateBaseBusiness = ((CalculatorActivity) getActivity()).rate2;

        rateCurrentAcc = rateBaseAcc;
        tvRateAcc.setText(DoubleFomat.format2(rateBaseAcc * 100) + "%");
        tvRateBase.setText(DoubleFomat.format2(rateBaseAcc * 100) + "%");

        rateCurrentBusiness = rateBaseBusiness;
        tvRateBusiness.setText(DoubleFomat.format2(rateBaseBusiness * 100) + "%");
        tvRateBaseBusiness.setText(DoubleFomat.format2(rateBaseBusiness * 100) + "%");

        tvDateAcc.setText(yearAcc);
        tvDateBusiness.setText(yearBusiness);

        yearList = new ArrayList<>();
        for (int i = 30; i > 0; i--) {
            yearList.add(new ItemSelect(i + "年(" + i * 12 + "期)", i + ""));
        }
    }

    private PubTipDialog dialogAcc;
    private String yearAcc = "20年(240期)";
    private String yearIdAcc = "20";
    private List<ItemSelect> yearList;
    private float rateBaseAcc;
    private float rateCurrentAcc;
    private PubTipDialog dialogBusiness;
    private String yearBusiness = "20年(240期)";
    private String yearIdBusiness = "20";
    private float rateBaseBusiness;
    private float rateCurrentBusiness;

    @Override
    public void initChart(float f1, float f2, String moneyAll, String moneyAccrual, String paymentFirst, String paymentOther) {
        layResult.setVisibility(View.VISIBLE);

        tvResultAll.setText(moneyAll);
        tvResultAccrual.setText(moneyAccrual);
        tvResultPaymentFirst.setText(paymentFirst);
        tvResultRateAcc.setText(DoubleFomat.format2(rateCurrentAcc * 100) + "%");
        tvResultRateBusiness.setText(DoubleFomat.format2(rateCurrentBusiness * 100) + "%");

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

    @OnClick({R.id.btnAccrual, R.id.btnMoney, R.id.tvDateAcc, R.id.tvRateAcc, R.id.tvDateBusiness,
            R.id.tvRateBusiness, R.id.btnCalculator})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAccrual:
                mPresenter.showResult(btnAccrual.isChecked(),
                        edMoneyAcc.getText().toString(), yearIdAcc, rateCurrentAcc,
                        edMoneyBusiness.getText().toString(), yearIdBusiness, rateCurrentBusiness
                );
                break;
            case R.id.btnMoney:
                mPresenter.showResult(btnAccrual.isChecked(),
                        edMoneyAcc.getText().toString(), yearIdAcc, rateCurrentAcc,
                        edMoneyBusiness.getText().toString(), yearIdBusiness, rateCurrentBusiness
                );
                break;
            case R.id.tvDateAcc:
                if (dialogAcc == null) {
                    dialogAcc = new PubTipDialog(getActivity());
                }
                dialogAcc.showDialogList("请选择按揭年数", yearAcc, yearList, new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        yearAcc = date.text;
                        yearIdAcc = date.getId();
                        tvDateAcc.setText(yearAcc);
                    }
                });
                break;
            case R.id.tvDateBusiness:
                if (dialogBusiness == null) {
                    dialogBusiness = new PubTipDialog(getActivity());
                }
                dialogBusiness.showDialogList("请选择按揭年数", yearBusiness, yearList, new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        yearBusiness = date.text;
                        yearIdBusiness = date.getId();
                        tvDateBusiness.setText(yearBusiness);
                    }
                });
                break;
            case R.id.tvRateAcc:
                RateActivity.startActivity(GroupFragment.this, rateBaseAcc, rateCurrentAcc, false, 2);
                break;
            case R.id.tvRateBusiness:
                RateActivity.startActivity(GroupFragment.this, rateBaseBusiness, rateCurrentBusiness, true, 3);
                break;
            case R.id.btnCalculator:
                mPresenter.showResult(btnAccrual.isChecked(),
                        edMoneyAcc.getText().toString(), yearIdAcc, rateCurrentAcc,
                        edMoneyBusiness.getText().toString(), yearIdBusiness, rateCurrentBusiness
                );
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 2:
                    rateCurrentAcc = data.getFloatExtra("data", 0);
                    tvRateAcc.setText(DoubleFomat.format2(rateCurrentAcc * 100) + "%");
                    break;
                case 3:
                    rateCurrentBusiness = data.getFloatExtra("data", 0);
                    tvRateBusiness.setText(DoubleFomat.format2(rateCurrentBusiness * 100) + "%");
                    break;
            }
        }
    }
}
