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
 * 现场看房
 */

public class LiveHouseAdapter extends BaseQuickAdapter<NetAppointmentBean, BaseViewHolder> {

    public ItemChildOnclickListenear itemChildOnClickListenear;

    public interface ItemChildOnclickListenear {
        void onClickPhone(String phoneNum);

        void onToVisit();
    }

    public void setItemChildOnclickListenear(ItemChildOnclickListenear itemChildOnClickListenear) {
        this.itemChildOnClickListenear = itemChildOnClickListenear;
    }

    public LiveHouseAdapter(int layoutResId, List<NetAppointmentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final NetAppointmentBean netAppointmentBean) {

        if (netAppointmentBean.dev != null) {
            if (netAppointmentBean.dev.effectId != null && netAppointmentBean.dev.effectId.size() != 0) {
                CacheManager.initImageClientList(mContext, (ImageView) holder.getView(R.id.iv_house), netAppointmentBean.dev.effectId.get(0).thumbnailImage);
            }
            holder.setText(R.id.tv_house_name, netAppointmentBean.dev.devName);
            holder.setText(R.id.tv_price, netAppointmentBean.dev.averPrice + "元/㎡");
            holder.setText(R.id.tv_area, netAppointmentBean.dev.devSquareMetre + "㎡");
            holder.setText(R.id.tv_room, netAppointmentBean.dev.devBedroom);
            holder.setText(R.id.tv_address, netAppointmentBean.dev.addressDistrict + "-" + netAppointmentBean.dev.addressTown + "   " + netAppointmentBean.dev.propertyType);
            if (!TextUtils.isEmpty(netAppointmentBean.look.arriveTime)) {
                holder.setText(R.id.tv_name_value, CustomerUtils.dateTransSdfMinutes(Long.parseLong(netAppointmentBean.look.arriveTime)));
            }
            holder.setText(R.id.tv_look_local_value, netAppointmentBean.dev.saleAddressDistrict + netAppointmentBean.dev.saleAddressTown + netAppointmentBean.dev.saleAddressDetail);
        }


        if (netAppointmentBean.look != null) {
            switch (netAppointmentBean.look.status) {

                case 3:
                    holder.setText(R.id.tv_book, "到访");
                    holder.setBackgroundRes(R.id.tv_book, R.drawable.comment_flow_text_bg);
                    holder.setTextColor(R.id.tv_book, mContext.getResources().getColor(R.color.gradient_end));
                    holder.getView(R.id.rl_export).setVisibility(View.VISIBLE);
                    holder.getView(R.id.rl_no).setVisibility(View.GONE);
                    holder.getView(R.id.iv_time).setVisibility(View.GONE);
                    if (netAppointmentBean.broker != null) {
                        CacheManager.initImageUserHeader(mContext, (ImageView) holder.getView(R.id.team_head), netAppointmentBean.broker.headImg);
                    } else {
                        CacheManager.initImageUserHeader(mContext, (ImageView) holder.getView(R.id.team_head), null);
                    }
                    holder.setText(R.id.team_name, netAppointmentBean.broker.name);
                    holder.setText(R.id.team_good_value, TextUtils.isEmpty(netAppointmentBean.broker.favorableRate) ? "0" : netAppointmentBean.broker.favorableRate + "%");
                    holder.setText(R.id.team_look_value, netAppointmentBean.broker.count + "次");
                    holder.getView(R.id.tv_book).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemChildOnClickListenear.onToVisit();
                        }
                    });
                    holder.getView(R.id.team_phone).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (netAppointmentBean.broker != null)
                                itemChildOnClickListenear.onClickPhone(netAppointmentBean.broker.phone);
                        }
                    });
                    break;
                default:
                    break;
            }
        }


    }
}
