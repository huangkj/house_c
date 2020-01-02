package com.easylife.house.customer.ui.homesearch.recenthouse;

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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.SearchHouseV3Adapter;
import com.easylife.house.customer.bean.CityAreaBean;
import com.easylife.house.customer.bean.HomeSearchRequestBean;
import com.easylife.house.customer.bean.HomeSearchResponseBean;
import com.easylife.house.customer.bean.LocateCache;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.searchhouse.SearchHouseActivity;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.ui.houses.map.mapfindhouse.MapFindHouseActivity;
import com.easylife.house.customer.view.popwindow.AreaPopWindow;
import com.easylife.house.customer.view.popwindow.HouseTypeFilterPopWindow;
import com.easylife.house.customer.view.popwindow.ListPopWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.SEARCH_HOUSE_FINISH;

public class RecentHouseActivity extends MVPBaseActivity<RecentHouseContract.View, RecentHousePresenter> implements RecentHouseContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.imgBackHouse)
    ImageView imgBackHouse;
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
    @Bind(R.id.tvSearch)
    TextView tvSearch;
    @Bind(R.id.ivAcreage)
    ImageView ivAcreage;
    @Bind(R.id.ivMapFind)
    ImageView ivMapFind;
    @Bind(R.id.llAcreage)
    LinearLayout llAcreage;
    @Bind(R.id.llSortContent)
    LinearLayout llSortContent;
    @Bind(R.id.relEmpty)
    RelativeLayout relEmpty;
    @Bind(R.id.tvMore)
    TextView tvMore;
    @Bind(R.id.tvNear)
    TextView tvNear;
    @Bind(R.id.ivMore)
    ImageView ivMore;
    @Bind(R.id.llMore)
    LinearLayout llMore;
    @Bind(R.id.llSerachView)
    LinearLayout llSerachView;
    @Bind(R.id.rcvHouseList)
    RecyclerView rcvHouseList;
    @Bind(R.id.rcvAroundHouse)
    RecyclerView rcvAroundHouse;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    private AreaPopWindow areaPopWindow;
    private ListPopWindow pricePopWindow;
    private String[] priceTexts = new String[]{"不限", "10000元以下", "10000-20000元", "20000-30000元", "30000-50000元", "50000-100000元", "100000元以上"};
    private String[] houseAreaTexts = new String[]{"不限", "50m²以下", "50-70m²", "70-90m²", "90-110m²", "110-150m²", "150-200m²", "200-300m²", "300m²以上"};
    private String[] houseType = new String[]{"住宅", "公寓", "别墅", "写字楼", "商业"};
    private String[] houseType2 = new String[]{"一居室", "二居室", "三居室", "四居室", "五居室及以上"};

    private ListPopWindow houseAreaPopWindow;
    private HouseTypeFilterPopWindow houseTypeFilterPopWindow;
    private String city = "北京市";
    private String cityId = "110100";
    private SearchHouseV3Adapter mAdapter;
    private List<HomeSearchResponseBean> baseBeanList;
    private List<HomeSearchResponseBean> allBaseBeanList = new ArrayList<>();
    private HomeSearchRequestBean homeSearchRequestBean;
    /**
     * 城市下的城区
     */
    private List<CityAreaBean> areas;
    private HashMap<String, HomeSearchRequestBean.AvgPriceBean> avgPriceBeanMap;
    private HashMap<String, HomeSearchRequestBean.HouseSizeBean> houseSizeBeanMap;
    private HashMap<String, Integer> houseTypeMap;
    private int type;
    /**
     * 现在选房 值传1 其他传空
     */
    private String isTransparent;
    private String devName;
    private SearchHouseV3Adapter nearAdapter;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_recent_house, null);
    }

    public static void startActivity(Activity activity, int type, String devName, String cityId, String city, String isTransparent, int requestCode) {
        activity.startActivityForResult(new Intent(activity, RecentHouseActivity.class)
                        .putExtra("type", type)
                        .putExtra("devName", devName)
                        .putExtra("cityId", cityId)
                        .putExtra("city", city)
                        .putExtra("isTransparent", isTransparent)
                , requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent event) {
        switch (event.MsgType) {
            case SEARCH_HOUSE_FINISH:
                finish();
                break;
        }
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        type = getIntent().getIntExtra("type", 0);
        devName = getIntent().getStringExtra("devName");
        cityId = getIntent().getStringExtra("cityId");
        city = getIntent().getStringExtra("city");
        isTransparent = getIntent().getStringExtra("isTransparent");

        mAdapter = new SearchHouseV3Adapter(R.layout.search_item, baseBeanList);
        mAdapter.setType(type);
        rcvHouseList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation();
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, rcvHouseList);
        rcvHouseList.setAdapter(mAdapter);


        nearAdapter = new SearchHouseV3Adapter(R.layout.search_item, null);
        rcvAroundHouse.setLayoutManager(new LinearLayoutManager(this));
        rcvAroundHouse.setAdapter(nearAdapter);

        rcvHouseList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (allBaseBeanList != null && allBaseBeanList.size() != 0) {
                    HomeSearchResponseBean baseBean = allBaseBeanList.get(position);
//                    searchSingleton.lookHouse.add(AllHouseActivity.this);
//                    if (baseBean.coOp == 3) {
//                        startActivity(new Intent(AllHouseActivity.this, HousesAndTypeSimpleActivity.class).putExtra("DEV_ID", baseBean.devId));
//                    } else {
                    HouseDetailActivity.startActivity(RecentHouseActivity.this, baseBean.getDevId(), baseBean.coOp, 0);
//                }
                }
            }
        });

        rcvAroundHouse.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeSearchResponseBean item = nearAdapter.getItem(position);
                HouseDetailActivity.startActivity(RecentHouseActivity.this, item.getDevId(), item.coOp, 0);
            }
        });

        View emptyView = getLayoutInflater().inflate(R.layout.empty_all_house_view, relEmpty, false);
        mAdapter.setEmptyView(emptyView);

        swipeLayout.setOnRefreshListener(this);


        // all_house_item.xml
        homeSearchRequestBean = new HomeSearchRequestBean();


        homeSearchRequestBean.setType(type + "");
        homeSearchRequestBean.setDevName(devName);

//        homeSearchRequestBean.setAreaId("110106");
        homeSearchRequestBean.setAreaId("");
        homeSearchRequestBean.setCityId(cityId);

        homeSearchRequestBean.setIsTransparent(isTransparent);

        ArrayList<HomeSearchRequestBean.AvgPriceBean> avgPriceBeans = new ArrayList<>();
        ArrayList<HomeSearchRequestBean.HouseSizeBean> houseSizeBeans = new ArrayList<>();

        homeSearchRequestBean.setAvgPrice(avgPriceBeans);
        homeSearchRequestBean.setHouseSize(houseSizeBeans);

        ArrayList<String> propertyType = new ArrayList<>();
        homeSearchRequestBean.setPropertyType(propertyType);


        ArrayList<String> devBedroomInfo = new ArrayList<>();
        homeSearchRequestBean.setDevBedroomInfo(devBedroomInfo);

        showLoading();
        mPresenter.requestHomeSearch(0, 1, homeSearchRequestBean);
        mPresenter.requestArea(0, "2", cityId);


        if (!TextUtils.isEmpty(devName)) {
            tvSearch.setText(devName);
            tvSearch.setTextColor(ContextCompat.getColor(this, R.color._5d5d5d));
        }
        initPopDataMap();

    }

    private void requestNearDev() {
        LocateCache locateCache = dao.getLocateCache();
        ArrayList<Double> locate = new ArrayList<>();
        locate.add(locateCache.lon);
        locate.add(locateCache.lat);
        mPresenter.requestNearDevs(0, cityId, locate);
    }

    private void initPopDataMap() {
        avgPriceBeanMap = new HashMap<>();
        avgPriceBeanMap.put("不限", new HomeSearchRequestBean.AvgPriceBean());
        avgPriceBeanMap.put("10000元以下", new HomeSearchRequestBean.AvgPriceBean("0", "10000"));
        avgPriceBeanMap.put("10000-20000元", new HomeSearchRequestBean.AvgPriceBean("10000", "20000"));
        avgPriceBeanMap.put("20000-30000元", new HomeSearchRequestBean.AvgPriceBean("20000", "30000"));
        avgPriceBeanMap.put("30000-50000元", new HomeSearchRequestBean.AvgPriceBean("30000", "50000"));
        avgPriceBeanMap.put("50000-100000元", new HomeSearchRequestBean.AvgPriceBean("50000", "100000"));
        avgPriceBeanMap.put("100000元以上", new HomeSearchRequestBean.AvgPriceBean("100000", ""));


        houseSizeBeanMap = new HashMap<>();
        houseSizeBeanMap.put("不限", new HomeSearchRequestBean.HouseSizeBean());
        houseSizeBeanMap.put("50m²以下", new HomeSearchRequestBean.HouseSizeBean("0", "50"));
        houseSizeBeanMap.put("50-70m²", new HomeSearchRequestBean.HouseSizeBean("50", "70"));
        houseSizeBeanMap.put("70-90m²", new HomeSearchRequestBean.HouseSizeBean("70", "90"));
        houseSizeBeanMap.put("110-150m²", new HomeSearchRequestBean.HouseSizeBean("110", "150"));
        houseSizeBeanMap.put("200-300m²", new HomeSearchRequestBean.HouseSizeBean("200", "300"));
        houseSizeBeanMap.put("300m²以上", new HomeSearchRequestBean.HouseSizeBean("300", ""));


        houseTypeMap = new HashMap<>();
        houseTypeMap.put("住宅", 0);
        houseTypeMap.put("别墅", 1);
        houseTypeMap.put("公寓", 2);
        houseTypeMap.put("写字楼", 3);
        houseTypeMap.put("商业", 4);
    }

    @Override
    protected void setActionBarDetail() {
//        if (type == 0) {
//            //在线选房
//            if (isTransparent.equals("1")) {
//                tvTitle.setText("在线选房");
//                actionBar.setVisibility(View.VISIBLE);
//                btnRight.setVisibility(View.VISIBLE);
//                btnRight.setImageResource(R.mipmap.ic_location_black);
//                llSerachView.setVisibility(View.GONE);
//            } else {
//                //全部楼盘
//                actionBar.setVisibility(View.GONE);
//                llSerachView.setVisibility(View.VISIBLE);
//                ivMapFind.setVisibility(TextUtils.isEmpty(devName) ? View.VISIBLE : View.GONE);
//            }
//
//        } else {
        //近期开盘
        tvTitle.setText("近期开盘");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.mipmap.ic_location_black);
        llSerachView.setVisibility(View.GONE);
        actionBar.setVisibility(View.VISIBLE);

//        }
    }

    @Override
    protected void tryRequestData() {

    }


    @Override
    public void showTip(String msg) {

    }

    @OnClick({R.id.llArea, R.id.llHousePrice, R.id.llAcreage, R.id.llMore, R.id.tvSearch, R.id.ivMapFind, R.id.imgBackHouse, R.id.btnRight})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.llArea:
                if (areaPopWindow == null) {
                    if (areas == null) {
                        return;
                    }
                    areaPopWindow = new AreaPopWindow(this, areas);
                    areaPopWindow.setOnItemClickListener(new AreaPopWindow.OnItemClickListener() {
                        @Override
                        public void onItemClick(CityAreaBean areaBean) {
                            homeSearchRequestBean.setAreaId(areaBean.areaid);
                            if (!areaBean.area.equals("全部")) {
                                tvArea.setText(areaBean.area);
                                tvArea.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._ff6800));
                            } else {
                                tvArea.setText("全部");
                                tvArea.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._5d5d5d));
                            }


                            onRefresh();
                        }
                    });


                    areaPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ObjectAnimator.ofFloat(ivArea, "rotation", 180, 0).setDuration(500).start();
                        }
                    });
                }
                showPopWindow2View(areaPopWindow, llSortContent);
                ObjectAnimator.ofFloat(ivArea, "rotation", 0, 180).setDuration(500).start();
                ivArea.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color._ff6800)));
                break;
            case R.id.llHousePrice:
                if (pricePopWindow == null) {
                    pricePopWindow = new ListPopWindow(this, priceTexts, 8);
                    pricePopWindow.setOnConfirmClickListener(new ListPopWindow.OnConfirmClickListener() {
                        @Override
                        public void onConfirmClick(String text) {
                            HomeSearchRequestBean.AvgPriceBean avgPriceBean = avgPriceBeanMap.get(text);
                            homeSearchRequestBean.getAvgPrice().clear();
                            homeSearchRequestBean.getAvgPrice().add(avgPriceBean);
                            if (!text.equals("不限")) {
                                tvHousePrice.setText(text);
                                tvHousePrice.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._ff6800));
                            } else {
                                tvHousePrice.setText("房价");
                                tvHousePrice.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._5d5d5d));
                            }
                            onRefresh();
                        }

                        @Override
                        public void onConfirmClick(String min, String max) {
                            HomeSearchRequestBean.AvgPriceBean avgPriceBean = new HomeSearchRequestBean.AvgPriceBean(min, max);
                            homeSearchRequestBean.getAvgPrice().clear();
                            homeSearchRequestBean.getAvgPrice().add(avgPriceBean);
                            if (TextUtils.isEmpty(min)) {
                                min = "0";
                            }

                            tvHousePrice.setText(min + "-" + max + "元");
                            tvHousePrice.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._ff6800));
                            onRefresh();
                        }
                    });
                    pricePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ObjectAnimator.ofFloat(ivHousePrice, "rotation", 180, 0).setDuration(500).start();
                            ivHousePrice.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(RecentHouseActivity.this, R.color._c4c4c4)));
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    KeyboardUtils.hideSoftInput(RecentHouseActivity.this);
                                }
                            }, 200);
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
                    houseAreaPopWindow.setOnConfirmClickListener(new ListPopWindow.OnConfirmClickListener() {
                        @Override
                        public void onConfirmClick(String text) {
                            HomeSearchRequestBean.HouseSizeBean houseSizeBean = houseSizeBeanMap.get(text);
                            homeSearchRequestBean.getHouseSize().clear();
                            homeSearchRequestBean.getHouseSize().add(houseSizeBean);
                            if (!text.equals("不限")) {
                                tvAcreage.setText(text);
                                tvAcreage.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._ff6800));
                            } else {
                                tvAcreage.setText("面积");
                                tvAcreage.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._5d5d5d));
                            }
                            onRefresh();
                        }

                        @Override
                        public void onConfirmClick(String min, String max) {
                            HomeSearchRequestBean.HouseSizeBean houseSizeBean = new HomeSearchRequestBean.HouseSizeBean(min, max);
                            homeSearchRequestBean.getHouseSize().clear();
                            homeSearchRequestBean.getHouseSize().add(houseSizeBean);

                            if (TextUtils.isEmpty(min)) {
                                min = "0";
                            }

                            tvAcreage.setText(min + "-" + max + "m²");
                            tvAcreage.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._ff6800));
                            onRefresh();
                        }
                    });
                    houseAreaPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ObjectAnimator.ofFloat(ivAcreage, "rotation", 180, 0).setDuration(500).start();
                            ivAcreage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(RecentHouseActivity.this, R.color._c4c4c4)));
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    KeyboardUtils.hideSoftInput(RecentHouseActivity.this);
                                }
                            }, 200);
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
                    houseTypeFilterPopWindow.setOnConfirmClickListener(new HouseTypeFilterPopWindow.OnConfirmClickListener() {
                        @Override
                        public void onConfirmClick(Object[] pos1, Object[] pos2) {
                            List<String> propertyType = homeSearchRequestBean.getPropertyType();
                            List<String> devBedroomInfo = homeSearchRequestBean.getDevBedroomInfo();
                            propertyType.clear();
                            devBedroomInfo.clear();
                            if (pos1 != null && pos1.length > 0) {
                                for (int i = 0; i < pos1.length; i++) {
                                    Integer integer = (Integer) pos1[i];
                                    propertyType.add(houseTypeMap.get(houseType[integer]) + "");
                                }
                            }

                            if (pos2 != null && pos2.length > 0) {
                                for (int i = 0; i < pos2.length; i++) {
                                    Integer integer = (Integer) pos2[i];
                                    devBedroomInfo.add(houseType2[integer]);
                                }
                            }

                            if (pos1.length > 0 || pos2.length > 0) {
                                tvMore.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._ff6800));
                            } else {
                                tvMore.setTextColor(ContextCompat.getColor(RecentHouseActivity.this, R.color._5d5d5d));
                            }

                            onRefresh();
                        }
                    });
                    houseTypeFilterPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            ObjectAnimator.ofFloat(ivMore, "rotation", 180, 0).setDuration(500).start();
                            ivMore.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(RecentHouseActivity.this, R.color._c4c4c4)));
                        }
                    });
                }
                showPopWindow2View(houseTypeFilterPopWindow, llSortContent);
                ObjectAnimator.ofFloat(ivMore, "rotation", 0, 180).setDuration(500).start();
                ivMore.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color._ff6800)));
                break;

            case R.id.tvSearch:
                Intent searchIntent = new Intent(this, SearchHouseActivity.class);
                searchIntent.putExtra("cityId", cityId);
                searchIntent.putExtra("city", city);
                startActivity(searchIntent);
                break;
            case R.id.ivMapFind:
//                startActivity(new Intent(this, MapFindHouseActivity.class));
                MapFindHouseActivity.startActivity(this, city, cityId);
                break;

            case R.id.imgBackHouse:
                finish();
                break;
            case R.id.btnRight:
//                startActivity(new Intent(this, MapFindHouseActivity.class));
                MapFindHouseActivity.startActivity(this, city, cityId);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String devName = data.getStringExtra("devName");
            tvSearch.setText(devName);
            tvSearch.setTextColor(ContextCompat.getColor(this, R.color._5d5d5d));
            homeSearchRequestBean.setDevName(devName);
            onRefresh();
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

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void showSuccessData(List<HomeSearchResponseBean> baseBeanList) {
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
        cancelLoading();
        this.baseBeanList = baseBeanList;
        if (page == Constants.PAGE_START) {
            mAdapter.setNewData(baseBeanList);
            if (baseBeanList.size() < 10) {
                requestNearDev();
            }
        } else {
            mAdapter.addData(baseBeanList);
            mAdapter.loadMoreComplete();
        }
        allBaseBeanList.addAll(baseBeanList);
    }

    @Override
    public void areasSuccessData(List<CityAreaBean> areas) {
        this.areas = areas;
    }

    @Override
    public void showFail(String code) {
        ToastUtils.showShort(code);
    }

    @Override
    public void showNearDevs(List<HomeSearchResponseBean> baseBeanList) {
        if (baseBeanList != null && baseBeanList.size() > 0) {
            nearAdapter.setNewData(baseBeanList);
        } else {
            tvNear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        rcvHouseList.post(new Runnable() {
            @Override
            public void run() {
                if (baseBeanList != null && baseBeanList.size() >= Constants.PAGE_SIZE) {
                    page++;
                    mPresenter.requestHomeSearch(0, page, homeSearchRequestBean);
                } else {
                    mAdapter.loadMoreEnd();
                    mAdapter.setEnableLoadMore(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        allBaseBeanList.clear();
        page = 1;
        mPresenter.requestHomeSearch(0, 1, homeSearchRequestBean);
    }
}
