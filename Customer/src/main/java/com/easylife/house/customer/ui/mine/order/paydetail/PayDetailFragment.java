package com.easylife.house.customer.ui.mine.order.paydetail;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.PayDetailAdapter;
import com.easylife.house.customer.bean.PayDetail;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

import butterknife.Bind;

/**
 * 支付明细
 */
public class PayDetailFragment extends MVPBaseFragment<PayDetailContract.View, PayDetailPresenter> implements PayDetailContract.View {
    @Bind(R.id.recyclePub)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;

    public static PayDetailFragment newInstance(String type, String orderCode) {
        PayDetailFragment fragment = new PayDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("orderCode", orderCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }

    private String type;
    private String orderCode;
    private PayDetailAdapter adapter;

    @Override
    public void initViews() {
        type = getArguments().getString("type");
        orderCode = getArguments().getString("orderCode");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PayDetailAdapter(R.layout.item_pay_detail, null);
        mRecyclerView.setAdapter(adapter);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNetData(type,orderCode);
            }
        });
        mPresenter.getNetData(type,orderCode);
    }

    @Override
    public void initData(List<PayDetail> data) {
        swipeLayout.setRefreshing(false);
        adapter.setNewData(data);
    }

    @Override
    public void getDataFail() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public int getLayout() {
        return R.layout.pub_activity_recyclerview;
    }

}
