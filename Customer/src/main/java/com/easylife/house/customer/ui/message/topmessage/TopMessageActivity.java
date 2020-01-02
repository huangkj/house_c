package com.easylife.house.customer.ui.message.topmessage;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * - @Description:  头条资讯v3
 * - @Author:  hkj
 * - @Time:  2018/9/17 14:20
 */
public class TopMessageActivity extends BaseActivity {


    private String[] titles = {"房产头条", "国家政策", "房市动态", "新闻资讯"};
    private ArrayList<Fragment> fragments = new ArrayList();
    @Bind(R.id.tabMessage)
    TabLayout tabMessage;
    @Bind(R.id.vpMessage)
    ViewPager vpMessage;
    private int currentTab;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_top_message, null);
    }

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, TopMessageActivity.class)
                , requestCode);
    }

    public static void startActivity(Activity activity, int currentTab, int requestCode) {
        activity.startActivityForResult(new Intent(activity, TopMessageActivity.class)
                        .putExtra("currentTab", currentTab)
                , requestCode);
    }


    @Override
    protected void initView() {
        currentTab = getIntent().getIntExtra("currentTab", 0);

        for (int i = 0; i < titles.length; i++) {
            tabMessage.addTab(tabMessage.newTab().setText(titles[i]));
        }
        fragments.add(TopMessageFragment.newInstance("1"));
        fragments.add(TopMessageFragment.newInstance("2"));
        fragments.add(TopMessageFragment.newInstance("3"));
        fragments.add(TopMessageFragment.newInstance("4"));
        MyViewPagerAdapter vpAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        vpMessage.setAdapter(vpAdapter);
        tabMessage.setupWithViewPager(vpMessage);
        vpMessage.setCurrentItem(currentTab);
        vpMessage.setOffscreenPageLimit(titles.length);

    }

    @Override
    protected void setActionBarDetail() {

    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

}
