package com.easylife.house.customer.ui.pub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easylife.house.customer.R;
import com.easylife.house.customer.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mars on 2017/4/10 20:11.
 * 描述：引导页
 */

public class GuideActivity extends AppCompatActivity {

    private ViewPager pager;
    private List<View> viewContainter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiUtil.setNoTitle(this);
        setContentView(R.layout.pub_activity_guide);

        pager = (ViewPager) findViewById(R.id.pager);

        viewContainter = new ArrayList<>();
        View view1 = LayoutInflater.from(this).inflate(R.layout.pub_activity_guide_1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.pub_activity_guide_2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.pub_activity_guide_3, null);
        View view4 = LayoutInflater.from(this).inflate(R.layout.pub_activity_guide_4, null);
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewContainter.add(view3);
        viewContainter.add(view4);

        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

        pager.setAdapter(new PagerAdapter() {

            //viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }
        });

    }
}
