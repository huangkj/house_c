package com.easylife.house.customer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class HousesPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragmentsList;
    List<String> mTitles;

    public HousesPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList, ArrayList<String> titles) {
        super(fm);
        this.fragmentsList = fragmentsList;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
