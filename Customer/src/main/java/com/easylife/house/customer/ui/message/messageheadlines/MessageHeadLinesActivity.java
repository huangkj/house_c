package com.easylife.house.customer.ui.message.messageheadlines;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.HeadLinesAdapter;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.config.Constants.URL_WEB_BASE;

/**
 * 头条列表 -
 */
public class MessageHeadLinesActivity extends MVPBaseActivity<MessageHeadLinesContract.View, MessageHeadLinesPresenter> implements MessageHeadLinesContract.View {

    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.recyclePub)
    RecyclerView mRecyclerView;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;


    List<MsgHeadLine> data = new ArrayList<>();

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_activity_recyclerview, null);
    }

    private HeadLinesAdapter adapter;
    private int page = Constants.PAGE_START;

    @Override
    protected void initView() {
        hideNoNetView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HeadLinesAdapter(R.layout.item_headline, null);
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
                }, 300);

            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mPresenter.requestCount(adapter.getItem(i).id);
                WebViewActivity.startActivity(activity, "资讯", URL_WEB_BASE + adapter.getItem(i).id, true, adapter.getItem(i));
            }
        });
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, rlEmpty, false);
        adapter.setEmptyView(emptyView);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = Constants.PAGE_START;
                mPresenter.getNetData(page);
                swipeLayout.setRefreshing(false);
            }
        });
        mPresenter.getCacheData();
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
    public void initData(List<MsgHeadLine> data) {
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
