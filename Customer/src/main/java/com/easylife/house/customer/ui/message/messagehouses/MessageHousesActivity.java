package com.easylife.house.customer.ui.message.messagehouses;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.DevSubscriptionsAdapter;
import com.easylife.house.customer.bean.MsgDevSub;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * 楼盘资讯列表
 */
public class MessageHousesActivity extends MVPBaseActivity<MessageHousesContract.View, MessageHousesPresenter> implements MessageHousesContract.View {

    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.recyclePub)
    RecyclerView mRecyclerView;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;
    List<MsgDevSub> data = new ArrayList<>();

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_activity_recyclerview, null);
    }

    private DevSubscriptionsAdapter adapter;
    private int page = 0;

    @Override
    protected void initView() {
        hideNoNetView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DevSubscriptionsAdapter(activity, R.layout.item_devsubscription, null);
        adapter.openLoadAnimation();
//        adapter.openLoadMore(Constants.PAGE_SIZE);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (data != null && data.size() >= Constants.PAGE_SIZE) {
                            page++;
                            mPresenter.getNetData(page);
                        } else {
                            adapter.loadMoreEnd();
                            CustomerUtils.showTip(activity, "没有更多了");
                        }
                    }
                }, 500);
            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MsgDevSub dev = adapter.getItem(i);
                if (dev == null || TextUtils.isEmpty(dev.devId)) {
                    showTip("错误数据");
                    return;
                }
                HouseDetailActivity.startActivity(activity, dev.devId, false, 0);
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = Constants.PAGE_START;
                mPresenter.getNetData(page);
            }
        });
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, rlEmpty, false);
        adapter.setEmptyView(emptyView);
        mPresenter.getNetData(page);
    }

    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @Override
    public void initData(List<MsgDevSub> data) {
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
        this.data = data;
        if (page == Constants.PAGE_START) {
            adapter.setNewData(data);
        } else {
            adapter.addData(data);
            adapter.loadMoreComplete();
        }
    }
}
