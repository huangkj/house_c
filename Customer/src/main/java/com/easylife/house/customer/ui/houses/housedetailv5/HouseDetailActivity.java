package com.easylife.house.customer.ui.houses.housedetailv5;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureSupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.CommentListAdapter;
import com.easylife.house.customer.adapter.DiscountAdapter;
import com.easylife.house.customer.adapter.ExpertTeamAdapter;
import com.easylife.house.customer.adapter.FavorableShowAdapter;
import com.easylife.house.customer.adapter.HouseLikeAdapter;
import com.easylife.house.customer.adapter.HousesDetailMainAdapter;
import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.CommentUrlBean;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.DisCountIsAlreadyBean;
import com.easylife.house.customer.bean.DiscountListBean;
import com.easylife.house.customer.bean.DymanicBeanList;
import com.easylife.house.customer.bean.ExportListBean;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.GitDisCountBean;
import com.easylife.house.customer.bean.HouseInfoSubBean;
import com.easylife.house.customer.bean.HouseSaleListBean;
import com.easylife.house.customer.bean.HouseSubNumBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.bean.HousesLikeBean;
import com.easylife.house.customer.bean.HousesToPagerImgBean;
import com.easylife.house.customer.bean.HousesTopImgBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.bean.PageTitleBean;
import com.easylife.house.customer.bean.ResultCustomerIsOrder;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.houses.exportshop.shop.ExportShopActivity;
import com.easylife.house.customer.ui.houses.housepincontrol.HousePinControlActivity;
import com.easylife.house.customer.ui.houses.housereslist.HouseResListActivity;
import com.easylife.house.customer.ui.houses.housesdetail.HouseDetailPresenter;
import com.easylife.house.customer.ui.houses.housesdetail.HousesDetailContract;
import com.easylife.house.customer.ui.houses.housesdetail.bookinghouse.BookingHouseActivity;
import com.easylife.house.customer.ui.houses.housesdetail.comment.CommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.exportlist.ExportActivity;
import com.easylife.house.customer.ui.houses.housesdetail.housescomment.CommentPagerActivity;
import com.easylife.house.customer.ui.houses.housesdetail.housescomment.HousesCommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.housesmore.HousesMoreActivity;
import com.easylife.house.customer.ui.houses.housesdetail.newdynamic.NewDynamicActivity;
import com.easylife.house.customer.ui.houses.housesdetail.pager.NetworkImageHolderView;
import com.easylife.house.customer.ui.houses.housesdetail.pager.PagerActivity;
import com.easylife.house.customer.ui.houses.housetype.housesTypeDetail.HousesTypeDetailActivity;
import com.easylife.house.customer.ui.houses.map.maparound.MapAroundActivity;
import com.easylife.house.customer.ui.houses.map.mapperipheral.MapPeripheralActivity;
import com.easylife.house.customer.ui.mine.calculator.CalculatorActivity;
import com.easylife.house.customer.ui.mine.order.orderdetail.OrderDetailActivity;
import com.easylife.house.customer.ui.payment.favorable.FavorableSelectActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DialogUtil;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.MapUtil;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.util.StatusBarUtils;
import com.easylife.house.customer.view.FlowViewGroup;
import com.easylife.house.customer.view.SpaceItemDecoration;
import com.easylife.house.customer.view.anchor.Anchor;
import com.easylife.house.customer.view.anchor.ObservableScrollView;
import com.easylife.house.customer.view.dialog.HouseNoticeDialog;
import com.easylife.house.customer.view.dialog.HouseNoticeDialog2;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.config.Constants.CTOB;
import static com.easylife.house.customer.config.Constants.HOUSE_NUMBER_URL;
import static com.easylife.house.customer.event.MessageEvent.HOUSES_INDEXT_COLLECTION;
import static com.easylife.house.customer.event.MessageEvent.LOGIN_STATE_CHANGE;
import static com.easylife.house.customer.event.MessageEvent.RESTART_HOUSE_COLLECT_STATE;
import static com.easylife.house.customer.event.MessageEvent.UPDATE_HOUSE_DETAIL_COMMENT;

@RuntimePermissions
public class HouseDetailActivity extends MVPBaseActivity<HousesDetailContract.View, HouseDetailPresenter> implements
        HousesDetailContract.View, AMap.OnMapLoadedListener, LocationSource, AMapLocationListener {
    @Bind(R.id.tv_houses_name)
    TextView tvHousesName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_start)
    TextView tvStart;
    @Bind(R.id.floviewgroup)
    FlowViewGroup floviewgroup;
    @Bind(R.id.tv_address_value)
    TextView tvAddressValue;
    @Bind(R.id.tv_open_time_value)
    TextView tvOpenTimeValue;
    @Bind(R.id.tv_complete_time_value)
    TextView tvCompleteTimeValue;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.fl_caculator)
    FrameLayout flCaculator;
    @Bind(R.id.rl_main)
    RelativeLayout rlMain;
    @Bind(R.id.recycleview_type)
    RecyclerView recycleviewType;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_dynamic_value)
    TextView tvDynamicValue;
    @Bind(R.id.rl_dynamic)
    RelativeLayout rlDynamic;
    @Bind(R.id.tv_dynamic_title)
    TextView tvDynamicTitle;
    @Bind(R.id.tv_dynamic_content)
    TextView tvDynamicContent;
    @Bind(R.id.tv_dynamic_time)
    TextView tvDynamicTime;
    @Bind(R.id.tv_houses_comment_value)
    TextView tvHousesCommentValue;
    @Bind(R.id.rl_houses_comment)
    RelativeLayout rlHousesComment;
    @Bind(R.id.houses_star)
    RatingBar housesStar;
    @Bind(R.id.tv_start_grade)
    TextView tvStartGrade;
    @Bind(R.id.comment_flowgroup)
    FlowViewGroup commentFlowgroup;
    @Bind(R.id.comment_recycle)
    RecyclerView commentRecycle;
    @Bind(R.id.tv_comment_btn)
    TextView tvCommentBtn;
    //    @Bind(R.id.iv_building)
//    ImageView ivBuilding;
    @Bind(R.id.rl_building)
    RelativeLayout rlBuilding;
    @Bind(R.id.team_recycle)
    RecyclerView teamRecycle;
    @Bind(R.id.rl_houses_team)
    RelativeLayout rlHousesTeam;
    //    @Bind(R.id.iv_like1)
//    ImageView ivLike1;
//    @Bind(R.id.tv_like1)
//    TextView tvLike1;
//    @Bind(R.id.tvName1)
//    TextView tvName1;
//    @Bind(R.id.tvName2)
//    TextView tvName2;
//    @Bind(R.id.ll_like1)
//    RelativeLayout llLike1;
//    @Bind(R.id.iv_like2)
//    ImageView ivLike2;
    @Bind(R.id.iv_address)
    ImageView ivAddress;
    //    @Bind(R.id.tv_like2)
//    TextView tvLike2;
//    @Bind(R.id.tv_like_area1)
//    TextView tvLikeArear1;
//    @Bind(R.id.tv_like_room1)
//    TextView tvLikeRoom1;
//    @Bind(R.id.tv_like_area2)
//    TextView tvLikeArear2;
//    @Bind(R.id.tv_like_room2)
//    TextView tvLikeRoom2;
//    @Bind(R.id.ll_like2)
//    RelativeLayout llLike2;
    @Bind(R.id.rl_houses_like)
    RelativeLayout rlHousesLike;
    //    @Bind(R.id.ll_like)
//    LinearLayout llLike;
    @Bind(R.id.ll_dymanic)
    LinearLayout llDynamic;
    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.rl_detail_more)
    RelativeLayout rlDetailMore;
    @Bind(R.id.tv_complete_time)
    TextView tvCompleteTime;
    @Bind(R.id.btnNavigation)
    TextView btnNavigation;
    @Bind(R.id.iv_map)
    View iv_map;
    @Bind(R.id.recycleview_saling)
    RecyclerView recycleSaling;
    @Bind(R.id.tv_saling_more)
    TextView tvSalingMore;
    @Bind(R.id.btnSelectBuy)
    TextView btnSelectBuy;
    @Bind(R.id.tvDiscount)
    TextView tvDiscount;
    @Bind(R.id.layDiscount)
    LinearLayout layDiscount;
    @Bind(R.id.recycleFavorable)
    RecyclerView recycleFavorable;
    @Bind(R.id.layFavorable)
    LinearLayout layFavorable;
    @Bind(R.id.llClub)
    LinearLayout llClub;
    @Bind(R.id.tv_houses_comment)
    TextView tvHousesComment;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    //        @Bind(R.id.view)
//    View view;
    @Bind(R.id.wv_building)
    WebView wvBuilding;
    @Bind(R.id.tv_open_time)
    TextView tvOpenTime;
    @Bind(R.id.rcvYouHuiXinXi)
    RecyclerView rcvYouHuiXinXi;
    @Bind(R.id.tvYouHuiTitle)
    TextView tvYouHuiTitle;
    @Bind(R.id.tvYouHuiDesc)
    TextView tvYouHuiDesc;
    @Bind(R.id.tvGetNow)
    TextView tvGetNow;
    @Bind(R.id.tvBannerCount)
    TextView tvBannerCount;
    //    @Bind(R.id.tv_get_discount)
//    TextView tv_get_discount;
    @Bind(R.id.relYouHuiXinxiSinge)
    RelativeLayout relYouHuiXinxiSinge;
    @Bind(R.id.rcvLikeHouse22)
    RecyclerView rcvLikeHouse;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tvHousetTile)
    TextView tvHousetTile;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.ivRecommend)
    ImageView ivRecommend;
    @Bind(R.id.tvRecommend)
    TextView tvRecommend;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.llHouseTitle)
    LinearLayout llHouseTitle;
    @Bind(R.id.llHouseContent)
    LinearLayout llHouseContent;
    @Bind(R.id.llHouseTypeContent)
    LinearLayout llHouseTypeContent;
    @Bind(R.id.llFeedbackContent)
    LinearLayout llFeedbackContent;
    @Bind(R.id.llAroundContent)
    LinearLayout llAroundContent;
    @Bind(R.id.scrollView)
    ObservableScrollView scrollView;

    @Bind(R.id.relBannerContent)
    RelativeLayout relBannerContent;
    @Bind(R.id.bottom)
    LinearLayout bottom;

    @Bind(R.id.bottomSimple)
    LinearLayout bottomSimple;


    @Bind(R.id.rl_collect)
    LinearLayout rlCollect;
    @Bind(R.id.iv_collect)
    CheckBox ivCollect;

    @Bind(R.id.rl_collect2)
    LinearLayout rlCollect2;
    @Bind(R.id.iv_collect2)
    CheckBox ivCollect2;

    //    @Bind(R.id.rl_ask)
//    LinearLayout rlAsk;
    //    @Bind(R.id.rl_sub)
//    LinearLayout rlSub;
//
//
//    @Bind(R.id.rl_sub2)
//    LinearLayout rlSub2;
    @Bind(R.id.ll_look)
    LinearLayout llLook;
    @Bind(R.id.tv_call)
    TextView tvCall;

    @Bind(R.id.tv_call2)
    TextView tvCall2;

    @Bind(R.id.tvEmptyComment)
    TextView tvEmptyComment;

    @Bind(R.id.tvEmptyDynamic)
    TextView tvEmptyDynamic;

    @Bind(R.id.llCommentStart)
    LinearLayout llCommentStart;

//
//    @Bind(R.id.vCommentCutLine)
//    View vCommentCutLine;


    @Bind(R.id.tvChangePriceNotice)
    TextView tvChangePriceNotice;
    @Bind(R.id.tvOpenSaleNotice)
    TextView tvOpenSaleNotice;
    @Bind(R.id.btnSubscribeNews)
    TextView btnSubscribeNews;
    private List<String> images = new ArrayList<>();
    private List<HousesTopImgBean> topImgBeanList = new ArrayList<>();
    private List<String> alredyList;
    private HousesDetailBaseBean baseBean;
    private HousesDynamicBean housesDynamicBean;
    private DymanicBeanList dymanicBeanList;
    private CommentListBean commentBean;
    private String devId, devName;
    private HousesDetailMainAdapter housesTypeAdapter;
    private List<HousesTypeBean.HouseLayoutDataBean> allList;
    private FavorableShowAdapter adapter;
    private List<View> listViews = null;
    private List<FavorableVip> favorableVips;
    private HouseLikeAdapter houseLikeAdapter;
    private DiscountAdapter discountAdapter;
    //锚点容器
    private List<View> mAimingPointList;
    /**
     * 优惠信息列表数据
     */
    private List<DiscountListBean> disCountListBean;
    //TabLayout显示的文字
    private String[] pointText = new String[]{"楼盘", "户型", "评价", "周边"};
    private boolean isCollect;
    private String dev_id;
    private int coop;
    private SearchSingleton searchSingleton;
    private List<String> typeCollectList;
    /**
     * 楼盘动态提醒dialog
     */
    private HouseNoticeDialog houseNewsNoticeDialog;
    /**
     * 楼盘动态提醒dialog  无验证手机号
     */
    private HouseNoticeDialog2 houseNewsNoticeDialog2;
    /**
     * 楼盘订阅人数
     */
    private HouseNoticeDialog housePriceChangeDialog;
    private HouseNoticeDialog2 housePriceChangeDialog2;
    private HouseNoticeDialog houseOpenSaleDialog;
    private HouseNoticeDialog2 houseOpenSaleDialog2;
    private HouseSubNumBean subNum;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_house_detail, null);
    }

    /**
     * 给TabLayout添加条目
     */
    public void addAimingPoint(TabLayout tabLayout, String text) {
        tabLayout.addTab(tabLayout.newTab().setText(text));
    }

    public static void startActivity(Activity activity, String devID, boolean isCollect, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HouseDetailActivity.class)
                        .putExtra("DEV_ID", devID).putExtra("isCollect", isCollect)
                , requestCode);
    }

    public static void startActivity(Activity activity, String devID, int coop, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HouseDetailActivity.class)
                        .putExtra("DEV_ID", devID).putExtra("coop", coop)
                , requestCode);
    }

    @Override
    protected void initView() {

        StatusBarUtils.setDarkMode(this);
        StatusBarUtils.setTranslucentForImageView2(activity, Color.argb(0, 255, 255, 255), llHouseTitle);
        EventBus.getDefault().register(this);//注册EventBus
        devId = getIntent().getStringExtra("DEV_ID");

        isCollect = getIntent().getBooleanExtra("isCollect", false);
        dev_id = getIntent().getStringExtra("DEV_ID");
        coop = getIntent().getIntExtra("coop", 0);
        searchSingleton = SearchSingleton.getIstance();

        updateCoopStyle();

        /**初始化tabLayout*/
        for (int i = 0; i < pointText.length; i++) {
            addAimingPoint(tabLayout, pointText[i]);
        }


        /**添加锚点到锚点列表*/
        mAimingPointList = new ArrayList<>();
        mAimingPointList.add(llHouseContent);
        mAimingPointList.add(llHouseTypeContent);
        mAimingPointList.add(llFeedbackContent);
        mAimingPointList.add(llAroundContent);
        final int statusBarHeightPx = StatusBarUtils.getStatusBarHeight(this);
        final int statusBarHeight = SizeUtils.px2dp(statusBarHeightPx);
//        Log.d("@@@",statusBarHeightPx+"");
        Anchor anchor = new Anchor(tabLayout, scrollView, mAimingPointList, this, 0, 88 + statusBarHeight);
        anchor.setOnScrollListener(new Anchor.OnScrollListener() {
            @Override
            public void onScroll(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                int scrollY = scrollView.getScrollY();
                int bannerContentHeight = relBannerContent.getHeight();
                int llHouseTitleHeight = llHouseTitle.getHeight();
//                Log.d("@@@","scrollY:"+ scrollY);
//                Log.d("@@@","bannerHeight:"+ bannerContentHeight);
//                Log.d("@@@","llHouseTitleHeight:"+ llHouseTitleHeight);


                float ratio = Math.min(1, (scrollY) / ((bannerContentHeight - llHouseTitleHeight - statusBarHeightPx) * 1f));
                if (ratio > 0) {
//                    StatusBarUtil.setTranslucent(HouseDetailActivity.this, ((int) (ratio * 0xFF)));

                    StatusBarUtils.setTranslucentForImageView2(activity, Color.argb(((int) (ratio * 0xFF)), 255, 255, 255), llHouseTitle);
                    StatusBarUtils.setLightMode(activity);
                    if (llHouseTitle.getVisibility() == View.GONE) {
                        llHouseTitle.setVisibility(View.VISIBLE);
                    }
                } else {
                    StatusBarUtils.setTranslucentForImageView2(activity, Color.argb(0, 255, 255, 255), llHouseTitle);
                    StatusBarUtils.setDarkMode(activity);
                    if (llHouseTitle.getVisibility() == View.VISIBLE) {
                        llHouseTitle.setVisibility(View.GONE);
                    }
                }

                llHouseTitle.getBackground().mutate().setAlpha((int) (ratio * 0xFF));

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HouseDetailActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleviewType.setLayoutManager(linearLayoutManager);
        housesTypeAdapter = new HousesDetailMainAdapter(R.layout.houses_item_type, allList);
        recycleviewType.setAdapter(housesTypeAdapter);
        recycleviewType.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(20), LinearLayout.HORIZONTAL));
        recycleviewType.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (allList.size() != 0) {
                    HousesTypeBean.HouseLayoutDataBean houseLayoutDataBean = allList.get(position);
                    startActivity(new Intent(HouseDetailActivity.this, HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
                            .putExtra("BASE_BEAN", baseBean));
//                    if (coop == 3) {
//                        startActivity(new Intent(HouseDetailActivity.this, HousesTypeDetailSimpleActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
//                                .putExtra("BASE_BEAN", baseBean));
//
//                    } else {
//                        startActivity(new Intent(HouseDetailActivity.this, HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
//                                .putExtra("BASE_BEAN", baseBean));
//
//                    }

                }
            }
        });

        adapter = new FavorableShowAdapter(R.layout.item_favorable, null);
        recycleFavorable.setLayoutManager(new LinearLayoutManager(HouseDetailActivity.this));
        recycleFavorable.setAdapter(adapter);


        showLoading();
        dialog.setOnKeyListener(keylistener);
//        devId = ((HousesAndTypeActivity) HouseDetailActivity.this).getDev_id();
//        devName = ((HousesAndTypeActivity) HouseDetailActivity.this).getDevName();


        mPresenter.requestHousesImg(devId);//楼盘详情banner图
        mPresenter.requestHousesDetail(devId);//楼盘详情基础信息
        mPresenter.requestMainType(devId);//主力户型
        //devId 和页码
        mPresenter.requestHousesTeam(devId, "1");
        mPresenter.requestHousesClub(devId);
        mPresenter.getDevFavorable(devId);
        mPresenter.getVipFavorable(devId);
        mPresenter.getDiscountList(devId);

        Customer customer = dao.getCustomer();
        if (customer != null) {
            mPresenter.collectHouseList(customer.userCode, customer.token, "0");
            mPresenter.requestIsGetDis(devId, customer.userCode, customer.token);//获取优惠
        }

        initMap();
//
//        ((HousesAndTypeActivity) HouseDetailActivity.this).setNoNetTryRequestData(new HousesAndTypeActivity.NoNetTryRequestData() {
//            @Override
//            public void tryRequestData() {
//                showLoading();
//                mPresenter.requestHousesImg(devId);
//                mPresenter.requestHousesDetail(devId);
//                mPresenter.requestMainType(devId);
//                //devId 和页码
//                mPresenter.requestHousesTeam(devId, "1");
//            }
//        });

        for (int i = 0; i < 5; i++) {
            TextView text = (TextView) LayoutInflater.from(HouseDetailActivity.this).inflate(R.layout.houses_item_flow_comment, commentFlowgroup, false);
            switch (i) {
                case 0:
                    text.setText("地段优势(0)");
                    break;
                case 1:
                    text.setText("环境好(0)");
                    break;
                case 2:
                    text.setText("价格优势(0)");
                    break;
                case 3:
                    text.setText("配套完善(0)");
                    break;
                case 4:
                    text.setText("交通便利(0)");
                    break;


            }
            commentFlowgroup.addView(text);
        }

        houseLikeAdapter = new HouseLikeAdapter(R.layout.search_item, null);
        houseLikeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                HousesLikeBean item = houseLikeAdapter.getItem(position);
//                    if (baseBean.coOp == 3) {
//                        startActivity(new Intent(AllHouseActivity.this, HousesAndTypeSimpleActivity.class).putExtra("DEV_ID", baseBean.devId));
//                    } else {
                startActivity(new Intent(HouseDetailActivity.this, HouseDetailActivity.class).putExtra("DEV_ID", item.devId));
//                }

            }
        });
        rcvLikeHouse.setLayoutManager(new LinearLayoutManager(HouseDetailActivity.this) {//解决scrollview 嵌套rcv滑动不流畅问题
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcvLikeHouse.setAdapter(houseLikeAdapter);
        //优惠信息

        discountAdapter = new DiscountAdapter(null);
        rcvYouHuiXinXi.setVisibility(View.VISIBLE);
        LinearLayoutManager youhuiLinearLayoutManager = new LinearLayoutManager(HouseDetailActivity.this);
        youhuiLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvYouHuiXinXi.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(15), LinearLayout.HORIZONTAL));
        rcvYouHuiXinXi.setLayoutManager(youhuiLinearLayoutManager);
        rcvYouHuiXinXi.setAdapter(discountAdapter);
        discountAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DiscountListBean item = discountAdapter.getItem(position);
                if (dao.isLogin()) {
                    if (item.getDisType().equals("member")) {
                        FavorableSelectActivity.startActivity(HouseDetailActivity.this, baseBean, item.getId(), 11);
                    } else {
                        String type = item.getDisType();
                        if (item.getDisType().equals("favourable")) {
                            type = "favourable" + item.getSeqence();
                        }
                        mPresenter.getDiscountCount(devId, type, position);
                    }
                } else {
                    LoginByVerifyCodeActivity.startActivity(HouseDetailActivity.this, 12);

                }


            }
        });


        setOnShareListener(new OnShareListener() {
            @Override
            public void onShare() {
                mPresenter.shareIntegration();
            }
        });
    }

    public void updateCoopStyle() {
        if (coop == 3) {
            bottomSimple.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
        } else {
            bottomSimple.setVisibility(View.GONE);
            bottom.setVisibility(View.VISIBLE);
        }
    }

    private AMap aMap;

    private void initMap() {
        if (aMap == null) {
            aMap = ((TextureSupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapView)).getMap();

            UiSettings mUiSettings = aMap.getUiSettings();
            mUiSettings.setScaleControlsEnabled(false);
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setAllGesturesEnabled(false);
            aMap.setOnMapLoadedListener(this);
            aMap.setMyLocationStyle(new MyLocationStyle().myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
            aMap.setLocationSource(this);
            aMap.getUiSettings().setMyLocationButtonEnabled(false);
            aMap.setMyLocationEnabled(true);
        }
    }


    @Override
    protected void setActionBarDetail() {
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void tryRequestData() {

    }

    private int pagetImgSize;//一共有多少张图片
    private List<HousesTopImgBean.ImgBean> imgBeanList = new ArrayList<>();//所有图片集合的bean
    //    private Map<Integer, Integer> currentMap = new HashMap<>();//当前点中的位置
    private Map<Integer, Integer> currentMap = new TreeMap<>();
    //大图浏览需要的标题list
    private List<PageTitleBean> titleList = new ArrayList<>();//所有相册的标题

    @Override
    public void showHousesTopImg(List<HousesTopImgBean> topImgBeanList) {
        this.topImgBeanList = topImgBeanList;

        for (int i = 0; i < topImgBeanList.size(); i++) {
            HousesTopImgBean topBean = topImgBeanList.get(i);
            //如果相册没有图片，则不添加相册标题
            if (topBean.img.size() != 0) {
                PageTitleBean bean = new PageTitleBean();
                bean.title = topBean.name;
                bean.num = topBean.img.size();
                titleList.add(bean);
            }

            if (i == 0) {
                currentMap.put(i, 0);
            } else {
                currentMap.put(i, pagetImgSize);
            }

            pagetImgSize += topImgBeanList.get(i).img.size();


            for (int j = 0; j < topBean.img.size(); j++) {
                HousesTopImgBean.ImgBean imgbean = topBean.img.get(j);
                imgbean.name = topBean.name;
                //相册的位置
                imgbean.position = i;
                //当前图片所在总相册浏览中的位置
                imgbean.currentPosition = pagetImgSize;
                //当前图片在当前相册中的位置
                imgbean.current = j + 1;
                imgBeanList.add(imgbean);

            }
        }

        setBanner();
        cancelLoading();
    }

    private void setBanner() {
        for (Map.Entry<Integer, Integer> entry : currentMap.entrySet()) {
            if (topImgBeanList.get(entry.getKey()).img.get(0).url != null && !TextUtils.isEmpty(topImgBeanList.get(entry.getKey()).img.get(0).url.trim())) {
                images.add(topImgBeanList.get(entry.getKey()).img.get(0).url.trim());
            } else if (topImgBeanList.get(entry.getKey()).img.get(0).vrUrl != null && !TextUtils.isEmpty(topImgBeanList.get(entry.getKey()).img.get(0).vrUrl.trim())) {
                images.add(topImgBeanList.get(entry.getKey()).img.get(0).imgUrl.trim());
            } else if (topImgBeanList.get(entry.getKey()).img.get(0).videoUrl != null && !TextUtils.isEmpty(topImgBeanList.get(entry.getKey()).img.get(0).videoUrl.trim())) {
                images.add(topImgBeanList.get(entry.getKey()).img.get(0).imgUrl.trim());
            }
        }


        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, images);
//                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //跳转大图浏览页面
                if (topImgBeanList.size() == 0) {
                    return;
                }

                HousesToPagerImgBean pagerImgBean = new HousesToPagerImgBean();
                pagerImgBean.imgBeanList = imgBeanList;
                pagerImgBean.titleList = titleList;
                pagerImgBean.pagetImgSize = pagetImgSize;
                pagerImgBean.currentPosition = currentMap.get(position);
                pagerImgBean.currentMap = currentMap;
                pagerImgBean.allBeanList = topImgBeanList;
                startActivity(new Intent(HouseDetailActivity.this, PagerActivity.class).putExtra("PAGER_BEAN", pagerImgBean));
            }
        });
        tvBannerCount.setText("1/" + images.size());
        tvBannerCount.setVisibility(images.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvBannerCount.setText((position + 1) + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        convenientBanner.setCanLoop(false);
    }

    private ResultCustomerIsOrder resultCustomerIsOrder;

    @Override
    public void checkVipFavorableStatus(ResultCustomerIsOrder resultCustomerIsOrder) {
// TODO: 2019/6/5 记得删掉

//        HousePinControlActivity.startActivity(this, devId, 0);


        this.resultCustomerIsOrder = resultCustomerIsOrder;
        if (resultCustomerIsOrder != null) {
            if (ResultCustomerIsOrder.STATUS_COMPLETE.equals(resultCustomerIsOrder.status)) {
                // 纯企业付费  或者  会员付费已支付成功
                HousePinControlActivity.startActivity(this, devId, 0);
            } else if (ResultCustomerIsOrder.STATUS_PAY_PART.equals(resultCustomerIsOrder.status)) {
                //  提示去订单详情页继续支付
                showTipPayContinue();
            } else if (ResultCustomerIsOrder.STATUS_NOT.equals(resultCustomerIsOrder.status)) {
                // 提示购买
                showTipBugFavorable();
            }
        } else {
            HousePinControlActivity.startActivity(this, devId, 0);
        }
    }

    /**
     * 楼盘基础信息
     *
     * @param baseBean
     */
    @Override
    public void showHousesDetail(HousesDetailBaseBean baseBean) {
        this.baseBean = baseBean;


        tvHousetTile.setText(baseBean.devName);

        mPresenter.getHousesSubNum(devId, baseBean.estateProjectId);


        if (dao.isLogin()) {
            LoginResult loginCache = dao.getLoginCache();
            mPresenter.getSubList(loginCache.userCode, loginCache.token, baseBean.estateProjectId, baseBean.devId);
        }


        // 1.  判断是否明房源 --- 是否显示按钮（初始显示状态为灰色按钮）
        if ("1".equals(baseBean.isTransparent)) {
//            llClub.setVisibility(View.VISIBLE);
            btnSelectBuy.setVisibility(View.VISIBLE);
        } else {
            btnSelectBuy.setVisibility(View.GONE);
        }

        if (baseBean.coOp == 3) {
            rlBuilding.setVisibility(View.GONE);
            btnSelectBuy.setVisibility(View.GONE);
        } else {
            if (baseBean.distribution != null && baseBean.distribution.size() > 0) {
                rlBuilding.setVisibility(View.VISIBLE);
            } else {
                rlBuilding.setVisibility(View.GONE);
            }
        }


//        HousesAndTypeActivity activity = (HousesAndTypeActivity) HouseDetailActivity.this;
//        activity.coop = baseBean.coOp;
//        activity.updateCoopStyle();
        if (baseBean.coOp == 3) {
            layFavorable.setVisibility(View.GONE);
        } else {
//            layFavorable.setVisibility(View.VISIBLE);
        }
        updateCoopStyle();


        //相似推荐
        if (baseBean != null) {
            mPresenter.requestHousesLike(baseBean.devId);
        } else {
            if (!TextUtils.isEmpty(devId)) {
                mPresenter.requestHousesLike(devId);
            }
        }

        //最新动态 和评价列表
        mPresenter.requestHousesDynamic(baseBean.estateProjectId, "1");
        mPresenter.requestHousesDynamicCount(baseBean.estateProjectId);
        mPresenter.requestHousesComment(baseBean.estateProjectId + "", "1");

//        ((HousesAndTypeActivity) HouseDetailActivity.this).setBaseBean(baseBean);
        tvHousesName.setText(baseBean.devName);

        if (!TextUtils.isEmpty(baseBean.averPrice) && !"0".equals(baseBean.averPrice)) {
            tvPrice.setText(baseBean.averPrice + "元/m²");
            tvStart.setVisibility(View.VISIBLE);
        } else {
            tvPrice.setText("价格待定");
            tvStart.setVisibility(View.GONE);
        }
        if (baseBean.feature != null && !TextUtils.isEmpty(baseBean.feature)) {
            String[] features = baseBean.feature.split(",");

            int length = features.length;
            if (length > 3) {
                length = 3;
            }
            for (int i = 0; i < length; i++) {
                TextView tvFeature = (TextView) LayoutInflater.from(HouseDetailActivity.this).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
                tvFeature.setText(features[i]);
                floviewgroup.addView(tvFeature);
            }
        } else {
            TextView tvFeature = (TextView) LayoutInflater.from(HouseDetailActivity.this).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
            tvFeature.setText("暂无数据");
            floviewgroup.addView(tvFeature);
        }
        tvAddressValue.setText(baseBean.addressDistrict + baseBean.addressTown + baseBean.addressDetail);
        tvOpenTimeValue.setText(baseBean.openingRange + " " + CustomerUtils.dateTransSdf(baseBean.openTime));
        tvCompleteTimeValue.setText(baseBean.liveRange + " " + CustomerUtils.dateTransSdf(baseBean.liveTime));

        switch (baseBean.saleStatus) {
            case "0":
                tvStatus.setText("在售");
                break;
            case "1":
                tvStatus.setText("待售");
                break;
            case "2":
                tvStatus.setText("不可售");
                break;
            case "3":
                tvStatus.setText("已售完");
                break;
        }

        if (baseBean.projectCoordinate != null && baseBean.projectCoordinate.size() != 0) {
            latLon = new LatLng(baseBean.projectCoordinate.get(1), baseBean.projectCoordinate.get(0));
            MapUtil.moveToCurrentLocation(aMap, latLon.latitude, latLon.longitude);
            addMarker(latLon, baseBean.devName, baseBean.averPrice);
        }
        if (baseBean.distribution != null && baseBean.distribution.size() != 0) {
//            Glide.with(HouseDetailActivity.this).load(baseBean.distribution.get(0).url).into(ivBuilding);
        }
        // 设置WebView属性，能够执行Javascript脚本
        wvBuilding.getSettings().setJavaScriptEnabled(true);
        wvBuilding.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        wvBuilding.loadUrl(HOUSE_NUMBER_URL + devId);
        dao.pointVisit(devId, devName);
    }


    @Override
    public void showFavorableDev(DevFavorable devFavorable) {
        //  置业优惠数据（第一块）
        if (devFavorable == null)
            return;

        if (baseBean.coOp == 3) {
            return;
        }


        if (devFavorable.discount != null && ("1".equals(devFavorable.discount.discountAllType) || "1".equals(devFavorable.discount.discountDaiType))) {
            layFavorable.setVisibility(View.VISIBLE);
            // 全款贷款
//            layDiscount.setVisibility(View.VISIBLE);
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
        } else {
            layDiscount.setVisibility(View.GONE);
        }
        if (devFavorable.favourable != null && devFavorable.favourable.size() != 0) {
            layFavorable.setVisibility(View.VISIBLE);
            // 楼盘优惠列表
            adapter.setNewData(devFavorable.favourable);
        }
    }

    @Override
    public void showFavorableVip(final List<FavorableVip> favorableVips) {
        //  置业优惠 分页展示（的二块）
        this.favorableVips = favorableVips;
//        if (favorableVips != null && favorableVips.size() > 0) {
//            llClub.setVisibility(View.VISIBLE);
//            viewPager.setVisibility(View.VISIBLE);
//
//            listViews = new ArrayList<>();
//            for (int i = 0; i < favorableVips.size(); i++) {
//                View view = LayoutInflater.from(HouseDetailActivity.this).inflate(
//                        R.layout.houseviepager_item, null);
//                LinearLayout llPager = (LinearLayout) view.findViewById(R.id.ll_pager);
//                TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
//                TextView tvPriceCh = (TextView) view.findViewById(R.id.tvprice_ch);
//                TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
//                tvPrice.setText(PriceUtil.formatNum(favorableVips.get(i).num + "", false) + "元");
//                tvPriceCh.setText(favorableVips.get(i).privilege);
//                tvContent.setText("适用于：" + favorableVips.get(i).scope);
//                listViews.add(view);
//                final int finalI = i;
//                llPager.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //                //   获取优惠
//                        if (dao.isLogin()) {
//                            try {
//                                // 在楼盘基本数据中添加展示图，无数据时才添加
//                                if (baseBean.distribution == null || baseBean.distribution.size() == 0) {
//                                    List<HousesDetailBaseBean.DistributionBean> covers = new ArrayList<>();
//                                    covers.add(new HousesDetailBaseBean.DistributionBean(topImgBeanList.get(0).img.get(0).url));
//                                    baseBean.distribution = covers;
//                                }
//                            } catch (Exception e) {
//                                LogOut.d("distribution:", e.toString());
//                            }
//                            FavorableSelectActivity.startActivity(HouseDetailActivity.this, baseBean, favorableVips.get(finalI).id, 11);
//                        } else {
//                            LoginByVerifyCodeActivity.startActivity(HouseDetailActivity.this, 12);
//                        }
//                    }
//                });
//            }
//            ClubAdapter clubAdapter = new ClubAdapter(listViews);
//            viewPager.setAdapter(clubAdapter);
//        }

    }

    /**
     * 会员专享
     */
    @Override
    public void showHousesClub(GitDisCountBean gitDisCountBean) {
    }

    private boolean isAlreadyDis = false;


    @Override
    public void showRequestGetDis(List<DisCountIsAlreadyBean> alreadyBeanList) {
        if (alreadyBeanList != null) {
            for (int i = 0; i < alreadyBeanList.size(); i++) {

                DisCountIsAlreadyBean disCountIsAlreadyBean = alreadyBeanList.get(i);
                if (disCountIsAlreadyBean.devId.equals(devId)) {
                    isAlreadyDis = true;
                } else {
                    isAlreadyDis = false;
                }

            }
        }
    }

    /**
     * 最新动态
     *
     * @param dynamicList
     */
    @Override
    public void showHousesDynamic(List<HousesDynamicBean> dynamicList) {
        if (dynamicList != null && dynamicList.size() > 0) {
            tvEmptyDynamic.setVisibility(View.GONE);
            dymanicBeanList = new DymanicBeanList();
            dymanicBeanList.beanList = dynamicList;
            if (dynamicList != null && dynamicList.size() != 0) {
                llDynamic.setVisibility(View.VISIBLE);
                housesDynamicBean = dynamicList.get(0);
                tvDynamicTitle.setText(housesDynamicBean.title);
                tvDynamicContent.setText(housesDynamicBean.content);
                tvDynamicTime.setText(CustomerUtils.dateTransSdfMinutes(housesDynamicBean.createTime));
            } else {
                llDynamic.setVisibility(View.GONE);
            }
        } else {
            llDynamic.setVisibility(View.GONE);
            tvEmptyDynamic.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 楼盘评价列表
     **/
    @Override
    public void showHousesComment(final CommentListBean commentBean) {
        if (commentBean == null || commentBean.reviews == null || commentBean.reviews.size() == 0) {
            tvEmptyComment.setVisibility(View.VISIBLE);
            llCommentStart.setVisibility(View.GONE);
//            vCommentCutLine.setVisibility(View.GONE);
        } else {
            tvEmptyComment.setVisibility(View.GONE);
            llCommentStart.setVisibility(View.VISIBLE);
//            vCommentCutLine.setVisibility(View.VISIBLE);
        }
        this.commentBean = commentBean;
        if (!TextUtils.isEmpty(commentBean.AVGScore)) {
            housesStar.setRating(Float.parseFloat(commentBean.AVGScore));
        }
        tvStartGrade.setText(commentBean.AVGScore + "分");
        CommentListBean.Strscores strscores = commentBean.strscores.get(0);
        /*
        *
        *    case 0:
                    text.setText("地段优势(0)");
                    break;
                case 1:
                    text.setText("环境好(0)");
                    break;
                case 2:
                    text.setText("价格优势(0)");
                    break;
                case 3:
                    text.setText("配套完善(0)");
                    break;
                case 4:
                    text.setText("交通便利(0)");
                    break;
        * */
        for (int i = 0; i < commentFlowgroup.getChildCount(); i++) {

            switch (i) {
                case 1:
                    ((TextView) commentFlowgroup.getChildAt(1)).setText("环境好(" + strscores.scoreEnvi + ")");
                    break;
                case 3:
                    ((TextView) commentFlowgroup.getChildAt(3)).setText("配套完善(" + strscores.scoreMating + ")");
                    break;
                case 4:
                    ((TextView) commentFlowgroup.getChildAt(4)).setText("交通便利(" + strscores.scoreTraffic + ")");
                    break;
                case 2:
                    ((TextView) commentFlowgroup.getChildAt(2)).setText("价格优势(" + strscores.scorePrice + ")");
                    break;
                case 0:
                    ((TextView) commentFlowgroup.getChildAt(0)).setText("地段优势(" + strscores.scoreDistrict + ")");
                    break;
            }
        }

        List<CommentListBean.ReviewsBean> list = new ArrayList<>();
        if (!TextUtils.isEmpty(commentBean.count)) {
            tvHousesCommentValue.setVisibility(View.VISIBLE);
            tvHousesCommentValue.setText("(" + commentBean.count + ")");
        } else {
            tvHousesCommentValue.setVisibility(View.GONE);
        }

        if (commentBean.reviews.size() == 0) {
            commentRecycle.setVisibility(View.GONE);
            tvEmptyComment.setVisibility(View.VISIBLE);
            llCommentStart.setVisibility(View.GONE);
        } else if (commentBean.reviews.size() == 1) {
            list.add(commentBean.reviews.get(0));
            commentRecycle.setVisibility(View.VISIBLE);
            tvEmptyComment.setVisibility(View.GONE);
            llCommentStart.setVisibility(View.VISIBLE);
        } else {
            list.add(commentBean.reviews.get(0));
            list.add(commentBean.reviews.get(1));
            commentRecycle.setVisibility(View.VISIBLE);
            tvEmptyComment.setVisibility(View.GONE);
            llCommentStart.setVisibility(View.VISIBLE);
        }
        CommentListAdapter commentListAdapter = new CommentListAdapter(R.layout.houses_commen_detail_item, list);
        commentListAdapter.setCommentImgOnclickListenear(new CommentListAdapter.CommentImgOnclickListenear() {
            @Override
            public void setCommentOnclick(List<CommentListBean.ReviewsBean.ReviewimgBean> reviewimg, int position) {
                CommentUrlBean urlBean = new CommentUrlBean();
                urlBean.beanList = reviewimg;
                urlBean.position = position;

                startActivity(new Intent(HouseDetailActivity.this, CommentPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
                Log.e("postion", position + "");
            }

            //跳转楼盘评价列表
            @Override
            public void setJumpCommentListClick() {
                if (baseBean == null)
                    return;
                startActivity(new Intent(HouseDetailActivity.this, HousesCommentActivity.class).putExtra("PROJECTID", baseBean.estateProjectId + ""));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(HouseDetailActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setSmoothScrollbarEnabled(true);
        commentRecycle.setLayoutManager(layoutManager);
        commentRecycle.setAdapter(commentListAdapter);
    }

    public void getLookHouseList() {
        if (dao.getLoginCache() != null) {
            mPresenter.getLookHouseList(dao.getLoginCache().userCode, dao.getLoginCache().token);
        }
    }

    /**
     * 已经预约的楼盘
     *
     * @param alredyList
     */
    @Override
    public void showAlreadyHouse(List<String> alredyList) {
        this.alredyList = alredyList;
    }

    /**
     * 主力户型数据显示
     */
    @Override
    public void showMainType(List<HousesTypeBean> beanList) {
        rlMain.setVisibility(View.VISIBLE);
        allList = new ArrayList<>();
        for (int i = 0; i < beanList.size(); i++) {
            HousesTypeBean housesTypeBean = beanList.get(i);
            allList.addAll(housesTypeBean.houseLayoutData);
        }
        housesTypeAdapter.setNewData(allList);
        if (allList.size() == 0) {
            rlMain.setVisibility(View.GONE);
        }
    }

    /**
     * 专家团队
     *
     * @param expertTeamList
     */
    private List<ExportListBean> expertTeamList;

    @Override
    public void showHousesTeam(List<ExportListBean> expertTeamList) {
        if (expertTeamList == null || expertTeamList.size() == 0) {
            rlHousesTeam.setVisibility(View.GONE);
            return;
        } else {
            rlHousesTeam.setVisibility(View.VISIBLE);
        }
        this.expertTeamList = expertTeamList;
        List<ExportListBean> list = new ArrayList<>();
        if (expertTeamList.size() == 0) {
            teamRecycle.setVisibility(View.GONE);
        } else if (expertTeamList.size() == 1) {
            list.add(expertTeamList.get(0));
            teamRecycle.setVisibility(View.VISIBLE);
        } else {
            list.add(expertTeamList.get(0));
            list.add(expertTeamList.get(1));
            teamRecycle.setVisibility(View.VISIBLE);
        }
        ExpertTeamAdapter expertTeamAdapter = new ExpertTeamAdapter(R.layout.expert_team, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HouseDetailActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        teamRecycle.setLayoutManager(layoutManager);
        teamRecycle.setAdapter(expertTeamAdapter);

        expertTeamAdapter.setOnPhoneClickLisenear(new ExpertTeamAdapter.onPhoneClickLisenear() {
            @Override
            public void phoneClick(String phone) {
                call(phone);
            }

            @Override
            public void exportPage(String brokeCode, String headUrl, String phone) {
//                CustomerUtils.showTip(HouseDetailActivity.this, "专家店铺");
                startActivity(new Intent(HouseDetailActivity.this, ExportShopActivity.class).putExtra("BROKECODE", brokeCode).putExtra("PHONE", phone));
            }

            @Override
            public void imChat(String imUsername, String brokeCode, String brokerName, String phone) {
                //进入IM聊天

                if (!TextUtils.isEmpty(imUsername)) {

                    HouseSaleListBean houseSaleListBean = null;
                    if (baseBean != null) {
                        houseSaleListBean = new HouseSaleListBean();
                        houseSaleListBean.name = baseBean.devName;
                        houseSaleListBean.propertyType = baseBean.propertyType;
                        houseSaleListBean.address = baseBean.addressDistrict + baseBean.addressTown;
                        houseSaleListBean.averPrice = baseBean.averPrice;
                        if (baseBean.distribution != null && baseBean.distribution.size() != 0) {
                            houseSaleListBean.imgUrl = baseBean.distribution.get(0).thumbnailImage;
                        }
                        houseSaleListBean.devId = baseBean.devId;
                        houseSaleListBean.share = baseBean.share;
                    }
                    if (dao.isLogin()) {
                     /*   startActivity(new Intent(HouseDetailActivity.this, ChatActivity.class).putExtra("userId", imUsername)
                                .putExtra("IM_BUILD", houseSaleListBean)
                                .putExtra("IM_TYPE", 1));
                        dao.pointIM(baseBean.devId, baseBean.devName, brokeCode, brokerName); // 通知后台HeadLineBean.ListBean处理
                        mPresenter.chatPush(baseBean.devId, brokeCode);*/
                    } else {
                        LoginByVerifyCodeActivity.startActivity(HouseDetailActivity.this, 15);
                    }

                }
            }
        });
    }


    /**
     * 相似楼盘
     *
     * @param likeBean
     */
    @Override
    public void showHousesLike(List<HousesLikeBean> likeBean) {
        if (likeBean != null && likeBean.size() > 0) {
            rlHousesLike.setVisibility(View.VISIBLE);
            houseLikeAdapter.setNewData(likeBean);
        } else {
            rlHousesLike.setVisibility(View.GONE);
        }
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
        ToastUtils.showShort(msg);
    }

    @Override
    public void showMainFail() {
        rlMain.setVisibility(View.GONE);
    }

    @Override
    public void showDymanicCount(String count) {
        tvDynamicValue.setText("(" + count + ")");
    }

    @Override
    public void showDisCountList(List<DiscountListBean> list) {
        disCountListBean = list;

        if (baseBean.coOp == 3) {
            layFavorable.setVisibility(View.GONE);
            return;
        } else {
            layFavorable.setVisibility(View.VISIBLE);
        }

        if (list != null && list.size() > 0) {
            layFavorable.setVisibility(View.VISIBLE);

            if (list.size() == 1) {
                rcvYouHuiXinXi.setVisibility(View.GONE);
                relYouHuiXinxiSinge.setVisibility(View.VISIBLE);
                tvYouHuiTitle.setText(list.get(0).getPrivilege());
                tvYouHuiDesc.setText("适用于:" + list.get(0).getScope() + " (" + list.get(0).getCount() + "人已领取)");
            } else {
                rcvYouHuiXinXi.setVisibility(View.VISIBLE);
                relYouHuiXinxiSinge.setVisibility(View.GONE);
                discountAdapter.setNewData(list);
            }

        } else {
            layFavorable.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateDisCount(String msg, int pos) {
        ToastUtils.showShort("领取成功");
        if (disCountListBean.size() == 1) {
            DiscountListBean item = disCountListBean.get(0);
//            item.setCount(item.getCount() + 1);
//            tvYouHuiDesc.setText("适用于:" + item.getScope() + " (" + item.getCount() + "人已领取)");
            tvYouHuiDesc.setText("适用于:" + item.getScope() + " (" + msg + "人已领取)");
        } else {
            DiscountListBean item = discountAdapter.getItem(pos);
//            item.setCount(item.getCount() + 1);
            if (!TextUtils.isEmpty(msg)) {
                item.setCount(Integer.parseInt(msg));
                discountAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showTipDialog(String tip) {
        DialogUtil.showTip(this, tip);
    }

    @Override
    public void getVerifyCodeSucc() {

    }

    @Override
    public void showSubList(List<String> mySubList) {
        ////0 价格 1 开盘 2 动态  //    <!--btnSubscribeNews tvChangePriceNotice tvOpenSaleNotice -->
        /**/


        Drawable iv_open_sale = getResources().getDrawable(R.mipmap.iv_open_sale);
        iv_open_sale.setBounds(0, 0, iv_open_sale.getMinimumWidth(), iv_open_sale.getMinimumHeight());


        Drawable iv_open_sale_false = getResources().getDrawable(R.mipmap.iv_open_sale_false);
        iv_open_sale_false.setBounds(0, 0, iv_open_sale_false.getMinimumWidth(), iv_open_sale_false.getMinimumHeight());


        Drawable iv_change_price = getResources().getDrawable(R.mipmap.iv_change_price);
        iv_change_price.setBounds(0, 0, iv_change_price.getMinimumWidth(), iv_change_price.getMinimumHeight());


        Drawable iv_change_price_false = getResources().getDrawable(R.mipmap.iv_change_price_false);
        iv_change_price_false.setBounds(0, 0, iv_change_price_false.getMinimumWidth(), iv_change_price_false.getMinimumHeight());


        if (mySubList.contains("0")) {
            tvChangePriceNotice.setText("取消变价提醒");
            tvChangePriceNotice.setCompoundDrawables(iv_change_price_false, null, null, null);
        } else {
            tvChangePriceNotice.setText("变价提醒我");
            tvChangePriceNotice.setCompoundDrawables(iv_change_price, null, null, null);
        }

        if (mySubList.contains("1")) {
            tvOpenSaleNotice.setText("取消开盘提醒");
            tvOpenSaleNotice.setCompoundDrawables(iv_open_sale_false, null, null, null);
        } else {
            tvOpenSaleNotice.setText("开盘提醒我");
            tvOpenSaleNotice.setCompoundDrawables(iv_open_sale, null, null, null);
        }

        if (mySubList.contains("2")) {
            btnSubscribeNews.setText("取消订阅");
        } else {
            btnSubscribeNews.setText("订阅楼盘动态");

        }
    }

    @Override
    public void showHouseSubNum(HouseSubNumBean numBean) {

        this.subNum = numBean;

//        ToastUtils.showShort("subNum:" + numBean.count);


    }

    @Override
    public void showGutSubSucc(String tag, String msg) {
        ToastUtils.showShort(msg);
        mPresenter.getHousesSubNum(baseBean.devId, baseBean.estateProjectId);
        String needSavePhone = null;
        /*  ////0 价格 1 开盘 2 动态  //    <!--btnSubscribeNews tvChangePriceNotice tvOpenSaleNotice -->
        }*/
        mPresenter.getHousesSubNum(baseBean.devId, baseBean.estateProjectId);


        switch (tag) {
            case "2":
                //订阅
                if (TextUtils.isEmpty(dao.getCustomer().phone)) {
                    houseNewsNoticeDialog.dismiss();
                    needSavePhone = houseNewsNoticeDialog.getPhone();
                } else {
                    houseNewsNoticeDialog2.dismiss();
                    needSavePhone = houseNewsNoticeDialog2.getPhone();
                }
                btnSubscribeNews.setText("取消订阅");
                break;


            case "0":
                //变价提醒
                if (TextUtils.isEmpty(dao.getCustomer().phone)) {
                    housePriceChangeDialog.dismiss();
                    needSavePhone = housePriceChangeDialog.getPhone();
                } else {
                    housePriceChangeDialog2.dismiss();
                    needSavePhone = housePriceChangeDialog2.getPhone();
                }
                tvChangePriceNotice.setText("取消变价提醒");


                Drawable iv_change_price_false = getResources().getDrawable(R.mipmap.iv_change_price_false);
                iv_change_price_false.setBounds(0, 0, iv_change_price_false.getMinimumWidth(), iv_change_price_false.getMinimumHeight());
                tvChangePriceNotice.setCompoundDrawables(iv_change_price_false, null, null, null);
                break;


            case "1":
                //开盘提醒


                if (TextUtils.isEmpty(dao.getCustomer().phone)) {
                    houseOpenSaleDialog.dismiss();
                    needSavePhone = houseOpenSaleDialog.getPhone();
                } else {
                    houseOpenSaleDialog2.dismiss();
                    needSavePhone = houseOpenSaleDialog2.getPhone();
                }


                tvOpenSaleNotice.setText("取消开盘提醒");

                Drawable iv_open_sale_false = getResources().getDrawable(R.mipmap.iv_open_sale_false);
                iv_open_sale_false.setBounds(0, 0, iv_open_sale_false.getMinimumWidth(), iv_open_sale_false.getMinimumHeight());
                tvOpenSaleNotice.setCompoundDrawables(iv_open_sale_false, null, null, null);
                break;

        }


        Customer customer = dao.getCustomer();
        customer.phone = needSavePhone;
        dao.saveCustomer(customer);


    }

    @Override
    public void showDelSubSucc(String tag, String msg) {
     /*
         ////0 价格 1 开盘 2 动态  //    <!--btnSubscribeNews tvChangePriceNotice tvOpenSaleNotice -->
     * */

        ToastUtils.showShort(msg);
        mPresenter.getHousesSubNum(baseBean.devId, baseBean.estateProjectId);
        if (tag.equals("0")) {
            tvChangePriceNotice.setText("变价提醒我");
            Drawable iv_change_price = getResources().getDrawable(R.mipmap.iv_change_price);
            iv_change_price.setBounds(0, 0, iv_change_price.getMinimumWidth(), iv_change_price.getMinimumHeight());
            tvChangePriceNotice.setCompoundDrawables(iv_change_price, null, null, null);


        } else if (tag.equals("1")) {
            tvOpenSaleNotice.setText("开盘提醒我");

            Drawable iv_open_sale = getResources().getDrawable(R.mipmap.iv_open_sale);
            iv_open_sale.setBounds(0, 0, iv_open_sale.getMinimumWidth(), iv_open_sale.getMinimumHeight());
            tvOpenSaleNotice.setCompoundDrawables(iv_open_sale, null, null, null);

        } else if (tag.equals("2")) {
            btnSubscribeNews.setText("订阅楼盘动态");


        }

    }

    @Override
    public void showCollect() {
        CustomerUtils.showTip(this, "收藏成功");
    }

    @Override
    public void showDelCollectSucc() {
        CustomerUtils.showTip(HouseDetailActivity.this, "取消收藏");
    }

    @Override
    public void showCollectList(List<CollectionListBean> collectionList) {
        typeCollectList = new ArrayList<>();
        for (CollectionListBean bean : collectionList) {
            typeCollectList.add(bean.devId);
        }
        if (typeCollectList.contains(dev_id)) {
            if (coop == 3) {
                ivCollect2.setChecked(true);
            } else {
                ivCollect.setChecked(true);
            }
        } else {
            if (coop == 3) {
                ivCollect2.setChecked(false);
            } else {
                ivCollect.setChecked(false);
            }
        }
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(HouseDetailActivity.this, msg);
    }

    //<!--iv_back_banner  iv_share_banner ivRecommend_banner
    @OnClick({R.id.tv_houses_name, R.id.fl_caculator, R.id.rl_detail_more, R.id.comment_recycle,
            R.id.tv_type, R.id.tv_comment_btn, R.id.btnNavigation, R.id.rl_building, R.id.ll_dymanic, R.id.rl_houses_comment,
            R.id.tv_arround, R.id.rl_houses_team, R.id.comment_flowgroup, R.id.iv_map, R.id.iv_address, R.id.tv_saling_more, R.id.rl_dynamic,
            R.id.btnSelectBuy, R.id.relYouHuiXinxiSinge, R.id.tvGetNow,
//            R.id.rl_ask,
            R.id.rl_collect, R.id.ll_look, R.id.tv_call, R.id.iv_back, R.id.iv_share, R.id.ivRecommend, R.id.tvRecommend, R.id.rl_collect2,
            R.id.tv_call2,
            R.id.iv_back_banner, R.id.iv_share_banner, R.id.ivRecommend_banner,
            R.id.btnSubscribeNews, R.id.llChangePriceNotice, R.id.llOpenSaleNotice, R.id.ll_address, R.id.llHouseTitle
            ,
    })//tv_get_discount
    public void onClick(View view) {
        final LoginResult loginCache = dao.getLoginCache();
        switch (view.getId()) {
            case R.id.btnSelectBuy:
                //   选房购房
                if (dao.isLogin()) {
                    mPresenter.checkVipFavorableStatus(devId);
                } else {
                    LoginByVerifyCodeActivity.startActivity(HouseDetailActivity.this, 12);
                }
                break;
            case R.id.tv_houses_name:
                break;
            case R.id.ll_address:
                if (baseBean == null)
                    return;
                MapPeripheralActivity.startActivity(HouseDetailActivity.this, baseBean);
                break;
            //房源列表页
            case R.id.tv_saling_more:
                startActivity(new Intent(HouseDetailActivity.this, HouseResListActivity.class));
                break;
            case R.id.rl_houses_comment:
                if (commentBean != null && commentBean.reviews != null && commentBean.reviews.size() > 0)
                    startActivity(new Intent(HouseDetailActivity.this, HousesCommentActivity.class).putExtra("PROJECTID", baseBean.estateProjectId));
                break;
            //最新动态
            case R.id.rl_dynamic:
            case R.id.ll_dymanic:
                if (dymanicBeanList != null && dymanicBeanList.beanList.size() > 0)
                    startActivity(new Intent(HouseDetailActivity.this, NewDynamicActivity.class).putExtra("DYNAMIC_BEAN", dymanicBeanList).putExtra("PROJECTID", baseBean.estateProjectId + ""));
                break;
            //楼盘更多信息
            case R.id.rl_detail_more:
                startActivity(new Intent(HouseDetailActivity.this, HousesMoreActivity.class).putExtra("MORE_INFO", baseBean));
                break;
            case R.id.iv_map:
                if (baseBean == null)
                    return;
                MapPeripheralActivity.startActivity(HouseDetailActivity.this, baseBean);
                break;
            case R.id.tv_arround:
                if (baseBean == null)
                    return;
                MapAroundActivity.startActivity(HouseDetailActivity.this, devId, baseBean, false);
                break;
            //获取优惠
//            case R.id.tv_get_discount:
//                if (!dao.isLogin()) {
//                    LoginByVerifyCodeActivity.startActivity(HouseDetailActivity.this, 12);
//                    return;
//                }
//                if (baseBean == null)
//                    return;
//
//                if (!isAlreadyDis) {
//                    startActivity(new Intent(HouseDetailActivity.this, GetDiscountActivity.class).putExtra("MORE_INFO", baseBean));
//                } else {
//                    CustomerUtils.showTip(HouseDetailActivity.this, "您已经获取过优惠券了");
//                }
//                break;
            //我要评价
            case R.id.tv_comment_btn:
                if (!dao.isLogin()) {
                    LoginByVerifyCodeActivity.startActivity(this, 12);
                    return;
                }
                String AVGscores = "0";
                if (commentBean != null && !TextUtils.isEmpty(commentBean.AVGScore + "")) {
                    AVGscores = commentBean.AVGScore + "";
                }
                if (baseBean == null)
                    return;
                startActivityForResult(new Intent(HouseDetailActivity.this, CommentActivity.class).putExtra("projectId", baseBean.estateProjectId), 10);
                break;
            //楼栋信息
            case R.id.rl_building:
                WebViewActivity.startActivity(HouseDetailActivity.this, "楼栋信息", Constants.BUILD_HOUSES_URL + "devId=" + devId);

                break;
            //计算器
            case R.id.fl_caculator:
                startActivity(new Intent(HouseDetailActivity.this, CalculatorActivity.class));
                break;
            //专家团队
            case R.id.rl_houses_team:
                if (baseBean == null)
                    return;
                startActivity(new Intent(HouseDetailActivity.this, ExportActivity.class).putExtra("BASE_BEAN", baseBean));
                break;
            case R.id.tv_type:
//                ((HousesAndTypeActivity) HouseDetailActivity.this).getTabLayout().getTabAt(1).select();

                if (coop == 3) {
                    HouseTypeActivity.startActivity(this, baseBean.devId, baseBean.coOp, baseBean, ivCollect2.isChecked(), 0);
                } else {
                    HouseTypeActivity.startActivity(this, baseBean.devId, baseBean.coOp, baseBean, ivCollect.isChecked(), 0);
                }
                break;
            case R.id.btnNavigation:
                if (mlocationClient != null) {
                    showLoading();
                    mlocationClient.startLocation();
                }
                break;
            case R.id.relYouHuiXinxiSinge: //优惠信息
                if (dao.isLogin()) {
                    DiscountListBean item = disCountListBean.get(0);
                    if (item.getDisType().equals("member")) {
                        FavorableSelectActivity.startActivity(HouseDetailActivity.this, baseBean, 11);
                    } else {
                        String type = item.getDisType();
                        if (item.getDisType().equals("favourable")) {
                            type = "favourable" + item.getSeqence();
                        }
                        mPresenter.getDiscountCount(devId, type, 0);
                    }
                } else {
                    LoginByVerifyCodeActivity.startActivity(HouseDetailActivity.this, 12);
                }
                break;

            case R.id.tvGetNow:
                DiscountListBean item = disCountListBean.get(0);
                if (item != null) {
                    if (dao.isLogin()) {
                        if (item.getDisType().equals("member")) {
                            FavorableSelectActivity.startActivity(HouseDetailActivity.this, baseBean, item.getId(), 11);
                        } else {
                            String type = item.getDisType();
                            if (item.getDisType().equals("favourable")) {
                                type = "favourable" + item.getSeqence();
                            }
                            mPresenter.getDiscountCount(devId, type, 0);
                        }
                    } else {
                        LoginByVerifyCodeActivity.startActivity(HouseDetailActivity.this, 12);

                    }
                }

                break;


            //----//
            case R.id.rl_collect:
                collect(loginCache, ivCollect);
                MobclickAgent.onEvent(activity, "house_collec");
                break;
            case R.id.rl_collect2://简版
                collect(loginCache, ivCollect2);
                MobclickAgent.onEvent(activity, "house_collec");
                break;
//            case R.id.rl_ask:
//                if (dao.isLogin()) {
//                    startActivity(new Intent(this, ChatActivity.class).putExtra("userId", EaseConstant.IM_CLIENT_ID));
//                } else {
//                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
//                }
//                MobclickAgent.onEvent(activity, "house_online");
//                break;
            case R.id.ll_look:
                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                    return;
                }

//                if (alredyList != null && alredyList.contains(dev_id)) {
//                    CustomerUtils.showTip(this, "您已预约该楼盘");
//                } else {
                if (baseBean != null) {
                    searchSingleton.lookHouse.add(HouseDetailActivity.this);
                    startActivity(new Intent(this, BookingHouseActivity.class).putExtra("BASE_BEAN", baseBean));
                }
//                }
                break;
            case R.id.tv_call:
                if (baseBean == null)
                    return;
                call(baseBean.hotline);
                break;
            case R.id.tv_call2://简版
                if (baseBean == null)
                    return;
                call(baseBean.hotline);
                break;
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_back_banner:
                finish();
                break;

            case R.id.llHouseTitle:
                break;

            case R.id.ivRecommend://隐藏了
                // 推荐有礼
                if (dao.isLogin()) {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            CTOB + "recommendCustomer?brokerName=" + dao.getCustomer().username + "&brokerPhone=" + dao.getCustomer().phone + "&devId=" + devId
                    );
                } else {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            CTOB + "recommendCustomer?devId=" + devId
                    );
                }

                MobclickAgent.onEvent(activity, "house_gift");
                break;
            case R.id.tvRecommend:
                // 推荐有礼
                if (dao.isLogin()) {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            CTOB + "recommendCustomer?brokerName=" + dao.getCustomer().username + "&brokerPhone=" + dao.getCustomer().phone + "&devId=" + devId
                    );
                } else {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            CTOB + "recommendCustomer?devId=" + devId
                    );
                }

                MobclickAgent.onEvent(activity, "house_gift");
                break;

            case R.id.ivRecommend_banner:
                if (dao.isLogin()) {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            CTOB + "recommendCustomer?brokerName=" + dao.getCustomer().username + "&brokerPhone=" + dao.getCustomer().phone + "&devId=" + devId
                    );
                } else {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            CTOB + "recommendCustomer?devId=" + devId
                    );
                }
                MobclickAgent.onEvent(activity, "house_gift");
                break;
            case R.id.iv_share:
                shareHouse();
                MobclickAgent.onEvent(activity, "house_share");
                break;

            case R.id.iv_share_banner:
                shareHouse();
                MobclickAgent.onEvent(activity, "house_share");
                break;
            //楼盘订阅
//            case R.id.rl_sub:
//                if (!dao.isLogin()) {
//                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 0);
//                    return;
//                }
//                if (baseBean == null)
//                    return;
//                startActivity(new Intent(this, NowSubActivity.class).putExtra("MORE_INFO", baseBean));
//                break;
//
//            case R.id.rl_sub2:
//                if (!dao.isLogin()) {
//                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 0);
//                    return;
//                }
//                if (baseBean == null)
//                    return;
//                startActivity(new Intent(this, NowSubActivity.class).putExtra("MORE_INFO", baseBean));
//                break;

//<!--btnSubscribeNews llChangePriceNotice llOpenSaleNotice-->-->
            case R.id.btnSubscribeNews://订阅楼盘动态
                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                    return;
                }

                if (btnSubscribeNews.getText().equals("订阅楼盘动态")) {

                    if (TextUtils.isEmpty(dao.getCustomer().phone)) {
                        if (houseNewsNoticeDialog == null) {
                            houseNewsNoticeDialog = new HouseNoticeDialog(this);
                            houseNewsNoticeDialog.setData("楼盘动态提醒", "已有" + subNum.getCount2() + "人订阅",
                                    "订阅楼盘动态，不在错过该楼盘的最新消息");

                            houseNewsNoticeDialog.setOnConfirmClickListener(new HouseNoticeDialog.OnConfirmClickListener() {
                                @Override
                                public void onConfirmClick(String phone, String vrificationCode) {
                                    Customer customer = dao.getCustomer();
                                    LoginResult loginCache = dao.getLoginCache();
                                    HouseInfoSubBean houseNewsSubBean = new HouseInfoSubBean();
                                    houseNewsSubBean.devId = baseBean.devId;
                                    houseNewsSubBean.token = loginCache.token;
                                    houseNewsSubBean.userCode = customer.userCode;
                                    houseNewsSubBean.projectId = baseBean.estateProjectId;
//                                houseNewsSubBean.name = customer.username;
                                    houseNewsSubBean.phone = phone;
                                    houseNewsSubBean.varifyCode = vrificationCode;
                                    houseNewsSubBean.tag = "2";
                                    mPresenter.getSub(houseNewsSubBean);

                                }
                            });

                            houseNewsNoticeDialog.setOnVerificationCodeListener(new HouseNoticeDialog.OnVerificationCodeListener() {
                                @Override
                                public void onGetCodeClick(String phone) {
                                    mPresenter.getVerifyCode(phone);
                                }
                            });
                        }

                        houseNewsNoticeDialog.show();

                    } else {
                        if (houseNewsNoticeDialog2 == null) {
                            houseNewsNoticeDialog2 = new HouseNoticeDialog2(this);
                            houseNewsNoticeDialog2.setData("楼盘动态提醒", "已有" + subNum.getCount2() + "人订阅",
                                    "订阅楼盘动态，不再错过该楼盘的最新消息", dao.getCustomer().phone);

                            houseNewsNoticeDialog2.setOnConfirmClickListener(new HouseNoticeDialog2.OnConfirmClickListener() {
                                @Override
                                public void onConfirmClick(String phone) {
                                    Customer customer = dao.getCustomer();
                                    LoginResult loginCache = dao.getLoginCache();
                                    HouseInfoSubBean houseNewsSubBean = new HouseInfoSubBean();
                                    houseNewsSubBean.devId = baseBean.devId;
                                    houseNewsSubBean.token = loginCache.token;
                                    houseNewsSubBean.userCode = customer.userCode;
                                    houseNewsSubBean.projectId = baseBean.estateProjectId;
//                                houseNewsSubBean.name = customer.username;
                                    houseNewsSubBean.phone = phone;
                                    houseNewsSubBean.varifyCode = "";
                                    houseNewsSubBean.tag = "2";
                                    mPresenter.getSub(houseNewsSubBean);
                                }
                            });
                        }
                        houseNewsNoticeDialog2.show();

                    }
                } else {
                    showCancelSubDialog("2");
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("house_subscription", "动态提醒");
                MobclickAgent.onEvent(activity, "house_subscription", map);

                break;
            case R.id.llChangePriceNotice://价格变化

                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                    return;
                }


                if (tvChangePriceNotice.getText().equals("变价提醒我")) {

                    if (TextUtils.isEmpty(dao.getCustomer().phone)) {
                        if (housePriceChangeDialog == null) {
                            housePriceChangeDialog = new HouseNoticeDialog(this);
                            housePriceChangeDialog.setData("楼盘变价提醒", "已有" + subNum.getCount0() + "人订阅",
                                    "订阅楼盘变价提醒，不再错过降价哦");

                            housePriceChangeDialog.setOnConfirmClickListener(new HouseNoticeDialog.OnConfirmClickListener() {
                                @Override
                                public void onConfirmClick(String phone, String vrificationCode) {
                                    Customer customer = dao.getCustomer();
                                    LoginResult loginCache = dao.getLoginCache();
                                    HouseInfoSubBean houseNewsSubBean = new HouseInfoSubBean();
                                    houseNewsSubBean.devId = baseBean.devId;
                                    houseNewsSubBean.token = loginCache.token;
                                    houseNewsSubBean.userCode = customer.userCode;
                                    houseNewsSubBean.projectId = baseBean.estateProjectId;
//                                houseNewsSubBean.name = customer.username;
                                    houseNewsSubBean.phone = phone;
                                    houseNewsSubBean.varifyCode = vrificationCode;
                                    houseNewsSubBean.tag = "0";
                                    mPresenter.getSub(houseNewsSubBean);

                                }
                            });

                            housePriceChangeDialog.setOnVerificationCodeListener(new HouseNoticeDialog.OnVerificationCodeListener() {
                                @Override
                                public void onGetCodeClick(String phone) {
                                    mPresenter.getVerifyCode(phone);
                                }
                            });
                        }

                        housePriceChangeDialog.show();

                    } else {
                        if (housePriceChangeDialog2 == null) {
                            housePriceChangeDialog2 = new HouseNoticeDialog2(this);
                            housePriceChangeDialog2.setData("楼盘变价提醒", "已有" + subNum.getCount0() + "人订阅",
                                    "订阅楼盘变价提醒，不再错过降价哦", dao.getCustomer().phone);

                            housePriceChangeDialog2.setOnConfirmClickListener(new HouseNoticeDialog2.OnConfirmClickListener() {
                                @Override
                                public void onConfirmClick(String phone) {
                                    Customer customer = dao.getCustomer();
                                    LoginResult loginCache = dao.getLoginCache();
                                    HouseInfoSubBean houseNewsSubBean = new HouseInfoSubBean();
                                    houseNewsSubBean.devId = baseBean.devId;
                                    houseNewsSubBean.token = loginCache.token;
                                    houseNewsSubBean.userCode = customer.userCode;
                                    houseNewsSubBean.projectId = baseBean.estateProjectId;
//                                houseNewsSubBean.name = customer.username;
                                    houseNewsSubBean.phone = phone;
                                    houseNewsSubBean.varifyCode = "";
                                    houseNewsSubBean.tag = "0";
                                    mPresenter.getSub(houseNewsSubBean);
                                }
                            });
                        }
                        housePriceChangeDialog2.show();
                    }

                    HashMap<String, String> map2 = new HashMap<>();
                    map2.put("house_subscription", "降价提醒");
                    MobclickAgent.onEvent(activity, "house_subscription", map2);
                } else {
                    showCancelSubDialog("0");
                }

                break;
            case R.id.llOpenSaleNotice://开盘提醒

                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                    return;
                }

                if (tvOpenSaleNotice.getText().equals("开盘提醒我")) {
                    if (TextUtils.isEmpty(dao.getCustomer().phone)) {
                        if (houseOpenSaleDialog == null) {
                            houseOpenSaleDialog = new HouseNoticeDialog(this);
                            houseOpenSaleDialog.setData("楼盘开盘提醒", "已有" + subNum.getCount1() + "人订阅",
                                    "订阅楼盘开盘提醒，不再错失好房源！");

                            houseOpenSaleDialog.setOnConfirmClickListener(new HouseNoticeDialog.OnConfirmClickListener() {
                                @Override
                                public void onConfirmClick(String phone, String vrificationCode) {
                                    Customer customer = dao.getCustomer();
                                    LoginResult loginCache = dao.getLoginCache();
                                    HouseInfoSubBean houseNewsSubBean = new HouseInfoSubBean();
                                    houseNewsSubBean.devId = baseBean.devId;
                                    houseNewsSubBean.token = loginCache.token;
                                    houseNewsSubBean.userCode = customer.userCode;
                                    houseNewsSubBean.projectId = baseBean.estateProjectId;
//                                houseNewsSubBean.name = customer.username;
                                    houseNewsSubBean.phone = phone;
                                    houseNewsSubBean.varifyCode = vrificationCode;
                                    houseNewsSubBean.tag = "1";
                                    mPresenter.getSub(houseNewsSubBean);

                                }
                            });

                            houseOpenSaleDialog.setOnVerificationCodeListener(new HouseNoticeDialog.OnVerificationCodeListener() {
                                @Override
                                public void onGetCodeClick(String phone) {
                                    mPresenter.getVerifyCode(phone);
                                }
                            });
                        }

                        houseOpenSaleDialog.show();

                    } else {
                        if (houseOpenSaleDialog2 == null) {
                            houseOpenSaleDialog2 = new HouseNoticeDialog2(this);
                            houseOpenSaleDialog2.setData("楼盘开盘提醒", "已有" + subNum.getCount1() + "人订阅",
                                    "订阅楼盘开盘提醒，不再错失好房源！", dao.getCustomer().phone);

                            houseOpenSaleDialog2.setOnConfirmClickListener(new HouseNoticeDialog2.OnConfirmClickListener() {
                                @Override
                                public void onConfirmClick(String phone) {
                                    Customer customer = dao.getCustomer();
                                    LoginResult loginCache = dao.getLoginCache();
                                    HouseInfoSubBean houseNewsSubBean = new HouseInfoSubBean();
                                    houseNewsSubBean.devId = baseBean.devId;
                                    houseNewsSubBean.token = loginCache.token;
                                    houseNewsSubBean.userCode = customer.userCode;
                                    houseNewsSubBean.projectId = baseBean.estateProjectId;
//                                houseNewsSubBean.name = customer.username;
                                    houseNewsSubBean.phone = phone;
                                    houseNewsSubBean.varifyCode = "";
                                    houseNewsSubBean.tag = "1";
                                    mPresenter.getSub(houseNewsSubBean);
                                }
                            });
                        }
                        houseOpenSaleDialog2.show();
                    }
                } else {
                    showCancelSubDialog("1");
                }


                HashMap<String, String> map3 = new HashMap<>();
                map3.put("house_subscription", "开盘提醒");
                MobclickAgent.onEvent(activity, "house_subscription", map3);

                break;

        }
    }


    /**
     * 取消订阅弹框
     *
     * @param tag ////0 价格 1 开盘 2 动态
     */
    private void showCancelSubDialog(final String tag) {
        new MaterialDialog.Builder(HouseDetailActivity.this).content("取消订阅后将无法收到最新通知").positiveText("确认").negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                LoginResult loginCache = dao.getLoginCache();
                mPresenter.delSubscribe(tag, loginCache.userCode, loginCache.token, baseBean.estateProjectId, baseBean.devId);
            }
        }).show();


    }

    private void shareHouse() {
        if (baseBean == null)
            return;
        if (baseBean.effectId != null) {
            if (baseBean.effectId.size() == 0) {
                share(baseBean.share + "&projectId=" + baseBean.estateProjectId,
                        baseBean.addressDistrict + " " + baseBean.addressTown + ",价格约" + baseBean.averPrice + "元/㎡,", baseBean.devName, ",好生活线上购买享受更多优惠",
                        baseBean.feature, "");
            } else {
                share(baseBean.share + "&projectId=" + baseBean.estateProjectId,
                        baseBean.addressDistrict + " " + baseBean.addressTown + ",价格约" + baseBean.averPrice + "元/㎡,", baseBean.devName, ",好生活线上购买享受更多优惠",
                        baseBean.feature, baseBean.effectId.get(0).thumbnailImage);
            }

        } else {
            share(baseBean.share + "&projectId=" + baseBean.estateProjectId,
                    baseBean.addressDistrict + " " + baseBean.addressTown + ",价格约" + baseBean.averPrice + "元/㎡,", baseBean.devName, ",好生活线上购买享受更多优惠",
                    baseBean.feature, "");
        }
    }

    public String getDev_id() {
        return dev_id;
    }

    public String getDevName() {
        if (baseBean == null)
            return null;
        return baseBean.devName;
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
                        HouseDetailActivityPermissionsDispatcher.jumpCallPhoneWithCheck(HouseDetailActivity.this, phone);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void jumpCallPhone(String phone) {
        dao.pointCall(dev_id, getDevName(), phone);
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
        HouseDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private PubTipDialog dialogBuy;

    /**
     * 显示购买优惠提示弹窗
     */
    private void showTipBugFavorable() {
        if (dialogBuy == null) {
            dialogBuy = new PubTipDialog(HouseDetailActivity.this, new PubTipDialog.InsideListener() {
                @Override
                public void note(boolean isOK) {
                    if (isOK) {
                        if (favorableVips == null || favorableVips.size() == 0) {
                            CustomerUtils.showTip(HouseDetailActivity.this, "该楼盘暂无可用的会员优惠");
                        } else {
                            //  购买优惠
                            FavorableSelectActivity.startActivity(HouseDetailActivity.this, baseBean, 11);
                        }
                    }
                }
            });
        }
        dialogBuy.showPayTip(R.layout.pub_toast_buy_favorable);
    }

    private PubTipDialog dialogPayContinue;

    /**
     * 显示继续支付提示弹窗
     */
    private void showTipPayContinue() {
        if (dialogPayContinue == null) {
            dialogPayContinue = new PubTipDialog(HouseDetailActivity.this, new PubTipDialog.InsideListener() {
                @Override
                public void note(boolean isOK) {
                    if (isOK) {
                        //  跳转订单详情
                        if (resultCustomerIsOrder != null)
                            OrderDetailActivity.startActivity(HouseDetailActivity.this, resultCustomerIsOrder.orderCode, 3);
                    }
                }
            });
        }
        dialogPayContinue.showPayTip(R.layout.pub_toast_pay_continue);
    }

    @Override
    public void onMapLoaded() {
        if (latLon == null)
            return;
    }

    private void addMarker(LatLng latLonPoint, String title, String snippet) {
        if (latLonPoint == null) {
            return;
        }
        aMap.clear();
        LogOut.d("addMarker : ", title + " ,  " + snippet + " , latLonPoint:(" + latLonPoint.latitude + "," + latLonPoint.longitude + ")");
        View layMarker = LayoutInflater.from(HouseDetailActivity.this).inflate(R.layout.layout_marker, null);

        TextView titleView = (TextView) layMarker.findViewById(R.id.title);
        TextView snippetView = (TextView) layMarker.findViewById(R.id.snippet);
        titleView.setText(title);
        if (!TextUtils.isEmpty(snippet) && !"0".equals(snippet)) {
            snippetView.setText(snippet + "元/m²");
        } else {
            snippetView.setText("价格待定");
        }
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPeripheralActivity.startActivity(HouseDetailActivity.this, baseBean);
            }
        });

        aMap.addMarker(
                new MarkerOptions()
                        .position(latLonPoint)
//                        .title(title)
                        .icon(BitmapDescriptorFactory.fromView(layMarker))
                        .zIndex(4)
//                        .snippet(snippet)
        );
    }

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private double myLat, myLon;
    private LatLng latLon;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(HouseDetailActivity.this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        }
    }

    @Override
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            myLat = aMapLocation.getLatitude();
            myLon = aMapLocation.getLongitude();
            mlocationClient.stopLocation();
            cancelLoading();
            MapUtil.startNavigation(HouseDetailActivity.this, myLat, myLon, latLon.latitude, latLon.longitude, "楼盘地址");
        }
    }


    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                HouseDetailActivity.this.finish();
                return false;
            } else {
                return false;
            }
        }
    };


    private void collect(LoginResult loginCache, CheckBox ivCollect) {
        if (dao.isLogin()) {
            if (!ivCollect.isChecked()) {
                ivCollect.setChecked(true);
                if (!TextUtils.isEmpty(dev_id) && !searchSingleton.collectList.contains(dev_id)) {
                    searchSingleton.collectList.add(dev_id);
                    EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
                    if (loginCache != null) {
                        mPresenter.collectHouse(dev_id, getDevName(), loginCache.userCode, loginCache.token, "0", "");
                    }
                }

            } else {
                ivCollect.setChecked(false);
                if (!TextUtils.isEmpty(dev_id) && searchSingleton.collectList.contains(dev_id)) {
                    searchSingleton.collectList.remove(dev_id);
                    EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
                    mPresenter.delCollectHouse(dev_id, getDevName(), loginCache.userCode, loginCache.token, "0", "");
                }

//                        new AlertDialog.Builder(HousesAndTypeActivity.this).setTitle("提示")
//                                .setMessage("是否取消收藏 ")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (!TextUtils.isEmpty(dev_id) && searchSingleton.collectList.contains(dev_id)) {
//                                            searchSingleton.collectList.remove(dev_id);
//                                            EventBus.getDefault().post(new MessageEvent(HOUSES_INDEXT_COLLECTION));
//                                            mPresenter.delCollectHouse(dev_id,loginCache.userCode,loginCache.token,"0","");
//                                        }
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        ivCollect.setChecked(true);
//                                    }
//                                })
//                                .show();

            }
        } else {
            ivCollect.setChecked(false);
            startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
        }
    }

    @Override
    public void exit() {
        showTip("楼盘数据错误");
        HouseDetailActivity.this.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogOut.d("onActivityResult", "requestCode:" + requestCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10:
                    if (baseBean != null) {
                        mPresenter.requestHousesComment(baseBean.estateProjectId + "", "1");
                    }
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        if (event.MsgType == UPDATE_HOUSE_DETAIL_COMMENT) {
            if (baseBean != null) {
                mPresenter.requestHousesComment(baseBean.estateProjectId + "", "1");
            }
        } else if (LOGIN_STATE_CHANGE == event.MsgType) {
            //登录成功
            LoginResult loginCache = dao.getLoginCache();
            mPresenter.getSubList(loginCache.userCode, loginCache.token, baseBean.estateProjectId, baseBean.devId);

        } else if (RESTART_HOUSE_COLLECT_STATE == event.MsgType) {
            //登录成功
            if (coop == 3) {
                ivCollect2.setChecked(event.isFlag());
            } else {
                ivCollect.setChecked(event.isFlag());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
