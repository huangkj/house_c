package com.easylife.house.customer.ui.houses.housetype.housesTypeDetail;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ClubAdapter;
import com.easylife.house.customer.adapter.CommentListAdapter;
import com.easylife.house.customer.adapter.FavorableShowAdapter;
import com.easylife.house.customer.adapter.HousesTypeDetailOtherTypeAdapter;
import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.CommentUrlBean;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.HouseSearchBean;
import com.easylife.house.customer.bean.HouseTypeDetailBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.LoanResult;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.bean.ResultCustomerIsOrder;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.housepincontrol.HousePinControlActivity;
import com.easylife.house.customer.ui.houses.housesdetail.bookinghouse.BookingHouseActivity;
import com.easylife.house.customer.ui.houses.housesdetail.comment.CommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.housescomment.CommentPagerActivity;
import com.easylife.house.customer.ui.houses.housesdetail.housescomment.HousesCommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.pager.NetworkImageHolderView;
import com.easylife.house.customer.ui.mine.calculator.CalculatorActivity;
import com.easylife.house.customer.ui.mine.order.orderdetail.OrderDetailActivity;
import com.easylife.house.customer.ui.payment.favorable.FavorableSelectActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DialogUtil;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.util.LoanUtil;
import com.easylife.house.customer.util.MoneyUtils;
import com.easylife.house.customer.util.PriceUtil;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.util.StatusBarUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.FlowViewGroup;
import com.easylife.house.customer.view.SpaceItemDecoration;
import com.easylife.house.customer.view.anchor.ObservableScrollView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.blankj.utilcode.util.StringUtils.null2Length0;
import static com.easylife.house.customer.config.ItemSelectManager.HOUSE_TYPE_FLAG_TEXTSIZE;

/**
 * 户型详情页面
 */
@RuntimePermissions
public class HousesTypeDetailActivity extends MVPBaseActivity<HousesTypeDetailContract.View, HousesTypeDetailPresenter> implements
        HousesTypeDetailContract.View {

    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.tv_houses_name)
    TextView tvHousesName;
    @Bind(R.id.tvDevName)
    TextView tvDevName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.floviewgroup)
    FlowViewGroup floviewgroup;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_simple_value)
    TextView tvSimpleValue;
    @Bind(R.id.fl_caculator)
    FrameLayout flCaculator;
    @Bind(R.id.rl_detail_more)
    RelativeLayout rlDetailMore;
    //    @Bind(R.id.tv_project)
//    TextView tvProject;
//    @Bind(R.id.tv_project_name)
//    TextView tvProjectName;
//    @Bind(R.id.iv_more)
//    ImageView ivMore;
    @Bind(R.id.tv_bussiness)
    TextView tvBussiness;
    //    @Bind(R.id.tv_load)
//    TextView tvLoad;
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
    @Bind(R.id.tv_houses_comment)
    TextView tvHousesComment;
    @Bind(R.id.tv_houses_comment_value)
    TextView tvHousesCommentValue;
    @Bind(R.id.rl_houses_comment)
    RelativeLayout rlHousesComment;
    @Bind(R.id.comment_recycle)
    RecyclerView commentRecycle;
    @Bind(R.id.tv_comment_btn)
    TextView tvCommentBtn;
    @Bind(R.id.scrollView)
    ObservableScrollView scrollView;
    //    @Bind(R.id.rl_collect)
//    RelativeLayout rlCollect;
//    @Bind(R.id.rl_ask)
//    RelativeLayout rlAsk;
//    @Bind(R.id.tv_look)
//    TextView tvLook;
//    @Bind(R.id.tv_call)
//    TextView tvCall;
//    @Bind(R.id.tv_detail)
//    TextView tvDeatil;
    @Bind(R.id.tv_area_top)
    TextView tvAreaTop;
    //    @Bind(R.id.iv_collect)
//    CheckBox ivCollect;
    @Bind(R.id.houses_type_item)
    RelativeLayout houseTypeItem;
    @Bind(R.id.type_line)
    View typeLine;
    @Bind(R.id.btnSelectBuy)
    ButtonTouch btnSelectBuy;
    @Bind(R.id.tvDiscount)
    TextView tvDiscount;
    @Bind(R.id.layDiscount)
    LinearLayout layDiscount;
    @Bind(R.id.recycleFavorable)
    RecyclerView recycleFavorable;
    @Bind(R.id.recycleview_type)
    RecyclerView recycleviewType;
    @Bind(R.id.layFavorable)
    LinearLayout layFavorable;
    @Bind(R.id.llClub)
    LinearLayout llClub;
    //    @Bind(R.id.ll_bottom_type_detail)
//    LinearLayout ll_bottom_type_detail;
    @Bind(R.id.viewpager)
    ViewPager viewPager;


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

    @Bind(R.id.rl_ask)
    LinearLayout rlAsk;
    //    @Bind(R.id.rl_sub)
//    LinearLayout rlSub;
//
//
//    @Bind(R.id.rl_sub2)
//    LinearLayout rlSub2;
    @Bind(R.id.tv_look)
    TextView tvLook;
    @Bind(R.id.tv_call)
    TextView tvCall;

    @Bind(R.id.tv_call2)
    TextView tvCall2;


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

    /*
     * <!--iv_house tv_transParent tv_price tv_house_name  tv_area tv_address floviewgroup tv_room-->

     * */


    private List<View> listViews = null;
    private CommentListBean commentBean;
    private HousesDetailBaseBean baseBean;
    private List<String> images = new ArrayList<>();
    private HousesTypeBean.HouseLayoutDataBean housesTypeDetailBean;
    private LoginResult loginCache;
    private boolean isCollect;
    private String dev_id;
    private FavorableShowAdapter adapter;
    /**
     * 户型列表
     */
    private List<HousesTypeBean.HouseLayoutDataBean> allList;
    private HousesTypeDetailOtherTypeAdapter housesTypeAdapter;

    public static void startActivity(Activity activity, HousesTypeBean.HouseLayoutDataBean housesTypeDetailBean,
                                     HousesDetailBaseBean baseBean, boolean isCollect, int requestCode) {

        activity.startActivityForResult(new Intent(activity, HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", housesTypeDetailBean)
                .putExtra("BASE_BEAN", baseBean).putExtra("isCollect", isCollect), requestCode);

    }

    public static void startActivity(Activity activity, String devId, String houseName, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HousesTypeDetailActivity.class).putExtra("DEV_ID", devId)
                .putExtra("HOUSE_NAME", houseName), requestCode);

    }


    public void updateCoopStyle() {
        if (baseBean.coOp == 3) {
            bottomSimple.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
        } else {
            bottomSimple.setVisibility(View.GONE);
            bottom.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_houses_type_detail, null);
    }

    @Override
    protected void initView() {
        StatusBarUtils.setDarkMode(this);
//        StatusBarUtils.setTranslucentForImageView(activity, 0, rlTop);
        StatusBarUtils.setTranslucentForImageView2(activity, Color.argb(30, 0, 0, 0), rlTop);
        adapter = new FavorableShowAdapter(R.layout.item_favorable, null);
        recycleFavorable.setLayoutManager(new LinearLayoutManager(this));
        recycleFavorable.setAdapter(adapter);

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

        loginCache = dao.getLoginCache();
        if (loginCache != null) {
            showLoading();
            mPresenter.collectHouseList(loginCache.userCode, loginCache.token, "1");
        }
        isCollect = getIntent().getBooleanExtra("isCollect", false);
        housesTypeDetailBean = (HousesTypeBean.HouseLayoutDataBean) getIntent().getSerializableExtra("HOUSE_TYPE_DETAIL");
        baseBean = (HousesDetailBaseBean) getIntent().getSerializableExtra("BASE_BEAN");
        if (baseBean == null) {
            dev_id = getIntent().getStringExtra("DEV_ID");


        } else {
            dev_id = baseBean.devId;
//            mPresenter.requestHousesComment(baseBean.estateProjectId + "", "1");
//            if (isCollect && baseBean != null) {
//                mPresenter.requestHousesDetail(baseBean.devId);//楼盘详情基础信息
//            }
        }
        mPresenter.requestHousesDetail(dev_id);//楼盘详情基础信息
//        mPresenter.requestMainType(dev_id);

        if (housesTypeDetailBean != null && baseBean != null) {
//            String houseName = getIntent().getStringExtra("HOUSE_NAME");
//            mPresenter.requestTypeDetail(dev_id, houseName);
            setHouseTypeData(housesTypeDetailBean);
            setHouseBaseData(baseBean);
            updateCoopStyle();
            setBelongHouseData();

        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HousesTypeDetailActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleviewType.setLayoutManager(linearLayoutManager);
        housesTypeAdapter = new HousesTypeDetailOtherTypeAdapter(R.layout.houses_item_type, null);
        recycleviewType.setAdapter(housesTypeAdapter);
        recycleviewType.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(20), LinearLayout.HORIZONTAL));
        recycleviewType.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (allList.size() != 0) {
                    HousesTypeBean.HouseLayoutDataBean houseLayoutDataBean = allList.get(position);

                    startActivity(new Intent(HousesTypeDetailActivity.this, HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
                            .putExtra("BASE_BEAN", baseBean));


//                    if (baseBean.coOp == 3) {
//                        startActivity(new Intent(HousesTypeDetailActivity.this, HousesTypeDetailSimpleActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
//                                .putExtra("BASE_BEAN", baseBean));
//
//                    } else {
//                        startActivity(new Intent(HousesTypeDetailActivity.this, HousesTypeDetailActivity.class).putExtra("HOUSE_TYPE_DETAIL", houseLayoutDataBean)
//                                .putExtra("BASE_BEAN", baseBean));
//
//                    }

                }
            }
        });


        setOnShareListener(new OnShareListener() {
            @Override
            public void onShare() {
                mPresenter.shareIntegration();
            }
        });
//        mPresenter.getDevFavorable(dev_id);
//        mPresenter.getVipFavorable(dev_id);
    }

    @Override
    protected void setActionBarDetail() {
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void tryRequestData() {
        if (loginCache != null) {
            showLoading();
            mPresenter.collectHouseList(loginCache.userCode, loginCache.token, "1");
        }
    }

    @Override
    public void showTip(String msg) {

    }

    private ResultCustomerIsOrder resultCustomerIsOrder;

    @Override
    public void checkVipFavorableStatus(ResultCustomerIsOrder resultCustomerIsOrder) {
        this.resultCustomerIsOrder = resultCustomerIsOrder;
        if (ResultCustomerIsOrder.STATUS_COMPLETE.equals(resultCustomerIsOrder.status)) {
            btnSelectBuy.setBackgroundResource(R.drawable.orange_rect_sold_bg);
            btnSelectBuy.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private PubTipDialog dialogBuy;

    /**
     * 显示购买优惠提示弹窗
     */
    private void showTipBugFavorable() {
        if (dialogBuy == null) {
            dialogBuy = new PubTipDialog(activity, new PubTipDialog.InsideListener() {
                @Override
                public void note(boolean isOK) {
                    if (isOK) {
                        //  购买优惠
                        FavorableSelectActivity.startActivity(activity, baseBean, 11);
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
            dialogPayContinue = new PubTipDialog(activity, new PubTipDialog.InsideListener() {
                @Override
                public void note(boolean isOK) {
                    if (isOK) {
                        //  跳转订单详情
                        if (resultCustomerIsOrder != null)
                            OrderDetailActivity.startActivity(activity, resultCustomerIsOrder.orderCode, 3);
                    }
                }
            });
        }
        dialogPayContinue.showPayTip(R.layout.pub_toast_pay_continue);
    }

    @OnClick({R.id.fl_caculator, R.id.rl_houses_comment, R.id.tv_comment_btn, R.id.iv_collect, R.id.iv_collect2, R.id.rl_ask,
            R.id.rl_collect, R.id.tv_call2, R.id.rl_collect2,
            R.id.tv_look, R.id.tv_call, R.id.btnSelectBuy, R.id.iv_back, R.id.iv_share, R.id.ivRecommend,
            R.id.iv_back_banner, R.id.iv_share_banner, R.id.ivRecommend_banner,})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tvSub:
//                if (!dao.isLogin()) {
//                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 0);
//                    return;
//                }
//                if (baseBean == null)
//                    return;
//                startActivity(new Intent(this, NowSubActivity.class).putExtra("MORE_INFO", baseBean));
//                break;
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_back_banner:
                finish();
                break;

            case R.id.iv_share_banner:
                if (housesTypeDetailBean != null && baseBean != null) {
                    if (housesTypeDetailBean.houseImg == null || housesTypeDetailBean.houseImg.size() == 0) {
                        share(housesTypeDetailBean.houseShare + "?devId=" + baseBean.devId + "&houseName=" + housesTypeDetailBean.houseName
                                        + "&projectId=" + baseBean.estateProjectId + "&coOp=" + baseBean.coOp,
                                housesTypeDetailBean.structure + " " + housesTypeDetailBean.fArea + " " + baseBean.addressDistrict + " " +
                                        housesTypeDetailBean.avgprice + "元/㎡,", housesTypeDetailBean.devName, ",好生活线上购买享受更多优惠",
                                housesTypeDetailBean.tag, "");
                    } else {
                        share(housesTypeDetailBean.houseShare + "?devId=" + baseBean.devId + "&houseName=" + housesTypeDetailBean.houseName
                                        + "&projectId=" + baseBean.estateProjectId + "&coOp=" + baseBean.coOp,
                                housesTypeDetailBean.structure + " " + housesTypeDetailBean.fArea + " " + baseBean.addressDistrict + " " +
                                        housesTypeDetailBean.avgprice + "元/㎡,", housesTypeDetailBean.devName, ",好生活线上购买享受更多优惠",
                                housesTypeDetailBean.tag, housesTypeDetailBean.houseImg.get(0).url);
                    }
                }
                break;

            case R.id.iv_share:
                if (housesTypeDetailBean != null && baseBean != null) {
                    if (housesTypeDetailBean.houseImg == null || housesTypeDetailBean.houseImg.size() == 0) {
                        share(housesTypeDetailBean.houseShare + "?devId=" + baseBean.devId + "&houseName=" + housesTypeDetailBean.houseName
                                        + "&projectId=" + baseBean.estateProjectId + "&coOp=" + baseBean.coOp,
                                housesTypeDetailBean.structure + " " + housesTypeDetailBean.fArea + " " + baseBean.addressDistrict + " " +
                                        housesTypeDetailBean.avgprice + "元/㎡,", housesTypeDetailBean.devName, ",好生活线上购买享受更多优惠",
                                housesTypeDetailBean.tag, "");
                    } else {
                        share(housesTypeDetailBean.houseShare + "?devId=" + baseBean.devId + "&houseName=" + housesTypeDetailBean.houseName
                                        + "&projectId=" + baseBean.estateProjectId + "&coOp=" + baseBean.coOp,
                                housesTypeDetailBean.structure + " " + housesTypeDetailBean.fArea + " " + baseBean.addressDistrict + " " +
                                        housesTypeDetailBean.avgprice + "元/㎡,", housesTypeDetailBean.devName, ",好生活线上购买享受更多优惠",
                                housesTypeDetailBean.tag, housesTypeDetailBean.houseImg.get(0).url);
                    }
                }
                break;
            case R.id.ivRecommend:
                // 推荐有礼
                if (dao.isLogin()) {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(dao.getCustomer().username, dao.getCustomer().phone));
                } else {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(null, null));
                }
                break;

            case R.id.ivRecommend_banner:
                // 推荐有礼
                if (dao.isLogin()) {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(dao.getCustomer().username, dao.getCustomer().phone));
                } else {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(null, null));
                }
                break;


            //计算器
            case R.id.fl_caculator:
                startActivity(new Intent(this, CalculatorActivity.class));
                break;
            //楼盘评价
            case R.id.rl_houses_comment:
                startActivity(new Intent(HousesTypeDetailActivity.this, HousesCommentActivity.class).putExtra("PROJECTID", baseBean.estateProjectId));
                break;
            //我要评价
            case R.id.tv_comment_btn:
                String AVGscores = "0";
                if (commentBean != null && !TextUtils.isEmpty(commentBean.AVGScore + "")) {
                    AVGscores = commentBean.AVGScore + "";
                }
                startActivityForResult(new Intent(this, CommentActivity.class).putExtra("projectId", baseBean.estateProjectId), 10);
                break;
            //收藏
            case R.id.iv_collect:
                collect(ivCollect);
                break;
            case R.id.iv_collect2:
                collect(ivCollect2);
                break;
            //咨询
            case R.id.rl_ask:
                if (dao.isLogin()) {
//                    startActivity(new Intent(this, ChatActivity.class).putExtra("userId", EaseConstant.IM_CLIENT_ID));
                } else {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 12);
                }
                break;
            //带看
            case R.id.tv_look:

                if (!dao.isLogin()) {
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 1);
                    return;
                }

                startActivity(new Intent(this, BookingHouseActivity.class).putExtra("BASE_BEAN", baseBean));
                break;
            //打电话
            case R.id.tv_call:
                call(baseBean.contactPhone);
                break;
            case R.id.tv_call2:
                call(baseBean.contactPhone);
                break;
            //查看详情
//            case R.id.tv_detail:
//                if (isCollect) {
//                    HouseDetailActivity.startActivity(this, baseBean.devId, false, 0);
//                } else {
//                    EventBus.getDefault().post(new MessageEvent(UPDATE_HOUSES_TYPE_DETAIL));
//                    finish();
//                }
//                break;
            case R.id.btnSelectBuy:
                //   选房购房
                if (dao.isLogin()) {
                    if (resultCustomerIsOrder != null) {
                        if (ResultCustomerIsOrder.STATUS_COMPLETE.equals(resultCustomerIsOrder.status)) {
                            // 纯企业付费  或者  会员付费已支付成功
                            HousePinControlActivity.startActivity(this, dev_id, 0);
                        } else if (ResultCustomerIsOrder.STATUS_PAY_PART.equals(resultCustomerIsOrder.status)) {
                            //  提示去订单详情页继续支付
                            showTipPayContinue();
                        } else if (ResultCustomerIsOrder.STATUS_NOT.equals(resultCustomerIsOrder.status)) {
                            // 提示购买
                            showTipBugFavorable();
                        }
                    } else {
                        HousePinControlActivity.startActivity(this, dev_id, 0);
                    }
                } else {
                    LoginByVerifyCodeActivity.startActivity(activity, 12);
                }
                break;
        }
    }

    private void collect(CheckBox ivCollect) {
        if (dao.isLogin()) {
            if (ivCollect.isChecked()) {
                if (loginCache != null) {
                    mPresenter.collectHouse(baseBean.devId, baseBean.devName, loginCache.userCode, loginCache.token, "1", housesTypeDetailBean.houseName);
                }
            } else {
                ivCollect.setChecked(false);
                if (loginCache != null) {
                    mPresenter.delCollectHouse(baseBean.devId, baseBean.devName, loginCache.userCode, loginCache.token, "1", housesTypeDetailBean.houseName);
                }
//                        new AlertDialog.Builder(HousesTypeDetailActivity.this).setTitle("提示")
//                                .setMessage("是否取消收藏 ")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (loginCache != null) {
//                                            mPresenter.delCollectHouse(baseBean.devId, loginCache.userCode, loginCache.token, "1", housesTypeDetailBean.houseName);
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
            startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 12);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            loginCache = dao.getLoginCache();
            if (loginCache != null) {
                mPresenter.collectHouseList(loginCache.userCode, loginCache.token, "1");
            }
        }

        if (requestCode == 10) {
            if (baseBean != null) {
                mPresenter.requestHousesComment(baseBean.estateProjectId + "", "1");
            }
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3:
                    // 订单继续支付成功
                case 11:
                    //  购买优惠成功
                case 12:
                    //  登录成功
                    mPresenter.checkVipFavorableStatus(dev_id);
                    break;
            }
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
                        HousesTypeDetailActivityPermissionsDispatcher.jumpCallPhoneWithCheck(HousesTypeDetailActivity.this, phone);
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
        HousesTypeDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Override
    public void showBaseData(HouseTypeDetailBean housesTypeBean) {
        if (housesTypeBean != null) {
            housesTypeDetailBean = new HousesTypeBean.HouseLayoutDataBean();
            if (!TextUtils.isEmpty(housesTypeBean.fArea)) {
                housesTypeDetailBean.fArea = housesTypeBean.fArea;
            } else {
                housesTypeDetailBean.fArea = "价格待定";
            }

            // 1.  判断是否明房源 --- 是否显示按钮（初始显示状态为灰色按钮）
            if ("1".equals(baseBean.isTransparent)) {
//                layFavorable.setVisibility(View.VISIBLE);
//                llClub.setVisibility(View.VISIBLE);
//                btnSelectBuy.setVisibility(View.VISIBLE);

                // 2. 是否登录
                if (dao.isLogin()) {
                    // 3. coOp ,
                    if (baseBean.coOp != Order.CoOp.ENTERPRISE.code) {
                        // 纯企业付费，点击直接跳转房源销控，显示橘色边框
                        btnSelectBuy.setBackgroundResource(R.drawable.orange_rect_sold_bg);
                        btnSelectBuy.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        // 4.会员付费类 - 需判断是否认筹支付完成，否则还是灰色
                        mPresenter.checkVipFavorableStatus(dev_id);
                    }
                }
            } else {
                btnSelectBuy.setVisibility(View.GONE);
            }

            housesTypeDetailBean.introduce = housesTypeBean.introduce;
            housesTypeDetailBean.houseName = housesTypeBean.houseName;
            housesTypeDetailBean.houseImg = housesTypeBean.houseImg;
            housesTypeDetailBean.tag = housesTypeBean.tag;
            housesTypeDetailBean.avgprice = housesTypeBean.avgprice;
            housesTypeDetailBean.createTime = housesTypeBean.createTime;
            housesTypeDetailBean.gArea = housesTypeBean.gArea;
            housesTypeDetailBean.uArea = housesTypeBean.uArea;
            housesTypeDetailBean.houseShare = housesTypeBean.houseShare;
            housesTypeDetailBean.salesStatus = housesTypeBean.salesStatus;
            housesTypeDetailBean.saleNum = housesTypeBean.saleNum;
            housesTypeDetailBean.price = housesTypeBean.price;
            housesTypeDetailBean.structure = housesTypeBean.structure;
            housesTypeDetailBean.devId = dev_id;
            housesTypeDetailBean.projectID = housesTypeBean.houseId;
            housesTypeDetailBean.buildCode = housesTypeBean.buildCode;
            housesTypeDetailBean.isTransparent = housesTypeBean.isTransparent;
            housesTypeDetailBean.houseId = housesTypeBean.houseId;


            setHouseTypeData(housesTypeDetailBean);
        }


    }

    public void setHouseBaseData(HousesDetailBaseBean baseBean) {
        // 1.  判断是否明房源 --- 是否显示按钮（初始显示状态为灰色按钮）
        if ("1".equals(baseBean.isTransparent)) {
//            llClub.setVisibility(View.VISIBLE);
//            btnSelectBuy.setVisibility(View.VISIBLE);

            // 2. 是否登录
            if (dao.isLogin()) {
                // 3. coOp ,
                if (baseBean.coOp != Order.CoOp.ENTERPRISE.code) {
                    // 纯企业付费，点击直接跳转房源销控，显示橘色边框
                    btnSelectBuy.setBackgroundResource(R.drawable.orange_rect_sold_bg);
                    btnSelectBuy.setTextColor(getResources().getColor(R.color.white));
                } else {
                    // 4.会员付费类 - 需判断是否认筹支付完成，否则还是灰色
                    mPresenter.checkVipFavorableStatus(dev_id);
                }
            }
        } else {
            btnSelectBuy.setVisibility(View.GONE);
        }
    }

    public void setHouseTypeData(final HousesTypeBean.HouseLayoutDataBean housesTypeDetailBean) {
        tvAreaTop.setText(housesTypeDetailBean.fArea + "㎡");
        tvHousesName.setText(housesTypeDetailBean.structure);
        tvDevName.setText(housesTypeDetailBean.structure + housesTypeDetailBean.fArea + "㎡");
        float price = 0;
        float firstPrice = 0;
        if (!TextUtils.isEmpty(housesTypeDetailBean.avgprice)) {
            price = Float.parseFloat(housesTypeDetailBean.avgprice) * (Float.parseFloat(housesTypeDetailBean.fArea));
            price = price / 10000;
        }
        if (0 == price) {
            houseTypeItem.setVisibility(View.GONE);
            typeLine.setVisibility(View.GONE);
            tvPrice.setText("价格待定");

        } else {
            tvPrice.setText(Math.round(price) + "万起");
            typeLine.setVisibility(View.VISIBLE);
            houseTypeItem.setVisibility(View.VISIBLE);

            //商贷参考UI及计算
            if (price != 0) {
                firstPrice = Float.parseFloat(housesTypeDetailBean.avgprice) * (Float.parseFloat(housesTypeDetailBean.fArea)) * 0.3f / 10000;
            }
            tvTotal.setText(Math.round(firstPrice) + "万");
            LoanResult loanResult = LoanUtil.getLoanResult(true, DoubleFomat.format2(price - firstPrice) + "", "30", "0.049");
            if (loanResult != null) {
                tvMoneyMonth.setText(Math.round(loanResult.paymentMonth) + "");
                tvLoanTotal.setText(Math.round(price - firstPrice) + "万");
            }
        }

        String[] tags = housesTypeDetailBean.tag.split(",");
        if (tags.length > 3) {
            for (int i = 0; i < 3; i++) {
                TextView tvFeature = (TextView) LayoutInflater.from(this).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
                tvFeature.setText(tags[i]);
                floviewgroup.addView(tvFeature);
            }

        } else {

            for (int i = 0; i < tags.length; i++) {
                TextView tvFeature = (TextView) LayoutInflater.from(this).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
                tvFeature.setText(tags[i]);
                floviewgroup.addView(tvFeature);
            }
        }


        tvSimpleValue.setText(housesTypeDetailBean.introduce);
//        tvProjectName.setText(housesTypeDetailBean.devName);

        for (int i = 0; i < housesTypeDetailBean.houseImg.size(); i++) {
            images.add(housesTypeDetailBean.houseImg.get(i).url);
        }

        convenientBanner.setBackgroundColor(getResources().getColor(R.color.Gray));
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

                startActivity(new Intent(HousesTypeDetailActivity.this, PagerDetailActivity.class).putExtra("PAGER_BEAN", housesTypeDetailBean));
            }
        });
        convenientBanner.setCanLoop(false);

    }

    @Override
    public void showHousesComment(final CommentListBean commentBean) {
        cancelLoading();
        this.commentBean = commentBean;
        if (commentBean == null || commentBean.reviews.size() == 0) {
            tvHousesCommentValue.setText(null);
        } else {
            tvHousesCommentValue.setText("(" + commentBean.reviews.size() + ")");
        }
        List<CommentListBean.ReviewsBean> list = new ArrayList<>();
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

                startActivity(new Intent(HousesTypeDetailActivity.this, CommentPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
                Log.e("postion", position + "");
            }

            //跳转楼盘评价列表
            @Override
            public void setJumpCommentListClick() {
                startActivity(new Intent(HousesTypeDetailActivity.this, HousesCommentActivity.class).putExtra("PROJECTID", baseBean.estateProjectId));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(HousesTypeDetailActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setSmoothScrollbarEnabled(true);
        commentRecycle.setLayoutManager(layoutManager);
        commentRecycle.setAdapter(commentListAdapter);
    }

    @Override
    public void showCollect() {
        CustomerUtils.showTip(this, "收藏成功");
    }

    @Override
    public void showCollectList(List<CollectionListBean> collectionList) {
        cancelLoading();
        if (collectionList != null) {
            List<String> typeCollectList = new ArrayList<>();
            for (CollectionListBean bean : collectionList) {
                typeCollectList.add(bean.houseName);
            }
            if (typeCollectList.contains(housesTypeDetailBean.houseName)) {

                if (baseBean.coOp == 3) {
                    ivCollect2.setChecked(true);
                } else {
                    ivCollect.setChecked(true);
                }

            } else {

                if (baseBean.coOp == 3) {
                    ivCollect2.setChecked(false);
                } else {
                    ivCollect.setChecked(false);
                }

            }
        }
    }

    @Override
    public void showHousesDetail(HousesDetailBaseBean baseBean) {
        if (baseBean != null) {
            this.baseBean = baseBean;
            setBaseDate(baseBean);
            setBelongHouseData();
            setTypeListData();
        }
    }

    private void setTypeListData() {
        List<HouseSearchBean.EstateDevBuildHousesBean> estateDevBuildHouses = baseBean.estateDevBuildHouses;

        allList = new ArrayList<>();
        Iterator<HouseSearchBean.EstateDevBuildHousesBean> iterator = estateDevBuildHouses.iterator();

        while (iterator.hasNext()) {
            HouseSearchBean.EstateDevBuildHousesBean next = iterator.next();
            if (next.houseId.equals(housesTypeDetailBean.houseId)) {
                iterator.remove();
            }
        }


        for (HouseSearchBean.EstateDevBuildHousesBean bean :
                estateDevBuildHouses) {

            HousesTypeBean.HouseLayoutDataBean houseLayoutDataBean = new HousesTypeBean.HouseLayoutDataBean();

            /*
            *
    public String avgprice;
    public String introduce;
    public String buildCode;
    public String devName;
    public String structure;
    public String fArea;
    public String houseName;
    public String devId;
    public String houseShare;
    public String uArea;
    public String createTime;
    public String price;
    public String salesStatus;
    public String tag;
    public String gArea;
    public String projectID;
    public String saleNum;
    public String houseId;
    public String isTransparent;

    public List<TypeImageBean> houseImg;
            *
            * */

            houseLayoutDataBean.avgprice = bean.avgprice;
            houseLayoutDataBean.introduce = bean.introduce;
            houseLayoutDataBean.buildCode = bean.buildCode;
//                houseLayoutDataBean.devName = bean.devName;
            houseLayoutDataBean.structure = bean.structure;
            houseLayoutDataBean.fArea = bean.fArea;
            houseLayoutDataBean.houseName = bean.houseName;
//                houseLayoutDataBean.devId = bean.devId;
//                houseLayoutDataBean.houseShare = bean.houseShare;
            houseLayoutDataBean.uArea = bean.uArea;
            houseLayoutDataBean.createTime = bean.createTime;
            houseLayoutDataBean.price = bean.price;
            houseLayoutDataBean.salesStatus = bean.salesStatus;
            houseLayoutDataBean.tag = bean.tag;
            houseLayoutDataBean.gArea = bean.gArea;
//                houseLayoutDataBean.projectID = bean.projectID;
//                houseLayoutDataBean.saleNum = bean.saleNum;
            houseLayoutDataBean.houseId = bean.houseId;
//                houseLayoutDataBean.isTransparent = bean.isTransparent;
            houseLayoutDataBean.tag = bean.tag;
            houseLayoutDataBean.houseImg = bean.houseImg;

            allList.add(houseLayoutDataBean);

        }


        housesTypeAdapter.setNewData(estateDevBuildHouses);
        if (allList.size() == 0) {
//            rlMain.setVisibility(View.GONE);
        }
    }

    public void setBaseDate(HousesDetailBaseBean baseBean) {
        String houseName = getIntent().getStringExtra("HOUSE_NAME");
        mPresenter.requestHousesComment(baseBean.estateProjectId + "", "1");
        mPresenter.requestTypeDetail(dev_id, houseName);


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

    @Override
    public void showDelCollectSucc() {
        CustomerUtils.showTip(this, "取消收藏");
    }

    @Override
    public void showFavorableDev(DevFavorable devFavorable) {
        if (devFavorable == null)
            return;
        if (devFavorable.discount != null && ("1".equals(devFavorable.discount.discountAllType) || "1".equals(devFavorable.discount.discountDaiType))) {
            layFavorable.setVisibility(View.VISIBLE);
            llClub.setVisibility(View.VISIBLE);
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
            llClub.setVisibility(View.VISIBLE);
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
                            FavorableSelectActivity.startActivity(HousesTypeDetailActivity.this, baseBean, favorableVips.get(finalI).id, 11);
                        } else {
                            LoginByVerifyCodeActivity.startActivity(HousesTypeDetailActivity.this, 12);
                        }
                    }
                });

            }
            ClubAdapter clubAdapter = new ClubAdapter(listViews);
            viewPager.setAdapter(clubAdapter);
        }
    }

    @Override
    public void showFial(String msg) {
        cancelLoading();
    }

    @Override
    public void showTipDialog(String tip) {
        DialogUtil.showTip(this, tip);
    }


    /**
     * 户型列表
     */
    @Override
    public void showMainType(List<HousesTypeBean> beanList) {
//        rlMain.setVisibility(View.VISIBLE);
   /*     allList = new ArrayList<>();
        for (int i = 0; i < beanList.size(); i++) {
            HousesTypeBean housesTypeBean = beanList.get(i);
            List<HousesTypeBean.HouseLayoutDataBean> houseLayoutData = housesTypeBean.houseLayoutData;
            Iterator<HousesTypeBean.HouseLayoutDataBean> iterator = houseLayoutData.iterator();
            while (iterator.hasNext()) {
                HousesTypeBean.HouseLayoutDataBean next = iterator.next();
                if (next.houseId.equals(housesTypeDetailBean.houseId)) {
                    iterator.remove();
                }
            }

            allList.addAll(houseLayoutData);
        }
        housesTypeAdapter.setNewData(allList);
        if (allList.size() == 0) {
//            rlMain.setVisibility(View.GONE);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isCollect) {
            setResult(RESULT_OK);
        }
    }
}
