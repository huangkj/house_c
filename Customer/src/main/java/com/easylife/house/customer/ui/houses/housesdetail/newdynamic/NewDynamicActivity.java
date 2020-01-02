package com.easylife.house.customer.ui.houses.housesdetail.newdynamic;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.NewDynamicAdapter;
import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 最新动态
 */
public class NewDynamicActivity extends MVPBaseActivity<NewDynamicContract.View, NewDynamicPresenter> implements
        NewDynamicContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycle_dynamic)
    RecyclerView recycleDynamic;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private NewDynamicAdapter mAdapter;
    //    private DymanicBeanList dynamicBean;
    private String projectid;
    List<HousesDynamicBean> dynamicList = new ArrayList<>();

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_dynamic, null);
    }

    @Override
    protected void initView() {
        projectid = getIntent().getStringExtra("PROJECTID");
        showLoading();
        mPresenter.loadMoreData(projectid, page + "");
        mAdapter = new NewDynamicAdapter(R.layout.latest_my_dynamic_item, dynamicList);
        mAdapter.openLoadAnimation();
//        mAdapter.openLoadMore(Constants.PAGE_SIZE);
        mAdapter.setOnLoadMoreListener(this, recycleDynamic);
        recycleDynamic.setLayoutManager(new LinearLayoutManager(this));
        recycleDynamic.setAdapter(mAdapter);
        //设置空数据view
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, rlEmpty, false);
        mAdapter.setEmptyView(emptyView);
    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("最新动态");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {
        showLoading();
        mPresenter.loadMoreData(projectid, page + "");
    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void onLoadMoreRequested() {

        recycleDynamic.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dynamicList != null && dynamicList.size() >= Constants.PAGE_SIZE) {
                    page++;
                    Log.e("加载更多" + getClass(), "" + page);
                    mPresenter.loadMoreData(projectid, page + "");
                } else {
                    mAdapter.loadMoreEnd();
                }
            }
        }, 500);

    }

    @Override
    public void showMoreData(List<HousesDynamicBean> dynamicList) {
        cancelLoading();
        this.dynamicList = dynamicList;
        if (page == Constants.PAGE_START) {
            mAdapter.setNewData(dynamicList);
            mAdapter.setEnableLoadMore(true);
        } else {
            mAdapter.addData(dynamicList);
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
        CustomerUtils.showTip(this, msg);
    }

}
