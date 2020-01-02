package com.easylife.house.customer.ui.houses.housedetailsimple;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.CommentListAdapter;
import com.easylife.house.customer.adapter.HousesDetailMainAdapter;
import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.CommentUrlBean;
import com.easylife.house.customer.bean.Customer;
import com.easylife.house.customer.bean.DymanicBeanList;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesDynamicBean;
import com.easylife.house.customer.bean.HousesLikeBean;
import com.easylife.house.customer.bean.HousesToPagerImgBean;
import com.easylife.house.customer.bean.HousesTopImgBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.PageTitleBean;
import com.easylife.house.customer.bean.ResultCustomerIsOrder;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.houses.HousesAndTypeActivity;
import com.easylife.house.customer.ui.houses.housesdetail.comment.CommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.housescomment.CommentPagerActivity;
import com.easylife.house.customer.ui.houses.housesdetail.housescomment.HousesCommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.newdynamic.NewDynamicActivity;
import com.easylife.house.customer.ui.houses.housesdetail.nowsub.NowSubActivity;
import com.easylife.house.customer.ui.houses.housesdetail.pager.NetworkImageHolderView;
import com.easylife.house.customer.ui.houses.housesdetail.pager.PagerActivity;
import com.easylife.house.customer.ui.houses.housetype.houseandtypesimple.HousesAndTypeSimpleActivity;
import com.easylife.house.customer.ui.houses.housetype.housesTypeDetail.HousesTypeDetailActivity;
import com.easylife.house.customer.ui.houses.map.maparound.MapAroundActivity;
import com.easylife.house.customer.ui.houses.map.mapperipheral.MapPeripheralActivity;
import com.easylife.house.customer.ui.mine.calculator.CalculatorActivity;
import com.easylife.house.customer.ui.mine.order.orderdetail.OrderDetailActivity;
import com.easylife.house.customer.ui.payment.favorable.FavorableSelectActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.MapUtil;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.ExpandView;
import com.easylife.house.customer.view.FlowViewGroup;

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
import static com.easylife.house.customer.event.MessageEvent.UPDATE_HOUSE_DETAIL_COMMENT;

/**
 * Created by zgm on 2017/3/21.
 * 楼盘详情
 */
public class HousesDetailSimpleFragment extends MVPBaseFragment<HousesDetailSimpleContract.View, HouseDetailSimplePresenter> implements
        HousesDetailSimpleContract.View, AMap.OnMapLoadedListener, LocationSource, AMapLocationListener {

    @Bind(R.id.tv_houses_name)
    TextView tvHousesName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.floviewgroup)
    FlowViewGroup floviewgroup;
    @Bind(R.id.tv_address_value)
    TextView tvAddressValue;
    @Bind(R.id.tv_open_time_value)
    TextView tvOpenTimeValue;
    @Bind(R.id.tv_complete_time_value)
    TextView tvCompleteTimeValue;
    //    @Bind(R.id.tv_sub)
//    TextView tvSub;
//    @Bind(R.id.tv_club_value)
//    TextView tvClubValue;
//    @Bind(R.id.tv_get_discount)
//    TextView tvGetDiscount;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.fl_caculator)
    FrameLayout flCaculator;
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
//    @Bind(R.id.rl_building)
//    RelativeLayout rlBuilding;
//    @Bind(R.id.team_recycle)
//    RecyclerView teamRecycle;
//    @Bind(R.id.rl_houses_team)
//    RelativeLayout rlHousesTeam;
    @Bind(R.id.iv_like1)
    ImageView ivLike1;
    @Bind(R.id.tv_like1)
    TextView tvLike1;
    @Bind(R.id.tvName1)
    TextView tvName1;
    @Bind(R.id.tvName2)
    TextView tvName2;
    @Bind(R.id.ll_like1)
    RelativeLayout llLike1;
    @Bind(R.id.iv_like2)
    ImageView ivLike2;
    @Bind(R.id.iv_address)
    ImageView ivAddress;
    @Bind(R.id.tv_like2)
    TextView tvLike2;
    @Bind(R.id.ll_like2)
    RelativeLayout llLike2;
    @Bind(R.id.rl_houses_like)
    RelativeLayout rlHousesLike;
    //    @Bind(R.id.rl_get_discount)
//    RelativeLayout rlGetDiscount;
    @Bind(R.id.ll_like)
    LinearLayout llLike;
    @Bind(R.id.ll_dymanic)
    LinearLayout llDynamic;
    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.tv_complete_time)
    TextView tvCompleteTime;
    @Bind(R.id.btnNavigation)
    ButtonTouch btnNavigation;
    @Bind(R.id.iv_map)
    View iv_map;
    //    @Bind(R.id.recycleview_saling)
//    RecyclerView recycleSaling;
//    @Bind(R.id.tv_saling_more)
//    TextView tvSalingMore;
//    @Bind(R.id.btnGetFavorable)
//    ButtonTouch btnGetFavorable;
//    @Bind(R.id.btnSelectBuy)
//    ButtonTouch btnSelectBuy;
//    @Bind(R.id.vOffset)
//    View vOffset;
//    @Bind(R.id.tvDiscount)
//    TextView tvDiscount;
//    @Bind(R.id.layDiscount)
//    LinearLayout layDiscount;
//    @Bind(R.id.recycleFavorable)
//    RecyclerView recycleFavorable;
//    @Bind(R.id.tvVip)
//    TextView tvVip;
//    @Bind(R.id.layVip)
//    LinearLayout layVip;
//    @Bind(R.id.layFavorable)
//    LinearLayout layFavorable;
    @Bind(R.id.tv_houses_comment)
    TextView tvHousesComment;
    @Bind(R.id.btn_sub)
    Button btnSub;
    @Bind(R.id.layout_title)
    RelativeLayout mLinearLayout;
    @Bind(R.id.expandView)
    ExpandView mExpandView;
    @Bind(R.id.textview_title)
    TextView mTextView;
    @Bind(R.id.tv_pack_up)
    TextView mPackUp;
    @Bind(R.id.iv_title)
    ImageView ivTitle;
    @Bind(R.id.iv_pack_up)
    ImageView ivPackUp;
    @Bind(R.id.rl_main)
    RelativeLayout rlMain;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.tv_like_area1)
    TextView tvLikeArear1;
    @Bind(R.id.tv_like_room1)
    TextView tvLikeRoom1;
    @Bind(R.id.tv_like_area2)
    TextView tvLikeArear2;
    @Bind(R.id.tv_like_room2)
    TextView tvLikeRoom2;

    public static final String BASE_BEAN = "BASE_BEAN";

    private List<String> images = new ArrayList<>();
    private List<HousesTopImgBean> topImgBeanList = new ArrayList<>();

    private HousesDetailBaseBean baseBean;
    private HousesDynamicBean housesDynamicBean;
    private DymanicBeanList dymanicBeanList;
    private CommentListBean commentBean;
    private String devId, devName;
    private HousesDetailMainAdapter housesTypeAdapter;
    private List<HousesTypeBean.HouseLayoutDataBean> allList;
//    private FavorableShowAdapter adapter;


    public static HousesDetailSimpleFragment newInstance() {
        HousesDetailSimpleFragment myFragment = new HousesDetailSimpleFragment();
        return myFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_houses_detail_simple;
    }

    @Override
    public void initViews() {

        mLinearLayout.setClickable(true);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mExpandView.isExpand()) {
                    mTextView.setVisibility(View.VISIBLE);
                    ivTitle.setVisibility(View.VISIBLE);
                    mExpandView.collapse();
                    mTextView.setText("查看更多信息");
                    mPackUp.setVisibility(View.INVISIBLE);
                    ivPackUp.setVisibility(View.INVISIBLE);
                } else {
                    mExpandView.expand();
                    mTextView.setVisibility(View.INVISIBLE);
                    ivTitle.setVisibility(View.INVISIBLE);
                    mPackUp.setText("收起");
                    mPackUp.setVisibility(View.VISIBLE);
                    ivPackUp.setVisibility(View.VISIBLE);
                }
            }
        });


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
        recycleviewType.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (allList.size() != 0) {
                    HousesTypeBean.HouseLayoutDataBean houseLayoutDataBean = allList.get(position);
                    startActivity(new Intent(getActivity(), HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
                            .putExtra("BASE_BEAN", ((HousesAndTypeSimpleActivity) getActivity()).getBaseBean()));
                }
            }
        });

//        adapter = new FavorableShowAdapter(R.layout.item_favorable, null);
//        recycleFavorable.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recycleFavorable.setAdapter(adapter);

        showLoading();
        dialog.setOnKeyListener(keylistener);
        devId = ((HousesAndTypeSimpleActivity) getActivity()).getDev_id();
        devName = ((HousesAndTypeSimpleActivity) getActivity()).getDevName();
        mPresenter.requestHousesImg(devId);//楼盘详情banner图
        mPresenter.requestHousesDetail(devId);//楼盘详情基础信息
        mPresenter.requestMainType(devId);//主力户型
        //devId 和页码
//        mPresenter.requestHousesTeam(devId, "1");//专家团队


//        mPresenter.requestHousesClub(devId);
//        mPresenter.getDevFavorable(devId);//楼盘优惠
//        mPresenter.getVipFavorable(devId);

        Customer customer = mDao.getCustomer();
//        if (customer != null) {
//            mPresenter.requestIsGetDis(devId, customer.userCode, customer.token);//获取优惠
//        }

        initMap();

        ((HousesAndTypeSimpleActivity) getActivity()).setNoNetTryRequestData(new HousesAndTypeSimpleActivity.NoNetTryRequestData() {
            @Override
            public void tryRequestData() {
                showLoading();
                mPresenter.requestHousesImg(devId);
                mPresenter.requestHousesDetail(devId);
                mPresenter.requestMainType(devId);
                //devId 和页码
//                mPresenter.requestHousesTeam(devId, "1");//专辑团队
            }
        });

        for (int i = 0; i < 5; i++) {
            TextView text = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.houses_item_flow_comment, commentFlowgroup, false);
            switch (i) {
                case 0:
                    text.setText("环境好(0)");
                    break;
                case 1:
                    text.setText("配套完善(0)");
                    break;
                case 2:
                    text.setText("交通便利(0)");
                    break;
                case 3:
                    text.setText("价格优势(0)");
                    break;
                case 4:
                    text.setText("地段优势(0)");
                    break;
            }
            commentFlowgroup.addView(text);
        }

//        switch (i) {
//            case 0:
//                text.setText("环境好(" + strscores.scoreEnvi + ")");
//                break;
//            case 1:
//                text.setText("配套完善(" + strscores.scoreMating + ")");
//                break;
//            case 2:
//                text.setText("交通便利(" + strscores.scoreTraffic + ")");
//                break;
//            case 3:
//                text.setText("价格优势(" + strscores.scorePrice + ")");
//                break;
//            case 4:
//                text.setText("地段优势(" + strscores.scoreDistrict + ")");
//                break;
//        }

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
//            aMap.setInfoWindowAdapter(this);
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
        }, images).setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

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
        convenientBanner.setCanLoop(false);
    }

    private ResultCustomerIsOrder resultCustomerIsOrder;

//    @Override
//    public void checkVipFavorableStatus(ResultCustomerIsOrder resultCustomerIsOrder) {
//        this.resultCustomerIsOrder = resultCustomerIsOrder;
//        if (ResultCustomerIsOrder.STATUS_COMPLETE.equals(resultCustomerIsOrder.status)) {
//            btnSelectBuy.setBackgroundResource(R.drawable.shape_orange_white_3);
//            btnSelectBuy.setTextColor(getResources().getColor(R.color.gradient_end));
//        }
//    }

    /**
     * 楼盘基础信息
     *
     * @param baseBean
     */
    @Override
    public void showHousesDetail(HousesDetailBaseBean baseBean) {

        this.baseBean = baseBean;
        // 1.  判断是否明房源 --- 是否显示按钮（初始显示状态为灰色按钮）
//        if ("1".equals(baseBean.isTransparent)) {
//            layFavorable.setVisibility(View.VISIBLE);
//            btnSelectBuy.setVisibility(View.VISIBLE);
//
//            if (btnGetFavorable.getVisibility() == View.VISIBLE) {
//                vOffset.setVisibility(View.VISIBLE);
//            }
//
//            // 2. 是否登录
//            if (mDao.isLogin()) {
//                // 3. coOp ,
//                if (baseBean.coOp == Order.CoOp.ENTERPRISE.code) {
//                    // 纯企业付费，点击直接跳转房源销控，显示橘色边框
//                    btnSelectBuy.setBackgroundResource(R.drawable.shape_orange_white_3);
//                    btnSelectBuy.setTextColor(getResources().getColor(R.color.gradient_end));
//                } else {
//                    // 4.会员付费类 - 需判断是否认筹支付完成，否则还是灰色
//                    mPresenter.checkVipFavorableStatus(devId);
//                }
//            }
//        } else {
//            btnSelectBuy.setVisibility(View.GONE);
//        }

        //更多信息展示
        mExpandView.setMoreDatas(baseBean);

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

        ((HousesAndTypeSimpleActivity) getActivity()).setBaseBean(baseBean);
        tvHousesName.setText(baseBean.devName);

        if (!TextUtils.isEmpty(baseBean.averPrice) && !"0".equals(baseBean.averPrice)) {
            tvPrice.setText(baseBean.averPrice + "元/m²");
        } else {
            tvPrice.setText("价格待定");
        }
//        CustomerUtils.setEmptyTv(tvPrice,baseBean.averPrice,"元/m²","价格待定");
//        tvPrice.setText(baseBean.averPrice + "元/m²");
        if (baseBean.feature != null && !TextUtils.isEmpty(baseBean.feature)) {
            String[] features = baseBean.feature.split(",");
            for (int i = 0; i < features.length; i++) {
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
//        CustomerUtils.isEmptyData(tvOpenTimeValue,baseBean.openingRange + " " + CustomerUtils.dateTransSdf(baseBean.openTime),"");
//        CustomerUtils.isEmptyData(tvCompleteTimeValue,baseBean.liveRange + " " + CustomerUtils.dateTransSdf(baseBean.liveTime),"");
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
//        if (baseBean.distribution != null && baseBean.distribution.size() != 0) {
//            Glide.with(getActivity()).load(baseBean.distribution.get(0).url).into(ivBuilding);
//        }

        mDao.pointVisit(devId, devName);
    }


//    @Override
//    public void showFavorableDev(DevFavorable devFavorable) {
//        if (devFavorable == null)
//            return;
//        if (devFavorable.discount != null && ("1".equals(devFavorable.discount.discountAllType) || "1".equals(devFavorable.discount.discountDaiType))) {
//            layFavorable.setVisibility(View.VISIBLE);
//            // 全款贷款
//            layDiscount.setVisibility(View.VISIBLE);
//            String strHtml = "";
//            if ("1".equals(devFavorable.discount.discountAllType)) {
//                // 显示全款折扣
//                strHtml += "全款<font   color=\"#FF6800\">" +
//                        devFavorable.discount.discountAll +
//                        "折</font>     ";
//            }
//            if ("1".equals(devFavorable.discount.discountDaiType)) {
//                // 显示贷款折扣
//                strHtml += "贷款<font   color=\"#FF6800\">" +
//                        devFavorable.discount.discountDai +
//                        "折</font>     ";
//            }
//
//            tvDiscount.setText(Html.fromHtml(strHtml));
//        }
//        if (devFavorable.favourable != null && devFavorable.favourable.size() != 0) {
//            layFavorable.setVisibility(View.VISIBLE);
//            // 楼盘优惠列表
//            adapter.setNewData(devFavorable.favourable);
//        }
//    }

//    @Override
//    public void showFavorableVip(List<FavorableVip> favorableVips) {
//        if (favorableVips != null && favorableVips.size() > 0) {
//            layFavorable.setVisibility(View.VISIBLE);
//            // 只显示最新一条认筹优惠
//            if (btnSelectBuy.getVisibility() == View.VISIBLE) {
//                vOffset.setVisibility(View.VISIBLE);
//            }
//            btnGetFavorable.setVisibility(View.VISIBLE);
//            layVip.setVisibility(View.VISIBLE);
//            tvVip.setText(favorableVips.get(0).privilege);
//        } else {
//            // 无会员优惠时不显示获取优惠按钮
//            btnGetFavorable.setVisibility(View.GONE);
//        }
//
//    }

    /**
     * 会员专享
     */
//    @Override
//    public void showHousesClub(GitDisCountBean gitDisCountBean) {
//    }

    private boolean isAlreadyDis = false;


//    @Override
//    public void showRequestGetDis(List<DisCountIsAlreadyBean> alreadyBeanList) {
//        if (alreadyBeanList != null) {
//            for (int i = 0; i < alreadyBeanList.size(); i++) {
//
//                DisCountIsAlreadyBean disCountIsAlreadyBean = alreadyBeanList.get(i);
//                if (disCountIsAlreadyBean.devId.equals(devId)) {
//                    isAlreadyDis = true;
//                } else {
//                    isAlreadyDis = false;
//                }
//
//            }
//        }
//    }

    @Override
    public void showMainFail() {
        view.setVisibility(View.GONE);
        rlMain.setVisibility(View.GONE);
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
     */
    @Override
    public void showHousesComment(final CommentListBean commentBean) {
        this.commentBean = commentBean;
        if (!TextUtils.isEmpty(commentBean.AVGScore)) {
            housesStar.setRating(Float.parseFloat(commentBean.AVGScore));
        }
        tvStartGrade.setText(commentBean.AVGScore + "分");
        CommentListBean.Strscores strscores = commentBean.strscores.get(0);
        for (int i = 0; i < commentFlowgroup.getChildCount(); i++) {

            switch (i) {
                case 0:
                    ((TextView) commentFlowgroup.getChildAt(0)).setText("环境好(" + strscores.scoreEnvi + ")");
                    break;
                case 1:
                    ((TextView) commentFlowgroup.getChildAt(1)).setText("配套完善(" + strscores.scoreMating + ")");
                    break;
                case 2:
                    ((TextView) commentFlowgroup.getChildAt(2)).setText("交通便利(" + strscores.scoreTraffic + ")");
                    break;
                case 3:
                    ((TextView) commentFlowgroup.getChildAt(3)).setText("价格优势(" + strscores.scorePrice + ")");
                    break;
                case 4:
                    ((TextView) commentFlowgroup.getChildAt(4)).setText("地段优势(" + strscores.scoreDistrict + ")");
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
        CommentListAdapter commentListAdapter = new CommentListAdapter(R.layout.houses_comment_item, list);
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
        allList = new ArrayList<>();
        for (int i = 0; i < beanList.size(); i++) {
            HousesTypeBean housesTypeBean = beanList.get(i);
            allList.addAll(housesTypeBean.houseLayoutData);
        }
        housesTypeAdapter.setNewData(allList);
        if (allList.size() == 0) {
            view.setVisibility(View.GONE);
            rlMain.setVisibility(View.GONE);
        }
    }

    /**
     * 专家团队
     *
     * @param expertTeamList
     */
//    private List<ExportListBean> expertTeamList;
//
//    @Override
//    public void showHousesTeam(List<ExportListBean> expertTeamList) {
//        if (expertTeamList == null || expertTeamList.size() == 0) {
//            rlHousesTeam.setVisibility(View.GONE);
//            return;
//        } else {
//            rlHousesTeam.setVisibility(View.VISIBLE);
//        }
//        this.expertTeamList = expertTeamList;
//        List<ExportListBean> list = new ArrayList<>();
//        if (expertTeamList.size() == 0) {
//            teamRecycle.setVisibility(View.GONE);
//        } else if (expertTeamList.size() == 1) {
//            list.add(expertTeamList.get(0));
//            teamRecycle.setVisibility(View.VISIBLE);
//        } else {
//            list.add(expertTeamList.get(0));
//            list.add(expertTeamList.get(1));
//            teamRecycle.setVisibility(View.VISIBLE);
//        }
//        ExpertTeamAdapter expertTeamAdapter = new ExpertTeamAdapter(R.layout.expert_team, list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
//        teamRecycle.setLayoutManager(layoutManager);
//        teamRecycle.setAdapter(expertTeamAdapter);
//
//        expertTeamAdapter.setOnPhoneClickLisenear(new ExpertTeamAdapter.onPhoneClickLisenear() {
//            @Override
//            public void phoneClick(String phone) {
//                ((HousesAndTypeSimpleActivity) getActivity()).call(phone);
//            }
//
//            @Override
//            public void exportPage(String brokeCode, String headUrl, String phone) {
//                startActivity(new Intent(getActivity(), ExportShopActivity.class).putExtra("BROKECODE", brokeCode).putExtra("PHONE", phone));
//            }
//
//            @Override
//            public void imChat(String imUsername, String brokeCode, String brokerName, String phone) {
//                //进入IM聊天
//
//                if (!TextUtils.isEmpty(imUsername)) {
//
//                    HouseSaleListBean houseSaleListBean = null;
//                    if (baseBean != null) {
//                        houseSaleListBean = new HouseSaleListBean();
//                        houseSaleListBean.name = baseBean.devName;
//                        houseSaleListBean.propertyType = baseBean.propertyType;
//                        houseSaleListBean.address = baseBean.addressDistrict + baseBean.addressTown;
//                        houseSaleListBean.averPrice = baseBean.averPrice;
//                        if (baseBean.distribution != null && baseBean.distribution.size() != 0) {
//                            houseSaleListBean.imgUrl = baseBean.distribution.get(0).thumbnailImage;
//                        }
//                        houseSaleListBean.devId = baseBean.devId;
//                        houseSaleListBean.share = baseBean.share;
//                    }
//                    if (mDao.isLogin()) {
//                        startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", imUsername)
//                                .putExtra("IM_BUILD", houseSaleListBean)
//                                .putExtra("IM_TYPE", 1));
//                        mDao.pointIM(baseBean.devId, baseBean.devName, brokeCode, brokerName);
//                    } else {
//                        LoginByVerifyCodeActivity.startActivity(HousesDetailSimpleFragment.this, 12);
//                    }
//
//                }
//            }
//        });
//    }


    /**
     * 相似楼盘
     *
     * @param likeBean
     */
    @Override
    public void showHousesLike(List<HousesLikeBean> likeBean) {
        if (likeBean == null || 0 == likeBean.size()) {
            llLike.setVisibility(View.GONE);
            rlHousesLike.setVisibility(View.GONE);
        } else if (1 == likeBean.size()) {
            rlHousesLike.setVisibility(View.VISIBLE);
            llLike.setVisibility(View.VISIBLE);
            llLike2.setVisibility(View.INVISIBLE);
            final HousesLikeBean housesLikeBean = likeBean.get(0);
            tvLikeArear1.setText(housesLikeBean.devSquareMetre);
            tvLikeRoom1.setText(housesLikeBean.devBedroom);
            if (housesLikeBean.house != null && housesLikeBean.effectId.size() != 0) {
                CacheManager.initImageClientList(getActivity(), ivLike1, housesLikeBean.effectId.get(0).thumbnailImage);
            }
            if (TextUtils.isEmpty(housesLikeBean.averPrice) || "0".equals(housesLikeBean.averPrice)) {
                tvLike1.setText("价格待定");
                tvName1.setText(housesLikeBean.devName);
            } else {
                tvName1.setText(housesLikeBean.devName);
                double averPrice = Double.parseDouble(housesLikeBean.averPrice);
                if (averPrice >= 1000000) {
                    tvLike1.setText("均价:" + (int) (averPrice / 10000) + "万元/㎡");
                } else {
                    tvLike1.setText("均价:" + housesLikeBean.averPrice + "元/㎡");
                }
            }
            llLike1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HousesAndTypeActivity.startActivity(getActivity(), housesLikeBean.devId, false, 0);
                }
            });
        } else if (likeBean.size() >= 2) {
            rlHousesLike.setVisibility(View.VISIBLE);
            llLike.setVisibility(View.VISIBLE);
            llLike2.setVisibility(View.VISIBLE);
            final HousesLikeBean housesLikeBean = likeBean.get(0);
            tvLikeArear1.setText(housesLikeBean.devSquareMetre);
            tvLikeRoom1.setText(housesLikeBean.devBedroom);
            if (housesLikeBean.house != null && housesLikeBean.effectId.size() != 0) {
                CacheManager.initImageClientList(getActivity(), ivLike1, housesLikeBean.effectId.get(0).thumbnailImage);
            }

            if (TextUtils.isEmpty(housesLikeBean.averPrice) || "0".equals(housesLikeBean.averPrice)) {
                tvLike1.setText("价格待定");
                tvName1.setText(housesLikeBean.devName);
            } else {
                tvName1.setText(housesLikeBean.devName);
                double averPrice = Double.parseDouble(housesLikeBean.averPrice);
                if (averPrice >= 1000000) {
                    tvLike1.setText("均价:" + (int) (averPrice / 10000) + "万元/㎡");
                } else {
                    tvLike1.setText("均价:" + housesLikeBean.averPrice + "元/㎡");
                }
            }

            final HousesLikeBean housesLikeBean1 = likeBean.get(1);
            tvLikeArear2.setText(housesLikeBean1.devSquareMetre);
            tvLikeRoom2.setText(housesLikeBean1.devBedroom);
            if (housesLikeBean.house != null && housesLikeBean1.effectId.size() != 0) {
                CacheManager.initImageClientList(getActivity(), ivLike2, housesLikeBean1.effectId.get(0).thumbnailImage);
            }

            if (TextUtils.isEmpty(housesLikeBean1.averPrice) || "0".equals(housesLikeBean1.averPrice)) {
                tvLike2.setText("价格待定");
                tvName2.setText(housesLikeBean1.devName);
            } else {
                tvName2.setText(housesLikeBean1.devName);
                double averPrice = Double.parseDouble(housesLikeBean1.averPrice);
                if (averPrice >= 1000000) {
                    tvLike2.setText("均价:" + (int) (averPrice / 10000) + "万元/㎡");
                } else {
                    tvLike2.setText("均价:" + housesLikeBean1.averPrice + "元/㎡");
                }
            }


            llLike1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HousesAndTypeActivity.startActivity(getActivity(), housesLikeBean.devId, false, 0);
                }
            });


            llLike2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HousesAndTypeActivity.startActivity(getActivity(), housesLikeBean1.devId, false, 0);
                }
            });
        }
    }

    @Override
    public void showFail(String msg) {
        cancelLoading();
    }

    @Override
    public void showDymanicCount(String count) {
        tvDynamicValue.setText("(" + count + ")");
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }


    @OnClick({R.id.tv_houses_name, R.id.fl_caculator, R.id.comment_recycle, R.id.tv_arround,
            R.id.tv_type, R.id.tv_comment_btn, R.id.btnNavigation, R.id.ll_dymanic, R.id.rl_houses_comment, R.id.comment_flowgroup, R.id.iv_map, R.id.iv_address, R.id.rl_dynamic, R.id.btn_sub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sub:
                //马上订阅
                if (!mDao.isLogin()) {
                    startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 0);
                    return;
                }
                if (baseBean == null)
                    return;
                startActivity(new Intent(getActivity(), NowSubActivity.class).putExtra("MORE_INFO", baseBean));
                break;
//            case R.id.btnGetFavorable:
//                //   获取优惠
//                if (mDao.isLogin()) {
//                    try {
//                        // 在楼盘基本数据中添加展示图，无数据时才添加
//                        if (baseBean.distribution == null || baseBean.distribution.size() == 0) {
//                            List<HousesDetailBaseBean.DistributionBean> covers = new ArrayList<>();
//                            covers.add(new HousesDetailBaseBean.DistributionBean(topImgBeanList.get(0).img.get(0).url));
//                            baseBean.distribution = covers;
//                        }
//                    } catch (Exception e) {
//                        LogOut.d("distribution:", e.toString());
//                    }
//                    FavorableSelectActivity.startActivity(this, baseBean, 11);
//                } else {
//                    LoginByVerifyCodeActivity.startActivity(this, 12);
//                }
//                break;
//            case R.id.btnSelectBuy:
//                //   选房购房
//                if (mDao.isLogin()) {
//                    if (resultCustomerIsOrder != null) {
//                        if (ResultCustomerIsOrder.STATUS_COMPLETE.equals(resultCustomerIsOrder.status)) {
//                            // 纯企业付费  或者  会员付费已支付成功
//                            HousePinControlActivity.startActivity(this, devId, 0);
//                        } else if (ResultCustomerIsOrder.STATUS_PAY_PART.equals(resultCustomerIsOrder.status)) {
//                            //  提示去订单详情页继续支付
//                            showTipPayContinue();
//                        } else if (ResultCustomerIsOrder.STATUS_NOT.equals(resultCustomerIsOrder.status)) {
//                            // 提示购买
//                            showTipBugFavorable();
//                        }
//                    } else {
//                        HousePinControlActivity.startActivity(this, devId, 0);
//                    }
//                } else {
//                    LoginByVerifyCodeActivity.startActivity(this, 12);
//                }
//                break;
            case R.id.tv_houses_name:
                break;
            case R.id.iv_address:
                if (baseBean == null)
                    return;
                MapPeripheralActivity.startActivity(getActivity(), baseBean);
                break;
            //房源列表页
//            case R.id.tv_saling_more:
//                startActivity(new Intent(getActivity(), HouseResListActivity.class));
//                break;
            case R.id.rl_houses_comment:
                startActivity(new Intent(getActivity(), HousesCommentActivity.class)
                        .putExtra("PROJECTID", baseBean.estateProjectId)
                        .putExtra(BASE_BEAN, baseBean));
                break;
            //最新动态
            case R.id.rl_dynamic:
            case R.id.ll_dymanic:
                if (dymanicBeanList != null)
                    startActivity(new Intent(getActivity(), NewDynamicActivity.class).putExtra("DYNAMIC_BEAN", dymanicBeanList).putExtra("PROJECTID", baseBean.estateProjectId + ""));
                break;
            //马上订阅
//            case R.id.tv_sub:
//                if (!mDao.isLogin()) {
//                    startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 12);
//                    return;
//                }
//                if (baseBean == null)
//                    return;
//                startActivity(new Intent(getActivity(), NowSubActivity.class).putExtra("MORE_INFO", baseBean));
//                break;
            case R.id.iv_map:
                if (baseBean == null)
                    return;
                MapPeripheralActivity.startActivity(getActivity(), baseBean);
                break;
            case R.id.tv_arround:
                if (baseBean == null)
                    return;
                MapAroundActivity.startActivity(getActivity(), devId, baseBean, true);
                break;
            //我要评价
            case R.id.tv_comment_btn:
                if (!mDao.isLogin()) {
                    startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 12);
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
//            case R.id.rl_building:
//                if (baseBean == null)
//                    return;
//
//                if (baseBean.effectId == null) {
//                    startActivity(new Intent(getActivity(), BuildingActivity.class).putExtra("HOUSES_URL", "").putExtra("DEV_ID", devId));
//                } else {
//                    if (baseBean.effectId.size() == 0) {
//                        startActivity(new Intent(getActivity(), BuildingActivity.class).putExtra("HOUSES_URL", "").putExtra("DEV_ID", devId));
//                    } else {
//                        if (baseBean != null && baseBean.distribution != null && baseBean.distribution.size() != 0) {
//                            startActivity(new Intent(getActivity(), BuildingActivity.class).putExtra("HOUSES_URL", baseBean.distribution.get(0).url).putExtra("DEV_ID", devId));
//                        } else {
//                            CustomerUtils.showTip(getActivity(), "暂无楼栋信息");
//                        }
//                    }
//                }
//
//                break;
            //计算器
            case R.id.fl_caculator:
                startActivity(new Intent(getActivity(), CalculatorActivity.class));
                break;
            //专家团队
//            case R.id.rl_houses_team:
//                if (baseBean == null)
//                    return;
//                startActivity(new Intent(getActivity(), ExportActivity.class).putExtra("BASE_BEAN", baseBean));
//                break;
            case R.id.tv_type:
                ((HousesAndTypeSimpleActivity) getActivity()).getTabLayout().getTabAt(1).select();
                break;
            case R.id.btnNavigation:
                if (mlocationClient != null) {
                    showLoading();
                    mlocationClient.startLocation();
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
                        //  购买优惠
                        FavorableSelectActivity.startActivity(HousesDetailSimpleFragment.this, baseBean, 11);
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
                            OrderDetailActivity.startActivity(HousesDetailSimpleFragment.this, resultCustomerIsOrder.orderCode, 3);
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10:
                    if (baseBean != null) {
                        mPresenter.requestHousesComment(baseBean.estateProjectId + "", "1");
                    }
                    break;
            }
        }
//        switch (requestCode) {
//            case 3:
//                // 订单详情页可能有支付操作
//            case 11:
//                //  购买优惠成功
//            case 12:
//                //  登录成功
//                mPresenter.checkVipFavorableStatus(devId);
//                break;
//        }
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
