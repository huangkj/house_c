package com.easylife.house.customer.ui.homefragment.homeindexv3;


import android.Manifest;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.App;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.HomeAdapter2;
import com.easylife.house.customer.adapter.HomeNewsAdapter;
import com.easylife.house.customer.adapter.SimpleViewPagerAdapter;
import com.easylife.house.customer.bean.BannerListBean;
import com.easylife.house.customer.bean.CityPriceBean;
import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.HeadLineBean;
import com.easylife.house.customer.bean.HomeBean;
import com.easylife.house.customer.bean.HomeNewsBean;
import com.easylife.house.customer.bean.HotDevBean;
import com.easylife.house.customer.bean.ItemBean;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.bean.MultipleItemBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.homesearch.allhouse.AllHouseActivity;
import com.easylife.house.customer.ui.homesearch.brandland.BrandLandActivity;
import com.easylife.house.customer.ui.homesearch.recenthouse.RecentHouseActivity;
import com.easylife.house.customer.ui.homesearch.searchhouse.SearchHouseActivity;
import com.easylife.house.customer.ui.homesearch.shoprent.ShopRentActivity;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.ui.houses.housesdetail.pager.NetworkImageHolderView;
import com.easylife.house.customer.ui.houses.map.mapfindhouse.MapFindHouseActivity;
import com.easylife.house.customer.ui.message.topmessage.TopMessageActivity;
import com.easylife.house.customer.ui.mine.calculator.CalculatorActivity;
import com.easylife.house.customer.ui.mine.sign.SignActivity;
import com.easylife.house.customer.ui.pub.MainActivity;
import com.easylife.house.customer.ui.pub.PubWebViewActivity;
import com.easylife.house.customer.ui.pub.StoreWebActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.ui.qrhousesignup.HouseSignUpActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DateUtil;
import com.easylife.house.customer.util.DimenUtils;
import com.easylife.house.customer.util.DrawableUtil;
import com.easylife.house.customer.view.AutoVerticalTextview;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;
import static com.easylife.house.customer.config.Constants.URL_WEB_BASE;
import static com.easylife.house.customer.event.MessageEvent.HOUSES_INDEXT_COLLECTION;
import static com.easylife.house.customer.event.MessageEvent.HOUSES_INDEXT_COLLECTION_LOGIN;
import static com.easylife.house.customer.event.MessageEvent.UPDATE_SEARCH_DATA;
import static com.easylife.house.customer.ui.pub.PubWebViewActivity.GAME_ACTIVITY;
import static com.easylife.house.customer.ui.pub.PubWebViewActivity.HOUSE_TOPIC;
import static com.easylife.house.customer.ui.pub.PubWebViewActivity.INSURANCE;


/**
 * 首页
 */
@RuntimePermissions
public class HomeIndexFragment extends MVPBaseFragment<HomeIndexContract.View, HomeIndexPresenter>
        implements HomeIndexContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.home_recycle)
    RecyclerView homeRecycle;
    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.tvCityHome)
    TextView tvCityHome;
    @Bind(R.id.appBar)
    AppBarLayout appBar;
    @Bind(R.id.llAppbarContent)
    LinearLayout llAppbarContent;
    private AnimationSet mHideSet;
    private AnimationSet mShowSet;
    private List<MultipleItemBean> beanList = new ArrayList<>();
    private HomeAdapter2 mAdapter;
    private boolean isExpand = true;
    private float pageMargin260;
    private float pageMargin114;
    private float pageMargin96;
    private float pageMargin56;
    private float pageMargin20;
    private float tvOffset1;
    private float tvOffset2;
    private float tvOffset3;
    public boolean isShowQuickTop;


    private static final int REQUEST_CODE = 10;
    private SearchSingleton singletonSearch;
    private SimpleViewPagerAdapter iconAdapter;
    private ConvenientBanner bannerHome;
    private ViewPager vpIndex;
    private RadioButton mIndicator0;
    private RadioButton mIndicator1;
    private RadioButton mIndicator2;
    private AutoVerticalTextview wheelAd;
    private RecyclerView rcvNews;
    private HomeNewsAdapter homeNewsAdapter;
    private ArrayList<HeadLineBean.ListBean> adlist;
    private View headView;
    private TextView tvHotHouseTx;
    private TextView tvCityName;
    private TextView tvCityPrice;
    private LinearLayout llCityPriceContainer;


    @Override
    public void showSuccess(List<HomeBean> homeList) {
//        cancelLoading();
//        if (swipeRefreshLayout.isRefreshing()) {
//            swipeRefreshLayout.setRefreshing(false);
//        }
//
//        if (mAdapter != null) {
//            if (homeList != null) {
//                Collections.sort(homeList, new Comparator<HomeBean>() {
//                    @OverriOde
//                    public int compare(HomeBean o1, HomeBean o2) {
//                        return o1.position.compareTo(o2.position);
//                    }
//                });
//                beanList.clear();
//                MultipleItemBean bean = new MultipleItemBean(10);
//                bean.beanList = homeList;
//                beanList.add(bean);tvCityHome
//                mAdapter.setType(false);
//                mAdapter.setNewData(beanList);
//            } else {
//                beanList.clear();
//                MultipleItemBean bean = new MultipleItemBean(-1);
//                beanList.add(bean);
//                mAdapter.setType(true);
//                mAdapter.setNewData(beanList);
//            }
//        }
    }

    @Override
    public void showFail(String msg) {
//        CustomerUtils.showTip(getActivity(), msg);
        cancelLoading();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void showCollectList(List<CollectionListBean> collectionList) {
//
//        for (CollectionListBean bean : collectionList) {
//            singletonSearch.collectList.add(bean.devId);
//        }
//        mAdapter.setCollectionList(singletonSearch.collectList);
//        mAdapter.notifyDataSetChanged();
    }

    /**
     * 收藏楼盘
     */
    @Override
    public void showCollect() {
        CustomerUtils.showTip(getActivity(), "收藏成功");
    }

    @Override
    public void showDelCollectSucc() {
        CustomerUtils.showTip(getActivity(), "取消收藏");
    }

    @Override
    public void jumpToCommitUserInfo(String params) {
        startActivity(new Intent(getActivity(), HouseSignUpActivity.class).putExtra("QR_STR", params));
    }

    @Override
    public void showBannerList(final BannerListBean bannerListBean) {
        final List<BannerListBean.ListBean> list = bannerListBean.getList();
        ArrayList<String> imgs = new ArrayList<>();

        for (BannerListBean.ListBean bean :
                list) {
            imgs.add(bean.getBannerAddress());
        }


        bannerHome.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, imgs).setPageIndicator(new int[]{R.mipmap.banner_unselect, R.mipmap.banner_select})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        bannerHome.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                MobclickAgent.onEvent(getActivity(), "home_banner");

                BannerListBean.ListBean banner = list.get(position);

                String linkAddress = banner.getLinkAddress();

                if (linkAddress.contains("/#/activity/card") || linkAddress.contains("/#/rotate")) {
                    if (mPresenter.isLogin()) {
                        //是活动链接，拼一下用户id
                        linkAddress = linkAddress + " &buyerId=" + mDao.localDao.getCustomer().id;
                        WebViewActivity.startActivity(getActivity(), "游戏活动", linkAddress);
                    } else {
                        WebViewActivity.startActivity(getActivity(), "游戏活动", linkAddress);
//                        startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 0);
                    }
                } else if (linkAddress.contains("thematicPage")) {
                    PubWebViewActivity.startActivity(getActivity(), "", linkAddress, HOUSE_TOPIC);
                } else {
                    WebViewActivity.startActivity(getActivity(), "", linkAddress);
                }
            }
        });
        bannerHome.setCanLoop(true);
        bannerHome.startTurning(3000);
    }

    @Override
    public void showHeadLine(HeadLineBean headLineBean) {
        if (headLineBean == null) {
            return;
        }

        adlist = headLineBean.getList();
        if (adlist == null || adlist.size() == 0) {
            return;
        }

        wheelAd.setDataList(adlist);
        wheelAd.setText(12, 0, getResources().getColor(R.color._797979));//设置属性
        wheelAd.setTextStillTime(3000);//设置停留时长间隔
        wheelAd.setAnimTime(300);//设置进入和退出的时间间隔
        wheelAd.startAutoScroll();


        wheelAd.setOnItemClickListener(new AutoVerticalTextview.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                HeadLineBean.ListBean listBean = adlist.get(position);
                MsgHeadLine msgHeadLine = new MsgHeadLine();
                msgHeadLine.createTime = DateUtil.phpToDate(listBean.getCreateTime() + "", "yyyy-MM-dd HH:mm:ss");
                msgHeadLine.logo = listBean.getLogo();
                msgHeadLine.id = listBean.getId();
                msgHeadLine.text = listBean.getText();
                msgHeadLine.title = listBean.getTitle();
                msgHeadLine.logoUrl = listBean.getLogoUrl();
                msgHeadLine.url = listBean.getUrl();
                WebViewActivity.startActivity(getActivity(), "资讯", URL_WEB_BASE + adlist.get(position).getId(), true, msgHeadLine);
            }
        });


//
//        try {
//            final List<HeadLineBean.ListBean> list = headLineBean.getList();
//            wheelAd.stopAutoScroll();
//            wheelAd.removeAllViews();
//
//            for (int i = 0; i < list.size(); i++) {
//                final TextView tv = new TextView(getActivity());
//                tv.setTextSize(14);
//                tv.setTextColor(ContextCompat.getColor(getActivity(), R.color._797979));
//                tv.setSingleLine();
//                tv.setEllipsize(TextUtils.TruncateAt.END);
//                tv.setGravity(Gravity.CENTER_VERTICAL);
//                tv.setLayoutParams(new LinearLayout.LayoutParams(
//                        MATCH_PARENT, MATCH_PARENT));
//                final HeadLineBean.ListBean listBean = list.get(i);
//                if (null != list) {
//                    tv.setText(listBean.getTitle());
//                }
//                wheelAd.addView(tv);
//                final int finalI = i;
//                tv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        MsgHeadLine msgHeadLine = new MsgHeadLine();
//                        msgHeadLine.createTime = DateUtil.phpToDate(listBean.getCreateTime() + "", "yyyy-MM-dd HH:mm:ss");
//                        msgHeadLine.logo = listBean.getLogo();
//                        msgHeadLine.id = listBean.getId();
//                        msgHeadLine.text = listBean.getText();
//                        msgHeadLine.title = listBean.getTitle();
//                        msgHeadLine.logoUrl = listBean.getLogoUrl();
//                        msgHeadLine.url = listBean.getUrl();
//                        WebViewActivity.startActivity(getActivity(), "资讯", URL_WEB_BASE + list.get(finalI).getId(), true, msgHeadLine);
//                    }
//                });
//
//            }
//            wheelAd.startAutoScroll();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void showHomeNews(HomeNewsBean homeNewsBean) {
        if (homeNewsBean != null) {
            List<HomeNewsBean.ListBean> list = homeNewsBean.getList();
            for (int i = 0; i < list.size(); i++) {
                HomeNewsBean.ListBean listBean = list.get(i);
                switch (i) {
                    case 0:
                        listBean.setTopTitle("#房产头条#");
                        if (TextUtils.isEmpty(listBean.getPictureAddress())) {
                            listBean.setLocalPicture(R.mipmap.iv_house_top);
                        }
                        break;
                    case 1:
                        listBean.setTopTitle("#国家政策#");
                        if (TextUtils.isEmpty(listBean.getPictureAddress())) {
                            listBean.setLocalPicture(R.mipmap.iv_nation_policy);
                        }
                        break;
                    case 2:
                        listBean.setTopTitle("#房市动态#");
                        if (TextUtils.isEmpty(listBean.getPictureAddress())) {
                            listBean.setLocalPicture(R.mipmap.iv_house_news_);
                        }
                        break;
                    case 3:
                        listBean.setTopTitle("#新闻资讯#");
                        if (TextUtils.isEmpty(listBean.getPictureAddress())) {
                            listBean.setLocalPicture(R.mipmap.iv_news_info);
                        }
                        break;
                }

            }
            homeNewsAdapter.setNewData(list);
        }

    }

    @Override
    public void setRefresh(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
    }

    @Override
    public void showHotDevList(List<HotDevBean> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void showCityPrice(CityPriceBean cityPriceBean) {
        if (cityPriceBean != null && !cityPriceBean.getPrice().equals("暂无")) {
            llCityPriceContainer.setVisibility(View.VISIBLE);
            if (cityPriceBean.getCityName().length() > 4) {
                tvCityName.setText(cityPriceBean.getCityName().substring(0, 4) + "...\n房市");
            } else {


                tvCityName.setText(cityPriceBean.getCityName() + "房市");
            }
            tvCityPrice.setText(cityPriceBean.getPrice());
        } else {
            llCityPriceContainer.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.tvCityHome, R.id.tvSearchHome, R.id.ivScanHome})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCityHome:
                Intent intent = new Intent(getActivity(), ChooseCityActivity.class);
                intent.putExtra("areaid", singletonSearch.cityId);
                intent.putExtra("area", singletonSearch.city);
                intent.putExtra("getAreaData", false);
                startActivityForResult(intent, 2);
//                ChooseCityActivity.startActivity(this, singletonSearch.cityId, singletonSearch.city, false, 2);
                break;

            case R.id.tvSearchHome:
//                FitmentActivity.startActivity(getActivity());
                SearchHouseActivity.startActivity(getActivity(), singletonSearch.cityId, singletonSearch.city, 0);
                break;
            case R.id.ivScanHome:
                if (mDao.isLogin()) {
                    HomeIndexFragmentPermissionsDispatcher.jumpQRWithCheck(this);
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 0);
                }
                break;


        }
    }

    @NeedsPermission((Manifest.permission.CAMERA))
    public void jumpQR() {
        //跳转二维码扫描
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * 6.0权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeIndexFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == UPDATE_SEARCH_DATA) {

        } else if (event.MsgType == HOUSES_INDEXT_COLLECTION_LOGIN) {
            if (mDao.isLogin()) {
                mPresenter.collectHouseList(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
            }
        } else if (event.MsgType == HOUSES_INDEXT_COLLECTION) {
            //楼盘详情 收藏楼盘 首页在此刷新
//            mAdapter.setCollectionList(singletonSearch.collectList);
//            mAdapter.notifyDataSetChanged();
        } else {
//            tvContent3.setText("");
//            tvSingleSearch.setText("");
        }
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
        Log.e("showTip", msg);
    }

    @Override
    public int getLayout() {
        return R.layout.home_fragment_v3;
    }


    public int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }


    @Override
    public void initViews() {
        //设置状态栏字体为黑色
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.gradient_end));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(true, 0, 200);

        singletonSearch = SearchSingleton.getIstance();

        if (mDao.localDao != null) {
            singletonSearch.city = mDao.localDao.getCity();
            singletonSearch.cityId = mDao.localDao.getCityId();
            singletonSearch.buyWhere = mDao.localDao.getCity();
            singletonSearch.whereHouse = "";
            if (TextUtils.isEmpty(singletonSearch.city)) {
                singletonSearch.cityId = "110100";
                singletonSearch.city = "北京市";
            }

//            tvContent1.setText(singletonSearch.city);
        } else {
            singletonSearch.cityId = "110100";
            singletonSearch.city = "北京市";
            singletonSearch.buyWhere = "北京市";
            singletonSearch.whereHouse = "";
//            tvContent1.setText("北京市");
        }

//        setSearchData(singletonSearch);
//        showLoading();

        headView = View.inflate(getActivity(), R.layout.home_fragment_headview, null);

        bannerHome = (ConvenientBanner) headView.findViewById(R.id.bannerHome);
        vpIndex = (ViewPager) headView.findViewById(R.id.vpIndex);
        mIndicator0 = (RadioButton) headView.findViewById(R.id.mIndicator0);
        mIndicator1 = (RadioButton) headView.findViewById(R.id.mIndicator1);
        mIndicator2 = (RadioButton) headView.findViewById(R.id.mIndicator2);
        wheelAd = (AutoVerticalTextview) headView.findViewById(R.id.wheelAd);
        rcvNews = (RecyclerView) headView.findViewById(R.id.rcvNews);
        tvHotHouseTx = (TextView) headView.findViewById(R.id.tvHotHouseTx);
        tvCityName = (TextView) headView.findViewById(R.id.tvCityName);
        tvCityPrice = (TextView) headView.findViewById(R.id.tvCityPrice);
        llCityPriceContainer = (LinearLayout) headView.findViewById(R.id.llCityPriceContainer);


        View footView = View.inflate(getActivity(), R.layout.home_hot_dev_footview, null);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更多楼盘
                AllHouseActivity.startActivity(getActivity(), 0, "", singletonSearch.cityId, singletonSearch.city, "", 0);
            }
        });


        mAdapter = new HomeAdapter2(null);
        mAdapter.addHeaderView(headView);
        mAdapter.addFooterView(footView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MobclickAgent.onEvent(getActivity(), "home_house");

                HotDevBean item = mAdapter.getItem(position);
//                    searchSingleton.lookHouse.add(AllHouseActivity.this);
//                    if (item.coOp == 3) {
//                        startActivity(new Intent(AllHouseActivity.this, HousesAndTypeSimpleActivity.class).putExtra("DEV_ID", baseBean.devId));
//                    } else {
//                startActivity(new Intent(getActivity(), HousesAndTypeActivity.class).putExtra("DEV_ID", item.devId));
//                }
//
                HouseDetailActivity.startActivity(getActivity(), item.devId, item.coOp, 0);
            }
        });

        homeRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeRecycle.setAdapter(mAdapter);


        homeNewsAdapter = new HomeNewsAdapter(R.layout.news_info_item, null);
        rcvNews.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rcvNews.addItemDecoration(new GridSpacingItemDecoration(2, SizeUtils.dp2px(15), false));
        rcvNews.setAdapter(homeNewsAdapter);
        rcvNews.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                MobclickAgent.onEvent(getActivity(), "home_info");

                HomeNewsBean.ListBean listBean = (HomeNewsBean.ListBean) baseQuickAdapter.getItem(i);
                String titleOrLink = listBean.getTitleOrLink();
                if (listBean.getType() == 1) {
                    //进入详情
                    if (titleOrLink.contains("thematicPage")) {
                        PubWebViewActivity.startActivity(getActivity(), "", titleOrLink, HOUSE_TOPIC);//楼盘专题
                    } else {
                        WebViewActivity.startActivity(getActivity(), "资讯", titleOrLink);
                    }
                } else {
                    TopMessageActivity.startActivity(getActivity(), i, 0);
                }


            }
        });

        mPresenter.getCacheData();
        mPresenter.requestIndexMsg();
        mPresenter.requestBannerList(singletonSearch.cityId);
        mPresenter.requestHeadList(singletonSearch.cityId);
        mPresenter.requestHomeNews(singletonSearch.cityId);
        mPresenter.requestHotDev(singletonSearch.cityId);
        mPresenter.getCityHousePrice(singletonSearch.cityId);

        //滚动头条
//        wheelAd.setCan3D(true);
//        wheelAd.setCanTouch(false);
        tvCityHome.setText(singletonSearch.city);


//        initData();
        initIcons();
        tvCityHome.setFocusableInTouchMode(true);
        tvCityHome.requestFocus();

        bannerHome.setFocusableInTouchMode(true);//防止rcv自动滑动
        bannerHome.requestFocus();


        int statusBarHeight = 0;
        int resourceId = App.applicationContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = App.applicationContext.getResources().getDimensionPixelSize(resourceId);
        }

        statusBarHeight = SizeUtils.px2dp(statusBarHeight);
        statusBarHeight += 4;
        llAppbarContent.setPadding(0, SizeUtils.dp2px(statusBarHeight), 0, 0);

//        homeRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//            }
//        });


        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBar.getTotalScrollRange();
                totalScrollRange = totalScrollRange - SizeUtils.dp2px(25);
                Log.d("onOffsetChanged", "verticalOffset:" + verticalOffset + "totalScrollRange:" + totalScrollRange);
//                if (totalScrollRange + verticalOffset <= 0) {
//                    vStatus.setVisibility(View.VISIBLE);
//                } else {
//                    vStatus.setVisibility(View.GONE);
//
//                }
            }
        });


    }

    private void initIcons() {
        String[] iconTexts = {"全部楼盘", "品牌地产", "商铺租赁", "在线订房", "房贷计算", "近期开盘", "每日签到", "游戏活动", "积分商城", "地图找房", "保险服务"};
        int iconImages[] = {R.mipmap.icon_all_house, R.mipmap.icon_brand, R.mipmap.icon_shop_rent,
                R.mipmap.icon_online_buy_house, R.mipmap.icon_house_loan, R.mipmap.icon_recent_house,
                R.mipmap.icon_sign, R.mipmap.icon_game, R.mipmap.icon_store, R.mipmap.icon_map_find, R.mipmap.icon_insurance};


        List<View> viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        LinearLayout linearLayout = null;
        LinearLayout verticalLinearLayout = null;

        for (int i = 0; i < iconTexts.length; i++) {
            if (i % 10 == 0) {
                verticalLinearLayout = new LinearLayout(getActivity());//
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);//水平线性布局
                verticalLinearLayout.setOrientation(VERTICAL);
                verticalLinearLayout.setLayoutParams(lp);
                viewList.add(verticalLinearLayout);
            }
            final String iconText = iconTexts[i];
            int iconImage = iconImages[i];

            if (i % 5 == 0) {
                linearLayout = new LinearLayout(getActivity());//水平线性布局
                LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                linearLayout.setOrientation(HORIZONTAL);
                linearLayout.setLayoutParams(hlp);
                verticalLinearLayout.addView(linearLayout);
            }
            RelativeLayout relIconView = (RelativeLayout) View.inflate(getActivity(), R.layout.home_icon_item, null);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, DimenUtils.dip2px(getActivity(), 90));
            lp2.weight = 1;
            relIconView.setLayoutParams(lp2);
            TextView tvIcon = (TextView) relIconView.findViewById(R.id.icon);
            tvIcon.setText(iconText);
            DrawableUtil.drawableTop(getActivity(), tvIcon, iconImage);
            linearLayout.addView(relIconView);

            relIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> map = new HashMap<>();
                    switch (iconText) {
                        case "全部楼盘":
                            map.put("home_icon", "全部楼盘");
                            AllHouseActivity.startActivity(getActivity(), 0, "", singletonSearch.cityId, singletonSearch.city, "", 0);
                            break;
                        case "品牌地产":
                            map.put("home_icon", "品牌地产");
                            startActivity(new Intent(getActivity(), BrandLandActivity.class));

                            break;
                        case "商铺租赁":
                            map.put("home_icon", "商铺租赁");
                            ShopRentActivity.startActivity(getActivity(), singletonSearch.cityId, 0);
                            break;
                        case "在线订房":
                            map.put("home_icon", "在线订房");
                            AllHouseActivity.startActivity(getActivity(), 0, "", singletonSearch.cityId, singletonSearch.city, "1", 0);
                            break;
                        case "房贷计算":
                            map.put("home_icon", "房贷计算");
                            startActivity(new Intent(getActivity(), CalculatorActivity.class));
                            break;
                        case "近期开盘":
                            map.put("home_icon", "近期开盘");
                            RecentHouseActivity.startActivity(getActivity(), 1, "", singletonSearch.cityId, singletonSearch.city, "", 0);
                            break;
                        case "地图找房":
                            map.put("home_icon", "地图找房");
                            MapFindHouseActivity.startActivity(getActivity(), singletonSearch.city, singletonSearch.cityId);
                            break;
                        case "每日签到":
                            map.put("home_icon", "每日签到");
                            if (!mPresenter.isLogin()) {
                                startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 0);
                                return;
                            }
                            startActivity(new Intent(getActivity(), SignActivity.class));
                            break;
                        case "游戏活动":
                            map.put("home_icon", "游戏活动");
                            if (!mPresenter.isLogin()) {
                                PubWebViewActivity.startActivity(getActivity(), "游戏活动", Constants.ACTIVITY_GAME_URL, GAME_ACTIVITY);
                                return;
                            }
                            PubWebViewActivity.startActivity(getActivity(), "游戏活动", Constants.ACTIVITY_GAME_URL + mDao.localDao.getCustomer().id, GAME_ACTIVITY);
                            break;

                        case "积分商城":
                            map.put("home_icon", "积分商城");
                            if (!mPresenter.isLogin()) {
//                                startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 0);
                                StoreWebActivity.startActivity(getActivity(), "积分商城",
                                        Constants.WEB_INTEGRAL_STORE);
                                return;
                            }
                            Customer customer = mDao.localDao.getCustomer();
                            StoreWebActivity.startActivity(getActivity(), "积分商城",
                                    Constants.WEB_INTEGRAL_STORE + "?userId=" + customer.id + "&userName=" + customer.username + "&userPhone=" + customer.phone
                            );

                            break;

                        case "保险服务":
                            map.put("home_icon", "保险服务");
                            PubWebViewActivity.startActivity(getActivity(), "保险服务", Constants.URL_WEB_INSURANCE, INSURANCE);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            vpIndex.setCurrentItem(0);
                                        }
                                    });
                                }
                            }, 1000);
                            break;
                        default:
                            break;
                    }
                    MobclickAgent.onEvent(getActivity(), "home_icon", map);
                }
            });

        }

        //补足
        LinearLayout endChildView = (LinearLayout) verticalLinearLayout.getChildAt(verticalLinearLayout.getChildCount() - 1);
        int childCount = endChildView.getChildCount();
        int needAddEmptyCount = 0;
        int im = childCount % 5;
        if (im != 0) {
            needAddEmptyCount = 5 - im;
        }

        if (needAddEmptyCount > 0) {
            for (int i = 0; i < needAddEmptyCount; i++) {
                RelativeLayout relIconView = (RelativeLayout) View.inflate(getActivity(), R.layout.home_icon_item, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, DimenUtils.dip2px(getActivity(), 120));
                lp.weight = 1;
                relIconView.setLayoutParams(lp);
                TextView tvIcon = (TextView) relIconView.findViewById(R.id.icon);
                tvIcon.setText("");
                DrawableUtil.drawableTop(getActivity(), tvIcon, R.mipmap.icon_all_house);
                relIconView.setVisibility(View.INVISIBLE);
                endChildView.addView(relIconView);
            }
        }


        iconAdapter = new SimpleViewPagerAdapter(viewList);
        vpIndex.setAdapter(iconAdapter);
        if (viewList.size() > 2) {
            mIndicator2.setVisibility(View.VISIBLE);
        } else {
            mIndicator2.setVisibility(View.GONE);

        }
        vpIndex.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    mIndicator0.setChecked(true);
                } else if (i == 1) {
                    mIndicator1.setChecked(true);
                } else {
                    mIndicator2.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });


        homeRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int height = 0;
            int tvH = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                height = headView.getHeight();
                tvH = tvHotHouseTx.getHeight();
                height = height - tvH - SizeUtils.dp2px(10);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy -= dy;
                String model = Build.MODEL;
                Log.d("!!!", totalDy + "  model:" + model);
                if (!"Redmi Note 3".equals(model)) {
                    if (totalDy < 0) {
                        appBar.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), R.animator.appbar_elevation));
                    } else {
                        appBar.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), R.animator.appbar_elevation0));
                    }
                }

//                int scollYDistance = getScollYDistance(homeRecycle);
                Log.d("@@", totalDy + "");
                if (totalDy + height < 0) {
                    ((MainActivity) getActivity()).setHomeTabIcon(R.mipmap.index_top, true);
                    isShowQuickTop = true;

                } else {
                    ((MainActivity) getActivity()).setHomeTabIcon(R.drawable.selector_index_tab_1, false);
                    isShowQuickTop = false;

                }
            }
        });


//        isFirstInto();
    }

    private int totalDy = 0;

    public void scroll2Top() {
        totalDy = 0;
        homeRecycle.scrollToPosition(0);
        appBar.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), R.animator.appbar_elevation0));
        isShowQuickTop = false;
        onRefresh();
    }


    public static HomeIndexFragment newInstance() {
        Bundle args = new Bundle();
        HomeIndexFragment fragment = new HomeIndexFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<ItemBean> listItem = new ArrayList<>();


    /**
     * 是不是第一次进入
     */
    private void isFirstInto() {
        final Timer timer = new Timer();
        final TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vpIndex.setCurrentItem(0);
                    }
                });

            }
        };

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vpIndex.setCurrentItem(1);
                        timer.schedule(task2, 1000);
                    }
                });

            }
        };


        timer.schedule(task, 1000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.d("saoyisao", result);
                    if (!TextUtils.isEmpty(result)) {

                        if (!NetworkUtils.isConnected(getActivity())) {
                            CustomerUtils.showTip(getActivity(), "当前网络不可用");
                            return;
                        }

                        if (result.contains(Constants.QR_URL)) {
                            mPresenter.resolveQrParams(result);
                        } else {
                            CustomerUtils.showTip(getActivity(), "二维码无效");
                        }
                    } else {
                        CustomerUtils.showTip(getActivity(), "二维码无效");
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            //   选择城市
            String city = ChooseCityActivity.getSelectedText(data);
            String cityId = ChooseCityActivity.getSelectedId(data);
            if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(cityId)) {
                tvCityHome.setText(ChooseCityActivity.getSelectedText(data));
                singletonSearch.cityId = cityId;
                singletonSearch.city = city;
                onRefresh();
            }

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        }, 200);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                mPresenter.requestIndexMsg();
                mPresenter.requestBannerList(singletonSearch.cityId);
                mPresenter.requestHeadList(singletonSearch.cityId);
                mPresenter.requestHomeNews(singletonSearch.cityId);
                mPresenter.requestHotDev(singletonSearch.cityId);
                mPresenter.getCityHousePrice(singletonSearch.cityId);
                if (mDao.isLogin()) {
                    mPresenter.collectHouseList(mDao.getLoginCache().userCode, mDao.getLoginCache().token, "0");
                }
            }
        }, 1000);


    }
}
