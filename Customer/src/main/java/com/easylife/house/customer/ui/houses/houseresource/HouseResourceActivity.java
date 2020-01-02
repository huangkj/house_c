package com.easylife.house.customer.ui.houses.houseresource;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ClubAdapter;
import com.easylife.house.customer.adapter.FavorableShowAdapter;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.HouseResource;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.LoanResult;
import com.easylife.house.customer.bean.PublicImage;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.housesdetail.bookinghouse.BookingHouseActivity;
import com.easylife.house.customer.ui.houses.housesdetail.pager.NetworkImageHolderView;
import com.easylife.house.customer.ui.payment.favorable.FavorableSelectActivity;
import com.easylife.house.customer.ui.pub.PhotoViewActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.util.LoanUtil;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.MoneyUtils;
import com.easylife.house.customer.util.PriceUtil;
import com.easylife.house.customer.util.StatusBarUtils;
import com.easylife.house.customer.view.FlowViewGroup;
import com.easylife.house.customer.view.anchor.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.blankj.utilcode.util.StringUtils.null2Length0;
import static com.easylife.house.customer.R.id.btnGetFavorable;
import static com.easylife.house.customer.R.id.tv_buy;
import static com.easylife.house.customer.config.ItemSelectManager.HOUSE_TYPE_FLAG_TEXTSIZE;

/**
 * 房源详情页
 */
@RuntimePermissions
public class HouseResourceActivity extends MVPBaseActivity<HouseResourceContract.View, HouseResourcePresenter> implements HouseResourceContract.View {

    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.tvHouseResourceName)
    TextView tvHouseResourceName;
    @Bind(R.id.tvHouseResourceStatus)
    TextView tvHouseResourceStatus;
    @Bind(R.id.tvHouseResourcePrice)
    TextView tvHouseResourcePrice;
    @Bind(R.id.tvHouseResourceDis)
    TextView tvHouseResourceDis;
    @Bind(R.id.tvHouseResourceDate)
    TextView tvHouseResourceDate;
    @Bind(R.id.tvHouseResourceSubcrib)
    TextView tvHouseResourceSubcrib;
    @Bind(R.id.tvHouseResourceLock)
    TextView tvHouseResourceLock;
    @Bind(R.id.tvHouseResourcePriceOriginal)
    TextView tvHouseResourcePriceOriginal;
    @Bind(R.id.tvHouseResourcePriceNow)
    TextView tvHouseResourcePriceNow;
    @Bind(R.id.tvHouseDesc)
    TextView tvHouseDesc;
    @Bind(R.id.tvStructure)
    TextView tvStructure;
    @Bind(R.id.tv_bussiness)
    TextView tvBussiness;
    //    @Bind(R.id.line)
//    View line;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.tv_total_money)
    TextView tvTotalMoney;
    @Bind(R.id.line2)
    View line2;
    @Bind(R.id.tv_money_month)
    TextView tvMoneyMonth;
    @Bind(R.id.tv_month)
    TextView tvMonth;
    @Bind(R.id.tv_first_rate_value)
    TextView tvFirstRateValue;
    @Bind(R.id.tv_first_rate)
    TextView tvFirstRate;
    @Bind(R.id.tv_loan_total)
    TextView tvLoanTotal;
    @Bind(R.id.tv_loan)
    TextView tvLoan;
    @Bind(R.id.tv_loan_year)
    TextView tvLoanYear;
    //    @Bind(R.id.tvHouseResourceType)
//    TextView tvHouseResourceType;
    @Bind(R.id.tvHouseToward)
    TextView tvHouseToward;
    @Bind(R.id.tvHouseBuildName)
    TextView tvHouseBuildName;
    @Bind(R.id.tvHouseUnitName)
    TextView tvHouseUnitName;
    @Bind(R.id.tvHouseFloor)
    TextView tvHouseFloor;
    @Bind(R.id.tvHouseAreaBuild)
    TextView tvHouseAreaBuild;
    @Bind(R.id.tvHouseAreaReal)
    TextView tvHouseAreaReal;
    //    @Bind(R.id.tvHouseType)
//    TextView tvHouseType;
    @Bind(R.id.tvHouseResourceLocate)
    TextView tvHouseResourceLocate;
    @Bind(R.id.rl_ask)
    LinearLayout rlAsk;
    @Bind(R.id.tv_look)
    TextView tvLook;
    @Bind(tv_buy)
    TextView btnBuy;
    @Bind(R.id.tvPriceMarket)
    TextView tvPriceMarket;
    @Bind(R.id.tvDiscount)
    TextView tvDiscount;
    @Bind(R.id.layDiscount)
    LinearLayout layDiscount;
    @Bind(R.id.layFavorable)
    LinearLayout layFavorable;
    @Bind(R.id.recycleFavorable)
    RecyclerView recycleFavorable;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.id_tablayout)
    TextView tvName;
    @Bind(R.id.ivRecommend)
    ImageView ivRecommend;
    @Bind(R.id.houses_type_item)
    RelativeLayout housesTypeItem;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    private List<View> listViews = null;

    @Bind(R.id.scrollView)
    ObservableScrollView scrollView;
    //所属楼盘

    @Bind(R.id.iv_house)
    ImageView ivHouseBelong;
    @Bind(R.id.tv_transParent)
    TextView tvTransParentBelong;
    @Bind(R.id.tv_price_belong)
    TextView tvPriceBelong;
    @Bind(R.id.tv_house_name)
    TextView tvHouseNameBelong;
    @Bind(R.id.tv_area)
    TextView tvAreaBelong;
    @Bind(R.id.tv_address)
    TextView tvAddressBelong;
    @Bind(R.id.tv_room)
    TextView tvRoomBelong;
    @Bind(R.id.floviewgroupBelong)
    FlowViewGroup floviewgroupBelong;
    @Bind(R.id.relBannerContent)
    RelativeLayout relBannerContent;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;


    public static void startActivity(Activity activity, String buildNo, String houseID, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HouseResourceActivity.class)
                        .putExtra("buildNo", buildNo)
                        .putExtra("houseID", houseID)
                , requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.house_activity_resource, null);
    }

    private String buildNo, houseID;
    HouseResource detail;
    HousesDetailBaseBean baseBean;
    private FavorableShowAdapter adapter;

    @Override
    protected void initView() {
        buildNo = getIntent().getStringExtra("buildNo");
        houseID = getIntent().getStringExtra("houseID");

        StatusBarUtils.setDarkMode(this);
        StatusBarUtils.setTranslucentForImageView2(activity, Color.argb(30, 0, 0, 0), rlTop);
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                int scrollY = scrollView.getScrollY();
                float ratio = Math.min(1, scrollY / (relBannerContent.getHeight() - rlTop.getHeight() * 1f));
                if (ratio > 0) {
                    StatusBarUtils.setTranslucentForImageView2(activity, Color.argb(((int) (ratio * 0xFF)), 255, 255, 255), rlTop);
                    StatusBarUtils.setLightMode(activity);
                    if (rlTop.getVisibility() == View.GONE)
                        rlTop.setVisibility(View.VISIBLE);
                } else {
                    StatusBarUtils.setTranslucentForImageView2(activity, Color.argb(30, 0, 0, 0), rlTop);
                    StatusBarUtils.setDarkMode(activity);
                    if (rlTop.getVisibility() == View.VISIBLE)
                        rlTop.setVisibility(View.GONE);
                }

                rlTop.getBackground().mutate().setAlpha((int) (ratio * 0xFF));
            }
        });
        adapter = new FavorableShowAdapter(R.layout.item_favorable, null);
        recycleFavorable.setLayoutManager(new LinearLayoutManager(this));
        recycleFavorable.setAdapter(adapter);


        if (!TextUtils.isEmpty(buildNo)) {
            tvName.setText(buildNo);
        }
        tvHouseResourcePriceOriginal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        showLoading();
        mPresenter.getNetData(houseID);
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showFavorableDev(DevFavorable devFavorable) {
        if (devFavorable == null)
            return;
        if (devFavorable.discount != null && ("1".equals(devFavorable.discount.discountAllType) || "1".equals(devFavorable.discount.discountDaiType))) {
            layFavorable.setVisibility(View.VISIBLE);
            // 全款贷款
            layDiscount.setVisibility(View.VISIBLE);
            String strHtml = "";
            if ("1".equals(devFavorable.discount.discountAllType)) {
                // 显示全款折扣
                strHtml += "全款<font   color=\"#FF6800\">" +
                        devFavorable.discount.discountAll +
                        "折</font>     ";
            }
            if ("1".equals(devFavorable.discount.discountDaiType)) {
                // 显示贷款折扣
                strHtml += "贷款<font   color=\"#FF6800\">" +
                        devFavorable.discount.discountDai +
                        "折</font>     ";
            }

            tvDiscount.setText(Html.fromHtml(strHtml));
        }
        if (devFavorable.favourable != null && devFavorable.favourable.size() != 0) {
            // 楼盘优惠列表
            layFavorable.setVisibility(View.VISIBLE);
            adapter.setNewData(devFavorable.favourable);
        }
    }

    @Override
    public void showFavorableVip(final List<FavorableVip> favorableVips) {
        if (favorableVips != null && favorableVips.size() > 0) {
            // 只显示最新一条认筹优惠
            viewPager.setVisibility(View.VISIBLE);
            listViews = new ArrayList<>();
            for (int i = 0; i < favorableVips.size(); i++) {
                View view = LayoutInflater.from(this).inflate(
                        R.layout.houseviepager_item, null);
                LinearLayout llPager = (LinearLayout) view.findViewById(R.id.ll_pager);
                TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
                TextView tvPriceCh = (TextView) view.findViewById(R.id.tvprice_ch);
                TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
                tvPrice.setText(PriceUtil.formatNum(favorableVips.get(i).num + "", false) + "元");
                tvPriceCh.setText(favorableVips.get(i).privilege);
                tvContent.setText("适用于：" + favorableVips.get(i).scope);
                listViews.add(view);

                final int finalI = i;
                llPager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                //   获取优惠
                        if (dao.isLogin()) {
                            try {
                                // 在楼盘基本数据中添加展示图，无数据时才添加
                                if (baseBean.distribution == null || baseBean.distribution.size() == 0) {
                                    List<HousesDetailBaseBean.DistributionBean> covers = new ArrayList<>();
                                    if (images != null && images.size() > 0)
                                        covers.add(new HousesDetailBaseBean.DistributionBean(images.get(0)));
                                    baseBean.distribution = covers;
                                }
                            } catch (Exception e) {
                                LogOut.d("distribution:", e.toString());
                            }
                            FavorableSelectActivity.startActivity(HouseResourceActivity.this, baseBean, favorableVips.get(finalI).id, 11);
                        } else {
                            LoginByVerifyCodeActivity.startActivity(HouseResourceActivity.this, 12);
                        }
                    }
                });
            }
            ClubAdapter clubAdapter = new ClubAdapter(listViews);
            viewPager.setAdapter(clubAdapter);
        }
    }

    @Override
    public void showHousesDetail(HousesDetailBaseBean baseBean) {
        this.baseBean = baseBean;
        setBelongHouseData();
    }

    @Override
    public void showTip(String msg) {

    }


    @OnClick({R.id.rl_ask, R.id.tv_look, R.id.tv_buy
            , R.id.iv_back, R.id.ivRecommend, R.id.iv_back_banner, R.id.ivRecommend_banner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_ask:
                if (dao.isLogin()) {
//                    startActivity(new Intent(this, ChatActivity.class).putExtra("userId", EaseConstant.IM_CLIENT_ID));
                } else {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                }
                break;
            case R.id.tv_look:
                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                    return;
                }
                startActivity(new Intent(this, BookingHouseActivity.class).putExtra("BASE_BEAN", baseBean));
                break;
            case btnGetFavorable:
                // 获取优惠
                if (baseBean == null)
                    return;
                FavorableSelectActivity.startActivity(activity, baseBean, 0);
                break;
            case tv_buy:
                // 认购下定
                CustomerUtils.showTip(activity, "功能开发中");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_back_banner:
                finish();
                break;
            case R.id.ivRecommend:
                if (dao.isLogin()) {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(dao.getCustomer().username, dao.getCustomer().phone));
                } else {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(null, null));
                }
                break;
            case R.id.ivRecommend_banner:
                if (dao.isLogin()) {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(dao.getCustomer().username, dao.getCustomer().phone));
                } else {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(null, null));
                }
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
                        HouseResourceActivityPermissionsDispatcher.jumpCallPhoneWithCheck(HouseResourceActivity.this, phone);
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
        HouseResourceActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void initDetail(HouseResource detail) {
        if (detail == null) {
            cancelLoading();
            return;
        }
        this.detail = detail;

        baseBean = new HousesDetailBaseBean();
        baseBean.devId = detail.devId;
        baseBean.devName = detail.devName;
        baseBean.projectName = detail.projectName;
        baseBean.addressDetail = detail.addressDetail;
        baseBean.averPrice = detail.mUnitPrice;
        baseBean.devSquareMetre = detail.fArea;
        baseBean.devBedroom = detail.structure;
        baseBean.propertyType = detail.propertyType;
        baseBean.addressTown = detail.addressTown;
        baseBean.addressDistrict = detail.addressTown;
        mPresenter.requestHousesDetail(detail.devId);

        /*
        if (baseBean.effectId != null && baseBean.effectId.size() != 0) {
            CacheManager.initSearchHouseList(this, (ImageView) ivHouseBelong, baseBean.effectId.get(0).thumbnailImage);
        } else {
            CacheManager.initSearchHouseList(this, (ImageView) ivHouseBelong, null);
        }
        if (TextUtils.isEmpty(baseBean.isTransparent) || "0".equals(baseBean.isTransparent)) {
            tvTransParentBelong.setVisibility(View.INVISIBLE);
        } else {
            tvTransParentBelong.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(baseBean.averPrice)) {

            double avgPrice = Double.parseDouble(baseBean.averPrice);

            if (avgPrice >= 1000000) {
                tvPriceBelong.setText(avgPrice / 10000 + "万元/㎡");
            } else {
                tvPriceBelong.setText(baseBean.averPrice + "元/㎡");
            }
        }


        String propertyType = "";
        if (!TextUtils.isEmpty(baseBean.propertyType)) {
            String[] split = baseBean.propertyType.split(",");
            propertyType = split[0];
        }


        tvHouseNameBelong.setText(null2Length0(baseBean.devName));
        if (!TextUtils.isEmpty(baseBean.addressDistrict) && !TextUtils.isEmpty(propertyType)) {
            tvAddressBelong.setText(null2Length0(baseBean.addressDistrict) + " 丨 " + propertyType);
        } else {
            if (!TextUtils.isEmpty(baseBean.addressDistrict)) {
                tvAddressBelong.setText(null2Length0(baseBean.addressDistrict));
            } else {
                tvAddressBelong.setText(null2Length0(propertyType));
            }
        }


        if (!TextUtils.isEmpty(baseBean.devSquareMetre) && !TextUtils.isEmpty(((TextView) tvAddressBelong).getText())) {
            tvAreaBelong.setText((" 丨 建面 " + baseBean.devSquareMetre + "㎡"));
        } else if (TextUtils.isEmpty(baseBean.devSquareMetre)) {
            tvAreaBelong.setText("");
        } else {
            tvAreaBelong.setText(("建面 " + baseBean.devSquareMetre + "㎡"));
        }


        tvRoomBelong.setText(TextUtils.isEmpty(baseBean.devBedroom) ? " " : baseBean.devBedroom);
        floviewgroupBelong.removeAllViews();
        if (!TextUtils.isEmpty(baseBean.feature)) {
            String[] features = baseBean.feature.split(",");

            int length = features.length;
            if (length > 3) {
                length = 3;
            }

            for (int i = 0; i < length; i++) {
                TextView tvFeature = (TextView) LayoutInflater.from(this).inflate(R.layout.houses_detail_flow_item, floviewgroupBelong, false);
                tvFeature.setTextSize(HOUSE_TYPE_FLAG_TEXTSIZE);
                tvFeature.setText(features[i]);
                floviewgroupBelong.addView(tvFeature);
            }
        }

        * */


        if (detail.houseImg != null && detail.houseImg.size() != 0) {
            setBanner();
        }

        buildNo = detail.buildName + "-" + detail.unitName + "-" + detail.cellName;

        tvName.setText(buildNo);
        tvHouseResourceName.setText(detail.devName);
        tvHouseResourcePrice.setText(detail.mUnitPrice + "元/㎡");
        double totalPrice = 0;
        try {
            totalPrice = Double.parseDouble(detail.mTotalPrice);
        } catch (Exception e) {
            LogOut.d("totalPrice", e.toString());
        }
        if (totalPrice == 0) {
            tvPriceMarket.setText("暂无数据");
        } else {
            tvPriceMarket.setText(DoubleFomat.format2(totalPrice / 10000) + "万元/套");
        }

        //商贷参考UI及计算
        if (totalPrice != 0) {
            double firstPrice = totalPrice * 0.3f / 10000;
            tvTotal.setText(Math.round(firstPrice) + "万");
            LoanResult loanResult = LoanUtil.getLoanResult(true, DoubleFomat.format2(totalPrice - firstPrice) + "", "30", "0.049");
            if (loanResult != null) {
                tvMoneyMonth.setText(Math.round(loanResult.paymentMonth / 10000) + "");
                tvLoanTotal.setText(Math.round((totalPrice - firstPrice) / 10000) + "万");
            }
        }

        tvStructure.setText(detail.structure);
//        tvHouseResourceType.setText(detail.structure);
        tvHouseToward.setText(detail.toward);
        tvHouseBuildName.setText(detail.buildName);
        tvHouseUnitName.setText(detail.unitName + "(共" + detail.unitNum + "单元）");
        tvHouseFloor.setText("第" + detail.floor + "层(共" + detail.floorNum + "层)");
        tvHouseAreaBuild.setText(detail.fArea + "㎡");
        tvHouseResourceDis.setText(detail.fArea + "㎡");
        tvHouseAreaReal.setText(detail.uArea + "㎡");
//        tvHouseType.setText(detail.buildType);
        tvHouseResourceLocate.setText(detail.addressDistrict + detail.addressTown + detail.addressDetail);
        tvHouseDesc.setText(detail.introduce);

//        mPresenter.getDevFavorable(detail.devId);
//        mPresenter.getVipFavorable(detail.devId);

        cancelLoading();
    }


    public void setBelongHouseData() {
        /*
    @Bind(R.id.iv_house)
    ImageView ivHouseBelong;
    @Bind(R.id.tv_transParent)
    TextView tvTransParentBelong;
    @Bind(R.id.tv_price_belong)
    TextView tvPriceBelong;
    @Bind(R.id.tv_area)
    TextView tvAreaBelong;
    @Bind(R.id.tv_address)
    TextView tvAddressBelong;
    @Bind(R.id.tv_room)
    TextView tvRoomBelong;
    @Bind(R.id.floviewgroupBelong)
    FlowViewGroup floviewgroupBelong;

        * */


        if (baseBean.effectId != null && baseBean.effectId.size() != 0) {
            CacheManager.initSearchHouseList(this, (ImageView) ivHouseBelong, baseBean.effectId.get(0).thumbnailImage);
        } else {
            CacheManager.initSearchHouseList(this, (ImageView) ivHouseBelong, null);
        }
        if (TextUtils.isEmpty(baseBean.isTransparent) || "0".equals(baseBean.isTransparent)) {
            tvTransParentBelong.setVisibility(View.INVISIBLE);
        } else {
            tvTransParentBelong.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(baseBean.averPrice) && !"0".equals(baseBean.averPrice)) {
            double avgPrice = Double.parseDouble(baseBean.averPrice);
            if (avgPrice >= 1000000) {
                avgPrice = MoneyUtils.formatDouble(avgPrice / 10000);
                tvPriceBelong.setText(avgPrice + "万元/㎡");
            } else {
                tvPriceBelong.setText(baseBean.averPrice + "元/㎡");
            }
        } else {
            tvPriceBelong.setText("价格待定");
        }


        String propertyType = "";
        if (!TextUtils.isEmpty(baseBean.propertyType)) {
            String[] split = baseBean.propertyType.split(",");
            propertyType = split[0];
        }


        tvHouseNameBelong.setText(null2Length0(baseBean.devName));
        if (!TextUtils.isEmpty(baseBean.addressDistrict) && !TextUtils.isEmpty(propertyType)) {
            tvAddressBelong.setText(null2Length0(baseBean.addressDistrict) + " 丨 " + propertyType);
        } else {
            if (!TextUtils.isEmpty(baseBean.addressDistrict)) {
                tvAddressBelong.setText(null2Length0(baseBean.addressDistrict));
            } else {
                tvAddressBelong.setText(null2Length0(propertyType));
            }
        }


        if (!TextUtils.isEmpty(baseBean.devSquareMetre) && !TextUtils.isEmpty(((TextView) tvAddressBelong).getText())) {
            tvAreaBelong.setText((" 丨 建面 " + baseBean.devSquareMetre + "㎡"));
        } else if (TextUtils.isEmpty(baseBean.devSquareMetre)) {
            tvAreaBelong.setText("");
        } else {
            tvAreaBelong.setText(("建面 " + baseBean.devSquareMetre + "㎡"));
        }


        tvRoomBelong.setText(TextUtils.isEmpty(baseBean.devBedroom) ? " " : baseBean.devBedroom);
        floviewgroupBelong.removeAllViews();
        if (!TextUtils.isEmpty(baseBean.feature)) {
            String[] features = baseBean.feature.split(",");

            int length = features.length;
            if (length > 3) {
                length = 3;
            }

            for (int i = 0; i < length; i++) {
                TextView tvFeature = (TextView) LayoutInflater.from(this).inflate(R.layout.houses_detail_flow_item, floviewgroupBelong, false);
                tvFeature.setTextSize(HOUSE_TYPE_FLAG_TEXTSIZE);
                tvFeature.setText(features[i]);
                floviewgroupBelong.addView(tvFeature);
            }
        }

    }


    private List<String> images;

    private void setBanner() {
        images = new ArrayList<>();
        for (HousesDetailBaseBean.DistributionBean img : detail.houseImg) {
            images.add(img.thumbnailImage);
        }
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, images).setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //跳转大图浏览页面
                if (images.size() == 0) {
                    return;
                }
                // TODO
                List<PublicImage> imgs = new ArrayList<>();
                for (String image : images) {
                    PublicImage img = new PublicImage();
                    img.cover = image;
                    imgs.add(img);
                }
                PhotoViewActivity.startActivity(activity, position, imgs);
            }
        });
        convenientBanner.setCanLoop(false);
    }
}
