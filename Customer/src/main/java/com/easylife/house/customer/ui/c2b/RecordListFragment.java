package com.easylife.house.customer.ui.c2b;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.RecordListAdapter;
import com.easylife.house.customer.adapter.ItemClickListener;
import com.easylife.house.customer.base.BaseFragment;
import com.easylife.house.customer.bean.Record;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.util.IntentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Mars on 2017/10/18 15:47.
 * 描述：客户列表
 */
public class RecordListFragment extends BaseFragment {
    @Bind(R.id.recyclePub)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;

    public static RecordListFragment newInstance(String type) {
        RecordListFragment fragment = new RecordListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.pub_activity_recyclerview;
    }

    private String type;
    private RecordListAdapter adapter;
    private boolean isEnd;

    @Override
    public void initViews() {

        type = getArguments().getString("type");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecordListAdapter(R.layout.item_record_list, type, null);
        adapter.setItemClickListener(new ItemClickListener<Record>() {
            @Override
            public void itemClick(int viewId, int actionType, Record data) {
                if (data == null)
                    return;
                switch (actionType) {
                    case 0:
                        // 跳转详情
                        RecordDetailActivity.startActivity(getActivity(), data.id, 0);
                        break;
                    case 1:
                        // 发短信
                        IntentUtils.sendMessage(getActivity(), data.phone, null);
                        break;
                    case 2:
                        // 打电话
                        ((MyRecommendActivity) getActivity()).call(data.phone);
                        break;
                    case 3:
                        // 推荐，已到访-已过期   显示再报备   TODO
                        RecordAgainActivity.startActivity(getActivity(), data, 0);
                        break;
                    case 4:
                        // 已认购，已签约-可提现        显示结佣   TODO
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isEnd = false;
                page = Constants.PAGE_START;
                getNetData();
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
                            adapter.loadMoreComplete();
                        } else {
                            getNetData();
                        }
                    }
                }, 1000);
            }
        }, mRecyclerView);
    }

    private void getNetData() {
        // TODO  获取接口数据

        List<Record> data = new ArrayList<>();
        data.add(new Record());
        data.add(new Record());
        data.add(new Record());

        initData(data);
    }

    private void initData(List<Record> data) {
        swipeLayout.setRefreshing(false);
        adapter.loadMoreComplete();
        if (page == Constants.PAGE_START) {
            adapter.setNewData(data);
        } else {
            adapter.addData(data);
        }
        if (data != null && data.size() == Constants.PAGE_SIZE) {
            page++;
        } else {
            isEnd = true;
        }

    }


}
