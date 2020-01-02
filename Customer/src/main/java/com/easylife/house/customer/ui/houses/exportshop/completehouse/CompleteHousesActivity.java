package com.easylife.house.customer.ui.houses.exportshop.completehouse;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ExportCompleteHomeAdapter;
import com.easylife.house.customer.bean.ExportCompeleteHomeBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 成交房源列表
 * Zgm
 */
public class CompleteHousesActivity extends MVPBaseActivity<CompleteHouseContract.View, CompleteHousePresenter> implements CompleteHouseContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.activity_export_comment_list)
    RelativeLayout rlEmpty;

    private String brokeCode;

    List<ExportCompeleteHomeBean> houeseList = new ArrayList<>();
    private ExportCompleteHomeAdapter mAdapter;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.activity_export_comment_list, null);
    }

    @Override
    protected void initView() {
        brokeCode = getIntent().getStringExtra("BROKECODE");
        showLoading();
        mPresenter.requestCompleteHome(brokeCode,"1");
        mAdapter = new ExportCompleteHomeAdapter(R.layout.export_complete_homes,houeseList);
        mAdapter.openLoadAnimation();
//        mAdapter.openLoadMore(Constants.PAGE_SIZE);
        mAdapter.setOnLoadMoreListener(this, recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(mAdapter);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view,rlEmpty,false);
        mAdapter.setEmptyView(emptyView);
    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("成交房源");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {
        showLoading();
        mPresenter.requestCompleteHome(brokeCode,"1");
    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void onLoadMoreRequested() {
        recycle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(houeseList != null && houeseList.size() >= Constants.PAGE_SIZE){
                    page++;
                    mPresenter.requestCompleteHome(brokeCode,page+"");
                }else {
                    mAdapter.loadMoreEnd();
                    CustomerUtils.showTip(CompleteHousesActivity.this,"没有更多数据了");
                }
            }
        },500);
    }

    @Override
    public void showCompleteHome(List<ExportCompeleteHomeBean> saleHousesBeanList) {

        if(dialog.isShowing()){
            cancelLoading();
        }

        houeseList = saleHousesBeanList;
        if(page == Constants.PAGE_START){
            mAdapter.setNewData(houeseList);
        }else {
            mAdapter.addData(houeseList);
            mAdapter.loadMoreComplete();
        }

    }

    @Override
    public void showFail(String msg) {
        if(dialog.isShowing()){
            cancelLoading();
        }
        CustomerUtils.showTip(this,msg);
    }
}
