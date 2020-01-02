package com.easylife.house.customer.ui.mine.order.orderlist;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.OrdersAdapter;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.util.CustomerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_ORDER_STATUS;

/**
 * 订单列表子页面
 */
public class OrderListFragment extends MVPBaseFragment<OrderListContract.View, OrderListPresenter> implements OrderListContract.View {
    @Bind(R.id.recyclePub)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;

    public static OrderListFragment newInstance(String type) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }

    private String type;
    private OrdersAdapter adapter;
    private boolean isEnd;

    @Override
    public void initViews() {
        type = getArguments().getString("type");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new OrdersAdapter(this, getActivity(), R.layout.item_order_mine, null);
        mRecyclerView.setAdapter(adapter);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isEnd = false;
                page = Constants.PAGE_START;
                mPresenter.getNetData(type, page);
            }
        });
        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, rlEmpty, false);
        adapter.setEmptyView(emptyView);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isEnd) {
                            adapter.loadMoreEnd();
                        } else {
                            mPresenter.getNetData(type, page);
                        }
                    }
                }, 1000);
            }
        }, mRecyclerView);
        mPresenter.getNetData(type, page);
    }


    @Override
    public void initData(List<Order> data) {
        swipeLayout.setRefreshing(false);
        adapter.loadMoreComplete();
        if (page == Constants.PAGE_START) {
            adapter.setNewData(data);
        } else {
            adapter.addData(data);
            adapter.loadMoreComplete();
        }
        if (data != null && data.size() == Constants.PAGE_SIZE) {
            page++;
        } else {
            isEnd = true;
        }
    }

    @Override
    public void getDataFail() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public int getLayout() {
        return R.layout.pub_activity_recyclerview;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == UPDATE_ORDER_STATUS) {
            int orderType = (Integer) event.obj;

            isEnd = false;
            page = Constants.PAGE_START;
            mPresenter.getNetData(orderType + "", page);

            if ((Order.OrderType.PAYED.code == orderType && "1".equals(type))
                    || (Order.OrderType.SIGNED.code == orderType && "2".equals(type))
                    || (Order.OrderType.REFUNDFIAL.code == orderType && "3,4,5".equals(type))) {
                isEnd = false;
                page = Constants.PAGE_START;
                mPresenter.getNetData(type, page);
            } else if ((Order.OrderType.DEL.msg.equals(orderType) && ("".equals(type) ||
                    "0".equals(type)))) {
                isEnd = false;
                page = Constants.PAGE_START;
                mPresenter.getNetData(type, page);
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
