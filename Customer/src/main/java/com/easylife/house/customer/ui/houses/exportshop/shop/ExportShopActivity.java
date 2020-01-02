package com.easylife.house.customer.ui.houses.exportshop.shop;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ExportCompleteHomeAdapter;
import com.easylife.house.customer.adapter.ExportHisDynamicAdapter;
import com.easylife.house.customer.adapter.ExportShopHouseAdapter;
import com.easylife.house.customer.adapter.UserCommentListAdapter;
import com.easylife.house.customer.bean.DynamicUrlPagerBean;
import com.easylife.house.customer.bean.ExportCompeleteHomeBean;
import com.easylife.house.customer.bean.ExportHisDymanicBean;
import com.easylife.house.customer.bean.ExportHisIdcBean;
import com.easylife.house.customer.bean.ExportSaleHousesBean;
import com.easylife.house.customer.bean.ExportShopBaseBean;
import com.easylife.house.customer.bean.ExportUserCommentBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.exportshop.commentlist.ExportCommentListActivity;
import com.easylife.house.customer.ui.houses.exportshop.completehouse.CompleteHousesActivity;
import com.easylife.house.customer.ui.houses.exportshop.hisdynamic.DynamicPagerActivity;
import com.easylife.house.customer.ui.houses.exportshop.hisdynamic.HisDynamicActivity;
import com.easylife.house.customer.ui.houses.exportshop.hisidc.HisIdcActivity;
import com.easylife.house.customer.ui.houses.exportshop.salehouses.SaleHousesActivity;
import com.easylife.house.customer.ui.houses.housesdetail.exportlist.exportComment.ExportCommentActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.view.photoview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 专家店铺
 * zgm
 */
@RuntimePermissions
public class ExportShopActivity extends MVPBaseActivity<ExportShopContract.View, ExportShopPresenter> implements ExportShopContract.View {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_head)
    RoundedImageView ivHead;
    @Bind(R.id.tv_good)
    TextView tvGood;
    @Bind(R.id.tv_good_value)
    TextView tvGoodValue;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.tv_integral)
    TextView tvIntegral;
    @Bind(R.id.tv_integral_value)
    TextView tvIntegralValue;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.tv_open_time)
    TextView tvOpenTime;
    @Bind(R.id.tv_open_time_value)
    TextView tvOpenTimeValue;
    @Bind(R.id.tv_comment)
    TextView tvComment;
    @Bind(R.id.tv_history_value)
    TextView tvHistoryValue;
    @Bind(R.id.tv_history)
    TextView tvHistory;
    @Bind(R.id.line3)
    View line3;
    @Bind(R.id.tv_avg_value)
    TextView tvAvgValue;
    @Bind(R.id.tv_avg)
    TextView tvAvg;
    @Bind(R.id.line4)
    View line4;
    @Bind(R.id.tv_look_value)
    TextView tvLookValue;
    @Bind(R.id.tv_look)
    TextView tvLook;
    @Bind(R.id.tv_comment_all)
    TextView tvCommentAll;
    @Bind(R.id.recycle_homes)
    RecyclerView recycleHomes;
    @Bind(R.id.tv_homes_all)
    TextView tvHomesAll;
    @Bind(R.id.recycle_houses)
    RecyclerView recycleHouses;
    @Bind(R.id.tv_houses_all)
    TextView tvHousesAll;
    @Bind(R.id.iv_idc)
    ImageView ivIdc;
    @Bind(R.id.tv_idc_name)
    TextView tvIdcName;
    @Bind(R.id.iv_idc1)
    ImageView ivIdc1;
    @Bind(R.id.tv_idc_name1)
    TextView tvIdcName1;
    @Bind(R.id.tv_his_idc_all)
    TextView tvHisIdcAll;
    @Bind(R.id.recycle_his_dymanic)
    RecyclerView recycleHisDymanic;
    @Bind(R.id.tv_his_dymanic)
    TextView tvHisDymanic;
    @Bind(R.id.tv_base_name)
    TextView tvBaseName;
    @Bind(R.id.recycle_comment)
    RecyclerView recycleComment;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.rl_his_idc)
    RelativeLayout rlHisIdc;
    @Bind(R.id.shop_level)
    RatingBar rbShopLv;
    @Bind(R.id.btn_call)
    Button btnCall;
    private UserCommentListAdapter commentAdapter;
    private ExportCompleteHomeAdapter housesAdapter;
    private ExportHisDynamicAdapter dynamicAdapter;
    private ExportShopHouseAdapter houseAdapter;
    private ExportShopBaseBean baseBean;
    private String brokeCode;
    List<ExportHisIdcBean> exportHisIdcBeanList = new ArrayList<>();
    List<ExportSaleHousesBean> exportSaleHousesBeanList = new ArrayList<>();
    private String phone;

    @Override
    protected View setContentLayoutView() {
//        SettingsUtil.statusColor(activity, R.color.black);
//        UiUtil.setNoTitle(this);
        return getLayoutInflater().inflate(R.layout.activity_export_shop, null);
    }

    @Override
    protected void initView() {
//        String customerCode = dao.getCustomerCode();
        phone = getIntent().getStringExtra("PHONE");
        brokeCode = getIntent().getStringExtra("BROKECODE");
        mPresenter.requestShopBase(brokeCode);//店铺基础信息
        mPresenter.requestUserComment(brokeCode);//评价列表
        mPresenter.requestCompleteHome(brokeCode);//成交房源
        mPresenter.requestSaleHouses(brokeCode, "0");//所售楼盘
        mPresenter.requestHisIdc(brokeCode);//TA的证书
        mPresenter.requestHisDymanic(brokeCode);//TA的动态

        recycleHomes.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                startActivity(new Intent(ExportShopActivity.this, CompleteHousesActivity.class).putExtra("BROKECODE", brokeCode));
            }
        });

        recycleComment.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                startActivity(new Intent(ExportShopActivity.this, ExportCommentListActivity.class).putExtra("BROKECODE", brokeCode));
            }
        });

    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void tryRequestData() {
        mPresenter.requestShopBase(brokeCode);//店铺基础信息
        mPresenter.requestUserComment(brokeCode);//评价列表
        mPresenter.requestCompleteHome(brokeCode);//成交房源
        mPresenter.requestSaleHouses(brokeCode, "0");//所售楼盘
        mPresenter.requestHisIdc(brokeCode);//TA的证书
        mPresenter.requestHisDymanic(brokeCode);//TA的动态
    }

    @Override
    public void showTip(String msg) {

    }

    /**
     * 专家店铺基础信息
     */
    @Override
    public void showShopBaseData(ExportShopBaseBean baseBean) {
        this.baseBean = baseBean;
        if(!TextUtils.isEmpty(baseBean.level)){
            rbShopLv.setRating(Float.parseFloat(baseBean.level));
        }
        CacheManager.initImageUserHeader(this, ivHead, baseBean.brokerImg);
        tvBaseName.setText(baseBean.name);
        tvGoodValue.setText(baseBean.praiseRate + "%");
        tvIntegralValue.setText(baseBean.integral + "分");
        tvOpenTimeValue.setText(CustomerUtils.dateTransSdf(baseBean.time));

        tvHistoryValue.setText(baseBean.historyAchievement);
        tvAvgValue.setText(baseBean.avgAchievement);
        tvLookValue.setText(baseBean.recentLook);
    }

    /**
     * 用户评价
     *
     * @param commentBeanList
     */
    @Override
    public void showUserCommentList(List<ExportUserCommentBean> commentBeanList) {
        List<ExportUserCommentBean> commentList = new ArrayList<>();
        if (commentBeanList != null && commentBeanList.size() != 0) {
            tvCommentAll.setText("查看全部");
            tvCommentAll.setClickable(true);
            commentList.add(commentBeanList.get(0));
            if(commentBeanList.size() > 1){
                commentList.add(commentBeanList.get(1));
            }
            commentAdapter = new UserCommentListAdapter(R.layout.export_comment_list, commentList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            recycleComment.setLayoutManager(layoutManager);
            recycleComment.setAdapter(commentAdapter);
        } else {
            tvCommentAll.setText("暂无数据");
            tvCommentAll.setClickable(false);
            tvCommentAll.setTextColor(getResources().getColor(R.color.black));
        }

    }

    /**
     * 获取成交房源
     *
     * @param saleHousesBeanList
     */
    @Override
    public void showCompleteHome(List<ExportCompeleteHomeBean> saleHousesBeanList) {
        List<ExportCompeleteHomeBean> houeseList = new ArrayList<>();
        if (saleHousesBeanList != null && saleHousesBeanList.size() != 0) {
            tvHomesAll.setText("查看全部");
            tvHomesAll.setClickable(true);
            houeseList.add(saleHousesBeanList.get(0));
            housesAdapter = new ExportCompleteHomeAdapter(R.layout.export_complete_homes, houeseList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            recycleHomes.setLayoutManager(layoutManager);
            recycleHomes.setAdapter(housesAdapter);
        } else {
            tvHomesAll.setText("暂无数据");
            tvHomesAll.setClickable(false);
            tvHomesAll.setTextColor(getResources().getColor(R.color.black));
        }
    }

    /**
     * TA的证书
     *
     * @param exportHisIdcBeanList
     */
    @Override
    public void showHisIdc(List<ExportHisIdcBean> exportHisIdcBeanList) {
        this.exportHisIdcBeanList = exportHisIdcBeanList;
        if (exportHisIdcBeanList != null) {
            switch (exportHisIdcBeanList.size()) {
                case 0:
                    tvHisIdcAll.setText("暂无数据");
                    tvHisIdcAll.setClickable(false);
                    tvHisIdcAll.setTextColor(getResources().getColor(R.color.black));
                    rlHisIdc.setVisibility(View.GONE);
                    break;
                case 1:
                    tvHisIdcAll.setText("查看全部");
                    tvHisIdcAll.setClickable(true);
                    rlHisIdc.setVisibility(View.VISIBLE);
                    ivIdc1.setVisibility(View.GONE);
                    tvIdcName1.setVisibility(View.GONE);
                    final ExportHisIdcBean exportHisIdcBean = exportHisIdcBeanList.get(0);
                    CacheManager.initImageClientList(this, ivIdc, exportHisIdcBean.url);
                    tvIdcName.setText(exportHisIdcBean.name);

                    ivIdc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DynamicUrlPagerBean urlBean = new DynamicUrlPagerBean();
                            urlBean.position = 0;
                            urlBean.urlList.add(exportHisIdcBean.url);
                            startActivity(new Intent(ExportShopActivity.this, DynamicPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
                        }
                    });
                    break;
                default:
                    tvHisIdcAll.setText("查看全部");
                    tvHisIdcAll.setClickable(true);
                    rlHisIdc.setVisibility(View.VISIBLE);
                    final ExportHisIdcBean exportHisIdcBean1 = exportHisIdcBeanList.get(0);
                    final ExportHisIdcBean exportHisIdcBean2 = exportHisIdcBeanList.get(1);
                    ivIdc.setVisibility(View.VISIBLE);
                    tvIdcName.setVisibility(View.VISIBLE);
                    ivIdc1.setVisibility(View.VISIBLE);
                    tvIdcName1.setVisibility(View.VISIBLE);
                    CacheManager.initImageClientList(this, ivIdc, exportHisIdcBean1.url);
                    tvIdcName.setText(exportHisIdcBean1.name);
                    CacheManager.initImageClientList(this, ivIdc1, exportHisIdcBean2.url);
                    tvIdcName1.setText(exportHisIdcBean2.name);


                    ivIdc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DynamicUrlPagerBean urlBean = new DynamicUrlPagerBean();
                            urlBean.position = 0;
                            urlBean.urlList.add(exportHisIdcBean1.url);
                            startActivity(new Intent(ExportShopActivity.this, DynamicPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
                        }
                    });

                    ivIdc1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DynamicUrlPagerBean urlBean = new DynamicUrlPagerBean();
                            urlBean.position = 0;
                            urlBean.urlList.add(exportHisIdcBean2.url);
                            startActivity(new Intent(ExportShopActivity.this, DynamicPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
                        }
                    });
                    break;
            }


        }

    }

    /**
     * 获取TA的动态
     *
     * @param exportHisDymanicBeanList
     */
    @Override
    public void showHisDynamic(List<ExportHisDymanicBean> exportHisDymanicBeanList) {
        List<ExportHisDymanicBean> hisDymanicBeanist = new ArrayList<>();
        if (exportHisDymanicBeanList != null && exportHisDymanicBeanList.size() != 0) {
            tvHisDymanic.setText("查看全部");
            tvHisDymanic.setClickable(true);
            hisDymanicBeanist.add(exportHisDymanicBeanList.get(0));
            dynamicAdapter = new ExportHisDynamicAdapter(R.layout.export_his_dymanic, hisDymanicBeanist);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            recycleHisDymanic.setLayoutManager(layoutManager);
            recycleHisDymanic.setAdapter(dynamicAdapter);

            dynamicAdapter.setCommentImgOnclickListenear(new ExportHisDynamicAdapter.CommentImgOnclickListenear() {
                @Override
                public void setCommentOnclick(int position, ExportHisDymanicBean exportHisDymanicBean) {
                    DynamicUrlPagerBean urlBean = new DynamicUrlPagerBean();
                    urlBean.position = position;
                    for (int i = 0; i < exportHisDymanicBean.contentUrl.size(); i++) {
                        urlBean.urlList.add(exportHisDymanicBean.contentUrl.get(i).url);
                    }

                    startActivity(new Intent(ExportShopActivity.this, DynamicPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
                }
            });
        } else {
            tvHisDymanic.setText("暂无数据");
            tvHisDymanic.setClickable(false);
            tvHisDymanic.setTextColor(getResources().getColor(R.color.black));
        }
    }

    /**
     * 获取所售楼盘
     *
     * @param exportSaleHousesBeanList
     */
    @Override
    public void showSaleHouses(List<ExportSaleHousesBean> exportSaleHousesBeanList) {
        if(exportSaleHousesBeanList == null && exportSaleHousesBeanList.size() == 0){
            tvHousesAll.setVisibility(View.GONE);
        }else {
            tvHousesAll.setVisibility(View.VISIBLE);
        }
        List<ExportSaleHousesBean> saleHousesBeanList = new ArrayList<>();
        if (exportSaleHousesBeanList != null && exportSaleHousesBeanList.size() != 0) {
            tvHousesAll.setText("查看全部");
            tvHousesAll.setClickable(true);
            saleHousesBeanList.add(exportSaleHousesBeanList.get(0));
            houseAdapter = new ExportShopHouseAdapter(R.layout.export_complete_homes, saleHousesBeanList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            recycleHouses.setLayoutManager(layoutManager);
            recycleHouses.setAdapter(houseAdapter);
        } else {
            tvHousesAll.setText("暂无数据");
            tvHousesAll.setClickable(false);
            tvHousesAll.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void showFail(String msg) {
        if("9001".equals(msg)){
            if(exportSaleHousesBeanList.size() == 0){
                recycleHouses.setVisibility(View.GONE);
                tvHousesAll.setText("暂无数据");
                tvHousesAll.setClickable(false);
                tvHousesAll.setTextColor(getResources().getColor(R.color.black));
            }

            if(exportHisIdcBeanList.size() == 0){
                rlHisIdc.setVisibility(View.GONE);
                tvHisIdcAll.setText("暂无数据");
                tvHisIdcAll.setClickable(false);
                tvHisIdcAll.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }


    @OnClick({R.id.tv_comment_all, R.id.tv_homes_all, R.id.tv_houses_all, R.id.tv_comment,
            R.id.tv_his_dymanic, R.id.tv_his_idc_all, R.id.iv_back,R.id.btn_call})
    public void onClick(View view) {
        switch (view.getId()) {
            //给他评价
            case R.id.tv_comment:
                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 0);
                    return;
                }
                ExportCommentActivity.startActivity(this,brokeCode,baseBean,10);
//                startActivity(new Intent(ExportShopActivity.this, ExportCommentActivity.class).putExtra("SHOP_BEAN", baseBean).putExtra("brokerCode", brokerCode));
                break;
            //全部评价列表
            case R.id.tv_comment_all:
                startActivity(new Intent(ExportShopActivity.this, ExportCommentListActivity.class).putExtra("BROKECODE", brokeCode));
                break;
            //全部成交房源
            case R.id.tv_homes_all:
                startActivity(new Intent(ExportShopActivity.this, CompleteHousesActivity.class).putExtra("BROKECODE", brokeCode));
                break;
            //全部所售楼盘
            case R.id.tv_houses_all:
                startActivity(new Intent(ExportShopActivity.this, SaleHousesActivity.class).putExtra("BROKECODE", brokeCode));
                break;
            //全部证书
            case R.id.tv_his_idc_all:
                startActivity(new Intent(ExportShopActivity.this, HisIdcActivity.class).putExtra("BROKECODE", brokeCode));
                break;
            //TA的动态
            case R.id.tv_his_dymanic:
                startActivity(new Intent(ExportShopActivity.this, HisDynamicActivity.class).putExtra("BROKECODE", brokeCode));
                break;
            case R.id.iv_back:
                finish();
                break;
            //打电话
            case R.id.btn_call:
                call(phone);
                break;
        }
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
                        ExportShopActivityPermissionsDispatcher.jumpCallPhoneWithCheck(ExportShopActivity.this, phone);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void jumpCallPhone(String phone) {
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
        ExportShopActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 10){
                mPresenter.requestUserComment(brokeCode);//评价列表
            }
        }
    }
}
