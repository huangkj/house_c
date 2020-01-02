package com.easylife.house.customer.ui.homesearch.shoprent;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ShopRentAdapter;
import com.easylife.house.customer.bean.AreaDoubleBean;
import com.easylife.house.customer.bean.HomeSearchRequestBean;
import com.easylife.house.customer.bean.ShopRentRequestBean;
import com.easylife.house.customer.bean.ShopRentResponseBean;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.pub.ShopRentDetailWebActivity;
import com.easylife.house.customer.view.popwindow.AreaDoublePopWindow;
import com.easylife.house.customer.view.popwindow.HouseTypeFilterPopWindow;
import com.easylife.house.customer.view.popwindow.ListPopWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.config.Constants.SHOP_RENT_URL;

public class ShopRentActivity extends MVPBaseActivity<ShopRentContract.View, ShopRentPresenter> implements ShopRentContract.View, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.imgBack)
    ImageView imgBack;
    @Bind(R.id.tvArea)
    TextView tvArea;
    @Bind(R.id.ivArea)
    ImageView ivArea;
    @Bind(R.id.llArea)
    LinearLayout llArea;
    @Bind(R.id.tvHousePrice)
    TextView tvHousePrice;
    @Bind(R.id.ivHousePrice)
    ImageView ivHousePrice;
    @Bind(R.id.llHousePrice)
    LinearLayout llHousePrice;
    @Bind(R.id.tvAcreage)
    TextView tvAcreage;
    @Bind(R.id.ivAcreage)
    ImageView ivAcreage;
    @Bind(R.id.llAcreage)
    LinearLayout llAcreage;
    @Bind(R.id.llSortContent)
    LinearLayout llSortContent;
    @Bind(R.id.tvMore)
    TextView tvMore;
    @Bind(R.id.ivMore)
    ImageView ivMore;
    @Bind(R.id.llMore)
    LinearLayout llMore;
    @Bind(R.id.rcvHouseList)
    RecyclerView rcvHouseList;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    private AreaDoublePopWindow areaPopWindow;
    private ListPopWindow pricePopWindow;
    private String[] priceTexts = new String[]{"不限", "5000元以下", "5000-8000元", "8000-10000元", "10000-15000元", "15000-20000元", "20000-50000元", "50000元以上"};
    private String[] houseAreaTexts = new String[]{"不限", "20m²以下", "20-50m²", "50-100m²", "100-200m²", "200-500m²", "500m²以上"};
    private String[] houseType = new String[]{"住宅底商", "临街门面", "写字楼配套商底", "购物中心/百货", "独栋商业", "商业街商铺", "其他"};
    private String[] houseType2 = new String[]{"毛坯", "简装修", "精装修"};
    private ListPopWindow houseAreaPopWindow;
    private HouseTypeFilterPopWindow houseTypeFilterPopWindow;
    private ShopRentAdapter shopRentAdapter;
    private List<AreaDoubleBean> areas;
    private String cityId;
    private ShopRentRequestBean shopRentRequestBean;

    private HashMap<String, HomeSearchRequestBean.AvgPriceBean> avgPriceBeanMap;
    private HashMap<String, HomeSearchRequestBean.HouseSizeBean> houseSizeBeanMap;
    private HashMap<String, String> houseTypeMap;
    private HashMap<String, String> houseTypeMap2;


    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_shop_rent, null);
    }

    public static void startActivity(Activity activity, String cityId, int requestCode) {
        activity.startActivityForResult(new Intent(activity, ShopRentActivity.class)
                        .putExtra("cityId", cityId)
                , requestCode);
    }


    @Override
    protected void initView() {
        cityId = getIntent().getStringExtra("cityId");


        shopRentRequestBean = new ShopRentRequestBean();
        shopRentRequestBean.setCityId(cityId);

        ArrayList<String> type = new ArrayList<>();
        ArrayList<String> decorate = new ArrayList<>();

        shopRentRequestBean.setType(type);
        shopRentRequestBean.setDecorate(decorate);


        shopRentAdapter = new ShopRentAdapter(R.layout.shop_rent_item, null);
        shopRentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopRentResponseBean item = (ShopRentResponseBean) adapter.getItem(position);
                ShopRentDetailWebActivity.startActivity(ShopRentActivity.this, "详情", SHOP_RENT_URL + "?id=" + item.getId(), item.getDevName());
            }
        });
        rcvHouseList.setLayoutManager(new LinearLayoutManager(this));
        rcvHouseList.setAdapter(shopRentAdapter);
        swipeLayout.setOnRefreshListener(this);
        initPopDataMap();
        showLoading();
        mPresenter.selectTradingByCityId(cityId);
        mPresenter.requestShopRentList(shopRentRequestBean);


    }


    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void tryRequestData() {

    }


    @Override
    public void showTip(String msg) {

    }

    @OnClick({R.id.llArea, R.id.llHousePrice, R.id.llAcreage, R.id.llMore})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.llArea:
                if (areas == null) {
                    return;
                }
                if (areaPopWindow == null) {
                    areaPopWindow = new AreaDoublePopWindow(this, areas);
                    areaPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ObjectAnimator.ofFloat(ivArea, "rotation", 180, 0).setDuration(500).start();
                            ivArea.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ShopRentActivity.this, R.color._c4c4c4)));
                        }
                    });
                }
                areaPopWindow.setOnItemClickListener(new AreaDoublePopWindow.OnItemClickListener() {

                    @Override
                    public void onItemClick(String areaId, String tradingCode, String areaName, String tradingName) {
                        shopRentRequestBean.setAreaId(areaId);
                        shopRentRequestBean.setTradingCode(tradingCode);
                        if (!areaName.equals("不限")) {
                            if (!TextUtils.isEmpty(tradingName)) {
                                tvArea.setText(tradingName);
                            } else {
                                tvArea.setText(areaName);
                            }
                            tvArea.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._ff6800));
                        } else {
                            tvArea.setText("全部");
                            tvArea.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._5d5d5d));
                        }
                        onRefresh();

                    }
                });

                showPopWindow2View(areaPopWindow, llSortContent);
                ObjectAnimator.ofFloat(ivArea, "rotation", 0, 180).setDuration(500).start();
                ivArea.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color._ff6800)));
                break;
            case R.id.llHousePrice:
                if (pricePopWindow == null) {
                    pricePopWindow = new ListPopWindow(this, priceTexts, 8);
                    pricePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ObjectAnimator.ofFloat(ivHousePrice, "rotation", 180, 0).setDuration(500).start();
                            ivHousePrice.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ShopRentActivity.this, R.color._c4c4c4)));
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    KeyboardUtils.hideSoftInput(ShopRentActivity.this);
                                }
                            }, 200);
                        }
                    });
                    pricePopWindow.setOnConfirmClickListener(new ListPopWindow.OnConfirmClickListener() {
                        @Override
                        public void onConfirmClick(String text) {
                            HomeSearchRequestBean.AvgPriceBean avgPriceBean = avgPriceBeanMap.get(text);
                            shopRentRequestBean.setRentMix(avgPriceBean.getMinAvgPrice());
                            shopRentRequestBean.setRentMax(avgPriceBean.getMaxAvgPrice());

                            if (!text.equals("不限")) {
                                tvHousePrice.setText(text);
                                tvHousePrice.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._ff6800));
                            } else {
                                tvHousePrice.setText("租金");
                                tvHousePrice.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._5d5d5d));
                            }

                            onRefresh();
                        }

                        @Override
                        public void onConfirmClick(String min, String max) {
                            shopRentRequestBean.setRentMix(min);
                            shopRentRequestBean.setRentMax(max);

                            if (TextUtils.isEmpty(min)) {
                                min = "0";
                            }


                            tvHousePrice.setText(min + "-" + max + "元");
                            tvHousePrice.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._ff6800));
                            onRefresh();
                        }
                    });

                }
                showPopWindow2View(pricePopWindow, llSortContent);
                ObjectAnimator.ofFloat(ivHousePrice, "rotation", 0, 180).setDuration(500).start();
                ivHousePrice.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color._ff6800)));

                break;
            case R.id.llAcreage:
                if (houseAreaPopWindow == null) {
                    houseAreaPopWindow = new ListPopWindow(this, houseAreaTexts, 5);
                    houseAreaPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ObjectAnimator.ofFloat(ivAcreage, "rotation", 180, 0).setDuration(500).start();
                            ivAcreage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ShopRentActivity.this, R.color._c4c4c4)));
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    KeyboardUtils.hideSoftInput(ShopRentActivity.this);
                                }
                            }, 200);
                        }
                    });

                    houseAreaPopWindow.setOnConfirmClickListener(new ListPopWindow.OnConfirmClickListener() {
                        @Override
                        public void onConfirmClick(String text) {
                            HomeSearchRequestBean.HouseSizeBean houseSizeBean = houseSizeBeanMap.get(text);
                            shopRentRequestBean.setAreaMix(houseSizeBean.getMinHouseSize());
                            shopRentRequestBean.setAreaMax(houseSizeBean.getMaxHouseSize());

                            if (!text.equals("不限")) {
                                tvAcreage.setText(text);
                                tvAcreage.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._ff6800));
                            } else {
                                tvAcreage.setText("面积");
                                tvAcreage.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._5d5d5d));
                            }
                            onRefresh();
                        }

                        @Override
                        public void onConfirmClick(String min, String max) {
                            shopRentRequestBean.setAreaMix(min);
                            shopRentRequestBean.setAreaMax(max);


                            if (TextUtils.isEmpty(min)) {
                                min = "0";
                            }

                            tvAcreage.setText(min + "-" + max + "m²");
                            tvAcreage.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._ff6800));

                            onRefresh();
                        }
                    });
                }
                showPopWindow2View(houseAreaPopWindow, llSortContent);
                ObjectAnimator.ofFloat(ivAcreage, "rotation", 0, 180).setDuration(500).start();
                ivAcreage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color._ff6800)));
                break;
            case R.id.llMore:
                if (houseTypeFilterPopWindow == null) {
                    houseTypeFilterPopWindow = new HouseTypeFilterPopWindow(this, houseType, houseType2);
                    houseTypeFilterPopWindow.setTypeTitle("类别", "装修");
                    houseTypeFilterPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ObjectAnimator.ofFloat(ivMore, "rotation", 180, 0).setDuration(500).start();
                            ivMore.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ShopRentActivity.this, R.color._c4c4c4)));
                        }
                    });

                    houseTypeFilterPopWindow.setOnConfirmClickListener(new HouseTypeFilterPopWindow.OnConfirmClickListener() {
                        @Override
                        public void onConfirmClick(Object[] pos1, Object[] pos2) {
                            List<String> type = shopRentRequestBean.getType();
                            List<String> decorate = shopRentRequestBean.getDecorate();
                            type.clear();
                            decorate.clear();

                            for (int i = 0; i < pos1.length; i++) {
                                Integer integer = (Integer) pos1[i];
                                type.add(houseTypeMap.get(houseType[integer]));
                            }

                            for (int i = 0; i < pos2.length; i++) {
                                Integer integer = (Integer) pos2[i];
                                decorate.add(houseTypeMap2.get(houseType2[integer]));
                            }

                            if (pos1.length > 0 || pos2.length > 0) {
                                tvMore.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._ff6800));
                            } else {
                                tvMore.setTextColor(ContextCompat.getColor(ShopRentActivity.this, R.color._5d5d5d));
                            }
                            onRefresh();

                        }
                    });
                }
                showPopWindow2View(houseTypeFilterPopWindow, llSortContent);
                ObjectAnimator.ofFloat(ivMore, "rotation", 0, 180).setDuration(500).start();
                ivMore.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color._ff6800)));
                break;
        }
    }

    public void showPopWindow2View(PopupWindow pop, View anchor) {

        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            pop.setHeight(height);
            pop.showAsDropDown(anchor);
        } else {
            pop.showAsDropDown(anchor);
        }
    }

    /*
    *   private String[] priceTexts = new String[]{"不限", "5000元以下", "5000-8000元", "10000-15000元", "15000-20000元", "20000-50000元", "50000元以上"};
        private String[] houseAreaTexts = new String[]{"不限", "20m²以下", "20-50m²", "50-100m²", "100-200m²", "110-150m²", "200-500m²", "500m²以上"};
        private String[] houseType = new String[]{"住宅底商", "临街门面", "写字楼配套商底", "购物中心/百货", "独栋商业", "商业街商铺", "其他"};
        private String[] houseType2 = new String[]{"毛坯", "简装修", "精装修"};
    *
    * */
    private void initPopDataMap() {
        avgPriceBeanMap = new HashMap<>();
        avgPriceBeanMap.put("不限", new HomeSearchRequestBean.AvgPriceBean());
        avgPriceBeanMap.put("5000元以下", new HomeSearchRequestBean.AvgPriceBean("", "5000"));
        avgPriceBeanMap.put("5000-8000元", new HomeSearchRequestBean.AvgPriceBean("5000", "8000"));
        avgPriceBeanMap.put("8000-10000元", new HomeSearchRequestBean.AvgPriceBean("8000", "10000"));
        avgPriceBeanMap.put("10000-15000元", new HomeSearchRequestBean.AvgPriceBean("10000", "15000"));
        avgPriceBeanMap.put("15000-20000元", new HomeSearchRequestBean.AvgPriceBean("15000", "20000"));
        avgPriceBeanMap.put("20000-50000元", new HomeSearchRequestBean.AvgPriceBean("20000", "50000"));
        avgPriceBeanMap.put("50000元以上", new HomeSearchRequestBean.AvgPriceBean("50000", ""));


        houseSizeBeanMap = new HashMap<>();
        houseSizeBeanMap.put("不限", new HomeSearchRequestBean.HouseSizeBean());
        houseSizeBeanMap.put("20m²以下", new HomeSearchRequestBean.HouseSizeBean("", "20"));
        houseSizeBeanMap.put("20-50m²", new HomeSearchRequestBean.HouseSizeBean("20", "50"));
        houseSizeBeanMap.put("50-100m²", new HomeSearchRequestBean.HouseSizeBean("50", "100"));
        houseSizeBeanMap.put("100-200m²", new HomeSearchRequestBean.HouseSizeBean("100", "200"));
        houseSizeBeanMap.put("200-500m²", new HomeSearchRequestBean.HouseSizeBean("200", "500"));
        houseSizeBeanMap.put("500m²以上", new HomeSearchRequestBean.HouseSizeBean("500", ""));


        houseTypeMap = new HashMap<>();
        houseTypeMap.put("住宅底商", "1");
        houseTypeMap.put("临街门面", "4");
        houseTypeMap.put("写字楼配套商底", "2");
        houseTypeMap.put("购物中心/百货", "5");
        houseTypeMap.put("独栋商业", "6");
        houseTypeMap.put("商业街商铺", "3");
        houseTypeMap.put("其他", "7");


        houseTypeMap2 = new HashMap<>();
        houseTypeMap2.put("毛坯", "3");
        houseTypeMap2.put("简装修", "1");
        houseTypeMap2.put("精装修", "2");


    }


    @Override
    public void tradingListSuccess(List<AreaDoubleBean> areas) {
        this.areas = areas;
    }

    @Override
    public void shopRentListSuccess(List<ShopRentResponseBean> shopList) {
        cancelLoading();
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
        shopRentAdapter.setNewData(shopList);
    }

    @Override
    public void showFail(String code) {
        cancelLoading();
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
        ToastUtils.showShort(code);
    }

    @Override
    public void onRefresh() {
        mPresenter.requestShopRentList(shopRentRequestBean);
    }
}
