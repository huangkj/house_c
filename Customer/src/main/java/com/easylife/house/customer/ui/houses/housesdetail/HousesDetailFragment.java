package com.easylife.house.customer.ui.houses.housesdetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.easylife.house.customer.adapter.ClubAdapter;
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
import com.easylife.house.customer.bean.HouseSaleListBean;
import com.easylife.house.customer.bean.HouseSubNumBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.bean.HousesLikeBean;
import com.easylife.house.customer.bean.HousesToPagerImgBean;
import com.easylife.house.customer.bean.HousesTopImgBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.PageTitleBean;
import com.easylife.house.customer.bean.ResultCustomerIsOrder;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.houses.HousesAndTypeActivity;
import com.easylife.house.customer.ui.houses.exportshop.shop.ExportShopActivity;
import com.easylife.house.customer.ui.houses.housepincontrol.HousePinControlActivity;
import com.easylife.house.customer.ui.houses.housereslist.HouseResListActivity;
import com.easylife.house.customer.ui.houses.housesdetail.comment.CommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.exportlist.ExportActivity;
import com.easylife.house.customer.ui.houses.housesdetail.getdiscount.GetDiscountActivity;
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
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.MapUtil;
import com.easylife.house.customer.util.PriceUtil;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.FlowViewGroup;
import com.easylife.house.customer.view.SpaceItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.easylife.house.customer.config.Constants.HOUSE_NUMBER_URL;
import static com.easylife.house.customer.event.MessageEvent.UPDATE_HOUSE_DETAIL_COMMENT;

/**
 * Created by zgm on 2017/3/21.
 * 楼盘详情
 */
public class HousesDetailFragment extends MVPBaseFragment<HousesDetailContract.View, HouseDetailPresenter> implements
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
    ButtonTouch btnNavigation;
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
    //    @Bind(R.id.recycleFavorable)
    RecyclerView recycleFavorable;
    @Bind(R.id.layFavorable)
    LinearLayout layFavorable;
    @Bind(R.id.llClub)
    LinearLayout llClub;
    @Bind(R.id.tv_houses_comment)
    TextView tvHousesComment;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    //    @Bind(R.id.view)
//    View view;
    WebView wvBuilding;
    @Bind(R.id.tv_open_time)
    TextView tvOpenTime;
    //    @Bind(R.id.rcvYouHuiXinXi)
    RecyclerView rcvYouHuiXinXi;
    @Bind(R.id.tvYouHuiTitle)
    TextView tvYouHuiTitle;
    @Bind(R.id.tvYouHuiDesc)
    TextView tvYouHuiDesc;
    @Bind(R.id.tvGetNow)
    TextView tvGetNow;
    TextView tvBannerCount;
    //    @Bind(R.id.relYouHuiXinxiSinge)
    RelativeLayout relYouHuiXinxiSinge;
    //    @Bind(R.id.rcvLikeHouse22)
    RecyclerView rcvLikeHouse;


    private List<String> images = new ArrayList<>();
    private List<HousesTopImgBean> topImgBeanList = new ArrayList<>();

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
    /**
     * 优惠信息列表数据
     */
    private List<DiscountListBean> disCountListBean;


    public static HousesDetailFragment newInstance() {
        HousesDetailFragment myFragment = new HousesDetailFragment();
        return myFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_houses_detail;
    }

    @Override
    public void initViews() {
        rcvLikeHouse = ((RecyclerView) root.findViewById(R.id.rcvLikeHouse22));
        rcvYouHuiXinXi = ((RecyclerView) root.findViewById(R.id.rcvYouHuiXinXi));
        relYouHuiXinxiSinge = ((RelativeLayout) root.findViewById(R.id.relYouHuiXinxiSinge));
        tvBannerCount = ((TextView) root.findViewById(R.id.tvBannerCount));
        wvBuilding = ((WebView) root.findViewById(R.id.wv_building));
        recycleFavorable = ((RecyclerView) root.findViewById(R.id.recycleFavorable));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
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
                    startActivity(new Intent(getActivity(), HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
                            .putExtra("BASE_BEAN", ((HousesAndTypeActivity) getActivity()).getBaseBean()));
                }
            }
        });

        adapter = new FavorableShowAdapter(R.layout.item_favorable, null);
        recycleFavorable.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleFavorable.setAdapter(adapter);


        showLoading();
        dialog.setOnKeyListener(keylistener);
        devId = ((HousesAndTypeActivity) getActivity()).getDev_id();
        devName = ((HousesAndTypeActivity) getActivity()).getDevName();
        mPresenter.requestHousesImg(devId);//楼盘详情banner图
        mPresenter.requestHousesDetail(devId);//楼盘详情基础信息
        mPresenter.requestMainType(devId);//主力户型
        //devId 和页码
        mPresenter.requestHousesTeam(devId, "1");
        mPresenter.requestHousesClub(devId);
//        mPresenter.getDevFavorable(devId);
//        mPresenter.getVipFavorable(devId);
        mPresenter.getDiscountList(devId);


        Customer customer = mDao.getCustomer();
        if (customer != null) {
            mPresenter.requestIsGetDis(devId, customer.userCode, customer.token);//获取优惠
        }

        initMap();

        ((HousesAndTypeActivity) getActivity()).setNoNetTryRequestData(new HousesAndTypeActivity.NoNetTryRequestData() {
            @Override
            public void tryRequestData() {
                showLoading();
                mPresenter.requestHousesImg(devId);
                mPresenter.requestHousesDetail(devId);
                mPresenter.requestMainType(devId);
                //devId 和页码
                mPresenter.requestHousesTeam(devId, "1");
            }
        });

        for (int i = 0; i < 5; i++) {
            TextView text = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.houses_item_flow_comment, commentFlowgroup, false);
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
                startActivity(new Intent(getActivity(), HousesAndTypeActivity.class).putExtra("DEV_ID", item.devId));
//                }

            }
        });
        rcvLikeHouse.setLayoutManager(new LinearLayoutManager(getActivity()) {//解决scrollview 嵌套rcv滑动不流畅问题
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcvLikeHouse.setAdapter(houseLikeAdapter);
        //优惠信息

        discountAdapter = new DiscountAdapter(null);
        rcvYouHuiXinXi.setVisibility(View.VISIBLE);
        LinearLayoutManager youhuiLinearLayoutManager = new LinearLayoutManager(getActivity());
        youhuiLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvYouHuiXinXi.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(15), LinearLayout.HORIZONTAL));
        rcvYouHuiXinXi.setLayoutManager(youhuiLinearLayoutManager);
        rcvYouHuiXinXi.setAdapter(discountAdapter);
        discountAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DiscountListBean item = discountAdapter.getItem(position);
                if (mDao.isLogin()) {
                    if (item.getDisType().equals("member")) {
                        FavorableSelectActivity.startActivity(getActivity(), baseBean, item.getId(), 11);
                    } else {
                        String type = item.getDisType();
                        if (item.getDisType().equals("favourable")) {
                            type = "favourable" + item.getSeqence();
                        }
                        mPresenter.getDiscountCount(devId, type, position);
                    }
                } else {
                    LoginByVerifyCodeActivity.startActivity(getActivity(), 12);

                }


            }
        });
    }

    private AMap aMap;

    private void initMap() {
        if (aMap == null) {
            aMap = ((TextureSupportMapFragment) getChildFragmentManager()
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
                startActivity(new Intent(getActivity(), PagerActivity.class).putExtra("PAGER_BEAN", pagerImgBean));
            }
        });
        tvBannerCount.setText("1/" + images.size());
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


        HousesAndTypeActivity activity = (HousesAndTypeActivity) getActivity();
        activity.coop = baseBean.coOp;
        activity.updateCoopStyle();
        if (baseBean.coOp == 3) {
            layFavorable.setVisibility(View.GONE);
        } else {
//            layFavorable.setVisibility(View.VISIBLE);
        }


        this.baseBean = baseBean;

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

        ((HousesAndTypeActivity) getActivity()).setBaseBean(baseBean);
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
                TextView tvFeature = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
                tvFeature.setText(features[i]);
                floviewgroup.addView(tvFeature);
            }
        } else {
            TextView tvFeature = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
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
//            Glide.with(getActivity()).load(baseBean.distribution.get(0).url).into(ivBuilding);
        }
        // 设置WebView属性，能够执行Javascript脚本
        wvBuilding.getSettings().setJavaScriptEnabled(true);
        wvBuilding.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        wvBuilding.loadUrl(HOUSE_NUMBER_URL + devId);// TODO: 2018/6/7

        mDao.pointVisit(devId, devName);
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
        if (favorableVips != null && favorableVips.size() > 0) {
            llClub.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);

            listViews = new ArrayList<>();
            for (int i = 0; i < favorableVips.size(); i++) {
                View view = LayoutInflater.from(getActivity()).inflate(
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
                        if (mDao.isLogin()) {
                            try {
                                // 在楼盘基本数据中添加展示图，无数据时才添加
                                if (baseBean.distribution == null || baseBean.distribution.size() == 0) {
                                    List<HousesDetailBaseBean.DistributionBean> covers = new ArrayList<>();
                                    covers.add(new HousesDetailBaseBean.DistributionBean(topImgBeanList.get(0).img.get(0).url));
                                    baseBean.distribution = covers;
                                }
                            } catch (Exception e) {
                                LogOut.d("distribution:", e.toString());
                            }
                            FavorableSelectActivity.startActivity(getActivity(), baseBean, favorableVips.get(finalI).id, 11);
                        } else {
                            LoginByVerifyCodeActivity.startActivity(getActivity(), 12);
                        }
                    }
                });
            }
            ClubAdapter clubAdapter = new ClubAdapter(listViews);
            viewPager.setAdapter(clubAdapter);
        }

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
    }

    /**
     * 楼盘评价列表
     **/
    @Override
    public void showHousesComment(final CommentListBean commentBean) {
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
        } else if (commentBean.reviews.size() == 1) {
            list.add(commentBean.reviews.get(0));
            commentRecycle.setVisibility(View.VISIBLE);
        } else {
            list.add(commentBean.reviews.get(0));
            list.add(commentBean.reviews.get(1));
            commentRecycle.setVisibility(View.VISIBLE);
        }
        CommentListAdapter commentListAdapter = new CommentListAdapter(R.layout.houses_commen_detail_item, list);
        commentListAdapter.setCommentImgOnclickListenear(new CommentListAdapter.CommentImgOnclickListenear() {
            @Override
            public void setCommentOnclick(List<CommentListBean.ReviewsBean.ReviewimgBean> reviewimg, int position) {
                CommentUrlBean urlBean = new CommentUrlBean();
                urlBean.beanList = reviewimg;
                urlBean.position = position;

                startActivity(new Intent(getActivity(), CommentPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
                Log.e("postion", position + "");
            }

            //跳转楼盘评价列表
            @Override
            public void setJumpCommentListClick() {
                if (baseBean == null)
                    return;
                startActivity(new Intent(getActivity(), HousesCommentActivity.class).putExtra("PROJECTID", baseBean.estateProjectId + ""));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setSmoothScrollbarEnabled(true);
        commentRecycle.setLayoutManager(layoutManager);
        commentRecycle.setAdapter(commentListAdapter);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
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
                ((HousesAndTypeActivity) getActivity()).call(phone);
            }

            @Override
            public void exportPage(String brokeCode, String headUrl, String phone) {
//                CustomerUtils.showTip(getActivity(), "专家店铺");
                startActivity(new Intent(getActivity(), ExportShopActivity.class).putExtra("BROKECODE", brokeCode).putExtra("PHONE", phone));
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
                    if (mDao.isLogin()) {
                      /*  startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", imUsername)
                                .putExtra("IM_BUILD", houseSaleListBean)
                                .putExtra("IM_TYPE", 1));
                        mDao.pointIM(baseBean.devId, baseBean.devName, brokeCode, brokerName); // 通知后台HeadLineBean.ListBean处理
                        mPresenter.chatPush(baseBean.devId, brokeCode);*/
                    } else {
                        LoginByVerifyCodeActivity.startActivity(HousesDetailFragment.this, 15);
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
    public void showCollect() {

    }

    @Override
    public void showDelCollectSucc() {

    }

    @Override
    public void showCollectList(List<CollectionListBean> collectionList) {

    }

    @Override
    public void showAlreadyHouse(List<String> alredyList) {

    }

    @Override
    public void showTipDialog(String tip) {

    }

    @Override
    public void getVerifyCodeSucc() {

    }

    @Override
    public void showSubList(List<String> mySubList) {

    }

    @Override
    public void showHouseSubNum(HouseSubNumBean numBean) {

    }

    @Override
    public void showGutSubSucc(String tag, String msg) {

    }

    @Override
    public void showDelSubSucc(String tag, String msg) {

    }


    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }


    @OnClick({R.id.tv_houses_name, R.id.tv_get_discount, R.id.fl_caculator, R.id.rl_detail_more, R.id.comment_recycle,
            R.id.tv_type, R.id.tv_comment_btn, R.id.btnNavigation, R.id.rl_building, R.id.ll_dymanic, R.id.rl_houses_comment,
            R.id.tv_arround, R.id.rl_houses_team, R.id.comment_flowgroup, R.id.iv_map, R.id.iv_address, R.id.tv_saling_more, R.id.rl_dynamic,
            R.id.btnSelectBuy, R.id.relYouHuiXinxiSinge, R.id.tvGetNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSelectBuy:
                //   选房购房
                if (mDao.isLogin()) {
                    mPresenter.checkVipFavorableStatus(devId);
                } else {
                    LoginByVerifyCodeActivity.startActivity(HousesDetailFragment.this, 12);
                }
                break;
            case R.id.tv_houses_name:
                break;
            case R.id.iv_address:
                if (baseBean == null)
                    return;
                MapPeripheralActivity.startActivity(getActivity(), baseBean);
                break;
            //房源列表页
            case R.id.tv_saling_more:
                startActivity(new Intent(getActivity(), HouseResListActivity.class));
                break;
            case R.id.rl_houses_comment:
                startActivity(new Intent(getActivity(), HousesCommentActivity.class).putExtra("PROJECTID", baseBean.estateProjectId));
                break;
            //最新动态
            case R.id.rl_dynamic:
            case R.id.ll_dymanic:
                if (dymanicBeanList != null)
                    startActivity(new Intent(getActivity(), NewDynamicActivity.class).putExtra("DYNAMIC_BEAN", dymanicBeanList).putExtra("PROJECTID", baseBean.estateProjectId + ""));
                break;
            //楼盘更多信息
            case R.id.rl_detail_more:
                startActivity(new Intent(getActivity(), HousesMoreActivity.class).putExtra("MORE_INFO", baseBean));
                break;
            case R.id.iv_map:
                if (baseBean == null)
                    return;
                MapPeripheralActivity.startActivity(getActivity(), baseBean);
                break;
            case R.id.tv_arround:
                if (baseBean == null)
                    return;
                MapAroundActivity.startActivity(getActivity(), devId, baseBean, false);
                break;
            //获取优惠
            case R.id.tv_get_discount:
                if (!mDao.isLogin()) {
                    LoginByVerifyCodeActivity.startActivity(HousesDetailFragment.this, 12);
                    return;
                }
                if (baseBean == null)
                    return;

                if (!isAlreadyDis) {
                    startActivity(new Intent(getActivity(), GetDiscountActivity.class).putExtra("MORE_INFO", baseBean));
                } else {
                    CustomerUtils.showTip(getActivity(), "您已经获取过优惠券了");
                }
                break;
            //我要评价
            case R.id.tv_comment_btn:
                if (!mDao.isLogin()) {
                    LoginByVerifyCodeActivity.startActivity(HousesDetailFragment.this, 12);
                    return;
                }
                String AVGscores = "0";
                if (commentBean != null && !TextUtils.isEmpty(commentBean.AVGScore + "")) {
                    AVGscores = commentBean.AVGScore + "";
                }
                if (baseBean == null)
                    return;
                startActivityForResult(new Intent(getActivity(), CommentActivity.class).putExtra("projectId", baseBean.estateProjectId), 10);
                break;
            //楼栋信息
            case R.id.rl_building:
                WebViewActivity.startActivity(getActivity(), "楼栋信息", Constants.BUILD_HOUSES_URL + "devId=" + devId);

                break;
            //计算器
            case R.id.fl_caculator:
                startActivity(new Intent(getActivity(), CalculatorActivity.class));
                break;
            //专家团队
            case R.id.rl_houses_team:
                if (baseBean == null)
                    return;
                startActivity(new Intent(getActivity(), ExportActivity.class).putExtra("BASE_BEAN", baseBean));
                break;
            case R.id.tv_type:
                ((HousesAndTypeActivity) getActivity()).getTabLayout().getTabAt(1).select();
                break;
            case R.id.btnNavigation:
                if (mlocationClient != null) {
                    showLoading();
                    mlocationClient.startLocation();
                }
                break;
            case R.id.relYouHuiXinxiSinge: //优惠信息
                if (mDao.isLogin()) {
                    DiscountListBean item = disCountListBean.get(0);
                    if (item.getDisType().equals("member")) {
                        FavorableSelectActivity.startActivity(getActivity(), baseBean, 11);
                    } else {
                        String type = item.getDisType();
                        if (item.getDisType().equals("favourable")) {
                            type = "favourable" + item.getSeqence();
                        }
                        mPresenter.getDiscountCount(devId, type, 0);
                    }
                } else {
                    LoginByVerifyCodeActivity.startActivity(getActivity(), 12);
                }
                break;

            case R.id.tvGetNow:
                DiscountListBean item = disCountListBean.get(0);
                if (item != null) {
                    if (mDao.isLogin()) {
                        if (item.getDisType().equals("member")) {
                            FavorableSelectActivity.startActivity(getActivity(), baseBean, item.getId(), 11);
                        } else {
                            String type = item.getDisType();
                            if (item.getDisType().equals("favourable")) {
                                type = "favourable" + item.getSeqence();
                            }
                            mPresenter.getDiscountCount(devId, type, 0);
                        }
                    } else {
                        LoginByVerifyCodeActivity.startActivity(getActivity(), 12);

                    }
                }


                break;
        }
    }

    private PubTipDialog dialogBuy;

    /**
     * 显示购买优惠提示弹窗
     */
    private void showTipBugFavorable() {
        if (dialogBuy == null) {
            dialogBuy = new PubTipDialog(getActivity(), new PubTipDialog.InsideListener() {
                @Override
                public void note(boolean isOK) {
                    if (isOK) {

                        if (favorableVips == null || favorableVips.size() == 0) {
                            CustomerUtils.showTip(getActivity(), "该楼盘暂无可用的会员优惠");
                        } else {
                            //  购买优惠
                            FavorableSelectActivity.startActivity(HousesDetailFragment.this, baseBean, 11);
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
            dialogPayContinue = new PubTipDialog(getActivity(), new PubTipDialog.InsideListener() {
                @Override
                public void note(boolean isOK) {
                    if (isOK) {
                        //  跳转订单详情
                        if (resultCustomerIsOrder != null)
                            OrderDetailActivity.startActivity(HousesDetailFragment.this, resultCustomerIsOrder.orderCode, 3);
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
        View layMarker = LayoutInflater.from(getActivity()).inflate(R.layout.layout_marker, null);

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
                MapPeripheralActivity.startActivity(getActivity(), baseBean);
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
            mlocationClient = new AMapLocationClient(getActivity());
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
            MapUtil.startNavigation(getActivity(), myLat, myLon, latLon.latitude, latLon.longitude, "楼盘地址");
        }
    }


    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                getActivity().finish();
                return false;
            } else {
                return false;
            }
        }
    };

    @Override
    public void exit() {
        showTip("楼盘数据错误");
        getActivity().finish();
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
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
