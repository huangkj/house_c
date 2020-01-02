package com.easylife.house.customer.ui.mine.calculator;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.TabPageFragment;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.ResultRate;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.calculator.accumulation.AccumulationFragment;
import com.easylife.house.customer.ui.mine.calculator.business.BusinessFragment;
import com.easylife.house.customer.ui.mine.calculator.group.GroupFragment;
import com.easylife.house.customer.view.NoScrollViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.R.attr.data;

/**
 * 房贷计算器
 */
public class CalculatorActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.tabMineCollect)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    NoScrollViewPager viewPager;
    public float rate1 = 0.0325f;
    public float rate2 = 0.0490f;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_calculator, null);
    }

    @Override
    protected void initView() {
        mDao.selectRate(1, "0", "0", this);
        mDao.selectRate(2, "1", "0", this);

        viewPager.setOffscreenPageLimit(2);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(AccumulationFragment.newInstance());
        fragments.add(BusinessFragment.newInstance());
        fragments.add(GroupFragment.newInstance());
        TabPageFragment adapter = new TabPageFragment(getSupportFragmentManager(), fragments, new String[]{"公积金贷款", "商业贷款", "组合贷款"});
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void setActionBarDetail() {
    }

    @Override
    public void onSuccess(String response, int requestType) {
        List<ResultRate> data = new Gson().fromJson(response, new TypeToken<List<ResultRate>>() {
        }.getType());
        if (data == null || data.size() == 0)
            return;
        switch (requestType) {
            case 1:
                rate1 = Float.parseFloat(data.get(0).rateNum);
                break;
            case 2:
                rate2 = Float.parseFloat(data.get(0).rateNum);
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
