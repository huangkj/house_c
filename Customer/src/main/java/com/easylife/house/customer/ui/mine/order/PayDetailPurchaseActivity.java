package com.easylife.house.customer.ui.mine.order;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.MainPagerAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.ui.mine.order.paydetail.PayDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Mars on 2017/7/4 14:27.
 * 描述：认购支付明细
 */

public class PayDetailPurchaseActivity extends BaseActivity {
    public static void startActivity(Activity activity, String orderCode, String orderName, String pay, String status, int requestCode) {
        activity.startActivityForResult(new Intent(activity, PayDetailPurchaseActivity.class)
                        .putExtra("orderCode", orderCode)
                        .putExtra("orderName", orderName)
                        .putExtra("pay", pay)
                        .putExtra("status", status)
                , requestCode
        );
    }

    @Bind(R.id.raBtnRaise)
    RadioButton raBtnRaise;
    @Bind(R.id.raBtnPurchase)
    RadioButton raBtnPurchase;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;
    @Bind(R.id.tvOrderName)
    TextView tvOrderName;
    @Bind(R.id.tvOrderStatus)
    TextView tvOrderStatus;
    @Bind(R.id.tvOrderPrice)
    TextView tvOrderPrice;
    @Bind(R.id.tvOrderNo)
    TextView tvOrderNo;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.payment_activity_raise_pay_detail, null);
    }

    private MainPagerAdapter adapter;
    private String orderCode;
    private String orderName;
    private String pay;
    private String status;

    @Override
    protected void initView() {
        orderCode = getIntent().getStringExtra("orderCode");
        orderName = getIntent().getStringExtra("orderName");
        pay = getIntent().getStringExtra("pay");
        status = getIntent().getStringExtra("status");


        tvOrderName.setText(orderName);
        tvOrderNo.setText(orderCode);
        tvOrderPrice.setText(pay);
        tvOrderStatus.setText(status);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(PayDetailFragment.newInstance(Order.FollowType.RAISE.name(), orderCode));
        fragments.add(PayDetailFragment.newInstance(Order.FollowType.PURCHASE.name(), orderCode));
        adapter = new MainPagerAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        raBtnRaise.setChecked(true);
                        break;
                    case 1:
                        raBtnPurchase.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.raBtnRaise:
                        // 认筹
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.raBtnPurchase:
                        // 认购
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    @Override
    protected void setActionBarDetail() {

    }

}
