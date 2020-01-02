package com.easylife.house.customer.ui.mine.recommenapp;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.RecommenAppAdapter;
import com.easylife.house.customer.bean.RecommenApp;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

import butterknife.Bind;

/**
 * 推荐应用
 */
public class RecommenAppActivity extends MVPBaseActivity<RecommenAppContract.View, RecommenAppPresenter> implements RecommenAppContract.View {
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.recyclePub)
    RecyclerView mRecyclerView;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_activity_recyclerview, null);
    }

    private RecommenAppAdapter adapter;

    @Override
    protected void initView() {
        hideNoNetView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecommenAppAdapter(R.layout.item_recommen_app, null);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                WebViewActivity.startActivity(activity, adapter.getItem(i).title, adapter.getItem(i).url);
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNetData();
            }
        });
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view,rlEmpty,false);
        adapter.setEmptyView(emptyView);
        mPresenter.getNetData();
    }


    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void initData(List<RecommenApp> apps) {
        swipeLayout.setRefreshing(false);
        adapter.setNewData(apps);
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }
}
