package com.easylife.house.customer.ui.houses.housesdetail.exportlist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ExportListAdapter;
import com.easylife.house.customer.bean.ExportListBean;
import com.easylife.house.customer.bean.HouseSaleListBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.exportshop.shop.ExportShopActivity;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

import butterknife.Bind;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 专家团队列表
 */
@RuntimePermissions
public class ExportActivity extends MVPBaseActivity<ExportContract.View, ExportPresenter> implements ExportContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.rl_empty)
    RelativeLayout rlEmpty;
    private String dev_id;
    private HousesDetailBaseBean baseBean;


    private ExportListAdapter mAdapter;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(getApplicationContext(), R.layout.activity_export, null);
    }

    @Override
    protected void initView() {
        baseBean = (HousesDetailBaseBean) getIntent().getSerializableExtra("BASE_BEAN");
        if(baseBean != null){
            dev_id = baseBean.devId;
        }
        showLoading();
        mPresenter.getExportList(dev_id,"1");
        mAdapter = new ExportListAdapter(R.layout.export_layout_item,exportList);
//        mAdapter.openLoadMore(Constants.PAGE_SIZE);
        mAdapter.openLoadAnimation();
        mAdapter.setOnLoadMoreListener(this, recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(mAdapter);
        //设置空数据view
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view,rlEmpty,false);
        mAdapter.setEmptyView(emptyView);

        mAdapter.setOnPhoneClickLisenear(new ExportListAdapter.onPhoneClickLisenear() {
            @Override
            public void phoneClick(String phone) {
                call(phone);
            }

            @Override
            public void exportPage(String brokeCode,String phone) {
                startActivity(new Intent(ExportActivity.this, ExportShopActivity.class).putExtra("BROKECODE", brokeCode).putExtra("PHONE",phone));
            }

            @Override
            public void imChat(String imUsername,String brokeCode,String brokerName,String phone) {
                //进入IM聊天

                if(!TextUtils.isEmpty(imUsername)){

                    HouseSaleListBean houseSaleListBean = null;
                    if(baseBean != null){
                        houseSaleListBean = new HouseSaleListBean();
                        houseSaleListBean.name = baseBean.devName;
                        houseSaleListBean.propertyType = baseBean.propertyType;
                        houseSaleListBean.address = baseBean.addressDistrict+baseBean.addressTown;
                        houseSaleListBean.averPrice = baseBean.averPrice;
                        if(baseBean.distribution != null && baseBean.distribution.size() != 0){
                            houseSaleListBean.imgUrl = baseBean.distribution.get(0).thumbnailImage;
                        }
                        houseSaleListBean.devId = baseBean.devId;
                        houseSaleListBean.share = baseBean.share;
                    }

                    if(dao.isLogin()){
                     /*   startActivity(new Intent(ExportActivity.this, ChatActivity.class).putExtra("userId", imUsername)
                                .putExtra("IM_BUILD",houseSaleListBean)
                                .putExtra("IM_TYPE",1));
                        dao.pointIM(baseBean.devId,baseBean.devName,brokeCode,brokerName);*/
                    }else {
//                        LoginContentActivity.startActivity(ExportActivity.this, 0);
                    }
                }
            }
        });
    }

    /**
     * 显示拨号提示框
     *
     * @param phone
     */


    public void call(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            CustomerUtils.showTip(this, "手机号错误");
            return;
        }
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否拨打 " + phone)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExportActivityPermissionsDispatcher.jumpCallPhoneWithCheck(ExportActivity.this,phone);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void jumpCallPhone(String phone){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

      /**
     * 6.0权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ExportActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("专家团队");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {
        showLoading();
        mPresenter.getExportList(dev_id,"1");
    }

    @Override
    public void showTip(String msg) {

    }

    List<ExportListBean> exportList;
    @Override
    public void showExportList(List<ExportListBean> exportList) {
        cancelLoading();
        this.exportList = exportList;
        if(page == Constants.PAGE_START){
            mAdapter.setNewData(exportList);
        }else {
            mAdapter.addData(exportList);
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
        CustomerUtils.showTip(this,msg);
    }

    @Override
    public void onLoadMoreRequested() {
        recycle.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (exportList != null && exportList.size() >= Constants.PAGE_SIZE) {
                    page++;
                    mPresenter.getExportList(dev_id,page+"");
                }else {
                    mAdapter.loadMoreEnd();
                    CustomerUtils.showTip(ExportActivity.this,"没有更多了");
                }
            }
        },500);

    }
}
