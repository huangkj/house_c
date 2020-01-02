package com.easylife.house.customer.ui.mine.prize;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.MyPrizeAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.MyPrizeBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.config.Constants.PAGE_SIZE;

/**
 * - @Description:  我的奖品
 * - @Author:  hkj
 * - @Time:  2018/9/18 16:54
 */
public class PrizeActivity extends BaseActivity implements RequestManagerImpl, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.rcvPrize)
    RecyclerView rcvPrize;
    @Bind(R.id.swipePrize)
    SwipeRefreshLayout swipePrize;
    private MyPrizeAdapter myPrizeAdapter;
    private int pageNum = 1;
    private List<MyPrizeBean> beanList;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_prize, null);
    }

    @Override
    protected void initView() {


        myPrizeAdapter = new MyPrizeAdapter(R.layout.my_prize_item, null);

        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_pub, rlEmpty, false);
        ((TextView) emptyView.findViewById(R.id.tvEmpty)).setText("暂无奖品");
        myPrizeAdapter.setEmptyView(emptyView);

        rcvPrize.setLayoutManager(new LinearLayoutManager(this));
        rcvPrize.setAdapter(myPrizeAdapter);

        swipePrize.post(new Runnable() {
            @Override
            public void run() {
                swipePrize.setRefreshing(true);
                getMyPrizeList();
            }
        });


        swipePrize.setOnRefreshListener(this);
        myPrizeAdapter.setOnLoadMoreListener(this, rcvPrize);
        myPrizeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PrizeDetailActivity.startActivity(PrizeActivity.this, ((MyPrizeBean) adapter.getItem(position)), 0);
            }
        });

    }

    private void getMyPrizeList() {
        mDao.myPrize(0, pageNum, PAGE_SIZE, this);
    }

    @Override
    protected void setActionBarDetail() {

    }


    @Override
    public void onSuccess(String response, int requestType) {
        if (swipePrize != null && swipePrize.isRefreshing()) {
            swipePrize.setRefreshing(false);
        }
        List<MyPrizeBean> beanList = new Gson().fromJson(response, new TypeToken<List<MyPrizeBean>>() {
        }.getType());

        this.beanList = beanList;
        if (pageNum == Constants.PAGE_START) {
            myPrizeAdapter.setNewData(beanList);
            myPrizeAdapter.setEnableLoadMore(true);
        } else {
            myPrizeAdapter.addData(beanList);
            myPrizeAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        if (swipePrize != null && swipePrize.isRefreshing()) {
            swipePrize.setRefreshing(false);
        }

        if (pageNum != 1) {
            myPrizeAdapter.loadMoreFail();
        }


    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        getMyPrizeList();
    }

    @Override
    public void onLoadMoreRequested() {
        rcvPrize.post(new Runnable() {
            @Override
            public void run() {
                if (beanList != null && beanList.size() >= Constants.PAGE_SIZE) {
                    pageNum++;
                    getMyPrizeList();
                } else {
                    myPrizeAdapter.loadMoreEnd();
                }
            }
        });
    }
}
