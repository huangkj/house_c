package com.easylife.house.customer.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.NetAppointmentBean;
import com.easylife.house.customer.config.CacheManager;
import com.easylife.house.customer.util.CustomerUtils;

import java.util.List;

/**
 * Created by zgm on 2017/3/29.
 * 买房成交
 */

public class BuyHouseAdapter extends BaseQuickAdapter<NetAppointmentBean, BaseViewHolder> {

    public ItemChildOnclickListenear itemChildOnClickListenear;

    public interface ItemChildOnclickListenear {
        void onClickPhone(String phoneNum);

        void onCancleClick();
    }

    public void setItemChildOnclickListenear(ItemChildOnclickListenear itemChildOnClickListenear) {
        this.itemChildOnClickListenear = itemChildOnClickListenear;
    }

    public BuyHouseAdapter(int layoutResId, List<NetAppointmentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final NetAppointmentBean netAppointmentBean) {
        holder.setText(R.id.tv_name, "认购时间:");
        holder.setText(R.id.tv_name_value, "暂无数据");

        holder.setText(R.id.tv_look_local, "签约时间:");
        holder.setText(R.id.tv_look_local_value, "暂无数据");
        if (netAppointmentBean.dev != null) {
            if (netAppointmentBean.dev.effectId != null && netAppointmentBean.dev.effectId.size() != 0) {
                CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_house), netAppointmentBean.dev.effectId.get(0).thumbnailImage);
            }
            holder.setText(R.id.tv_house_name, netAppointmentBean.dev.devName);
            holder.setText(R.id.tv_price, netAppointmentBean.dev.averPrice + "元/㎡");
            holder.setText(R.id.tv_area, netAppointmentBean.dev.devSquareMetre + "㎡");
            holder.setText(R.id.tv_room, netAppointmentBean.dev.devBedroom);
            holder.setText(R.id.tv_address, netAppointmentBean.dev.addressDistrict + "-" + netAppointmentBean.dev.addressTown + "   " + netAppointmentBean.dev.propertyType);
            if (!TextUtils.isEmpty(netAppointmentBean.look.purchaseTime)) {
                holder.setText(R.id.tv_name_value, CustomerUtils.dateTransSdf(Long.parseLong(netAppointmentBean.look.purchaseTime)));
            }
        }

        holder.setVisible(R.id.tv_buy, true);
        holder.setVisible(R.id.tv_book, false);

        holder.getView(R.id.rl_export).setVisibility(View.VISIBLE);
        holder.getView(R.id.rl_no).setVisibility(View.GONE);
        holder.getView(R.id.iv_time).setVisibility(View.GONE);
        if (netAppointmentBean.broker != null) {
            CacheManager.initImageUserHeader(mContext, (ImageView) holder.getView(R.id.team_head), netAppointmentBean.broker.headImg);
        } else {
            CacheManager.initImageUserHeader(mContext, (ImageView) holder.getView(R.id.team_head), null);
        }

        holder.setText(R.id.team_name, netAppointmentBean.broker.name);
        holder.setText(R.id.team_good_value, netAppointmentBean.broker.favorableRate + "%");
        holder.setText(R.id.team_look_value, netAppointmentBean.broker.count + "");
        holder.getView(R.id.team_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (netAppointmentBean.broker != null)
                    itemChildOnClickListenear.onClickPhone(netAppointmentBean.broker.phone);
            }
        });

        if (netAppointmentBean.look != null) {
            switch (netAppointmentBean.look.status) {
                case 4:
                    // 认购
                    holder.setText(R.id.tv_buy, "认购");

                    holder.setVisible(R.id.tv_book, false);
                    holder.setText(R.id.tv_look_local_value, "暂无数据");
                    break;
                case 5:
                    // 签约
                    holder.setText(R.id.tv_buy, "认购");

                    if (!TextUtils.isEmpty(netAppointmentBean.look.signTime)) {
                        holder.setText(R.id.tv_look_local_value, CustomerUtils.dateTransSdf(Long.parseLong(netAppointmentBean.look.signTime)));
                    }
                    if (TextUtils.isEmpty(netAppointmentBean.look.purchaseTime)) {
                        holder.setVisible(R.id.tv_buy, false);
                        holder.setText(R.id.tv_name_value, "暂无数据");
                    }
                    holder.setVisible(R.id.tv_book, true);
                    holder.setText(R.id.tv_book, "签约");
                    break;
                case 6:
                    // 退认购
                    holder.setText(R.id.tv_buy, "退认购");

                    holder.setVisible(R.id.tv_book, false);
                    holder.setText(R.id.tv_look_local_value, "暂无数据");
                    break;
                default:
                    break;
            }
        }


    }
}
