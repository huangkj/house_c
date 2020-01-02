package com.easylife.house.customer.ui.houses.housetype.housesTypeDetailSimple;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.CommentListAdapter;
import com.easylife.house.customer.adapter.FavorableShowAdapter;
import com.easylife.house.customer.bean.CollectionListBean;
import com.easylife.house.customer.bean.CommentListBean;
import com.easylife.house.customer.bean.CommentUrlBean;
import com.easylife.house.customer.bean.DevFavorable;
import com.easylife.house.customer.bean.FavorableVip;
import com.easylife.house.customer.bean.HouseTypeDetailBean;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.HousesTypeBean;
import com.easylife.house.customer.bean.LoanResult;
import com.easylife.house.customer.bean.LoginResult;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.bean.ResultCustomerIsOrder;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.HousesAndTypeActivity;
import com.easylife.house.customer.ui.houses.housepincontrol.HousePinControlActivity;
import com.easylife.house.customer.ui.houses.housesdetail.comment.CommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.housescomment.CommentPagerActivity;
import com.easylife.house.customer.ui.houses.housesdetail.housescomment.HousesCommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.pager.NetworkImageHolderView;
import com.easylife.house.customer.ui.houses.housetype.housesTypeDetail.PagerDetailActivity;
import com.easylife.house.customer.ui.mine.calculator.CalculatorActivity;
import com.easylife.house.customer.ui.mine.order.orderdetail.OrderDetailActivity;
import com.easylife.house.customer.ui.payment.favorable.FavorableSelectActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.util.LoanUtil;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.FlowViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_HOUSES_TYPE_DETAIL;

/**
 * 户型详情页面
 */
@RuntimePermissions
public class HousesTypeDetailSimpleActivity extends MVPBaseActivity<HousesTypeDetailSimpleContract.View, HousesTypeDetailSimplePresenter> implements
        HousesTypeDetailSimpleContract.View {

    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.tv_houses_name)
    TextView tvHousesName;
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
    @Bind(R.id.tv_project)
    TextView tvProject;
    @Bind(R.id.tv_project_name)
    TextView tvProjectName;
    @Bind(R.id.iv_more)
    ImageView ivMore;
    @Bind(R.id.tv_bussiness)
    TextView tvBussiness;
    //    @Bind(R.id.tv_load)
//    TextView tvLoad;
    @Bind(R.id.line)
    View line;
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
    ScrollView scrollView;
    @Bind(R.id.rl_collect)
    RelativeLayout rlCollect;
    @Bind(R.id.tv_call)
    TextView tvCall;
    @Bind(R.id.tv_detail)
    TextView tvDeatil;
    @Bind(R.id.tv_area_top)
    TextView tvAreaTop;
    @Bind(R.id.iv_collect)
    CheckBox ivCollect;
    @Bind(R.id.houses_type_item)
    RelativeLayout houseTypeItem;
    @Bind(R.id.type_line)
    View typeLine;
    @Bind(R.id.btnGetFavorable)
    ButtonTouch btnGetFavorable;
    @Bind(R.id.btnSelectBuy)
    ButtonTouch btnSelectBuy;
    @Bind(R.id.vOffset)
    View vOffset;
    @Bind(R.id.tvDiscount)
    TextView tvDiscount;
    @Bind(R.id.layDiscount)
    LinearLayout layDiscount;
    @Bind(R.id.recycleFavorable)
    RecyclerView recycleFavorable;
    @Bind(R.id.tvVip)
    TextView tvVip;
    @Bind(R.id.tv_collect)
    TextView tvCollect;
    @Bind(R.id.layVip)
    LinearLayout layVip;
    @Bind(R.id.ll_bottom_type_detail_simple)
    LinearLayout ll_bottom_type_detail_simple;
    @Bind(R.id.layFavorable)
    LinearLayout layFavorable;


    private CommentListBean commentBean;
    private HousesDetailBaseBean baseBean;
    private List<String> images = new ArrayList<>();
    private HousesTypeBean.HouseLayoutDataBean housesTypeDetailBean;
    private LoginResult loginCache;
    private boolean isCollect;
    private String dev_id;
    private FavorableShowAdapter adapter;

    public static void startActivity(Activity activity, HousesTypeBean.HouseLayoutDataBean housesTypeDetailBean,
                                     HousesDetailBaseBean baseBean, boolean isCollect, int requestCode) {

        activity.startActivityForResult(new Intent(activity, HousesTypeDetailSimpleActivity.class).putExtra("HOUSE_TYPE_DETAIL", housesTypeDetailBean)
                .putExtra("BASE_BEAN", baseBean).putExtra("isCollect", isCollect), requestCode);

    }

    public static void startActivity(Activity activity, String devId, String houseName, int requestCode) {
        activity.startActivityForResult(new Intent(activity, HousesTypeDetailSimpleActivity.class).putExtra("DEV_ID", devId)
                .putExtra("HOUSE_NAME", houseName), requestCode);

    }

    ;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_houses_type_detail_simple, null);
    }

    @Override
    protected void initView() {

        adapter = new FavorableShowAdapter(R.layout.item_favorable, null);
        recycleFavorable.setLayoutManager(new LinearLayoutManager(this));
        recycleFavorable.setAdapter(adapter);

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
            mPresenter.requestHousesDetail(dev_id);//楼盘详情基础信息

        } else {
            dev_id = baseBean.devId;
            mPresenter.requestHousesComment(baseBean.estateProjectId + "", "1");
//            if (isCollect && baseBean != null) {
//                mPresenter.requestHousesDetail(baseBean.devId);//楼盘详情基础信息
//            }
        }


        if (housesTypeDetailBean == null) {
            String houseName = getIntent().getStringExtra("HOUSE_NAME");
            mPresenter.requestTypeDetail(dev_id, houseName);
        } else {
            setHouseTypeData(housesTypeDetailBean);
        }

        mPresenter.getDevFavorable(dev_id);
        mPresenter.getVipFavorable(dev_id);
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
            btnSelectBuy.setBackgroundResource(R.drawable.shape_orange_white_3);
            btnSelectBuy.setTextColor(getResources().getColor(R.color.gradient_end));
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

    @OnClick({R.id.fl_caculator, R.id.btnGetFavorable, R.id.rl_houses_comment, R.id.tv_comment_btn, R.id.iv_collect, R.id.tv_call, R.id.tv_detail, R.id.btnSelectBuy, R.id.iv_back, R.id.iv_share, R.id.ivRecommend
            , R.id.ll_bottom_type_detail_simple})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
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
            case R.id.btnGetFavorable:
                //   获取优惠
                if (dao.isLogin()) {
                    FavorableSelectActivity.startActivity(this, baseBean, 0);
                } else {
                    LoginByVerifyCodeActivity.startActivity(activity, 12);
                }
                break;
            //计算器
            case R.id.fl_caculator:
                startActivity(new Intent(this, CalculatorActivity.class));
                break;
            //楼盘评价
            case R.id.rl_houses_comment:
                startActivity(new Intent(HousesTypeDetailSimpleActivity.this, HousesCommentActivity.class).putExtra("PROJECTID", baseBean.estateProjectId));
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
                if (dao.isLogin()) {
                    if (ivCollect.isChecked()) {
                        tvCollect.setText("已收藏");
                        if (loginCache != null) {
                            mPresenter.collectHouse(baseBean.devId, baseBean.devName, loginCache.userCode, loginCache.token, "1", housesTypeDetailBean.houseName);
                        }
                    } else {
                        tvCollect.setText("收藏");
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
                    tvCollect.setText("收藏");
                    startActivityForResult(new Intent(this, LoginByVerifyCodeActivity.class), 12);
                }
                break;
            //打电话
            case R.id.tv_call:
                call(baseBean.hotline);
                break;
            //查看详情
            case R.id.tv_detail:
                if (isCollect) {
                    HousesAndTypeActivity.startActivity(this, baseBean.devId, false, 0);
                } else {
                    EventBus.getDefault().post(new MessageEvent(UPDATE_HOUSES_TYPE_DETAIL));
                    finish();
                }
                break;
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
                .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HousesTypeDetailSimpleActivityPermissionsDispatcher.jumpCallPhoneWithCheck(HousesTypeDetailSimpleActivity.this, phone);
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
        HousesTypeDetailSimpleActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
                layFavorable.setVisibility(View.VISIBLE);
                btnSelectBuy.setVisibility(View.VISIBLE);

                if (btnGetFavorable.getVisibility() == View.VISIBLE) {
                    vOffset.setVisibility(View.VISIBLE);
                }

                // 2. 是否登录
                if (dao.isLogin()) {
                    // 3. coOp ,
                    if (baseBean.coOp != Order.CoOp.ENTERPRISE.code) {
                        // 纯企业付费，点击直接跳转房源销控，显示橘色边框
                        btnSelectBuy.setBackgroundResource(R.drawable.shape_orange_white_3);
                        btnSelectBuy.setTextColor(getResources().getColor(R.color.gradient_end));
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

            setHouseTypeData(housesTypeDetailBean);
        }


    }

    public void setHouseTypeData(final HousesTypeBean.HouseLayoutDataBean housesTypeDetailBean) {
        tvAreaTop.setText(housesTypeDetailBean.fArea + "㎡");
        tvHousesName.setText(housesTypeDetailBean.structure);
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
            tvPrice.setText(Math.round(price) + "万/套起");
            typeLine.setVisibility(View.VISIBLE);
            houseTypeItem.setVisibility(View.VISIBLE);

            //商贷参考UI及计算
            if (price != 0) {
                firstPrice = Float.parseFloat(housesTypeDetailBean.avgprice) * (Float.parseFloat(housesTypeDetailBean.fArea)) * 0.3f / 10000;
            }
            tvTotal.setText(Math.round(firstPrice) + "");
            LoanResult loanResult = LoanUtil.getLoanResult(true, DoubleFomat.format2(price - firstPrice) + "", "30", "0.049");
            if (loanResult != null) {
                tvMoneyMonth.setText(Math.round(loanResult.paymentMonth) + "");
                tvLoanTotal.setText(Math.round(price - firstPrice) + "");
            }
        }
        // 是否明源
        if ("1".equals(housesTypeDetailBean.isTransparent)) {
            layFavorable.setVisibility(View.VISIBLE);
            btnSelectBuy.setVisibility(View.VISIBLE);

            if (btnGetFavorable.getVisibility() == View.VISIBLE) {
                vOffset.setVisibility(View.VISIBLE);
            }
        } else {
            btnSelectBuy.setVisibility(View.GONE);
        }

        String[] tags = housesTypeDetailBean.tag.split(",");
        for (int i = 0; i < tags.length; i++) {
            TextView tvFeature = (TextView) LayoutInflater.from(this).inflate(R.layout.houses_detail_flow_item, floviewgroup, false);
            tvFeature.setText(tags[i]);
            floviewgroup.addView(tvFeature);
        }

        tvSimpleValue.setText(housesTypeDetailBean.introduce);
        tvProjectName.setText(housesTypeDetailBean.devName);

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

                startActivity(new Intent(HousesTypeDetailSimpleActivity.this, PagerDetailActivity.class).putExtra("PAGER_BEAN", housesTypeDetailBean));
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

                startActivity(new Intent(HousesTypeDetailSimpleActivity.this, CommentPagerActivity.class).putExtra("ITEM_BEAN", urlBean));
                Log.e("postion", position + "");
            }

            //跳转楼盘评价列表
            @Override
            public void setJumpCommentListClick() {
                startActivity(new Intent(HousesTypeDetailSimpleActivity.this, HousesCommentActivity.class).putExtra("PROJECTID", baseBean.estateProjectId));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(HousesTypeDetailSimpleActivity.this) {
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
                tvCollect.setText("已收藏");
                ivCollect.setChecked(true);
            } else {
                tvCollect.setText("收藏");
                ivCollect.setChecked(false);
            }
        }
    }

    @Override
    public void showHousesDetail(HousesDetailBaseBean baseBean) {
        if (baseBean != null) {
            this.baseBean = baseBean;
            setBaseDate(baseBean);
        }
    }

    public void setBaseDate(HousesDetailBaseBean baseBean) {
        mPresenter.requestHousesComment(baseBean.estateProjectId + "", "1");
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
    public void showFavorableVip(List<FavorableVip> favorableVips) {
        if (favorableVips != null && favorableVips.size() > 0) {
            layFavorable.setVisibility(View.VISIBLE);
            // 只显示最新一条认筹优惠
            if (btnSelectBuy.getVisibility() == View.VISIBLE) {
                vOffset.setVisibility(View.VISIBLE);
            }
            btnGetFavorable.setVisibility(View.VISIBLE);
            layVip.setVisibility(View.VISIBLE);
            tvVip.setText(favorableVips.get(0).privilege);
        } else {
            // 无会员优惠时不显示获取优惠按钮
            btnGetFavorable.setVisibility(View.GONE);
        }
    }

    @Override
    public void showFial(String msg) {
        cancelLoading();
//        CustomerUtils.showTip(this, msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isCollect) {
            setResult(RESULT_OK);
        }
    }
}
