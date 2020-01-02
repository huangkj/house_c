package com.easylife.house.customer.ui.mine.collection.houselist;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.HouseCollectAdapter;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class HouseListFragment extends MVPBaseFragment<HouseListContract.View, HouseListPresenter> implements HouseListContract.View {

    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.recyclePub)
    RecyclerView mRecyclerView;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;
    List<HousesDetailBaseBean> apps = new ArrayList<>();

    public static HouseListFragment newInstance() {
        HouseListFragment fragment = new HouseListFragment();
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.pub_activity_recyclerview;
    }

    private HouseCollectAdapter adapter;

    @Override
    public void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HouseCollectAdapter(R.layout.item_houses, null);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if(apps.size() == 0){
                    return;
                }
                HouseDetailActivity.startActivity(getActivity(), apps.get(position).id, true, 2);
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getNetData();
            }
        });
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_view,rlEmpty,false);
        adapter.setEmptyView(emptyView);
        mPresenter.getNetData();
    }

    @Override
    public void initData(List<HousesDetailBaseBean> apps) {
        this.apps = apps;
        swipeLayout.setRefreshing(false);
        adapter.setNewData(apps);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getNetData();
    }

    @Override
    public void showFail() {
        if(swipeLayout.isRefreshing()){
            swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            swipeLayout.setRefreshing(true);
            mPresenter.getNetData();
        }
    }
}
