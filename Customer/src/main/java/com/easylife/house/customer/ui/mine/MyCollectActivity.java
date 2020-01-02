package com.easylife.house.customer.ui.mine;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.TabPageFragment;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.ui.mine.collection.houselist.HouseListFragment;
import com.easylife.house.customer.ui.mine.collection.housetypelist.HouseTypeListFragment;
import com.easylife.house.customer.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Mars on 2017/3/21 14:58.
 * 描述：我的收藏
 */

public class MyCollectActivity extends BaseActivity {
    @Bind(R.id.viewpager)
    NoScrollViewPager viewPager;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_collect, null);
    }

    @Override
    protected void initView() {
        viewPager.setOffscreenPageLimit(2);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HouseListFragment.newInstance());
        fragments.add(HouseTypeListFragment.newInstance());
        TabPageFragment adapter = new TabPageFragment(getSupportFragmentManager(), fragments, new String[]{"楼盘", "户型"});
        viewPager.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.raBtnDev) {
                    viewPager.setCurrentItem(0);
                } else {
                    viewPager.setCurrentItem(1);
                }
            }
        });
    }

    @Override
    protected void setActionBarDetail() {
        layActionBar.setVisibility(View.GONE);
    }

    @OnClick({R.id.btnBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
