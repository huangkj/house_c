package com.easylife.house.customer.ui.houses.exportshop.hisdynamic;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ExportHisDynamicAdapter;
import com.easylife.house.customer.bean.DynamicUrlPagerBean;
import com.easylife.house.customer.bean.ExportHisDymanicBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * TA的动态
 * zgm
 */
public class HisDynamicActivity extends MVPBaseActivity<HisDynamicContract.View, HisDynamicPresenter> implements
        HisDynamicContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.activity_export_comment_list)
    RelativeLayout rlEmpty;

    private String brokeCode;
    List<ExportHisDymanicBean> hisDymanicBeanist = new ArrayList<>();
    private ExportHisDynamicAdapter mAdapter;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.activity_export_comment_list, null);
    }

    @Override
    protected void initView() {
        brokeCode = getIntent().getStringExtra("BROKECODE");
        showLoading();
        mPresenter.requestHisDymanic(brokeCode, page + "");
        mAdapter = new ExportHisDynamicAdapter(R.layout.export_his_dymanic, hisDymanicBeanist);
        mAdapter.openLoadAnimation();
//        mAdapter.openLoadMore(Constants.PAGE_SIZE);
        mAdapter.setOnLoadMoreListener(this, recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(mAdapter);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, rlEmpty, false);
        mAdapter.setEmptyView(emptyView);


        mAdapter.setCommentImgOnclickListenear(new ExportHisDynamicAdapter.CommentImgOnclickListenear() {
            @Override
            public void setCommentOnclick(int position, ExportHisDymanicBean exportHisDymanicBean) {
                DynamicUrlPagerBean urlBean = new DynamicUrlPagerBean();
                urlBean.position = position;
                for (int i = 0; i < exportHisDymanicBean.contentUrl.size(); i++) {
                    urlBean.urlList.add(exportHisDymanicBean.contentUrl.get(i).url);
                }
                startActivity(new Intent(HisDynamicActivity.this, DynamicPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
            }
        });
    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("TA的动态");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {
        showLoading();
        mPresenter.requestHisDymanic(brokeCode, page + "");
    }

    @Override
    public void showDynamic(List<ExportHisDymanicBean> exportHisDymanicBeanList) {
        if (dialog.isShowing()) {
            cancelLoading();
        }
        hisDymanicBeanist = exportHisDymanicBeanList;
        if (page == Constants.PAGE_START) {
            mAdapter.setNewData(hisDymanicBeanist);
        } else {
            mAdapter.addData(hisDymanicBeanist);
            mAdapter.loadMoreComplete();
        }

    }

    @Override
    public void showFail(String msg) {
        if (dialog.isShowing()) {
            cancelLoading();
        }

        CustomerUtils.showTip(this, msg);
    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void onLoadMoreRequested() {
        recycle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hisDymanicBeanist != null && hisDymanicBeanist.size() >= Constants.PAGE_SIZE) {
                    page++;
                    mPresenter.requestHisDymanic(brokeCode, page + "");
                } else {
                    mAdapter.loadMoreEnd();
                    CustomerUtils.showTip(HisDynamicActivity.this, "没有更多数据了");
                }
            }
        },500);
    }
}
