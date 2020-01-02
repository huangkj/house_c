package com.easylife.house.customer.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.bean.Order;
import com.easylife.house.customer.bean.OrderParameter;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.ui.houses.housepincontrol.HousePinControlActivity;
import com.easylife.house.customer.ui.houses.housesdetail.comment.CommentActivity;
import com.easylife.house.customer.ui.houses.housesdetail.exportlist.exportComment.ExportCommentActivity;
import com.easylife.house.customer.ui.mine.order.SignatureActivity;
import com.easylife.house.customer.ui.mine.order.orderdetail.OrderDetailActivity;
import com.easylife.house.customer.ui.payment.favorable.FavorableBuyActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;

import java.util.ArrayList;
import java.util.List;

import static com.easylife.house.customer.R.id.btnCommentServe;

/**
 * Created by Mars on 2017/3/20 19:40.
 * 描述：我的订单
 */

public class OrdersAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {
    private Activity activity;
    private Fragment fragment;

    public OrdersAdapter(Fragment fragment, Activity activity, int layoutResId, List<Order> data) {
        super(layoutResId, data);
        this.activity = activity;
        this.fragment = fragment;
    }

    private boolean isFavorable;

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Order order) {

        /**
         *       "devId": "97",
         "image": "",
         "orderType": 0,
         "preferentialWay": "五万抵十万 适用于 三居",
         "coop": 0,
         "proccessFollowStatus": "PLACE",
         "orderTime": "2017年07月20日 17:40:06",
         "isTransparent": 1,
         "orderCode": "67531500433992047121",
         "vipMoney": 10000,
         "devName": "影姐测试专用",
         "type": "买房优惠"
         */

        CacheManager.initImageClientList(mContext, (ImageView) baseViewHolder.getView(R.id.imgCover), order.image);
        baseViewHolder.setText(R.id.tvCreateTime, order.orderTime);
        baseViewHolder.setText(R.id.tvDevName, order.devName);
        baseViewHolder.setText(R.id.tvStatusOrder, order.type);

        baseViewHolder.setVisible(R.id.btnPay, false);
        isFavorable = false;
        if (!TextUtils.isEmpty(order.type)) {
            isFavorable = order.type.contains("优惠");
        }
        if (isFavorable) {
            baseViewHolder.setVisible(R.id.layFavorable, true);
            baseViewHolder.setVisible(R.id.layDev, false);


            baseViewHolder.setText(R.id.tvFavorableType, order.preferentialWay);
            baseViewHolder.setText(R.id.tvVipCost, "￥" + DoubleFomat.format2(order.vipMoney));
        } else {
            baseViewHolder.setVisible(R.id.layFavorable, false);
            baseViewHolder.setVisible(R.id.layDev, true);

            baseViewHolder.setText(R.id.tvDevInfo, order.information);
            baseViewHolder.setText(R.id.tvDevPay, "￥" + DoubleFomat.format2(order.purchaseMoney));
        }

        if (order.orderType == Order.OrderType.PAYED.code) {
            // 已付款
            if (Order.FollowStatus.PART.name().equals(order.proccessFollowStatus)) {
                // 待支付： 未全部支付
                // 是否允许在App支付
                if (order.allowed) {
                    order.typeBtn = 1;

                    baseViewHolder.setVisible(R.id.btnPay, true);
                    baseViewHolder.setText(R.id.btnPay, "继续支付");
                }
            } else if (Order.FollowStatus.SIGNATURED.name().equals(order.proccessFollowStatus)) {
                // 已签章
                baseViewHolder.setVisible(R.id.btnPay, false);
            } else if (Order.FollowStatus.SIGNED.name().equals(order.proccessFollowStatus)) {
                // 已签约
                baseViewHolder.setVisible(R.id.btnPay, false);
            } else if (!isFavorable && Order.FollowStatus.ALL.name().equals(order.proccessFollowStatus)) {
                // 已付
                baseViewHolder.setVisible(R.id.btnPay, true);
                if (isFavorable) {
                    order.typeBtn = 3;
                    baseViewHolder.setText(R.id.btnPay, "选房下定");
                } else {
                    order.typeBtn = 2;
                    baseViewHolder.setText(R.id.btnPay, "签章");
                }
            }
        } else if (order.orderType == Order.OrderType.PLACE.code) {
            //刚下单， 未付款
            // 是否允许在App支付
            if (order.allowed) {
                order.typeBtn = 1;

                baseViewHolder.setVisible(R.id.btnPay, true);
                baseViewHolder.setText(R.id.btnPay, "支付购买");
            }
        }

        baseViewHolder.setOnClickListener(R.id.layDetail, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailActivity.startActivity(fragment, order.orderCode, 1);
            }
        });
        baseViewHolder.setOnClickListener(R.id.btnCommentDev, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.startActivity(activity, order.projectId, 2);
            }
        });
        baseViewHolder.setOnClickListener(btnCommentServe, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(order.brokerCode)) {
                    CustomerUtils.showTip(activity, "暂未匹配经纪人");
                    return;
                }
                ExportCommentActivity.startActivity(activity, order.brokerCode, 3);
            }
        });

        baseViewHolder.setOnClickListener(R.id.btnPay, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (order.typeBtn) {
                    case 1:
                        // 待支付： 未全部支付
                        // 去支付
                        if (isFavorable) {
                            // 优惠（认筹）支付流程

                            HousesDetailBaseBean detail = new HousesDetailBaseBean();
                            detail.devId = order.devId;
                            detail.devName = order.devName;
                            detail.addressDetail = order.addressDistrict + order.addressTown + order.addressDetail;
                            List<HousesDetailBaseBean.EffectIdBean> list = new ArrayList<>();
                            HousesDetailBaseBean.EffectIdBean effectIdBean = new HousesDetailBaseBean.EffectIdBean();
                            effectIdBean.url = order.image;
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
                            FavorableBuyActivity.startActivity(fragment, order.orderCode, detail, parameter, (order.vipMoney - order.paid) + "", order.vipMoney + "", 4);
                        } else {
                            // 认购支付流程
                            CustomerUtils.showTip(activity, "认购功能开发中");
                        }
                        break;
                    case 2:
                        // 在线签章
                        SignatureActivity.startActivity(fragment, order.orderCode, order.customerName, 5);
                        break;
                    case 3:
                        // 认购下定
                        HousePinControlActivity.startActivity(fragment, order.devId, 6);
                        break;
                }
            }
        });

    }

}
