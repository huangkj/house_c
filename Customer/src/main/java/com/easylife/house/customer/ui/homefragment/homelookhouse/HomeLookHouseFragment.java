package com.easylife.house.customer.ui.homefragment.homelookhouse;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.easylife.house.customer.R;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.homefragment.homelookhouse.buyhouse.BuyHouseFragment;
import com.easylife.house.customer.ui.homefragment.homelookhouse.livehouse.LiveHouseFragment;
import com.easylife.house.customer.ui.homefragment.homelookhouse.netbook.NetAppointmentFragment;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;

import butterknife.Bind;


public class HomeLookHouseFragment extends MVPBaseFragment<HomeLookHouseContract.View, HomeLookHousePresenter> implements HomeLookHouseContract.View {

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.id_viewpager)
    ViewPager viewPager;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public static HomeLookHouseFragment newInstance() {
        Bundle args = new Bundle();
        HomeLookHouseFragment fragment = new HomeLookHouseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }

    @Override
    public int getLayout() {
        return R.layout.home_fragment_look_house;
    }

    @Override
    public void initViews() {

        mTitles.add("网上预约");
        mTitles.add("现场看房");
        mTitles.add("买房成交");

        NetAppointmentFragment netBookFragment = NetAppointmentFragment.newInstance();
        LiveHouseFragment liveHouseFragment = LiveHouseFragment.newInstance();
        BuyHouseFragment buyHouseFragment = BuyHouseFragment.newInstance();

        fragmentsList.add(netBookFragment);
        fragmentsList.add(liveHouseFragment);
        fragmentsList.add(buyHouseFragment);

        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);

        LookPagerAdapter adapter = new LookPagerAdapter(getChildFragmentManager(),fragmentsList,mTitles);
        tablayout.setSelected(true);
        tablayout.setTabsFromPagerAdapter(adapter);
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);


    }


}
