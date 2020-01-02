package com.easylife.house.customer.ui.houses.exportshop.salehouses;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ExportShopHouseAdapter;
import com.easylife.house.customer.bean.ExportSaleHousesBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 所售楼盘
 * zgm
 */
public class SaleHousesActivity extends MVPBaseActivity<SaleHousesContract.View, SaleHousesPresenter> implements SaleHousesContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycle)
    RecyclerView recycle;
//    @Bind(R.id.imgEmpty)
//    ImageView imgEmpty;
    @Bind(R.id.activity_export_comment_list)
    RelativeLayout rlEmpty;
    private String brokeCode;
    private List<ExportSaleHousesBean> saleHousesBeanList = new ArrayList<>();
    private ExportShopHouseAdapter houseAdapter;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.activity_export_comment_list, null);
    }

    @Override
    protected void initView() {
        brokeCode = getIntent().getStringExtra("BROKECODE");
        showLoading();
        mPresenter.requestSaleHouses(brokeCode,page+"");
        houseAdapter = new ExportShopHouseAdapter(R.layout.export_complete_homes, saleHousesBeanList);
        houseAdapter.openLoadAnimation();
//        houseAdapter.openLoadMore(Constants.PAGE_SIZE);
        houseAdapter.setOnLoadMoreListener(this, recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(houseAdapter);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view,rlEmpty,false);
        houseAdapter.setEmptyView(emptyView);
    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("所售楼盘");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {
        showLoading();
        mPresenter.requestSaleHouses(brokeCode,page+"");
    }

    @Override
    public void showTip(String msg) {

    }

    @Override
    public void onLoadMoreRequested() {
        recycle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(saleHousesBeanList != null && saleHousesBeanList.size() >= Constants.PAGE_SIZE){
                    page++;
                    mPresenter.requestSaleHouses(brokeCode,page+"");
                }else {
                    houseAdapter.loadMoreEnd();
                    CustomerUtils.showTip(SaleHousesActivity.this,"没有更多数据了");
                }
            }
        },500);
    }

    @Override
    public void showHousesData(List<ExportSaleHousesBean> saleHousesBeanList) {
        if(dialog.isShowing()){
            cancelLoading();
        }

        this.saleHousesBeanList = saleHousesBeanList;

        if(page == Constants.PAGE_START){
            houseAdapter.setNewData(saleHousesBeanList);
        }else {
            houseAdapter.addData(saleHousesBeanList);
            houseAdapter.loadMoreComplete();
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
