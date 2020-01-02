package com.easylife.house.customer.ui.mine.order.orderdetail;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.bean.OrderParameter;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.houses.housepincontrol.HousePinControlActivity;
import com.easylife.house.customer.ui.houses.housesdetail.comment.CommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.exportlist.exportComment.ExportCommentActivity;
import com.easylife.house.customer.ui.mine.order.FavorableCertificateActivity;
import com.easylife.house.customer.ui.mine.order.PayDetailMixtureActivity;
import com.easylife.house.customer.ui.mine.order.PayDetailPurchaseActivity;
import com.easylife.house.customer.ui.mine.order.PayDetailRaiseActivity;
import com.easylife.house.customer.ui.payment.ApplyRefundActivity;
import com.easylife.house.customer.ui.payment.RefundRateActivity;
import com.easylife.house.customer.ui.payment.favorable.FavorableBuyActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.PriceUtil;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.FlowViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 订单详情
 */
@RuntimePermissions
public class OrderDetailActivity extends MVPBaseActivity<OrderDetailContract.View, OrderDetailPresenter> implements OrderDetailContract.View {

    @Bind(R.id.imgCover)
    ImageView imgCover;
    @Bind(R.id.tvDevName)
    TextView tvDevName;
    @Bind(R.id.tvAvgPrice)
    TextView tvAvgPrice;
    @Bind(R.id.tvDevAddress)
    TextView tvDevAddress;
    @Bind(R.id.tvDevType)
    TextView tvDevType;
    @Bind(R.id.tvDevHouseNum)
    TextView tvDevHouseNum;
    @Bind(R.id.tvDevArea)
    TextView tvDevArea;
    @Bind(R.id.groupTags)
    FlowViewGroup groupTags;
    @Bind(R.id.tvStatusOrder)
    TextView tvStatusOrder;
    @Bind(R.id.tvOrderNum)
    TextView tvOrderNum;
    @Bind(R.id.tvFavorable)
    TextView tvFavorable;
    @Bind(R.id.tvFavorableDesc)
    TextView tvFavorableDesc;
    @Bind(R.id.tvVipPriceDesc)
    TextView tvVipPriceDesc;
    @Bind(R.id.tvCustomerName)
    TextView tvCustomerName;
    @Bind(R.id.tvBrokerCall)
    TextView tvBrokerCall;
    @Bind(R.id.btnCommentDev)
    ButtonTouch btnCommentDev;
    @Bind(R.id.btnCommentServe)
    ButtonTouch btnCommentServe;
    @Bind(R.id.tvVipPrice)
    TextView tvVipPrice;
    @Bind(R.id.tvPayDetail)
    TextView tvPayDetail;
    @Bind(R.id.tvBrokerName)
    TextView tvBrokerName;
    @Bind(R.id.layBrokerName)
    LinearLayout layBrokerName;
    @Bind(R.id.bottom)
    LinearLayout bottom;
    @Bind(R.id.tvTimePay)
    TextView tvTimePay;
    @Bind(R.id.tvRefundMoney)
    TextView tvRefundMoney;
    @Bind(R.id.tvRefundDesc)
    TextView tvRefundDesc;
    @Bind(R.id.layRefundMiddle)
    LinearLayout layRefundMiddle;
    @Bind(R.id.layRefund)
    LinearLayout layRefund;
    @Bind(R.id.tvBrokerMatching)
    TextView tvBrokerMatching;
    @Bind(R.id.tvRuleLock)
    TextView tvRuleLock;
    @Bind(R.id.layContactBroker)
    LinearLayout layContactBroker;
    @Bind(R.id.btnSignCertificate)
    ButtonTouch btnSignCertificate;
    @Bind(R.id.btnNextMiddle)
    ButtonTouch btnNextMiddle;
    @Bind(R.id.tvOrderStatus1)
    TextView tvOrderStatus1;
    @Bind(R.id.imgPoint11)
    ImageView imgPoint11;
    @Bind(R.id.imgLine11)
    View imgLine11;
    @Bind(R.id.cbMakeOrder)
    CheckBox cbMakeOrder;
    @Bind(R.id.tvOrderMakeTime)
    TextView tvOrderMakeTime;
    @Bind(R.id.tvOrderNo)
    TextView tvOrderNo;
    @Bind(R.id.lay11)
    LinearLayout lay11;
    @Bind(R.id.imgPoint12)
    ImageView imgPoint12;
    @Bind(R.id.imgLine12)
    View imgLine12;
    @Bind(R.id.cbHadPay)
    CheckBox cbHadPay;
    @Bind(R.id.tvFavorableInformation)
    TextView tvFavorableInformation;
    @Bind(R.id.tvHadPay)
    TextView tvHadPay;
    @Bind(R.id.tvPayNo)
    TextView tvPayNo;
    @Bind(R.id.tvPayNo2)
    TextView tvPayNo2;
    @Bind(R.id.layPayNo)
    LinearLayout layPayNo;
    @Bind(R.id.layPayNo2)
    LinearLayout layPayNo2;
    @Bind(R.id.tvPayCompleteTime)
    TextView tvPayCompleteTime;
    @Bind(R.id.tvPayCompleteTime2)
    TextView tvPayCompleteTime2;
    @Bind(R.id.layPayCompleteTime)
    LinearLayout layPayCompleteTime;
    @Bind(R.id.layPayCompleteTime2)
    LinearLayout layPayCompleteTime2;
    @Bind(R.id.lay12)
    LinearLayout lay12;
    @Bind(R.id.imgPoint13)
    ImageView imgPoint13;
    @Bind(R.id.cbRefund)
    CheckBox cbRefund;
    @Bind(R.id.tvFavorableInformationRefund)
    TextView tvFavorableInformationRefund;
    @Bind(R.id.tvPayRefund)
    TextView tvPayRefund;
    @Bind(R.id.tvPayNoRefund)
    TextView tvPayNoRefund;
    @Bind(R.id.tvPayCompleteTimeRefund)
    TextView tvPayCompleteTimeRefund;
    @Bind(R.id.tvRefundRate)
    TextView tvRefundRate;
    @Bind(R.id.layRefundRate)
    LinearLayout layRefundRate;
    @Bind(R.id.layPayNoRefund)
    LinearLayout layPayNoRefund;
    @Bind(R.id.tvRefundApplyTime)
    TextView tvRefundApplyTime;
    @Bind(R.id.layRefundApplyTime)
    LinearLayout layRefundApplyTime;
    @Bind(R.id.tvRefundCompleteTime)
    TextView tvRefundCompleteTime;
    @Bind(R.id.layRefundCompleteTime)
    LinearLayout layRefundCompleteTime;
    @Bind(R.id.layStatusRefund)
    LinearLayout layStatusRefund;
    @Bind(R.id.layStatus1)
    LinearLayout layStatus1;
    @Bind(R.id.tvOrderStatus2)
    TextView tvOrderStatus2;
    @Bind(R.id.imgPoint21)
    ImageView imgPoint21;
    @Bind(R.id.imgLine21)
    View imgLine21;
    @Bind(R.id.cbPay)
    CheckBox cbPay;
    @Bind(R.id.tvSubscriptionEarnest)
    TextView tvSubscriptionEarnest;
    @Bind(R.id.tvHadPaySignature)
    TextView tvHadPaySignature;
    @Bind(R.id.lay21)
    LinearLayout lay21;
    @Bind(R.id.imgPoint22)
    ImageView imgPoint22;
    @Bind(R.id.cbSignature)
    CheckBox cbSignature;
    @Bind(R.id.tvTimeSignature)
    TextView tvTimeSignature;
    @Bind(R.id.lay22)
    LinearLayout lay22;
    @Bind(R.id.layStatus2)
    LinearLayout layStatus2;
    @Bind(R.id.layStatus3)
    LinearLayout layStatus3;
    @Bind(R.id.tvOrderStatus3)
    TextView tvOrderStatus3;
    @Bind(R.id.imgLine31)
    View imgLine31;
    @Bind(R.id.imgPoint31)
    ImageView imgPoint31;
    @Bind(R.id.cbSign)
    CheckBox cbSign;
    @Bind(R.id.tvTimeSign)
    TextView tvTimeSign;
    @Bind(R.id.lay31)
    LinearLayout lay31;
    @Bind(R.id.layOrderStatus3)
    LinearLayout layOrderStatus3;
    @Bind(R.id.btnNextBottom)
    ButtonTouch btnNextBottom;
    @Bind(R.id.layDev)
    LinearLayout layDev;
    @Bind(R.id.tvHouseDevName)
    TextView tvHouseDevName;
    @Bind(R.id.tvHouseNo)
    TextView tvHouseNo;
    @Bind(R.id.tvHouseStructure)
    TextView tvHouseStructure;
    @Bind(R.id.tvHouseArea)
    TextView tvHouseArea;
    @Bind(R.id.tvStatusOrderHouse)
    TextView tvStatusOrderHouse;
    @Bind(R.id.layHouse)
    LinearLayout layHouse;
    @Bind(R.id.tvCountDownDesc)
    TextView tvCountDownDesc;
    @Bind(R.id.tvCountDownTime)
    TextView tvCountDownTime;
    @Bind(R.id.layCountDown)
    LinearLayout layCountDown;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.order_activity_detail, null);
    }

    /**
     * @param activity
     * @param orderCode
     * @param requestCode
     */
    public static void startActivity(Activity activity, String orderCode,
                                     int requestCode) {
        activity.startActivityForResult(new Intent(activity, OrderDetailActivity.class)
                        .putExtra("orderCode", orderCode)
                , requestCode
        );
    }

    public static void startActivity(Fragment fragment, String orderCode,
                                     int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getActivity(), OrderDetailActivity.class)
                        .putExtra("orderCode", orderCode)
                , requestCode
        );
    }

    /**
     * 订单号
     */
    private String orderCode;
    /**
     * 订单状态的文字描述
     */
    private String orderType = Order.OrderType.PLACE.msg;
    private Order order;

    /**
     * 中间右侧按钮
     */
    private int typeBtnNextMiddle;
    /**
     * 取消订单
     */
    public final int TYPE_MIDDLE_CANCEL = 1;

    /**
     * 申请退款
     */
    public final int TYPE_MIDDLE_REFUND_APPLY = 2;
    /**
     * 退款进度
     */
    public final int TYPE_MIDDLE_REFUND_RATE = 3;


    /**
     * 底部右侧按钮
     */
    private int typeBtnNextBottom;
    /**
     * 去支付，继续支付
     */
    public final int TYPE_BOTTOM_TO_PAY = 1;
    /**
     * 选房下定
     */
    public final int TYPE_BOTTOM_TO_BUY_HOUSE = 2;

    @Override
    protected void initView() {
        hideNoNetView();
        orderCode = getIntent().getStringExtra("orderCode");

        mPresenter.getNetData(orderCode);
    }

    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void tryRequestData() {
    }


    @Override
    public void initData(Order order) {
        if (order == null) {
            return;
        }
        this.order = order;

//        1.判断订单状态-阶段状态
        if (order.coOp == Order.CoOp.MENBER.code) {
//            会员 - 认筹
            layStatus1.setVisibility(View.VISIBLE);
            layStatus2.setVisibility(View.GONE);
            layStatus3.setVisibility(View.VISIBLE);
            tvOrderStatus3.setText("第二阶段 - 签约");
        } else if (order.coOp == Order.CoOp.ENTERPRISE.code) {
//            企业-认购
            layStatus1.setVisibility(View.GONE);
            layStatus2.setVisibility(View.VISIBLE);
            layStatus3.setVisibility(View.VISIBLE);
            tvOrderStatus2.setText("第一阶段 - 认购下定");
            tvOrderStatus3.setText("第二阶段 - 签约");
        } else if (order.coOp == Order.CoOp.MENBER_ENTERPRISE.code) {
//            会员+企业。认筹-认购
            layStatus1.setVisibility(View.VISIBLE);
            layStatus2.setVisibility(View.VISIBLE);
            layStatus3.setVisibility(View.VISIBLE);
            tvOrderStatus2.setText("第二阶段 - 认购下定");
            tvOrderStatus3.setText("第三阶段 - 签约");
        }


        // 获取订单状态文本
        if (order.orderType == Order.OrderType.PLACE.code) {
            orderType = Order.OrderType.PLACE.msg;
        } else if (order.orderType == Order.OrderType.PAYED.code) {
            orderType = Order.OrderType.PAYED.msg;
        } else if (order.orderType == Order.OrderType.SIGNED.code) {
            orderType = Order.OrderType.SIGNED.msg;
        } else if (order.orderType == Order.OrderType.REFUND.code) {
            orderType = "退款成功";
        } else if (order.orderType == Order.OrderType.REFUNDING.code) {
            orderType = Order.OrderType.REFUNDING.msg;
        } else if (order.orderType == Order.OrderType.REFUNDFIAL.code) {
            orderType = Order.OrderType.REFUNDFIAL.msg;
        } else if (order.orderType == Order.OrderType.DEL.code) {
            orderType = Order.OrderType.DEL.msg;
        }

        // 订单基本信息
        tvOrderNum.setText(orderCode);// 订单编号
        tvFavorable.setText(order.preferentialWay);// 优惠方式
        tvCustomerName.setText(order.customerName); // 购买人
//        2.基本信息展示判断-认购/认筹
        if (Order.FollowType.PURCHASE.code == order.followType) {
            // 认购,认筹+认购中的认购阶段
            tvFavorableDesc.setText("专项优惠:");
            tvVipPriceDesc.setText("认购定金:");
            tvRuleLock.setVisibility(View.VISIBLE);
            tvVipPrice.setText(PriceUtil.checkPriceValue(order.purchaseMoney + ""));// 认购价格

            layHouse.setVisibility(View.VISIBLE);// 显示房源信息
            layDev.setVisibility(View.GONE);// 显示楼盘信息
            CacheManager.initCenterCropImage(activity, imgCover, order.houseImg);// 房源图片
            tvHouseDevName.setText(order.devName);// 显示房源的楼盘名称
            tvHouseNo.setText(order.information);// 显示房间号
            tvHouseStructure.setText(order.structure); // 房源户型信息
            tvHouseArea.setText(order.fArea); // 房源面积

            tvStatusOrderHouse.setText(orderType);

        } else {
            // 认筹，认筹+认购中的认筹阶段

            tvVipPriceDesc.setText("会员费:");
            tvRuleLock.setVisibility(View.GONE);
            tvVipPrice.setText(PriceUtil.checkPriceValue(order.vipMoney + ""));// 认筹价格

            layHouse.setVisibility(View.GONE); // 隐藏房源信息
            layDev.setVisibility(View.VISIBLE); // 显示楼盘信息布局
            CacheManager.initCenterCropImage(activity, imgCover, order.devImg);// 楼盘图片
            tvDevName.setText(order.devName); // 显示楼盘名称
            tvAvgPrice.setText(PriceUtil.formatNum(order.avgprice) + "元/㎡"); // 显示楼盘平均价格
            tvDevAddress.setText(order.addressDistrict + order.addressTown + order.addressDetail); // 显示楼盘地址
            tvDevType.setText(order.propertyType);// 楼盘物业类型
            tvDevHouseNum.setText(order.devbedroom);// 居室
            if (TextUtils.isEmpty(order.avgprice)) {
                tvDevArea.setText(null); // 面积
            } else {
                tvDevArea.setText(order.devsquaremetre + "㎡"); // 面积
            }
            String[] features = order.feature.split(",");
            if (features.length >= 3) {
                String[] featuresNew = new String[3];
                for (int i = 0; i < 3; i++) {
                    featuresNew[i] = features[i];
                }
                groupTags.addViews(R.layout.item_flow_house_type_tag, featuresNew, null);
            } else {
                groupTags.addViews(R.layout.item_flow_house_type_tag, features, null);
            }

            tvStatusOrder.setText(orderType);
        }

        // 分配的经纪人的信息
        if (TextUtils.isEmpty(order.brokerPhone)) {
            // 未匹配经纪人 - 取消订单按钮，显示匹配中文本
            tvBrokerMatching.setVisibility(View.VISIBLE);
            layBrokerName.setVisibility(View.GONE);
            layContactBroker.setVisibility(View.GONE);
        } else {
            // 已匹配经纪人 - 显示
            tvBrokerMatching.setVisibility(View.GONE);
            layBrokerName.setVisibility(View.VISIBLE);
            layContactBroker.setVisibility(View.VISIBLE);
            tvBrokerName.setText(order.brokerName);
        }


        // 3.判断中间和底部按钮信息以及部分切换的数据信息
        // 认筹订单 。 会员付费
        if (order.orderType == Order.OrderType.PLACE.code) {
            // 无支付行为
            // 已下单：完全未支付，可取消订单，区分是否已匹配经纪人

            // 中间显示取消订单
            btnNextMiddle.setVisibility(View.VISIBLE);
            btnNextMiddle.setText("取消订单");
            typeBtnNextMiddle = TYPE_MIDDLE_CANCEL;

            // 底部显示去支付
            // 是否允许在App支付
            if (order.allowed) {
                btnNextBottom.setVisibility(View.VISIBLE);
                btnNextBottom.setText("支付购买");
                typeBtnNextBottom = TYPE_BOTTOM_TO_PAY;
            }
        } else if (order.orderType == Order.OrderType.DEL.code) {
            // 已取消。无任何按钮信息,无支付信息
            bottom.setVisibility(View.GONE);
            //    该隐藏的隐藏
            btnNextBottom.setVisibility(View.GONE);
            btnNextMiddle.setVisibility(View.GONE);
            tvBrokerMatching.setVisibility(View.GONE);
            tvRuleLock.setVisibility(View.GONE);
        } else {
            // 有支付行为：显示支付信息
            imgPoint12.setImageResource(R.drawable.circle_end);
            imgLine12.setBackgroundColor(getResources().getColor(R.color.gradient_end));

            // 显示支付明细
            tvPayDetail.setVisibility(View.VISIBLE);

            if (order.orderType == Order.OrderType.PAYED.code) {
                // 已付款：
                //           1.待支付：分批支付-未全部支付。此时显示底部继续支付按钮
                //           2.已付款：全部支付完成
                //因已有支付行为，认筹的支付是可退款的，所以这里显示退款申请按钮

                btnNextMiddle.setVisibility(View.VISIBLE);
                btnNextMiddle.setText("申请退款");
                typeBtnNextMiddle = TYPE_MIDDLE_REFUND_APPLY;
            } else if (order.orderType == Order.OrderType.SIGNED.code) {
                // 已签约
                imgLine31.setBackgroundColor(getResources().getColor(R.color.gradient_end));
                imgPoint31.setImageResource(R.drawable.circle_end);
            } else if (order.orderType == Order.OrderType.REFUND.code) {
                // 退款成功：
                //        显示退款模块，状态：已退款
                //        显示退款进度按钮，支付明细

                layRefund.setVisibility(View.VISIBLE);
                cbRefund.setText("已退款");
                imgLine12.setVisibility(View.VISIBLE);

                // 退款进度
                btnNextMiddle.setVisibility(View.VISIBLE);
                btnNextMiddle.setText("退款进度");
                typeBtnNextMiddle = TYPE_MIDDLE_REFUND_RATE;


            } else {
                // 退款中
                //      1.退款中
                //      2.退款失败
                layRefund.setVisibility(View.VISIBLE);
                layRefundMiddle.setVisibility(View.VISIBLE);
                btnNextMiddle.setVisibility(View.VISIBLE);
                tvTimePay.setText(order.first.paidTime); // 中间部分的支付时间
                tvRefundMoney.setText(order.first.paid + "元"); // 中间部分的退款金额

                cbRefund.setText("退款中");
                if (order.orderType == Order.OrderType.REFUNDFIAL.code) {
                    // 退款失败
                    tvStatusOrder.setText(Order.OrderType.REFUNDFIAL.msg);
                    tvRefundRate.setText("退款失败");

                    btnNextMiddle.setText("重新申请");
                    typeBtnNextMiddle = TYPE_MIDDLE_REFUND_APPLY;
                } else if (order.orderType == Order.OrderType.REFUNDING.code) {
                    // 退款中
                    tvStatusOrder.setText(Order.OrderType.REFUNDING.msg);
                    tvRefundRate.setText("已提交");

                    btnNextMiddle.setText("退款进度");
                    typeBtnNextMiddle = TYPE_MIDDLE_REFUND_RATE;
                }
            }
        }

        // 4.显示阶段数据
        // - 1.一阶段
        if (order.first != null && !TextUtils.isEmpty(order.first.proccessFollowStatus)) {
            // 认筹阶段有数据
            tvOrderMakeTime.setText(order.orderTime); // 下单时间
            tvOrderNo.setText(orderCode); // 订单编号
            tvFavorableInformation.setText(order.preferentialWay); // 优惠信息

            // 付款状态
            if (Order.SettleStatus.NOT.name().equals(order.first.settleStatus)) {
                // 已下单，无支付信息。最初始状态
                cbHadPay.setText("待支付");
            } else {
                imgPoint12.setImageResource(R.drawable.circle_end);
                tvHadPay.setText(PriceUtil.checkPriceValue(order.first.paid));// 已支付金额

                if (Order.SettleStatus.PART.name().equals(order.first.settleStatus)) {
                    // 待支付：支付部分
                    cbHadPay.setText("待支付");
                    // 底部显示去支付
                    // 是否允许在App支付
                    if (order.allowed) {
                        btnNextBottom.setVisibility(View.VISIBLE);
                        btnNextBottom.setText("继续支付");
                        typeBtnNextBottom = TYPE_BOTTOM_TO_PAY;
                    }
                } else if (Order.SettleStatus.ALL.name().equals(order.first.settleStatus)) {
                    // 已付款：全部支付完成
                    cbHadPay.setText("已付款");

                    layPayNo.setVisibility(View.VISIBLE);
                    tvPayNo.setText(order.first.serialNumber); // 支付流水号
                    layPayCompleteTime.setVisibility(View.VISIBLE);
                    tvPayCompleteTime.setText(order.first.paidTime); // 支付完成时间

                    // 非认购订单，如认筹已支付完成，并且是明房源，则底部显示选房下定
                    if (order.coOp != Order.CoOp.ENTERPRISE.code) {
                        btnSignCertificate.setVisibility(View.VISIBLE);
                        if ("1".equals(order.isTransparent)) {
                            btnNextBottom.setVisibility(View.VISIBLE);
                            btnNextBottom.setText("选房下定");
                            typeBtnNextBottom = TYPE_BOTTOM_TO_BUY_HOUSE;
                        }
                    }
                    if (Order.FollowType.RAISE.code == order.followType) {
                        // 认筹已支付
                        tvFavorableDesc.setText("优惠方式:");
                    }
                }
            }

            // 退款状态
            if ("1".equals(order.first.orderRefund)) {

                tvBrokerMatching.setVisibility(View.GONE);
                layBrokerName.setVisibility(View.GONE);
                layContactBroker.setVisibility(View.GONE);


                btnNextBottom.setVisibility(View.GONE);

                btnSignCertificate.setVisibility(View.GONE);
                // 显示退款信息
                layRefund.setVisibility(View.VISIBLE);
                imgLine12.setVisibility(View.VISIBLE);
                imgLine12.setBackgroundColor(getResources().getColor(R.color.gradient_end));
                imgPoint13.setImageResource(R.drawable.circle_end);

                // 判断退款进度
                cbRefund.setText("退款中");
                layRefundRate.setVisibility(View.VISIBLE);
                tvPayNoRefund.setVisibility(View.VISIBLE);
                tvFavorableInformationRefund.setText(order.preferentialWay);//优惠信息
                tvPayRefund.setText(PriceUtil.checkPriceValue(order.first.paid)); // 支付金额
                tvPayNoRefund.setText(order.first.serialNumber);  //　支付流水号
                tvPayCompleteTimeRefund.setText(order.first.paidTime); // 支付完成时间
                tvRefundApplyTime.setText(order.first.createTime); // 退款申请时间
                tvRefundCompleteTime.setText(TextUtils.isEmpty(order.first.endTime) ? "暂无数据" : order.first.endTime);// 退款完成时间

                //  PLACE(0, "已提交"), PART(1, "待受理"), ALL(2, "已退款"), WAIT_SIGNATURE(3, "退款失败");
                if (Order.RefundStatus.PLACE.name().equals(order.first.orderRefundStatus)) {
                    // 已提交
                    tvRefundRate.setText("已提交");
                } else if (Order.RefundStatus.PART.name().equals(order.first.orderRefundStatus)) {
                    // PART(1, "待受理")
                    tvRefundRate.setText("待受理");
                } else if (Order.RefundStatus.ALL.name().equals(order.first.orderRefundStatus)) {
                    // 已退款
                    cbRefund.setText("已退款");
                    layRefundRate.setVisibility(View.GONE);
                } else if (Order.RefundStatus.WAIT_SIGNATURE.name().equals(order.first.orderRefundStatus)) {
                    // 退款失败
                    tvRefundRate.setText("退款失败");
                }
            } else {
                layRefund.setVisibility(View.GONE);
            }
        }
        // - 2.二阶段
        if (order.second != null && !TextUtils.isEmpty(order.second.proccessFollowStatus)) {
            /**
             *  PLACE(0, "未支付"), PART(1, "待支付"), ALL(2, "已付款"),
             SIGNATURED(3, "已签章"), WAIT_SIGN(4, "待签约"),
             SIGNED(5, "已签约"), REFUND(6, "退款");
             */
            tvSubscriptionEarnest.setText(PriceUtil.checkPriceValue(order.second.pay)); // 认购定金

            tvHadPaySignature.setText(PriceUtil.checkPriceValue(order.second.paid)); // 已支付金额
            if (Order.FollowStatus.PLACE.name().equals(order.first.proccessFollowStatus)) {
                //未支付
                cbPay.setText("支付");
                cbSignature.setText("签章");
            } else if (Order.FollowStatus.PART.name().equals(order.first.proccessFollowStatus)) {
                // 待支付
                cbPay.setText("待支付");
                cbSignature.setText("签章");

                imgPoint21.setImageResource(R.drawable.circle_end);
                imgLine21.setBackgroundColor(getResources().getColor(R.color.gradient_end));
            } else {
                cbPay.setText("已付款");
                tvPayNo2.setText(order.second.serialNumber); // 支付流水号
                tvPayCompleteTime2.setText(TextUtils.isEmpty(order.second.paidTime) ? "暂无数据" : order.second.paidTime); // 支付完成时间

                if (Order.FollowStatus.ALL.name().equals(order.first.proccessFollowStatus)) {
                    // 已付款，未签章
                    cbSignature.setText("签章");
                } else if (Order.FollowStatus.SIGNATURED.name().equals(order.first.proccessFollowStatus)) {
                    // 已签章
                    cbSignature.setText("已签章");
                    imgPoint22.setImageResource(R.drawable.circle_end);
                    tvTimeSignature.setText(TextUtils.isEmpty(order.second.paidTime) ? "暂无数据" : order.second.paidTime); // TODO 签章时间？？？？？
                }
            }
        }

        // - 3.三阶段
        if (order.third != null && !TextUtils.isEmpty(order.third.proccessFollowStatus)) {
            imgPoint31.setImageResource(TextUtils.isEmpty(order.third.signTime) ? R.drawable.circle_gray : R.drawable.circle_end);
            tvTimeSign.setText(TextUtils.isEmpty(order.third.signTime) ? "暂无数据" : order.third.signTime);
        }

        // TODO ，需测试此句代码是否有必要
        if (order.orderType == Order.OrderType.SIGNED.code) {
            // 已签约情况下只有楼盘评价和顾问评价按钮
            btnNextBottom.setVisibility(View.GONE);
            btnSignCertificate.setVisibility(View.GONE);
            btnNextMiddle.setVisibility(View.GONE);
            typeBtnNextMiddle = 0;
            typeBtnNextBottom = 0;
        }
    }

    @Override
    public void showResultCancelOrder(boolean isSuccess) {
        if (isSuccess) {
            setResult(RESULT_OK);
            showTip("订单取消成功");
            finish();
        } else {
            showTip("订单取消失败");
        }
    }

    @Override
    public void refund() {
        ApplyRefundActivity.startActivity(activity, orderCode, order.first.paid, 5);
    }

    @OnClick({R.id.tvPayDetail, R.id.tvRefundDesc, R.id.tvBrokerCall, R.id.tvRuleLock, R.id.btnSignCertificate, R.id.btnNextMiddle, R.id.cbMakeOrder, R.id.cbHadPay, R.id.cbRefund, R.id.cbPay, R.id.cbSignature, R.id.cbSign, R.id.btnCommentDev, R.id.btnCommentServe, R.id.btnNextBottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvPayDetail:
                // 查看支付明细
                if (order.coOp == Order.CoOp.ENTERPRISE.code) {
                    //企业。认购
                    PayDetailPurchaseActivity.startActivity(activity, orderCode, order.information, order.purchaseMoney + "", orderType, 0);
                } else if (order.coOp == Order.CoOp.MENBER_ENTERPRISE.code) {
                    // 会员+企业， 认筹+认购
                    PayDetailMixtureActivity.startActivity(activity, orderCode, 0);
                } else {
                    // 会员。认筹
                    PayDetailRaiseActivity.startActivity(activity, orderCode, 0);
                }
                break;
            case R.id.tvRefundDesc:
                // 退款说明
                WebViewActivity.startActivity(activity, "在线退款说明", Constants.URL_REFUND_DESC);
                break;
            case R.id.tvBrokerCall:
                // 电话联系置业顾问
                if (order == null)
                    return;
                call(order.brokerPhone);
                break;
            case R.id.tvRuleLock:
                // 房源锁定规则
                WebViewActivity.startActivity(activity, "房源锁定规则", Constants.URL_HOUSE_LOCK_RULE);
                break;
            case R.id.btnSignCertificate:
                // 优惠凭证
//                if (order.type.contains("优惠")) {
                FavorableCertificateActivity.startActivity(activity, orderCode, 0);
//                } else {
//                    WebViewActivity.startActivity(activity, order.devName + "认购通知书", Constants.URL_FAVORABLE_DEV + orderCode);
//                }
                break;
            case R.id.btnNextMiddle:
                switch (typeBtnNextMiddle) {
                    case TYPE_MIDDLE_CANCEL:
                        // 取消订单
                        cancelOrder();
                        break;
                    case TYPE_MIDDLE_REFUND_RATE:
                        // 查看退款进度
                        RefundRateActivity.startActivity(activity, orderCode, 0);
                        break;
                    case TYPE_MIDDLE_REFUND_APPLY:
                        // 申请退款
                        mPresenter.applyRefund(orderCode);
                        break;
                }
                break;
            case R.id.btnNextBottom:
                switch (typeBtnNextBottom) {
                    case TYPE_BOTTOM_TO_PAY:
                        // 去支付 .接入认购时需要区分认购和认筹
                        try {
                            HousesDetailBaseBean detail = new HousesDetailBaseBean();
                            detail.devId = order.devId;
                            detail.devName = order.devName;
                            detail.addressDetail = order.addressDistrict + order.addressTown + order.addressDetail;
                            List<HousesDetailBaseBean.EffectIdBean> list = new ArrayList<>();
                            HousesDetailBaseBean.EffectIdBean effectIdBean = new HousesDetailBaseBean.EffectIdBean();
                            effectIdBean.url = order.devImg;
                            list.add(effectIdBean);
                            detail.effectId = list;
                            detail.projectName = order.projectName;
                            detail.averPrice = order.avgprice;

                            OrderParameter parameter = new OrderParameter();
                            OrderParameter.Favorable favorable = new OrderParameter.Favorable();
                            favorable.content = order.preferentialWay;
                            favorable.discountType = OrderParameter.Favorable.TYPE_VIP;
                            parameter.orderDiscount.clear();
                            parameter.orderDiscount.add(favorable);

                            parameter.menberDiscountId = order.menberDiscountId;
                            parameter.orderLog.payOnThisTime = "0";
                            parameter.orderLog.pay = (order.vipMoney - order.paid) + "";

                            /**
                             * @param activity
                             * @param orderCode     订单号
                             * @param detail        楼盘数据 : devId ,devName ,address, distribution[] ,projectName
                             * @param parameter     订单参数 ： orderDiscount（优惠信息:content（优惠文本），discountType （OrderParameter.Favorable.TYPE_VIP -认筹）），
                             *                      menberDiscountId（认筹的优惠id），orderLog（payOnThisTime ， pay）
                             * @param payment       要支付的金额
                             * @param requestCode
                             */
                            FavorableBuyActivity.startActivity(activity, orderCode, detail, parameter, (order.vipMoney - order.paid) + "", order.vipMoney + "", 6);
                        } catch (Exception e) {
                            LogOut.d("支付参数转换", e.toString());
                        }
                        break;
                    case TYPE_BOTTOM_TO_BUY_HOUSE:
                        // 选房下定
                        HousePinControlActivity.startActivity(activity, order.devId, 0);
                        break;
                }
                break;
            case R.id.cbMakeOrder:
                // 1.已下单 阶段
                lay11.setVisibility(cbMakeOrder.isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.cbHadPay:
                // 1.已付款阶段
                lay12.setVisibility(cbHadPay.isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.cbRefund:
                // 1.退款阶段
                layStatusRefund.setVisibility(cbRefund.isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.cbPay:
                // 2.已付款阶段
                lay21.setVisibility(cbPay.isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.cbSignature:
                // 2.签章
                lay22.setVisibility(cbSignature.isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.cbSign:
                // 3.签约阶段
                lay31.setVisibility(cbSign.isChecked() ? View.VISIBLE : View.GONE);
                break;
            case R.id.btnCommentDev:
                // 楼盘评价
                CommentActivity.startActivity(activity, order.projectId, 2);
                break;
            case R.id.btnCommentServe:
                // 服务评价
                if (order == null) {
                    showTip("订单数据获取失败");
                    return;
                }
                if (TextUtils.isEmpty(order.brokerCode)) {
                    showTip("暂未匹配经纪人");
                    return;
                }
                ExportCommentActivity.startActivity(activity, order.brokerCode, 3);
                break;
        }
    }

    private void cancelOrder() {
        PubTipDialog dialog = new PubTipDialog(activity, new PubTipDialog.InsideListener() {
            @Override
            public void note(boolean isOK) {
                if (isOK) {
                    mPresenter.cancelOrder(orderCode);
                }
            }
        });
        dialog.showdialog("提示", "是否确认放弃优惠购买？", null, "取消", "确定");
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            mPresenter.getNetData(orderCode);
//            switch (requestCode) {
//                case 1:
//                    //签章成功，更新状态
//                    break;
//                case 2:
//                    // 楼盘评价
//                    break;
//                case 3:
//                    // 服务评价
//                    break;
//                case 4:
//                    // 支付购买成功返回
//                    break;
//                case 5:
//                    // 申请退款，更新状态
//                    break;
//                case 6:
//                    // 认筹支付返回
//                    break;
//            }
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
                        OrderDetailActivityPermissionsDispatcher.jumpCallPhoneWithCheck(OrderDetailActivity.this, phone);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OrderDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
