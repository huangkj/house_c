package com.easylife.house.customer.ui.mine.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.MainPagerAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.ui.mine.order.orderlist.OrderListFragment;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_ORDER_STATUS;

/**
 * Created by Mars on 2017/6/19 14:24.
 * 描述：订单列表
 */

public class OrderListActivity extends BaseActivity {
    //    @Bind(R.id.btnAll)
//    RadioButton btnAll;
//    @Bind(R.id.btnWaitPay)
//    RadioButton btnWaitPay;
//    @Bind(R.id.btnHadPay)
//    RadioButton btnHadPay;
//    @Bind(R.id.btnHadSign)
//    RadioButton btnHadSign;
//    @Bind(R.id.btnRefund)
//    RadioButton btnRefund;
    @Bind(R.id.layContent)
    ViewPager layContent;
    @Bind(R.id.tagLayout)
    TabLayout tagLayout;


    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, OrderListActivity.class), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.order_activity_list, null);
    }

    private MainPagerAdapter adapter;

    @Override
    protected void initView() {
        List<String> titles = new ArrayList<>();
        titles.add("全部");
        titles.add("已下单");
        titles.add("已付款");
        titles.add("已签约");
        titles.add("退款");
        tagLayout.setupWithViewPager(layContent);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(OrderListFragment.newInstance(""));
        fragments.add(OrderListFragment.newInstance("0"));
        fragments.add(OrderListFragment.newInstance("1"));
        fragments.add(OrderListFragment.newInstance("2"));
        fragments.add(OrderListFragment.newInstance("3,4,5"));
        adapter = new MainPagerAdapter(fragments, titles, getSupportFragmentManager());
        layContent.setAdapter(adapter);
    }

    @Override
    protected void setActionBarDetail() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == UPDATE_ORDER_STATUS) {
            int orderType = (Integer) event.obj;
            //  PLACE(0, "已下单"), PAYED(1, "已付款"), SIGNED(2, "已签约"),
            //  REFUND(3, "已退款"), REFUNDING(4, "退款中"), REFUNDFIAL(5, "退款失败"), DEL(9, "已删除");
            if (Order.OrderType.PLACE.code == orderType) {
                //  订单状态无变化
            } else if (Order.OrderType.PAYED.code == orderType) {
                layContent.setCurrentItem(2);
            } else if (Order.OrderType.SIGNED.code == orderType) {
                layContent.setCurrentItem(3);
            } else if (Order.OrderType.REFUND.code == orderType) {
                layContent.setCurrentItem(4);
            } else if (Order.OrderType.REFUNDING.code == orderType) {
                layContent.setCurrentItem(4);
            } else if (Order.OrderType.REFUNDFIAL.code == orderType) {
                layContent.setCurrentItem(4);
            } else if (Order.OrderType.DEL.code == orderType) {
                layContent.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
