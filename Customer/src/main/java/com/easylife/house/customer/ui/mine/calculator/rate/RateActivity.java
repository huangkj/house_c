package com.easylife.house.customer.ui.mine.calculator.rate;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.RateSelectAdapter;
import com.easylife.house.customer.bean.ResultRate;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.view.NumberLimitEditText;

import java.util.List;

import butterknife.Bind;

/**
 * 利率列表
 */
public class RateActivity extends MVPBaseActivity<RateContract.View, RatePresenter> implements RateContract.View {

    @Bind(R.id.tvRate)
    NumberLimitEditText tvRate;
    @Bind(R.id.layTop)
    LinearLayout layTop;
    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;

    public static void startActivity(Activity activity, float rateBase, float rateCurrent, boolean isBusiness, int requestCode) {
        activity.startActivityForResult(new Intent(activity, RateActivity.class)
                .putExtra("rateBase", rateBase)
                .putExtra("rateCurrent", rateCurrent)
                .putExtra("isBusiness", isBusiness), requestCode);
    }

    public static void startActivity(Fragment fragment, float rateBase, float rateCurrent, boolean isBusiness, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), RateActivity.class)
                .putExtra("rateBase", rateBase)
                .putExtra("rateCurrent", rateCurrent)
                .putExtra("isBusiness", isBusiness), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.calculator_activity_rate, null);
    }

    private float rateBase;
    private float rateCurrent;
    private boolean isBusiness;
    private RateSelectAdapter adapter;

    private String temp;

    @Override
    protected void initView() {
        rateBase = getIntent().getFloatExtra("rateBase", 0.0325f);
        rateCurrent = getIntent().getFloatExtra("rateCurrent", 0.0325f);
        isBusiness = getIntent().getBooleanExtra("isBusiness", false);

        tvRate.setMaxDouble(10000);
        tvRate.setFloatLength(2);

        if (isBusiness) {
            tvTitle.setText("商贷利率");
            tvDesc.setText("或选择商贷利率");
        } else {
            tvTitle.setText("公积金利率");
            tvDesc.setText("或选择公积金贷利率");
        }

        tvRate.setText(DoubleFomat.format2(rateCurrent * 100) + "");
        tvRate.setText("3.366");

        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ResultRate item = (ResultRate) baseQuickAdapter.getItem(i);
                rateCurrent = Float.parseFloat(item.rateNum);
                tvRate.setText(DoubleFomat.format2(rateCurrent * 100) + "");
                adapter.setRateNum(rateCurrent + "");
            }
        });

        mPresenter.getNetRate(isBusiness);
        tvRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (TextUtils.isEmpty(text))
                    return;
                double d = Double.parseDouble(text);
                if (d > 100) {
                    tvRate.setText(temp);
                }
            }
        });
    }


    @Override
    protected void setActionBarDetail() {
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setText("完成");
        btnRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rateCurrent = Float.parseFloat(tvRate.getText().toString()) / 100;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK, new Intent().putExtra("data", rateCurrent));
                finish();
            }
        });
    }

    @Override
    protected void tryRequestData() {
        mPresenter.getNetRate(isBusiness);
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @Override
    public void initNetData(List<ResultRate> data) {
        adapter = new RateSelectAdapter(R.layout.pub_item_select, data, rateCurrent + "");
        recycleView.setAdapter(adapter);
    }
}
