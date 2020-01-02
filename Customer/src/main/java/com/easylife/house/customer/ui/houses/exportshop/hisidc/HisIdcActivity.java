package com.easylife.house.customer.ui.houses.exportshop.hisidc;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ExportHisIdcAdapter;
import com.easylife.house.customer.bean.DynamicUrlPagerBean;
import com.easylife.house.customer.bean.ExportHisIdcBean;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.exportshop.hisdynamic.DynamicPagerActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * TA的证书
 * zgm
 */
public class HisIdcActivity extends MVPBaseActivity<HisIdcContract.View, HisIdcPresenter> implements HisIdcContract.View {

    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.activity_export_comment_list)
    RelativeLayout rlEmpty;
    private String brokeCode;
    List<ExportHisIdcBean> exportHisIdcBeanList = new ArrayList<>();
    private ExportHisIdcAdapter mAdapter;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.activity_export_comment_list, null);
    }

    @Override
    protected void initView() {
        brokeCode = getIntent().getStringExtra("BROKECODE");
        showLoading();
        mPresenter.requestHisIdc(brokeCode);
        mAdapter = new ExportHisIdcAdapter(R.layout.export_his_idc_list, exportHisIdcBeanList);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(mAdapter);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view,rlEmpty,false);
        mAdapter.setEmptyView(emptyView);

        mAdapter.setCommentImgOnclickListenear(new ExportHisIdcAdapter.CommentImgOnclickListenear() {
            @Override
            public void setCommentOnclick(int position, ExportHisIdcBean exportHisDymanicBean) {
                DynamicUrlPagerBean urlBean = new DynamicUrlPagerBean();
                urlBean.position = position;
                urlBean.urlList.add(exportHisDymanicBean.url);
                startActivity(new Intent(HisIdcActivity.this, DynamicPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
            }
        });

    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("TA的证书");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {
        showLoading();
        mPresenter.requestHisIdc(brokeCode);
    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void showIdcData(List<ExportHisIdcBean> exportHisIdcBeanList) {
        if (dialog.isShowing()) {
            cancelLoading();
        }
        mAdapter.setNewData(exportHisIdcBeanList);


    }

    @Override
    public void showFail(String msg) {
        if (dialog.isShowing()) {
            cancelLoading();
        }
        CustomerUtils.showTip(this, msg);
    }
}
