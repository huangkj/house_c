package com.easylife.house.customer.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.BrokerageRecordBean;
import com.easylife.house.customer.util.CommonUtils;
import com.easylife.house.customer.util.MoneyUtils;
import com.easylife.house.customer.view.SpaceItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BrokerageRecordAdapter extends BaseQuickAdapter<BrokerageRecordBean, BaseViewHolder> {

    private static final int EXPAND_COUNT = 3;
    private ArrayList<String> subItemList = new ArrayList<>();
    private int clickPos;
    private BrokerageRecordBean currentItem;
    private SparseBooleanArray expandStatus = new SparseBooleanArray();
    private int parentAdapterPosition;

    public BrokerageRecordAdapter(int layoutResId, @Nullable List<BrokerageRecordBean> data) {
        super(layoutResId, data);
    }


    @Override
    public void setNewData(@Nullable List<BrokerageRecordBean> data) {
        super.setNewData(data);
        expandStatus.clear();
        for (int i = 0; i < data.size(); i++) {
            expandStatus.put(i, false);
        }
    }


    @Override
    protected void convert(final BaseViewHolder helper, final BrokerageRecordBean item) {
        currentItem = item;
        parentAdapterPosition = helper.getLayoutPosition();

        helper.setText(R.id.tvDateBrokerage, TimeUtils.millis2String(item.getCreateTime(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm")));

//审核状态：1:待审核,2:审核中,3:审核通过,4:审核拒绝"
    /*    if (item.getAuditStatus() == 1) {
            helper.setText(R.id.tvBrokerageStatus, "未审核");
            helper.setTextColor(R.id.tvBrokerageStatus, ContextCompat.getColor(mContext, R.color._c5c5ca));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_order_gray);
        } else if (item.getAuditStatus() == 2) {
            helper.setText(R.id.tvBrokerageStatus, "审核中");
            helper.setTextColor(R.id.tvBrokerageStatus, ContextCompat.getColor(mContext, R.color._fda90b));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_order_onrage);
        } else if (item.getAuditStatus() == 3) {
            helper.setText(R.id.tvBrokerageStatus, "审核通过");
            helper.setTextColor(R.id.tvBrokerageStatus, ContextCompat.getColor(mContext, R.color._44be7e));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_order_green);
        } else if (item.getAuditStatus() == 4) {
            helper.setText(R.id.tvBrokerageStatus, "审核拒绝");
            helper.setTextColor(R.id.tvBrokerageStatus, ContextCompat.getColor(mContext, R.color._fe505c));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_order_red);
        }*/
        helper.setText(R.id.tvBrokerageStatus, item.getStateStr());


        if ("资料确认中".equals(item.getStateStr()) || "交易处理中".equals(item.getStateStr())
                || "支付确认中".equals(item.getStateStr()) || "银行打款中".equals(item.getStateStr())) {
            helper.setTextColor(R.id.tvBrokerageStatus, Color.parseColor("#FDA90B"));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_status_bg1);

        } else if ("待重新提交".equals(item.getStateStr())) {
            helper.setTextColor(R.id.tvBrokerageStatus, Color.parseColor("#FDA90B"));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_status_bg2);

        } else if ("交易完成".equals(item.getStateStr())) {
            helper.setTextColor(R.id.tvBrokerageStatus, Color.parseColor("#1CAC9E"));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_status_bg3);

        } else if ("申请结佣".equals(item.getStateStr())) {
            helper.setTextColor(R.id.tvBrokerageStatus, Color.parseColor("#FD6A41"));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_status_bg4);

        } else if ("审核拒绝".equals(item.getStateStr())) {
            helper.setTextColor(R.id.tvBrokerageStatus, Color.parseColor("#FE505C"));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_status_bg5);

        } else {
            helper.setTextColor(R.id.tvBrokerageStatus, Color.parseColor("#c5c5ca"));
            helper.setBackgroundRes(R.id.tvBrokerageStatus, R.drawable.broker_order_gray);
        }

        helper.setText(R.id.tvOrderCount, "共" + item.getBrokerList().size() + "笔申请订单");
        helper.setText(R.id.tvTotalPrice, "金额： ¥" + MoneyUtils.moneyFormat3(item.getApplyBrokerage()));



        final RecyclerView rcvItem = (RecyclerView) helper.getView(R.id.rcvItem);
        ItemAdapter itemAdapter = (ItemAdapter) rcvItem.getAdapter();
        helper.setVisible(R.id.ivOrderCountStatus, item.getBrokerList().size() > EXPAND_COUNT);


        boolean expand = expandStatus.get(helper.getAdapterPosition());

        ImageView ivOrderCountStatus = (ImageView) helper.getView(R.id.ivOrderCountStatus);

        if (expand) {
            ivOrderCountStatus.setImageResource(R.mipmap.arrow_house_up);
            ivOrderCountStatus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color._ff7c7c7c)));

        } else {


            ivOrderCountStatus.setImageResource(R.mipmap.arrow_house_down);
            ivOrderCountStatus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color._ff6800)));
        }


        if (itemAdapter == null) {
            rcvItem.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(10)));
            rcvItem.setLayoutManager(new LinearLayoutManager(mContext));
            rcvItem.setFocusableInTouchMode(false);
            rcvItem.requestFocus();

        } else {
//            rcvItem.removeAllViews();
//            itemAdapter.notifyDataSetChanged();
        }
        itemAdapter = new ItemAdapter(parentAdapterPosition, item.getBrokerList());
        rcvItem.setAdapter(itemAdapter);


//        if (item.getBrokerList().size() > EXPAND_COUNT) {
        helper.getView(R.id.llOrderCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = helper.getAdapterPosition();
                boolean b = expandStatus.get(position);
                expandStatus.put(position, !b);
                notifyDataSetChanged();
            }
        });

//        }

    }


    public class ItemAdapter extends BaseQuickAdapter<BrokerageRecordBean.BrokerListBean, BaseViewHolder> {

        private int index;

        public ItemAdapter(int index, List<BrokerageRecordBean.BrokerListBean> data) {
            super(R.layout.brokerage_record_item_item, data);
            this.index = index;
        }

        @Override
        protected void convert(BaseViewHolder helper, BrokerageRecordBean.BrokerListBean item) {
            int adapterPosition = helper.getAdapterPosition();
            helper.setText(R.id.tvIndex, (adapterPosition + 1) + "");

            helper.setText(R.id.tvUserNameIItem, item.getCustomerName() + " (" + CommonUtils.hidePhoneNumber(item.getCustomerPhone()) + ")");
            helper.setText(R.id.tvHouseNameIItem, item.getDevName() + " " + item.getHouseNum());
//            helper.setText(R.id.tvMoneyIItem, "¥" + MoneyUtils.moneyFormat3(item.getShouldAmount()));
            helper.setText(R.id.tvMoneyIItem, "¥" + MoneyUtils.moneyFormat3(item.getShouldAmount()));
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BrokerageRecordAdapter.this.setOnItemClick(null, index);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (getData().size() <= EXPAND_COUNT) {
                return super.getItemCount();
            } else {
                return expandStatus.get(index) ? super.getItemCount() : EXPAND_COUNT;
            }
        }
    }

}
